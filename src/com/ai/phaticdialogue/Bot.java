package com.ai.phaticdialogue;

import java.io.*;
import java.util.*;

public class Bot {
    private Map<String, List<String>> allSentences;
    private Map<String, String> wordsToTransform;

    private static final String ANSWERS_FILE_NAME = "answers.txt";
    private static final String QUESTION_FILE_NAME = "questions.txt";
    private static final String GREETINGS_FILE_NAME = "greetings.txt";
    private static final String GREETING_QUESTION_FILE_NAME = "greetingQuestion.txt";
    private static final String BYES_FILE_NAME = "byes.txt";
    private static final String WORD_TO_TRANSFORM = "words-to-transform.txt";

    private ArrayList<String> filesPaths;

    public Bot() {
        init();
        listenChat();
    }

    private void listenChat() {
        listenForGreeting();
        listen();
    }

    private void listen() {
        String input;
        String inputLowerCase;

        while (true) {
            input = ConsoleHelper.readString();
            inputLowerCase = input.toLowerCase();

            //checking the goodbyes
            if (allSentences.get(BYES_FILE_NAME).contains(inputLowerCase)) {
                ConsoleHelper.writeMessage(input);
                break;
            }

            //checking for questions
            if (input.contains("?")) {
                writeAnswer(input);
            } else {
                askQuestion(input);
            }
        }
    }

    private void askQuestion(String input) {
        String[] inputArr = input.split("\\s");

        if(inputArr.length == 1 && !inputArr[0].contains("*")) {
            askShortQuestionWithNoYes(inputArr[0]);
        } else {
            String keyword = extractWordWithAsterisk(inputArr);
            askQuestionWithKeyWord(keyword);
        }

    }

    private void askShortQuestionWithNoYes(String s) {
        ConsoleHelper.writeMessage("Why " + s + "?");
    }

    private void askQuestionWithKeyWord(String keyword) {
        List<String> tempQuestionList = allSentences.get(QUESTION_FILE_NAME);

        String askPattern = tempQuestionList.get(0);

        ConsoleHelper.writeMessage(putKeyWordInside(askPattern, keyword));

        moveFirstElemToTheEnd(tempQuestionList);
    }

    private void writeAnswer(String input) {
        String[] inputArr = input.replace("?", "").split("\\s");

        String keyword = extractWordWithAsterisk(inputArr);

        answerWithKeyword(keyword);
    }

    public String extractWordWithAsterisk(String[] inputArr) {
        for (String tempStr : inputArr) {
            if (tempStr.contains("*")) {
                return tempStr.replace("*", "");
            }
        }
        return "it";
    }

    private void answerWithKeyword(String keyword) {
        List<String> tempAnswerList = allSentences.get(ANSWERS_FILE_NAME);

        String answerPattern = tempAnswerList.get(0);

        ConsoleHelper.writeMessage(putKeyWordInside(answerPattern, keyword));

        moveFirstElemToTheEnd(tempAnswerList);
    }

    private String putKeyWordInside(String answer, String keyword) {
        if (wordsToTransform.containsKey(keyword.toLowerCase())) {
            keyword = wordsToTransform.get(keyword.toLowerCase());
        }
        return answer.replace("$", keyword);
    }

    private void listenForGreeting() {
        while (true) {
            String userInput = ConsoleHelper.readString();
            String userInputLowerCase = userInput.toLowerCase();

            if (allSentences.get(GREETINGS_FILE_NAME).contains(userInputLowerCase)) {
                ConsoleHelper.writeMessage(userInput);
                break;
            } else {
                List<String> tempGreetingQuestion = allSentences.get(GREETING_QUESTION_FILE_NAME);
                ConsoleHelper.writeMessage(tempGreetingQuestion.get(0));

                moveFirstElemToTheEnd(tempGreetingQuestion);
            }
        }
    }

    private void moveFirstElemToTheEnd(List<String> list) {
        String firstEl = list.get(0);

        list.remove(0);

        list.add(firstEl);
    }

    private void init() {
        wordsToTransform = new HashMap<>();
        allSentences = new HashMap<>();
        filesPaths = new ArrayList<>();

        filesPaths.add(ANSWERS_FILE_NAME);
        filesPaths.add(GREETINGS_FILE_NAME);
        filesPaths.add(GREETING_QUESTION_FILE_NAME);
        filesPaths.add(QUESTION_FILE_NAME);
        filesPaths.add(BYES_FILE_NAME);
        filesPaths.add(WORD_TO_TRANSFORM);

        for (String fileName : filesPaths) {
            try (BufferedReader br = new BufferedReader(new FileReader("txts/" + fileName))) {

                //fill the wordsToTransform map
                if(fileName.equals(WORD_TO_TRANSFORM)) {
                    while(br.ready()) {
                        String input = br.readLine();
                        if(input.length() > 0) {
                            String[] tempArr = input.split("=");
                            wordsToTransform.put(tempArr[0], tempArr[1]);
                        }
                    }
                } else {
                    //fill other lists
                    List<String> tempList = new LinkedList<>();
                    while (br.ready()) {
                        String input = br.readLine();
                        if(input.length() > 0) {
                            tempList.add(input);
                        }

                    }

                    allSentences.put(fileName, tempList);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}