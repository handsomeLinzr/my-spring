package my.spring.framework.beans.support;

import my.spring.framework.beans.config.AZBeanDefinition;
import my.spring.framework.context.support.AZAbstractApplicationContext;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Description:
 *
 * @author Linzr
 * @version V1.0.0
 * @date 2021/3/3 1:09 下午
 * @since V1.0.0
 */
public class AZDefaultListableBeanFactory extends AZAbstractApplicationContext {

    // 存放beanDefinition的容器
    protected Map<String, AZBeanDefinition> beanDefinitionMap = new ConcurrentHashMap<>();

}
