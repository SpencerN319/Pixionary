package test;
//simple object for pairing words with their urls
public class WordLink {
	
	public String word;
	public String link;
	public WordLink (String word, String link)
	{
		this.word=word;
		this.link=link;
		
	}
	
	public String getWord()
	{
		return word;
	}
	
	public String getLink()
	{
		return link;
	}

}
