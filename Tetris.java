package com.example.oop_rt2;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Tetris extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage peaLava) {
        Menüüpaneel menüüpaneel = new Menüüpaneel(peaLava);
        Scene menuScene = new Scene(menüüpaneel, 300, 400);

        peaLava.setTitle("Tetris");
        peaLava.setScene(menuScene);
        peaLava.setResizable(false);
        peaLava.centerOnScreen();
        peaLava.show();

    }
}