package com.junyi.permission.task;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.junyi.permission.annotation.NoAuthorized;
import com.junyi.permission.annotation.Public;
import com.junyi.permission.entity.ButtonInterface;
import com.junyi.permission.entity.Interface;
import com.junyi.permission.mapper.ButtonInterfaceMapper;
import com.junyi.permission.mapper.InterfaceMapper;
import com.junyi.permission.service.ButtonService;
import com.junyi.permission.service.InterfaceService;
import com.junyi.permission.util.CommonUtils;

import io.swagger.annotations.ApiOperation;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author erma66.feng
 * @email erma66@sina.cn
 * @date 2020/3/18 0018
 * @description xxxx
 */
@Component
@Order(1)
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class InterfaceScanner implements CommandLineRunner {
    private static final String MATCH_URL_PARAM = "(\\w|-)+";
    private static final String[] SWAGGER_PATHS = {"/swagger-resources", "/api-docs"};

    private final @NonNull RequestMappingHandlerMapping handlerMapping;
    private final @NonNull InterfaceService interfaceService;
    private final @NonNull ButtonService buttonService;
    private final @NonNull InterfaceMapper interfaceMapper;
    private final @NonNull ButtonInterfaceMapper buttonInterfaceMapper;

    @Override
    public void run(String... args) {
        Map<String, Interface> mapDb = getIntergfaceFromDb();
        Map<String, Interface> mapCode = getInterfaceFromCode();

        List<Interface> diffList = diff(mapCode, mapDb);
        interfaceService.saveOrUpdateBatch(diffList);
        List<String> deletedIds = getDeleted(mapCode, mapDb);
        if (deletedIds.size() > 0) {
            interfaceMapper.deleteBatchIds(deletedIds);
            buttonInterfaceMapper.delete(
                    Wrappers.<ButtonInterface>lambdaQuery()
                            .in(ButtonInterface::getInterfaceId, deletedIds));
        }
        interfaceService.loadUserInterface();
        interfaceService.loadPublicInterface();
        interfaceService.loadNoAuthorisedInterface();
    }

    private List<String> getDeleted(Map<String, Interface> mapCode, Map<String, Interface> mapDb) {
        List<String> ids = new ArrayList<>();
        for (Map.Entry<String, Interface> entry : mapDb.entrySet()) {
            if (!mapCode.containsKey(entry.getKey())) {
                ids.add(entry.getValue().getGuid());
            }
        }
        return ids;
    }

    /**
     * 如果代码中的接口在数据库中已经存在，并且内容相同，则不处理 如果代码中的接口在数据库中已经存在，但内容不同，则更新数据库中的 如果代码中的接口在数据库中不存在，则插入数据库
     *
     * @param mapCode 代码中的接口
     * @param mapDb 数据库中的接口
     * @return
     */
    private List<Interface> diff(Map<String, Interface> mapCode, Map<String, Interface> mapDb) {
        List<Interface> interfaceList = new ArrayList<>();
        for (Map.Entry<String, Interface> entry : mapCode.entrySet()) {
            String key = entry.getKey();
            Interface interCode = entry.getValue();
            if (mapDb.containsKey(key)) {
                Interface interDb = mapDb.get(key);
                if (!interCode.isSimilar(interDb)) {
                    interDb.setName(interCode.getName());
                    interDb.setTag(interCode.getTag());
                    interDb.setNotes(interCode.getNotes());
                    interDb.setAuthorized(interCode.isAuthorized());
                    interDb.setPublicInterface(interCode.isPublicInterface());
                    interfaceList.add(interDb);
                }
            } else {
                interCode.setGuid(CommonUtils.getUUID());
                interfaceList.add(interCode);
            }
        }
        return interfaceList;
    }

    private Map<String, Interface> getIntergfaceFromDb() {
        List<Interface> list = interfaceService.list();
        Map<String, Interface> map = new HashMap<>(list.size());

        for (Interface inter : list) {
            map.put(inter.getUrl() + inter.getMethodType(), inter);
            log.debug(
                    "interface:{},{},{},{}",
                    inter.getName(),
                    inter.getTag(),
                    inter.getUrl(),
                    inter.getMethodType());
        }

        return map;
    }

    private Map<String, Interface> getInterfaceFromCode() {
        Map<String, Interface> map = new HashMap<>();

        Map<RequestMappingInfo, HandlerMethod> map1 = handlerMapping.getHandlerMethods();
        for (Map.Entry<RequestMappingInfo, HandlerMethod> entry : map1.entrySet()) {
            RequestMappingInfo info = entry.getKey();
            String url = info.getPatternsCondition().toString();
            if (StringUtils.isEmpty(url)) {
                continue;
            }
            if (isSwaggerUrl(url)) {
                continue;
            }
            HandlerMethod method = entry.getValue();
            Interface inter = new Interface();
            inter.setUrl(processUrl(url));
            if (info.getMethodsCondition().isEmpty()) {
                continue;
            } else {
                inter.setMethodType(replaceBrackets(info.getMethodsCondition().toString()));
            }
            ApiOperation annotation = method.getMethodAnnotation(ApiOperation.class);
            if (annotation == null) {
                inter.setName(method.getMethod().getName());
                inter.setNotes(method.getMethod().getName());
                inter.setTag(method.getBeanType().getSimpleName());
            } else {
                inter.setName(annotation.value());
                inter.setTag(annotation.tags()[0]);
                inter.setNotes(annotation.notes());
            }
            inter.setPublicInterface(method.hasMethodAnnotation(Public.class));
            inter.setAuthorized(
                    !inter.isPublicInterface() && !method.hasMethodAnnotation(NoAuthorized.class));
            map.put(inter.getUrl() + inter.getMethodType(), inter);
        }

        return map;
    }

    private boolean isSwaggerUrl(String url) {
        for (String swaggerUrl : SWAGGER_PATHS) {
            if (url.contains(swaggerUrl)) {
                return true;
            }
        }
        return false;
    }

    /** 处理url */
    public String processUrl(String url) {
        url = replaceBrackets(url);

        if (isNeedMatch(url)) {
            return toRegular(url);
        }

        return url;
    }

    /** 去除中括号 */
    private String replaceBrackets(String str) {
        if (str.startsWith("[") && str.endsWith("]")) {
            return str.substring(1, str.length() - 1);
        }
        return str;
    }

    /** 转为正则表达式 */
    private String toRegular(String url) {
        int start = url.indexOf("{");
        int end = url.indexOf("}");

        url = url.substring(0, start) + MATCH_URL_PARAM + url.substring(end + 1);

        if (isNeedMatch(url)) {
            return toRegular(url);
        } else {
            url = "^" + url;
            url = url.replace("/", "\\/");
            return url;
        }
    }

    private boolean isNeedMatch(String str) {
        return str.contains("{") && str.contains("}");
    }
}
