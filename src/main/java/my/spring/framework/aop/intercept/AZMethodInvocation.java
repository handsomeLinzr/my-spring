package my.spring.framework.aop.intercept;

import java.lang.reflect.Method;
import java.util.List;

/**
 * Description:
 *
 * @author Linzr
 * @version V1.0.0
 * @date 2021/3/9 12:58 下午
 * @since V1.0.0
 */
public class AZMethodInvocation {

    public AZMethodInvocation(Object proxy, Object target, Method method, Object[] arguments,
                              Class<?> targetClass, List<Object> interceptorsAndDynamicMethodMatchers) {
    }

    public Object proceed() {
        return null;
    }

}
