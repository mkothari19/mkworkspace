package com.mk.spark;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Encoders;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

import java.util.Arrays;
import java.util.List;

public class WordCountApp {
    private ExternalResource externalResource;

    public WordCountApp(ExternalResource externalResource){
        this.externalResource=externalResource;

    }
    public Dataset<Row> process(SparkSession spark){
        List<String> inputText=  externalResource.fetchDataset();
        System.out.println("spark session "+spark);
        Dataset<String>inputDS=spark.createDataset(inputText, Encoders.bean(String.class));
        return inputDS
                .flatMap((String line) -> Arrays.asList(line.split(" ")).iterator(), Encoders.STRING())
                .groupBy("value")
                .count();
    }

}
