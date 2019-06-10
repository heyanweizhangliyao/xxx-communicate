package com.study.demo.hbase;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.*;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.filter.Filter;
import org.apache.hadoop.hbase.util.Bytes;
import org.springframework.util.StringUtils;

import java.io.IOException;

/**
 * hbase 简单例子
 */
public class HBaseDemo {

    private static final Connection connection;

    private static Admin admin;

    static {
        connection = initHbase();
        try {
            admin = connection.getAdmin();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {

        try {
          /*  String[] cols = {"information","contact"};
            createTable("sys_user",cols);*/
//            deleteTable("sys_user");
          /*  SysUser sysUser = new SysUser("11","lisi",45,"10086");
            sysUser.setEmail("test1@163.com");
            sysUser.setQQ("098786738");
            insertSysUser(sysUser);*/
            getNoDealData("sys_user");
           /* SysUser abcd = getById("abcd","","");
            System.out.println(JSON.toString(abcd));*/
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Connection initHbase() {
        try {
            Configuration configuration = HBaseConfiguration.create();
            configuration.set("hbase.zookeeper.property.clientPort","2181");
            configuration.set("hbase.zookeeper.quorum","127.0.0.1");//如果是集群，value多个ip用逗号隔开
            return ConnectionFactory.createConnection(configuration);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void createTable(String tableName,String[] cols) throws IOException{
        //判断表是否存在
        TableName table = TableName.valueOf(tableName);
        if(admin.tableExists(table)){
            System.err.println("表: "+tableName+" 已存在");
            return ;
        }
        HTableDescriptor hTableDescriptor = new HTableDescriptor(table);
        for (String col : cols) {
            HColumnDescriptor hColumnDescriptor = new HColumnDescriptor(col);
            hTableDescriptor.addFamily(hColumnDescriptor);
        }
        admin.createTable(hTableDescriptor);
        System.out.println("建表成功,表名： "+tableName);
    }

    public static SysUser insertSysUser(SysUser sysUser){
        TableName sys_user = TableName.valueOf("sys_user");
        try {
            Table table = connection.getTable(sys_user);
            Put put = new Put(sysUser.getId().getBytes());
            //参数：1.列族名  2.列名  3.值
            put.addColumn("information".getBytes(),"NAME".getBytes(),sysUser.getName().getBytes());
            put.addColumn("information".getBytes(),"AGE".getBytes(),(sysUser.getAge()+"").getBytes());
            put.addColumn("information".getBytes(),"phone".getBytes(),(sysUser.getPhone()).getBytes());
            put.addColumn("contact".getBytes(), "email".getBytes(), sysUser.getQQ().getBytes());
            put.addColumn("contact".getBytes(), "qq".getBytes(), sysUser.getEmail().getBytes());
            table.put(put);
            return sysUser;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 删除表
     * @param name
     */
    public static void deleteTable(String name){
        try {
            TableName tableName = TableName.valueOf(name);
            if(!admin.tableExists(tableName)){
                System.out.println("表不存在，"+name);
                return;
            }
            admin.disableTable(tableName);
            admin.deleteTable(tableName);
            System.out.println("表删除成功，"+name);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //获取原始数据
    public static void getNoDealData(String tableName){
        try {
            Table table= initHbase().getTable(TableName.valueOf(tableName));
            Scan scan = new Scan();

//            Filter filter;
//            scan.setFilter(filter);
//            scan.setLimit(1);
            ResultScanner resutScanner = table.getScanner(scan);
            for(Result result: resutScanner){
                System.out.println("scan:  " + new String(result.getValue("information".getBytes(),"NAME".getBytes())));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 查询数据
     * @param id
     * @return
     */
    public static SysUser getById(String id,String family,String qualifier){
        TableName tableName = TableName.valueOf("sys_user");
        try {
            Table table = connection.getTable(tableName);
            Get get = new Get(id.getBytes());
            if(StringUtils.hasText(family)){
                get.addFamily(family.getBytes());
                if(StringUtils.hasText(qualifier)){
                    get.addColumn(family.getBytes(),qualifier.getBytes());
                }
            }
            SysUser sysUser = new SysUser();
            sysUser.setId(id);
            if(!get.isCheckExistenceOnly()){//确保数据存在
                Result result = table.get(get);
                for (Cell cell : result.rawCells()) {
                    String colName = Bytes.toString(cell.getQualifierArray(),cell.getQualifierOffset(),cell.getQualifierLength());
                    String value = Bytes.toString(cell.getValueArray(), cell.getValueOffset(), cell.getValueLength());
                    if(colName.toLowerCase().equals("name")){
                        sysUser.setName(value);
                    }
                    if(colName.toLowerCase().equals("age")){
                        sysUser.setAge(Integer.parseInt(value));
                    }
                    if (colName.toLowerCase().equals("phone")){
                        sysUser.setPhone(value);
                    }
                    if (colName.toLowerCase().equals("email")){
                        sysUser.setEmail(value);
                    }
                    if (colName.toLowerCase().equals("qq")){
                        sysUser.setQQ(value);
                    }

                }
                return sysUser;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
