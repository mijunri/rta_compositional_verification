package rta;

import java.util.*;

/**
 * the defination of real timed automata
 */
public class RTA {
    private String name;
    private Set<String> sigma;
    private List<Location> locations;
    private List<Location> acceptLocations;
    private String initName;
    private Location initLocation;
    private Map<Integer,Location> locationMap;
    private List<Transition> transitions;
    private String sinkName;
    private Location sinkLocation;
    private int size;

    public static final int MAX_TIME = 1000;



    public RTA(String name, Set<String> sigma, List<Location> locations, List<Transition> transitions, String initName, Location initLocation, List<Location> acceptLocations) {
        this.name = name;
        this.sigma = sigma;
        this.locations = locations;
        this.transitions = transitions;
        this.initName = initName;
        this.initLocation = initLocation;
        this.acceptLocations = acceptLocations;
        this.size = locations.size();
        this.locationMap = RTAUtil.getLocationMapFromList(locations);
    }

    public RTA(String name, Set<String> sigma, List<Location> locations, List<Transition> transitions) {
        this.name = name;
        this.sigma = sigma;
        this.locations = locations;
        this.transitions = transitions;
        this.initLocation = initLocation(locations);
        this.initName= (initLocation==null)?null:initLocation.getName();
        this.acceptLocations = acceptLocations(locations);
        this.size = locations.size();
        this.locationMap = RTAUtil.getLocationMapFromList(locations);
    }

    private List<Location> acceptLocations(List<Location> locations){
        List<Location> list = new ArrayList<>();
        for(Location l:locations){
            if(l.isAccept()){
                list.add(l);
            }
        }
        return list;
    }

    private Location initLocation(List<Location> locations){
        for(Location l:locations){
            if(l.isInit()){
                return l;
            }
        }
        return null;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<String> getSigma() {
        return new HashSet<String>(sigma);
    }


    public List<Location> getLocations() {
        return locations;
    }

    public List<Transition> getTransitions() {
        return transitions;
    }


    public String getInitName() {
        return initName;
    }


    public Location getInitLocation() {
        return initLocation;
    }


    public List<Location> getAcceptLocations() {
        return acceptLocations;
    }


    public String getSinkName() {
        return sinkName;
    }

    public void setSinkName(String sinkName) {
        this.sinkName = sinkName;
    }

    public Location getSinkLocation() {
        return sinkLocation;
    }

    public void setSinkLocation(Location sinkLocation) {
        this.sinkLocation = sinkLocation;
    }

    public int getSize() {
        return size;
    }

    public int getInitId(){
        return getInitLocation()==null?0:getInitLocation().getId();
    }

    public void setTransitions(List<Transition> transitions) {
        this.transitions = transitions;
    }

    public Map<Integer, Location> getLocationMap() {
        return locationMap;
    }

    public Location getLocationById(int id){
        return locationMap.get(id);
    }

    public List<Transition> getTransitionsFromLocation(Location location){
        List<Transition> list = new ArrayList<>();
        for(Transition t:transitions){
            if(t.getSourceName().equals(location.getName())){
                list.add(t);
            }
        }
        return list;
    }

    public List<Transition> getTransitionsFromLocationAndAction(Location location,String action){
        List<Transition> list = getTransitionsFromLocation(location);
        List<Transition> list1 = new ArrayList<>();
        for(Transition t:list){
            if(t.getAction().equals(action)){
                list1.add(t);
            }
        }
        return list1;
    }

    public List<Transition> getTransitionsToLocation(Location location){
        List<Transition> list = new ArrayList<>();
        for(Transition t:transitions){
            if(t.getTargetName().equals(location.getName())){
                list.add(t);
            }
        }
        return list;
    }

    public List<Transition> getTransitionsBetweenLocations(Location sourceLocation, Location targetLocation){
        List<Transition> list = new ArrayList<>();
        for(Transition t:transitions){
            if(t.getSourceName().equals(sourceLocation.getName()) && t.getTargetName().equals(targetLocation.getName())){
                list.add(t);
            }
        }
        return list;
    }

    public List<Transition> getTransBetweenABAndWithAction(Location sourceLocation, Location targetLocation,String action){
        List<Transition> transitions = getTransitionsBetweenLocations(sourceLocation,targetLocation);
        List<Transition> list = new ArrayList<>();
        for(Transition t:transitions){
            if(t.getAction().equals(action)){
                list.add(t);
            }
        }
        return list;
    }

    public boolean isAccepted(TimeWords timeWords){
        Location location = traceToLocationByTimeWord(timeWords);
        if(location == null || !location.isAccept()){
            return false;
        }
        return true;
    }


    public String toJson(){
        return RTAJson.transferToJson(this);
    }

    @Override
    public String toString(){
        return toJson();
    }

    public Location traceToLocationByTimeWord(TimeWords words){
        Location location = getInitLocation();
        List<Transition> transitions;
        for(TimeWord w:words.getWordList()){
            transitions = getTransitionsFromLocation(location);
            boolean flag = false;
            for(Transition t:transitions){
                if(t.isPass(w)){
                    location = t.getTargetLocation();
                    flag = true;
                    break;
                }
            }
            if(flag == false){
                return null;
            }
        }
        return location;
    }



}



