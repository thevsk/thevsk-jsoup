package top.thevsk.service;

import com.jfinal.kit.StrKit;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import top.thevsk.entity.Constants;
import top.thevsk.exception.VskException;
import top.thevsk.utils.CQUtils;

import java.io.IOException;

/**
 * 碧蓝航线wiki查询
 */
public class AzurLaneService extends BaseService {

    private static final String wikiUrl = "http://wiki.joyme.com/blhx/";
    private StringBuilder templateOfShip;
    private StringBuilder templateOfShip1;
    private StringBuilder templateOfShip2;
    private StringBuilder templateOfShip3;
    private StringBuilder templateOfPlace;
    private StringBuilder templateOfPlace1;

    private String copyright() {
        return "内容来源：碧蓝海事局(碧蓝航线WIKI) *注:仅转载，不作为商业用途";
    }

    @Override
    public void init() {
        templateOfShip = new StringBuilder();
        templateOfShip1 = new StringBuilder();
        templateOfShip2 = new StringBuilder();
        templateOfShip3 = new StringBuilder();
        templateOfPlace = new StringBuilder();
        templateOfPlace1 = new StringBuilder();
        initShip();
        initPlace();
    }

    private void initShip() {
        templateOfShip.append("舰娘信息:");
        templateOfShip.append(Constants.newLine);
        templateOfShip.append("{立绘}");
        templateOfShip.append(Constants.newLine);
        templateOfShip.append("{名称}");
        templateOfShip.append(Constants.newLine);
        templateOfShip.append("编号:{编号}");
        templateOfShip.append(Constants.newLine);
        templateOfShip.append("初始星级:{初始星级}");
        templateOfShip.append(Constants.newLine);
        templateOfShip.append("类型:{类型}");
        templateOfShip.append(Constants.newLine);
        templateOfShip.append("稀有度:{稀有度}");
        templateOfShip.append(Constants.newLine);
        templateOfShip.append("阵营:{阵营}");
        templateOfShip.append(Constants.newLine);
        templateOfShip.append("耗时:{耗时}");
        templateOfShip.append(Constants.newLine);
        templateOfShip.append("营养价值:{营养价值}");
        templateOfShip.append(Constants.newLine);
        templateOfShip.append("退役收益:{退役收益}");
        templateOfShip.append(Constants.nextMsg);
        templateOfShip.append("普通掉落点:{普通掉落点}");
        templateOfShip.append(Constants.newLine);
        templateOfShip.append("活动掉落点:{活动掉落点}");
        templateOfShip.append(Constants.nextMsg);
        templateOfShip.append("性能:");
        templateOfShip.append(Constants.newLine);
        templateOfShip.append("{性能}");
        templateOfShip.append(Constants.nextMsg);
        templateOfShip.append("突破升星效果:");
        templateOfShip.append(Constants.newLine);
        templateOfShip.append("{突破升星效果}");
        templateOfShip.append(Constants.nextMsg);
        templateOfShip.append("技能:");
        templateOfShip.append(Constants.newLine);
        templateOfShip.append("{技能}");
        templateOfShip.append(Constants.newLine);
        templateOfShip.append("{copyright}");
        templateOfShip1.append("耐久:{耐久eng}({耐久content})");
        templateOfShip1.append(Constants.newLine);
        templateOfShip1.append("防空:{防空eng}({防空content})");
        templateOfShip1.append(Constants.newLine);
        templateOfShip1.append("机动:{机动eng}({机动content})");
        templateOfShip1.append(Constants.newLine);
        templateOfShip1.append("航空:{航空eng}({航空content})");
        templateOfShip1.append(Constants.newLine);
        templateOfShip1.append("雷击:{雷击eng}({雷击content})");
        templateOfShip1.append(Constants.newLine);
        templateOfShip1.append("炮击:{炮击eng}({炮击content})");
        templateOfShip1.append(Constants.newLine);
        templateOfShip1.append("装填:({装填content})");
        templateOfShip1.append(Constants.newLine);
        templateOfShip1.append("消耗:({消耗content})");
        templateOfShip1.append(Constants.newLine);
        templateOfShip1.append("反潜:({反潜content})");
        templateOfShip1.append(Constants.newLine);
        templateOfShip1.append("航速:({航速content})");
        templateOfShip1.append(Constants.newLine);
        templateOfShip1.append("装甲:{装甲content}");
        templateOfShip2.append("{阶段}:{阶段内容}");
        templateOfShip2.append(Constants.newLine);
        templateOfShip3.append("{技能ico}");
        templateOfShip3.append(Constants.newLine);
        templateOfShip3.append("{技能名称}:{技能描述}");
        templateOfShip3.append(Constants.newLine);
    }

