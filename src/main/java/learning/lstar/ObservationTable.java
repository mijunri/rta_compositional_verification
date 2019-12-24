package learning.lstar;

import learning.Membership;
import learning.RTAMembership;
import rta.*;

import java.util.*;

public class ObservationTable {
    private String name;
    private Set<String> alphabets;
    private List<TimeWord> sigma;
    private List<TimeWords> suffixes;
    private Set<TimeWords> s;
    private Set<TimeWords> r;
    private Map<TimeWords, Map<TimeWords,Boolean>> answers;
    private Membership membership;
    private List<TimeWords> unConsistentCouple = new ArrayList<>();
    private TimeWord key = null;
    private Set<TimeWord> nextWords;
    private TimeWords unEvidWords;

    public ObservationTable(Set<String> alphabets,Membership membership,String name) {
        this.alphabets = alphabets;
        this.membership = membership;
        this.name = name;
        answers = new HashMap<>();
        initSigma();
        nextWords = new HashSet<>();
        for(TimeWord w:sigma){
            nextWords.add(w);
        }
        init();
        fillTable();
        while (!isPrepared()){
            if(!isClosed()){
                makeClosed();
            }
            if(!isConsistent()){
                makeConsistent();
            }
            if(!isEvidClosed()){
                makeEvidClosed();
            }
        }
    }


    private void initSigma(){
        sigma = new ArrayList<>();
        for(String action:alphabets){
            TimeWord word = new TimeWord(action,0);
            sigma.add(word);
        }
    }

    private void init(){
        this.suffixes = new ArrayList<>();
        suffixes.add(TimeWords.EMPTY_WORDS);
        s = new HashSet<>();
        s.add(TimeWords.EMPTY_WORDS);
        r = new HashSet<>();
        for(TimeWord word:sigma){
            r.add(TimeWordsUtil.concat(TimeWords.EMPTY_WORDS,word));
        }
    }

    public void fillTable(){
        fillTable(s);
        fillTable(r);
    }

    private void fillTable(Set<TimeWords> set){
        for(TimeWords sWords:set){
            Map<TimeWords,Boolean> map = answers.getOrDefault(sWords,new HashMap<>());
            for(TimeWords suffixWords:suffixes){
                if(!map.containsKey(suffixWords)){
                    map.put(suffixWords,membership.answer(TimeWordsUtil.concat(sWords,suffixWords)));
                }
            }
            answers.put(sWords,map);
        }
    }

    public Set<String> getAlphabets() {
        return alphabets;
    }

    public boolean isPrepared(){
        return isClosed()&&isConsistent()&&isEvidClosed();
    }

    public boolean isClosed() {
        Set<String> sStrings = getAnswer(s);
        Set<String> rStrings = getAnswer(r);
        return sStrings.containsAll(rStrings);
    }

    public void makeClosed(){
        Set<String> sStrings = getAnswer(s);
        for(TimeWords words:r){
            String tuple = getTuple(words);
            if(!sStrings.contains(tuple)){
                s.add(words);
                r.remove(words);
                for(TimeWord word:sigma){
                    TimeWords newWords = TimeWordsUtil.concat(words,word);
                    if(!s.contains(newWords) && !r.contains(newWords)){
                        r.add(newWords);
                    }
                }
                break;
            }
        }
        fillTable(r);
    }

