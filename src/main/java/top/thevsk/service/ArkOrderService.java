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

public class ArkOrderService extends BaseService {

    private static final String wikiUrl = "http://wiki.joyme.com/fzzl/";
    private StringBuilder templateOfGod;
    private StringBuilder templateOfGodSkill;
    private StringBuilder templateOfGodUpdate;
    private StringBuilder templateOfPlace;
    private StringBuilder templateOfWeapon;

    @Override
    public void init() {
        templateOfGod = new StringBuilder();
        templateOfGodSkill = new StringBuilder();
        templateOfGodUpdate = new StringBuilder();
        templateOfPlace = new StringBuilder();
        templateOfWeapon = new StringBuilder();
        initTemplateOfGod();
        initTemplateOfPlace();
        initTemplateOfWeapon();
    }

    private void initTemplateOfGod() {
        templateOfGod.append("誓灵信息：");
        templateOfGod.append(Constants.newLine);
        templateOfGod.append("{立绘}");
        templateOfGod.append(Constants.newLine);
        templateOfGod.append("{名称}");
        templateOfGod.append(Constants.newLine);
        templateOfGod.append("编号：{编号}");
        templateOfGod.append(Constants.newLine);
        templateOfGod.append("稀有度：{稀有度}");
        templateOfGod.append(Constants.newLine);
        templateOfGod.append("属性：{属性}");
        templateOfGod.append(Constants.newLine);
        templateOfGod.append("出击消耗：{消耗}");
        templateOfGod.append(Constants.newLine);
        templateOfGod.append("获取方式：{出没地点}");
        templateOfGod.append(Constants.newLine);
        templateOfGod.append("天赋：{天赋名称}");
        templateOfGod.append(Constants.newLine);
        templateOfGod.append("{天赋属性}");
        templateOfGod.append(Constants.nextMsg);
        templateOfGod.append("六维：");
        templateOfGod.append(Constants.newLine);
        templateOfGod.append("生命：{生命}");
        templateOfGod.append(Constants.newLine);
        templateOfGod.append("物攻：{物攻}");
        templateOfGod.append(Constants.newLine);
        templateOfGod.append("魔攻：{魔攻}");
        templateOfGod.append(Constants.newLine);
        templateOfGod.append("物防：{物防}");
        templateOfGod.append(Constants.newLine);
        templateOfGod.append("魔防：{魔防}");
        templateOfGod.append(Constants.newLine);
        templateOfGod.append("速度：{速度}");
        templateOfGod.append(Constants.nextMsg);
        templateOfGod.append("技能：");
        templateOfGod.append(Constants.newLine);
        templateOfGod.append("{技能列表}");
        templateOfGod.append(Constants.nextMsg);
        templateOfGod.append("进化：");
        templateOfGod.append(Constants.newLine);
        templateOfGod.append("{进化}");
        templateOfGod.append(Constants.newLine);
        templateOfGod.append("{copyright}");
        templateOfGodSkill.append("{图标}");
        templateOfGodSkill.append(Constants.blank);
        templateOfGodSkill.append("{名称}：");
        templateOfGodSkill.append(Constants.newLine);
        templateOfGodSkill.append("{属性类型}");
        templateOfGodSkill.append(Constants.newLine);
        templateOfGodSkill.append("{PP威力命中}");
        templateOfGodSkill.append(Constants.newLine);
        templateOfGodSkill.append("{范围}");
        templateOfGodSkill.append(Constants.newLine);
        templateOfGodSkill.append("{描述}");
        templateOfGodSkill.append(Constants.newLine);
        templateOfGodSkill.append("{备注}");
        templateOfGodSkill.append(Constants.newLine);
        templateOfGodUpdate.append("三星：金币消耗：{进化金币-三星}");
        templateOfGodUpdate.append(Constants.newLine);
        templateOfGodUpdate.append("材料：{进化材料-三星}");
        templateOfGodUpdate.append(Constants.newLine);
        templateOfGodUpdate.append("四星：金币消耗：{进化金币-四星}");
        templateOfGodUpdate.append(Constants.newLine);
        templateOfGodUpdate.append("材料：{进化材料-四星}");
        templateOfGodUpdate.append(Constants.newLine);
        templateOfGodUpdate.append("五星：金币消耗：{进化金币-五星}");
        templateOfGodUpdate.append(Constants.newLine);
        templateOfGodUpdate.append("材料：{进化材料-五星}");
    }