    private void initPlace() {
        templateOfPlace.append("关卡信息:");
        templateOfPlace.append(Constants.newLine);
        templateOfPlace.append("{立绘}");
        templateOfPlace.append(Constants.newLine);
        templateOfPlace.append("{名称}");
        templateOfPlace.append(Constants.newLine);
        templateOfPlace.append("地图掉落:{地图掉落}");
        templateOfPlace.append(Constants.nextMsg);
        templateOfPlace.append("{掉落信息}");
        templateOfPlace.append(Constants.newLine);
        templateOfPlace.append("{copyright}");
        templateOfPlace1.append("{标题}:{内容}");
        templateOfPlace1.append(Constants.newLine);
    }

    private enum Type {
        SHIP, PLACE, OTHER
    }

    public String search(String name) {
        if (StrKit.isBlank(name)) {
            throw new VskException("未知查询条件");
        }
        Document document = connect(name.trim().replace(" ", ""));
        if (document == null) {
            throw new VskException("目标不存在或无法访问");
        }
        Type type = getType(document);
        switch (type) {
            case SHIP:
                return parseShip(document);
            case PLACE:
                return parsePlace(document);
            default:
                throw new VskException("未知类型");
        }
    }

    private Document connect(String name) {
        try {
            return Jsoup.connect(wikiUrl + name).get();
        } catch (IOException e) {
            Constants.doNothing();
        }
        return null;
    }

    private Type getType(Document document) {
        try {
            String html = document.select(".sv-general").select("tr:eq(1) td:eq(1)").outerHtml();
            if (html.contains("编号")) {
                return Type.SHIP;
            }
        } catch (Exception e) {
            Constants.doNothing();
        }
        try {
            String html = document.select(".wikitable").get(0).select("tr:eq(2) th").outerHtml();
            if (html.contains("开放等级")) {
                return Type.PLACE;
            }
        } catch (Exception e) {
            Constants.doNothing();
        }
        return Type.OTHER;
    }

    private String parseShip(Document d) {
        String template = templateOfShip.toString();
        Elements t = d.select(".sv-general");
        template = template.replace("{立绘}", CQUtils.image(d.select("div.Contentbox2 img").get(0).attr("src")));
        template = template.replace("{名称}", clean(t.select("tr:eq(0) td:eq(0)").text()));
        template = template.replace("{编号}", clean(t.select("tr:eq(1) td:eq(2)").text()));
        template = template.replace("{初始星级}", clean(t.select("tr:eq(1) td:eq(4)").text()));
        template = template.replace("{类型}", clean(t.select("tr:eq(2) td:eq(1)").text()));
        template = template.replace("{稀有度}", clean(t.select("tr:eq(2) td:eq(3)").text()));
        template = template.replace("{阵营}", clean(t.select("tr:eq(3) td:eq(1)").text()));
        template = template.replace("{耗时}", clean(t.select("tr:eq(3) td:eq(3)").text()));
        template = template.replace("{营养价值}", clean(t.select("tr:eq(6) td:eq(1)").text()));
        template = template.replace("{退役收益}", clean(t.select("tr:eq(7) td:eq(1)").text()));
        template = template.replace("{普通掉落点}", clean(t.select("tr:eq(4) td:eq(1)").text()));
        template = template.replace("{活动掉落点}", clean(t.select("tr:eq(5) td:eq(1)").text()));
        template = template.replace("{性能}", parseShip1(d));
        template = template.replace("{突破升星效果}", parseShip2(d));
        template = template.replace("{技能}", parseShip3(d));
        template = template.replace("{copyright}", copyright());
        return template;
    }

