package my.spring.framework.webmvc.servlet;

import lombok.extern.slf4j.Slf4j;
import my.spring.framework.annotation.AZController;
import my.spring.framework.annotation.AZRequestMapping;
import my.spring.framework.context.AZApplicationContext;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class AZDispatcherServlet extends HttpServlet {

    private AZApplicationContext applicationContext;

    // 初始化参数，从web.xml拿
    private static final String CONTEXT_CONFIG_LOCATION = "contextConfigLocation";

    private static final String TEMPLATE_ROOT = "templateRoot";

    private final List<AZHandlerMapping> handlerMappings = new ArrayList<>();

    private final Map<AZHandlerMapping, AZHandlerAdapter> handlerAdapters = new ConcurrentHashMap<>();

    // 初始化方法
    @Override
    public void init(ServletConfig config) throws ServletException {
        // 初始化容器
        applicationContext = new AZApplicationContext(config.getInitParameter(CONTEXT_CONFIG_LOCATION));

        // 注册九大组件
        try {
            initStrategies(applicationContext);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initStrategies(AZApplicationContext context) throws Exception {
        //多文件上传的组件
        initMultipartResolver(context);
        //初始化本地语言环境
        initLocaleResolver(context);
        //初始化模板处理器
        initThemeResolver(context);


        //handlerMapping，必须实现
        initHandlerMappings(context);
        //初始化参数适配器，必须实现
        initHandlerAdapters(context);
        //初始化异常拦截器
        initHandlerExceptionResolvers(context);
        //初始化视图预处理器
        initRequestToViewNameTranslator(context);


        //初始化视图转换器，必须实现
        initViewResolvers(context);
        //参数缓存器
        initFlashMapManager(context);
    }

    private void initFlashMapManager(AZApplicationContext context) {

    }

    private void initViewResolvers(AZApplicationContext context) {
    }

    private void initRequestToViewNameTranslator(AZApplicationContext context) {

    }

    private void initHandlerExceptionResolvers(AZApplicationContext context) {

    }

    private void initHandlerAdapters(AZApplicationContext context) {
        for (AZHandlerMapping handlerMapping : handlerMappings) {
            handlerAdapters.put(handlerMapping, new AZHandlerAdapter());
        }
    }

    private void initHandlerMappings(AZApplicationContext context) throws Exception {
        String[] beanDefinitionNames = context.getBeanDefinitionNames();
        for (String beanDefinitionName : beanDefinitionNames) {
            // 获取是bean
            Object controller = context.getBean(beanDefinitionName);
            Class<?> clazz = controller.getClass();
            // 没有controller注解直接跳过
            if (!clazz.isAnnotationPresent(AZController.class)) { continue; }

            String basePath = "";
            if (clazz.isAnnotationPresent(AZRequestMapping.class)) {
                AZRequestMapping mapping = clazz.getAnnotation(AZRequestMapping.class);
                basePath = mapping.value();
            }

            Method[] methods = clazz.getDeclaredMethods();
            for (Method method : methods) {
                // 没有requestMapping 注解则跳过
                if (!method.isAnnotationPresent(AZRequestMapping.class)) {continue;}
                AZRequestMapping methodMapping = method.getAnnotation(AZRequestMapping.class);

                String regex = "/" + basePath + "/" + methodMapping.value().replaceAll("\\*", ".*").replaceAll("/+", "/");
                Pattern pattern = Pattern.compile(regex);
                this.handlerMappings.add(new AZHandlerMapping(pattern, controller, method));
                log.info("Mapped " + regex + "," + method);
            }

        }
    }

    private void initLocaleResolver(AZApplicationContext context) {

    }

    private void initThemeResolver(AZApplicationContext context) {

    }

    private void initMultipartResolver(AZApplicationContext context) {
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            this.doDispatch(req, resp);
        } catch (Exception e) {
            resp.getWriter().write("500");
            e.printStackTrace();
        }
    }

    protected void doDispatch(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        // 1.通过请求拿到url,获得handlerMapping
        AZHandlerMapping handler = getHandler(req);
        if (handler == null) {
            resp.getWriter().write("404");
        }

        // 2.获得handlerAdapter，准备调用前的数据
        AZHandlerAdapter ha = getHandlerAdapter(handler);
        if (ha == null) {
            resp.getWriter().write("500");
        }

        // 3.真正调用方法，返回ModelAndView，存储了页面数据和页面模板名称
        ModelAndView mv = ha.handler(req, resp, handler);

        // 4.输出渲染
        processDispatchResult(req, resp, mv);
    }

    private void processDispatchResult(HttpServletRequest req, HttpServletResponse resp, ModelAndView mv) {

    }

    private AZHandlerAdapter getHandlerAdapter(AZHandlerMapping handler) {
        if (this.handlerAdapters.isEmpty()) { return null; }
        AZHandlerAdapter ha = this.handlerAdapters.get(handler);
        if (ha.supports(handler)) {
            return ha;
        }
        return null;
    }

    private AZHandlerMapping getHandler(HttpServletRequest req) {
        if (handlerMappings.isEmpty()) {
            return null;
        }
        String url = req.getRequestURI();
        String contextPath = req.getContextPath();
        // 请求路径
        url = url.replace(contextPath, "").replaceAll("/+", "/");

        for (AZHandlerMapping handlerMapping : handlerMappings) {

            // 和请求路径匹配
            Matcher matcher = handlerMapping.getPattern().matcher(url);
            if (!matcher.matches()) {continue;}
            return handlerMapping;
        }
        return null;
    }

}
