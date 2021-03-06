package my.spring.framework.webmvc.servlet;

import lombok.Data;

import java.lang.reflect.Method;
import java.util.regex.Pattern;

@Data
public class AZHandlerMapping {

    private Pattern pattern;
    private Object controller;
    private Method method;

    public AZHandlerMapping(Pattern pattern, Object controller, Method method) {
        this.pattern = pattern;
        this.controller = controller;
        this.method = method;
    }
}
