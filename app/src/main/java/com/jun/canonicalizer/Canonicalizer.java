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
        while (true) {
            try {
                Scanner scanner = new Scanner(System.in);
                String mode = "";
                System.out.println("Enter 'f' for File Mode 'i' for Interactive Mode.");
                String choice = scanner.nextLine();
                Transformer transformer = new Transformer();

                if (choice.equals("f") || choice.equals("F")) {
                    System.out.println("Enter Filename:");
                    String fileName = scanner.nextLine();
                    FileReader fileReader = new FileReader(fileName);
                    BufferedReader bufferedReader = new BufferedReader(fileReader);


                    FileWriter fileWriter = new FileWriter("output.out");
                    BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

                    String lineEq = null;
                    while ((lineEq = bufferedReader.readLine()) != null) {
                        if (transformer.isValidInput(lineEq)) {
                            String res = transformer.transformEq(transformer.parseEq(lineEq));
                            System.out.print(lineEq + " => " + res);
                            bufferedWriter.write(res);

                        } else {
                            System.out.println("This input is invalid.");
                            bufferedWriter.write("This input is invalid.");
                        }
                    }
                    bufferedReader.close();
                    bufferedWriter.close();

                } else if (choice.equals("i") || choice.equals("I")) {
                    System.out.println("Enter Equation:");
                    String equation = scanner.nextLine();
                    if (transformer.isValidInput(equation)) {
                        System.out.println(transformer.transformEq(transformer.parseEq(equation)));
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
