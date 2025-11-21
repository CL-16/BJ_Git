package com.example.blackjackgame117;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class Table extends Application {

    @Override
    public void start(Stage stage) throws Exception {

        FXMLLoader loader = new FXMLLoader(Table.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(loader.load());

        stage.setTitle("BlackJack");
        stage.setScene(scene);

        // ===== FULLSCREEN MODE =====
        stage.setFullScreen(true);
        stage.setFullScreenExitHint(""); // remove ESC message

        // (Optional) prevent resizing
        stage.setResizable(false);

        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
