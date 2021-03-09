package my.spring.framework.aop.support;

import java.lang.reflect.Method;
import java.util.List;

/**
 * Description:
 *
 * @author Linzr
 * @version V1.0.0
 * @date 2021/3/9 12:46 下午
 * @since V1.0.0
 */
public class AZAdvisedSupport {

    private Class<?> targetClass;

    public Class<?> getTargetClass() {
        return targetClass;
    }

    public void setTargetClass(Class<?> targetClass) {
        this.targetClass = targetClass;
    }

    public List<Object> getInterceptorsAndDynamicInterceptionAdvice(Method method, Class<?> targetClass) {
        return null;
    }
}