    private void initTemplateOfPlace() {
        templateOfPlace.append("关卡信息：");
        templateOfPlace.append(Constants.newLine);
        templateOfPlace.append("{名称}");
        templateOfPlace.append(Constants.newLine);
        templateOfPlace.append("三星条件：{三星条件}");
        templateOfPlace.append(Constants.newLine);
        templateOfPlace.append("素材掉落：{素材掉落}");
        templateOfPlace.append(Constants.newLine);
        templateOfPlace.append("道具掉落：{道具掉落}");
        templateOfPlace.append(Constants.newLine);
        templateOfPlace.append("道中等级：{道中等级}");
        templateOfPlace.append(Constants.newLine);
        templateOfPlace.append("道中经验：{道中经验}");
        templateOfPlace.append(Constants.newLine);
        templateOfPlace.append("BOSS等级：{BOSS等级}");
        templateOfPlace.append(Constants.newLine);
        templateOfPlace.append("BOSS经验：{BOSS经验}");
        templateOfPlace.append(Constants.newLine);
        templateOfPlace.append("BOSS阵容：{BOSS阵容}");
        templateOfPlace.append(Constants.nextMsg);
        templateOfPlace.append("誓灵掉落：");
        templateOfPlace.append(Constants.newLine);
        templateOfPlace.append("UR：{UR誓灵掉落}");
        templateOfPlace.append(Constants.newLine);
        templateOfPlace.append("SR：{SR誓灵掉落}");
        templateOfPlace.append(Constants.newLine);
        templateOfPlace.append("R：{R誓灵掉落}");
        templateOfPlace.append(Constants.newLine);
        templateOfPlace.append("N：{N誓灵掉落}");
        templateOfPlace.append(Constants.newLine);
        templateOfPlace.append("{copyright}");
    }

    private void initTemplateOfWeapon() {
        templateOfWeapon.append("圣器信息：");
        templateOfWeapon.append(Constants.newLine);
        templateOfWeapon.append("{立绘}");
        templateOfWeapon.append(Constants.newLine);
        templateOfWeapon.append("{名称}");
        templateOfWeapon.append(Constants.newLine);
        templateOfWeapon.append("{获得途径}");
        templateOfWeapon.append(Constants.newLine);
        templateOfWeapon.append("level1:{lin1}");
        templateOfWeapon.append(Constants.newLine);
        templateOfWeapon.append("level2:{lin2}");
        templateOfWeapon.append(Constants.newLine);
        templateOfWeapon.append("level3:{lin3}");
        templateOfWeapon.append(Constants.newLine);
        templateOfWeapon.append("{copyright}");
    }

