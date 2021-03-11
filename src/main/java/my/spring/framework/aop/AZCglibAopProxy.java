package my.spring.framework.aop;

import my.spring.framework.aop.support.AZAdvisedSupport;

/**
 * Description:
 *
 * @author Linzr
 * @version V1.0.0
 * @date 2021/3/9 9:15 上午
 * @since V1.0.0
 */
public class AZCglibAopProxy implements AZAopProxy{
    public AZCglibAopProxy(AZAdvisedSupport config) {
    }

    @Override
    public Object getProxy() {
        return null;
    }

    @Override
    public Object getProxy(ClassLoader classLoader) {
        return null;
    }
}
