import java.util.List;
import java.io.*;
import java.util.Scanner;

public class Main {

    public static double PercentageOfReferat = 1;
    public static int sentenceCount = 0;

    public static Sentence[] sentencesOrig = new Sentence[100];
    public static Sentence[] sentencesStem = new Sentence[100];
    public static Token[] tokens = new Token[100000];
    public static StringBuilder build = new StringBuilder();
    public static List<String> res = MyStemRunner.run();
    public static int currentSentence = 0, cur = 0;

    public static String[] source = {"и","в","не","на", "что","с","а","как","это","по","к","но","у","из","за","от","о",
            "так","для","же","все","или","бы","если","до","то","да","при","нет","чтобы","даже","ни","раз","ну","со","под",
            "много","ли","чем","надо","без","через","об","конечно","ведь","хотя","перед","между","лишь","уж","над","однако",
            "право","вообще","например","правда","про","оно","кроме","будто","среди","значит","действительно","из-за","хоть",
            "все-таки","наконец","против","наверное","ко","пусть","словно","поскольку","впрочем","либо","главное","вроде",
            "пол","ж","было","разве","чтоб","вместо","никак","спасибо","зато","видимо","кажется","ибо","лучше","б","пожалуйста",
            "ради","сквозь","мимо","наоборот","во-первых","мол","кино","собственно","благодаря","пост","пожалуй","есть",
            "из-под","тем","неужели","случайно","ой","напротив","вероятно","во-вторых","спустя","себе","помимо","путем",
            "вне","плюс","обо","наверно","э","насчет","включая","наверняка","мм","безусловно","увы","следовательно","де",
            "благо","якобы","верно","ага","ох","по-видимому","несомненно","вернее","толк","изо","таки","эх","че","словом",
            "нежели","дескать","эй","вопреки","вице","вследствие","минус","стук","передо","ан","аж","угу","марш","пускай",
            "дабы","ай","посредством","ха","меж","бывало","небось","ы","ну-ка","в-третьих","ура","коли","ввиду","единственно",
            "ха-ха","поди","ежели","гамма","притом","тьфу","алло","се","топ","коль","ю","ишь","близ","скрип","дак","то-то",
            "сверх","безо","пред","бум","неужто","ух","ей-богу","видео","ого","ку","воистину","авто","ль","ото","фу",
            "наподобие","му","иль","ну-ну","этак","хм","бишь","авось","оп","невесть","бесспорно","пас","аминь","уф","эдак",
            "гм","ба","чик","браво","кабы","яко","средь","подо","пардон","ава","вишь","в-четвертых","нате","постольку",
            "превыше","тик","ниче","ака","тюк","бис","хе-хе","бац","исключая","ме","ау"};

    public static void main(String[] args){

        ReadOriginalSentences();
        ReadFromMyStem();

        DeletePredlogs();
        MakeTokenMas();
        Token[] rslt = SortTokens(tokens);

        int[] sent = CountSentanceWeight(rslt);
        int[] result = SortSentanceWeight(sent);

        Output(result);
    }

