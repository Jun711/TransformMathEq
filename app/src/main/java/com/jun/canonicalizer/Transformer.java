package com.jun.canonicalizer;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;

/**
 * Created by Jun on 2017-02-27.
 * Transform equation into canonical form
 */
public class Transformer {

    public Transformer() {

    }

    public String transformEq(String equation) {
        return "";
    }

    /**
     * Parse equation to process all the terms
     */
    public String parseEq(String equation) {
        String[] sides = equation.split("=");

        String leftSide = sides[0].trim();
        String rightSide = sides[1].trim();

        HashMap<String, Double> leftTermMap= buildTermMap(leftSide);
        HashMap<String, Double> rightTermMap= buildTermMap(rightSide);

        return "";
    }

    /**
     * Build a HashMap that maps variables to their coefficient and sum of numbers to "#"
     * if a variable has a power 0, its coefficient contributes to the sum of number
     * TODO: count matching parenthesis?
     */
    public HashMap<String, Double> buildTermMap(String lineEq) {
        HashMap<String, Double> termMap = new HashMap<>();
        int numCoefficient;
        int sign = 1;
        Double coefficient = 1.0;
        Double sum = 0.0;
        ArrayList<Variable> variables = null;
        Deque<Term> inBracketStack = new ArrayDeque<>();
        Deque<Term> processBracketStack = new ArrayDeque<>();
        Deque<Double> numberStack = new ArrayDeque<>();

        for (int i = 0; i < lineEq.length(); i++) {
            if (Character.isDigit(lineEq.charAt(i))) {
                numCoefficient = lineEq.charAt(i) - '0';
                int decimalCoefficient = 0;
                while (i + 1 < lineEq.length() && Character.isDigit(lineEq.charAt(i + 1))) {
                    numCoefficient = numCoefficient * 10 + lineEq.charAt(i + 1) - '0';
                    i++;
                }
                if (i + 1 < lineEq.length() && lineEq.charAt(i + 1) == '.') {
                    i += 2;
                    decimalCoefficient = lineEq.charAt(i) - '0';
                    while (i + 1 < lineEq.length() && Character.isDigit(lineEq.charAt(i + 1))) {
                        decimalCoefficient = decimalCoefficient * 10 + lineEq.charAt(i + 1) - '0';
                        i++;
                    }
                }

                coefficient = Double.parseDouble(Integer.toString(numCoefficient) + "." + Integer.toString(decimalCoefficient));

                // if it is end of line or the next character isn't a letter, then this is just a number
                if (i + 1 == lineEq.length() || !Character.isLetter(lineEq.charAt(i + 1))) {
                    sum += sign * coefficient ;
                    coefficient = 1.0;
                }
            } else if (lineEq.charAt(i) == '+') {
                sign = 1;
            } else if (lineEq.charAt(i) == '-') {
                sign = -1;
            } else if (lineEq.charAt(i) == '(') {
                // set variables of the term to null to represent sign
                Term signTerm = new Term(1.0 * sign, null);
                inBracketStack.addFirst(signTerm);
                numberStack.addFirst(sum);
                numberStack.addFirst(1.0 * sign);
                sum = 0.0;
                sign = 1;
            } else if (lineEq.charAt(i) == ')') {
                // sum values within brackets and the numbers to left of the current brackets
                sum = sum * numberStack.removeFirst() + numberStack.removeFirst();
                while (inBracketStack.peekFirst().getVariables() != null) {
                    processBracketStack.addFirst(inBracketStack.removeFirst());
                }
                Term tempSignTerm = inBracketStack.removeFirst();
                double tempSign = tempSignTerm.getCoefficient();
                if (inBracketStack.isEmpty()) {
                    while (!processBracketStack.isEmpty()) {
                        Term term = processBracketStack.removeFirst();
                        if (!termMap.containsKey(term.getVariableString())) {
                            termMap.put(term.getVariableString(), tempSign * term.getCoefficient());
                        } else {
                            Double previousCoefficient = termMap.get(term.getVariableString());
                            Double updatedCoefficient = tempSign * term.getCoefficient() + previousCoefficient;
                            termMap.put(term.getVariableString(), updatedCoefficient);
                        }
                    }
                } else {
                    while (!processBracketStack.isEmpty()) {
                        Term term = processBracketStack.removeFirst();
                        term.setCoefficient(tempSign * term.getCoefficient());
                        inBracketStack.addFirst(term);
                    }
                }
            } else if (Character.isLetter(lineEq.charAt(i))){
                String var = Character.toString(lineEq.charAt(i));
                if (variables == null) {
                    variables = new ArrayList<>();
                }

                // check if the variable has a power factor
                if (i + 1 < lineEq.length() && lineEq.charAt(i + 1) == ('^')) {
                    i += 2; // to skip to digit
                    int powerSign = 1;
                    if (lineEq.charAt(i) == '-') {
                        powerSign = -1;
                        i++;
                    }
                    int power;
                    power = lineEq.charAt(i) - '0';
                    while (i + 1 < lineEq.length() && Character.isDigit(lineEq.charAt(i + 1))) {
                        power = power * 10 + lineEq.charAt(i + 1) - '0';
                        i++;
                    }
                    power = power == 0 ? 0 : powerSign * power;
                    variables.add(new Variable(var, power));
                } else {
                    variables.add(new Variable(var, 1));
                }

                // if it is end of line or the next character isn't a letter, then this term is completed
                if (i + 1 == lineEq.length() || !Character.isLetter(lineEq.charAt(i + 1))) {
                    coefficient = (sign * coefficient) == -0.0 ? 0.0 : (sign * coefficient);
                    Term term = new Term(coefficient, variables);
                    // if the term contains only variable(s) that have to
                    // the power of zero only, add their coefficients to sum of number
                    if (term.getVariableString() != null && term.getVariableString().isEmpty()) {
                        sum += coefficient ;
                    } else {
                        if (!inBracketStack.isEmpty()) {
                            inBracketStack.addFirst(term);
                        } else {
                            if (!termMap.containsKey(term.getVariableString())) {
                                termMap.put(term.getVariableString(), term.getCoefficient());
                            } else {
                                Double previousCoefficient = termMap.get(term.getVariableString());
                                Double updatedCoefficient = term.getCoefficient() + previousCoefficient;
                                updatedCoefficient =  updatedCoefficient == -0.0 ? 0.0 :  updatedCoefficient;
                                termMap.put(term.getVariableString(), updatedCoefficient);
                            }
                        }
                    }
                    // reset variables
                    sign = 1;
                    coefficient = 1.0;
                    variables = null;
                }
            }

        }

        // if sum is not zero, put a key "#" that maps to the sum of numbers
        if (sum != 0.0) {
            ArrayList<Variable> sumTermVariable = new ArrayList<>();
            sumTermVariable.add(new Variable("#", 1));
            Term sumTerm = new Term(sum, sumTermVariable);
            termMap.put(sumTerm.getVariableString(), sumTerm.getCoefficient());
        }
        return termMap;
    }

    /**
     * Validates input String
     */
    public boolean isValidInput(String input) {
        String singleTerm = "[(]*([0-9]+(\\.[0-9]+)?)*([a-z]+(\\^[-]*[0-9]+)?)*[)]*";
        String equation = singleTerm + "([\\+-]" + singleTerm + ")*";
        return input.replaceAll(" ", "").matches(
                equation + "=" + equation);
    }
}
