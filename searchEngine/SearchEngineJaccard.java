package searchEngine;

import searchEngine.utils.BagOfWords;
import searchEngine.utils.Vocabular;

public class SearchEngineJaccard extends SearchEngineImpl {

    public SearchEngineJaccard(Vocabular vocabular) {
        super(vocabular);
    }

    @Override
    protected float computeSimilarity(BagOfWords b1, BagOfWords b2) {
        return b1.getJaccardIndice(b2);
    }
}
