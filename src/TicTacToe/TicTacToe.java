package TicTacToe;

import java.util.Scanner;

public class TicTacToe {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Ввод строки от пользователя
        System.out.print("Enter cells: ");
        String input = scanner.nextLine();

        // Замена символа '_' на пробел для удобного вывода
        String str = input.replace('_', ' ');

        // Превращаем строку в двумерный массив 3х3
        char[][] board = new char[3][3];
        int index = 0;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                board[i][j] = str.charAt(index++);
            }
        }

        // Печать игрового поля (Этап 2)
        printBoard(board);

        // Анализ состояния игры (Этап 3)
        analyzeGameState(board, input);

        scanner.close();
    }

    // Метод для вывода игровой сетки в консоль
    public static void printBoard(char[][] board) {
        System.out.println("---------");
        for (int i = 0; i < 3; i++) {
            System.out.print("| ");
            for (int j = 0; j < 3; j++) {
                System.out.print(board[i][j] + " ");
            }
            System.out.println("|");
        }
        System.out.println("---------");
    }

    // Метод анализа состояния игры
    public static void analyzeGameState(char[][] board, String originalInput) {
        int countX = 0;
        int countO = 0;
        int countEmpty = 0;

        // Подсчёт количества 'X', 'O' и пустых клеток
        for (char c : originalInput.toCharArray()) {
            if (c == 'X') countX++;
            else if (c == 'O') countO++;
            else countEmpty++;
        }

        // Проверка победных линий
        boolean xWins = checkWin(board, 'X');
        boolean oWins = checkWin(board, 'O');

        // Логика определения результата:
        // 1. Невозможное состояние (Impossible):
        //    - Одновременно победили и X, и O
        //    - Разница в количестве ходов X и O >= 2
        if ((xWins && oWins) || Math.abs(countX - countO) >= 2) {
            System.out.println("Impossible");
        }
        // 2. Победил X
        else if (xWins) {
            System.out.println("X wins");
        }
        // 3. Победил O
        else if (oWins) {
            System.out.println("O wins");
        }
        // 4. Игра не окончена (есть пустые клетки)
        else if (countEmpty > 0) {
            System.out.println("Game not finished");
        }
        // 5. Ничья (нет пустых клеток и никто не выиграл)
        else {
            System.out.println("Draw");
        }
    }

    // Проверка, есть ли 3 одинаковых символа в ряд/столбец/диагональ
    public static boolean checkWin(char[][] b, char symbol) {
        // Проверка строк и столбцов
        for (int i = 0; i < 3; i++) {
            if ((b[i][0] == symbol && b[i][1] == symbol && b[i][2] == symbol) ||
                    (b[0][i] == symbol && b[1][i] == symbol && b[2][i] == symbol)) {
                return true;
            }
        }
        // Проверка диагоналей
        if ((b[0][0] == symbol && b[1][1] == symbol && b[2][2] == symbol) ||
                (b[0][2] == symbol && b[1][1] == symbol && b[2][0] == symbol)) {
            return true;
        }

        return false;
    }
}