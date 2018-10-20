package top.thevsk.config;

import com.jfinal.config.*;
import com.jfinal.kit.LogKit;
import com.jfinal.kit.PathKit;
import com.jfinal.kit.PropKit;
import com.jfinal.template.Engine;
import top.thevsk.controller.ArkOrderController;
import top.thevsk.controller.MainController;
import top.thevsk.interceptors.AuthInterceptor;
import top.thevsk.interceptors.PostInterceptor;

public class HttpConfig extends JFinalConfig {

    public void configConstant(Constants constants) {
        PathKit.setWebRootPath("/asd");
    }

    public void configRoute(Routes routes) {
        routes.add("/", MainController.class);
        routes.add("/arkOrder", ArkOrderController.class);
    }

    public void configEngine(Engine engine) {
    }

    public void configPlugin(Plugins plugins) {
    }

    public void configInterceptor(Interceptors interceptors) {
        interceptors.add(new PostInterceptor());
        interceptors.add(new AuthInterceptor());
    }

    public void configHandler(Handlers handlers) {
    }

    public void afterJFinalStart() {
        LogKit.info("[服务] 启动完成");
        LogKit.info("[服务] 端口 " + PropKit.getInt("server.port"));
    }
}