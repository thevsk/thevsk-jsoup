package top.thevsk.utils;

import com.jfinal.aop.Enhancer;
import top.thevsk.service.BaseService;

import java.util.HashMap;
import java.util.Map;

public class ServicesKit {

    private static Map<String, Object> map;

    static {
        map = new HashMap<>();
    }

    public static <T extends BaseService> T get(Class<T> tClass) {
        Object service = map.get(tClass.getName());
        if (service == null) {
            service = Enhancer.enhance(tClass);
            ((BaseService) service).init();
            map.put(tClass.getName(), service);
        }
        return (T) service;
    }
}
