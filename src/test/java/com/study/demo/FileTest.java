package com.study.demo;

import com.alibaba.dubbo.common.json.JSON;
import com.alibaba.dubbo.common.json.JSONArray;
import com.alibaba.dubbo.common.json.JSONObject;
import com.study.demo.util.JavaSourceFromString;
import org.apache.commons.lang3.StringUtils;

import javax.tools.*;
import java.io.*;
import java.util.Arrays;

/**
 * Created by heyanwei-thinkpad on 2019/2/27.
 */
public class FileTest {

    public static void main(String[] args)throws Exception{
        StringBuilder classStr = new StringBuilder("package dyclass;public class Foo{");
        classStr.append("public void test(){");
        classStr.append("System.out.println(\"Foo2\");}}");

        JavaCompiler jc = ToolProvider.getSystemJavaCompiler();
        StandardJavaFileManager fileManager = jc.getStandardFileManager(null, null, null);
        JavaFileManager.Location location = StandardLocation.CLASS_OUTPUT;
        File file = new File("bin/");
        if(!file.exists()){
            file.mkdirs();
        }
        File[] outputs = new File[]{file};
        try {
            fileManager.setLocation(location, Arrays.asList(outputs));
        } catch (IOException e) {
            e.printStackTrace();
        }

        JavaFileObject jfo = new JavaSourceFromString("dyclass.Foo", classStr.toString());
        JavaFileObject[] jfos = new JavaFileObject[]{jfo};
        Iterable<? extends JavaFileObject> compilationUnits = Arrays.asList(jfos);
        boolean b = jc.getTask(null, fileManager, null, null, null, compilationUnits).call();
        if(b){//如果编译成功

        }
    }

    public static void fileToJsonFile() throws Exception{
        File gudongFile = new File("C:\\Users\\heyanwei-thinkpad\\Desktop\\前海数据\\腾讯来源\\公司股东信息.txt");
        File gaoguanFile = new File("C:\\Users\\heyanwei-thinkpad\\Desktop\\前海数据\\腾讯来源\\公司主要成员信息.txt");
        FileReader fr = new FileReader(gudongFile);
        BufferedReader br = new BufferedReader(fr);
        FileOutputStream out = new FileOutputStream(new File("C:\\Users\\heyanwei-thinkpad\\Desktop\\前海数据\\董监高姓名.txt"));
        String s = "";
        int lineNum = 1;
        StringBuilder builder = new StringBuilder();
        while (StringUtils.isNotBlank(s = br.readLine())){
            JSONObject jo = (JSONObject) JSON.parse(s);

            if(jo.get("respCode").toString() .equals( "0")){
                JSONObject jo2 = (JSONObject) jo.get("respBody");
                JSONArray array = (JSONArray) jo2.get("ShareholderInfolist");
                for (int i = 0; i < array.length(); i++) {
                    String investor_name = (String) ((JSONObject) array.get(i)).get("investor_name");
                    if(StringUtils.isNotBlank(investor_name) && builder.indexOf(investor_name) == -1 && investor_name.indexOf("公司") == -1 && investor_name.indexOf("企业") == -1){
                        builder.append(investor_name);
                        builder.append(",");
                    }
                }
            }
            appendGaoguan(new LineNumberReader(new FileReader(gaoguanFile)),builder,lineNum);
            out.write(builder.toString().getBytes());
            out.write(System.getProperty("line.separator").getBytes());
            out.flush();
            builder.delete(0,builder.length());
            lineNum ++;
        }
        out.close();
        br.close();
        fr.close();

    }

    public static void appendGaoguan(LineNumberReader br, StringBuilder builder,int linenum) throws Exception{
        String s= "";
        int num = 1;
        while (StringUtils.isNotBlank(s = br.readLine())){
            if(num < linenum){
                num ++;
                continue;
            }
            if(num > linenum){
                break;
            }
            JSONObject jo = (JSONObject) JSON.parse(s);
            if(!jo.get("respCode").toString() .equals( "0")){
                continue;
            }
            JSONObject jo2 = (JSONObject) jo.get("respBody");
            JSONArray array = (JSONArray) jo2.get("staffInfolist");
            for (int i = 0; i < array.length(); i++) {
                String name = (String) ((JSONObject) array.get(i)).get("name");
                if(StringUtils.isNotBlank(name) && builder.indexOf(name) == -1 && name.indexOf("公司") == -1 && name.indexOf("企业") == -1){
                    builder.append(name);
                    builder.append(",");
                }
            }
            if(builder.toString().endsWith(",")){
                builder.deleteCharAt(builder.length()-1);
            }
            if(StringUtils.isBlank(builder.toString())){
                builder.append("无");
            }
            num++;
            System.out.println();
        }
        br.close();
    }

}
