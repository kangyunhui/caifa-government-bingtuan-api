package com.junyi.permission.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.junyi.permission.constant.CommonConstant;
import com.junyi.permission.entity.ButtonInterface;
import com.junyi.permission.entity.Interface;
import com.junyi.permission.mapper.ButtonInterfaceMapper;
import com.junyi.permission.mapper.InterfaceMapper;
import com.junyi.permission.model.Node;
import com.junyi.permission.model.UserView;
import com.junyi.permission.service.InterfaceService;
import com.junyi.permission.service.TokenService;
import com.junyi.permission.service.UserService;
import com.junyi.permission.util.JWTUtils;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class InterfaceServiceImpl extends ServiceImpl<InterfaceMapper, Interface>
        implements InterfaceService {

    private final @NonNull InterfaceMapper interfaceMapper;
    private final @NonNull ButtonInterfaceMapper buttonInterfaceMapper;
    private final @NonNull UserService userService;

    @Resource private HttpServletRequest request;

    private static Map<String, Pattern> patternMap;
    private static Map<String, Set<String>> userInterfaceMap;
    private static Set<String> noAuthorisedUrl;
    private static Set<String> publicUrl;

    @Override
    public List<Node> getGroupInterface(String buttonId) {
        List<Interface> interfaceList;
        String currentUser = UserService.getUserId(request);
        // 不是管理员的话只能看到自己拥有权限的接口

        UserView userView = userService.getById(currentUser);
        if (!CommonConstant.ADMIN.equals(userView.getName())) {
            interfaceList = interfaceMapper.selectByUserId(currentUser);
        } else {
            interfaceList =
                    interfaceMapper.selectList(
                            Wrappers.<Interface>lambdaQuery().eq(Interface::isAuthorized, true));
        }

        List<ButtonInterface> buttonInterfaceList =
                buttonInterfaceMapper.selectList(
                        Wrappers.<ButtonInterface>lambdaQuery()
                                .eq(ButtonInterface::getButtonId, buttonId));

        Set<String> checkedSet =
                buttonInterfaceList.stream()
                        .map(ButtonInterface::getInterfaceId)
                        .collect(Collectors.toSet());

        Map<String, List<Interface>> interGroup =
                interfaceList.stream().collect(Collectors.groupingBy(Interface::getTag));

        List<Node> nodeList = new ArrayList<>(interGroup.size());
        int index = 1;
        for (Map.Entry<String, List<Interface>> entry : interGroup.entrySet()) {
            String name = entry.getKey();
            List<Interface> interList = entry.getValue();
            Node node = new Node(index + "", name);

            List<Node> children = new ArrayList<>(interList.size());
            interList.forEach(
                    inter -> {
                        Node child =
                                new Node(
                                        inter.getGuid() + "",
                                        inter.getName(),
                                        checkedSet.contains(inter.getGuid()));
                        children.add(child);
                    });
            node.setChildren(children);
            nodeList.add(node);
            index++;
        }

        return nodeList;
    }

    @Override
    public void loadUserInterface() {
        if (userInterfaceMap == null) {
            userInterfaceMap = new HashMap<>();
        }
        if (patternMap == null) {
            patternMap = new HashMap<>();
        }
        userInterfaceMap.clear();
        List<Map<String, String>> allList = interfaceMapper.findAllUserInterface();
        reloadUserInterface(allList);
    }

    @Override
    public void loadUserInterface(String userId) {
        userInterfaceMap.remove(userId);
        List<Map<String, String>> allList = interfaceMapper.findAllUserInterfaceByUser(userId);
        reloadUserInterface(allList);
    }

    private void reloadUserInterface(List<Map<String, String>> allList) {
        allList.forEach(
                map -> {
                    String userId = map.get("user_id");
                    String url = map.get("url");
                    Set<String> set =
                            userInterfaceMap.computeIfAbsent(userId, k -> new HashSet<>());
                    set.add(url);
                });
    }

    @Override
    public synchronized void loadNoAuthorisedInterface() {
        if (!CollectionUtils.isEmpty(noAuthorisedUrl)) {
            return;
        }
        List<Interface> interfaceList =
                interfaceMapper.selectList(
                        Wrappers.<Interface>lambdaQuery().eq(Interface::isAuthorized, false));
        noAuthorisedUrl = new HashSet<>(interfaceList.size());
        noAuthorisedUrl.addAll(
                interfaceList.stream()
                        .map(i -> i.getUrl() + i.getMethodType())
                        .collect(Collectors.toSet()));
    }

    private Set<String> getPublicUrl() {
        List<Interface> interfaceList =
                interfaceMapper.selectList(
                        Wrappers.<Interface>lambdaQuery().eq(Interface::isPublicInterface, true));
        return interfaceList.stream()
                .map(i -> i.getUrl() + i.getMethodType())
                .collect(Collectors.toSet());
    }

    /**
     * 验证接口权限 验证条件：包含该接口，后者符合接口的正则
     *
     * @param token token
     * @param requestUrl 请求url，包含接口路径和访问类型
     * @return 验证是否通过
     */
    @Override
    public boolean verifyPermission(String token, String requestUrl) {
        if (noAuthorisedUrl == null) {
            loadNoAuthorisedInterface();
        }

        if (matchInterface(requestUrl, noAuthorisedUrl)) {
            return true;
        }

        if (token == null) {
            return false;
        }

        String userId = JWTUtils.getValue(token, TokenService.USERID);
        UserView userView = userService.getById(userId);
        if (userView == null) {
            return false;
        }
        if (CommonConstant.ADMIN.equals(userView.getName())) {
            return true;
        }

        Set<String> urlSet = userInterfaceMap.get(userId);
        if (urlSet == null) {
            return false;
        }
        return matchInterface(requestUrl, urlSet);
    }

    private boolean matchInterface(String requestUrl, Set<String> urlSet) {
        if (urlSet.contains(requestUrl)) {
            return true;
        }

        for (String url : urlSet) {
            Pattern pattern = patternMap.computeIfAbsent(url, Pattern::compile);
            if (pattern.matcher(requestUrl).matches()) {
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean matchPublicInterface(String url) {
        if (publicUrl == null) {
            loadPublicInterface();
        }
        return matchInterface(url, publicUrl);
    }

    @Override
    public synchronized void loadPublicInterface() {
        if (!CollectionUtils.isEmpty(publicUrl)) {
            return;
        }

        publicUrl = getPublicUrl();
    }

    @Override
    public List<Interface> get() {
        return interfaceMapper.selectList(
                Wrappers.<Interface>lambdaQuery().eq(Interface::isAuthorized, true));
    }

    @Override
    public List<String> getCheckedInterface(String buttonId) {
        return buttonInterfaceMapper
                .selectList(
                        Wrappers.<ButtonInterface>lambdaQuery()
                                .eq(ButtonInterface::getButtonId, buttonId))
                .stream()
                .map(ButtonInterface::getInterfaceId)
                .collect(Collectors.toList());
    }
}
