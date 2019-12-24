package rta;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.*;

public class RTAUtil {
    public static Map<Integer, Location> getLocationMapFromList(List<Location> locations){
        Map<Integer,Location> map = new HashMap<>();
        for(Location l: locations){
            map.put(l.getId(),l);
        }
        return map;
    }

//    public static TimeGuard getTimeGuardFromString(String pattern){
//        pattern = pattern.trim();
//        TimeGuard timeGuard = new TimeGuard();
//        if(pattern.charAt(0) == '['){
//            timeGuard.setLeftOpen(false);
//        }else if(pattern.charAt(0) == '('){
//            timeGuard.setLeftOpen(true);
//        }else {
//            throw new RuntimeException("guard pattern error");
//        }
//        int size = pattern.length();
//        if(pattern.charAt(size-1) == ']'){
//            timeGuard.setRightOpen(false);
//        }else if(pattern.charAt(size-1) == ')'){
//            timeGuard.setRightOpen(true);
//        }else {
//            throw new RuntimeException("guard pattern error");
//        }
//        String[] numbers = pattern.split("\\,|\\[|\\(|\\]|\\)");
//        int left = Integer.parseInt(numbers[1]);
//        int right;
//        if(numbers[2].equals("+")){
//            right = RTA.MAX_TIME;
//        }else {
//            right = Integer.parseInt(numbers[2]);
//        }
//        timeGuard.setLeft(left);
//        timeGuard.setRight(right);
//        return timeGuard;
//    }

//    public static String transferToJson(RTA RTA){
//        JSONObject jsonObject = new JSONObject();
//        jsonObject.put("name",RTA.getName());
//        JSONArray stateArray = new JSONArray();
//        for(Location l:RTA.getLocations()){
//            stateArray.add(l.getId());
//        }
//        jsonObject.put("l",stateArray);
//        JSONArray sigmaArray = new JSONArray();
//        for(String s:RTA.getSigma()){
//            sigmaArray.add(s);
//        }
//        jsonObject.put("sigma",sigmaArray);
//        JSONObject tranJson = new JSONObject();
//        List<Transition> transitions = RTA.getTransitions();
//        for(int i = 0; i < transitions.size();i++){
//            JSONArray tranArray = new JSONArray();
//            tranArray.add(transitions.get(i).getSourceId());
//            tranArray.add(transitions.get(i).getAction());
//            tranArray.add(transitions.get(i).getTimeGuard().toString());
//            tranArray.add(transitions.get(i).getTargetId());
//            tranJson.put(String.valueOf(i),tranArray);
//        }
//        jsonObject.put("tran",tranJson);
//        jsonObject.put("init",RTA.getInitId());
//        JSONArray acceptArray = new JSONArray();
//        for(Location acceptLocation: RTA.getAcceptLocations()){
//            acceptArray.add(acceptLocation.getId());
//        }
//        jsonObject.put("accept",acceptArray);
//        return jsonObject.toJSONString();
//    }
//
//    public static RTA getRTAFromJson(String json){
//        JSONObject jsonObject = JSON.parseObject(json);
//        String name = getName(jsonObject);
//        Set<String> sigma = getSigma(jsonObject);
//        List<Location> locations = getLocations(jsonObject);
//        List<Transition> transitions = getTransitions(jsonObject,locations);
//        return new RTA(name,sigma,locations,transitions);
//    }
//
//    public static String getName(JSONObject RTAJsonObject){
//        return RTAJsonObject.getString("name");
//    }
//
//    public static String getName(String RTAJson){
//        JSONObject jsonObject = JSON.parseObject(RTAJson);
//        return getName(jsonObject);
//    }
//
//    public static Set<String> getSigma(JSONObject RTAJsonObject){
//        JSONArray jsonArray = RTAJsonObject.getJSONArray("sigma");
//        Set<String> sigmaSet = new HashSet<>();
//        Iterator<Object> iterator = jsonArray.iterator();
//        while (iterator.hasNext()){
//            sigmaSet.add((String)iterator.next());
//        }
//        return sigmaSet;
//    }
//
//    public static Set<String> getSigma(String RTAJson){
//        JSONObject jsonObject = JSON.parseObject(RTAJson);
//        return getSigma(jsonObject);
//    }
//
//    public static List<Location> getLocations(JSONObject jsonObject){
//        List<Location> locations = new ArrayList<>();
//        JSONArray locationArray = jsonObject.getJSONArray("l");
//        List<Integer> list = new ArrayList<>();
//        for(int i = 0; i < locationArray.size(); i ++){
//            list.add(locationArray.getIntValue(i));
//        }
//        JSONArray acceptArray = jsonObject.getJSONArray("accept");
//        Set<Integer> set = new HashSet<>();
//        for(int i = 0; i < acceptArray.size(); i ++){
//            set.add(acceptArray.getIntValue(i));
//        }
//        int initId = jsonObject.getInteger("init");
//        for(int id:list){
//            Location location = new Location(id);
//            location.setName(""+id);
//            if(set.contains(id)){
//                location.setAccept(true);
//            }else {
//                location.setAccept(false);
//            }
//            if(id == initId){
//                location.setInit(true);
//            }else {
//                location.setInit(false);
//            }
//            locations.add(location);
//        }
//        return locations;
//    }
//
//    public static List<Location> getLocations(String json){
//        JSONObject jsonObject = JSON.parseObject(json);
//        return getLocations(jsonObject);
//    }
//
//    private static List<Transition> getTransitions(JSONObject RTAJsonObject,List<Location> locations){
//        JSONObject jsonObject = RTAJsonObject.getJSONObject("tran");
//        Map<Integer,Location> map = getLocationMapFromList(locations);
//        int size = jsonObject.size();
//        List<Transition> transitions = new ArrayList<>();
//        for(int i = 0; i < size; i++){
//            JSONArray array = jsonObject.getJSONArray(String.valueOf(i));
//            int sourceId = array.getInteger(0);
//            Location sourceLocation =  map.get(sourceId);
//            String action = array.getString(1);
//            TimeGuard timeGuard = getTimeGuardFromString(array.getString(2));
//            int targetId = array.getInteger(3);
//            Location targetLocation = map.get(targetId);
//            Transition transition = new Transition(sourceLocation,targetLocation,timeGuard,action);
//            transitions.add(transition);
//        }
//        return transitions;
//    }

//    public static Set<TimeWords> getAllPrefixes(TimeWords words){
//        int len = words.size();
//        Set<TimeWords> prefixes = new HashSet<>();
//        List<TimeWord> words1 = new ArrayList<>();
//        for(int i = 0; i < len; i ++){
//            words1.add(words.get(i));
//            List<TimeWord> words2 = new ArrayList<>(words1);
//            TimeWords timeWords = new TimeWords(words2);
//            prefixes.add(timeWords);
//        }
//        return prefixes;
//    }

//    public static TimeWords concat(TimeWords prefix, TimeWords suffix){
//        List<TimeWord> prefixList = prefix.getWordList();
//        List<TimeWord> suffixList = suffix.getWordList();
//        List<TimeWord> list = new ArrayList<>();
//        list.addAll(prefixList);
//        list.addAll(suffixList);
//        return new TimeWords(list);
//    }
//
//    public static TimeWords concat(TimeWords prefix, TimeWord delta){
//        List<TimeWord> suffixList = new ArrayList<>();
//        suffixList.add(delta);
//        TimeWords suffix = new TimeWords(suffixList);
//        return concat(prefix,suffix);
//    }
//
//    public static TimeWords concat(TimeWord prefixWord, TimeWords suffix){
//        List<TimeWord> prefixList = new ArrayList<>();
//        prefixList.add(prefixWord);
//        TimeWords prefix = new TimeWords(prefixList);
//        return concat(prefix,suffix);
//    }
//
//    public static TimeWords concat(TimeWord prefixWord, TimeWord suffix){
//        List<TimeWord> prefixList = new ArrayList<>();
//        prefixList.add(prefixWord);
//        TimeWords prefix = new TimeWords(prefixList);
//        return concat(prefix,suffix);
//    }


//    public static TimeWords getFirstN(TimeWords words,int n){
//        if(n == 0){
//            return TimeWords.EMPTY_WORDS;
//        }
//        List<TimeWord> wordList = words.getWordList();
//        List<TimeWord> wordList1 = wordList.subList(0,n-1);
//        return new TimeWords(wordList1);
//    }

