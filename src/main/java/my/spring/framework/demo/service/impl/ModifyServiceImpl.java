package my.spring.framework.demo.service.impl;

import my.spring.framework.annotation.AZService;
import my.spring.framework.demo.service.IModifyService;

import java.util.HashMap;
import java.util.Map;

/**
 * Description:
 *
 * @author Linzr
 * @version V1.0.0
 * @date 2021/3/8 12:37 下午
 * @since V1.0.0
 */
@AZService
public class ModifyServiceImpl implements IModifyService {
    @Override
    public Map<String, Object> makeData(String teacher, String data) {
        Map<String, Object> map = new HashMap<>(2);
        map.put("teacher", "handsome " + teacher);
        map.put("data", "my  "+ data);
        return map;
    }
}
