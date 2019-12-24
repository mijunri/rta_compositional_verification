package uppaalRTA;

public class UppaalTransition {
    String sourceId;
    String targetId;
    String sync;
    boolean isSend;
    String guard;
    String assignment;
    String rtaName;

    public String getRtaName() {
        return rtaName;
    }

    public void setRtaName(String rtaName) {
        this.rtaName = rtaName;
    }

    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    public String getTargetId() {
        return targetId;
    }

    public void setTargetId(String targetId) {
        this.targetId = targetId;
    }

    public String getSync() {
        return sync;
    }

    public void setSync(String sync) {
        this.sync = sync;
    }

    public String getGuard() {
        return guard;
    }

    public void setGuard(String guard) {
        this.guard = guard;
    }

    public String getAssignment() {
        return assignment;
    }

    public void setAssignment(String assignment) {
        this.assignment = assignment;
    }

    public boolean isSend() {
        return isSend;
    }

    public void setSend(boolean send) {
        isSend = send;
    }

    @Override
    public String toString() {
        return "UppaalTransition{" +
                "sourceId='" + sourceId + '\'' +
                ", targetId='" + targetId + '\'' +
                ", sync='" + sync + '\'' +
                ", isSend=" + isSend +
                ", guard='" + guard + '\'' +
                ", assignment='" + assignment + '\'' +
                ", rtaName='" + rtaName + '\'' +
                '}';
    }


}