    public static boolean isSimilar(TimeWords words1,String action,TimeWords words2){
        if(words2.size()==words1.size()+1){
            if(words1.size() == 0 || words1.equals(words2.getFirstN(words1.size()))){
                if(words2.get(words2.size()-1).getAction().equals(action)){
                    return true;
                }
            }
        }
        return false;
    }

    public static TimeGuard toIntegerTimeGuard(TimeWord word){
        TimeGuard timeGuard = new TimeGuard(false,true,(int)word.getValue(),RTA.MAX_TIME);
        if(word.getValue()>(int)word.getValue()){
            timeGuard.setLeftOpen(true);
        }
        return timeGuard;
    }

    public static RTA evidRTAToRTA(RTA evidRTA){
        List<Location> locations = evidRTA.getLocations();
        List<Location> newLocations = new ArrayList<>(locations);
        List<Transition> newTransitions = new ArrayList<>();
        for(Location l: locations){
            List<Transition> transitions = evidRTA.getTransitionsFromLocation(l);
            for(String action: evidRTA.getSigma()){
                List<Transition> actionTranList = RTAUtil.getTranOfAction(transitions,action);
                actionTranList.sort(new Comparator<Transition>() {
                    @Override
                    public int compare(Transition o1, Transition o2) {
                        if(o1.getTimeGuard().getLeft() < o2.getTimeGuard().getLeft()){
                            return -1;
                        }
                        else if(o1.getTimeGuard().getLeft() == o2.getTimeGuard().getLeft()){
                            if(!o1.getTimeGuard().isLeftOpen()){
                                return -1;
                            }
                        }
                        return 1;
                    }
                });
                for(int i = 0; i < actionTranList.size(); i++){
                    if(i < actionTranList.size()-1){
                        TimeGuard timeGuard = new TimeGuard(actionTranList.get(i).getTimeGuard());
                        timeGuard.setRight(actionTranList.get(i+1).getTimeGuard().getLeft());
                        timeGuard.setRightOpen(!actionTranList.get(i+1).getTimeGuard().isLeftOpen());
                        Transition Transition = new Transition(actionTranList.get(i).getSourceLocation(),actionTranList.get(i).getTargetLocation(),timeGuard,action);
                        newTransitions.add(Transition);
                    }else {
                        Transition Transition = new Transition(actionTranList.get(i).getSourceLocation(),actionTranList.get(i).getTargetLocation(),actionTranList.get(i).getTimeGuard(),action);
                        newTransitions.add(Transition);
                    }
                }
            }

        }
        return new RTA(evidRTA.getName(),evidRTA.getSigma(),newLocations,newTransitions);
    }

