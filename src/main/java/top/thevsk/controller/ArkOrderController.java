package top.thevsk.controller;

import top.thevsk.entity.Msg;
import top.thevsk.exception.VskException;
import top.thevsk.service.ArkOrderService;
import top.thevsk.utils.ServicesKit;

public class ArkOrderController extends BaseController {

    private ArkOrderService arkOrderService = ServicesKit.get(ArkOrderService.class);

    public void index() {
        renderJson(new Msg().setCode(200));
    }

    public void search() {
        String name = getPara("name");
        try {
            renderJson(new Msg().setCode(200).setData(arkOrderService.search(name)));
        } catch (VskException ve) {
            renderJson(new Msg().setCode(500).setData(ve.getMessage()));
        } catch (Exception e) {
            renderJson(new Msg().setCode(501).setData(e.getMessage()));
        }
    }
}
