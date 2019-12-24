package learning;

import rta.RTA;
import rta.TimeWords;

public class RTAMembership implements Membership {
    private RTA rta;

    public RTAMembership(RTA rta) {
        this.rta = rta;
    }

    @Override
    public boolean answer(TimeWords words) {
        return rta.isAccepted(words);
    }
}
