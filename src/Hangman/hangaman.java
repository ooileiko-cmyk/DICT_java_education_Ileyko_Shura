package Hangman;

import java.util.Random;
import java.util.Scanner;

public class hangaman {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String[] words = {"python", "java", "javascript", "kotlin"};
        Random random = new Random();

        while (true) {
            System.out.println("HANGMAN");
            System.out.print("Type \"play\" to play the game, \"exit\" to quit: > ");
            String command = scanner.nextLine().trim();

            if (command.equalsIgnoreCase("exit")) {
                break;
            } else if (!command.equalsIgnoreCase("play")) {
                // Если введено что-то другое, меню выводится снова
                continue;
            }

            // Выбор случайного слова
            String word = words[random.nextInt(words.length)];
            StringBuilder guessedState = new StringBuilder();

            // Первые 2 буквы открыты, остальные закрыты дефисами
            for (int i = 0; i < word.length(); i++) {
                if (i < 2) {
                    guessedState.append(word.charAt(i));
                } else {
                    guessedState.append("-");
                }
            }

            int attempts = 8;
            // Строка для отслеживания уже введенных букв
            StringBuilder triedLetters = new StringBuilder();

            while (attempts > 0) {
                System.out.println(guessedState);
                System.out.print("Input a letter: > ");
                String input = scanner.nextLine();

                // Проверка: ровно ли один символ
                if (input.length() != 1) {
                    System.out.println("You should input a single letter");
                    continue;
                }

                char letter = input.charAt(0);

                // Проверка: строчная ли это английская буква
                if (letter < 'a' || letter > 'z') {
                    System.out.println("Please enter a lowercase English letter");
                    continue;
                }

                // Проверка: вводилась ли эта буква ранее
                if (triedLetters.indexOf(String.valueOf(letter)) != -1) {
                    System.out.println("You've already guessed this letter");
                    continue;
                }

                // Добавляем букву в список опробованных
                triedLetters.append(letter);

                // Проверяем, есть ли буква в загаданном слове
                if (word.indexOf(letter) == -1) {
                    System.out.println("That letter doesn't appear in the word");
                    attempts--;
                } else {
                    boolean improved = false;
                    for (int i = 0; i < word.length(); i++) {
                        if (word.charAt(i) == letter && guessedState.charAt(i) == '-') {
                            guessedState.setCharAt(i, letter);
                            improved = true;
                        }
                    }
                    if (!improved) {
                        System.out.println("No improvements");
                        attempts--;
                    }
                }

                // Проверка на победу
                if (guessedState.toString().equals(word)) {
                    System.out.println("You guessed the word " + word + "!");
                    System.out.println("You survived!");
                    break;
                }
            }

            // Проверка на поражение
            if (attempts == 0 && !guessedState.toString().equals(word)) {
                System.out.println("You lost!");
            }
        }
        scanner.close();
    }
}
