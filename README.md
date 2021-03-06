# Hackathon using September 2016

## Datasets
**Zika sample**: sequenced with Nextseq and Miseq. Contigs generated using IDBA and spades and 
consolodiated using GARM. 




## Scripts
**contigs.php**: a web-based script to retrieves a contig, given its id in the URL, from the database and display it in a fasta format
e.g.:
```
/URL/contigs.php?id=<contigID>
```

**ContigStats.pl**: perl script to process a bam file, provides sttistics about number 
of reads mapping, coverage of contig, depth etc...
e.g.: 
```
./ContigStats.pl -b input_bowtie2.bam -contig contig.fa -out stats.txt
```

**Preprocessing.sh**: bash script to carry out bowtie2 mapping of reads of contigs and 
preprocessing stats
e.g.:
```
Preprocessing.sh file_R1.fastq file_R2.fastq contig.fa Outprefix
```
**Checks-v1.sh**: bash script to check for sequencing adapters and carry out classification of the reads using DIAMOND and BLASTN 
e.g.:
```
Checks-v1.sh ContigFile.fa Outprefix
```

**JoinTables.pl**: perl script to join the mapped read table, entropy/GC table, 
diamond table, blast table, adaptor match table
e.g.:
```
perl JoinTables.pl -adaptor checks_adapters_out -diamond checks_diamond.m8 -blast checks_blastn.m8 -entropy contigs.complex -mapped mapping.txt -out output.txt
```

**profileComplexSeq1.pl** perl script to calculate the complexity of the contigs
e.g.:
```
perl profileComplexSeq1.pl <filename.fa>
```
The output is filename.complex where columns are tab delimited in the order of:
 
seq is the header of the contigs/sequence;

gc is the complexity as measured by the GC content of the sequence;

gcs is the complexity as measured by the GC-skew of the sequence;

cpg is  the complexity as measured by CpG island content of the sequence;

cwf is the complexity as measured by Woottoon and Federhen value;

ce is  the complexity as measured by Shannon Entropy;

cz is  the complexity as measured by compression factor using Gzip;

**RandomDark.jar** Java program to randomly generate paired end DNA sequences in FASTQ format. User supplies the read length and number of reads to output. The default behaviour is to use equal probabilities (25%) for the ACGT bases and 0 probability for N bases, but the user can override these. An output stub filename needs to be provided, and stub_1.fastq and stub_2.fastq files will be written. The source code is in RandomDark.java
```
java -jar RandomDark.jar NumberOfReads[int] ReadLength[int] OutputFilenameStub[string]
java -jar RandomDark.jar NumberOfReads[int] ReadLength[int] OutputFilenameStub[string] Aprob[double] Cprob[double] Gprob[double] Tprob[double] Nprob[double]
```

**RandomVirus.jar** Java program to generate paired end DNA sequences in FASTQ format, randomly generated from a given sequence file containing host and/or viral genome(s). The input sequence file must be in FASTA single sequence line format. User supplies the read length and number of reads to output. Reads can be generated from the input sequences with equal probability (each input sequence has equal weight: eqProb=Y) or by scaling the probability to reflect sequence length (eqProb=N). An output stub filename needs to be provided, and stub_1.fastq and stub_2.fastq files will be written. The source code is in RandomVirus.java
```
java -jar RandomVirus.jar InputSequenFileName[string] NumberOfReads[int] ReadLength[int] EqualProbability[y/n] OutputFilenameStub[string]

```
**extarctDarkContigs.sh** and **DarkContigs.sql** Bash and SQL script to extract current dark sequences from the database and re-run classification using latest version of the nt and nr DB using BLASTN and DIAMOND respectively.
TODO: 
1. This script needs to be updated to update the records in MySQL if the sequences are re-classified.
2. Add this script to cronjob to re-run the classification when newer versions of the DBs are available on the server.
```
extarctDarkContigs.sh ContigFileName Outprefix
```

**Scripts to process and add data to the database**

To add to Sample table 
```
AddToSample-v1.sh sample-1.txt
```

To add to Sequence table 
```
AddToSequence-v1.sh sequence-1.txt
```

To add to Analysis table 
```
AddToAnalysis-v1.sh analysis-1.txt
```

To add to MergeContigs and KnownDark tables 
```
AddToMergeTable-v1.sh midge1_join.txt midge1-0167e2
```


**PredictClassContig.sh**  is the bash script is to predict the class (i.e. dark or light) of the contigs from their complexity (e.g. GC content, entropy, etc.) based on the machine learning. Here support vector machine (SVM) algorithm. The training set is randomly selected 70,000 contigs with their complexity, the test set is randomly selected 30,000 contigs with their complexity.  usage:
```
PredictClassContig ContigFile.fa
```
The input file is the contigs to be predicted with FASTA format.

There are five output files from this script:

(1)model_SV.csv : The key contigs in training set with their corresponding attributes values (e.g. GC content, entropy, etc.) , which are support vectors for the machine learning and prediction;

(2)predict_result.csv : The prediction results with the contig ID to be predicted and the corresponding predicted class of contigs (dark or light);

(3)FeatureCorrleation.jpg: The correlation plot among the attributes from the training dataset;

(4)SVMclassification.jpg: The 2-D plots to show the SVM classification on the training set;

(5) predict_accuracy.csv: The prediction accuracy  of test set based on the training set.




**PCAplot.r** is an R script to retrieve the different statistics from the database and generate a principal component analysis plot of PC1 against PC2. 
The plot generated is in html format and used plotly to provide interactivity of zooming in and out. To run the program, you need to provide
 an output file name (with html extension), a size cut-off and the password to the database. As the database is getting larger, it is advisable to 
 use a high-ish size cut-off.
```
Rscript PCAplot.r PCA3000.html 3000 *****
```
TODO:

1. Provide a means of pltting wither dark, light or both

2. Integrate Maha's php script for fetching the sequence

3. Add option to query a specific sample


**MDSplot.r** is an R script to retrieve the different statistis and produce and MDS plot 
of the dark contigs. It is run in a similar wayas PCAplot.r
```
Rscript MDSplot.r MDS3000.html 3000 *****
```
TODO:

1. Improve the clicking for fetching the sequence

**clustering.sh** is  bash script to carry out the cluster analysis for dark contigs using cd-hit.
```
clustering.sh DarkContigFile.fa IdentityThreshold(e.g. 0.9) Outprefix
```



