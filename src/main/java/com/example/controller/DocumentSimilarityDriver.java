package com.example.controller;

import com.example.mapper.DocumentSimilarityMapper;
import com.example.reducer.DocumentSimilarityReducer;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class DocumentSimilarityDriver {

    public static void main(String[] args)
            throws Exception {

        if (args.length != 2) {
            System.err.println(
                    "Usage: DocumentSimilarityDriver <input> <output>");
            System.exit(1);
        }

        Configuration conf =
                new Configuration();

        Job job =
                Job.getInstance(conf,
                        "Document Similarity");

        job.setJarByClass(
                DocumentSimilarityDriver.class);

        // Set Mapper & Reducer
        job.setMapperClass(
                DocumentSimilarityMapper.class);

        job.setReducerClass(
                DocumentSimilarityReducer.class);

        // Output types
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(Text.class);

        // Input & Output paths
        FileInputFormat.addInputPath(
                job, new Path(args[0]));

        FileOutputFormat.setOutputPath(
                job, new Path(args[1]));

        System.exit(
                job.waitForCompletion(true)
                        ? 0 : 1
        );
    }
}