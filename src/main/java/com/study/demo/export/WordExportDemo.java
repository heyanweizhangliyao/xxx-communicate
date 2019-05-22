package com.study.demo.export;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by heyanwei-thinkpad on 2019/3/14.
 */
public class WordExportDemo {

    public static void main(String[] args) {

        //测试数据准备
        //1.标题
        String title = "t1heluosh1-使用POI导出Word";
        //2.段落数据
        String font_song_four_red = "这里是宋体四号红色字体";
        String font_black_three_black = "这里是黑体三号号黑色字体";
        String font_micro_five_red = "这里是微软雅黑五号红色字体";

        //存放段落数据
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("title", title);
        map.put("font_song_four_red", font_song_four_red);
        map.put("font_black_three_black", font_black_three_black);
        map.put("font_micro_five_red", font_micro_five_red);

        //3.表格数据
        List<Map<String,String>> excelMapList = new ArrayList<Map<String,String>>();
        Map<String,String> excelMapTemp = null;
        for (int i=1;i<11;i++) {
            excelMapTemp = new HashMap<String,String>();
            excelMapTemp.put("excel.no1", "one-"+i);
            excelMapTemp.put("excel.no2", "two-"+i);
            excelMapTemp.put("excel.no3", "three-"+i);
            excelMapTemp.put("excel.no4", "four-"+i);
            excelMapTemp.put("excel.no5", "five-"+i);
            excelMapList.add(excelMapTemp);
        }

        //模板存放位置
        String demoTemplate = "C:\\Users\\heyanwei-thinkpad\\Desktop\\muban.docx";
        //生成文档存放位置
        String targetPath = "C:\\Users\\heyanwei-thinkpad\\Desktop\\target.docx";

        //初始化导出
        WordExport export = new WordExport(demoTemplate);
        try {
            export.init();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            export.export(map);
            //0为表格的下标，第一个表格下标为0，以此类推
            export.export(excelMapList, 0);
            export.generate(targetPath);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
