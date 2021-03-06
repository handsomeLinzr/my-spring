package my.spring.framework.context;

import com.sun.xml.internal.messaging.saaj.packaging.mime.util.BEncoderStream;
import my.spring.framework.annotation.AZAutowire;
import my.spring.framework.annotation.AZController;
import my.spring.framework.annotation.AZService;
import my.spring.framework.beans.AZBeanWrapper;
import my.spring.framework.beans.config.AZBeanDefinition;
import my.spring.framework.beans.config.AZBeanPostProcessor;
import my.spring.framework.beans.support.AZBeanDefinitionReader;
import my.spring.framework.beans.support.AZDefaultListableBeanFactory;
import my.spring.framework.core.AZBeanFactory;
import org.omg.PortableInterceptor.INACTIVE;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

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

    // 配置文件
    private String[] locations;

    // 单例bean缓存容器（一级缓存）
    private final Map<String, Object> singletonObjects = new ConcurrentHashMap<>(256);

    // IOC容器
    private final Map<String, AZBeanWrapper> factoryBeanInstanceCache = new ConcurrentHashMap<>(256);

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

    // 获取beanDefinition，拿到beanClass，反射实例化，包装成beanWrapper，实例返回
    @Override
    public Object getBean(String beanName) throws Exception{

            AZBeanDefinition beanDefinition = super.beanDefinitionMap.get(beanName);

            Object instance = null;
            // 前置处理
            AZBeanPostProcessor beanPostProcessor = new AZBeanPostProcessor();
            beanPostProcessor.postProcessBeforeInitialization(instance, beanName);

            instance = instantiateBean(beanName, beanDefinition);

            // 后置处理
            AZBeanWrapper beanWrapper = new AZBeanWrapper(instance);

            this.factoryBeanInstanceCache.put(beanName, beanWrapper);
            this.factoryBeanInstanceCache.put(beanWrapper.getWrappedClass().getName(), beanWrapper);

            beanPostProcessor.postProcessAfterInitialization(instance, beanName);

            // 注入
            populateBean(beanName, beanDefinition, beanWrapper);

            return this.factoryBeanInstanceCache.get(beanName).getWrappedInstance();

    }

    @Override
    public Object getBean(Class<?> clazz) throws Exception {
        return getBean(clazz.getName());
    }

    // DI依赖注入
    private void populateBean(String beanName, AZBeanDefinition beanDefinition, AZBeanWrapper azBeanWrapper) throws Exception {

        Object instance = azBeanWrapper.getWrappedInstance();
        Class<?> beanClass = azBeanWrapper.getWrappedClass();
        // 判断注解
        if (!(beanClass.isAnnotationPresent(AZController.class) || beanClass.isAnnotationPresent(AZService.class))) { return; }

        Field[] files = beanClass.getDeclaredFields();
        for (Field file : files) {
            if (! file.isAnnotationPresent(AZAutowire.class)) { continue; }
            AZAutowire annotation = file.getAnnotation(AZAutowire.class);
            // 名称
            String autowiredName = annotation.value().trim();
            if ("".equals(autowiredName)) {
                // 类型
                autowiredName = file.getType().getName();
            }

            file.setAccessible(true);
            file.set(instance, this.factoryBeanInstanceCache.get(autowiredName).getWrappedInstance());
        }
    }

    // 实例化
    private Object instantiateBean(String beanName, AZBeanDefinition beanDefinition) throws Exception {

        Object singleObject = this.singletonObjects.get(beanName);
        if (singleObject == null) {
            String beanClassName = beanDefinition.getBeanClassName();
            Class<?> clazz = Class.forName(beanClassName);
            singleObject = clazz.newInstance();

            // 放入缓存
            this.singletonObjects.put(beanName, singleObject);
            this.singletonObjects.put(beanClassName, singleObject);
        }
        return singleObject;
    }
}
