package com.sts.ims.intercepter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sts.ims.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class RequestInterceptor implements HandlerInterceptor {


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object object) throws Exception {
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        TenantContext.clear();
    }

    private String restResponseBytes(Response response) throws IOException {
        return new ObjectMapper().writeValueAsString(response);
    }

}
