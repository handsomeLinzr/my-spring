package my.spring.framework.beans.config;

/**
 * Description:
 *
 * @author Linzr
 * @version V1.0.0
 * @date 2021/3/5 1:20 下午
 * @since V1.0.0
 */
public class AZBeanPostProcessor {


    public Object postProcessBeforeInitialization(Object bean, String beanName) throws Exception {
        return bean;
    }

    public Object postProcessAfterInitialization(Object bean, String beanName) throws Exception {
        return bean;
    }

}
