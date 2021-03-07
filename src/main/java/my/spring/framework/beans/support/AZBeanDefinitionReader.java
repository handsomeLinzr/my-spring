package my.spring.framework.beans.support;

import my.spring.framework.beans.config.AZBeanDefinition;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Description:
 *
 * @author Linzr
 * @version V1.0.0
 * @date 2021/3/3 1:12 下午
 * @since V1.0.0
 */
public class AZBeanDefinitionReader {

    private Properties config = new Properties();
    private static final String SCAN_PATH = "scanPackage";
    private List<String> registerBeanClasses = new ArrayList<>();

    public AZBeanDefinitionReader(String... locations) {
        // 获得配置文件的输入流
        InputStream in = this.getClass().getClassLoader().getResourceAsStream(locations[0].replace("classpath:", ""));
        // 加载配置
        try {
            config.load(in);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        // 扫描配置
        doScanner(this.config.getProperty(SCAN_PATH));
    }

    // 扫描配置，并把所有的class名加到容器中
    // scanPath: xx.xx.xx
    private void doScanner(String scanPath) {
        URL url = this.getClass().getClassLoader().getResource(scanPath.replaceAll("\\.", "/"));
        File classPath = new File(url.getFile());
        for (File file : classPath.listFiles()) {
            if (file.isDirectory()) {
                doScanner(scanPath + "." + file.getName());
            } else {
                if (!file.getName().endsWith(".class")) {
                    continue;
                }
                String beanClassName = scanPath + "." + file.getName().replace(".class", "");
                registerBeanClasses.add(beanClassName);
            }
        }
    }

    /**
     * 加载beanDefinition
     * @return
     */
    public List<AZBeanDefinition> loadBeanDefinitions() {
        try {
            List<AZBeanDefinition> result = new ArrayList<>();
            if (registerBeanClasses.isEmpty()) {
                return result;
            }
            for (String beanClassName : registerBeanClasses) {
                Class<?> clazz = Class.forName(beanClassName);
                // 接口不能实例化
                if (clazz.isInterface()) { continue;}
                result.add(doCreateBeanDefinition(toLowerFirstCase(clazz.getSimpleName()), clazz.getName()));

                // 设置类上的所有接口
                for (Class<?> i : clazz.getInterfaces()) {
                    result.add(doCreateBeanDefinition(i.getName(), clazz.getName()));
                }
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // 创建beanDefinition
    private AZBeanDefinition doCreateBeanDefinition(String factoryBeanName, String beanClassName) {
        AZBeanDefinition beanDefinition = new AZBeanDefinition();
        beanDefinition.setBeanClassName(beanClassName);
        beanDefinition.setFactoryBeanName(factoryBeanName);
        return beanDefinition;
    }

    // 首字母小写
    private String toLowerFirstCase(String name) {
        char[] chars = name.toCharArray();
        chars[0] += 32;
        return String.valueOf(chars);
    }

    public Properties getConfig() {
        return this.config;
    }

}
