package oop2;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.scene.control.Button;

public class Mängupaneel extends Pane implements Runnable {
    public static final int laius = 1280;
    public static final int kõrgus = 720;
    public static final int fps = 60;
    Thread mänguLõim;
    public static int mängijaid = 1; // vaikimisi üksikmäng

    private Canvas lõuend;
    private GraphicsContext gc;
    private Mänguhaldur mh1;
    private Mänguhaldur mh2;
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
        getChildren().add(lõuend);

        if (mängijaid == 1) {
            // ühe mängijaga
            mh1 = new Mänguhaldur(laius / 3, true);
            käivitaMäng();

        } else {
            // Mõlemad mängud
            mh1 = new Mänguhaldur(0, true); // Mängija 1 vasakul
            mh2 = new Mänguhaldur(laius / 2, false); // Mängija 2 paremal

            mh1.setVastane(mh2);
            mh2.setVastane(mh1);

            käivitaMäng();
        }
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

        while (mänguLõim != null && !mh1.kasMängOnLäbi && (mh2 == null || !mh2.kasMängOnLäbi)) {
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

        // Kui mäng lõppes
        javafx.application.Platform.runLater(() -> {
            kuvaLõpuAken();
        });
    }

    // mänguinfo uudendatakse siis, kui mäng pole pausil
    private void uuendaInfot() {
        if (!Klahvihaldur.pausSees && !mh1.kasMängOnLäbi && (mh2 == null || !mh2.kasMängOnLäbi)) {
            mh1.uuenda();
            if (mh2 != null) mh2.uuenda();
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
        if (mh2 != null) {
            gc.setStroke(Color.WHITE);
            gc.setLineWidth(2);
            gc.strokeLine(laius / 2, 0, laius / 2, kõrgus);
        }

        // Mängud
        mh1.joonista(gc);
        if (mh2 != null) mh2.joonista(gc);

        // Kirjeldused
        if (mh2 != null) {
            gc.setFill(Color.WHITE);
            gc.setFont(new Font(20));
            gc.fillText(Tetris.mängija_1_nimi, laius / 4 - 80, 30);
            gc.fillText(Tetris.mängija_2_nimi, 3 * laius / 4 - 100, 30);
        } else {
            gc.setFill(Color.WHITE);
            gc.setFont(new Font(20));
            gc.fillText(Tetris.mängija_1_nimi, laius / 2, 30);
        }
    }

    private void kuvaLõpuAken() {
        VBox lõppPaneel = new VBox(15);
        lõppPaneel.setStyle("-fx-background-color: black;");
        lõppPaneel.setPrefSize(laius, kõrgus);
        lõppPaneel.setAlignment(Pos.CENTER);

        Label lõppTekst;

        if (mängijaid == 1) {
            int mängijaSkoor = mh1.getSkoor();
            Tetris.uuendaSkoori(Tetris.mängija_1_nimi, mängijaSkoor);
            lõppTekst = new Label("Mäng on lõppenud!\nSinu skoor: " + mängijaSkoor);
        } else {
            int mangija1Skoor = mh1.getSkoor();
            int mangija2Skoor = mh2.getSkoor();
            Tetris.uuendaSkoori(Tetris.mängija_1_nimi, mangija1Skoor);
            Tetris.uuendaSkoori(Tetris.mängija_2_nimi, mangija2Skoor);
            lõppTekst = new Label("Mäng on lõppenud!\n" + Tetris.mängija_1_nimi + " Sinu skoor: " + mangija1Skoor +
                    "\n" + Tetris.mängija_2_nimi + " Sinu skoor: " + mangija2Skoor);
        }

        lõppTekst.setFont(new Font(30));
        lõppTekst.setTextFill(Color.WHITE);

        Button tagasiBtn = new Button("Naase menüüsse");
        tagasiBtn.setOnAction(e -> {
            try {
                new Tetris().start(peaLava);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        lõppPaneel.getChildren().addAll(lõppTekst, tagasiBtn);
        Scene lõpuStseen = new Scene(lõppPaneel, laius, kõrgus);
        peaLava.setScene(lõpuStseen);
    }

}
