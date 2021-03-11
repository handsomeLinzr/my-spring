package my.spring.framework.aop.intercept;

/**
 * Description:
 *
 * @author Linzr
 * @version V1.0.0
 * @date 2021/3/9 1:06 下午
 * @since V1.0.0
 */
public interface AZMethodInterceptor {
    Object invoke(AZMethodInvocation invocation) throws Throwable;
}
