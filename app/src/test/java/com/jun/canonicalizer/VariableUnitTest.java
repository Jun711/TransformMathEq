package com.jun.canonicalizer;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by Jun on 2017-02-26.
 * Unit tests for Variable Class
 */
public class VariableUnitTest {
    @Test
    public void testVariableToString() {
        Variable var1 = new Variable("x", 3);
        Variable var2 = new Variable("x", 0);
        Variable var3 = new Variable("x", -1);
        Variable var4 = new Variable("x", 1);

        assertEquals("x^3", var1.toString());
        assertEquals("", var2.toString());
        assertEquals("x^-1", var3.toString());
        assertEquals("x", var4.toString());
    }
}
