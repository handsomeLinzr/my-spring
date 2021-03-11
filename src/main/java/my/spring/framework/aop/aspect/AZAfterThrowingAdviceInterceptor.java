package my.spring.framework.aop.aspect;

import my.spring.framework.aop.intercept.AZMethodInterceptor;
import my.spring.framework.aop.intercept.AZMethodInvocation;

import java.lang.reflect.Method;

public class AZAfterThrowingAdviceInterceptor extends AZAbstractAspectAdvice implements AZMethodInterceptor {

    private String throwingName;

    public AZAfterThrowingAdviceInterceptor(Method aspectMethod, Object aspectObject) {
        super(aspectMethod, aspectObject);
    }

    @Override
    public Object invoke(AZMethodInvocation mi) throws Throwable {
        try {
            return mi.proceed();
        } catch (Throwable ex) {
            super.invokeAdviceMethod(mi, null, ex);
            throw ex;
        }
    }

    public String getThrowingName() {
        return throwingName;
    }

    public void setThrowingName(String throwingName) {
        this.throwingName = throwingName;
    }
}
