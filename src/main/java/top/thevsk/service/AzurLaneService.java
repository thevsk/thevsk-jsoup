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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 碧蓝航线wiki查询
 */
public class AzurLaneService extends BaseService {

    public static void main(String[] args) {
        AzurLaneService azurLaneService = new AzurLaneService();
        azurLaneService.init();
        System.out.println(azurLaneService.search("新月"));
    }

    private static final String wikiUrl = "http://wiki.joyme.com/blhx/";
    private StringBuilder templateOfShip;
    private StringBuilder templateOfShip1;
    private StringBuilder templateOfShip2;
    private StringBuilder templateOfShip3;
    private StringBuilder templateOfPlace;
    private StringBuilder templateOfPlace1;

    private String copyright() {
        return "内容来源：碧蓝海事局(碧蓝航线WIKI)";
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
        templateOfShip.append("{信息}");
        /*templateOfShip.append("编号:{编号}");
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
        templateOfShip.append("活动掉落点:{活动掉落点}");*/
        templateOfShip.append(Constants.nextMsg);
        templateOfShip.append("性能:");
        templateOfShip.append(Constants.newLine);
        templateOfShip.append("{性能}");
        templateOfShip.append(Constants.nextMsg);
        templateOfShip.append("技能:");
        templateOfShip.append(Constants.newLine);
        templateOfShip.append("{技能}");
        templateOfShip.append(Constants.nextMsg);
        templateOfShip.append("{改造}");
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
        templateOfPlace.append("{信息}");
        //templateOfPlace.append("地图掉落:{地图掉落}");
        templateOfPlace.append(Constants.nextMsg);
        templateOfPlace.append("{掉落信息}");
        templateOfPlace.append(Constants.newLine);
        templateOfPlace.append("{copyright}");
        templateOfPlace1.append("{标题}:{内容}");
        templateOfPlace1.append(Constants.newLine);
        templateOfPlace1.append(Constants.newLine);
    }

    private enum Type {
        SHIP, PLACE, OTHER
    }

