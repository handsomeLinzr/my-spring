package my.spring.framework.aop.aspect;

import java.lang.reflect.Method;

public abstract class AZAbstractAspectAdvice {
    // 切面方法
    private Method aspectMethod;

    // 切面对象
    private Object aspectObject;
    public AZAbstractAspectAdvice(Method aspectMethod, Object aspectObject) {
        this.aspectMethod = aspectMethod;
        this.aspectObject = aspectObject;
    }

    /**
     * 根据传过来的参数执行切面方法
     * @param joinPoint
     * @param returnValue
     * @param ex
     * @return
     */
    public Object invokeAdviceMethod(AZJoinPoint joinPoint, Object returnValue, Throwable ex) throws Throwable {
        Class<?>[] parameterTypes = aspectMethod.getParameterTypes();
        if (null == parameterTypes || parameterTypes.length == 0) {
            // 没有参数，直接反射
            return aspectMethod.invoke(aspectObject);
        }
        // 传递进来的参数
        Object [] params = new Object[parameterTypes.length];
        for (int i = 0; i < parameterTypes.length; i++) {
            if (parameterTypes[i] == AZJoinPoint.class) {
                params[i] = joinPoint;
            } else if (parameterTypes[i] == Object.class) {
                params[i] = returnValue;
            } else if (parameterTypes[i] == Throwable.class) {
                params[i] = ex;
            }
        }
        return aspectMethod.invoke(aspectObject, params);
    }

}