    public static void refineTran(RTA rta){
        List<Transition> newTransitions = new ArrayList<>();
        List<Location> locationList = rta.getLocations();
        Set<String> sigma = rta.getSigma();
        for(int i = 0; i < locationList.size(); i++){
            for(int j = 0;j<locationList.size();j++){
                for(String action: sigma){
                    List<Transition> trans = rta.getTransBetweenABAndWithAction(locationList.get(i),locationList.get(j),action);
                    newTransitions.addAll(refine(trans));
                }
            }
        }
        newTransitions.sort(new Comparator<Transition>() {
            @Override
            public int compare(Transition o1, Transition o2) {
                if(o1.getSourceLocation().getId()<o2.getSourceLocation().getId()){
                    return -1;
                }
                if(o1.getSourceLocation().getId()>o2.getSourceLocation().getId()){
                    return 1;
                }
                if(o1.getTimeGuard().getLeft() <= o2.getTimeGuard().getLeft()){
                    return -1;
                }
                else if(o1.getTimeGuard().getLeft() == o2.getTimeGuard().getLeft()){
                    if(!o1.getTimeGuard().isLeftOpen()){
                        return -1;
                    }
                }
                return 1;
            }
        });
        rta.setTransitions(newTransitions);
    }

    private static List<Transition> refine(List<Transition> trans){
        List<Transition> list = new ArrayList<>();
        for (int i = 0; i <trans.size(); i++) {
            Transition current = trans.get(i);
            if(i != trans.size()-1){
                Transition next = trans.get(i+1);
                TimeGuard currentTimeGuard = current.getTimeGuard();
                TimeGuard nextTimeGuard = next.getTimeGuard();
                if(currentTimeGuard.isRightOpen() != nextTimeGuard.isLeftOpen()){
                    if(currentTimeGuard.getRight() == nextTimeGuard.getLeft()){
                        nextTimeGuard.setLeftOpen(currentTimeGuard.isLeftOpen());
                        nextTimeGuard.setLeft(currentTimeGuard.getLeft());
                        continue;
                    }
                }
            }
            list.add(current);
        }
        return list;
    }

