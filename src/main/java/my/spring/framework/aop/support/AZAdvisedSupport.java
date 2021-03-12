package my.spring.framework.aop.support;

import my.spring.framework.aop.aspect.AZAfterThrowingAdviceInterceptor;
import my.spring.framework.aop.aspect.AZMethodAfterReturningAdviceInterceptor;
import my.spring.framework.aop.aspect.AZMethodBeforeAdviceInterceptor;
import my.spring.framework.aop.config.AZAopConfig;
import my.spring.framework.beans.config.AZBeanDefinition;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Description:
 *
 * @author Linzr
 * @version V1.0.0
 * @date 2021/3/9 12:46 下午
 * @since V1.0.0
 */
public class AZAdvisedSupport {

    // 目标类
    private Class<?> targetClass;

    // 目标对象
    private Object target;

    private AZAopConfig config;

    private Pattern pointCutClassPattern;

    private Map<Method, List<Object>> methodCache;

    public AZAdvisedSupport(AZAopConfig config) {
        this.config = config;
    }

    public Object getTarget() {
        return target;
    }

    public void setTarget(Object target) {
        this.target = target;
    }


    public Class<?> getTargetClass() {
        return targetClass;
    }

    public void setTargetClass(Class<?> targetClass) {
        this.targetClass = targetClass;
        // 解析
        parse();
    }

    private void parse() {
        // public .* my.spring.framework.demo.service..*Service*.*(.*)
        String pointCut = config.getPointCut()
                .replaceAll("\\.", "\\\\.")
                .replaceAll("\\\\.\\*", ".*")
                .replaceAll("\\(", "\\\\(")
                .replaceAll("\\)", "\\\\)");
        String pointCutForClassRegex = pointCut.substring(0, pointCut.lastIndexOf("\\("));
        pointCutClassPattern = Pattern.compile("class " + pointCutForClassRegex.substring(pointCutForClassRegex.lastIndexOf(" ")+1));

        try {
            methodCache = new HashMap<>();

            // 获得切面方法
            Class<?> aspectClass = Class.forName(this.config.getAspectClass());
            Map<String, Method> aspectMethods = new HashMap<>();
            for (Method method : aspectClass.getMethods()) {
                aspectMethods.put(method.getName(), method);
            }

            // 方法的正则表达式
            Pattern compile = Pattern.compile(pointCut);
            for (Method method : this.targetClass.getMethods()) {
                String methodString = method.toString();
                // 去除抛异常
                if (methodString.contains("throws")) {
                    methodString = methodString.substring(0, methodString.lastIndexOf("throws")).trim();
                }
                // 匹配上，则添加切面方法
                if (compile.matcher(methodString).matches()) {
                    // 所谓的执行器链
                    List<Object> advices = new LinkedList<>();
                    // 前置通知
                    if (null != this.config.getAspectBefore()) {
                        advices.add(new AZMethodBeforeAdviceInterceptor(aspectMethods.get(this.config.getAspectBefore()), aspectClass.newInstance()));
                    }
                    // 后置通知
                    if (null != this.config.getAspectAfter()) {
                        advices.add(new AZMethodAfterReturningAdviceInterceptor(aspectMethods.get(this.config.getAspectAfter()), aspectClass.newInstance()));
                    }
                    // 返回异常通知
                    if (null != this.config.getAspectAfterThrow()) {
                        AZAfterThrowingAdviceInterceptor afterThrowingAdvice =
                                new AZAfterThrowingAdviceInterceptor(aspectMethods.get(this.config.getAspectAfterThrow()), aspectClass.newInstance());
                        afterThrowingAdvice.setThrowingName(this.config.getAspectAfterThrowingName());
                        advices.add(afterThrowingAdvice);
                    }
                    // 将执行链缓存到map中
                    methodCache.put(method, advices);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public List<Object> getInterceptorsAndDynamicInterceptionAdvice(Method method, Class<?> targetClass) throws Exception {
        List<Object> cache = this.methodCache.get(method);
        if (cache == null) {
            Method m = targetClass.getMethod(method.getName(), method.getReturnType());

            cache = methodCache.get(m);

            methodCache.put(method, cache);
        }
        return cache;
    }

    // 当前targetClass是否匹配得上该切面
    public boolean pointCutMatch() {
        return this.pointCutClassPattern.matcher(this.targetClass.toString()).matches();
    }
}
