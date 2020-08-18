import java.util.*;

public class SudokuSolver {

    private int[][] board;

    private List<int[][]> solvedBoardsList;

    // Take in the START_BOARD from GUI and set it as the this.board
    public SudokuSolver(int[][] board) {
        this.board = Arrays.stream(board).map(int[]::clone).toArray(int[][]::new);
        solvedBoardsList = new ArrayList<>();
    }

    // Return an ArrayList where each element will be a 2D array that
    // corresponds to each step of the solving process
    public List<int[][]> returnList() {
        solve();
        return solvedBoardsList;
    }



    public boolean solve() {
//        printBoard();
        int[][] currentBoard = Arrays.stream(board).map(int[]::clone).toArray(int[][]::new);
        solvedBoardsList.add(currentBoard);
        int[] tempBoard = findEmpty(board);
        int row;
        int col;
        if (tempBoard[0] == -1 && tempBoard[1] == -1) {
            return true;
        } else {
            row = tempBoard[0];
            col = tempBoard[1];

        }

        for(int i = 1; i < 10; i++) {
            if(valid(board, i, row, col)) {
                board[row][col] = i;
                if(solve()) {
                    return true;
                }
                board[row][col] = 0;
            }
        }
        return false;
    }


    public boolean valid(int[][] board, int num, int row, int col) {
        int[] pos = {row, col};
        // Check row
        for(int i = 0; i < board[0].length; i++) {
            if(board[pos[0]][i] == num && pos[1] != i) {
                return false;
            }
        }
        // Check column
        for(int i = 0; i < board.length; i++) {
            if(board[i][pos[1]] == num && pos[0] != i) {
                return false;
            }
        }

        // Check box
        int boxX = pos[1] / 3;
        int boxY = pos[0] / 3;

        for(int i = boxY * 3; i < boxY * 3 + 3; i++) {
            for(int j = boxX * 3; j < boxX * 3 + 3; j++) {
                if(board[i][j] == num && (i != pos[0] && j != pos[1])) {
                    return false;
                }
            }
        }
        return true;
    }


    private void printBoard() {
        for (int i = 0; i < board.length; i++) {
            if (i % 3 == 0 && i != 0) {
                System.out.println("- - - - - - - - - - - - ");
            }
            for (int j = 0; j < board[0].length; j++) {
                if (j % 3 == 0 && j != 0) {
                    System.out.print(" | ");
                }
                if (j == 8) {
                    System.out.println(board[i][j]);
                } else {
                    System.out.print(board[i][j] + " ");
                }
            }
        }
        System.out.println("-----------------------");
    }

    private int[] findEmpty(int[][] board) {
        int[] newBoard = {-1, -1};
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                if (board[i][j] == 0) {
                    newBoard[0] = i;
                    newBoard[1] = j;
                    return newBoard;
                }
            }
        }
        return newBoard;
    }
}