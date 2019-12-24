package rta;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.io.*;
import java.util.*;

import static rta.RTAUtil.getLocationMapFromList;

public class RTAJson {

    public static String transferToJson(RTA RTA){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name",RTA.getName());
        JSONArray stateArray = new JSONArray();
        for(Location l:RTA.getLocations()){
            stateArray.add(l.getId());
        }
        jsonObject.put("l",stateArray);
        JSONArray sigmaArray = new JSONArray();
        for(String s:RTA.getSigma()){
            sigmaArray.add(s);
        }
        jsonObject.put("sigma",sigmaArray);
        JSONObject tranJson = new JSONObject();
        List<Transition> transitions = RTA.getTransitions();
        for(int i = 0; i < transitions.size();i++){
            JSONArray tranArray = new JSONArray();
            tranArray.add(transitions.get(i).getSourceId());
            tranArray.add(transitions.get(i).getAction());
            tranArray.add(transitions.get(i).getTimeGuard().toString());
            tranArray.add(transitions.get(i).getTargetId());
            tranJson.put(String.valueOf(i),tranArray);
        }
        jsonObject.put("tran",tranJson);
        jsonObject.put("init",RTA.getInitId());
        JSONArray acceptArray = new JSONArray();
        for(Location acceptLocation: RTA.getAcceptLocations()){
            acceptArray.add(acceptLocation.getId());
        }
        jsonObject.put("accept",acceptArray);
        return jsonObject.toJSONString();
    }

    public static RTA getRTA(String path) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(path)));
        String str = null;
        StringBuilder json = new StringBuilder();
        while ((str = reader.readLine()) != null){
            json.append(str);
        }
        return getRTAFromJson(json.toString());
    }

    //从json字符串获得一个RTA
    public static RTA getRTAFromJson(String json){
        JSONObject jsonObject = JSON.parseObject(json);
        String name = getName(jsonObject);
        Set<String> sigma = getSigma(jsonObject);
        List<Location> locations = getLocations(jsonObject);
        List<Transition> transitions = getTransitions(jsonObject,locations);
        return new RTA(name,sigma,locations,transitions);
    }

    public static String getName(JSONObject RTAJsonObject){
        return RTAJsonObject.getString("name");
    }

    //从json字符串获得RTA的name
    public static String getName(String RTAJson){
        JSONObject jsonObject = JSON.parseObject(RTAJson);
        return getName(jsonObject);
    }

    public static Set<String> getSigma(JSONObject RTAJsonObject){
        JSONArray jsonArray = RTAJsonObject.getJSONArray("sigma");
        Set<String> sigmaSet = new HashSet<>();
        Iterator<Object> iterator = jsonArray.iterator();
        while (iterator.hasNext()){
            sigmaSet.add((String)iterator.next());
        }
        return sigmaSet;
    }

    //从json字符串获得RTA的sigma
    public static Set<String> getSigma(String RTAJson){
        JSONObject jsonObject = JSON.parseObject(RTAJson);
        return getSigma(jsonObject);
    }

    public static List<Location> getLocations(JSONObject jsonObject){
        List<Location> locations = new ArrayList<>();
        JSONArray locationArray = jsonObject.getJSONArray("l");
        List<Integer> list = new ArrayList<>();
        for(int i = 0; i < locationArray.size(); i ++){
            list.add(locationArray.getIntValue(i));
        }
        JSONArray acceptArray = jsonObject.getJSONArray("accept");
        Set<Integer> set = new HashSet<>();
        for(int i = 0; i < acceptArray.size(); i ++){
            set.add(acceptArray.getIntValue(i));
        }
        int initId = jsonObject.getInteger("init");
        for(int id:list){
            Location location = new Location(id);
            location.setName(""+id);
            if(set.contains(id)){
                location.setAccept(true);
            }else {
                location.setAccept(false);
            }
            if(id == initId){
                location.setInit(true);
            }else {
                location.setInit(false);
            }
            locations.add(location);
        }
        return locations;
    }

    //从json字符串获得RTA的location
    public static List<Location> getLocations(String json){
        JSONObject jsonObject = JSON.parseObject(json);
        return getLocations(jsonObject);
    }

    private static List<Transition> getTransitions(JSONObject RTAJsonObject,List<Location> locations){
        JSONObject jsonObject = RTAJsonObject.getJSONObject("tran");
        Map<Integer,Location> map = getLocationMapFromList(locations);
        int size = jsonObject.size();
        List<Transition> transitions = new ArrayList<>();
        for(int i = 0; i < size; i++){
            JSONArray array = jsonObject.getJSONArray(String.valueOf(i));
            int sourceId = array.getInteger(0);
            Location sourceLocation =  map.get(sourceId);
            String action = array.getString(1);
            TimeGuard timeGuard = new TimeGuard(array.getString(2));
            int targetId = array.getInteger(3);
            Location targetLocation = map.get(targetId);
            Transition transition = new Transition(sourceLocation,targetLocation,timeGuard,action);
            transitions.add(transition);
        }
        return transitions;
    }
}
