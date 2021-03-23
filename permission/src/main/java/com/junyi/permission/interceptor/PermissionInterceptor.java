package com.junyi.permission.interceptor;

import com.junyi.permission.service.InterfaceService;
import com.junyi.permission.service.TokenService;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PermissionInterceptor implements HandlerInterceptor {

    private final @NonNull InterfaceService interfaceService;
    private final @NonNull TokenService tokenService;

    @Override
    public void afterCompletion(
            HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, Exception arg3) {
        // TODO Auto-generated method stub
    }

    @Override
    public void postHandle(
            HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, ModelAndView arg3) {
        // TODO Auto-generated method stub
    }

    @Override
    public boolean preHandle(
            HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        if (interfaceService.matchPublicInterface(request.getRequestURI())) {
            return true;
        }
        //        String token = request.getHeader(TokenService.ACCESS_TOKEN);
        //
        //        boolean verify =
        //                tokenService.validate(token, request.getRequestURI() +
        //                        request.getMethod());
        //        if (!verify) {
        //            response.setStatus(200);
        //            response.getOutputStream()
        //                    .write(
        //                            JSONObject.toJSONString(
        //                                    ResultUtils.fail(ResultCode.UNAUTHORIZED,
        //                                            "没有访问权限"))
        //                                    .getBytes(StandardCharsets.UTF_8));
        //            return false;
        //        }
        //
        //        verify =
        //                interfaceService.verifyPermission(
        //                        token, request.getRequestURI() + request.getMethod());
        //        if (!verify) {
        //            response.setStatus(200);
        //            response.getOutputStream()
        //                    .write(
        //                            JSONObject.toJSONString(ResultUtils.fail(ResultCode.FAIL,
        //                                    "没有此功能的权限"))
        //                                    .getBytes(StandardCharsets.UTF_8));
        //            return false;
        //        }

        return true;
    }
}
