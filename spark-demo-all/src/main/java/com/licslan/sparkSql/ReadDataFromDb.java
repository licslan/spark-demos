package com.licslan.sparkSql;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.SQLContext;

import java.util.Properties;

/**
 * Created by Administrator on 2018/10/2.
 */
public class ReadDataFromDb {

    private static void getTagByDay(SQLContext sqlContext) {
        String url = "jdbc:mysql://xxx:3306/testdb";
        //查找的表名
        String table = "news";
        //增加数据库的用户名(user)密码(password),指定test数据库的驱动(driver)
        Properties connectionProperties = new Properties();
        connectionProperties.put("user", "root");
        connectionProperties.put("password", "123456");
        connectionProperties.put("driver", "com.mysql.jdbc.Driver");

        //SparkJdbc读取Postgresql的products表内容
        System.out.println("读取test数据库中的user_test表内容");

        // 读取表中所有数据
        sqlContext.read().jdbc(url, table, connectionProperties).createOrReplaceTempView("news");
        Dataset jd = sqlContext.sql("SELECT * FROM news  ");
        //显示数据
        jd.show();

    }

}
