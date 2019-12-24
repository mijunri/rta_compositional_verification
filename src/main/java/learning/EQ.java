package learning;

import rta.*;

import java.util.*;

public class EQ implements EquivalenceQuery {
    private RTA rta;

    public EQ(RTA rta){
        this.rta = rta;
    }

    @Override
    public TimeWords findCounterExample(RTA hypotheses) throws CloneNotSupportedException {
        RTA negRTA = RTAUtil.negRTA(rta);
        RTA negHypothesis = RTAUtil.negRTA(hypotheses);

        RTA r1 = RTAUtil.cartesian(negRTA,hypotheses);
        RTA r2 = RTAUtil.cartesian(rta,negHypothesis);

        TimeWords w1 = counterExample(r1);
        if(w1!=null){
            return w1;
        }

        TimeWords w2 = counterExample(r2);
        if(w2!=null){
            return w2;
        }

        return null;
    }

    private TimeWords counterExample(RTA rta){
        Set<Location> visited = new HashSet<>();
        Map<Location,TimeWords> map = new HashMap<>();
        Deque<Location> stack = new LinkedList<>();

        Location initLocation = rta.getInitLocation();
        visited.add(initLocation);
        map.put(initLocation,TimeWords.EMPTY_WORDS);
        stack.push(initLocation);
        while(!stack.isEmpty()){
            Location current = stack.pop();
            List<Transition> transitions = rta.getTransitionsFromLocation(current);
            for(Transition t:transitions){
                Location source = t.getSourceLocation();
                Location target = t.getTargetLocation();
                TimeWords locationWords = map.get(source);
                if(!visited.contains(target)){
                    visited.add(target);
                    TimeWord word = t.toWord();
                    TimeWords words = TimeWordsUtil.concat(locationWords,word);
                    map.put(target,words);
                    stack.push(target);
                }
            }
        }

        List<Location> acceptedLocations = rta.getAcceptLocations();
        for(Location l:acceptedLocations){
            if(map.containsKey(l)){
                return map.get(l);
            }
        }
        return null;


    }
}
