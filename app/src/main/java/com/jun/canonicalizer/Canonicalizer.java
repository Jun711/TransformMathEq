package com.jun.canonicalizer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

/**
 * Created by Jun on 2017-02-25.
 * Run Transformer to transform equations to canonical forms
 */
public class Canonicalizer {

    protected static final String MODE_SELECTION_MSG = "Enter 'f' for File Mode 'i' for Interactive Mode.";
    protected static final String CHANGE_MODE_FROM_FILE_MSG = "Enter Filename or Enter 'c' to change mode:";
    protected static final String CHANGE_MODE_FROM_INT_MSG = "Enter Equation or Enter 'c' to change mode:";
    protected static final String UNSUPPORTED_INPUT_MSG = "This input is not supported.";
    protected static final String NO_SUCH_MODE_MSG = "Sorry, no such mode.";

    public static void main(String[] args) {
        String mode = "";
        Scanner scanner = new Scanner(System.in);
        Transformer transformer = new Transformer();
        while (true) {
            try {
                if (mode.isEmpty()) {
                    System.out.println(MODE_SELECTION_MSG);
                    mode = scanner.nextLine();
                }

                if (mode.equals("f") || mode.equals("F")) {
                    System.out.println(CHANGE_MODE_FROM_FILE_MSG);
                    String fileName = scanner.nextLine();
                    if (fileName.equals("c")) {
                        mode = "";
                        continue;
                    }
                    FileReader fileReader = new FileReader(fileName);
                    BufferedReader bufferedReader = new BufferedReader(fileReader);

                    FileWriter fileWriter = new FileWriter("output.out");
                    BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

                    String lineEq;
                    while ((lineEq = bufferedReader.readLine()) != null) {
                        if (transformer.isValidInput(lineEq)) {
                            String res = transformer.transformEq(lineEq);
                            System.out.print(lineEq + " => " + res);
                            System.out.println();
                            bufferedWriter.write(res);
                        } else {
                            System.out.println(UNSUPPORTED_INPUT_MSG);
                            bufferedWriter.write(UNSUPPORTED_INPUT_MSG);
                        }
                        bufferedWriter.newLine();
                    }
                    bufferedReader.close();
                    bufferedWriter.close();

                } else if (mode.equals("i") || mode.equals("I")) {
                    System.out.println(CHANGE_MODE_FROM_INT_MSG);
                    String equation = scanner.nextLine();
                    if (equation.equals("c")) {
                        mode = "";
                        continue;
                    }
                    if (transformer.isValidInput(equation)) {
                        System.out.println(transformer.transformEq(equation));
                    } else {
                        System.out.println(UNSUPPORTED_INPUT_MSG);
                    }
                } else {
                    System.out.println(NO_SUCH_MODE_MSG);
                    mode = "";
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}