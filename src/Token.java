import java.util.*;

public class Token {
    public List<Integer> sentences;
    public int weight;
    public String word;

    Token(String wrd)
    {
        this.word = wrd;
        this.sentences = new ArrayList<Integer>();
    }
}
