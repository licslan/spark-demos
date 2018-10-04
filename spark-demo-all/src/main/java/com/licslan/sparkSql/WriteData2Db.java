package com.licslan.sparkSql;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.RowFactory;
import org.apache.spark.sql.SQLContext;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.StructType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import java.util.Properties;

/**
 * Created by licslan on 2018/10/2.
 */
public class WriteData2Db {


    public void writedata2db(){
    SparkConf conf = new SparkConf().setAppName("HelloWorlds").setMaster("local");

    JavaSparkContext sc = new JavaSparkContext(conf);
    SQLContext sqlContext = new SQLContext(sc);

    //写入的数据内容
    JavaRDD<String> personData = sc.parallelize(Arrays.asList("java chinese 5", "c++ chinese 6"));
    //数据库内容
    String url = "jdbc:mysql://localhost:3306/demo";
    Properties connectionProperties = new Properties();
        connectionProperties.put("user", "root");
        connectionProperties.put("password", "123456");
        connectionProperties.put("driver", "com.mysql.jdbc.Driver");
    /**
     * 第一步：在RDD的基础上创建类型为Row的RDD
     */
    //将RDD变成以Row为类型的RDD。Row可以简单理解为Table的一行数据
    JavaRDD<Row> personsRDD = personData.map(new Function<String, Row>() {
        @Override
        public Row call(String line) throws Exception {
            String[] splited = line.split(" ");
            return RowFactory.create(splited[0], splited[1], Integer.valueOf(splited[2]));
        }
    });

    /**
     * 第二步：动态构造DataFrame的元数据。
     */
    List structFields = new ArrayList();
        structFields.add(DataTypes.createStructField("search_word", DataTypes.StringType, true));
        structFields.add(DataTypes.createStructField("lang", DataTypes.StringType, true));
        structFields.add(DataTypes.createStructField("hot_index", DataTypes.IntegerType, true));

    //构建StructType，用于最后DataFrame元数据的描述
    StructType structType = DataTypes.createStructType(structFields);

    /**
     * 第三步：基于已有的元数据以及RDD<Row>来构造DataFrame
     */
    Dataset personsDF = sqlContext.createDataFrame(personsRDD, structType);

    /**
     * 第四步：将数据写入到person表中
     */
        personsDF.write().mode("append").jdbc(url, "person", connectionProperties);
        sc.close();
    }
}
