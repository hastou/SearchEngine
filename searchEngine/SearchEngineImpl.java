package searchEngine;

import searchEngine.index.ReversedIndex;
import searchEngine.utils.*;

import java.util.*;
import java.util.stream.Collectors;

public abstract class SearchEngineImpl extends SearchEngine {

    protected Vocabular _vocabular;
    protected ReversedIndex _reversedIndex;
    protected Map<Integer, DocumentInfo> corpus;
    protected Map<Integer, BagOfWords> descriptors;

    public SearchEngineImpl(Vocabular vocabular) {
        _vocabular = vocabular;
        _reversedIndex = new ReversedIndex();
        corpus = new HashMap<>();
        descriptors = new HashMap<>();
    }

    @Override
    public void indexDatabase() {
        for (DocumentInfo documentInfo : this.getDatabase()) {
            corpus.put(documentInfo.id, documentInfo);
            descriptors.put(documentInfo.id, new BagOfWords(documentInfo, _vocabular));
        }
        _reversedIndex.index(descriptors.values());
    }

    /**
     *  To be implemented in child classes to personalize score compute method
     * @param b1 First of two descriptors to compare
     * @param b2 Second of two descriptors to compare
     * @return Computed score
     */
    protected abstract double computeSimilarity(BagOfWords b1, BagOfWords b2);


    @Override
    public Vector<DocumentInfo> queryDatabase(String query) {
        BagOfWords queryDescriptor = new BagOfWords(query, _vocabular);
        String[] queryTerms = queryDescriptor.getTerms().keySet().toArray(new String[queryDescriptor.getSize()]);

        Map<Integer, Double> documentsScores = new TreeMap<>();

        Set<Integer> concernedDocumentsIds = _reversedIndex.getDocumentsIdContainingTerms(queryTerms);

        // Calcul des scores de similarité
        for (Integer documentId : concernedDocumentsIds) {
            BagOfWords documentDescriptor = descriptors.get(documentId);
            documentsScores.put(documentId, computeSimilarity(documentDescriptor, queryDescriptor));
        }

        Vector<DocumentInfo> queryResult = new Vector<>();


        for (Integer documentId : sortByValue(documentsScores).keySet()) {
            if (corpus.containsKey(documentId)) {
                queryResult.add(corpus.get(documentId));
            }
        }

        return queryResult;
    }


    protected static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(Map<K, V> map) {
        return map.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(Collections.reverseOrder()))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new
                ));
    }
}
