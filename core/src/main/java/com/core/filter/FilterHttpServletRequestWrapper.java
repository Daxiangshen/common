package com.core.filter;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.core.exception.ServiceException;
import org.springframework.util.StreamUtils;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class FilterHttpServletRequestWrapper extends HttpServletRequestWrapper {
    private byte[] requestBody;

    // 传入是JSON格式 转换成JSONObject
    JSONObject getRequestBody() {
        return JSON.parseObject((new String(requestBody, StandardCharsets.UTF_8)));
    }

    void setRequestBody(JSONObject jsonObject) {
        this.requestBody = jsonObject.toJSONString().getBytes(StandardCharsets.UTF_8);
    }

    FilterHttpServletRequestWrapper(HttpServletRequest request) {

        super(request);

        try {
            requestBody = StreamUtils.copyToByteArray(request.getInputStream());
        } catch (IOException e) {
            throw new ServiceException("FilterHttpServletRequestWrapper error," + e);
        }
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        if (requestBody == null) {
            requestBody = new byte[0];
        }
        final ByteArrayInputStream bais = new ByteArrayInputStream(requestBody);
        return new ServletInputStream() {
            @Override
            public int read() throws IOException {
                return bais.read();
            }

            @Override
            public boolean isFinished() {
                return false;
            }

            @Override
            public boolean isReady() {
                return true;
            }

            @Override
            public void setReadListener(ReadListener listener) {

            }
        };
    }

    @Override
    public BufferedReader getReader() throws IOException {
        return new BufferedReader(new InputStreamReader(getInputStream()));
    }
}
