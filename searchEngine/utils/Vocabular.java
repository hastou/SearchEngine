package searchEngine.utils;

import searchEngine.DocumentInfo;

import javax.print.Doc;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;
import java.util.TreeMap;
import java.util.Vector;


/**
 * Class to process DocumentInfo or String with tokenizer, stop list and stemmer
 * Permit to count terms frequency in corpus to check Zipf law or count hapax
 * Also give access to vocabular list to compute document frequency
 */
public class Vocabular {

    private Map<String, Integer> _terms;
    private Tokenizer _tokenizer;
    private StopList _stopList;
    private Stemmer _stemmer;

    public Vocabular(Tokenizer tokenizer) {
        this(tokenizer, null, null);
    }

    public Vocabular(Tokenizer tokenizer, StopList stopList) {
        this(tokenizer, stopList, null);
    }

    public Vocabular(Tokenizer tokenizer, StopList stopList, Stemmer stemmer) {
        _terms = new TreeMap<>();
        _tokenizer = tokenizer;
        _stopList = stopList;
        _stemmer = stemmer;
    }

    public void clear() {
        _terms.clear();
    }

    /**
     *
     * @param string
     * @return Tokenized terms of string processed by stop list and stemmer if configured
     */
    public String[] processString(String string) {

        String lowerCased = string.toLowerCase();
        String[] words = _tokenizer.tokenize(lowerCased);

        if (_stopList != null) {
            words = _stopList.filter(words);
        }

        if (_stemmer != null) {
            words = _stemmer.stem(words);
        }

        return words;
    }

    public String[] processDocument(DocumentInfo document) {
        return processString(document.getContent());
    }


    /**
     *
     * @param string
     * @param frequencyByTerm
     */
    public void computeTermsFrequencyInString(String string, Map<String, Integer> frequencyByTerm) {
        String[] terms = processString(string);
        for (String term : terms) {
            Integer frequency = frequencyByTerm.get(term);
            if (frequency != null) {
                frequencyByTerm.put(term, ++frequency);
            } else {
                frequencyByTerm.put(term, 1);
            }
        }
    }

    public void addCorpus(String corpusPath) {
        Vector<DocumentInfo> corpus = CollectionReader.readDatabaseFile(corpusPath);
        addCorpus(corpus);
    }

    public void addCorpus(Vector<DocumentInfo> corpus) {
        clear();
        for (DocumentInfo doc : corpus) {
            computeTermsFrequencyInString(doc.getContent(), _terms);
        }
    }

    public int size() {
        return _terms.size();
    }

    public int getHapaxCount() {
        int hapaxCount = 0;
        for (String term : _terms.keySet()) {
            if (_terms.get(term) == 1) {
                hapaxCount++;
            }
        }
        return hapaxCount;
    }

    /**
     * @param csvPath
     * Permet d'enregistrer le vocabulaire dans un csv pour procéder à une analyse externe dans un logiciel de traçage
     * de courbes (notamment pour tracer la courbe de la loi de Zipf)
     * Permet également de vérifier la bonne qualité des traitements effectués sur le corpus
     */
    public void toCSV(String csvPath) {
        try {
            PrintWriter printWriter = new PrintWriter(new FileOutputStream(csvPath, false));
            for (String term : _terms.keySet()) {
                printWriter.write(term + "," + _terms.get(term) + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        StringBuilder output = new StringBuilder();
        for (String term : _terms.keySet()) {
            output.append(term).append(" : ").append(_terms.get(term)).append("\n");
        }
        return output.toString();
    }

    public Map<String, Integer> get_terms() {
        return _terms;
    }
}
