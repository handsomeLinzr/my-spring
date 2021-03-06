package my.spring.framework.demo;

import my.spring.framework.annotation.AZAutowire;
import my.spring.framework.annotation.AZController;

/**
 * Description:
 *
 * @author Linzr
 * @version V1.0.0
 * @date 2021/3/4 5:00 下午
 * @since V1.0.0
 */
@AZController
public class MyDemo {

    @AZAutowire
    private MyService myService;

}
