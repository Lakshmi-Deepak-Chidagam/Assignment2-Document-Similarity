package com.example.mapper;

// imports
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.Mapper;
import java.io.IOException;

public class DocumentSimilarityMapper
        extends Mapper<LongWritable, Text, Text, Text> {

    @Override
    protected void map(LongWritable key, Text value, Context context)
            throws IOException, InterruptedException {

        // Read one line (one document)
        String line = value.toString().trim();

        if (line.isEmpty()) return;

        // Split: DocumentID + Content
        String[] parts = line.split("\\s+", 2);

        if (parts.length < 2) return;

        String docId = parts[0];
        String content = parts[1]
                .toLowerCase()
                .replaceAll("[^a-z ]", "");

        String[] words = content.split("\\s+");

        // Emit: docId -> word
        for (String word : words) {
            if (!word.isEmpty()) {
                context.write(
                        new Text(docId),
                        new Text(word)
                );
            }
        }
    }
}