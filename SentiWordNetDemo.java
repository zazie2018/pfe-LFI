package stanford;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class SentiWordNetDemo {

	private Map<String, Double> dictionary;

	public SentiWordNetDemo(String pathToSWN) throws IOException {
		// This is our main dictionary representation
		dictionary = new HashMap<String, Double>();

		// From String to list of doubles.
		HashMap<String, HashMap<Integer, Double>> tempDictionary = new HashMap<String, HashMap<Integer, Double>>();

		BufferedReader swnDataFile = null;
		try {
			swnDataFile = new BufferedReader(new FileReader(pathToSWN));
			int lineNumber = 0;

			String line;
			while ((line = swnDataFile.readLine()) != null) {
				lineNumber++;

				// If it's a comment, skip this line.
				if (!line.trim().startsWith("#")) {
					// We use tab separation
					String[] data = line.split("\t");
					String wordTypeMarker = data[0];

					// Example line:
					// POS ID PosS NegS SynsetTerm#sensenumber Desc
					// a 00009618 0.5 0.25 spartan#4 austere#3 ascetical#2
					// ascetic#2 practicing great self-denial;...etc

					// Is it a valid line? Otherwise, through exception.
					if (data.length != 6) {
						throw new IllegalArgumentException("Incorrect tabulation format in file, line: " + lineNumber);
					}

					// Calculate synset score as score = PosS - NegS
					Double synsetScore = Double.parseDouble(data[2]) - Double.parseDouble(data[3]);

					// Get all Synset terms
					String[] synTermsSplit = data[4].split(" ");

					// Go through all terms of current synset.
					for (String synTermSplit : synTermsSplit) {
						// Get synterm and synterm rank
						String[] synTermAndRank = synTermSplit.split("#");
						String synTerm = synTermAndRank[0] + "#" + wordTypeMarker;

						int synTermRank = Integer.parseInt(synTermAndRank[1]);
						// What we get here is a map of the type:
						// term -> {score of synset#1, score of synset#2...}

						// Add map to term if it doesn't have one
						if (!tempDictionary.containsKey(synTerm)) {
							tempDictionary.put(synTerm, new HashMap<Integer, Double>());
						}

						// Add synset link to synterm
						tempDictionary.get(synTerm).put(synTermRank, synsetScore);
					}
				}
			}

			// Go through all the terms.
			for (Map.Entry<String, HashMap<Integer, Double>> entry : tempDictionary.entrySet()) {
				String word = entry.getKey();
				Map<Integer, Double> synSetScoreMap = entry.getValue();

				// Calculate weighted average. Weigh the synsets according to
				// their rank.
				// Score= 1/2*first + 1/3*second + 1/4*third ..... etc.
				// Sum = 1/1 + 1/2 + 1/3 ...
				double score = 0.0;
				double sum = 0.0;
				for (Map.Entry<Integer, Double> setScore : synSetScoreMap.entrySet()) {
					score += setScore.getValue() / (double) setScore.getKey();
					sum += 1.0 / (double) setScore.getKey();
				}
				score /= sum;
				dictionary.put(word, score);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (swnDataFile != null) {
				swnDataFile.close();
			}
		}

	}

	public double extract(String word, String pos) {
		if (dictionary.get(word + "#" + pos) != null)
			return dictionary.get(word + "#" + pos);
		else
			return 0.0;
	}

	public double extract(String token) {
		if (dictionary.get(token) != null)
			return dictionary.get(token);
		else
			return 0.0;
	}

	
	public static void main(String[] args) throws IOException {
		String pathToSWN = "C:/Users/Helmi/eclipse-workspace/stanford/sentiword.txt";
		SentiWordNetDemo sentiWordNet = new SentiWordNetDemo(pathToSWN);
		//*******Entrer le text d'analyse
		Scanner sc = new Scanner(System.in);
		System.out.println("Taper le text à analyser");
		String testString = sc.nextLine();
		
		//String testString = "I like this smartphone like my brother , the camera is great #lovely @samsung ";
		// String testString = "the phone is awful , the camera is bad , the battery is
		// garbage, i hate it ";

		//********Preprocessing et tag 
		PosTagger tagger = new PosTagger();//To think about coreference and dependancy
		String[] tokens = tagger.convertToSimpleTags(tagger.tagString(testString)).split(" ");
		
		Double finalScore = 0.0;
		for (String token : tokens) {
			//Attribuer un score pour chaque mot
			Double score = sentiWordNet.extract(token);
			//sommer les scores pour tout la phrase
			finalScore += score;//We have to sum for each aspect
			System.out.println(token + " ->" + score);
		}
		System.out.println("final score " + finalScore);
	}
}