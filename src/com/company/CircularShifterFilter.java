package com.company;

import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

public class CircularShifterFilter implements Runnable {

    private LinkedBlockingQueue<String> outputPipe;
    private LinkedBlockingQueue<String> inputPipe;
    private List<String>  wordsToIgnore;

    public CircularShifterFilter(LinkedBlockingQueue<String> inputPipe, LinkedBlockingQueue<String> outputPipe, List<String> wordsToIgnore){
        this.outputPipe = outputPipe;
        this.inputPipe = inputPipe;
        this.wordsToIgnore = wordsToIgnore;
    }

    private void circularShift() {

        try {
            String inputLine = inputPipe.take();
            String[] words = getWordsFromLine(inputLine);
            for(int i = 0; i < words.length; i++) {
                words = shiftLeft(words);
                if(!wordsToIgnore.contains(words[0])) {
                    String sentence = String.join(" ", words);

                    //Check for fullstop not at end
                    while (sentence.contains(".") && (sentence.indexOf(".") != sentence.length() - 1)) {
                        //Regex syntax
                        sentence = sentence.replaceFirst("\\.", "");
                    }

                    //Remove comma if at end
                    while (sentence.contains(",") && (sentence.lastIndexOf(",") == sentence.length() - 1)) {
                        sentence = sentence.substring(0, sentence.lastIndexOf(","));
                    }

                    outputPipe.put(sentence);
                }
            }
        }catch (Exception e){
            System.out.println(e.getStackTrace());
        }
    }

    private String[] getWordsFromLine(String line) {
        return line.split("\\s+");
    }

    private String[] shiftLeft(String[] words) {

        String[] shiftedWords = new String[words.length]; //Temp array
        if (words.length > 1) {
            //Copy (2nd word onwards) to shiftedWords then copy first word to the last index of shiftedWords
            System.arraycopy(words, 1, shiftedWords, 0, words.length - 1);
            System.arraycopy(words, 0, shiftedWords, shiftedWords.length - 1, 1);
        } else {
            return words;
        }
        return shiftedWords;
    }

    @Override
    public void run(){
        while(true){
            circularShift();
        }
    }

}
