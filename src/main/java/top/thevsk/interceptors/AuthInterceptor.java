package top.thevsk.interceptors;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import top.thevsk.controller.BaseController;
import top.thevsk.entity.Constants;

public class AuthInterceptor implements Interceptor {

    @Override
    public void intercept(Invocation invocation) {
        if (invocation.getTarget() instanceof BaseController) {
            BaseController controller = invocation.getTarget();
            String auth = controller.getAuthorization();
            if (!auth.startsWith(Constants.authorization)) {
                controller.redirect("/");
                return;
            }
        }
        invocation.invoke();
    }
}
