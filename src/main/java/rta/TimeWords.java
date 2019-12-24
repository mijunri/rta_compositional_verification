package rta;

import java.util.ArrayList;
import java.util.List;

public class TimeWords {

    private List<TimeWord> wordList;

    public static final TimeWords EMPTY_WORDS = new TimeWords(new ArrayList<TimeWord>());

    public TimeWords(List<TimeWord> wordList) {
        this.wordList = wordList;
    }

    public List<TimeWord> getWordList(){
        return new ArrayList(wordList);
    }

    public int size(){
        return wordList.size();
    }

    public TimeWord get(int i){
        return wordList.get(i);
    }

    //获取前n个word
    public  TimeWords getFirstN(int n){
        if(n == 0){
            return TimeWords.EMPTY_WORDS;
        }
        List<TimeWord> wordList = this.getWordList();
        List<TimeWord> wordList1 = new ArrayList<>();
        for(int i = 0; i < n; i ++){
            wordList1.add(wordList.get(i));
        }
        return new TimeWords(wordList1);
    }

    @Override
    public String toString(){
        StringBuilder stringBuilder = new StringBuilder();
        for(int i = 0; i < size(); i ++){
            stringBuilder.append(get(i));
            if(i<size()-1){
                stringBuilder.append(" -> ");
            }
        }
        return stringBuilder.toString();
    }

    @Override
    public boolean equals(Object o){
        TimeWords words = (TimeWords)o;
        if(words.size() != size()){
            return false;
        }
        for(int i = 0; i < size(); i ++){
            if(!words.get(i).equals(get(i))){
                return false;
            }
        }
        return true;
    }

    @Override
    public int hashCode(){
        int hash = 0;
        for(int i = 0; i < size() ; i++){
            hash+=i*get(i).hashCode();
        }
        return hash;
    }


}

