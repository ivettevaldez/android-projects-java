package silviavaldez.mygitlabciapp;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

/**
 * Simple Test class for the Calculator.java class.
 * Example taken from: https://io2015codelabs.appspot.com/codelabs/android-studio-testing#4
 * Created by Silvia Valdez on 8/25/16.
 */
public class CalculatorTest {

    private Calculator mCalculator;

    @Before
    public void setUp() throws Exception {
        mCalculator = new Calculator();
    }

    @Test
    public void testSum() throws Exception {
        // Expected: 6, sum of 1 and 5
        assertEquals(6d, mCalculator.sum(1d, 5d), 0);
    }

    @Test
    public void testSubtract() throws Exception {
        // Expected: 1, subtract of 5 and 4
        assertEquals(1d, mCalculator.subtract(5d, 4d), 0);
    }

    @Test
    public void testDivide() throws Exception {
        // Expected: 4, division of 20 and 5
        assertEquals(4d, mCalculator.divide(20d, 5d), 0);
    }

    @Test
    public void testMultiply() throws Exception {
        // Expected: 10, multiplication of 2 and 5
        assertEquals(10d, mCalculator.multiply(2d, 5d), 0);
    }

}