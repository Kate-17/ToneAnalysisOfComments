package NaiveBayes;

import javaFX.Controller;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.FacetField;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrInputDocument;

import java.io.IOException;

public class Classification {

    static double numberOfPositiveDocs = 0, numberOfNegativeDocs = 0, numberOfNeitralDocs = 0, numberOfAllDocs = 0;
    static double numberOfPositiveTokens = 0, numberOfNegativeTokens = 0, numberOfNeitralTokens = 0, numberOfAllTokens = 0;
    static int uniqueTokens = 0;
    static double probabilityOfPositiveClassForToken = 1, probabilityOfNegativeClassForToken = 1, probabilityOfNeitralClassForToken = 1;
    static double probabilityOfPositiveClass, probabilityOfNegativeClass, probabilityOfNeitralClass;
    static SolrClient solrForComment = new HttpSolrClient.Builder("http://localhost:8983/solr/commentSet").build();
    static SolrClient solrForTrainingSet = new HttpSolrClient.Builder("http://localhost:8983/solr/trainingSet").build();
    public static int n, p, ntr;
    static long appearOfPositive, appearOfNeitral, appearOfNegative;


    public static void addComment(String textOriginal, String textOriginalForTokens, String videoId, String authorDisplayName, String idComment,
                                  String likeCount, String authorChannelUrl, String authorChannelId) throws Exception {
        if (Controller.flag == false) {
            infoFromTeachingSet();
            n=0;p=0;ntr=0;
            Controller.flag = true;
        }

        probabilityOfPositiveClassForToken = 1;
        probabilityOfNegativeClassForToken = 1;
        probabilityOfNeitralClassForToken = 1;
        SolrInputDocument doc = new SolrInputDocument();
        doc.addField("textOriginal", textOriginal);
        doc.addField("textOriginalTokens", textOriginalForTokens);
        doc.addField("videoId", videoId);
        doc.addField("authorDisplayName", authorDisplayName);
        doc.addField("idComment", idComment);
        doc.addField("likeCount", likeCount);
        doc.addField("authorChannelUrl", authorChannelUrl);
        doc.addField("authorChannelId", authorChannelId);
        solrForComment.add(doc);
        solrForComment.commit();
        doc.clear();

        SolrQuery queryOfLastComment = new SolrQuery("idComment:" + (char) 34 + idComment + (char) 34);
        queryOfLastComment.setRows(1);
        queryOfLastComment.setStart(0);
        queryOfLastComment.setFacetLimit(-1);
        queryOfLastComment.addFacetField("textOriginalTokens");

        QueryResponse responseOfLastComment = solrForComment.query(queryOfLastComment);
        FacetField tokensFromComment = responseOfLastComment.getFacetField("textOriginalTokens");

        FacetField tokensOfResponsePr2;

        if (tokensFromComment != null) {
            appearOfPositive = 0;
            appearOfNeitral = 0;
            appearOfNegative = 0;
            for (FacetField.Count count : tokensFromComment.getValues()) {
                if (count.getCount() != 0) {
                    SolrQuery queryOfTrainingSet2 = new SolrQuery("textClass:" + count.getName()).addFacetField("class").setFacetLimit(-1);

                    QueryResponse responseOfTrainingSet2 = solrForTrainingSet.query(queryOfTrainingSet2);
                    tokensOfResponsePr2 = responseOfTrainingSet2.getFacetField("class");
                    for (FacetField.Count count3 : tokensOfResponsePr2.getValues()) {
                        if (Integer.valueOf(count3.getName()) == 1) {
                            appearOfPositive = count3.getCount();
                        } else if (Integer.valueOf(count3.getName()) == 0) {
                            appearOfNeitral = count3.getCount();
                        } else if (Integer.valueOf(count3.getName()) == 2) {
                            appearOfNegative = count3.getCount();
                        }
                    }
                    probabilityOfPositiveClassForToken *= (1 + appearOfPositive) / (numberOfPositiveTokens + uniqueTokens);
                    probabilityOfNeitralClassForToken *= (1 + appearOfNeitral) / (numberOfNeitralTokens + uniqueTokens);
                    probabilityOfNegativeClassForToken *= (1 + appearOfNegative) / (numberOfNegativeTokens + uniqueTokens);
                    if (probabilityOfPositiveClassForToken < 1.9999999999999999E-250 || probabilityOfNeitralClassForToken < 1.9999999999999999E-250 ||
                            probabilityOfNegativeClassForToken < 1.9999999999999999E-250) {

                        probabilityOfPositiveClassForToken *= Math.pow(10, 20);
                        probabilityOfNeitralClassForToken *= Math.pow(10, 20);
                        probabilityOfNegativeClassForToken *= Math.pow(10, 20);
                    } else if (probabilityOfPositiveClassForToken > 0 || probabilityOfNeitralClassForToken > 0 ||
                            probabilityOfNegativeClassForToken > 0) {
                        probabilityOfPositiveClassForToken *= Math.pow(10, -50);
                        probabilityOfNeitralClassForToken *= Math.pow(10, -50);
                        probabilityOfNegativeClassForToken *= Math.pow(10, -50);
                    }
                }
            }

            if (probabilityOfPositiveClassForToken * probabilityOfPositiveClass < probabilityOfNegativeClassForToken * probabilityOfNegativeClass
                    & probabilityOfNeitralClassForToken * probabilityOfNeitralClass < probabilityOfNegativeClassForToken * probabilityOfNegativeClass) {
                n++;
                Controller.textAreaOfNegative.appendText("Комментарий: " + textOriginal + "\n");
                Controller.textAreaOfNegative.appendText("Likes: " + likeCount + "       ");
                Controller.textAreaOfNegative.appendText("Название канала: " + authorDisplayName + "\n");
                Controller.textAreaOfNegative.appendText("URL канала: " + authorChannelUrl + "\n\n");
            } else if (probabilityOfNegativeClassForToken * probabilityOfNegativeClass < probabilityOfPositiveClassForToken * probabilityOfPositiveClass
                    & probabilityOfNeitralClassForToken * probabilityOfNeitralClass < probabilityOfPositiveClassForToken * probabilityOfPositiveClass) {
                p++;
                Controller.textAreaOfPositive.appendText("Комментарий: " + textOriginal + "\n");
                Controller.textAreaOfPositive.appendText("Likes: " + likeCount + "       ");
                Controller.textAreaOfPositive.appendText("Название канала: " + authorDisplayName + "\n");
                Controller.textAreaOfPositive.appendText("URL канала: " + authorChannelUrl + "\n\n");
            } else if (probabilityOfNegativeClassForToken * probabilityOfNegativeClass < probabilityOfNeitralClassForToken * probabilityOfNeitralClass
                    & probabilityOfPositiveClassForToken * probabilityOfPositiveClass < probabilityOfNeitralClassForToken * probabilityOfNeitralClass) {
                ntr++;
                Controller.textAreaOfNeitral.appendText("Комментарий: " + textOriginal + "\n");
                Controller.textAreaOfNeitral.appendText("Likes: " + likeCount + "       ");
                Controller.textAreaOfNeitral.appendText("Название канала: " + authorDisplayName + "\n");
                Controller.textAreaOfNeitral.appendText("URL канала: " + authorChannelUrl + "\n\n");
            }
        }
    }

