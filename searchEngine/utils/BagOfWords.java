package searchEngine.utils;

import searchEngine.DocumentInfo;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class BagOfWords {
    private final Vocabular _vocabular;
    private Map<String, Integer> _terms;
    private Map<String, Double> _weights;
    private int _id;

    public BagOfWords(DocumentInfo document, Vocabular vocabular) {
        _vocabular = vocabular;
        _terms = new TreeMap<>();
        _weights = new TreeMap<>();
        _id = document.getId();
        _vocabular.computeTermsFrequencyInString(document.getContent(), _terms);
    }

    public BagOfWords(String documentContent, Vocabular vocabular) {
        _vocabular = vocabular;
        _terms = new TreeMap<>();
        _weights = new TreeMap<>();
        _id = 0;
        _vocabular.computeTermsFrequencyInString(documentContent, _terms);
    }

    public String toString() {
        StringBuilder output = new StringBuilder();
        for (String term : _terms.keySet()) {
            output.append(term).append(" : ").append(_terms.get(term)).append("\n");
        }
        return output.toString();
    }

    public int getTermCount(String term) {
        return _terms.getOrDefault(term, 0);
    }

    public Map<String, Integer> getTerms() {
        return _terms;
    }

    public int getId() {
        return _id;
    }

    public int getSize() {
        return _terms.size();
    }


    public float getDiceIndice(BagOfWords bagOfWords) {
        int commonTermsCount = 0;
        int totalTermsCount = bagOfWords.getSize() + this.getSize();

        for (String term : _terms.keySet()) {
            if (bagOfWords.getTermCount(term) != 0) {
                commonTermsCount++;
            }
        }

        return ((float)2*commonTermsCount) / (float) totalTermsCount;
    }


    public float getJaccardIndice(BagOfWords bagOfWords) {
        int commonTermsCount = 0;
        Set<String> union = new HashSet<>();

        union.addAll(bagOfWords.getTerms().keySet());
        union.addAll(_terms.keySet());

        for (String term : _terms.keySet()) {
            if (bagOfWords.getTermCount(term) != 0) {
                commonTermsCount++;
            }
        }

        return (float) commonTermsCount / (float) union.size();
    }

    public void computeWeights(Map<String, Double> idf) {

        int documentLength = 0;
        for (String term : _terms.keySet()) {
            documentLength += _terms.get(term);
        }

        for (String term : _terms.keySet()) {
            // default zero in case query term is not in corpus

            double weight = ((double)_terms.get(term)) * idf.getOrDefault(term, 0.0);
            _weights.put(term, weight);
        }
    }

    public Map<String, Double> getWeights() {
        return _weights;
    }

    public double getWeight(String term) {
        return _weights.getOrDefault(term, 0.0);
    }
}
