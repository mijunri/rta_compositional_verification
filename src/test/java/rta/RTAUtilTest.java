package rta;

import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class RTAUtilTest {
    private RTA input;
    private RTA output;

    @BeforeEach
    void setUp() throws IOException {
        input = RTAJson.getRTA("input.json");
    }


    @Test
    void negRTA() throws CloneNotSupportedException {
        RTA rta = RTAUtil.negRTA(input);
        System.out.println(rta);
    }

    @Test
    void cartesian() throws CloneNotSupportedException {
        RTA rta = RTAUtil.negRTA(input);
        RTA c = RTAUtil.cartesian(rta,input);
        System.out.println();
    }


}