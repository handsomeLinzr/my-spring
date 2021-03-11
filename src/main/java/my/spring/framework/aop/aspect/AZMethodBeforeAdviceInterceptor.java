package my.spring.framework.aop.aspect;

import my.spring.framework.aop.intercept.AZMethodInterceptor;
import my.spring.framework.aop.intercept.AZMethodInvocation;

import java.lang.reflect.Method;

/**
 * 前置通知拦截器
 */
public class AZMethodBeforeAdviceInterceptor extends AZAbstractAspectAdvice implements AZMethodInterceptor {

    private AZJoinPoint joinPoint;

    public AZMethodBeforeAdviceInterceptor(Method aspectMethod, Object aspectObject) {
        super(aspectMethod, aspectObject);
    }

    private void before(Method method,Object[] args,Object target) throws Throwable {
        super.invokeAdviceMethod(joinPoint, null, null);
    }

    @Override
    public Object invoke(AZMethodInvocation mi) throws Throwable {
        this.joinPoint = mi;
        before(mi.getMethod(), mi.getArguments(), mi.getThis());
        return mi.proceed();
    }
}
