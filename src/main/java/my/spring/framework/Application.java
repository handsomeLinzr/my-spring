package my.spring.framework;

import my.spring.framework.context.AZApplicationContext;

/**
 * Description:
 *
 * @author Linzr
 * @version V1.0.0
 * @date 2021/3/3 2:46 下午
 * @since V1.0.0
 */
public class Application {
    public static void main(String[] args) {
        AZApplicationContext azApplicationContext = new AZApplicationContext("classpath:application.properties");
        try {
            Object myDemo = azApplicationContext.getBean("myDemo");
            System.out.println(myDemo);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
