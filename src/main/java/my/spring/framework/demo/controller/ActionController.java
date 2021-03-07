package my.spring.framework.demo.controller;

import my.spring.framework.annotation.AZAutowire;
import my.spring.framework.annotation.AZController;
import my.spring.framework.annotation.AZRequestMapping;
import my.spring.framework.annotation.AZRequestParam;
import my.spring.framework.demo.service.IQueryService;
import my.spring.framework.webmvc.servlet.AZModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@AZController
@AZRequestMapping("/web")
public class ActionController {

    @AZAutowire
    private IQueryService queryService;

    @AZRequestMapping("/query")
    public AZModelAndView query(HttpServletRequest request, HttpServletResponse response, @AZRequestParam("name") String name) {
        String result = queryService.query(name);
        AZModelAndView modelAndView ;
        try {
            modelAndView = out(response, result);
        } catch (Exception e) {
            Map<String, Object> paramsValue = new HashMap<>(2);
            paramsValue.put("detail", e.getMessage());
            paramsValue.put("stackTrace", Arrays.toString(e.getStackTrace()));
            return new AZModelAndView("500", paramsValue);
        }
        return modelAndView;
    }

    private AZModelAndView out(HttpServletResponse response, String result) throws Exception {
//        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(result);
        return null;
    }

}