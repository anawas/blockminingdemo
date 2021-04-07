package ch.abbts.sya;

import org.junit.After;
import org.junit.Test;

import static org.junit.Assert.*;

public class MinerTest {

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void hashStringTest() {
        Miner miner = new Miner();
        miner.setDifficulty(3);
        assertEquals("000", miner.getDifficulty());

        miner.setDifficulty(0);
        assertEquals("0", miner.getDifficulty());
    }

    @Test
    public void mineHashTest() {
    }
}