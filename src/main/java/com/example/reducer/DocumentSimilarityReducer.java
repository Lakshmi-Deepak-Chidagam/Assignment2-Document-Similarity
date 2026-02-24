package com.example.reducer;

import java.io.IOException;
import java.util.*;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.Reducer;

public class DocumentSimilarityReducer
        extends Reducer<Text, Text, Text, Text> {

    // Store: Document -> Set of Words
    private static Map<String, Set<String>> docMap =
            new HashMap<>();

    @Override
    protected void reduce(Text key,
                          Iterable<Text> values,
                          Context context)
            throws IOException, InterruptedException {

        String docId = key.toString();
        Set<String> words = new HashSet<>();

        for (Text val : values) {
            words.add(val.toString());
        }

        docMap.put(docId, words);
    }

    @Override
    protected void cleanup(Context context)
            throws IOException, InterruptedException {

        List<String> docs =
                new ArrayList<>(docMap.keySet());

        // Compare all document pairs
        for (int i = 0; i < docs.size(); i++) {
            for (int j = i + 1; j < docs.size(); j++) {

                String d1 = docs.get(i);
                String d2 = docs.get(j);

                Set<String> s1 = docMap.get(d1);
                Set<String> s2 = docMap.get(d2);

                // Intersection
                Set<String> inter =
                        new HashSet<>(s1);
                inter.retainAll(s2);

                // Union
                Set<String> uni =
                        new HashSet<>(s1);
                uni.addAll(s2);

                double similarity =
                        (double) inter.size()
                                / uni.size();

                String result =
                        "Similarity: " +
                        String.format("%.2f",
                                similarity);

                context.write(
                        new Text(d1 + ", " + d2),
                        new Text(result)
                );
            }
        }
    }
}