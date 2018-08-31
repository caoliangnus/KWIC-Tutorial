package com.company;

import java.util.List;

public class Output {

    private Pipe inputPipe;

    public Output(Pipe inputPipe){
        this.inputPipe = inputPipe;
    }

    public void printAllSortedLines() {

        List<String> sortedLines = inputPipe.getData();

        System.out.println("These are the shifted and sorted lines:");

        for (String line : sortedLines) {
            System.out.println(line);
        }

        System.out.println("Press Ctrl D to end the program");
        System.out.println("Please type in the line(s):");
    }
}
