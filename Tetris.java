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
        Mängupaneel mängupaneel = new Mängupaneel();
        mängupaneel.setPeaLava(peaLava);

        Scene stseen = new Scene(mängupaneel, Mängupaneel.laius, Mängupaneel.kõrgus);

        Klahvihaldur kh = new Klahvihaldur();
        stseen.setOnKeyPressed(kh);
        stseen.setOnKeyReleased(kh);

        peaLava.setTitle("Tetris");
        peaLava.setScene(stseen);
        peaLava.setResizable(false);
        peaLava.centerOnScreen();
        peaLava.show();

        // Vajaliku fookuse määramine, et klahvid töötaksid
        mängupaneel.requestFocus();
    }
}