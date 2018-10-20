package top.thevsk.interceptors;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import top.thevsk.controller.BaseController;

public class PostInterceptor implements Interceptor {

    @Override
    public void intercept(Invocation invocation) {
        if (invocation.getTarget() instanceof BaseController) {
            BaseController controller = invocation.getTarget();
            if (!controller.isPost()) {
                controller.redirect("/");
                return;
            }
        }
        invocation.invoke();
    }
}