    private String parseShip1(Document d) {
        String template = templateOfShip1.toString();
        Elements t1 = d.select(".sv-performance").select(".sv-breakthrough");
        Elements t2 = d.select(".sv-performance");
        template = template.replace("{耐久eng}", clean(t1.select("tr:eq(0) td:eq(1)").text()));
        template = template.replace("{防空eng}", clean(t1.select("tr:eq(1) td:eq(1)").text()));
        template = template.replace("{机动eng}", clean(t1.select("tr:eq(2) td:eq(1)").text()));
        template = template.replace("{航空eng}", clean(t1.select("tr:eq(3) td:eq(1)").text()));
        template = template.replace("{雷击eng}", clean(t1.select("tr:eq(4) td:eq(1)").text()));
        template = template.replace("{炮击eng}", clean(t1.select("tr:eq(5) td:eq(1)").text()));
        template = template.replace("{耐久content}", clean1(t2.select("tr:eq(3) td:eq(1)").text()));
        template = template.replace("{防空content}", clean1(t2.select("tr:eq(5) td:eq(1)").text()));
        template = template.replace("{机动content}", clean1(t2.select("tr:eq(4) td:eq(5)").text()));
        template = template.replace("{航空content}", clean1(t2.select("tr:eq(5) td:eq(3)").text()));
        template = template.replace("{雷击content}", clean1(t2.select("tr:eq(4) td:eq(3)").text()));
        template = template.replace("{炮击content}", clean1(t2.select("tr:eq(4) td:eq(1)").text()));
        template = template.replace("{装填content}", clean1(t2.select("tr:eq(3) td:eq(5)").text()));
        template = template.replace("{消耗content}", clean1(t2.select("tr:eq(5) td:eq(5)").text()));
        template = template.replace("{反潜content}", clean1(t2.select("tr:eq(6) td:eq(1)").text()));
        template = template.replace("{航速content}", clean1(t2.select("tr:eq(7) td:eq(1)").text()));
        template = template.replace("{装甲content}", clean1(t2.select("tr:eq(3) td:eq(3)").text()));
        return template;
    }

    private String clean1(String s) {
        return clean(s
                .replace("A ", "")
                .replace("B ", "")
                .replace("C ", "")
                .replace("D ", "")
                .replace("E ", ""));
    }

    private String clean(String s) {
        s = s.replace("\r", "");
        s = s.replace("\n", "");
        s = s.replace("\t", "");
        s = s.replace("→", " >> ");
        return s.trim();
    }

    private String parseShip2(Document d) {
        String template = templateOfShip2.toString();
        StringBuilder result = new StringBuilder();
        Elements t = d.select(".sv-breakthrough").get(1).select("tr");
        for (int i = 0; i < t.size(); i++) {
            if (i == 0) continue;
            Element tr = t.get(i);
            String res = template;
            res = res.replace("{阶段}", clean(tr.select("td:eq(0)").text()));
            res = res.replace("{阶段内容}", clean(tr.select("td:eq(1)").text()));
            result.append(res);
        }
        return result.toString();
    }

    private String parseShip3(Document d) {
        String template = templateOfShip3.toString();
        StringBuilder result = new StringBuilder();
        Elements t = d.select(".sv-skill").get(0).select("tr");
        for (int i = 0; i < t.size(); i++) {
            if (i == 0) continue;
            Element tr = t.get(i);
            if (tr.attr("style").contains("display")) continue;
            String res = template;
            res = res.replace("{技能ico}", CQUtils.image(tr.select("td:eq(0) img").attr("src")));
            res = res.replace("{技能名称}", clean(tr.select("td:eq(0)").text()));
            res = res.replace("{技能描述}", clean(tr.select("td:eq(1)").text()));
            result.append(res);
        }
        return result.toString();
    }

    private String parsePlace(Document d) {
        String template = templateOfPlace.toString();
        Element t = d.select(".wikitable").get(0);
        template = template.replace("{立绘}", CQUtils.image(d.select(".wikitable").get(1).select("img").get(0).attr("src")));
        template = template.replace("{名称}", clean2(t.select("tr:eq(0) th").text()));
        template = template.replace("{地图掉落}", clean(t.select("tr:eq(12) td").text()));
        template = template.replace("{掉落信息}", parsePlace1(d));
        template = template.replace("{copyright}", copyright());
        return template;
    }

    private String parsePlace1(Document d) {
        String template = templateOfPlace1.toString();
        StringBuilder result = new StringBuilder();
        Elements t = d.select(".table-DropList").get(0).select("tr");
        for (Element tr : t) {
            String res = template;
            res = res.replace("{标题}", clean(tr.select("th").text()));
            res = res.replace("{内容}", clean(tr.select("td").text()));
            result.append(res);
        }
        return result.toString();
    }

    private String clean2(String s) {
        return clean(s
                .replace("▶", ""));
    }
}
