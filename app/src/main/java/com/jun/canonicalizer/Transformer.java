package com.jun.canonicalizer;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by Jun on 2017-02-27.
 * Transform equation into canonical form
 */
public class Transformer {

    protected static final String UNEVEN_BRACKETS_ERROR_MSG = "The equation doesn't have an even number of brackets.";

    public Transformer() {

    }

    /**
     * Transform an equation to its canonical form
     */
    public String transformEq(String equation) {
        String[] sides = equation.split("=");

        String leftSide = sides[0].trim();
        String rightSide = sides[1].trim();

        HashMap<String, Double> leftTermMap= buildTermMap(leftSide);
        if (leftTermMap.containsKey("!"))
            return UNEVEN_BRACKETS_ERROR_MSG;
        HashMap<String, Double> rightTermMap= buildTermMap(rightSide);
        if (rightTermMap.containsKey("!"))
            return UNEVEN_BRACKETS_ERROR_MSG;

        return combineTerms(leftTermMap, rightTermMap);
    }

    /**
     * Combine terms we get on left and right of the equation and return all the terms as a string
     */
    public String combineTerms(HashMap<String, Double> leftTermMap, HashMap<String, Double> rightTermMap) {
        StringBuilder transformedSb = new StringBuilder();
        Iterator<Map.Entry<String, Double>> rightMapEntries = rightTermMap.entrySet().iterator();
        // Combine all the terms into one map
        while (rightMapEntries.hasNext()) {
            Map.Entry<String, Double> entry = rightMapEntries.next();
            Double coefficient = entry.getValue();
            if (leftTermMap.containsKey(entry.getKey())) {
                leftTermMap.put(entry.getKey(), leftTermMap.get(entry.getKey()) - coefficient);
            } else {
                leftTermMap.put(entry.getKey(), -1.0 * coefficient);
            }
        }

        Iterator<Map.Entry<String, Double>> leftMapEntries = leftTermMap.entrySet().iterator();
        boolean notFirstTerm = false;
        while (leftMapEntries.hasNext()) {
            Map.Entry<String, Double> entry = leftMapEntries.next();
            Double coefficient = entry.getValue();

            if (coefficient != 0) {
                // to determine which sign to include
                if (coefficient > 0 && !notFirstTerm) {
                    notFirstTerm = true;
                } else if(coefficient > 0 && notFirstTerm){
                    transformedSb.append(" + ");
                } else if (coefficient < 0 && !notFirstTerm){
                    transformedSb.append("- ");
                    notFirstTerm = true;
                    coefficient *= -1;
                } else {
                    transformedSb.append(" - ");
                    coefficient *= -1;
                }

                if (coefficient == 1.0) {
                    // if it is a number, append only the coefficient
                    if (!entry.getKey().equals("#"))
                        transformedSb.append(entry.getKey());
                    else
                        transformedSb.append('1');
                    continue;
                }

                if (coefficient % 1 == 0)
                    transformedSb.append(coefficient.intValue());
                else
                    transformedSb.append(coefficient);

                if (!entry.getKey().equals("#"))
                    transformedSb.append(entry.getKey());
            }
        }

        if (transformedSb.length() == 0) {
            transformedSb.append("0");
        }
        transformedSb.append(" = 0");
        return transformedSb.toString();
    }

    /**
     * Build a HashMap that maps variables to their coefficient and sum of numbers to "#"
     * if a variable has a power 0, its coefficient contributes to the sum of number
     * Main Idea of the algorithm to buildTermMap in Transformer.java as following:
     * - Go through each character in the equation and categorize each character into coefficient, term, power or number.
     * - Use 3 stacks to handle brackets:
     * - one stack to keep track of number
     * - two stacks to keep track of all the other terms
     * - runtime: O(n) and space: O(n)
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
        int countLeftBracket = 0;
        int countRightBracket = 0;

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
                countLeftBracket++;
                // set variables of the term to null to represent sign
                Term signTerm = new Term(1.0 * sign, null);
                inBracketStack.addFirst(signTerm);
                numberStack.addFirst(sum);
                numberStack.addFirst(1.0 * sign);
                sum = 0.0;
                sign = 1;
            } else if (lineEq.charAt(i) == ')') {
                countRightBracket++;
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

        // for checking if there are even and odd numbers of brackets
        if (countLeftBracket != countRightBracket) {
            ArrayList<Variable> bracketTermVariable = new ArrayList<>();
            bracketTermVariable.add(new Variable("!", 1));
            Term bracketTerm = new Term(1.0, bracketTermVariable);
            termMap.put(bracketTerm.getVariableString(), bracketTerm.getCoefficient());
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
