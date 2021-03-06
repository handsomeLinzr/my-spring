package my.spring.framework.core;

/**
 * Description:工厂顶级接口
 *
 * @author Linzr
 * @version V1.0.0
 * @date 2021/3/3 1:04 下午
 * @since V1.0.0
 */
public interface AZBeanFactory {
    /**
     * 获取bean
     * @param beanName bean名称
     * @return
     */
    Object getBean(String beanName) throws Exception;

    Object getBean(Class<?> clazz) throws Exception;

}
