package learning.lstar;

import rta.RTA;
import rta.RTAUtil;
import rta.TimeWords;

public class LstarLearner {
    private ObservationTable observationTable;
    private RTA hypothesis;

    public LstarLearner( ObservationTable observationTable) {
        this.observationTable = observationTable;
        hypothesis = observationTable.tranToRTA();
        //hypothesis = RTAUtil.removeSink(hypothesis);
    }

    public void refine(TimeWords ce){
        observationTable.refine(ce);
        hypothesis = observationTable.tranToRTA();
        //hypothesis = RTAUtil.removeSink(hypothesis);
    }

    public RTA getHypothesis(){
        return hypothesis;
    }
}
