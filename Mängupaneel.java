package com.example.oop_rt2;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class Mängupaneel extends Pane implements Runnable {

    public static final int laius = 1280;
    public static final int kõrgus = 720;
    public static final int fps = 60;
    Thread mänguLõim;

    private Canvas lõuend;
    private GraphicsContext gc;
    private Mänguhaldur mh;
    private Stage peaLava;

    public void setPeaLava(Stage lava) {
        this.peaLava = lava;
    }

    public Mängupaneel() {
        // paneeli seaded
        setPrefSize(laius, kõrgus);
        setStyle("-fx-background-color: black;");

        lõuend = new Canvas(laius, kõrgus);
        gc = lõuend.getGraphicsContext2D();
        this.getChildren().add(lõuend);

        mh = new Mänguhaldur();

        käivitaMäng();
    }

    public void käivitaMäng() {
        mänguLõim = new Thread(this);
        mänguLõim.start(); // lõime käivitamisel käivitatakse automaatselt ka run meetod
    }

    // mängutsükkel
    @Override
    public void run() {
        double joonistusIntervall = 1000000000.0 / fps;
        double delta = 0;
        long viimaneAeg = System.nanoTime();
        long praeguneAeg;

        while (mänguLõim != null && !mh.kasMängOnLäbi) {
            praeguneAeg = System.nanoTime();
            delta += (praeguneAeg - viimaneAeg) / joonistusIntervall;
            viimaneAeg = praeguneAeg;

            if (delta >= 1) {
                javafx.application.Platform.runLater(() -> {
                    uuendaInfot();
                    kuvaKomponent();
                });
                delta--;
            }
        }
    }

    // mänguinfo uudendatakse siis, kui mäng pole pausil
    private void uuendaInfot() {
        if (Klahvihaldur.pausSees == false && mh.kasMängOnLäbi == false) {
            mh.uuenda();
        }
        /*
        else {
            if (peaLava != null && mh.kasMängOnLäbi == true) {
                peaLava.close();
            }
        }
        */
    }

    private void kuvaKomponent() {
        gc.clearRect(0, 0, laius, kõrgus); // lõuend tühjendatakse
        mh.joonista(gc); // mängu elementide kuvamine
    }

}