package my.spring.framework.context;

import my.spring.framework.beans.AZBeanWrapper;
import my.spring.framework.beans.config.AZBeanDefinition;
import my.spring.framework.beans.support.AZBeanDefinitionReader;
import my.spring.framework.beans.support.AZDefaultListableBeanFactory;
import my.spring.framework.core.AZBeanFactory;

import java.util.List;
import java.util.Map;

/**
 * Description: 具体实现
 * 定位   加载    注册     初始化单例
 *
 * @author Linzr
 * @version V1.0.0
 * @date 2021/3/3 1:06 下午
 * @since V1.0.0
 */
public class AZApplicationContext extends AZDefaultListableBeanFactory implements AZBeanFactory {

    private String[] locations;

    public AZApplicationContext(String... locations) {
        this.locations = locations;
        try {
            refresh();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void refresh() throws Exception {
        // 定位
        AZBeanDefinitionReader reader = new AZBeanDefinitionReader(this.locations);

        // 加载
        List<AZBeanDefinition> beanDefinitions = reader.loadBeanDefinitions();

        // 注册
        doRegisterBeanDefinition(beanDefinitions);

        // 初始化单例
        doAutowired();
    }

    // 注册————》填充beanDefinition到map
    private void doRegisterBeanDefinition(List<AZBeanDefinition> beanDefinitions) throws Exception {
        if (beanDefinitions.isEmpty()) { return;}
        for (AZBeanDefinition beanDefinition : beanDefinitions) {
            if (super.beanDefinitionMap.containsKey(beanDefinition.getFactoryBeanName())) {
                throw new Exception("The BeanDefinition " + beanDefinition + "is alread in map!");
            }
            super.beanDefinitionMap.put(beanDefinition.getFactoryBeanName(), beanDefinition);
        }
    }

    // 初始化单例————》调用getBean
    private void doAutowired() {
        for (Map.Entry<String, AZBeanDefinition> beanDefinitionEntry : super.beanDefinitionMap.entrySet()) {
            if (!beanDefinitionEntry.getValue().isLazyInit()) {
                try {
                    getBean(beanDefinitionEntry.getKey());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public Object getBean(String beanName) throws Exception{

        AZBeanDefinition beanDefinition = super.beanDefinitionMap.get(beanName);

        Object instance = null;
        instance = instantiateBean(beanName,beanDefinition);

        populateBean(beanName, beanDefinition, new AZBeanWrapper(instance));

        return null;
    }

    // DI依赖注入
    private void populateBean(String beanName, AZBeanDefinition beanDefinition, AZBeanWrapper azBeanWrapper) {

    }

    // 实例化
    private Object instantiateBean(String beanName, AZBeanDefinition beanDefinition) {
        return null;
    }
}