    private enum Type {
        GOD, WEAPON, PLACE, OTHER
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
            case GOD:
                return parseGod(document);
            case PLACE:
                return parsePlace(document);
            case WEAPON:
                return parseWeapon(document);
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
            String html = document.select(".wikitable").get(0).select("tr:eq(5) td:eq(0)").outerHtml();
            if (html.contains("誓约破除")) {
                return Type.GOD;
            }
        } catch (Exception e) {
            Constants.doNothing();
        }
        try {
            String html = document.select(".wikitable").get(1).select("tr:eq(1) th").outerHtml();
            if (html.contains("三星条件")) {
                return Type.PLACE;
            }
        } catch (Exception e) {
            Constants.doNothing();
        }
        try {
            String html = document.select("#mw-normal-catlinks").get(0).outerHtml();
            if (html.contains("圣器")) {
                return Type.WEAPON;
            }
        } catch (Exception e) {
            Constants.doNothing();
        }
        return Type.OTHER;
    }

    private String parseGod(Document d) {
        String template = templateOfGod.toString();
        Elements t = d.select(".wikitable");
        template = template.replace("{立绘}", CQUtils.image(d.select(".tabbertab:eq(0) img").attr("src")));
        template = template.replace("{名称}", t.get(0).select("tr:eq(0) th").text());
        template = template.replace("{编号}", clean(t.get(0).select("tr:eq(1) td:eq(2)").text()));
        template = template.replace("{稀有度}", clean(t.get(0).select("tr:eq(1) td:eq(4)").text()));
        template = template.replace("{属性}", clean(t.get(0).select("tr:eq(2) td:eq(1)").text()));
        template = template.replace("{消耗}", clean(t.get(0).select("tr:eq(2) td:eq(3)").text()));
        template = template.replace("{出没地点}", clean(t.get(0).select("tr:eq(3) td:eq(1)").text()));
        template = template.replace("{天赋名称}", clean(t.get(1).select("tr:eq(6) td b:eq(1)").text()));
        template = template.replace("{天赋属性}", clean(t.get(1).select("tr:eq(7) td").text()));
        String lv_sm = clean(t.get(2).select("tr:eq(0) td").text());
        String lv_wg = clean(t.get(2).select("tr:eq(1) td").text());
        String lv_mg = clean(t.get(2).select("tr:eq(4) td").text());
        String lv_wf = clean(t.get(2).select("tr:eq(2) td").text());
        String lv_mf = clean(t.get(2).select("tr:eq(5) td").text());
        String lv_sd = clean(t.get(2).select("tr:eq(3) td").text());
        lv_sm += "(" + clean(t.get(1).select("tr:eq(3) td").get(1).text()) + ")";
        lv_wg += "(" + clean(t.get(1).select("tr:eq(4) td").get(1).text()) + ")";
        lv_mg += "(" + clean(t.get(1).select("tr:eq(5) td").get(1).text()) + ")";
        lv_wf += "(" + clean(t.get(1).select("tr:eq(4) td").get(2).text()) + ")";
        lv_mf += "(" + clean(t.get(1).select("tr:eq(5) td").get(2).text()) + ")";
        lv_sd += "(" + clean(t.get(1).select("tr:eq(3) td").get(2).text()) + ")";
        template = template.replace("{生命}", lv_sm);
        template = template.replace("{物攻}", lv_wg);
        template = template.replace("{魔攻}", lv_mg);
        template = template.replace("{物防}", lv_wf);
        template = template.replace("{魔防}", lv_mf);
        template = template.replace("{速度}", lv_sd);
        template = template.replace("{技能列表}", parseGodSkill(d));
        template = template.replace("{进化}", parseGodUpdate(d));
        template = template.replace("{copyright}", copyright());
        return template;
    }

    private String parseGodSkill(Document d) {
        StringBuilder result = new StringBuilder();
        Element t = d.select(".wikitable").get(4);
        int length = t.select("tr").size() - 1;
        for (int i = 0; i < length; i++) {
            Elements ts = t.select("tr:eq(" + (1 + i) + ") td");
            for (Element e : ts) {
                if (!StrKit.isBlank(clean(e.text()))) {
                    result.append(parseGodSkill(e));
                }
            }
        }
        String skill = result.toString();
        if (!StrKit.isBlank(skill) && skill.length() > 3) {
            return skill.substring(0, skill.length() - 2);
        }
        return skill;
    }

    private String parseGodSkill(Element e) {
        String template = templateOfGodSkill.toString();
        template = template.replace("{图标}", CQUtils.image(e.select("p:eq(0) a:eq(0) img").attr("src")));
        template = template.replace("{名称}", clean(e.select("p:eq(0) a:eq(1) b font").text()));
        template = template.replace("{属性类型}", clean(e.select("p:eq(1) span").text()));
        template = template.replace("{PP威力命中}", clean(e.select("p:eq(2)").text()));
        template = template.replace("{范围}", clean(e.select("p:eq(3)").text()));
        template = template.replace("{描述}", clean(e.select("p:eq(4)").text()));
        template = template.replace("{备注}", clean(e.select("p:eq(5) font").text()));
        return template;
    }

    private String parseGodUpdate(Document d) {
        String template = templateOfGodUpdate.toString();
        Element t = d.select(".wikitable").get(3);
        template = template.replace("{进化金币-三星}", clean(t.select("tr:eq(2) td:eq(0)").text()));
        template = template.replace("{进化材料-三星}", parseGodUpdate(d, 1));
        template = template.replace("{进化金币-四星}", clean(t.select("tr:eq(4) td:eq(0)").text()));
        template = template.replace("{进化材料-四星}", parseGodUpdate(d, 3));
        template = template.replace("{进化金币-五星}", clean(t.select("tr:eq(6) td:eq(0)").text()));
        template = template.replace("{进化材料-五星}", parseGodUpdate(d, 5));
        return template;
    }

    private String parseGodUpdate(Document d, int line) {
        StringBuilder result = new StringBuilder();
        Element t = d.select(".wikitable").get(3);
        int length = t.select("tr:eq(" + line + ") td").size() - 2;
        for (int i = 0; i < length; i++) {
            result.append(CQUtils.image(t.select("tr:eq(" + line + ") td:eq(" + (2 + i) + ") img").attr("src")));
            result.append("：");
            result.append(clean(t.select("tr:eq(" + (line + 1) + ") td:eq(" + (1 + i) + ")").text()));
            result.append(Constants.blank);
        }
        return result.toString();
    }

    private String parsePlace(Document d) {
        String template = templateOfPlace.toString();
        Element t = d.select(".wikitable").get(1);
        template = template.replace("{名称}", clean(t.select("tr:eq(0) th").text()));
        template = template.replace("{三星条件}", clean(t.select("tr:eq(1) td").text()));
        template = template.replace("{素材掉落}", clean(t.select("tr:eq(2) td").text()));
        template = template.replace("{道具掉落}", clean(t.select("tr:eq(3) td").text()));
        template = template.replace("{道中等级}", clean(t.select("tr:eq(4) td").text()));
        template = template.replace("{道中经验}", clean(t.select("tr:eq(5) td").text()));
        template = template.replace("{BOSS等级}", clean(t.select("tr:eq(6) td").text()));
        template = template.replace("{BOSS经验}", clean(t.select("tr:eq(7) td").text()));
        template = template.replace("{BOSS阵容}", clean(t.select("tr:eq(8) td").text()));
        template = template.replace("{UR誓灵掉落}", clean(t.select("tr:eq(9) td").text()));
        template = template.replace("{SR誓灵掉落}", clean(t.select("tr:eq(10) td").text()));
        template = template.replace("{R誓灵掉落}", clean(t.select("tr:eq(11) td").text()));
        template = template.replace("{N誓灵掉落}", clean(t.select("tr:eq(12) td").text()));
        template = template.replace("{copyright}", copyright());
        return template;
    }

    private String parseWeapon(Document d) {
        String template = templateOfWeapon.toString();
        Element t = d.select(".wikitable").get(0);
        template = template.replace("{立绘}", CQUtils.image(t.select("tr:eq(3) td:eq(0) img").attr("src")));
        template = template.replace("{名称}", clean(t.select("tr:eq(0) th").text()));
        template = template.replace("{获得途径}", clean(t.select("tr:eq(2) td").text()));
        try {
            template = template.replace("{lin1}", clean(t.select("tr:eq(3) td:eq(1)").text()));
        } catch (Exception e) {
            template = template.replace("{lin1}", "无");
        }
        try {
            template = template.replace("{lin2}", clean(t.select("tr:eq(4) td:eq(1)").text()));
        } catch (Exception e) {
            template = template.replace("{lin2}", "无");
        }
        try {
            template = template.replace("{lin3}", clean(t.select("tr:eq(5) td:eq(1)").text()));
        } catch (Exception e) {
            template = template.replace("{lin3}", "无");
        }
        template = template.replace("{copyright}", copyright());
        return template;
    }

    private String copyright() {
        return "内容来源：方舟WIKI";
    }

    private String clean(String s) {
        s = s.replace("\r", "");
        s = s.replace("\n", "");
        s = s.replace("\t", "");
        s = s.replace(" ", "");
        s = s.replace("30px", "");
        s = s.replace("☞", " -> ");
        return s;
    }

    private String cleanHtmlAndBr(String s) {
        s = clean(s);
        s = s.replace("<br>", Constants.newLine);
        s = s.replace("<br/>", Constants.newLine);
        s = s.replace("<b>", "");
        s = s.replace("</b>", "");
        s = s.replace("<td>", "");
        s = s.replace("</td>", "");
        s = s.replace("<p>", "");
        s = s.replace("</p>", "");
        return s;
    }

    public static void main(String[] args) {
        ArkOrderService arkOrderService = new ArkOrderService();
        arkOrderService.init();
        System.out.println("------------");
        System.out.println(arkOrderService.search("蚩尤"));
        System.out.println("------------");
    }
}
