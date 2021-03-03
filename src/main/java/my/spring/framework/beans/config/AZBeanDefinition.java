package my.spring.framework.beans.config;

import lombok.Data;

/**
 * Description:
 *
 * @author Linzr
 * @version V1.0.0
 * @date 2021/3/3 1:13 下午
 * @since V1.0.0
 */
@Data
public class AZBeanDefinition {

    // bean的class名
    private String beanClassName;

    // 是否延时加载
    private boolean lazyInit = false;

    // bean名
    private String factoryBeanName;

}
