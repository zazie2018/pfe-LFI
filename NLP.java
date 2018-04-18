package stanford;
import java.util.Properties;

import edu.stanford.nlp.coref.CorefCoreAnnotations;

import edu.stanford.nlp.coref.data.CorefChain;
import edu.stanford.nlp.io.*;
import edu.stanford.nlp.ling.*;
import edu.stanford.nlp.pipeline.*;
import edu.stanford.nlp.semgraph.SemanticGraph;
import edu.stanford.nlp.semgraph.SemanticGraphCoreAnnotations;
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations;
import edu.stanford.nlp.trees.*;
import edu.stanford.nlp.util.*;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.neural.rnn.RNNCoreAnnotations;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations;
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations.SentimentAnnotatedTree;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.util.CoreMap;

public class NLP {
	static StanfordCoreNLP pipeline;
    public static void init() 
    {
    	Properties props=new Properties();
	    props.setProperty("annotators","tokenize, ssplit, parse, sentiment");   
	    pipeline = new StanfordCoreNLP(props);
    }
    public static int computeSentiment(String text)
    {
 		int score = 2; // Default as Neutral. 1 = Negative, 2 = Neutral, 3 = Positive
    	String scoreStr; 
    	Annotation annotation = pipeline.process(text);
    	for(CoreMap sentence : annotation.get(CoreAnnotations.SentencesAnnotation.class))
    	{
    		scoreStr = sentence.get(SentimentCoreAnnotations.SentimentClass.class);
    		Tree tree = sentence.get(SentimentAnnotatedTree.class);
    		score = RNNCoreAnnotations.getPredictedClass(tree);
    		System.out.println(scoreStr + "\t" + score + "\t" + sentence);
    	}
    	return(score);
    }
}