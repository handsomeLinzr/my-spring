package my.spring.framework.webmvc.servlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 处理适配器
 */
public class AZHandlerAdapter {

    public boolean supports(Object handler) {
        return handler instanceof AZHandlerMapping;
    }

    // 真正调用方法
    public ModelAndView handler(HttpServletRequest request, HttpServletResponse response, Object handler) {
        return null;
    }

}
