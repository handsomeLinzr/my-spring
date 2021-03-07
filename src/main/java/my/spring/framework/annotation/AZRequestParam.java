package my.spring.framework.annotation;

import java.lang.annotation.*;

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AZRequestParam {
    // 参数名称
    String value() default "";

    // 必填
    boolean required() default true;
}
