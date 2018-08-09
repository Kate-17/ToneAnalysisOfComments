package tests;


import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.FacetField;
import org.apache.solr.client.solrj.response.QueryResponse;

import java.io.IOException;


public class ForTest {
    static double numberOfPositiveDocs = 0, numberOfNegativeDocs = 0, numberOfNeitralDocs = 0, numberOfAllDocs = 0;
    static double numberOfPositiveTokens = 0, numberOfNegativeTokens = 0, numberOfNeitralTokens = 0, numberOfAllTokens = 0;
    static int uniqueTokens = 0;
    static double probabilityOfPositiveClassForToken = 1, probabilityOfNegativeClassForToken = 1, probabilityOfNeitralClassForToken = 1;
    static double probabilityOfPositiveClass, probabilityOfNegativeClass, probabilityOfNeitralClass;
   static SolrClient testTrainingSet = new HttpSolrClient.Builder("http://localhost:8983/solr/testTrainingTwitterSet").build();
   static SolrClient testSet = new HttpSolrClient.Builder("http://localhost:8983/solr/testTwitterSet").build();

    public static void main(String[] args) throws IOException, SolrServerException {
        long posCALC = 0, negCALC = 0, neitCALC = 0;

        SolrQuery queryOfTrainingSet = new SolrQuery("*:*");
        queryOfTrainingSet.setStart(0);
        queryOfTrainingSet.setFacetLimit(-1);
        queryOfTrainingSet.setRows(10000000);
        queryOfTrainingSet.addFacetField("textClass");
        QueryResponse responseOfTrainingSet = testTrainingSet.query(queryOfTrainingSet);
        FacetField tokensFromTrainingSet = responseOfTrainingSet.getFacetField("textClass");

        if (tokensFromTrainingSet != null) {
            for (FacetField.Count count : tokensFromTrainingSet.getValues()) {
                uniqueTokens++;
            }
        }

        SolrQuery queryOfNumberForPositiveClass = new SolrQuery("class:1");
        queryOfNumberForPositiveClass.setStart(0);
        queryOfNumberForPositiveClass.setFacetLimit(-1);
        queryOfNumberForPositiveClass.addFacetField("textClass");
        QueryResponse responseOfNumberForPositiveClass = testTrainingSet.query(queryOfNumberForPositiveClass);
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
        QueryResponse responseOfNumberForNeitralClass = testTrainingSet.query(queryOfNumberForNeitralClass);
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
        QueryResponse responseOfNumberFoNegativeClass = testTrainingSet.query(queryOfNumberFoNegativeClass);
        numberOfNegativeDocs = responseOfNumberFoNegativeClass.getResults().getNumFound();
        FacetField tokensFromTrainingSetNegative = responseOfNumberFoNegativeClass.getFacetField("textClass");

        for (FacetField.Count count : tokensFromTrainingSetNegative.getValues()) {
            if (count.getCount() != 0)
                numberOfNegativeTokens++;
        }


        SolrQuery queryOfNumberOfAllDocs = new SolrQuery("*:*");
        queryOfNumberOfAllDocs.setStart(0);
        QueryResponse responseOfNumberOfAllDocs = testTrainingSet.query(queryOfNumberOfAllDocs);
        numberOfAllDocs = responseOfNumberOfAllDocs.getResults().getNumFound();

        String truth, tryOfTruth = "";
        long appearP = 0, appearNtr = 0, appearN = 0;

        SolrQuery queryPr = new SolrQuery("*:*");
        queryPr.addFacetField("id");
        queryPr.setFacetLimit(-1);
        QueryResponse responsePr = testSet.query(queryPr);
        FacetField tokensOfResponsePr = responsePr.getFacetField("id");




        SolrQuery queryPr2;
        QueryResponse responsePr2;
        FacetField tokensOfResponsePr2, tokensOfResponsePr2_2;
        SolrQuery queryOfTrainingSet2;
        QueryResponse responseOfTrainingSet2;
        int m = 0, eq = 0, errors = 0;

        probabilityOfPositiveClass = (double) numberOfPositiveDocs / numberOfAllDocs;
        probabilityOfNegativeClass = (double) numberOfNegativeDocs / numberOfAllDocs;
        probabilityOfNeitralClass = (double) numberOfNeitralDocs / numberOfAllDocs;

        if (tokensOfResponsePr != null) {
            for (FacetField.Count count : tokensOfResponsePr.getValues()) {
                probabilityOfPositiveClassForToken = 1;
                probabilityOfNeitralClassForToken = 1;
                probabilityOfNegativeClassForToken = 1;
                queryPr2 = new SolrQuery("id:" + (char) 34 + count.getName() + (char) 34).addFacetField("textClass").setFacetLimit(-1);
                responsePr2 = testSet.query(queryPr2);
                truth = responsePr2.getResults().get(0).getFieldValues("class").toString();

                tokensOfResponsePr2 = responsePr2.getFacetField("textClass");
                if (tokensOfResponsePr2 != null) {
                    for (FacetField.Count count2 : tokensOfResponsePr2.getValues()) {
                        if (count2.getCount() != 0) {
                            queryOfTrainingSet2 = new SolrQuery("textClass:" + count2.getName()).addFacetField("class").setFacetLimit(-1);
                            ;
                            responseOfTrainingSet2 = testTrainingSet.query(queryOfTrainingSet2);
                            tokensOfResponsePr2_2 = responseOfTrainingSet2.getFacetField("class");
                            for (FacetField.Count count3 : tokensOfResponsePr2_2.getValues()) {
                                if (Integer.valueOf(count3.getName())==1) { appearP=count3.getCount();}
                                else if (Integer.valueOf(count3.getName())==0) { appearNtr=count3.getCount();}
                                else if (Integer.valueOf(count3.getName())==2) { appearN=count3.getCount();}
                            }

                            probabilityOfPositiveClassForToken *= (1 + appearP) / (numberOfPositiveTokens + uniqueTokens);
                            probabilityOfNeitralClassForToken *= (1 + appearNtr) / (numberOfNeitralTokens + uniqueTokens);
                            probabilityOfNegativeClassForToken *= (1 + appearN) / (numberOfNegativeTokens + uniqueTokens);

                            if (probabilityOfPositiveClassForToken < 1.9999999999999999E-250 || probabilityOfNeitralClassForToken < 1.9999999999999999E-250 ||
                                    probabilityOfNegativeClassForToken < 1.9999999999999999E-250) {

                                probabilityOfPositiveClassForToken *= Math.pow(10, 40);
                                probabilityOfNeitralClassForToken *= Math.pow(10, 40);
                                probabilityOfNegativeClassForToken *= Math.pow(10, 40);
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
                        negCALC++;
                        tryOfTruth = "[2]";
                    } else if (probabilityOfNegativeClassForToken * probabilityOfNegativeClass < probabilityOfPositiveClassForToken * probabilityOfPositiveClass
                            & probabilityOfNeitralClassForToken * probabilityOfNeitralClass < probabilityOfPositiveClassForToken * probabilityOfPositiveClass) {
                        posCALC++;
                        tryOfTruth = "[1]";
                    } else if (probabilityOfNegativeClassForToken * probabilityOfNegativeClass < probabilityOfNeitralClassForToken * probabilityOfNeitralClass
                            & probabilityOfPositiveClassForToken * probabilityOfPositiveClass < probabilityOfNeitralClassForToken * probabilityOfNeitralClass) {
                        neitCALC++;
                        tryOfTruth = "[0]";
                    }
                    if (tryOfTruth.equals(truth)) eq++;
                    else errors++;
                    System.out.println(m++ + " CALC " + posCALC + " " + neitCALC + " " + negCALC + ", equals = " + eq + " errors = " + errors);//+" " + responsePr2.getResults().get(0));
                    tryOfTruth = "";
                }
            }
        }
    }
}
