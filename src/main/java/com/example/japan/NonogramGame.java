// NonogramGame.java (исправленная версия с вертикальными подсказками)
package com.example.japan;

import javafx.scene.layout.*;
import javafx.scene.canvas.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.scene.input.MouseButton;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.reverse;

public class NonogramGame {
    private static final int CELL_SIZE = 30;

    private NonogramBoard board;
    private Canvas canvas;
    private boolean gameWon = false;
    private final int ClueRowSize;
    private final int ClueColSize;

    public NonogramGame(File file) throws Exception {
        this.board = NonogramFileLoader.loadFromFile(file);
        ClueRowSize = board.maxClues(board.getRowClues());
        ClueColSize = board.maxClues(board.getColClues());
    }

    public Pane createContent() {
        Pane root = new Pane();

        int width = ClueRowSize*CELL_SIZE + board.getCols() * CELL_SIZE + 20;
        int height = ClueColSize*CELL_SIZE + board.getRows() * CELL_SIZE + 20;

        canvas = new Canvas(width, height);
        root.getChildren().add(canvas);

        drawBoard();

        // Обработка кликов
        canvas.setOnMouseClicked(event -> {
            if (gameWon) return;

            double x = event.getX();
            double y = event.getY();

            // Проверяем, попали ли в игровое поле
            if (x >= ClueRowSize*CELL_SIZE && y >= ClueColSize*CELL_SIZE) {
                int col = (int) ((x - ClueRowSize*CELL_SIZE) / CELL_SIZE);
                int row = (int) ((y - ClueColSize*CELL_SIZE) / CELL_SIZE);

                if (row < board.getRows() && col < board.getCols()) {
                    Cell cell = board.getCell(row, col);

                    if (event.getButton() == MouseButton.PRIMARY) {
                        cell.toggleState();
                    } else if (event.getButton() == MouseButton.SECONDARY) {
                        cell.toggleLock();
                    }

                    drawBoard();

                    // Проверка победы
                    if (board.checkWin()) {
                        gameWon = true;
                        showWinMessage();
                    }
                }
            }
        });

        return root;
    }

    private void drawBoard() {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

        // Рисуем подсказки для столбцов (сверху)
        drawColumnClues(gc);

        // Рисуем подсказки для строк (слева)
        drawRowClues(gc);

        // Рисуем игровое поле
        drawGrid(gc);

        // Рисуем клетки
        drawCells(gc);
    }

    private void drawColumnClues(GraphicsContext gc) {
        gc.setFill(Color.BLACK);
        gc.setFont(new Font(14));
        gc.setTextAlign(TextAlignment.CENTER);

        List<List<Integer>> colClues = board.getColClues();

        for (int col = 0; col < board.getCols(); col++) {
            List<Integer> clues = colClues.get(col);

            // Рисуем цифры вертикально снизу вверх
            double startX = ClueRowSize*CELL_SIZE + col * CELL_SIZE + CELL_SIZE / 2;
            double startY = ClueColSize*CELL_SIZE - CELL_SIZE/2;

            for (int i = clues.size() - 1; i >= 0; i--) {
                int clue = clues.get(i);
                double y = startY - (clues.size() - 1 - i) * CELL_SIZE;
                gc.fillText(String.valueOf(clue), startX, y);
            }
        }
    }

    private void drawRowClues(GraphicsContext gc) {
        gc.setFill(Color.BLACK);
        gc.setFont(new Font(14));
        gc.setTextAlign(TextAlignment.RIGHT);

        List<List<Integer>> rowClues = board.getRowClues();

        for (int row = 0; row < board.getRows(); row++) {
            List<Integer> clues = rowClues.get(row);

            // Рисуем цифры горизонтально справа налево
            double startX = ClueRowSize*CELL_SIZE - CELL_SIZE / 2;
            double startY = ClueColSize*(CELL_SIZE) + (row)*CELL_SIZE + CELL_SIZE/2;

            for (int i = 0; i < clues.size(); i++) {
                int clue = clues.get(i);

                double x = startX - i*CELL_SIZE;
                gc.fillText(String.valueOf(clue), x, startY);

            }
        }
    }

    private void drawGrid(GraphicsContext gc) {
        gc.setStroke(Color.GRAY);
        gc.setLineWidth(1);

        // Вертикальные линии
        for (int col = 0; col <= board.getCols(); col++) {
            double x = (ClueRowSize + col) * CELL_SIZE;
            double y1 = ClueColSize*CELL_SIZE;
            double y2 = ClueColSize*CELL_SIZE + board.getRows() * CELL_SIZE;

            // Каждые 5 линий делаем толще
            if (col % 5 == 0) {
                gc.setLineWidth(2);
            } else {
                gc.setLineWidth(1);
            }

            gc.strokeLine(x, y1, x, y2);
        }

        // Горизонтальные линии
        for (int row = 0; row <= board.getRows(); row++) {
            double y = (ClueColSize + row) * CELL_SIZE;
            double x1 = ClueRowSize*CELL_SIZE;
            double x2 = ClueRowSize*CELL_SIZE + board.getCols() * CELL_SIZE;

            // Каждые 5 линий делаем толще
            if (row % 5 == 0) {
                gc.setLineWidth(2);
            } else {
                gc.setLineWidth(1);
            }

            gc.strokeLine(x1, y, x2, y);
        }
    }

    private void drawCells(GraphicsContext gc) {
        for (int row = 0; row < board.getRows(); row++) {
            for (int col = 0; col < board.getCols(); col++) {
                Cell cell = board.getCell(row, col);
                double x = (ClueRowSize + col) * CELL_SIZE;
                double y = (ClueColSize + row) * CELL_SIZE;

                switch (cell.getState()) {
                    case FILLED:
                        gc.setFill(Color.BLACK);
                        gc.fillRect(x + 1, y + 1, CELL_SIZE - 2, CELL_SIZE - 2);
                        break;
                    case MARKED:
                        gc.setFill(Color.WHITE);
                        gc.fillRect(x + 1, y + 1, CELL_SIZE - 2, CELL_SIZE - 2);
                        gc.setFill(Color.LIGHTGRAY);
                        gc.fillOval(x + CELL_SIZE/4, y + CELL_SIZE/4,
                                CELL_SIZE/2, CELL_SIZE/2);
                        break;
                    case EMPTY:
                        gc.setFill(Color.WHITE);
                        gc.fillRect(x + 1, y + 1, CELL_SIZE - 2, CELL_SIZE - 2);
                        break;
                }

                // Рамка для заблокированных клеток
                if (cell.isLocked()) {
                    gc.setStroke(Color.RED);
                    gc.setLineWidth(2);
                    gc.strokeRect(x + 2, y + 2, CELL_SIZE - 4, CELL_SIZE - 4);
                }
            }
        }
    }

    private void showWinMessage() {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Поздравляем!");
        alert.setHeaderText("Вы победили!");
        alert.setContentText("Вы успешно разгадали японский кроссворд!");
        alert.showAndWait();
    }
}