package com.jun.canonicalizer;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Jun on 2017-02-25.
 * Term data structure
 */
public class Term {
    private double coefficient;
    private ArrayList<Variable> variables;
    private boolean sign;

    public Term(double coefficient, ArrayList<Variable> variables) {
        this.coefficient = coefficient;
        if (variables != null)
            Collections.sort(variables);
        this.variables = variables;
    }

    public Term(double coefficient, ArrayList<Variable> variables, boolean sign) {
        this.coefficient = coefficient;
        this.variables = variables;
        this.sign = sign;
    }

    public void setCoefficient(double coefficient) {
        this.coefficient = coefficient;
    }

    public double getCoefficient() {
        return coefficient;
    }

    public void setVariables(ArrayList<Variable> variables) {
        Collections.sort(variables);
        this.variables = variables;
    }

    public ArrayList<Variable> getVariables() {
        return variables;
    }

    public void setSign(boolean sign) {
        this.sign = sign;
    }

    public boolean getSign() {
        return sign;
    }

    public String getVariableString() {
        StringBuilder sb = new StringBuilder();
        for (Variable var : variables) {
            sb.append(var.toString());
        }

        return sb.toString();
    }

    @Override
    public String toString() {
        StringBuilder res = new StringBuilder();

        if (sign) {

        }

        return res.toString();
    }

}
