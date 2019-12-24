package learning.ttt;

import learning.EQ;
import learning.EquivalenceQuery;
import learning.Membership;
import learning.RTAMembership;
import learning.lstar.LstarLearner;
import learning.lstar.ObservationTable;
import rta.RTA;
import rta.RTAJson;
import rta.RTAUtil;
import rta.TimeWords;

import java.io.IOException;
import java.util.Set;

public class TTTExperiment {
    public static void main(String[] args) throws IOException, CloneNotSupportedException {
        RTA teacher = RTAJson.getRTA("test.json");
        teacher = RTAUtil.toCompleteRTA(teacher);
        EquivalenceQuery eq = new EQ(teacher);
        Membership membership = new RTAMembership(teacher);
        Set<String> sigma = teacher.getSigma();
        DTree dTree = new DTree(sigma,membership,"test-Hypothesis");
        TTTLearner tttLearner = new TTTLearner(dTree);

        boolean isEq = false;
        RTA hypothesis = tttLearner.getHypothesis();
        TimeWords ce = eq.findCounterExample(hypothesis);
        while(ce != null){
            tttLearner.refine(ce);
            hypothesis = tttLearner.getHypothesis();
            ce = eq.findCounterExample(hypothesis);
        }
        System.out.println(hypothesis);
    }
}
