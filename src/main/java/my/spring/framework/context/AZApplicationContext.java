package my.spring.framework.context;

import my.spring.framework.annotation.AZAutowire;
import my.spring.framework.annotation.AZController;
import my.spring.framework.annotation.AZService;
import my.spring.framework.aop.AZAopProxy;
import my.spring.framework.aop.AZCglibAopProxy;
import my.spring.framework.aop.AZJdkDynamicAopProxy;
import my.spring.framework.aop.config.AZAopConfig;
import my.spring.framework.aop.support.AZAdvisedSupport;
import my.spring.framework.beans.AZBeanWrapper;
import my.spring.framework.beans.config.AZBeanDefinition;
import my.spring.framework.beans.config.AZBeanPostProcessor;
import my.spring.framework.beans.support.AZBeanDefinitionReader;
import my.spring.framework.beans.support.AZDefaultListableBeanFactory;
import my.spring.framework.core.AZBeanFactory;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Properties;
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

    //  定义aop的配置名
    private static final String POINT_CUT = "pointCut";
    private static final String ASPECT_CLASS = "aspectClass";
    private static final String ASPECT_BEFORE = "aspectBefore";
    private static final String ASPECT_AFTER = "aspectAfter";
    private static final String ASPECT_AFTER_THROW = "aspectAfterThrow";
    private static final String ASPECT_AFTER_THROWING_NAME = "aspectAfterThrowingName";

    AZBeanDefinitionReader reader = null;

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
        reader = new AZBeanDefinitionReader(this.locations);

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
                String beanName = beanDefinitionEntry.getKey();
                try {
                    getBean(beanName);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // 获取beanDefinition，拿到beanClass，反射实例化，包装成beanWrapper，实例返回
    @Override
    public Object getBean(String beanName) throws Exception{

            if (this.factoryBeanInstanceCache.containsKey(beanName)) {
                return this.factoryBeanInstanceCache.get(beanName).getWrappedInstance();
            }

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
            if (this.factoryBeanInstanceCache.get(autowiredName) == null) {
                getBean(autowiredName);
            }
            file.set(instance, this.factoryBeanInstanceCache.get(autowiredName).getWrappedInstance());
        }
    }

    // 实例化
    private Object instantiateBean(String beanName, AZBeanDefinition beanDefinition) throws Exception {

        Object instance = this.singletonObjects.get(beanName);
        if (instance == null) {
            String beanClassName = beanDefinition.getBeanClassName();
            Class<?> clazz = Class.forName(beanClassName);
            instance = clazz.newInstance();

            // 根读取配置信息，封装成advisedSupport
            AZAdvisedSupport config = instantionAopConfig(beanDefinition);
            config.setTargetClass(clazz);
            config.setTarget(instance);
            // 匹配上则创建代理，并替换当前instance
            if (config.pointCutMatch()) {
                // 根据config自动选择策略获得代理方式
                instance = createProxy(config).getProxy();
            }

            // 放入缓存
            this.singletonObjects.put(beanDefinition.getFactoryBeanName(), instance);
            this.singletonObjects.put(beanClassName, instance);
        }
        return instance;
    }

    private AZAopProxy createProxy(AZAdvisedSupport config) {
        Class<?> clazz = config.getTarget().getClass();
        if (clazz.getInterfaces().length > 0 ) {
            // 有接口
            return new AZJdkDynamicAopProxy(config);
        }
        return new AZCglibAopProxy(config);
    }

    // 配置advisedSupport
    private AZAdvisedSupport instantionAopConfig(AZBeanDefinition beanDefinition) {
        AZAopConfig config = new AZAopConfig();
        config.setPointCut(this.reader.getConfig().getProperty(POINT_CUT));
        config.setAspectClass(this.reader.getConfig().getProperty(ASPECT_CLASS));
        config.setAspectBefore(this.reader.getConfig().getProperty(ASPECT_BEFORE));
        config.setAspectAfter(this.reader.getConfig().getProperty(ASPECT_AFTER));
        config.setAspectAfterThrow(this.reader.getConfig().getProperty(ASPECT_AFTER_THROW));
        config.setAspectAfterThrowingName(this.reader.getConfig().getProperty(ASPECT_AFTER_THROWING_NAME));
        return new AZAdvisedSupport(config);
    }

    public String[] getBeanDefinitionNames() {
        return super.beanDefinitionMap.keySet().toArray(new String[super.beanDefinitionMap.size()]);
    }

    public Properties getConfig() {
        return this.reader.getConfig();
    }

}
