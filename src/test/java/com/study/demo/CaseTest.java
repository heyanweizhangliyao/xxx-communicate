package com.study.demo;

import com.alibaba.dubbo.common.json.JSON;
import com.alibaba.dubbo.common.json.JSONObject;
import com.study.demo.util.HashUtils;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.poi.ss.usermodel.Row;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.*;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by heyanwei-thinkpad on 2019/1/3.
 */
public class CaseTest {

    final static String txbasi_Uri = "https://zhengxin.urlsec.qq.com/";
    final static String tx_type = "gongshang";

    static final String tx_basicinfouri = "/GetCompany";
    static final String tx_changeinfo = "/GetCompanyChangeInfo";
    static final String tx_jingyingyichang = "/GetCompanyAbnormalInfo";
    static final String tx_yanzhongweifa = "/GetCompanyIllegalInfo";
    static final String tx_shixinrenyuan = "/GetCompanyDishonestInfo";
    static final String tx_gongsiqianshui = "/GetCompanyOwnTaxInfo";
    static final String tx_zhuyaochengyuan = "/GetCompanyStaffInfo";

    static final String tx_gudongxinxi = "/GetCompanyShareholderInfo";
    static final String tx_touzizhe = "/GetCompanyInvestorInfo";
    static final String tx_zigongsi = "/GetCompanyBranchInfo";

    static final String tx_gongxigaoguan = "/GetStockSeniorExecutiveInfo";
    static final String vcangukonggu = "/GetStockHoldingCompanyInfo";
    static final String tx_shidagudong = "/GetStockShareholderInfo";

    static BiMap<String, String> tx_urlnamemappings = HashBiMap.create();
    private static Map<String, String> qcc_urlnamemappings = HashBiMap.create();
    private static String QCC_SECRITY_KEY = "6D991ECB7237A142394331B341D198FA";

    static {
        tx_urlnamemappings.put(txbasi_Uri + tx_basicinfouri, "公司基础信息");
        tx_urlnamemappings.put(txbasi_Uri + tx_changeinfo, "公司变更信息");
        tx_urlnamemappings.put(txbasi_Uri + tx_jingyingyichang, "公司经营异常信息");
        tx_urlnamemappings.put(txbasi_Uri + tx_yanzhongweifa, "公司严重违法信息");
        tx_urlnamemappings.put(txbasi_Uri + tx_shixinrenyuan, "失信人信息");
        tx_urlnamemappings.put(txbasi_Uri + tx_gongsiqianshui, "公司欠税信息");
        tx_urlnamemappings.put(txbasi_Uri + tx_zhuyaochengyuan, "公司主要成员信息");
        tx_urlnamemappings.put(txbasi_Uri + tx_gudongxinxi, "公司股东信息");
        tx_urlnamemappings.put(txbasi_Uri + tx_touzizhe, "公司投资者信息");
        tx_urlnamemappings.put(txbasi_Uri + tx_zigongsi, "子公司信息");
        tx_urlnamemappings.put(txbasi_Uri + tx_gongxigaoguan, "公司高管信息");
        tx_urlnamemappings.put(txbasi_Uri + vcangukonggu, "公司参股控股信息");
        tx_urlnamemappings.put(txbasi_Uri + tx_shidagudong, "公司十大股东信息");

//        qcc_urlnamemappings.put("http://api.qichacha.com/SeriousViolation/GetSeriousViolationList", "严重违法");
//        qcc_urlnamemappings.put("http://api.qichacha.com/CIAEmployeeV4/GetStockRelationInfo", "董监高");
//        qcc_urlnamemappings.put("http://api.qichacha.com/ECIV4/GetDetailsByName", "企业关键字精确获取详细信息");
//        qcc_urlnamemappings.put("http://api.qichacha.com/ECIRelationV4/GenerateMultiDimensionalTreeCompanyMap", "企业图谱");
//        qcc_urlnamemappings.put("http://api.qichacha.com/ECICompanyMap/GetStockAnalysisData", "企业股权穿透");
//        qcc_urlnamemappings.put("http://api.qichacha.com/Beneficiary/GetBeneficiary", "受益人穿透");
        qcc_urlnamemappings.put("http://api.qichacha.com/ECIException/GetOpException", "企业经营异常");
    }


    public static void main(String[] args) throws Exception {
        requestQcc();
    }

    private static void requestQcc() {
        requestData(qcc_urlnamemappings, "C:\\Users\\heyanwei-thinkpad\\Desktop\\前海数据\\企查查\\", "qcc");

    }

    private static void requestTx() {
        requestData(tx_urlnamemappings, "C:\\Users\\heyanwei-thinkpad\\Desktop\\前海数据\\腾讯来源\\", "tx");
    }


