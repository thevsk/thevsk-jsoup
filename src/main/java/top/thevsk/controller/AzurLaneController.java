package top.thevsk.controller;

import top.thevsk.entity.Msg;
import top.thevsk.exception.VskException;
import top.thevsk.service.ArkOrderService;
import top.thevsk.service.AzurLaneService;
import top.thevsk.utils.ServicesKit;

public class AzurLaneController extends BaseController {

    private AzurLaneService azurLaneService = ServicesKit.get(AzurLaneService.class);

    public void index() {
        renderJson(new Msg().setCode(200));
    }

    public void search() {
        String name = getPara("name");
        try {
            renderJson(new Msg().setCode(200).setData(azurLaneService.search(name)));
        } catch (VskException ve) {
            renderJson(new Msg().setCode(500).setData(ve.getMessage()));
        } catch (Exception e) {
            renderJson(new Msg().setCode(501).setData(e.getMessage()));
        }
    }
}
