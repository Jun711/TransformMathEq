package com.jun.canonicalizer;

import java.util.Arrays;

/**
 * Created by Jun on 2017-02-25.
 * Represent a single variable
 */
public class Variable {
    private String varName;
    private int power;

    public Variable(String varName, int power) {
        this.varName = varName;
        this.power = power;
    }

    public void setVarName(String varName) {
        char[] varNameArr = varName.toCharArray();
        Arrays.sort(varNameArr);
        // so that yx is equal to xy
        this.varName = String.valueOf(varNameArr);
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
}
