package com.core.filter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.core.constant.ExceptionEnum;
import com.core.exception.ServiceException;
import com.core.util.ResultUtil;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter
public class ControllerFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        FilterHttpServletRequestWrapper requestWrapper = new FilterHttpServletRequestWrapper(httpServletRequest);
        //POST请求，并且不是上传文件的请求 走下面的逻辑
        if ("POST".equalsIgnoreCase(httpServletRequest.getMethod()) && !
                requestWrapper.getHeader("Content-Type").contains("multipart/form-data")) {
            JSONObject requestBody = requestWrapper.getRequestBody();
            try {
                if (requestBody != null) {
                    JSONObject modifyRequestBody = requestBody.getJSONObject("data");
                    if (modifyRequestBody != null)
                        requestWrapper.setRequestBody(modifyRequestBody);
                    filterChain.doFilter(requestWrapper, httpServletResponse);
                } else {
                    filterChain.doFilter(httpServletRequest, httpServletResponse);
                }
            } catch (Exception e) {
                if (e instanceof ServiceException) {
                    httpServletResponse.setContentType("application/json");
                    httpServletResponse.setCharacterEncoding("UTF-8");
                    String write = JSON.toJSONString(ResultUtil.error(ExceptionEnum.INVALID_PARAMETER));
                    httpServletResponse.getWriter().write(write);
                    return;
                }
                throw e;
            }
        } else {
            filterChain.doFilter(requestWrapper, httpServletResponse);
        }

    }
}
