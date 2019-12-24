package rta;

public class Transition {

    public static final double THETA = 0.2;

    private int sourceId;    //the id of the source location
    private int targetId;    //the id of the target location
    private String sourceName;     //the name of the source location
    private String targetName;     //the name of the target location
    private Location sourceLocation;     //source location
    private Location targetLocation;     //target location
    private String action;      // action of the transition
    private TimeGuard timeGuard;    // time guard of the transition



    public Transition(Location sourceLocation, Location targetLocation, TimeGuard timeGuard,String action) {
        this.sourceId = sourceLocation.getId();
        this.targetId = targetLocation.getId();
        this.sourceLocation = sourceLocation;
        this.targetLocation = targetLocation;
        this.timeGuard = timeGuard;
        this.sourceName = sourceLocation.getName();
        this.targetName = targetLocation.getName();
        this.action = action;
    }

    public int getSourceId() {
        return sourceId;
    }

    public void setSourceId(int sourceId) {
        this.sourceId = sourceId;
        this.sourceLocation.setId(sourceId);
    }

    public int getTargetId() {
        return targetId;
    }

    public void setTargetId(int targetId) {
        this.targetId = targetId;
        this.targetLocation.setId(targetId);
    }

    public String getSourceName() {
        return sourceName;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
        this.sourceLocation.setName(sourceName);
    }

    public String getTargetName() {
        return targetName;
    }

    public void setTargetName(String targetName) {
        this.targetName = targetName;
        this.targetLocation.setName(targetName);
    }

    public Location getSourceLocation() {
        return sourceLocation;
    }

    public void setSourceLocation(Location sourceLocation) {
        this.sourceLocation = sourceLocation;
        setSourceId(sourceLocation.getId());
        setSourceName(sourceLocation.getName());
    }

    public Location getTargetLocation() {
        return targetLocation;
    }

    public void setTargetLocation(Location targetLocation) {
        this.targetLocation = targetLocation;
        setTargetId(targetLocation.getId());
        setTargetName(targetLocation.getName());
    }

    public TimeGuard getTimeGuard() {
        return timeGuard;
    }

    public void setTimeGuard(TimeGuard timeGuard) {
        this.timeGuard = timeGuard;
    }


    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public boolean isPass(TimeWord word){
        String action = word.getAction();
        double value = word.getValue();
        if(this.action.equals(action)){
            return timeGuard.isPass(value);
        }
        return false;
    }

    public TimeWord toWord(){
        boolean leftOpen = timeGuard.isLeftOpen();
        int left = timeGuard.getLeft();
        int right = timeGuard.getRight();

        if(!leftOpen){
            return new TimeWord(action,left);
        }
        else {
            return new TimeWord(action,left+THETA);
        }
    }
}

