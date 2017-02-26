package com.jun.canonicalizer;

import java.util.Arrays;
import java.util.Comparator;

/**
 * Created by Jun on 2017-02-25.
 * Represent a single variable
 */
public class Variable implements Comparable<Variable> {
    private String varName;
    private int power;

    public Variable(String varName, int power) {
        this.varName = varName;
        this.power = power;
    }

    public void setVarName(String varName) {
//        char[] varNameArr = varName.toCharArray();
//        Arrays.sort(varNameArr);
        // so that yx is equal to xy
        this.varName = varName;
        //String.valueOf(varNameArr);
    }

    public String getVarName() {
        return this.varName;
    }

    public void setPower(int power) {
        this.power = power;
    }

    public int getPower() {
        return this.power;
    }

    @Override
    public boolean equals(Object obj) {
        Variable toCompare = (Variable) obj;
        if (this.varName.equals(toCompare.getVarName()) && this.power == toCompare.getPower()){
            return true;
        }
        return false;
    }

    @Override
    public int compareTo(Variable otherVar) {
        return this.getVarName().compareTo(otherVar.getVarName());
    }

    @Override
    public String toString() {
        if (this.power == 0) {
            return "";
        } else if (this.power == 1) {
            return this.getVarName();
        } else {
            return (this.getVarName() + "^" + this.getPower());
        }
    }
//    @Override
//    public int compare(Variable var1, Variable var2) {
//        return var1.getVarName().compareTo(var2.getVarName());
//    }
}
