package com.jun.canonicalizer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

/**
 * Created by Jun on 2017-02-25.
 * Transform equation into canonical form
 */
public class Canonicalizer {
    public static void main(String[] args) {
        while (true) {
            try {
                Scanner scanner = new Scanner(System.in);
                String mode = "";
                System.out.println("Enter 'f' for File Mode 'i' for Interactive Mode.");
                String choice = scanner.nextLine();

                if (choice.equals("f") || choice.equals("F")) {
                    System.out.println("Enter Filename:");
                    String fileName = scanner.nextLine();
                    FileReader fileReader = new FileReader(fileName);
                    BufferedReader bufferedReader = new BufferedReader(fileReader);


                    FileWriter fileWriter = new FileWriter("output.out");
                    BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

                    String line = null;
                    while ((line = bufferedReader.readLine()) != null) {
                        if (isValidInput(line)) {

                        } else {
                            System.out.println("This input is invalid.");
                        }

//                        System.out.println(line);
//                        String output = createOutput(parseInput(line));
//                        System.out.println(output);
//                        bufferedWriter.write(output);
                    }

                } else if (choice.equals("i") || choice.equals("I")) {


                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * Validates input String
     */
    public static boolean isValidInput(String input) {
      //  String singleTerm = "[\\+-][(]*([0-9]+)?(\\.[0-9]+)) |";
        String singleTerm = "[(]*([0-9]+(\\.[0-9]+)?)*([a-z]+(\\^[0-9]+)?)*[)]*";
        String equation = singleTerm + "([\\+-]"
                + singleTerm + ")*";
        return input.replaceAll(" ", "").matches(
                equation + "=" + equation);
    }
}