    public static void infoFromTeachingSet() throws IOException, SolrServerException {
        SolrQuery queryOfTrainingSet = new SolrQuery("*:*");
        queryOfTrainingSet.setStart(0);
        queryOfTrainingSet.setRows(10000000);
        queryOfTrainingSet.setFacetLimit(-1);
        queryOfTrainingSet.addFacetField("textClass");
        QueryResponse responseOfTrainingSet = solrForTrainingSet.query(queryOfTrainingSet);
        FacetField tokensFromTrainingSet = responseOfTrainingSet.getFacetField("textClass");

        if (tokensFromTrainingSet != null) {
            for (FacetField.Count count : tokensFromTrainingSet.getValues()) {
                uniqueTokens++;
            }
        }

        SolrQuery queryOfNumberOfAllDocs = new SolrQuery("*:*");
        queryOfNumberOfAllDocs.setStart(0);
        QueryResponse responseOfNumberOfAllDocs = solrForTrainingSet.query(queryOfNumberOfAllDocs);
        numberOfAllDocs = responseOfNumberOfAllDocs.getResults().getNumFound();

        FacetField tokensFromTrainingSetAll = responseOfTrainingSet.getFacetField("textClass");

        for (FacetField.Count count : tokensFromTrainingSetAll.getValues())
            numberOfAllTokens++;

        SolrQuery queryOfNumberForPositiveClass = new SolrQuery("class:1");
        queryOfNumberForPositiveClass.setStart(0);
        queryOfNumberForPositiveClass.setFacetLimit(-1);
        queryOfNumberForPositiveClass.addFacetField("textClass");
        QueryResponse responseOfNumberForPositiveClass = solrForTrainingSet.query(queryOfNumberForPositiveClass);
        numberOfPositiveDocs = responseOfNumberForPositiveClass.getResults().getNumFound();
        FacetField tokensFromTrainingSetPositive = responseOfNumberForPositiveClass.getFacetField("textClass");

        for (FacetField.Count count : tokensFromTrainingSetPositive.getValues()) {
            if (count.getCount() != 0)
                numberOfPositiveTokens++;
        }

        SolrQuery queryOfNumberForNeitralClass = new SolrQuery("class:0");
        queryOfNumberForNeitralClass.setStart(0);
        queryOfNumberForNeitralClass.setFacetLimit(-1);
        queryOfNumberForNeitralClass.addFacetField("textClass");
        QueryResponse responseOfNumberForNeitralClass = solrForTrainingSet.query(queryOfNumberForNeitralClass);
        numberOfNeitralDocs = responseOfNumberForNeitralClass.getResults().getNumFound();
        FacetField tokensFromTrainingSetNeitral = responseOfNumberForNeitralClass.getFacetField("textClass");

        for (FacetField.Count count : tokensFromTrainingSetNeitral.getValues()) {
            if (count.getCount() != 0)
                numberOfNeitralTokens++;
        }

        SolrQuery queryOfNumberFoNegativeClass = new SolrQuery("class:2");
        queryOfNumberFoNegativeClass.setStart(0);
        queryOfNumberFoNegativeClass.setFacetLimit(-1);
        queryOfNumberFoNegativeClass.addFacetField("textClass");
        QueryResponse responseOfNumberFoNegativeClass = solrForTrainingSet.query(queryOfNumberFoNegativeClass);
        numberOfNegativeDocs = responseOfNumberFoNegativeClass.getResults().getNumFound();
        FacetField tokensFromTrainingSetNegative = responseOfNumberFoNegativeClass.getFacetField("textClass");

        for (FacetField.Count count : tokensFromTrainingSetNegative.getValues()) {
            if (count.getCount() != 0)
                numberOfNegativeTokens++;
        }

        probabilityOfPositiveClass = numberOfPositiveDocs / numberOfAllDocs;
        probabilityOfNegativeClass = numberOfNegativeDocs / numberOfAllDocs;
        probabilityOfNeitralClass = numberOfNeitralDocs / numberOfAllDocs;
    }

}
