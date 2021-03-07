package my.spring.framework.demo.service.impl;

import my.spring.framework.annotation.AZService;
import my.spring.framework.demo.service.IQueryService;

@AZService
public class QueryServiceImpl implements IQueryService {
    @Override
    public String query(String name) {
        return "query:" + name + ",SUCCESS";
    }
}