    /**
     * 请求腾讯提供的工商数据接口
     *
     * @throws Exception
     */
    public static void requestData(Map<String, String> mappings, String directory, String type) {
        mappings.forEach((url, fileName) -> {


            if(fileName.contains("企业经营异常")){
                try {
//                    requestQytp();

                    String rs = "";
                    FileOutputStream fos = new FileOutputStream(new File(directory + fileName + ".txt"));
                    BufferedReader reader = new BufferedReader(new FileReader("C:\\Users\\heyanwei-thinkpad\\Desktop\\前海数据\\企查查\\KeyNo.txt"));
                    String line = "";
                    CloseableHttpClient httpclient = HttpClients.createDefault();
                    CloseableHttpResponse response = null;
                    while( StringUtils.isNotBlank(line = reader.readLine())  ){
                        line = line.trim();
                        String urls = url + "?key=" + appKey + "&keyNo=" + line;
                        HttpGet httpGet = new HttpGet(urls);
                        String unixTime = System.currentTimeMillis() / 1000 + "";
                        httpGet.setHeader("Token", getQccToken(unixTime));
                        httpGet.setHeader("Timespan", unixTime);
                        response = httpclient.execute(httpGet);

                        HttpEntity entity = response.getEntity();
                        if (entity != null) {
                            rs = EntityUtils.toString(entity, "UTF-8");
                            fos.write(rs.getBytes());
                            fos.write(System.getProperty("line.separator").getBytes());
                            fos.flush();
                        }
                        response.close();
                    }
                    httpclient.close();
                    return ;

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return;
//            try {
//                String dataName = fileName;
//                Workbook wb = new XSSFWorkbook(new File("C:\\Users\\heyanwei-thinkpad\\Desktop\\137家 .xlsx"));
//                Sheet sheetAt = wb.getSheetAt(0);
//                FileOutputStream fos = new FileOutputStream(new File(directory + dataName + ".txt"));
//                for (int i = 3; i < 140; i++) {
//                    Cell cell = sheetAt.getRow(i).getCell(0);
//                    String qymc = cell.getStringCellValue();
////                    String json = requetTx(qymc, url, type,i-2);
//                    String json = requetQccGsDetail(sheetAt.getRow(i), url);
//                    if (StringUtils.isNotBlank(json)) {
//                        try {
//                            json = json.replace("\n", "").replace("\t", "");
//                            fos.write(json.getBytes());
//                            fos.write(System.getProperty("line.separator").getBytes());
//                            fos.flush();
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }
//                fos.close();
//                wb.close();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }

        });


    }


    final static String appKey = "e4cf5fa94ab24370a31e0293e42c4980";
    private static void requestQytp() throws Exception {
        BufferedReader reader = new BufferedReader(new FileReader("C:\\Users\\heyanwei-thinkpad\\Desktop\\前海数据\\企查查\\企业关键字精确获取详细信息.txt"));
        String line = "";
        FileWriter fw = new FileWriter(new File("C:\\Users\\heyanwei-thinkpad\\Desktop\\前海数据\\企查查\\KeyNo.txt"));
        while( StringUtils.isNotBlank(line=reader.readLine()) ){
            JSONObject jo = (JSONObject) JSON.parse(line);
            JSONObject result = (JSONObject) jo.get("Result");
            Object keyNo = result.get("KeyNo");
            if(keyNo != null && StringUtils.isNotBlank(keyNo.toString())){
                fw.write(keyNo.toString());
            }else{
                fw.write("无");
            }
            fw.write(System.getProperty("line.separator"));
        }
        fw.close();
        reader.close();
    }

    private static String requetQccGsDetail(Row row, String url) {
        CloseableHttpClient httpclient = HttpClients.createDefault();

        CloseableHttpResponse response = null;
        String rs = "";

        try {
            if (StringUtils.isNotBlank(row.getCell(0).getStringCellValue())) {

                url += "?key=" + appKey + "&keyNo=" + row.getCell(0).getStringCellValue();
                HttpGet httpGet = new HttpGet(url);
                String unixTime = System.currentTimeMillis() / 1000 + "";
                httpGet.setHeader("Token", getQccToken(unixTime));
                httpGet.setHeader("Timespan", unixTime);
                response = httpclient.execute(httpGet);

                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    rs = EntityUtils.toString(entity, "UTF-8");
                    if (StringUtils.isBlank(rs) || !((JSONObject) JSON.parse(rs)).get("Status").toString().equals("200")) {
                        url += "?key=" + appKey + "&keyNo=" + row.getCell(1).getStringCellValue();
                        httpGet = new HttpGet(url);
                        unixTime = System.currentTimeMillis() / 1000 + "";
                        httpGet.setHeader("Token", getQccToken(unixTime));
                        httpGet.setHeader("Timespan", unixTime);
                        response = httpclient.execute(httpGet);
                        rs = EntityUtils.toString(entity, "UTF-8");
                    }
                }
                response.close();
            }

            httpclient.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rs;

    }


    public static String requetTx(String qymc, String url, String type, int linenum) throws Exception {
//        //采用绕过验证的方式处理https请求
//        SSLContext sslcontext = createIgnoreVerifySSL();
//
//        // 设置协议http和https对应的处理socket链接工厂的对象
//        Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
//                .register("http", PlainConnectionSocketFactory.INSTANCE)
//                .register("https", new SSLConnectionSocketFactory(sslcontext))
//                .build();
//        PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
//        HttpClients.custom().setConnectionManager(connManager);
//        CloseableHttpClient httpclient = HttpClients.custom().setConnectionManagerectionManager(connManager).build();
        CloseableHttpClient httpclient = HttpClients.createDefault();

        CloseableHttpResponse response = null;
        if (type.equals("qcc")) {
            List<String> ll = new ArrayList<String>();
            String[] names = getName(linenum).split(",");
            for (int i = 0; i < names.length; i++) {
                if (StringUtils.isNotBlank(names[i])) {
                    try {
                        url += "?key=" + appKey + "&companyName=" + qymc + "&name=" + names[i];
                        HttpGet httpGet = new HttpGet(url);
                        String unixTime = System.currentTimeMillis() / 1000 + "";
                        httpGet.setHeader("Token", getQccToken(unixTime));
                        httpGet.setHeader("Timespan", unixTime);
                        response = httpclient.execute(httpGet);
                    } catch (Exception e) {
                        e.printStackTrace();
                        continue;
                    }
                    HttpEntity entity = response.getEntity();
                    if (entity != null) {
                        ll.add(EntityUtils.toString(entity, "UTF-8"));
                    }
                    response.close();
                }
            }

            httpclient.close();
            return JSON.json(ll);
        } else if (type.equals("tx")) {
            // 创建httppost
            HttpPost httppost = new HttpPost(url);
            // 创建参数队列
            Map<String, Object> param = new HashMap<>();
            String unixTime = System.currentTimeMillis() / 1000 + "";
            param.put("company", qymc);
            param.put("signId", getSignId(unixTime));
            param.put("appid", tx_type);
            param.put("unixTime", Long.parseLong(unixTime));
            StringEntity stringEntity = new StringEntity(JSON.json(param), "UTF-8");
            stringEntity.setContentType("application/json");
            httppost.setEntity(stringEntity);
            response = httpclient.execute(httppost);


        }
        HttpEntity entity = response.getEntity();
        String rs = null;
        if (entity != null) {
            rs = EntityUtils.toString(entity, "UTF-8");
        }
        response.close();
        httpclient.close();
        return rs;
    }

    private static String getName(int linenum) throws Exception {
        LineNumberReader br = new LineNumberReader(new FileReader("C:\\Users\\heyanwei-thinkpad\\Desktop\\前海数据\\企查查\\董监高姓名.txt"));
        String s = "";
        int num = 1;
        while (StringUtils.isNotBlank(s = br.readLine())) {
            if (num < linenum) {
                num++;
                continue;
            }
            if (num >= linenum) {
                break;
            }
            num++;
        }
        br.close();
        return s;

    }

    private static String getQccToken(String unixTime) throws Exception {

        StringBuilder builder = new StringBuilder();
        builder.append(appKey).append(unixTime).append(QCC_SECRITY_KEY);
        return HashUtils.md5String(builder.toString()).toUpperCase();
    }

    static final String txPassword = "YwCo2TbqmbqTnet4VZJkUobFJOZWypay";

    private static String getSignId(String unixTime) throws Exception {
        return HashUtils.md5String(tx_type + txPassword + unixTime).substring(0, 16);
    }


    /**
     * 绕过验证
     *
     * @return
     * @throws NoSuchAlgorithmException
     * @throws KeyManagementException
     */
    public static SSLContext createIgnoreVerifySSL() throws NoSuchAlgorithmException, KeyManagementException {
        SSLContext sc = SSLContext.getInstance("SSLv3");

        // 实现一个X509TrustManager接口，用于绕过验证，不用修改里面的方法
        X509TrustManager trustManager;
        trustManager = new X509TrustManager() {
            @Override
            public void checkClientTrusted(
                    java.security.cert.X509Certificate[] paramArrayOfX509Certificate,
                    String paramString) throws CertificateException {
            }

            @Override
            public void checkServerTrusted(
                    java.security.cert.X509Certificate[] paramArrayOfX509Certificate,
                    String paramString) throws CertificateException {
            }

            @Override
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return null;
            }
        };

        sc.init(null, new TrustManager[]{trustManager}, null);
        return sc;
    }
}
