package com.spark;

import com.alibaba.fastjson.JSON;
import com.spark.entity.People;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SQLContext;

import java.util.List;

/**
 * Created with IDEA
 * User: vector
 * Data: 2018/4/20 0020
 * Time: 10:41
 * Description:
 */
public class JavaSparkSql {
    public static void main(String[] args) {
        String filePath = "D:\\data\\spark\\people.json";
        SparkConf conf = new SparkConf().setMaster("local").setAppName("Spark SQL");
        JavaSparkContext jsc = new JavaSparkContext(conf);

        SQLContext sqlContext = new SQLContext(jsc);

        Dataset<Row> json = sqlContext.read().json(filePath);

        /**
         * 显示表的内容 (前20条)
         */
        json.show();

        /**
         * 打印节点 (tree 结构)
         */
        json.printSchema();

        /**
         *  选择属性显示 并对属性做简单操作
         */
        json.select(json.col("name"), json.col("age").plus(1)).show();

        /**
         * 简单的过滤
         */
        json.filter(json.col("age").gt(21)).show();

        /**
         * 分组统计
         */
        json.groupBy("age").count().show();


        JavaRDD<People> map = jsc.textFile(filePath).map(new Function<String, People>() {
            @Override
            public People call(String line) throws Exception {
                return JSON.parseObject(line, People.class);
            }
        });

        Dataset<Row> peopleDF = sqlContext.createDataFrame(map, People.class);
        peopleDF.registerTempTable("people");

        // SQL can be run over RDDs that have been registered as tables.
        Dataset<Row> teenagers = sqlContext.sql("select name from people where age > 13 and age <=19");

        List<String> teenagerName = teenagers.toJavaRDD().map(new Function<Row, String>() {
            @Override
            public String call(Row row) throws Exception {
                return "Name: " + row.getString(0);
            }
        }).collect();

        for (String name : teenagerName) {
            System.out.println(name);
        }


        /**
         * parquet file
         */
        peopleDF.write().parquet("people.parquet");

        /**
         * 对parquet文件做些简单的操作
         *
         */
        System.out.println("=== Data source: Parquet File ===");

        Dataset<Row> parquet = sqlContext.read().parquet("people.parquet");
        parquet.show();

        parquet.registerTempTable("parquetFile");

        Dataset<Row> teenagers2 = sqlContext.sql("select name from parquetFile where age > 13 and age <= 19");

        List<String> collect = teenagers2.toJavaRDD().map(new Function<Row, String>() {
            @Override
            public String call(Row row) throws Exception {
                return "Name: " + row.getString(0);
            }
        }).collect();

        for (String name : collect) {
            System.out.println(name);
        }

    }
}
