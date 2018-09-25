package wordfunnel;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import javax.net.ssl.HttpsURLConnection;
import java.util.*;
import java.util.stream.Collectors;
public class WordFunnel {
    public static final int ASCII_MODULAR_VALUE = 97;
    public static void main(String[] args){
        ArrayList<String> enable1WordArray = GetEnable1Text.getEnable1Text();
        Map<Character, List<String>> alphabetizedWords = enable1WordArray.stream()
                .collect(Collectors.groupingBy(elem -> elem.charAt(0)));
        ArrayList<Map<Integer, List<String>>> masterWordList = new ArrayList<>(26);
        alphabetizedWords.values().forEach(listItem -> masterWordList.add(
                listItem.stream().collect(Collectors.groupingBy(listString -> listString.length()))));
        funnel();
        System.out.println(bonus("dragoon", enable1WordArray, masterWordList) + "\n");
        System.out.println(bonus("boats", enable1WordArray, masterWordList) + "\n");
        bonus2(enable1WordArray, masterWordList);
    }
    public static void funnel(){
        String[][] wordsArray = new String[][]{
        {"leave","eave"},
        {"reset", "rest"},
        {"dragoon", "dragon"},
        {"eave", "lets"},
        {"sleet", "lets"},
        {"skiff", "ski"}
        };
        for(String[] wordSet : wordsArray){
            boolean noMatchFound = false;
            StringBuilder result;
            for(int counter = 0; counter < wordSet[0].length(); counter++){
                result = new StringBuilder(wordSet[0]);
                result.deleteCharAt(counter);
                if(result.toString().equals(wordSet[1])){
                    System.out.println("funnel(\"" + wordSet[0] + "\", \"" + wordSet[1] + "\") => true");
                    noMatchFound = false;
                    break;
                }
                noMatchFound = true;
            }
            if(noMatchFound){
                System.out.println("funnel(\"" + wordSet[0] + "\", \"" + wordSet[1] + "\") => false");
            }
            System.out.println();
        }
    }
    public static ArrayList<String> bonus(String inputWord,
            ArrayList<String> enable1WordArray, ArrayList<Map<Integer, List<String>>> masterWordList){
        ArrayList<String> wordArray = new ArrayList<>();
        wordArray.add("input word = " + inputWord);
        outerLoop: for(int counter = 0; counter < inputWord.length(); counter++){
            String result = new StringBuilder(inputWord).deleteCharAt(counter).toString();
            List<String> wordsToCompare = masterWordList.get((result.charAt(0)-ASCII_MODULAR_VALUE)).get(result.length());
            if(wordsToCompare == null){break outerLoop;}//if search for words to compare with "result" yeild nothing then break outerLoop
            for(String wordToCompare : wordsToCompare){
                if(result.equals(wordToCompare) && !(wordArray.contains(result))){
                    wordArray.add(result);
                }
            }
        }
        return wordArray;
    }
    public static void bonus2(ArrayList<String> enable1WordArray, ArrayList<Map<Integer, List<String>>> masterWordList){
        long startTime = System.currentTimeMillis();
        ArrayList<ArrayList<String>> bonus2WordArray = new ArrayList<>(28);
        for(String singleWord : enable1WordArray){
            if(bonus2WordArray.add(bonus(singleWord, enable1WordArray, masterWordList)) == true
                    && bonus2WordArray.get(bonus2WordArray.size()-1).size() != 6){
                bonus2WordArray.remove(bonus2WordArray.size()-1);
            }
        }
        bonus2WordArray.forEach(e -> System.out.println(e));
        System.out.println((System.currentTimeMillis()-startTime) + " = time for bonus2 method to complete in milliseconds");
    }
}
class GetEnable1Text{
    public static ArrayList<String> getEnable1Text(){
        URL url = null;
        HttpsURLConnection con = null;
        ArrayList<String> wordArray = null;
        try{
            url = new URL("https://raw.githubusercontent.com/dolph/dictionary/master/enable1.txt");
            InputStream is = url.openStream();
            StringBuffer sb = new StringBuffer();
            int eof;
            while((eof = is.read()) != -1){
                sb.append((char)eof);
            }
            String[] s = sb.toString().split("\n");
            wordArray = new ArrayList<>(Arrays.asList(s));
        }catch(MalformedURLException mue){}
        catch(IOException ioe){}
        return wordArray;
    }
}