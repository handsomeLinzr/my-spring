package my.spring.framework.aop.aspect;

import java.lang.reflect.Method;

public interface AZJoinPoint {

    Object getThis();

    Object[] getArguments();

    Method getMethod();

    void setUserAttribute(String key, Object value);

    Object getUserAttribute(String key);

}
