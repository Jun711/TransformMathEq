package com.jun.canonicalizer;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

/**
 * Created by Jun on 2017-02-26.
 * Unit tests for Term Class
 */
public class TermUnitTest {

    @Test
    public void testVariableToString() {
        Variable var1 = new Variable("x", 3);
        Variable var2 = new Variable("y", 1);
        Variable var3 = new Variable("z", -1);
        ArrayList<Variable> vars = new ArrayList<>();
        vars.add(var1);
        vars.add(var2);
        vars.add(var3);
        Term term1 = new Term(1, vars);
        assertEquals(3, term1.getVariables().size());
        assertEquals("x^3yz^-1", term1.getVariableString());

        Variable var4 = new Variable("x", 0);
        Variable var5 = new Variable("y", 1);
        ArrayList<Variable> vars2 = new ArrayList<>();
        vars2.add(var4);
        vars2.add(var5);
        Term term2 = new Term(1, vars2);
        assertEquals(2, term2.getVariables().size());
        assertEquals(term2.getVariableString(), "y");
    }

    @Test
    public void testSetVariable() {
        // it should sort variables' names alphabetically
        Variable var1 = new Variable("z", 1);
        Variable var2 = new Variable("y", 1);
        Variable var3 = new Variable("x", 1);
        ArrayList<Variable> vars = new ArrayList<>();
        vars.add(var1);
        vars.add(var2);
        vars.add(var3);

        Term term1 = new Term(1, null);
        term1.setVariables(vars);

        assertEquals("xyz", term1.getVariableString());

        Variable var4 = new Variable("x", 1);
        Variable var5 = new Variable("y", 2);
        Variable var6 = new Variable("z", 3);
        ArrayList<Variable> vars2 = new ArrayList<>();
        vars2.add(var4);
        vars2.add(var5);
        vars2.add(var6);

        Term term2 = new Term(1, null);
        term2.setVariables(vars2);

        assertEquals("xy^2z^3", term2.getVariableString());
    }
}
