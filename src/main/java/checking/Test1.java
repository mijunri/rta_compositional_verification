package checking;



import learning.EquivalenceQuery;
import learning.lstar.LstarLearner;
import learning.lstar.ObservationTable;
import rta.RTA;
import rta.RTAJson;
import rta.RTAUtil;
import rta.TimeWords;
import uppaalRTA.UppaalEq;
import uppaalRTA.UppaalMembership;
import uppaalRTA.UppaalMembershipCTL;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class Test1 {
    public static void main(String[] args) throws IOException {
        RTA input = RTAJson.getRTA("input.json");
        RTA output = RTAJson.getRTA("output.json");;
        String property = "A[] not (Input.I1 and Output.O1)";
        Set<String> sigma = new HashSet<>();
        sigma.addAll(input.getSigma());
        sigma.retainAll(output.getSigma());
        UppaalMembershipCTL uppaalMembership = new UppaalMembershipCTL(input,output,property);
        ObservationTable observationTable = new ObservationTable(sigma,uppaalMembership,"assume");
        UppaalEq eq = new UppaalEq(input,output,property);
        LstarLearner learner = new LstarLearner(observationTable);
        RTA hypothesis = learner.getHypothesis();
        TimeWords ce;

        for(;;){
            //step1
            if((ce = eq.findCounterEa(hypothesis))!=null){
                learner.refine(ce);
                hypothesis = learner.getHypothesis();
                continue;
            }

            //step2
            if((ce = eq.findCounterExample(hypothesis))!=null){
                learner.refine(ce);
                hypothesis = learner.getHypothesis();
            }
            else {
                break;
            }
        }
        System.out.println(hypothesis);
    }
}
