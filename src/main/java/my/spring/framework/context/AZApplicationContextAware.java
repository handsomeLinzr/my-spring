package my.spring.framework.context;

/**
 * Description:通过解耦方式获得IOC容器的顶层设计，后面将通过监听器去扫描所有的类，
 * 只要实现了这个接口，后边将通过setApplicationContext() 方法将IOC容器注入到目标
 * 类中
 *
 * @author Linzr
 * @version V1.0.0
 * @date 2021/3/6 11:23 上午
 * @since V1.0.0
 */
public interface AZApplicationContextAware {
    void setApplicationContext(AZApplicationContext applicationContext);
}
