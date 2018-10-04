package com.spark;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.api.java.function.PairFunction;
import org.apache.spark.api.java.function.VoidFunction;
import scala.Tuple2;

import java.util.Arrays;

/**
 * @Author: wangxc
 * @GitHub: https://github.com/vector4wang
 * @CSDN: http://blog.csdn.net/qqhjqs?viewmode=contents
 * @BLOG: http://vector4wang.tk
 * @wxid: BMHJQS
 *
 * 《巴黎圣母院》英文版的统计 用于本机学习与测试
 */
public class WordCount {
    public static void main(String[] args) {

//        SparkConf conf = new SparkConf().setMaster("local").setAppName("WordCount");
        SparkConf conf = new SparkConf().setAppName("WordCount");
        JavaSparkContext context = new JavaSparkContext(conf);

//        JavaRDD<String> javaRDD = context.textFile("D:\\data\\spark\\blsmy.txt");  -- 用于idea测试
//        JavaRDD<String> javaRDD = context.textFile("file:///mnt/data/blsmy.txt"); -- 用于集群运行(前提，运行的各节点都需要有此文件)
        JavaRDD<String> javaRDD = context.textFile("hdfs://spark-master:9000/wordcount/blsmy.txt");
        JavaRDD<String> strings = javaRDD.flatMap(new FlatMapFunction<String, String>() {
            @Override
            public Iterable<String> call(String line) throws Exception {
                return Arrays.asList(line.split(" "));
            }
        });

        JavaPairRDD<String, Integer> pairs = strings.mapToPair(new PairFunction<String, String, Integer>() {
            @Override
            public Tuple2<String, Integer> call(String s) throws Exception {
                return new Tuple2<>(s, 1);
            }
        });

        JavaPairRDD<String, Integer> reduceByKey = pairs.reduceByKey(new Function2<Integer, Integer, Integer>() {
            @Override
            public Integer call(Integer integer, Integer integer2) throws Exception {
                return integer + integer2;
            }
        });

        JavaPairRDD<Integer, String> integerStringJavaPairRDD = reduceByKey.mapToPair(new PairFunction<Tuple2<String, Integer>, Integer, String>() {
            @Override
            public Tuple2<Integer, String> call(Tuple2<String, Integer> stringIntegerTuple2) throws Exception {
                return new Tuple2<>(stringIntegerTuple2._2, stringIntegerTuple2._1);
            }
        });


        JavaPairRDD<String, Integer> mapToPair = integerStringJavaPairRDD.sortByKey(false).mapToPair(new PairFunction<Tuple2<Integer, String>, String, Integer>() {
            @Override
            public Tuple2<String, Integer> call(Tuple2<Integer, String> tuple) throws Exception {
                return new Tuple2<>(tuple._2, tuple._1);
            }
        });

        mapToPair.foreach(new VoidFunction<Tuple2<String, Integer>>() {
            @Override
            public void call(Tuple2<String, Integer> tuple) throws Exception {
                System.out.println(tuple._1 + ": " + tuple._2);
            }
        });
    }
}
