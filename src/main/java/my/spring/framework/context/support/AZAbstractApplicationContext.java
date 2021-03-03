package my.spring.framework.context.support;

/**
 * Description: IOC容器顶层实现
 *
 * @author Linzr
 * @version V1.0.0
 * @date 2021/3/3 1:08 下午
 * @since V1.0.0
 */
public abstract class AZAbstractApplicationContext {
    /**
     * 刷新容器方法，子类实现
     */
    public void refresh() throws Exception{}
}
