# corpus.sinhala.wildcard.search

<H5>Installation </H5>

* clone and build https://github.com/lasandun/corpus.sinhala.wildcard.search

* Install dependancies and build project by command "mvn clean install" at project root directory

* Edit the solr.properties file as required specifying the paths and URLs.

	solrPostJarPath=/home/user/solr-4.9.0/example/exampledocs/post.jar
	solrServerURL=http://sinhala-corpus.projects.uom.lk/solr/ 
	java=java
	solrWildcardWordListPath=/home/user/words.csv
	solrWildcardXMLPath=/home/user/solr/parsedXMLDir/
	solrWildcardUploadSummaryFile=/home/user/solrUploadSummary.txt
	parsedXMLPath=/home/user/solr/parsed/
	solrWildcardEncodedLetterSeparator=a
	debug=no

* Now you are ready to use corpus.sinhala.wildcard.search.


<H5>Usage </H5>

This repository includes the source files of the controller of the Apache Solr database of the project Sinmin.
Check the high level functions at https://github.com/lasandun/corpus.sinhala.wildcard.search/blob/master/src/main/java/corpus/sinhala/wildcard/search/SolrWildCardSearch.java
