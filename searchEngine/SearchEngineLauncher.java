package searchEngine;

import gui.SearchEngineUI;
import searchEngine.utils.Stemmer;
import searchEngine.utils.StopList;
import searchEngine.utils.Tokenizer;
import searchEngine.utils.Vocabular;

public class SearchEngineLauncher {

	public static void main( String[] args ) {
		final Tokenizer tokenizer = new Tokenizer(", \n!?:-/;')([]{}.");
		final StopList stopList = new StopList("C:\\Users\\Quentin\\IdeaProjects\\moteur_de_recherche_textuel\\src\\data\\stopListEnglish.txt");
		final Stemmer stemmer = new Stemmer(Stemmer.StemmerLanguage.ENGLISH);

		final Vocabular vocabular = new Vocabular(tokenizer, stopList, stemmer);
		final SearchEngine se = new SearchEngineTFIDF(vocabular);

		
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
            	new SearchEngineUI( se );
            }
        });	
	}
}
