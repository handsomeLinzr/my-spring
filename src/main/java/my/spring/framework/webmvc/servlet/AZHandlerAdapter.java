package my.spring.framework.webmvc.servlet;

import my.spring.framework.annotation.AZRequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * 处理适配器
 */
public class AZHandlerAdapter {

    public boolean supports(Object handler) {
        return handler instanceof AZHandlerMapping;
    }

    // 真正调用方法
    public AZModelAndView handler(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        AZHandlerMapping handlerMapping = (AZHandlerMapping) handler;
        Method method = handlerMapping.getMethod();
        Annotation[][] parameterAnnotations = method.getParameterAnnotations();
        Class<?>[] parameterTypes = method.getParameterTypes();

        Map<String, Integer> parameterIndex = new HashMap<>(parameterAnnotations.length + 2);

        // 第 x 个参数
        for (int i = 0; i < parameterAnnotations.length; i++) {
            // 遍历这个参数的所有注解
            for (Annotation annotation : parameterAnnotations[i]) {
                if (!(annotation instanceof AZRequestParam)) { continue;}
                String paramName = ((AZRequestParam) annotation).value();
                parameterIndex.put(paramName, i);
            }
        }

        // 将 request 和 response 参数位置也存到 parameterIndex 中
        for (int i = 0; i < parameterTypes.length; i++) {
            Class<?> type = parameterTypes[i];
            if (HttpServletRequest.class == type || HttpServletResponse.class == type) {
                parameterIndex.put(type.getName(), i);
            }
        }

        // 设置实参
        Object[] paramValues = new Object[parameterTypes.length];
        Map<String, String[]> parameterMap = request.getParameterMap();
        for (Map.Entry<String, String[]> entry : parameterMap.entrySet()) {
            if (!parameterIndex.containsKey(entry.getKey())) {continue;}
            // 数组换成字符串
            String value = Arrays.toString(entry.getValue()).replaceAll("\\[|\\]", "").replaceAll("\\s", ",");
            // 第 x 个参数
            Integer index = parameterIndex.get(entry.getKey());
            paramValues[index] = caseStringValue(value, parameterTypes[index]);
        }

        // 设置 request 和 response
        if (parameterIndex.containsKey(HttpServletRequest.class.getName())) {
            Integer index = parameterIndex.get(HttpServletRequest.class.getName());
            paramValues[index] = request;
        }
        if (parameterIndex.containsKey(HttpServletResponse.class.getName())) {
            Integer index = parameterIndex.get(HttpServletResponse.class.getName());
            paramValues[index] = response;
        }

        // 进行反射调用方法
        Object result = method.invoke(handlerMapping.getController(), paramValues);
        Class<?> returnType = method.getReturnType();

        if (result == null || returnType == Void.TYPE) { return null; }
        if (returnType == AZModelAndView.class) {
            return (AZModelAndView) result;
        }
        return null;
    }

    private Object caseStringValue(String value, Class<?> type) {
        if (type == String.class) {
            return value;
        }
        if (type == Integer.class) {
            return Integer.valueOf(value);
        }
        if (type == Double.class) {
            return Double.valueOf(value);
        } else {
            return value;
        }
    }

}
