package uppaalRTA;

import rta.RTA;
import rta.Location;
import rta.Transition;

import java.util.ArrayList;
import java.util.List;

public class UppaalTemplate {
    private String name;
    private String declaration;
    private String init;
    private List<UppaalLocation> locationList = new ArrayList<>();
    private List<UppaalTransition> transitionList = new ArrayList<>();


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDeclaration() {
        return declaration;
    }

    public void setDeclaration(String declaration) {
        this.declaration = declaration;
    }

    public String getInit() {
        return init;
    }

    public void setInit(String init) {
        this.init = init;
    }

    public List<UppaalLocation> getLocationList() {
        return locationList;
    }

    public void setLocationList(List<UppaalLocation> locationList) {
        this.locationList = locationList;
    }

    public List<UppaalTransition> getTransitionList() {
        return transitionList;
    }

    public void setTransitionList(List<UppaalTransition> transitionList) {
        this.transitionList = transitionList;
    }

    /**
     * 根据给定的RTA生成一个UppaalRTA
     * @param rta
     */
    public UppaalTemplate(RTA rta){
        this.name = rta.getName();
        this.declaration = "clock x;";
        List<Location> rtaLocations = rta.getLocations();
        for(Location l:rtaLocations){
            UppaalLocation uppaalLocation = new UppaalLocation();
            uppaalLocation.setId(rta.getName()+l.getId());
            uppaalLocation.setName(rta.getName()+l.getId());
            locationList.add(uppaalLocation);
        }
        init = rta.getName()+rta.getInitId();
        List<Transition> rtaTransitions = rta.getTransitions();
        for(Transition t:rtaTransitions){
            UppaalTransition uppaalTransition = new UppaalTransition();
            uppaalTransition.setSourceId(rta.getName()+t.getSourceId());
            uppaalTransition.setTargetId(rta.getName()+t.getTargetId());
            uppaalTransition.setSync(t.getAction());
            uppaalTransition.setSend(false);
            uppaalTransition.setAssignment("x:=0");
            uppaalTransition.setGuard(t.getTimeGuard().toExpression());
            uppaalTransition.setRtaName(rta.getName());
            transitionList.add(uppaalTransition);
        }
    }

    public List<UppaalTransition> getTransitionListFromSourceId(String sourceId){
        List<UppaalTransition> list = new ArrayList<>();
        for (UppaalTransition t:transitionList){
            if(t.getSourceId().equals(sourceId)){
                list.add(t);
            }
        }
        return list;
    }

    public List<UppaalTransition> getTransitionListFromTargetId(String targetId){
        List<UppaalTransition> list = new ArrayList<>();
        for (UppaalTransition t:transitionList){
            if(t.getTargetId().equals(targetId)){
                list.add(t);
            }
        }
        return list;
    }

    public List<UppaalTransition> getTransitionListFromAction(String action){
        List<UppaalTransition> list = new ArrayList<>();
        for (UppaalTransition t:transitionList){
            if(t.getSync().equals(action)){
                list.add(t);
            }
        }
        return list;
    }

    public boolean isCommitted(UppaalLocation location){
        List<UppaalTransition> list = getTransitionListFromSourceId(location.getId());
        for(UppaalTransition ut : list){
            if(!ut.isSend){
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return "UppaalTemplate{" +
                "name='" + name + '\'' +
                ", declaration='" + declaration + '\'' +
                ", locationList=" + locationList +
                ", transitionList=" + transitionList +
                '}';
    }
}
