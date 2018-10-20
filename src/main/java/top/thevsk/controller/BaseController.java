package top.thevsk.controller;

import com.alibaba.fastjson.JSON;
import com.jfinal.core.Controller;
import com.jfinal.kit.StrKit;
import top.thevsk.entity.Msg;

public class BaseController extends Controller {

    public boolean isPost() {
        return getRequest().getMethod().toUpperCase().equals("POST");
    }

    public String getAuthorization() {
        String authorization = getHeader("Authorization");
        return StrKit.isBlank(authorization) ? "" : authorization;
    }

    public void renderJson(Msg msg) {
        renderJson(JSON.toJSONString(msg));
    }
}
