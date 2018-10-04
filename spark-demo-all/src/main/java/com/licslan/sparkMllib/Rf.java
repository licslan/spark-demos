package com.licslan.sparkMllib;

import org.apache.spark.ml.Pipeline;
import org.apache.spark.ml.PipelineModel;
import org.apache.spark.ml.PipelineStage;
import org.apache.spark.ml.classification.RandomForestClassificationModel;
import org.apache.spark.ml.classification.RandomForestClassifier;
import org.apache.spark.ml.evaluation.MulticlassClassificationEvaluator;
import org.apache.spark.ml.feature.*;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

/**
 * Created by Administrator on 2018/10/2.
 */

public class Rf {
    /**
     *
     * Random forests are a popular family of classification and regression methods.
     * More information about the spark.ml implementation can be found further
     * in the section on random forests.
     *  Examples
         The following examples load a dataset in LibSVM format, split it into training
         and test sets, train on the first dataset, and then evaluate on the held-out test set.
         We use two feature transformers to prepare the data; these help index categories for the
         label and categorical features, adding metadata to the DataFrame which the tree-based algorithms can recognize.

     Random forests are ensembles of decision trees. Random forests are one of the most successful
     machine learning models for classification and regression. They combine many decision trees
     in order to reduce the risk of overfitting. Like decision trees, random forests handle categorical
     features, extend to the multiclass classification setting, do not require feature scaling, and are
     able to capture non-linearities and feature interactions.

     spark.mllib supports random forests for binary and multiclass classification and for regression, using
     both continuous and categorical features. spark.mllib implements random forests using the existing decision
     tree implementation. Please see the decision tree guide for more information on trees.

     http://spark.apache.org/docs/latest/ml-classification-regression.html
     *
     */

    public void testRF(){
        SparkSession spark = SparkSession.builder()
                .appName("lcc_java_habase_local")
                .master("local[4]")
                .getOrCreate();
        // Load and parse the data file, converting it to a DataFrame.
        Dataset<Row> data = spark.read().format("libsvm").load("data/mllib/sample_libsvm_data.txt");

// Index labels, adding metadata to the label column.
// Fit on whole dataset to include all labels in index.
        StringIndexerModel labelIndexer = new StringIndexer()
                .setInputCol("label")
                .setOutputCol("indexedLabel")
                .fit(data);
// Automatically identify categorical features, and index them.
// Set maxCategories so features with > 4 distinct values are treated as continuous.
        VectorIndexerModel featureIndexer = new VectorIndexer()
                .setInputCol("features")
                .setOutputCol("indexedFeatures")
                .setMaxCategories(4)
                .fit(data);

// Split the data into training and test sets (30% held out for testing)
        Dataset<Row>[] splits = data.randomSplit(new double[] {0.7, 0.3});
        Dataset<Row> trainingData = splits[0];
        Dataset<Row> testData = splits[1];

// Train a RandomForest model.
        RandomForestClassifier rf = new RandomForestClassifier()
                .setLabelCol("indexedLabel")
                .setFeaturesCol("indexedFeatures");

// Convert indexed labels back to original labels.
        IndexToString labelConverter = new IndexToString()
                .setInputCol("prediction")
                .setOutputCol("predictedLabel")
                .setLabels(labelIndexer.labels());

// Chain indexers and forest in a Pipeline
        Pipeline pipeline = new Pipeline()
                .setStages(new PipelineStage[] {labelIndexer, featureIndexer, rf, labelConverter});

// Train model. This also runs the indexers.
        PipelineModel model = pipeline.fit(trainingData);

// Make predictions.
        Dataset<Row> predictions = model.transform(testData);

// Select example rows to display.
        predictions.select("predictedLabel", "label", "features").show(5);

// Select (prediction, true label) and compute test error
        MulticlassClassificationEvaluator evaluator = new MulticlassClassificationEvaluator()
                .setLabelCol("indexedLabel")
                .setPredictionCol("prediction")
                .setMetricName("accuracy");
        double accuracy = evaluator.evaluate(predictions);
        System.out.println("Test Error = " + (1.0 - accuracy));

        RandomForestClassificationModel rfModel = (RandomForestClassificationModel)(model.stages()[2]);
        System.out.println("Learned classification forest model:\n" + rfModel.toDebugString());
    }

}
