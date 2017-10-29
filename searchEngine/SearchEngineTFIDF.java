package searchEngine;

import searchEngine.utils.BagOfWords;
import searchEngine.utils.Vocabular;

import java.util.*;


public class SearchEngineTFIDF extends SearchEngineImpl {

    // Keep document frequency in memory
    private Map<String, Double> _idf;

    public SearchEngineTFIDF(Vocabular vocabular) {
        super(vocabular);
        _idf = new TreeMap<>();
    }

    /**
     * Compute inversed document frequency after indexation
     */
    @Override
    public void indexDatabase() {
        super.indexDatabase();
        int corpusSize = corpus.size();
        _vocabular.addCorpus(getDatabase());
        for (String term : _vocabular.get_terms().keySet()) {
            int df = _reversedIndex.getDocumentsIdContainingTerms(new String[]{term}).size();
            double idf = Math.log10(((double) corpusSize) / ((double) df));
            _idf.put(term, idf);
        }
    }

    private double computeCosineSimilarity(Vector<Double> a, Vector<Double> b) {
        double dotProduct = 0.0;
        double normA = 0.0;
        double normB = 0.0;
        for (int i = 0; i < a.size(); i++) {
            dotProduct += a.get(i) * b.get(i);
            normA += a.get(i) * a.get(i);
            normB += b.get(i) * b.get(i);
        }
        return (dotProduct / (Math.sqrt(normA) * Math.sqrt(normB)));
    }

    @Override
    protected double computeSimilarity(BagOfWords b1, BagOfWords b2) {

        if (b1.getWeights().size() == 0) {
            b1.computeWeights(_idf);
        }

        if (b2.getWeights().size() == 0) {
            b2.computeWeights(_idf);
        }

        if (b1.getId() == 1) {
//            System.out.println("Doc 1 : " + b1.getWeights());
//            System.out.println("Query : " + b2.getWeights());
        }


        Vector<Double> a = new Vector<>();
        Vector<Double> b = new Vector<>();

        Set<String> terms = new HashSet<>();
        terms.addAll(b2.getTerms().keySet());
        terms.addAll(b1.getTerms().keySet());

        for (String term : terms) {
            a.add(b1.getWeight(term));
            b.add(b2.getWeight(term));
        }

//        System.out.println(b1.getId() + " : " + computeCosineSimilarity(a, b));

        return computeCosineSimilarity(a, b);
    }
}
