package my.spring.framework.aop.intercept;

import my.spring.framework.aop.aspect.AZJoinPoint;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Description:
 *
 * @author Linzr
 * @version V1.0.0
 * @date 2021/3/9 12:58 下午
 * @since V1.0.0
 */
public class AZMethodInvocation implements AZJoinPoint {

    private Object proxy;
    private Object target;
    private Class<?> targetClass;
    private Method method;
    private Object [] arguments;
    private List<Object> interceptorsAndDynamicMethodMatchers;
    private Map<String, Object> userAttributes;

    //定义一个索引，从-1开始来记录当前拦截器执行的位置
    private int currentInterceptorIndex = -1;

    public AZMethodInvocation(Object proxy, Object target, Method method, Object[] arguments,
                              Class<?> targetClass, List<Object> interceptorsAndDynamicMethodMatchers) {

        this.proxy = proxy;
        this.target = target;
        this.targetClass = targetClass;
        this.method = method;
        this.arguments = arguments;
        this.interceptorsAndDynamicMethodMatchers = interceptorsAndDynamicMethodMatchers;

        userAttributes = new HashMap<>();

    }

    public Object proceed() throws Throwable {
        // 执行链执行完了，则执行 pointcut 方法
        if (currentInterceptorIndex == this.interceptorsAndDynamicMethodMatchers.size()-1) {
            return this.method.invoke(this.target, this.arguments);
        }

        // 获得当前执行拦截器
        Object interceptorOrInterceptionAdvice = interceptorsAndDynamicMethodMatchers.get(++currentInterceptorIndex);
        if (interceptorOrInterceptionAdvice instanceof AZMethodInterceptor) {
            AZMethodInterceptor mi = (AZMethodInterceptor) interceptorOrInterceptionAdvice;
            return mi.invoke(this);
        } else {
            // 匹配不上，继续执行
            return proceed();
        }
    }

    @Override
    public Object getThis() {
        return this.target;
    }

    @Override
    public Object[] getArguments() {
        return this.arguments;
    }

    @Override
    public Method getMethod() {
        return this.method;
    }

    @Override
    public void setUserAttribute(String key, Object value) {
        this.userAttributes.put(key, value);
    }

    @Override
    public Object getUserAttribute(String key) {
        return this.userAttributes.get(key);
    }
}
