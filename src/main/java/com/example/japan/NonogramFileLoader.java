package com.example.japan;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class NonogramFileLoader {

    public static NonogramBoard loadFromFile(File file) throws IOException {
        List<String> lines = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line.trim());
            }
        }

        if (lines.isEmpty()) {
            throw new IOException("Файл пуст");
        }

        int rows = lines.size();
        int cols = lines.get(0).length();
        boolean[][] solution = new boolean[rows][cols];

        for (int i = 0; i < rows; i++) {
            String currentLine = lines.get(i);
            if (currentLine.length() != cols) {
                throw new IOException("Несовпадение размеров строк в файле");
            }

            for (int j = 0; j < cols; j++) {
                char c = currentLine.charAt(j);
                if (c == '1' || c == 'X' || c == '█') {
                    solution[i][j] = true;
                } else if (c == '0' || c == '.' || c == ' ') {
                    solution[i][j] = false;
                } else {
                    throw new IOException("Недопустимый символ в файле: " + c);
                }
            }
        }

        return new NonogramBoard(rows, cols, solution);
    }
}