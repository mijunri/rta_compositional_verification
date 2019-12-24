package learning.ttt;

import learning.Membership;
import rta.RTA;
import rta.TimeWords;



public class TTTLearner {
    private DTree dTree;
    private RTA hypothesis;

    public TTTLearner( DTree dTree) {
        this.dTree = dTree;
        hypothesis = dTree.tranToRTA();
        //hypothesis = RTAUtil.removeSink(hypothesis);
    }

    public void refine(TimeWords ce){
        dTree.refine(ce);
        hypothesis = dTree.tranToRTA();
        //hypothesis = RTAUtil.removeSink(hypothesis);
    }

    public RTA getHypothesis(){
        return hypothesis;
    }
}