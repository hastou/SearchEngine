package searchEngine;

import searchEngine.utils.BagOfWords;
import searchEngine.utils.Vocabular;

public class SearchEngineDice extends SearchEngineImpl {

    public SearchEngineDice(Vocabular vocabular) {
        super(vocabular);
    }

    @Override
    protected float computeSimilarity(BagOfWords b1, BagOfWords b2) {
        return b1.getDiceIndice(b2);
    }
}
