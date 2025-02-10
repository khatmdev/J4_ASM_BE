package org.example.java4_asm_backend.utils;

import jakarta.servlet.http.HttpServletRequest;

public class RequestHolder {
    private static final ThreadLocal<HttpServletRequest> requestThreadLocal = new ThreadLocal<>();

    public static void setRequest(HttpServletRequest request) {
        requestThreadLocal.set(request);
    }

    public static HttpServletRequest getRequest() {
        return requestThreadLocal.get();
    }

    public static void clear() {
        requestThreadLocal.remove();
    }
}
