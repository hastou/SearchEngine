package searchEngine.utils;

import searchEngine.DocumentInfo;

import java.util.Vector;

public class Tests {

    final Tokenizer tokenizer = new Tokenizer(",\\ -\n!?:/;')([]$€{}.\"");
    final StopList stopList = new StopList("C:\\Users\\Quentin\\IdeaProjects\\moteur_de_recherche_textuel\\src\\data\\stopListEnglish.txt");
    final Stemmer stemmer = new Stemmer(Stemmer.StemmerLanguage.ENGLISH);


    public static void main(String[] args) {
        Tests tests = new Tests();

    }

    public Tests() {

        String[] corpusToTest = {"cacm", "cran", "med"};
        vocabularTest(corpusToTest);

    }

    private void vocabularTest(String[] corpusToTest) {
        Vocabular vocabular = new Vocabular(tokenizer, stopList, stemmer);

        for (String corpus : corpusToTest) {
            vocabular.clear();
            vocabular.addCorpus("data/" + corpus + ".trec");
            vocabular.toCSV("data/" + corpus + ".csv");
            System.out.println("Hapax count : " + corpus + " : " + vocabular.getHapaxCount());
        }

        for (String corpus : corpusToTest) {
            Vector<DocumentInfo> documentInfos = CollectionReader.readDatabaseFile("data/" + corpus + ".trec");
            BagOfWords bagOfWords = new BagOfWords(documentInfos.get(45), vocabular);
            System.out.println("Bag Of Word sample from " + corpus + " : ");
            System.out.println(bagOfWords);
        }
    }
}
