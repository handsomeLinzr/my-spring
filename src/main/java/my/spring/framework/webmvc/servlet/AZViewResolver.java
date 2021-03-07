package my.spring.framework.webmvc.servlet;

import java.io.File;
import java.util.Locale;

public class AZViewResolver {

    private final String DEFAULT_TEMPLATE_SUFFX = ".html";
    File rootDir;

    public AZViewResolver(String templateRootPath) {
        String rootPath = this.getClass().getClassLoader().getResource(templateRootPath).getFile();
        rootDir = new File(rootPath);
    }

    // 处理并返回对应的View
    public AZView resolveViewName(String viewName, Locale locale) throws Exception {
        // 没有指定页面
        if (viewName == null || "".equals(viewName.trim())) {
            return null;
        }
        viewName = viewName.endsWith(DEFAULT_TEMPLATE_SUFFX)? viewName : viewName + DEFAULT_TEMPLATE_SUFFX;
        File viewFile = new File((rootDir.getPath() + "/" + viewName).replaceAll("//+", "/"));
        return new AZView(viewFile);
    }

}
