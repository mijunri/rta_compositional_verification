package learning;

import rta.RTA;
import rta.TimeWords;

public interface EquivalenceQuery {
    TimeWords findCounterExample(RTA rta) throws CloneNotSupportedException;
}