    public static List<Transition> getTranOfAction(List<Transition> transitions,String action){
        List<Transition> transitionList = new ArrayList<>();
        for(Transition t:transitions){
            if(t.getAction().equals(action)){
                transitionList.add(t);
            }
        }
        return transitionList;
    }

    public static RTA removeSink(RTA rta){
        List<Transition> newTransitions = new ArrayList<>(rta.getTransitions());
        List<Location> newLocations = new ArrayList<>();
        Set<String> sigma = rta.getSigma();
        List<Location> locations = rta.getLocations();
        for(int i = 0; i < locations.size(); i++){
            Location location = locations.get(i);
            if(location.isAccept()){
                newLocations.add(location);
            }else {
                List<Transition> list1 = rta.getTransitionsFromLocation(location);
                List<Transition> list2 = rta.getTransitionsToLocation(location);
                newTransitions.removeAll(list1);
                newTransitions.removeAll(list2);
            }
        }
        return new RTA(rta.getName(),sigma,newLocations,newTransitions);
    }

    //将RTA补全
    public static RTA toCompleteRTA(RTA rta){
        Set<String> sigma = rta.getSigma();
        Location sinkLocation = new Location(rta.getSize()+1,"sink",false,false);
        List<Location> newLocations = new ArrayList<>();
        List<Transition> newTransitions = new ArrayList<>();
        newLocations.addAll(rta.getLocations());
        newLocations.add(sinkLocation);
        newTransitions.addAll(rta.getTransitions());
        for(String action:sigma){
            TimeGuard timeGuard = new TimeGuard(false,true,0,RTA.MAX_TIME);
            Transition transition = new Transition(sinkLocation,sinkLocation,timeGuard,action);
            newTransitions.add(transition);
        }
        for(Location l:rta.getLocations()){
            List<Transition> trans = rta.getTransitionsFromLocation(l);
            for(String action:sigma){
                List<Transition> actionTrans = RTAUtil.getTranOfAction(trans,action);
                TimeGuard timeGuard = new TimeGuard(false,true,0,0);
                Transition pre = new Transition(sinkLocation,sinkLocation,timeGuard,action);
                for(int i = 0; i < actionTrans.size(); i++){
                    Transition current = actionTrans.get(i);
                    TimeGuard preTimeGuard = pre.getTimeGuard();
                    TimeGuard currentTimeGuard = current.getTimeGuard();
                    boolean var0 = preTimeGuard.getRight() ==0 && currentTimeGuard.getLeft()==0;
                    boolean var1 = currentTimeGuard.getLeft()==preTimeGuard.getRight();
                    boolean var2 = currentTimeGuard.isLeftOpen();
                    boolean var3 = !preTimeGuard.isRightOpen();
                    if(var0 || (var1 && var2 && var3)){
                        pre = current;
                    }else {
                        boolean leftO = !preTimeGuard.isRightOpen();
                        boolean rightO = !currentTimeGuard.isLeftOpen();
                        int left = preTimeGuard.getRight();
                        int right = currentTimeGuard.getLeft();
                        TimeGuard timeGuard1 = new TimeGuard(leftO,rightO,left,right);
                        Transition t = new Transition(l,sinkLocation,timeGuard1,action);
                        newTransitions.add(t);
                        pre = current;
                    }
                }
                TimeGuard preTimeGuard = pre.getTimeGuard();
                if(preTimeGuard.getRight()!=RTA.MAX_TIME){
                    boolean leftO = !preTimeGuard.isRightOpen();
                    boolean rightO = false;
                    int left = preTimeGuard.getRight();
                    int right = RTA.MAX_TIME;
                    TimeGuard timeGuard1 = new TimeGuard(leftO,rightO,left,right);
                    Transition t = new Transition(l,sinkLocation,timeGuard1,action);
                    newTransitions.add(t);
                }
            }
        }
        return  new RTA(rta.getName(),sigma,newLocations,newTransitions);
    }

