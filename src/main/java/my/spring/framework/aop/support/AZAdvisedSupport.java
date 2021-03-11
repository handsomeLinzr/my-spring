package my.spring.framework.aop.support;

import my.spring.framework.aop.config.AZAopConfig;
import my.spring.framework.beans.config.AZBeanDefinition;

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

    // 目标类
    private Class<?> targetClass;

    // 目标对象
    private Object target;

    private AZAopConfig config;

    public AZAdvisedSupport(AZAopConfig config) {
        this.config = config;
    }

    public Object getTarget() {
        return target;
    }

    public void setTarget(Object target) {
        this.target = target;
    }


    public Class<?> getTargetClass() {
        return targetClass;
    }

    public void setTargetClass(Class<?> targetClass) {
        this.targetClass = targetClass;

    }

    public List<Object> getInterceptorsAndDynamicInterceptionAdvice(Method method, Class<?> targetClass) {
        return null;
    }

    // 当前targetClass是否匹配得上该切面
    public boolean pointCutMatch() {
        return true;
    }
}
