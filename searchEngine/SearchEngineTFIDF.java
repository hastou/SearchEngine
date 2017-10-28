package searchEngine;

import searchEngine.utils.BagOfWords;
import searchEngine.utils.Vocabular;

import java.util.Map;
import java.util.TreeMap;
import java.util.Vector;

public class SearchEngineTFIDF extends SearchEngineImpl {

    // Keep document frequency in memory
    private Map<String, Float> _idf;

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
        int corpusSize = descriptors.size();
        _vocabular.addCorpus(getDatabase());
        for (String term : _vocabular.get_terms().keySet()) {
            int df = _reversedIndex.getDocumentsIdContainingTerms(new String[] {term}).size();
            float idf = (float)Math.log((double)corpusSize/(double)df);
            _idf.put(term, idf);
        }
    }

    private float computeCosineSimilarity(Vector<Float> a, Vector<Float> b) {
        double dotProduct = 0.0;
        double normA = 0.0;
        double normB = 0.0;
        for (int i = 0; i < a.size(); i++) {
            dotProduct += a.get(i) * b.get(i);
            normA += Math.pow(a.get(i), 2);
            normB += Math.pow(b.get(i), 2);
        }
        return (float) (dotProduct / (Math.sqrt(normA) * Math.sqrt(normB)));
    }

    @Override
    protected float computeSimilarity(BagOfWords b1, BagOfWords b2) {

        if (b1.getWeights().size() == 0) {
            b1.computeWeights(_idf);
        }

        if (b2.getWeights().size() == 0) {
            b2.computeWeights(_idf);
        }

        float score = 0f;
        Vector<Float> a = new Vector<>();
        Vector<Float> b = new Vector<>();

        for (String term : b1.getTerms().keySet()) {
            if (b2.getTerms().containsKey(term)) {
                a.add(b1.getWeight(term));
                b.add(b2.getWeight(term));
            }
        }

        return computeCosineSimilarity(a, b);
    }
}
