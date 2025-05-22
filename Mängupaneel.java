package com.example.oop_rt2;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class Mängupaneel extends Pane implements Runnable {
    public static final int laius = 1280;
    public static final int kõrgus = 720;
    public static final int fps = 60;
    Thread mänguLõim;

    private Canvas lõuend;
    private GraphicsContext gc;
    private Mänguhaldur mh1;
    private Mänguhaldur mh2;
    private Stage peaLava;

    private String player1Name;
    private String player2Name;

    public void setPeaLava(Stage lava) {
        this.peaLava = lava;
    }

    public Mängupaneel(String player1Name, String player2Name) {
        this.player1Name = player1Name;
        this.player2Name = player2Name;

        // paneeli seaded
        setPrefSize(laius, kõrgus);
        setStyle("-fx-background-color: black;");

        lõuend = new Canvas(laius, kõrgus);
        gc = lõuend.getGraphicsContext2D();
        getChildren().add(lõuend);

        // Mõlemad mängud
        mh1 = new Mänguhaldur(0, true); // Mängija 1 vasakul
        mh2 = new Mänguhaldur(laius / 2, false); // Mängija 2 paremal

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

        while (mänguLõim != null && !mh1.kasMängOnLäbi && !mh2.kasMängOnLäbi) {
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

        if (mh1.kasMängOnLäbi || mh2.kasMängOnLäbi) {
            Menüüpaneel.salvestaSkoor(player1Name, mh1.skoor);
            Menüüpaneel.salvestaSkoor(player2Name, mh2.skoor);
        }
    }

    // mänguinfo uudendatakse siis, kui mäng pole pausil
    private void uuendaInfot() {
        if (!Klahvihaldur.pausSees && !mh1.kasMängOnLäbi && !mh2.kasMängOnLäbi) {
            mh1.uuenda();
            mh2.uuenda();
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
        gc.clearRect(0, 0, laius, kõrgus);

        // Keskelt poolitav joon
        gc.setStroke(Color.WHITE);
        gc.setLineWidth(2);
        gc.strokeLine(laius / 2, 0, laius / 2, kõrgus);

        // Mängud
        mh1.joonista(gc);
        mh2.joonista(gc);

        // Kirjeldused
        gc.setFill(Color.WHITE);
        gc.setFont(new Font(20));
        gc.fillText(player1Name, laius / 4 - 80, 30);
        gc.fillText(player2Name, 3 * laius / 4 - 100, 30);
    }
}