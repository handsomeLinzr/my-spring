package my.spring.framework.aop.aspect;

import my.spring.framework.aop.intercept.AZMethodInterceptor;
import my.spring.framework.aop.intercept.AZMethodInvocation;

import java.lang.reflect.Method;

public class AZMethodAfterReturningAdviceInterceptor extends AZAbstractAspectAdvice implements AZMethodInterceptor {

    private AZJoinPoint joinPoint;

    public AZMethodAfterReturningAdviceInterceptor(Method aspectMethod, Object aspectObject) {
        super(aspectMethod, aspectObject);
    }

    private void after(Object retVal, Method method, Object[] arguments, Object aThis) throws Throwable {
        super.invokeAdviceMethod(joinPoint, retVal, null);
    }

    @Override
    public Object invoke(AZMethodInvocation mi) throws Throwable {
        Object returnValue = mi.proceed();
        this.joinPoint = mi;
        after(returnValue, mi.getMethod(), mi.getArguments(), mi.getThis());
        return returnValue;
    }
}
