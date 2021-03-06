package my.spring.framework.annotation;

import java.lang.annotation.*;

/**
 * Description:
 *
 * @author Linzr
 * @version V1.0.0
 * @date 2021/3/5 12:26 下午
 * @since V1.0.0
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AZController {
    String value() default "";
}
