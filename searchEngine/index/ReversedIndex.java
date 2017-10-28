package searchEngine.index;

import searchEngine.utils.BagOfWords;

import java.util.*;

public class ReversedIndex {
    private Map<String, List<Integer>> _index;

    public ReversedIndex() {
        _index = new TreeMap<>();
    }

    public void index(Collection<BagOfWords> docs) {
        Map<String, Integer> terms;
        for (BagOfWords doc : docs) {
            terms = doc.getTerms();
            for (String term : terms.keySet()) {
                if (!_index.containsKey(term)) {
                    _index.put(term, new ArrayList<>());
                }
                _index.get(term).add(doc.getId());
            }
        }
    }

    public Set<Integer> getDocumentsIdContainingTerms(String[] terms) {
        Set<Integer> resultingDocumentsIds = new HashSet<>();
        for (String term : terms) {
            if (_index.containsKey(term)) {
                resultingDocumentsIds.addAll(_index.get(term));
            }
        }
        return resultingDocumentsIds;
    }

    @Override
    public String toString() {
        return _index.toString();
    }
}
