package learning.lstar;

import learning.EQ;
import learning.EquivalenceQuery;
import learning.Membership;
import learning.RTAMembership;
import rta.RTA;
import rta.RTAJson;
import rta.RTAUtil;
import rta.TimeWords;

import java.io.IOException;
import java.util.Set;

public class LStarExperiment {
    public static void main(String[] args) throws IOException, CloneNotSupportedException {
        RTA teacher = RTAJson.getRTA("input.json");
        teacher = RTAUtil.toCompleteRTA(teacher);
        EquivalenceQuery eq = new EQ(teacher);
        Membership membership = new RTAMembership(teacher);
        Set<String> sigma = teacher.getSigma();
        ObservationTable observationTable = new ObservationTable(sigma,membership,"input-Hypothesis");
        LstarLearner lstarLearner = new LstarLearner(observationTable);

        boolean isEq = false;
        RTA hypothesis = lstarLearner.getHypothesis();
        TimeWords ce = eq.findCounterExample(hypothesis);
        while(ce != null){
            lstarLearner.refine(ce);
            hypothesis = lstarLearner.getHypothesis();
            ce = eq.findCounterExample(hypothesis);
        }
        System.out.println(hypothesis);
    }
}
