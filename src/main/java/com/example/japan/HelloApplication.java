package com.example.japan;// Main.java

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.stage.FileChooser;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import java.io.File;

public class HelloApplication extends Application {

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Японский кроссворд");

        // Выбор файла при запуске
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Выберите файл с кроссвордом");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Nonogram Files", "*.nono")
        );

        File file = fileChooser.showOpenDialog(primaryStage);

        if (file != null) {
            try {
                NonogramGame game = new NonogramGame(file);
                Scene scene = new Scene(game.createContent());
                primaryStage.setScene(scene);
                primaryStage.setResizable(false);
                primaryStage.show();
            } catch (Exception e) {
                showError("Ошибка загрузки файла: " + e.getMessage());
            }
        } else {
            primaryStage.close();
        }
    }

    private void showError(String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Ошибка");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}