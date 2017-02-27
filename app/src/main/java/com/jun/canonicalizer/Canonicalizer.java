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
    public static void main(String[] args) {
        String mode = "";
        Scanner scanner = new Scanner(System.in);
        Transformer transformer = new Transformer();
        while (true) {
            try {
                if (mode != null && mode.isEmpty()) {
                    //Scanner scanner = new Scanner(System.in);
                    System.out.println("Enter 'f' for File Mode 'i' for Interactive Mode.");
                    mode = scanner.nextLine();
                }

                if (mode.equals("f") || mode.equals("F")) {
                    System.out.println("Enter Filename:");
                    String fileName = scanner.nextLine();
                    FileReader fileReader = new FileReader(fileName);
                    BufferedReader bufferedReader = new BufferedReader(fileReader);

                    FileWriter fileWriter = new FileWriter("output.out");
                    BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

                    String lineEq;
                    while ((lineEq = bufferedReader.readLine()) != null) {
                        if (transformer.isValidInput(lineEq)) {
                            String res = transformer.transformEq(lineEq);
                            System.out.print(lineEq + " => " + res);
                            bufferedWriter.write(res);

                        } else {
                            System.out.println("This input is invalid.");
                            bufferedWriter.write("This input is invalid.");
                        }
                    }
                    bufferedReader.close();
                    bufferedWriter.close();

                } else if (mode.equals("i") || mode.equals("I")) {
                    System.out.println("Enter Equation:");
                    String equation = scanner.nextLine();
                    if (transformer.isValidInput(equation)) {
                        System.out.println(transformer.transformEq(equation));
                    } else {
                        System.out.println("This input is invalid.");
                    }
                } else {
                    System.out.println("No such option.");
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
