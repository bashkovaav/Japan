// NonogramBoard.java
package com.example.japan;

import com.example.japan.Cell;

import java.util.ArrayList;
import java.util.List;

public class NonogramBoard {
    private final int rows;
    private final int cols;
    private Cell[][] cells;
    private boolean[][] solution;
    private List<List<Integer>> rowClues;
    private List<List<Integer>> colClues;

    public NonogramBoard(int rows, int cols, boolean[][] solution) {
        this.rows = rows;
        this.cols = cols;
        this.solution = solution;
        this.cells = new Cell[rows][cols];
        initializeCells();
        calculateClues();
    }

    private void initializeCells() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                cells[i][j] = new Cell(i, j);
            }
        }
    }

    private void calculateClues() {
        // Вычисляем подсказки для строк
        rowClues = new ArrayList<>();
        for (int i = 0; i < rows; i++) {
            rowClues.add(calculateLineClues(solution[i]));
        }

        // Вычисляем подсказки для столбцов
        colClues = new ArrayList<>();
        for (int j = 0; j < cols; j++) {
            boolean[] column = new boolean[rows];
            for (int i = 0; i < rows; i++) {
                column[i] = solution[i][j];
            }
            colClues.add(calculateLineClues(column));
        }
    }

    private List<Integer> calculateLineClues(boolean[] line) {
        List<Integer> clues = new ArrayList<>();
        int count = 0;

        for (boolean cell : line) {
            if (cell) {
                count++;
            } else if (count > 0) {
                clues.add(count);
                count = 0;
            }
        }

        if (count > 0) {
            clues.add(count);
        }

        // Если вся строка/столбец пустые, добавляем 0
        if (clues.isEmpty()) {
            clues.add(0);
        }

        return clues;
    }

    // Проверка победы
    public boolean checkWin() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                boolean shouldBeFilled = solution[i][j];
                boolean isFilled = cells[i][j].getState() == Cell.State.FILLED;
                if (shouldBeFilled != isFilled) {
                    return false;
                }
            }
        }
        return true;
    }

    public int maxClues(List<List<Integer>> clues) {
        int maxClue = 1;
        for (int i = 0; i < clues.size(); i++) {
            int nowClueSize = clues.get(i).size();
            maxClue = Math.max(maxClue, nowClueSize);
        }
        return maxClue;
    }

    // Геттеры
    public int getRows() { return rows; }
    public int getCols() { return cols; }
    public Cell getCell(int row, int col) { return cells[row][col]; }
    public List<List<Integer>> getRowClues() { return rowClues; }
    public List<List<Integer>> getColClues() { return colClues; }
    public boolean[][] getSolution() { return solution; }

    // Методы для отладки
    public void printSolution() {
        System.out.println("Solution:");
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                System.out.print(solution[i][j] ? "1" : "0");
            }
            System.out.println();
        }
    }

    public void printClues() {
        System.out.println("Row clues:");
        for (int i = 0; i < rowClues.size(); i++) {
            System.out.println("Row " + i + ": " + rowClues.get(i));
        }

        System.out.println("Column clues:");
        for (int i = 0; i < colClues.size(); i++) {
            System.out.println("Col " + i + ": " + colClues.get(i));
        }
    }
}