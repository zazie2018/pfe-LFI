package stanford;


public class BrandSentimentScore 
{
	public static void main(String[] args) 
	{
		String text = "this smartphone is awful to say the least,"
				+ "the camera is bad "
				+ "the battery  is awful  "
				+ "the colour is ugly "
				+ "the screen is small and vague   ";
				
		NLP.init();
		NLP.computeSentiment(text);
	}
}