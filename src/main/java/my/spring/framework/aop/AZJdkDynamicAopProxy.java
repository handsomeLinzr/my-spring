package my.spring.framework.aop;

import my.spring.framework.aop.intercept.AZMethodInvocation;
import my.spring.framework.aop.support.AZAdvisedSupport;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.List;

/**
 * Description: JDK代理类
 *
 * @author Linzr
 * @version V1.0.0
 * @date 2021/3/9 9:18 上午
 * @since V1.0.0
 */
public class AZJdkDynamicAopProxy implements AZAopProxy, InvocationHandler {

    private final AZAdvisedSupport advised;

    public AZJdkDynamicAopProxy(AZAdvisedSupport config) {
        this.advised = config;
    }

    @Override
    public Object getProxy() {
        return getProxy(this.advised.getTargetClass().getClassLoader());
    }

    @Override
    public Object getProxy(ClassLoader classLoader) {
        return Proxy.newProxyInstance(classLoader, this.advised.getTargetClass().getInterfaces(), this);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        List<Object> chain = this.advised.getInterceptorsAndDynamicInterceptionAdvice(method, this.advised.getTargetClass());
        AZMethodInvocation invocation = new AZMethodInvocation(proxy, this.advised.getTarget(), method, args, this.advised.getTargetClass(), chain);
        return invocation.proceed();
    }
}