    public String search(String name) {
        if (StrKit.isBlank(name)) {
            return null;
        }
        Document document = connect(name.trim().replace(" ", ""));
        if (document == null) {
            return null;
        }
        Type type = getType(document);
        switch (type) {
            case SHIP:
                return parseShip(document);
            case PLACE:
                return parsePlace(document);
            default:
                return null;
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
        try {
            template = template.replace("{立绘}", CQUtils.image(d.select("div.Contentbox2 img").get(0).attr("src")));
        } catch (Exception e) {
            e.getMessage();
        }
        try {
            template = template.replace("{名称}", clean(t.select("tr:eq(0) td:eq(0)").text()));
        } catch (Exception e) {
            e.getMessage();
        }
        StringBuilder stringBuilder = new StringBuilder();
        Elements msg = t.select("tr");
        for (int i = 0; i < msg.size(); i++) {
            try {
                if (i == 0) continue;
                if (i == 1) {
                    stringBuilder.append(clean(msg.get(i).select("td:eq(1)").text()));
                    stringBuilder.append(":");
                    stringBuilder.append(clean(msg.get(i).select("td:eq(2)").text()));
                    stringBuilder.append(Constants.newLine);
                    stringBuilder.append(clean(msg.get(i).select("td:eq(3)").text()));
                    stringBuilder.append(":");
                    stringBuilder.append(clean(msg.get(i).select("td:eq(4)").text()));
                    stringBuilder.append(Constants.newLine);
                    continue;
                }
                if (i == 2) {
                    stringBuilder.append(clean(msg.get(i).select("td:eq(0)").text()));
                    stringBuilder.append(":");
                    stringBuilder.append(clean(msg.get(i).select("td:eq(1)").text()));
                    stringBuilder.append(Constants.newLine);
                    stringBuilder.append(clean(msg.get(i).select("td:eq(2)").text()));
                    stringBuilder.append(":");
                    stringBuilder.append(clean(msg.get(i).select("td:eq(3)").text()));
                    stringBuilder.append(Constants.newLine);
                }
                if (clean(msg.get(i).select("td:eq(0)").text()).contains("强化所需经验")) break;
                stringBuilder.append(clean(msg.get(i).select("td:eq(0)").text()));
                stringBuilder.append(":");
                stringBuilder.append(clean(msg.get(i).select("td:eq(1)").text()));
                stringBuilder.append(Constants.newLine);
            } catch (Exception e) {
                e.getMessage();
            }
        }
        template = template.replace("{信息}", stringBuilder.toString());
        /*try {
            template = template.replace("{编号}", clean(t.select("tr:eq(1) td:eq(2)").text()));
        } catch (Exception e) {
            e.getMessage();
        }
        try {
            template = template.replace("{初始星级}", clean(t.select("tr:eq(1) td:eq(4)").text()));
        } catch (Exception e) {
            e.getMessage();
        }
        try {
            template = template.replace("{类型}", clean(t.select("tr:eq(2) td:eq(1)").text()));
        } catch (Exception e) {
            e.getMessage();
        }
        try {
            template = template.replace("{稀有度}", clean(t.select("tr:eq(2) td:eq(3)").text()));
        } catch (Exception e) {
            e.getMessage();
        }
        try {
            template = template.replace("{阵营}", clean(t.select("tr:eq(3) td:eq(1)").text()));
        } catch (Exception e) {
            e.getMessage();
        }
        try {
            template = template.replace("{耗时}", clean(t.select("tr:eq(3) td:eq(3)").text()));
        } catch (Exception e) {
            e.getMessage();
        }
        try {
            template = template.replace("{营养价值}", clean(t.select("tr:eq(6) td:eq(1)").text()));
        } catch (Exception e) {
            e.getMessage();
        }
        try {
            template = template.replace("{退役收益}", clean(t.select("tr:eq(7) td:eq(1)").text()));
        } catch (Exception e) {
            e.getMessage();
        }
        try {
            template = template.replace("{普通掉落点}", clean(t.select("tr:eq(4) td:eq(1)").text()));
        } catch (Exception e) {
            e.getMessage();
        }
        try {
            template = template.replace("{活动掉落点}", clean(t.select("tr:eq(5) td:eq(1)").text()));
        } catch (Exception e) {
            e.getMessage();
        }*/
        try {
            template = template.replace("{性能}", parseShip1(d));
        } catch (Exception e) {
            e.getMessage();
        }
        try {
            template = template.replace("{技能}", parseShip3(d));
        } catch (Exception e) {
            e.getMessage();
        }
        try {
            boolean gzFlag = false;
            StringBuilder gzStr = new StringBuilder();
            Elements gzSearcher = d.select(".mw-headline");
            for (Element element : gzSearcher) {
                if (element.text().contains("改造详情")) {
                    if (d.select(".wikibox-biginside").size() > 1) {
                        gzFlag = true;
                    }
                    break;
                }
            }
            if (gzFlag) {
                gzStr.append("改造：");
                gzStr.append(Constants.newLine);
                Element gzEle = d.select(".wikibox-biginside").get(1);
                try {
                    gzStr.append(CQUtils.image(gzEle.select(".wikitable").get(0).select("img").attr("src")));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                gzStr.append(Constants.newLine);
                gzStr.append("需求合计：");
                gzStr.append(clean(gzEle.select(".wikitable").get(1).select("tr").last().select("td").text()));
            }
            template = template.replace("{改造}", gzStr.toString());
        } catch (Exception e) {
            e.getMessage();
        }
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
        System.out.println(t2.select("tr:eq(3) td:eq(1)").text());
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
                .replace("A→A", "")
                .replace("A ", "")
                .replace("B→B", "")
                .replace("B ", "")
                .replace("C→C", "")
                .replace("C ", "")
                .replace("D→D", "")
                .replace("D ", "")
                .replace("E→E", "")
                .replace("E ", ""));
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
        try {
            template = template.replace("{立绘}", CQUtils.image(d.select(".wikitable").get(1).select("img").get(0).attr("src")));
        } catch (Exception e) {
            e.getMessage();
        }
        try {
            template = template.replace("{名称}", clean2(t.select("tr:eq(0) th").text()));
        } catch (Exception e) {
            e.getMessage();
        }
        Elements msg = t.select("tr");
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < msg.size(); i++) {
            try {
                if (i == 0) continue;
                String value  = clean(msg.get(i).select("td").text());
                if (value.contains("{{{")) continue;
                String title = clean(msg.get(i).select("th").text());
                if (title.contains("介绍")) continue;
                if (title.contains("推图视频")) continue;
                if (title.contains("三星条件")) continue;
                if (title.contains("三星奖励")) continue;
                if (title.contains("信号强度")) continue;
                if (title.contains("通关奖励")) continue;
                if (title.contains("危险等级")) continue;
                if (title.contains("敌舰等级")) {
                    stringBuilder.append("敌舰等级");
                    stringBuilder.append(":");
                    stringBuilder.append(clean(msg.get(i).select("td").html().split("<br>")[0]));
                    stringBuilder.append(Constants.newLine);
                    continue;
                }
                if (title.contains("BOSS位置")) {
                    stringBuilder.append("BOSS信息");
                    stringBuilder.append(":");
                    stringBuilder.append(clean(msg.get(i).select("td").html().split("<br>")[1]));
                    stringBuilder.append(Constants.blank);
                    stringBuilder.append("等级");
                    stringBuilder.append(clean(msg.get(i).select("td").html().split("<br>")[2]));
                    stringBuilder.append(Constants.newLine);
                    continue;
                }
                if (title.contains("备注")) {
                    if (msg.get(i).select("td").html().contains("<br>")) {
                        stringBuilder.append("备注");
                        stringBuilder.append(":");
                        stringBuilder.append(Constants.newLine);
                        String[] back = msg.get(i).select("td").html().split("<br>");
                        for (String bak : back) {
                            if (StrKit.isBlank(bak)) continue;
                            if (bak.contains("<") || bak.contains(">")) continue;
                            stringBuilder.append(bak);
                            stringBuilder.append(Constants.newLine);
                        }
                        continue;
                    }
                }
                stringBuilder.append(title);
                stringBuilder.append(":");
                stringBuilder.append(value);
                stringBuilder.append(Constants.newLine);
            } catch (Exception e) {
                e.getMessage();
            }
        }
        template = template.replace("{信息}", stringBuilder.toString());
        /*try {
            template = template.replace("{地图掉落}", clean(t.select("tr:eq(12) td").text()));
        } catch (Exception e) {
            e.getMessage();
        }*/
        try {
            template = template.replace("{掉落信息}", parsePlace1(d));
        } catch (Exception e) {
            e.getMessage();
        }
        template = template.replace("{copyright}", copyright());
        return template;
    }

    private String clean(String s) {
        s = s.replace("\r", "");
        s = s.replace("\n", "");
        s = s.replace("\t", "");
        s = s.replace("→", " → ");
        return delHTMLTag(s.trim());
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

    private String delHTMLTag(String htmlStr) {
        String regEx_script = "<script[^>]*?>[\\s\\S]*?<\\/script>";
        String regEx_style = "<style[^>]*?>[\\s\\S]*?<\\/style>";
        String regEx_html = "<[^>]+>";

        Pattern p_script = Pattern.compile(regEx_script, Pattern.CASE_INSENSITIVE);
        Matcher m_script = p_script.matcher(htmlStr);
        htmlStr = m_script.replaceAll("");

        Pattern p_style = Pattern.compile(regEx_style, Pattern.CASE_INSENSITIVE);
        Matcher m_style = p_style.matcher(htmlStr);
        htmlStr = m_style.replaceAll("");

        Pattern p_html = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);
        Matcher m_html = p_html.matcher(htmlStr);
        htmlStr = m_html.replaceAll("");

        return htmlStr.trim();
    }
}