    //对给定的RTA求反
    public static RTA negRTA(RTA rta) throws CloneNotSupportedException {
        List<Location> locations = rta.getLocations();
        List<Transition> oldTransitions = rta.getTransitions();
        String oldName = rta.getName();
        Set<String> oldSigma = rta.getSigma();

        List<Location> newLocations = new ArrayList<>();
        int len = locations.size();
        for(int i = 0; i < len; i ++){
            Location oldLocation = locations.get(i);
            Location newLocation = oldLocation.clone();
            boolean newAccepted = !oldLocation.isAccept();
            newLocation.setAccept(newAccepted);
            newLocations.add(newLocation);
        }
        List<Transition> newTransitions = new ArrayList<>(oldTransitions);
        String newName = "neg"+oldName;
        Set<String> newSigma = new HashSet<>(oldSigma);
        return new RTA(newName,newSigma,newLocations,newTransitions);
    }

    /**
     * 求给定两个RTA的笛卡尔积
     */

    public static RTA cartesian(RTA r1, RTA r2){
        List<Location> locations1 = r1.getLocations();
        List<Location> locations2 = r2.getLocations();
        Set<String> sigma = new HashSet<>();
        sigma.addAll(r1.getSigma());
        sigma.addAll(r2.getSigma());
        String name = r1.getName()+"-"+r2.getName();
        Map<Integer,Location> locationMap1 = RTAUtil.getLocationMapFromList(locations1);
        Map<Integer,Location> locationMap2 = RTAUtil.getLocationMapFromList(locations2);
        Map<Integer,Location> locationMap = null;

        //location的笛卡尔积
        int len1 = locations1.size();
        int len2 = locations2.size();
        List<Location> newLocations = new ArrayList<>();
        for(int i = 1; i <= len1; i ++){
            Location location1 = locationMap1.get(i);
            for(int j = 1; j <= len2; j ++){
                Location location2 = locationMap2.get(j);
                int id = (i-1)*len2 + j;
                String locationName = location1.getName()+"-"+location2.getName();
                boolean init = location1.isInit() && location2.isInit();
                boolean accpted = location1.isAccept() && location2.isAccept();
                Location location = new Location(id,locationName,init,accpted);
                newLocations.add(location);
            }
        }
        locationMap = getLocationMapFromList(newLocations);

        //transition的笛卡尔积
        List<Transition> newTransitions = new ArrayList<>();

        for(int i = 1; i <= len1; i++){
            for(int j = 1; j <= len2; j++){
                for(String action:sigma){
                    Location location1 = locationMap1.get(i);
                    Location location2 = locationMap2.get(j);
                    List<Transition> transitionList1 = r1.getTransitionsFromLocationAndAction(location1,action);
                    List<Transition> transitionList2 = r2.getTransitionsFromLocationAndAction(location2,action);
                    int tranLen1 = transitionList1.size();
                    int tranLen2 = transitionList2.size();

                    for(int m = 0; m < tranLen1; m++){
                        Transition tran1 = transitionList1.get(m);
                        Location sourceLocation1 = tran1.getSourceLocation();
                        Location targetLocation1 = tran1.getTargetLocation();
                        int sourceId1 = sourceLocation1.getId();
                        int targetId1 = targetLocation1.getId();
                        TimeGuard timeGuard1 = tran1.getTimeGuard();
                        for(int n = 0; n < tranLen2; n++){
                            Transition tran2 = transitionList2.get(n);
                            Location sourceLocation2 = tran2.getSourceLocation();
                            Location targetLocation2 = tran2.getTargetLocation();
                            int sourceId2 = sourceLocation2.getId();
                            int targetId2 = targetLocation2.getId();
                            TimeGuard timeGuard2 = tran2.getTimeGuard();

                            int newSourceId = (sourceId1-1)*len2 + sourceId2;
                            int newTargetId = (targetId1-1)*len2 + targetId2;
                            Location newSourceLocation = locationMap.get(newSourceId);
                            Location newTargetLocation = locationMap.get(newTargetId);
                            TimeGuard newTimeGuard = timeGuard1.conj(timeGuard2);
                            if(newTimeGuard != null){
                                Transition newTransition = new Transition(newSourceLocation,newTargetLocation,newTimeGuard,action);
                                newTransitions.add(newTransition);
                            }
                        }
                    }
                }
            }
        }
        return new RTA(name,sigma,newLocations,newTransitions);
    }



}
