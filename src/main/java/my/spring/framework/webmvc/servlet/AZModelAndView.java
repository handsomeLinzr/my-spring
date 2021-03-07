package my.spring.framework.webmvc.servlet;

import java.util.Map;

public class AZModelAndView {
    private final String viewName;
    private final Map<String,?> model;

    public AZModelAndView(String viewName, Map<String, ?> model) {
        this.viewName = viewName;
        this.model = model;
    }

    public String getViewName() {
        return viewName;
    }

    public Map<String, ?> getModel() {
        return model;
    }
}