    public boolean isConsistent() {
        Set<TimeWords> sr = new HashSet<>();
        sr.addAll(s);
        sr.addAll(r);
        List<TimeWords> list = new ArrayList<>(sr);
        for(int i = 0; i < list.size(); i++){
            String s = getTuple(list.get(i));
            for(int j = i + 1; j < list.size(); j ++){
                if(s.equals(getTuple(list.get(j)))){
                    for(TimeWord w: nextWords){
                        TimeWords words1 = TimeWordsUtil.concat(list.get(i),w);
                        TimeWords words2 = TimeWordsUtil.concat(list.get(j),w);
                        String tuple1 = getTuple(words1);
                        String tuple2 = getTuple(words2);
                        if(tuple1!=null && tuple2!=null && !tuple1.equals(tuple2)){
                            unConsistentCouple.add(list.get(i));
                            unConsistentCouple.add(list.get(j));
                            key = w;
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }

    public void makeConsistent(){
        TimeWords words1 = unConsistentCouple.get(0);
        TimeWords words2 = unConsistentCouple.get(1);
        TimeWords target1 = TimeWordsUtil.concat(words1,key);
        TimeWords target2 = TimeWordsUtil.concat(words2,key);
        Map<TimeWords,Boolean> map1 = answers.get(target1);
        Map<TimeWords,Boolean> map2 = answers.get(target2);
        for(TimeWords w:suffixes){
            if(!map1.get(w).equals(map2.get(w))){
                TimeWords words = TimeWordsUtil.concat(key,w);
                suffixes.add(words);
                unConsistentCouple = new ArrayList<>();
                key = null;
                break;
            }
        }
        fillTable();
    }


    public boolean isEvidClosed() {
        for(TimeWords sPrefix:s){
            for(TimeWords suffix:suffixes){
                TimeWords words= TimeWordsUtil.concat(sPrefix,suffix);
                if(!s.contains(words) && !r.contains(words)){
                    unEvidWords = words;
                    return false;
                }
            }
        }
        return true;
    }


    public void makeEvidClosed(){
        r.add(unEvidWords);
        fillTable(r);
    }

    private Set<String> getAnswer(Set<TimeWords> set){
        Set<String> strings = new HashSet<>();
        if(set != null && !set.isEmpty()){
            for(TimeWords words:set){
                strings.add(getTuple(words));
            }
        }
        return strings;
    }

    private String getTuple(TimeWords TimeWords){
        StringBuilder stringBuilder = null;
        if(!answers.containsKey(TimeWords)){
            return null;
        }else {
            stringBuilder = new StringBuilder();
            Map<TimeWords,Boolean> map = answers.get(TimeWords);
            for(TimeWords suffix:suffixes){
                stringBuilder.append(map.get(suffix));
            }
            return stringBuilder.toString();
        }
    }

    public void refine(TimeWords ce){
        Set<TimeWords> allPrefixes = TimeWordsUtil.getAllPrefixes(ce);
        for(TimeWord w:ce.getWordList()){
            nextWords.add(w);
        }
        r.addAll(allPrefixes);
        fillTable(r);
        while (!isPrepared()){
            if(!isClosed()){
                makeClosed();
            }
            if(!isConsistent()){
                makeConsistent();
            }
            if(!isEvidClosed()){
                makeEvidClosed();
            }
        }
    }

    /**
     * 生成EvidRTA
     * @return RTA
     */
    public RTA tranToEvidRTA(){
        Map<TimeWords, Location> bindMap = new HashMap<>();
        Map<String,Location> stringMap = new HashMap<>();
        List<Location> locations = new ArrayList<>();
        List<Transition> transitions = new ArrayList<>();
        Set<String> stringSet = new HashSet<>();
        int id = 1;
        //将S中的不同的row加入到状态集合中。
        for(TimeWords sWords: s){
            //获得对应元组
            String str = getTuple(sWords);
            if(!stringSet.contains(str)){
                stringSet.add(str);
                Location location = new Location(id,String.valueOf(id),false,false);
                if(sWords.equals(TimeWords.EMPTY_WORDS)){
                    location.setInit(true);
                }
                Map<TimeWords,Boolean> map = answers.get(sWords);
                if(map.get(suffixes.get(0))==true){
                    location.setAccept(true);
                }
                locations.add(location);
                bindMap.put(sWords,location);
                stringMap.put(getTuple(sWords),location);
                id++;
            }
        }
        Set<String> visitedLocation = new HashSet<>();
        //建立迁移
        for(TimeWords sWords: s){
            //遍历每一个没有visit状态
            String string = getTuple(sWords);
            if(!visitedLocation.contains(string)){
                //将遍历的状态加入visit集合。
                visitedLocation.add(string);
                for(String action: alphabets){
                    //为S中的每一个状态与每一个action获得下一个状态
                    Map<TimeWord,TimeWords> matchMap = getAllMatch(sWords,action);
                    Location sourceLocation = stringMap.get(string);
                    for(TimeWord key :matchMap.keySet()){
                        Location targetLocation = stringMap.get(getTuple(matchMap.get(key)));
                        TimeGuard timeGuard = RTAUtil.toIntegerTimeGuard(key);
                        Transition transition = new Transition(sourceLocation,targetLocation,timeGuard,action);
                        transitions.add(transition);
                    }
                }
            }
        }
        RTA evidRTA = new RTA(name,alphabets,locations,transitions);
        return evidRTA;
    }

    public RTA tranToRTA(){
        RTA evidRTA = tranToEvidRTA();
        RTA rta = RTAUtil.evidRTAToRTA(evidRTA);
        RTAUtil.refineTran(rta);
        return rta;
    }


    public Map<TimeWord,TimeWords> getAllMatch(TimeWords words, String action){
        Set<TimeWords> sr = new HashSet<>();
        sr.addAll(s);
        sr.addAll(r);
        List<TimeWords> list = new ArrayList<>(sr);
        list.remove(words);
        Map<TimeWord,TimeWords> wordsMatchMap = new HashMap<>();
        for(TimeWords w: list){
            if(RTAUtil.isSimilar(words,action,w)){
                if(!wordsMatchMap.containsKey(w.get(w.size()-1))){
                    wordsMatchMap.put(w.get(w.size()-1),w);
                }
            }
        }
        return wordsMatchMap;
    }

    public void setAlphabets(Set<String> alphabets) {
        this.alphabets = alphabets;
    }


    public List<TimeWords> getSuffixes() {
        return suffixes;
    }

    public void setSuffixes(List<TimeWords> suffixes) {
        this.suffixes = suffixes;
    }

    public Set<TimeWords> getS() {
        return s;
    }

    public void setS(Set<TimeWords> s) {
        this.s = s;
    }

    public Set<TimeWords> getR() {
        return r;
    }

    public void setR(Set<TimeWords> r) {
        this.r = r;
    }

    public Map<TimeWords, Map<TimeWords, Boolean>> getAnswers() {
        return answers;
    }

    public void setAnswers(Map<TimeWords, Map<TimeWords, Boolean>> answers) {
        this.answers = answers;
    }

    public Membership getMembership() {
        return membership;
    }

    public void setMembership(RTAMembership membership) {
        this.membership = membership;
    }

}
