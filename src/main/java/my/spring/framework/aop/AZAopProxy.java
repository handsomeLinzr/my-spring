package my.spring.framework.aop;

/**
 * Description: 代理的类
 *
 * @author Linzr
 * @version V1.0.0
 * @date 2021/3/9 9:12 上午
 * @since V1.0.0
 */
public interface AZAopProxy {

    Object getProxy();


    Object getProxy(ClassLoader classLoader);


}
