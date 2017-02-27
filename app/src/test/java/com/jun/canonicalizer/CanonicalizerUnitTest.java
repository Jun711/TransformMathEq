package com.jun.canonicalizer;

import android.util.Log;

import org.junit.Test;

import java.util.HashMap;
import java.util.HashSet;

import static org.junit.Assert.*;

import static org.junit.Assert.*;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class CanonicalizerUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
        assertEquals("\\.","\\.");
        assertEquals('.','.');
        assertEquals(Integer.toString(-1),"-1");
    }

    @Test
    public void testBuildTermMapCheckTerms() {

        HashSet<String> keys1 = new HashSet<>();
        keys1.add("x");
        keys1.add("y");
        keys1.add("#");
        assertEquals(3, keys1.size());
        HashMap<String, Double> termMap1 = Canonicalizer.buildTermMap("3x + y - 1");
        assertEquals(keys1, termMap1.keySet());

        // check if composite variables work
        HashSet<String> keys2 = new HashSet<>();
        keys2.add("xz");
        keys2.add("y");
        assertEquals(2, keys2.size());
        HashMap<String, Double> termMap2 = Canonicalizer.buildTermMap("3xz + y");
        assertEquals(keys2, termMap2.keySet());

        // check if variables that have power work
        HashSet<String> keys3 = new HashSet<>();
        keys3.add("xz^3");
        keys3.add("y");
        keys3.add("k");

        assertEquals(3, keys3.size());
        HashMap<String, Double> termMap3 = Canonicalizer.buildTermMap("xz^3 + y - k");
        assertTrue(keys3.contains("k"));
        assertTrue(termMap3.keySet().contains("k"));
        assertFalse(termMap3.keySet().contains("a"));
        assertEquals(keys3, termMap3.keySet());

        // check if multiple variables with power work
        HashSet<String> keys4 = new HashSet<>();
        keys4.add("x^2z^3");
        keys4.add("y^2");
        keys4.add("k");

        HashMap<String, Double> termMap4 = Canonicalizer.buildTermMap("x^2z^3 - y^2 - k^1");
        assertEquals(keys4, termMap4.keySet());

        // check if a variable with pos and neg power 0 works
        HashSet<String> keys5 = new HashSet<>();
        keys5.add("x^2z^3");
        keys5.add("y^2");
        keys5.add("#");

        HashMap<String, Double> termMap5 = Canonicalizer.buildTermMap("x^2z^3 - y^2 - k^0 - k^-0");
        assertEquals(keys5, termMap5.keySet());
        assertEquals(Double.valueOf(-2.0), termMap5.get("#"));

        // check if a variable with neg power works
        HashSet<String> keys6 = new HashSet<>();
        keys6.add("x^2z^-3");
        keys6.add("y^2");
        keys6.add("z^-11");

        HashMap<String, Double> termMap6 = Canonicalizer.buildTermMap("x^2z^-3 - y^2 - z^-11");
        assertEquals(keys6, termMap6.keySet());

        // check if eq is just a zero
        HashMap<String, Double> termMap7 = Canonicalizer.buildTermMap("0");
        assertTrue(termMap7.keySet().isEmpty());
    }

    // Check if coefficients of variables are correct
    @Test
    public void testBuildTermMapCheckCoefficient() {

        HashMap<String, Double> termMap1 = Canonicalizer.buildTermMap("3x + y");
        assertEquals(Double.valueOf(3.0), termMap1.get("x"));
        assertEquals(Double.valueOf(1.0), termMap1.get("y"));

        // check if two digits work
        HashMap<String, Double> termMap2 = Canonicalizer.buildTermMap("34x + y^-2 - 1.1");
        assertEquals(Double.valueOf(34.0), termMap2.get("x"));
        assertEquals(Double.valueOf(1.0), termMap2.get("y^-2"));
        assertEquals(Double.valueOf(-1.1), termMap2.get("#"));

        // check if decimals work
        HashMap<String, Double> termMap3 = Canonicalizer.buildTermMap("3.33x + y");
        assertEquals(Double.valueOf(3.33),termMap3.get("x"));

        // check if negative work
        HashMap<String, Double> termMap4 = Canonicalizer.buildTermMap("34.33x - 5y");
        assertEquals(Double.valueOf(-5.0),termMap4.get("y"));
        assertEquals(Double.valueOf(34.33),termMap4.get("x"));

        // check if negative decimal var works
        HashMap<String, Double> termMap5 = Canonicalizer.buildTermMap("34.33x + -5.3y");
        assertEquals(Double.valueOf(-5.3),termMap5.get("y"));

        // check if pos and neg var without coefficient works
        HashMap<String, Double> termMap6 = Canonicalizer.buildTermMap("x-y");
        assertEquals(Double.valueOf(1.0), termMap6.get("x"));
        assertEquals(Double.valueOf(-1.0), termMap6.get("y"));

        // check if pos and neg coefficient zero works
        HashMap<String, Double> termMap7 = Canonicalizer.buildTermMap("0x-0y");
        assertEquals(Double.valueOf(0.0), termMap7.get("x"));
        assertEquals(Double.valueOf(0.0), termMap7.get("y"));

        // check if coefficient 0 and variable with power
        HashSet<String> keys8 = new HashSet<>();
        keys8.add("x^2");
        HashMap<String, Double> termMap8 = Canonicalizer.buildTermMap("0x^2");
        assertEquals(keys8, termMap8.keySet());
        assertEquals(Double.valueOf(0.0), termMap8.get("x^2"));

        // check if coefficient 0 and variable with 0
        HashSet<String> keys9 = new HashSet<>();
        keys9.add("#");
        HashMap<String, Double> termMap9 = Canonicalizer.buildTermMap("0.0x^0");
        assertNotEquals(keys9, termMap9.keySet());
        assertNotEquals(Double.valueOf(0.0), termMap9.get("#"));
    }

    // Check if eqs work with brackets
    @Test
    public void testBuildTermMapCheckBrackets() {

        // check if bracket works
        HashSet<String> keys1 = new HashSet<>();
        keys1.add("x");
        keys1.add("y");
        assertEquals(2, keys1.size());
        HashMap<String, Double> termMap1 = Canonicalizer.buildTermMap("(x + y + 0y)");
        assertEquals(keys1, termMap1.keySet());
        assertEquals(Double.valueOf(1.0), termMap1.get("x"));
        assertEquals(Double.valueOf(1.0), termMap1.get("y"));

        // check if bracket works with neg number
        HashSet<String> keys2 = new HashSet<>();
        keys2.add("x");
        keys2.add("y");

        HashMap<String, Double> termMap2 = Canonicalizer.buildTermMap("(3x - y)");
        assertEquals(keys2, termMap2.keySet());
        assertEquals(Double.valueOf(3.0), termMap2.get("x"));
        assertEquals(Double.valueOf(-1.0), termMap2.get("y"));

        // check if bracket works with neg sign before bracket
        HashSet<String> keys3 = new HashSet<>();
        keys3.add("x");
        keys3.add("y");

        HashMap<String, Double> termMap3 = Canonicalizer.buildTermMap("-(3x - y)");
        assertEquals(keys3, termMap3.keySet());
        assertEquals(Double.valueOf(-3.0), termMap3.get("x"));
        assertEquals(Double.valueOf(1.0), termMap3.get("y"));

        // check if double brackets work
        HashSet<String> keys4 = new HashSet<>();
        keys4.add("x");
        keys4.add("y");

        HashMap<String, Double> termMap4 = Canonicalizer.buildTermMap("-((3x - y))");
        assertEquals(keys4, termMap4.keySet());
        assertEquals(Double.valueOf(-3.0), termMap4.get("x"));
        assertEquals(Double.valueOf(1.0), termMap4.get("y"));

        // check if missing matching brackets work TODO: add a specific term for error msg
        HashSet<String> keys5 = new HashSet<>();
        keys5.add("x");
        keys5.add("y");

        HashMap<String, Double> termMap5 = Canonicalizer.buildTermMap("-(3x - y");
        assertNotEquals(keys5, termMap5.keySet());
        assertNotEquals(Double.valueOf(-3.0), termMap5.get("x"));
        assertNotEquals(Double.valueOf(1.0), termMap5.get("y"));

        // check if more complicated eq works
        HashSet<String> keys6 = new HashSet<>();
        keys6.add("x");
        keys6.add("y");

        HashMap<String, Double> termMap6 = Canonicalizer.buildTermMap("(x - (3x - y))");
        assertEquals(keys6, termMap6.keySet());
        assertEquals(Double.valueOf(-2.0), termMap6.get("x"));
        assertEquals(Double.valueOf(1.0), termMap6.get("y"));

        // check if more complicated eq works
        HashSet<String> keys7 = new HashSet<>();
        keys7.add("x");
        keys7.add("y");

        HashMap<String, Double> termMap7 = Canonicalizer.buildTermMap("(x - (3x - y) + y)");
        assertEquals(keys7, termMap7.keySet());
        assertEquals(Double.valueOf(-2.0), termMap7.get("x"));
        assertEquals(Double.valueOf(2.0), termMap7.get("y"));

        // check if eq with nested brackets works
        HashSet<String> keys8 = new HashSet<>();
        keys8.add("x");
        keys8.add("y");
        keys8.add("z");

        HashMap<String, Double> termMap8 = Canonicalizer.buildTermMap("(x - ((3x - y) - 6z) + y)");
        assertEquals(keys8, termMap8.keySet());
        assertEquals(Double.valueOf(-2.0), termMap8.get("x"));
        assertEquals(Double.valueOf(2.0), termMap8.get("y"));
        assertNotEquals(Double.valueOf(-6.0), termMap8.get("z"));
        assertEquals(Double.valueOf(6.0), termMap8.get("z"));

        // check if brackets and multiple variables with power work
        HashSet<String> keys9 = new HashSet<>();
        keys9.add("x^2z^3");
        keys9.add("y^2");
        keys9.add("k");

        HashMap<String, Double> termMap9 = Canonicalizer.buildTermMap("(x^2z^3 - y^2 - k^1)");
        assertEquals(keys9, termMap9.keySet());
        assertEquals(Double.valueOf(1.0), termMap9.get("x^2z^3"));
        assertEquals(Double.valueOf(-1.0), termMap9.get("y^2"));
        assertEquals(Double.valueOf(-1.0), termMap9.get("k"));

        // check if nexted brackets and multiple variables with power work
        HashSet<String> keys10 = new HashSet<>();
        keys10.add("x^2z^3");
        keys10.add("y");
        keys10.add("x");
        keys10.add("k");

        HashMap<String, Double> termMap10 = Canonicalizer.buildTermMap("(x^2z^3 - y + (x - (5x + 3y) - 6y) - k^1 + y)");
        assertEquals(keys10, termMap10.keySet());
        assertEquals(Double.valueOf(1.0), termMap10.get("x^2z^3"));
        assertEquals(Double.valueOf(-9.0), termMap10.get("y"));
        assertEquals(Double.valueOf(-4.0), termMap10.get("x"));
        assertEquals(Double.valueOf(-1.0), termMap10.get("k"));

        // check if it works for the provided example
        HashSet<String> keys11 = new HashSet<>();
        keys11.add("x");
        keys11.add("y^2");

        HashMap<String, Double> termMap11 = Canonicalizer.buildTermMap("x - (y^2 - x)");
        assertEquals(keys11, termMap11.keySet());
        assertEquals(Double.valueOf(2.0), termMap11.get("x"));
        assertEquals(Double.valueOf(-1.0), termMap11.get("y^2"));

        // check if it works for the provided example
        HashSet<String> keys12 = new HashSet<>();
        keys12.add("x");

        HashMap<String, Double> termMap12 = Canonicalizer.buildTermMap("x - (0 - (0 - x))");
        assertEquals(keys12, termMap12.keySet());
        assertEquals(Double.valueOf(0.0), termMap12.get("x"));
    }

    // Check if numbers add up properly
    @Test
    public void testBuildTermMapCheckNumber() {

        HashSet<String> keys1 = new HashSet<>();
        keys1.add("x");
        keys1.add("y");
        keys1.add("#");
        assertEquals(3, keys1.size());
        HashMap<String, Double> termMap1 = Canonicalizer.buildTermMap("3x + y + 3");
        assertEquals(keys1, termMap1.keySet());
        assertEquals(Double.valueOf(3.0), termMap1.get("#"));
        assertEquals(Double.valueOf(3.0), termMap1.get("x"));
        assertEquals(Double.valueOf(1.0), termMap1.get("y"));

        // check if single number works
        HashSet<String> keys2 = new HashSet<>();
        keys2.add("#");
        assertEquals(1, keys2.size());
        HashMap<String, Double> termMap2 = Canonicalizer.buildTermMap("11");
        assertEquals(keys2, termMap2.keySet());
        assertEquals(Double.valueOf(11.0), termMap2.get("#"));

        // check if numbers work with brackets
        HashSet<String> keys3 = new HashSet<>();
        keys3.add("x");
        keys3.add("#");
        HashMap<String, Double> termMap3 = Canonicalizer.buildTermMap("x - (1 - (5 - x))");
        assertEquals(keys3, termMap3.keySet());
        assertEquals(Double.valueOf(0.0), termMap3.get("x"));
        assertEquals(Double.valueOf(4.0), termMap3.get("#"));

        // check if numbers work with brackets and variables with power
        HashSet<String> keys4 = new HashSet<>();
        keys4.add("x");
        keys4.add("#");
        keys4.add("y^2");
        HashMap<String, Double> termMap4 = Canonicalizer.buildTermMap("x - (1 - (5 - x)) + y^2  - 3");
        assertEquals(keys4, termMap4.keySet());
        assertEquals(Double.valueOf(0.0), termMap4.get("x"));
        assertEquals(Double.valueOf(1.0), termMap4.get("y^2"));
        assertEquals(Double.valueOf(1.0), termMap4.get("#"));

        // check if numbers work with brackets and variables with neg power
        HashSet<String> keys5 = new HashSet<>();
        keys5.add("x");
        keys5.add("#");
        keys5.add("y^-2");
        HashMap<String, Double> termMap5 = Canonicalizer.buildTermMap("x - (1 - (5 - x)) + y^-2  - 3.5 + 3.5y^-2");
        assertEquals(keys5, termMap5.keySet());
        assertEquals(Double.valueOf(0.0), termMap5.get("x"));
        assertEquals(Double.valueOf(4.5), termMap5.get("y^-2"));
        assertEquals(Double.valueOf(0.5), termMap5.get("#"));

        // check if single neg decimal number works
        HashSet<String> keys6 = new HashSet<>();
        keys6.add("#");
        assertEquals(1, keys6.size());
        HashMap<String, Double> termMap6 = Canonicalizer.buildTermMap("-11.5");
        assertEquals(keys6, termMap6.keySet());
        assertEquals(Double.valueOf(-11.5), termMap6.get("#"));

        // check if there is neg 0
        HashSet<String> keys7 = new HashSet<>();
        HashMap<String, Double> termMap7 = Canonicalizer.buildTermMap("-0.0");
        assertEquals(keys7, termMap7.keySet());

        // check if number adds up with variable with power 0
        HashSet<String> keys8 = new HashSet<>();
        keys8.add("#");
        HashMap<String, Double> termMap8 = Canonicalizer.buildTermMap("11x^0 + (12y^0 + 10) - 3.5");
        assertEquals(keys8, termMap8.keySet());
        assertEquals(Double.valueOf(29.5), termMap8.get("#"));

        // check if number adds up with variable with power 0
        HashSet<String> keys9 = new HashSet<>();
        keys9.add("#");
        HashMap<String, Double> termMap9 = Canonicalizer.buildTermMap("11x^0 - (2y^0 + 3 + 0.0z^0) - 3.5");
        assertEquals(keys9, termMap9.keySet());
        assertEquals(Double.valueOf(2.5), termMap9.get("#"));

        // check if number adds up with variable with neg power 0
        HashSet<String> keys10 = new HashSet<>();
        keys10.add("#");
        keys10.add("z");
        HashMap<String, Double> termMap10 = Canonicalizer.buildTermMap("11x^0 - (2y^0 + 3 -z + 0.0z^0) - 3.5 + y^-0");
        assertEquals(keys10, termMap10.keySet());
        assertEquals(Double.valueOf(3.5), termMap10.get("#"));
        assertEquals(Double.valueOf(1.0), termMap10.get("z"));
    }

    @Test
    public void testIsValidInput() {
        // doesn't handle y^ == y^1
        assertFalse(Canonicalizer.isValidInput("x^2 + 3.5xy + y = y^ - xy + y"));
        assertFalse(Canonicalizer.isValidInput("x^2 + 3.5xy y = y^ - xy + y"));
        assertFalse(Canonicalizer.isValidInput("x7^2 + 3.5xy = y^2 - xy + y"));
        assertFalse(Canonicalizer.isValidInput("1^2x + 3.5xy = y^2 - xy + y"));
        assertFalse(Canonicalizer.isValidInput("1^2x * 3.5xy = y^2 - xy + y"));
        // no multiplication between terms enclosed in brackets
        assertFalse(Canonicalizer.isValidInput("(1^2x - 3.5xy)(x) = y^2 - xy + y"));
        assertFalse(Canonicalizer.isValidInput("(1^2x - 3.xy)(x) = y^2 - xy + y"));
        assertFalse(Canonicalizer.isValidInput("x - ((x^2.3y^2 - x) + x) = 0"));

        assertTrue(Canonicalizer.isValidInput("x - ((x^-2y^2 - x) + x) = 0"));
        assertTrue(Canonicalizer.isValidInput("x - ((x^2y^2 - x) + x) = 0"));
        assertTrue(Canonicalizer.isValidInput("x - ((y^2 - x) + x) = 0"));
        assertTrue(Canonicalizer.isValidInput("x - (y^2 - x) = 0"));
        assertTrue(Canonicalizer.isValidInput("0x - 0y = 0"));
        assertTrue(Canonicalizer.isValidInput("x^2 + 3.5xy + y = y^2 - xy + y"));
        assertTrue(Canonicalizer.isValidInput("x^2 + 3.5xy y = y^1 - xy + y"));
        assertTrue(Canonicalizer.isValidInput("x = 1"));
        assertTrue(Canonicalizer.isValidInput("x - (0 - (0 - x)) = 0"));
        assertTrue(Canonicalizer.isValidInput("5 - 0 = 5"));
        assertTrue(Canonicalizer.isValidInput("0 = 0"));
    }
}