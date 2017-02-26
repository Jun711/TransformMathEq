package com.jun.canonicalizer;

import org.junit.Test;
import static org.junit.Assert.*;

import static org.junit.Assert.*;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void testIsValidInput() {
        // it can be extended to handle y^ == y^1
        assertFalse(Canonicalizer.isValidInput("x^2 + 3.5xy + y = y^ - xy + y"));
        assertFalse(Canonicalizer.isValidInput("x^2 + 3.5xy y = y^ - xy + y"));
        assertFalse(Canonicalizer.isValidInput("x7^2 + 3.5xy = y^2 - xy + y"));
        assertFalse(Canonicalizer.isValidInput("1^2x + 3.5xy = y^2 - xy + y"));
        assertFalse(Canonicalizer.isValidInput("1^2x * 3.5xy = y^2 - xy + y"));
        // no multiplication between terms enclosed in brackets
        assertFalse(Canonicalizer.isValidInput("(1^2x - 3.5xy)(x) = y^2 - xy + y"));

        assertTrue(Canonicalizer.isValidInput("x - ((y^2 - x) + x) = 0"));
        assertTrue(Canonicalizer.isValidInput("x - (y^2 - x) = 0"));
        assertTrue(Canonicalizer.isValidInput("x^2 + 3.5xy + y = y^2 - xy + y"));
        assertTrue(Canonicalizer.isValidInput("x^2 + 3.5xy y = y^1 - xy + y"));
        assertTrue(Canonicalizer.isValidInput("x = 1"));
        assertTrue(Canonicalizer.isValidInput("x - (0 - (0 - x)) = 0"));
    }

}