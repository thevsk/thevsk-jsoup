package top.thevsk.controller;

import com.jfinal.aop.Clear;

@Clear
public class MainController extends BaseController {

    public void index() {
        renderHtml("it's work");
    }
}