    public static void ReadOriginalSentences(){
        try(FileReader reader = new FileReader("text_source.txt"))
        {
            int c;
            char chr;
            boolean ok = false;
            while((c=reader.read())!=-1){
                chr = (char)c;
                if ((chr == '.') || (chr == '?') || (chr == '!') || (chr == ';'))
                {
                    build.append(chr);
                    ok = true;
                }
                else
                {
                    build.append(chr);
                }
                if (ok)
                {
                    sentencesOrig[cur] = new Sentence(cur, build.toString().split(" "));
                    cur++;
                    sentenceCount++;
                    ok = false;
                    build.delete(0, build.length());
                }
                else
                {
                    try{
                    sentencesOrig[cur] = new Sentence(cur, build.toString().split(" "));}
                    catch (Exception e)
                    {
                        System.out.println("Ошибка. Максимум 100 предложений!");
                        System.exit(0);
                    }
                }
            }
            for (int i = 0; i < 100; i++)
            {
                if (sentencesOrig[i] == null)
                    break;
                else{
                    char w = (char)13;
                    char w2 = (char)10;
                    if ((sentencesOrig[i].words[0].indexOf(w) == 0) || (sentencesOrig[i].words[0].indexOf(w2) == 0))
                    {
                        sentencesOrig[i].words[0] = sentencesOrig[i].words[0].substring(2, sentencesOrig[i].words[0].length());
                    }
                    else
                    {
                        if (sentencesOrig[i].words[0].equalsIgnoreCase(""))
                        {
                            for (int q = 0; q < sentencesOrig[i].words.length - 1; q++)
                            {
                                sentencesOrig[i].words[q] = sentencesOrig[i].words[q + 1];
                            }
                            sentencesOrig[i].words[sentencesOrig[i].words.length - 1] = "";
                        }
                    }
                }
            }
            for (int i = 0; i < 100; i++)
            {
                if (sentencesOrig[i] == null)
                    break;
                else{
                    char w = (char)13;
                    char w2 = (char)10;
                    if ((sentencesOrig[i].words[0].indexOf(w) == 0) || (sentencesOrig[i].words[0].indexOf(w2) == 0))
                    {
                        sentencesOrig[i].words[0] = sentencesOrig[i].words[0].substring(2, sentencesOrig[i].words[0].length());
                    }
                    else
                    {
                        if (sentencesOrig[i].words[0].equalsIgnoreCase(""))
                        {
                            for (int q = 0; q < sentencesOrig[i].words.length - 1; q++)
                            {
                                sentencesOrig[i].words[q] = sentencesOrig[i].words[q + 1];
                            }
                            sentencesOrig[i].words[sentencesOrig[i].words.length - 1] = "";
                        }
                    }
                }
            }
        }
        catch(IOException ex){
            System.out.println(ex.getMessage());
        }
    }

    public static void ReadFromMyStem(){
        for (int i = 0; i < res.size(); i++)
        {
            String str = res.get(i);
            String[] d1 = str.split("s");
            StringBuilder builder = new StringBuilder();
            for (int k = 0; k < d1.length; k++)
            {
                String[] r = d1[k].split(" ");
                for (int j = 0; j < r.length; j++) {
                    int in = r[j].indexOf("{");
                    if (in != -1)
                        r[j] = r[j].substring(in);
                    r[j] = r[j].replaceAll("[{}]", "").replaceAll("(\\|[^ ]+)", "");
                    if ((j == r.length - 2) && !(r[j].contains(".")) && (r[j + 1].contains("\\")))
                        r[j] = r[j] + ".";
                }
                for (int j = 0; j < r.length; j++) {
                    builder.append(r[j]);
                    builder.append(" ");
                }
                d1[k] = builder.toString().substring(0, builder.length() - 1);
                d1[k] = d1[k].replace("\\","");
                builder.delete(0, builder.length());
            }
            for (int j = 0; j < d1.length; j++) {
                builder.append(d1[j]);
                builder.append(" ");
            }
            res.set(i, builder.toString().substring(0, builder.length() - 1));

            for (int i1 = 0; i1 < d1.length; i1++)
            {
                sentencesStem[currentSentence] = new Sentence(currentSentence, d1[i1].split(" "));
                currentSentence++;
            }

        }
    }

    public static void DeletePredlogs(){
        for (int i = 0; i < sentencesStem.length; i++)
        {
            if (sentencesStem[i] == null)
                break;
            for (int j = 0; j < sentencesStem[i].words.length; j++)
            {
                if (checkIfPredlog(sentencesStem[i].words[j]))
                {
                    for (int q = j; q < sentencesStem[i].words.length - 1; q++)
                    {
                        sentencesStem[i].words[q] = sentencesStem[i].words[q + 1];
                    }
                    sentencesStem[i].words[sentencesStem[i].words.length - 1] = "";
                }
            }
        }
    }

    public static boolean checkIfPredlog(String str){
        for (int i = 0; i < 242; i++)
        {
            if (source[i].equalsIgnoreCase(str.toLowerCase()))
                return true;
        }
        return false;
    }

