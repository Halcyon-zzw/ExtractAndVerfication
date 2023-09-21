package com.zhuzw.reptile;


import com.zhuzw.constant.ReptileConstant;
import com.zhuzw.exception.ReptileException;
import com.zhuzw.utils.FileUtil;
import com.zhuzw.utils.ListToXLSXConverter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * TODO
 *
 * @author zhuzhiwei
 * @date 2023/9/19 10:01
 */
@Slf4j
@Service
public class ReptileHtmlFileService {



    public static void main(String[] args) throws Exception{
//        reptileSecondHandHouse();
//        reptileNewHouse();
        reptileShops();
    }

    private static void reptileShops() throws IOException {
        List<String> elementClassList = List.of(
                ReptileConstant.TITLE_SHOPS,
                ReptileConstant.UNIT_PRICE_SHOPS,
                ReptileConstant.TOTAL_PRICE_SHOPS,
                ReptileConstant.AREA_SHOPS,
                ReptileConstant.ADDRESS_SHOPS,
                ReptileConstant.TAG_SHOPS,
                ReptileConstant.DESC_SHOPS
        );
        String folderPath = "/Users/zhuzhiwei/Library/Containers/com.tencent.xinWeChat/Data/Library/Application Support/com.tencent.xinWeChat/2.0b4.0.9/d270a7251950896a4e8da4b478f4f93b/Message/MessageTemp/36b666f54d6ba33ff3d80f9cced8f0ff/File/安居客网页内容/商铺";
        List<List<String>> resultList = reptileHtmlFile(folderPath, ReptileConstant.LIST_SHOPS, elementClassList);
        try {
            String path = "shops_" + UUID.randomUUID() + ".xlsx";
            ListToXLSXConverter.convertToXlsx(resultList, path);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private static void reptileNewHouse() throws IOException, InterruptedException {
        List<String> elementClassList = List.of(
                ReptileConstant.CELL_NAME_NEW_HOUSE,
                ReptileConstant.UNIT_PRICE_NEW_HOUSE,
                ReptileConstant.HOST_TYPE_NEW_HOUSE,
                ReptileConstant.ADDRESS_NEW_HOUSE
        );
        String folderPath = "/Users/zhuzhiwei/Library/Containers/com.tencent.xinWeChat/Data/Library/Application Support/com.tencent.xinWeChat/2.0b4.0.9/d270a7251950896a4e8da4b478f4f93b/Message/MessageTemp/36b666f54d6ba33ff3d80f9cced8f0ff/File/安居客网页内容/新房";
        List<List<String>> resultList = reptileHtmlFile(folderPath, ReptileConstant.LIST_NEW_HOUSE, elementClassList);
        try {
            String path = "new_hose_" + UUID.randomUUID() + ".xlsx";
            ListToXLSXConverter.convertToXlsx(resultList, path);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void reptileSecondHandHouse() throws IOException {
        List<String> elementClassList = List.of(
                ReptileConstant.CELL_NAME_SECOND_HAND_HOUSE,
                ReptileConstant.TOTAL_PRICE_HAND_HOUSE,
                ReptileConstant.UNIT_PRICE_SECOND_HAND_HOUSE,
                ReptileConstant.HOST_TYPE_SECOND_HAND_HOUSE,
                ReptileConstant.ADDRESS_SECOND_HAND_HOUSE,
                ReptileConstant.DESC_SECOND_HAND_HOUSE
                );
        String folderPath = "/Users/zhuzhiwei/Library/Containers/com.tencent.xinWeChat/Data/Library/Application Support/com.tencent.xinWeChat/2.0b4.0.9/d270a7251950896a4e8da4b478f4f93b/Message/MessageTemp/36b666f54d6ba33ff3d80f9cced8f0ff/File/安居客网页内容/二手房";
        List<List<String>> resultList = reptileHtmlFile(folderPath, ReptileConstant.LIST_SECOND_HAND_HOUSE, elementClassList);
        try {
            String path = UUID.randomUUID() + "_second_hand_house.xlsx";
            ListToXLSXConverter.convertToXlsx(resultList, path);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static List<List<String>> reptileHtmlFile(String folderPath, String listType, List<String> elementClassList) throws IOException {
        List<List<String>> resultList = new ArrayList<>();
        //读取html文件列表
        List<File> fileList = FileUtil.extractHtmlFiles(folderPath, "html", false);
//        fileList = fileList.subList(0, 1);
        for (File file : fileList) {
            System.out.println("当前处理:" + file.getName());
            String html = readFile(file);
            resultList.addAll(reptileHtml(html, elementClassList, listType));
        }
        return resultList;
    }

    private static String readFile(File file) {
        return FileUtil.readFileData(file);
    }

    private static List<List<String>> reptileHtml(String html, List<String> elementClassList, String listType) throws IOException {

        Document document = Jsoup.parse(html);
//        Document document = Jsoup.parse("<div tongji_tag=\"fcpc_ersflist_gzcount\" class=\"property\" data-v-9a1aeaec=\"\" data-v-7ba6f82f=\"\"><a href=\"https://yingtan.anjuke.com/prop/view/A6600103724?auction=201&amp;hpType=1&amp;entry=102&amp;position=0&amp;kwtype=filter&amp;now_time=1695089335&amp;spread=filtersearch_p&amp;from=from_esf_List_search&amp;index=0\" data-action=\"esf_list\" target=\"_blank\" data-ep=\"{&quot;exposure&quot;:{&quot;esf_list&quot;:1,&quot;broker_id&quot;:&quot;202805861&quot;}}\" data-cp=\"{&quot;broker_id&quot;:&quot;202805861&quot;}\" data-lego=\"{&quot;entity_id&quot;:&quot;3025311059045388&quot;,&quot;tid&quot;:&quot;-&quot;}\" class=\"property-ex\" data-v-9a1aeaec=\"\" data-exposure=\"true\"><div class=\"property-image\" data-v-9a1aeaec=\"\"><img alt=\"\" src=\"https://pic1.ajkimg.com/display/ajk/a04115a0f5fedbe97d18cdc4bb918ecd/640x420c.jpg?frame=1&amp;t=1\" class=\"lazy-img cover\" data-v-a84560ba=\"\" data-v-9a1aeaec=\"\" data-src=\"https://pic1.ajkimg.com/display/ajk/a04115a0f5fedbe97d18cdc4bb918ecd/640x420c.jpg?frame=1&amp;t=1\" lazy=\"loaded\"> <!----> <span id=\"main-0\" class=\"property-image-vr\" data-v-9a1aeaec=\"\"><svg xmlns=\"http://www.w3.org/2000/svg\" viewBox=\"0 0 370 370\" width=\"370\" height=\"370\" preserveAspectRatio=\"xMidYMid meet\" style=\"width: 100%; height: 100%; transform: translate3d(0px, 0px, 0px);\"><defs><clipPath id=\"__lottie_element_2\"><rect width=\"370\" height=\"370\" x=\"0\" y=\"0\"></rect></clipPath><clipPath id=\"__lottie_element_4\"><path d=\"M0,0 L300,0 L300,300 L0,300z\"></path></clipPath></defs><g clip-path=\"url(#__lottie_element_2)\"><g transform=\"matrix(1.2239700555801392,0,0,1.2239700555801392,185,169.00025939941406)\" opacity=\"0.5\" style=\"display: block;\"><g opacity=\"1\" transform=\"matrix(1,0,0,1,-0.27300000190734863,13.72700023651123)\"><path fill=\"rgb(0,0,0)\" fill-opacity=\"1\" d=\" M0,-150 C82.78500366210938,-150 150,-82.78500366210938 150,0 C150,82.78500366210938 82.78500366210938,150 0,150 C-82.78500366210938,150 -150,82.78500366210938 -150,0 C-150,-82.78500366210938 -82.78500366210938,-150 0,-150z\"></path></g></g><g clip-path=\"url(#__lottie_element_4)\" transform=\"matrix(0.9999902248382568,0,0,0.9999902248382568,35.00146484375,35.00146484375)\" opacity=\"1\" style=\"display: block;\"><g transform=\"matrix(1,0,0,1,145,145)\" opacity=\"1\" style=\"display: block;\"><g opacity=\"1\" transform=\"matrix(1,0,0,1,0,0)\"><path stroke-linecap=\"butt\" stroke-linejoin=\"miter\" fill-opacity=\"0\" stroke-miterlimit=\"4\" stroke=\"rgb(255,255,255)\" stroke-opacity=\"1\" stroke-width=\"13.599\" d=\"M0 0\"></path></g><g opacity=\"1\" transform=\"matrix(1,0,0,1,-17.826000213623047,-17.326000213623047)\"><path fill=\"rgb(255,255,255)\" fill-opacity=\"0\" d=\" M69,-69 C69,-69 69,69 69,69 C69,69 -69,69 -69,69 C-69,69 -69,-69 -69,-69 C-69,-69 69,-69 69,-69z\"></path><path stroke-linecap=\"butt\" stroke-linejoin=\"miter\" fill-opacity=\"0\" stroke-miterlimit=\"4\" stroke=\"rgb(255,255,255)\" stroke-opacity=\"1\" stroke-width=\"12\" d=\" M69,-69 C69,-69 69,69 69,69 C69,69 -69,69 -69,69 C-69,69 -69,-69 -69,-69 C-69,-69 69,-69 69,-69z\"></path></g><g opacity=\"1\" transform=\"matrix(1,0,0,1,0,0)\"><path fill=\"rgb(255,255,255)\" fill-opacity=\"0\" d=\" M-87,-86.5 C-87,-86.5 -48,-47.5 -48,-47.5\"></path><path stroke-linecap=\"butt\" stroke-linejoin=\"miter\" fill-opacity=\"0\" stroke-miterlimit=\"4\" stroke=\"rgb(255,255,255)\" stroke-opacity=\"1\" stroke-width=\"12\" d=\" M-87,-86.5 C-87,-86.5 -48,-47.5 -48,-47.5\"></path></g></g><g transform=\"matrix(-1,0,0,-1,149.34799194335938,150.34799194335938)\" opacity=\"1\" style=\"display: block;\"><g opacity=\"1\" transform=\"matrix(1,0,0,1,-18.013999938964844,-17.85700035095215)\"><path fill=\"rgb(255,255,255)\" fill-opacity=\"0.3\" d=\" M69,-69 C69,-69 69,69 69,69 C69,69 -69,69 -69,69 C-69,69 -69,-69 -69,-69 C-69,-69 69,-69 69,-69z\"></path><path stroke-linecap=\"butt\" stroke-linejoin=\"miter\" fill-opacity=\"0\" stroke-miterlimit=\"4\" stroke=\"rgb(255,255,255)\" stroke-opacity=\"1\" stroke-width=\"12\" d=\" M69,-69 C69,-69 69,69 69,69 C69,69 -69,69 -69,69 C-69,69 -69,-69 -69,-69 C-69,-69 69,-69 69,-69z\"></path></g><g opacity=\"1\" transform=\"matrix(1,0,0,1,0,0)\"><path fill=\"rgb(255,255,255)\" fill-opacity=\"1\" d=\" M-87,-86.5 C-87,-86.5 -48,-47.5 -48,-47.5\"></path><path stroke-linecap=\"butt\" stroke-linejoin=\"miter\" fill-opacity=\"0\" stroke-miterlimit=\"4\" stroke=\"rgb(255,255,255)\" stroke-opacity=\"1\" stroke-width=\"12\" d=\" M-87,-86.5 C-87,-86.5 -48,-47.5 -48,-47.5\"></path></g></g></g></g></svg></span></div> <div class=\"property-content\" data-v-9a1aeaec=\"\"><div class=\"property-content-detail\" data-v-9a1aeaec=\"\"><div class=\"property-content-title\" data-v-9a1aeaec=\"\"><h3 title=\"都市港湾满两年 南北通透 中间楼层 有电梯 刚需小三居\" class=\"property-content-title-name\" style=\"max-width:501px;\" data-v-9a1aeaec=\"\">都市港湾满两年 南北通透 中间楼层 有电梯 刚需小三居</h3> <img src=\"https://pages.anjukestatic.com/usersite/site/img/broker_detail/esf_list_img_anxuangolden@2x.png\" class=\"property-content-title-anxuan\" style=\"width:38px;height:24px;\" data-v-9a1aeaec=\"\">  <!----></div> <section data-v-9a1aeaec=\"\"><div class=\"property-content-info\" data-v-9a1aeaec=\"\"><p class=\"property-content-info-text property-content-info-attribute\" data-v-9a1aeaec=\"\"><span data-v-9a1aeaec=\"\">3</span> <span data-v-9a1aeaec=\"\">室</span> <span data-v-9a1aeaec=\"\">2</span> <span data-v-9a1aeaec=\"\">厅</span> <span data-v-9a1aeaec=\"\">2</span> <span data-v-9a1aeaec=\"\">卫</span></p> <p class=\"property-content-info-text\" data-v-9a1aeaec=\"\">\n" +
//                "                            100㎡\n" +
//                "                        </p> <p class=\"property-content-info-text\" data-v-9a1aeaec=\"\">南北</p> <p class=\"property-content-info-text\" data-v-9a1aeaec=\"\">\n" +
//                "                            中层(共27层)\n" +
//                "                        </p> <p class=\"property-content-info-text\" data-v-9a1aeaec=\"\">\n" +
//                "                            2019年建造\n" +
//                "                        </p></div> <div class=\"property-content-info property-content-info-comm\" data-v-9a1aeaec=\"\"><p class=\"property-content-info-comm-name\" data-v-9a1aeaec=\"\">都市港湾</p> <p class=\"property-content-info-comm-address\" data-v-9a1aeaec=\"\"><span data-v-9a1aeaec=\"\">月湖</span><span data-v-9a1aeaec=\"\">月湖城区</span><span data-v-9a1aeaec=\"\">天洁西路5号</span></p></div> <div class=\"property-content-info\" data-v-9a1aeaec=\"\"><span class=\"property-content-info-tag\" data-v-9a1aeaec=\"\">繁华地段</span><span class=\"property-content-info-tag\" data-v-9a1aeaec=\"\">交通便利</span><span class=\"property-content-info-tag\" data-v-9a1aeaec=\"\">配套成熟</span></div></section> <div class=\"property-extra-wrap\" data-v-9a1aeaec=\"\"><div class=\"property-extra\" data-v-9a1aeaec=\"\"><div class=\"property-extra-photo\" data-v-9a1aeaec=\"\"><img alt=\"\" src=\"https://spic1.ajkimg.com/GdSbK-c0mUllA0Bm3DoYMoUeA7adKn6hILq8kduSbd6-6e-QZGuihGImw3sfKFXecW1G1_jZG7bGcuPNgl97LvhqxMHNa6IAwDjWiJW4t90rxJKAczU6lHoeMjgX2HhQnODEmAYAsyN12j8N0Ll_NlLbZNfUPyNFDaHRSwbNFJMvAzzNJZkF9GVAyXk4Cnle\" class=\"lazy-img cover\" data-v-a84560ba=\"\" data-v-9a1aeaec=\"\" data-src=\"https://spic1.ajkimg.com/GdSbK-c0mUllA0Bm3DoYMoUeA7adKn6hILq8kduSbd6-6e-QZGuihGImw3sfKFXecW1G1_jZG7bGcuPNgl97LvhqxMHNa6IAwDjWiJW4t90rxJKAczU6lHoeMjgX2HhQnODEmAYAsyN12j8N0Ll_NlLbZNfUPyNFDaHRSwbNFJMvAzzNJZkF9GVAyXk4Cnle\" lazy=\"loaded\"></div> <span class=\"property-extra-text\" data-v-9a1aeaec=\"\">熊兰华</span> <!----> <span class=\"property-extra-text\" data-v-9a1aeaec=\"\">4.8分</span> <span class=\"property-extra-text\" data-v-9a1aeaec=\"\">家乐居博泰房产</span> <!----></div> <!----></div> <!----></div> <div class=\"property-price\" data-v-9a1aeaec=\"\"><p class=\"property-price-total\" data-v-9a1aeaec=\"\"><span class=\"property-price-total-num\" data-v-9a1aeaec=\"\">78</span> <span class=\"property-price-total-text\" data-v-9a1aeaec=\"\">万</span></p> <p class=\"property-price-average\" data-v-9a1aeaec=\"\">7800元/㎡</p></div></div></a></div>");
        List<List<String>> resultList = new ArrayList<>();
        Elements elements = document.getElementsByClass(listType);
        for (Element element : elements) {
            List<String> eachList = new ArrayList<>();
            for (String elementClass : elementClassList) {
                String text = element.getElementsByClass(elementClass).text();
                eachList.add(text);
            }
//            checkList(eachList);
            resultList.add(eachList);
        }
        return resultList;
    }


    private static void checkList(List<String> resultList) {
        int emptySize = 0;
        for (String s : resultList) {
            if (StringUtils.isEmpty(s)) {
                emptySize ++;
            }
        }
        if (emptySize == resultList.size()) {
            throw new ReptileException("爬取失败");
        }
    }
}

