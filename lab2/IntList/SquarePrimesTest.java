package IntList;

import static org.junit.Assert.*;
import org.junit.Test;

public class SquarePrimesTest {

    /**
     * Here is a test for isPrime method. Try running it.
     * It passes, but the starter code implementation of isPrime
     * is broken. Write your own JUnit Test to try to uncover the bug!
     */
    @Test
    public void testSquarePrimesSimple() {
        IntList lst = IntList.of(14, 15, 16, 17, 18);
        boolean changed = IntListExercises.squarePrimes(lst);
        assertEquals("14 -> 15 -> 16 -> 289 -> 18", lst.toString());
        assertTrue(changed);
    }

    @Test
    public void testSquarePrimesSimple2() {
        IntList lst = IntList.of(14, 15, 16, 2, 18);
        boolean changed = IntListExercises.squarePrimes(lst);
        assertEquals("14 -> 15 -> 16 -> 4 -> 18", lst.toString());
        assertTrue(changed);
    }

    @Test
    public void testSquarePrimesSimple3() {
        IntList lst = IntList.of(14, 15, 16, 1, 18);
        boolean changed = IntListExercises.squarePrimes(lst);
        assertEquals("14 -> 15 -> 16 -> 1 -> 18", lst.toString());
        assertTrue(!changed);
    }

    @Test
    public void testSquarePrimesSimple4() {
        IntList lst = IntList.of(7, 15, 16, 1, 18);
        boolean changed = IntListExercises.squarePrimes(lst);
        assertEquals("49 -> 15 -> 16 -> 1 -> 18", lst.toString());
        assertTrue(changed);
    }

    @Test
    public void testSquarePrimesSimple5() {
        IntList lst = IntList.of(0, 15, 16, 1, 18);
        boolean changed = IntListExercises.squarePrimes(lst);
        assertEquals("0 -> 15 -> 16 -> 1 -> 18", lst.toString());
        assertTrue(!changed);
    }


    @Test
    public void testSquarePrimesSimple6() {
        IntList lst = IntList.of(0, 15, 16, 1, 18, 20, 7);
        boolean changed = IntListExercises.squarePrimes(lst);
        assertEquals("0 -> 15 -> 16 -> 1 -> 18 -> 20 -> 49", lst.toString());
        assertTrue(changed);
    }

    @Test
    public void testSquarePrimesSimple7() {
        IntList lst = IntList.of(0, 13, 11, 1, 18, 20, 7);
        boolean changed = IntListExercises.squarePrimes(lst);
        assertEquals("0 -> 169 -> 121 -> 1 -> 18 -> 20 -> 49", lst.toString());
        assertTrue(changed);
    }

    @Test
    public void testSquarePrimesSimple8() {
        IntList lst = IntList.of(0, 15, 11, 1, 18, 20, 7);
        boolean changed = IntListExercises.squarePrimes(lst);
        assertEquals("0 -> 15 -> 121 -> 1 -> 18 -> 20 -> 49", lst.toString());
        assertTrue(changed);
    }
}
