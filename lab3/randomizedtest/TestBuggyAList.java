package randomizedtest;

import edu.princeton.cs.algs4.StdRandom;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Created by hug.
 */
public class TestBuggyAList {
  // YOUR TESTS HERE

    @Test
    public void testThreeAddThreeRemove(){
         AListNoResizing<Integer> anr = new AListNoResizing<Integer>();
         BuggyAList<Integer> bal = new BuggyAList<Integer>();

        anr.addLast(3);
        bal.addLast(3);
        anr.addLast(4);
        bal.addLast(4);
        anr.addLast(5);
        bal.addLast(5);
        int result1 = anr.removeLast();
        int result2 = bal.removeLast();
        assertEquals("Should have the same value", result1, result2, 0.0);

        result1 = anr.removeLast();
        result2 = bal.removeLast();
        assertEquals("Should have the same value", result1, result2, 0.0);

        result1 = anr.removeLast();
        result2 = bal.removeLast();
        assertEquals("Should have the same value", result1, result2, 0.0);
    }

    @Test
    public void randomizedTest(){
        AListNoResizing<Integer> L = new AListNoResizing<Integer>();
        BuggyAList<Integer> B = new BuggyAList<Integer>();
        int N = 500000;
        for (int i = 0; i < N; i += 1) {
            int operationNumber = StdRandom.uniform(0, 3);
            if (operationNumber == 0) {
                // addLast
                int randVal = StdRandom.uniform(0, 100);
                L.addLast(randVal);
                B.addLast(randVal);
                System.out.println("addLast(" + randVal + ")");
            } else if (operationNumber == 1) {
                // size
                int size1 = L.size();
                int size2 = B.size();
                assertEquals("Should have the same size", size2, size1, 0.0);
                System.out.println("size: " + size1);
            }else if(operationNumber == 2){
                if(L.size() != 0){
                    int last1 = L.getLast();
                    int last2 = B.getLast();
                    assertEquals("Should have the same value", last1, last2, 0.0);
                    last1 = L.removeLast();
                    last2 = B.removeLast();
                    assertEquals("Should have the same value", last1, last2, 0.0);
                }
            }
        }

    }

}