    public static void MakeTokenMas(){
        for (int i = 0; i < sentencesStem.length; i++)
        {
            if (sentencesStem[i] == null)
                break;
            for (int j = 0; j < sentencesStem[i].words.length; j++) {
                int pos = FindToken(sentencesStem[i].words[j]);
                if (tokens[pos] == null)
                {
                    tokens[pos] = new Token(sentencesStem[i].words[j]);
                    tokens[pos].weight = tokens[pos].weight + 1;
                    tokens[pos].sentences.add(i);
                }
                else {
                    tokens[pos].word = sentencesStem[i].words[j];
                    tokens[pos].weight = tokens[pos].weight + 1;
                    if (!tokens[pos].sentences.contains(i))
                        tokens[pos].sentences.add(i);
                }
            }
        }
    }

    public static int FindToken(String str){
        for (int i = 0; i < 100000; i++)
        {
            if (tokens[i] == null)
                return i;
            else {
                if (tokens[i].word.equalsIgnoreCase(str))
                    return i;
            }
        }
        return -1;
    }

    public static Token[] SortTokens(Token[] tkn){
        Token[] temp = new Token[tkn.length];
        int currTok = 0;

        for (int i = 0; i < tkn.length; i++)
        {
            if (tkn[i] == null)
                break;
            int currMax = -1, maxPos = -1;
            for (int j = 0; j < tkn.length; j++)
            {
                if (tkn[j] == null)
                    break;
                if (tkn[j].weight > currMax)
                {
                    currMax = tkn[j].weight;
                    maxPos = j;
                }
            }
            temp[currTok] = new Token(tkn[maxPos].word);
            temp[currTok].weight = tkn[maxPos].weight;
            temp[currTok].sentences = tkn[maxPos].sentences;
            tkn[maxPos].weight = -1;
            currTok++;
        }

        return temp;
    }

    public static int[] CountSentanceWeight(Token[] tkn){
        int[] temp = new int[sentencesStem.length];
        for (int i = 1; i < tkn.length; i++)
        {
            if (tkn[i] == null)
                break;
            for (int j = 0; j < tkn[i].sentences.size(); j++)
            {
                temp[tkn[i].sentences.get(j)] = temp[tkn[i].sentences.get(j)] + tkn[i].weight;
            }
        }

        return temp;
    }

    public static int[] SortSentanceWeight(int[] mas){
        int[] temp = new int[mas.length];
        int curPos = 0;

        for (int i = 0; i < mas.length; i++)
        {
            if (mas[i] == 0)
                break;
            int maxPos = -1, maxValue = -1;
            for (int j = 0; j < mas.length; j++) {
                if (mas[j] == 0)
                    break;
                if (mas[j] > maxValue) {
                    maxPos = j;
                    maxValue = mas[j];
                }
            }
            temp[curPos] = maxPos;
            curPos++;
            mas[maxPos] = -1;
        }

        return temp;
    }

    public static void Output(int[] snt){
        System.out.print("Введите коэффициент сжатия текста (от 0 до 100%): ");
        boolean ok = false;

        do {
            Scanner in = new Scanner(System.in);
            if (in.hasNextDouble()) {
                PercentageOfReferat = in.nextDouble();
                if ((PercentageOfReferat <= 0) || (PercentageOfReferat > 100))
                    System.out.print("Введите число от 0 до 100: ");
                else
                    ok = true;
            }
            else
                System.out.print("Введите число от 0 до 100: ");
        } while (!ok);

        int countOutput = (int)(sentenceCount * PercentageOfReferat / 100);
        if (countOutput == 0)
            countOutput = 1;

        int[] result = new int[countOutput];
        int crnt = 0;
        for (int i = 0; i < countOutput; i++)
        {
            int minValue = Integer.MAX_VALUE, minPos = -1;
            for (int j = 0; j < countOutput; j++)
            {
                if (minValue > snt[j])
                {
                    minValue = snt[j];
                    minPos = j;
                }
            }
            result[crnt] = minValue;
            crnt++;
            snt[minPos] = Integer.MAX_VALUE;
        }

        StringBuilder buildr = new StringBuilder();

        for (int i = 0; i < countOutput; i++)
        {
            for (int j = 0; j < sentencesOrig[result[i]].words.length; j++)
            {
                buildr.append(sentencesOrig[result[i]].words[j]);
                buildr.append(" ");
            }
            buildr.append("\n");
        }

        System.out.println("");

        System.out.println(buildr);
    }
}
