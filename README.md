### **Assignment 2: Document Similarity using MapReduce**

#### Name: Lakshmi Deepak Chidagam

#### Student ID: 801461579

###### **Approach and Implementation :**


This project implements document similarity using Hadoop MapReduce. Each document is processed to extract unique words, and the similarity between every pair of documents is calculated using Jaccard Similarity.



The solution uses three main components:

\- Mapper: Extracts words from each document

\- Reducer: Builds word sets and computes similarity

\- Driver: Configures and runs the MapReduce job



###### **Mapper Design :**



The input to the Mapper is:

\- Key: Line offset (LongWritable)

\- Value: One line from the input file (Text)



Each line represents one document.



Example:



Document1 This is a sample document





##### **Processing :**

The Mapper:

1\. Splits the line into document ID and content

2\. Converts text to lowercase

3\. Removes punctuation

4\. Splits content into words



###### **Output :**

The Mapper emits:



<documentID, word>





Example Output:



Document1 → this

Document1 → is

Document1 → sample





##### **Purpose :**

This groups all words belonging to the same document, which helps in building word sets for each document in the Reducer.



###### **Reducer Design :**



Input

The Reducer receives:

\- Key: Document ID (Text)

\- Value: List of words belonging to that document



Example:



Document1 → \[this, is, sample, document]





##### **Processing :**

The Reducer:

1\. Stores each document and its unique words in a HashSet

2\. Builds a map: Document → Set of Words

3\. Compares every pair of documents

4\. Calculates intersection and union of word sets

5\. Computes Jaccard Similarity



Jaccard Similarity Formula :



Similarity = |A ∩ B| / |A ∪ B|



Where:

\- A and B are word sets of two documents

\- ∩ is intersection

\- ∪ is union



##### **Output :**

The Reducer emits:



<Document1, Document2 Similarity: value>



Example:



Document1, Document2 Similarity: 0.56



##### **Overall Data Flow :**



1\. Input file is stored in HDFS

2\. Mapper reads each line and emits document-word pairs

3\. Shuffle and Sort groups values by document ID

4\. Reducer builds word sets and computes similarity

5\. Output is written to HDFS



Flow:



Input → Mapper → Shuffle/Sort → Reducer → Output



##### **Setup and Execution :**



1\. Start Hadoop Cluster



```docker compose up -d```

2\. Build the Code

```mvn clean package```

3\. Copy JAR to Docker Container

```docker cp target/DocumentSimilarity-0.0.1-SNAPSHOT-0.0.1-SNAPSHOT.jar \\```

```resourcemanager:/opt/hadoop-3.2.1/share/hadoop/mapreduce/```

4\. Copy Dataset to Docker Container

```docker cp shared-folder/input/data/input.txt \\```

```resourcemanager:/opt/hadoop-3.2.1/share/hadoop/mapreduce/```

5\. Connect to Docker Container

```docker exec -it resourcemanager /bin/bash```

```cd /opt/hadoop-3.2.1/share/hadoop/mapreduce/```

6\. Set Up HDFS



Create directory:



```hadoop fs -mkdir -p /input/data```



Upload file:



```hadoop fs -put input.txt /input/data```

7\. Execute MapReduce Job

```hadoop jar DocumentSimilarity-0.0.1-SNAPSHOT-0.0.1-SNAPSHOT.jar \\```

```com.example.controller.DocumentSimilarityDriver \\```

```/input/data/input.txt/output1 ```



Note: If output directory already exists, a new folder name (output1/output2) was used.



8\. View Output

```hadoop fs -cat /output2/\*```

9\. Copy Output to Local System



From HDFS to container:



```hdfs dfs -get /output2 /opt/hadoop-3.2.1/share/hadoop/mapreduce/```



Exit container:



```exit```



Copy to local machine:



```docker cp resourcemanager:/opt/hadoop-3.2.1/share/hadoop/mapreduce/output2\\```

```shared-folder/output/```

10\. Commit and Push

```git add .```

```git commit -m "Completed Assignment 2"```

```git push```

Challenges and Solutions

1\. Output Directory Already Exists



Problem:

Hadoop throws an error if the output directory already exists.



Solution:

A new output directory (output1/output2) was used.



2\. Implementing Mapper, Reducer, and Driver



Problem:

Initially, the project contained empty template files.



Solution:

The Mapper, Reducer, and Driver classes were implemented by following Hadoop MapReduce architecture.



3\. Debugging MapReduce Jobs



Problem:

Debugging distributed jobs is difficult.



Solution:

Small datasets were used for testing, and logs were analyzed.



4\. Managing Docker and HDFS



Problem:

Understanding file movement between local system, container, and HDFS.



Solution:

Commands were practiced step-by-step to ensure proper data flow.


Input :



Document1 This is a sample document containing words

Document2 Another document that also has words

Document3 Sample text with different words



Obtained Output :



Document3, Document2	Similarity: 0.10

Document3, Document1	Similarity: 0.20

Document2, Document1	Similarity: 0.18

