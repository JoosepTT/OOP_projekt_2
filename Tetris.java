package oop2;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.geometry.Pos;
import java.io.*;
import java.util.*;
import javafx.animation.PauseTransition;
import javafx.util.Duration;

// mängu menüü
public class Tetris extends Application {

    private static final String skoori_fail = "src/Skoorid.txt";
    private static Map<String, Integer> skoorid = new LinkedHashMap<>();
    public int mängijate_arv = 1;
    public static String mängija_1_nimi;
    public static String mängija_2_nimi;

    public static void main(String[] args) {
        laeSkoorid();
        launch(args);
    }

    @Override
    public void start(Stage peaLava) {
        // menüü nelja nupu seadistamine
        peaLava.setTitle("Mängu menüü");

        Button üheMängijaga = new Button("Alusta üksikmängu");
        Button kaheMängijaga = new Button("Alusta duelli");
        Button skooriTabel = new Button("Vaata skooritabelit");
        Button sulgemiseNupp = new Button("Välju");

        üheMängijaga.setStyle("-fx-background-color: ORANGE;");
        kaheMängijaga.setStyle("-fx-background-color: ORANGE;");
        skooriTabel.setStyle("-fx-background-color: ORANGE;");
        sulgemiseNupp.setStyle("-fx-background-color: ORANGE;");

        üheMängijaga.setOnAction(e -> alustaTavamangu(peaLava));
        kaheMängijaga.setOnAction(e -> alustaDuelli(peaLava));
        skooriTabel.setOnAction(e -> kuvaSkoorid(peaLava));
        sulgemiseNupp.setOnAction(e -> {
            System.out.println("Mäng lõpetatud.");
            salvestaSkoorid();
            peaLava.close();
            javafx.application.Platform.exit();
        });

        VBox menüü = new VBox(15);
        menüü.setStyle("-fx-background-color: black;");
        menüü.setAlignment(Pos.CENTER);
        menüü.getChildren().addAll(üheMängijaga, kaheMängijaga, skooriTabel, sulgemiseNupp);

        Scene stseen = new Scene(menüü, 400, 350);
        peaLava.setScene(stseen);
        peaLava.show();
    }

    private void alustaTavamangu(Stage peaLava) {
        // ühe mängijaga mängu alusamine
        Label nimeSisestus = new Label("Sisesta oma nimi:");
        nimeSisestus.setTextFill(Color.ORANGE);
        TextField nimeSisestusVäli = new TextField();
        Label tagasiside = new Label();
        tagasiside.setTextFill(Color.ORANGE);
        Button startNupp = new Button("Alusta mängu");

        VBox vb = new VBox(10, nimeSisestus, nimeSisestusVäli, tagasiside, startNupp);
        vb.setAlignment(Pos.CENTER);
        vb.setStyle("-fx-background-color: black;");
        vb.setPadding(new javafx.geometry.Insets(20));

        Scene nimeStseen = new Scene(vb, 400, 250);
        peaLava.setScene(nimeStseen);

        startNupp.setOnAction(e -> {
            String nimi = nimeSisestusVäli.getText().trim();
            try {
                validateName(nimi);
                mängija_1_nimi = nimi.toLowerCase();
                mängijate_arv = 1;
                tagasiside.setText("Tere, " + mängija_1_nimi + "! Kohe alustame mänguga...");

                PauseTransition paus = new PauseTransition(Duration.seconds(2));
                paus.setOnFinished(ev -> alustaMängu(peaLava, mängija_1_nimi, null));
                paus.play();
            } catch (ViganeNimiErind ex) {
                tagasiside.setText(ex.getMessage());
            }
        });
    }

    private void alustaDuelli(Stage peaLava) {
        // kahe mängijaga mängu alustamine
        Mängupaneel.mängijaid = 2;
        Label m1 = new Label("Mängija 1 nimi:");
        m1.setTextFill(Color.ORANGE);
        TextField m1Sisend = new TextField();
        Label m2 = new Label("Mängija 2 nimi:");
        m2.setTextFill(Color.CYAN);
        TextField m2Sisend = new TextField();
        Label tagasiSide = new Label();
        tagasiSide.setTextFill(Color.ORANGE);
        Button startNupp = new Button("Alusta duelli");

        VBox vb = new VBox(10, m1, m1Sisend, m2, m2Sisend, tagasiSide, startNupp);
        vb.setAlignment(Pos.CENTER);
        vb.setStyle("-fx-background-color: black;");
        vb.setPadding(new javafx.geometry.Insets(20));

        Scene nimeStseen = new Scene(vb, 400, 300);
        peaLava.setScene(nimeStseen);

        startNupp.setOnAction(e -> {
            try {
                String nimi1 = m1Sisend.getText().trim();
                String nimi2 = m2Sisend.getText().trim();

                validateName(nimi1);
                validateName(nimi2);

                if (nimi1.equalsIgnoreCase(nimi2)) {
                    throw new ViganeNimiErind("Mängijatel peavad olema erinevad nimed!");
                }

                mängija_1_nimi = nimi1.toLowerCase();
                mängija_2_nimi = nimi2.toLowerCase();
                mängijate_arv = 2;

                tagasiSide.setText("Duell " + mängija_1_nimi + " ja " + mängija_2_nimi + " vahel...");

                PauseTransition paus = new PauseTransition(Duration.seconds(2));
                paus.setOnFinished(ev -> alustaMängu(peaLava, mängija_1_nimi, mängija_2_nimi));
                paus.play();
            } catch (ViganeNimiErind ex) {
                tagasiSide.setText(ex.getMessage());
            }
        });
    }

    private void alustaMängu(Stage peaLava, String mängija1, String mängija2) {
        // mängu tööle panemine
        mängijate_arv = (mängija2 == null) ? 1 : 2;
        mängija_1_nimi = mängija1;
        mängija_2_nimi = mängija2;

        Mängupaneel mängupaneel = new Mängupaneel();
        mängupaneel.setPeaLava(peaLava);

        Scene mänguStseen = new Scene(mängupaneel, Mängupaneel.laius, Mängupaneel.kõrgus);
        Klahvihaldur kh = new Klahvihaldur();
        mänguStseen.setOnKeyPressed(kh);
        mänguStseen.setOnKeyReleased(kh);

        peaLava.setTitle("Tetris - " + ((mängijate_arv == 1) ? "Üksikmäng" : "Duell"));
        peaLava.setScene(mänguStseen);
        peaLava.setResizable(false);
        peaLava.centerOnScreen();
        peaLava.show();

        mängupaneel.requestFocus();
    }

    private void kuvaSkoorid(Stage peaLava) {
        // skooride kuvamine
        VBox vb = new VBox(10);
        vb.setStyle("-fx-background-color: black;");
        vb.setPadding(new javafx.geometry.Insets(20));

        int rank = 1;
        for (Map.Entry<String, Integer> entry : skoorid.entrySet()) {
            String rida = rank + ". " + entry.getKey() + " - " + entry.getValue();
            Label skoorid = new Label(rida);
            skoorid.setTextFill(Color.CYAN);
            vb.getChildren().add(skoorid);
            rank++;
        }

        ScrollPane sp = new ScrollPane(vb);
        sp.setFitToWidth(true);
        sp.setStyle("-fx-background-color: orange;");
        sp.setPrefHeight(250);

        Button tagasiNupp = new Button("Tagasi");
        tagasiNupp.setOnAction(e -> start(peaLava));

        VBox vbox = new VBox(15, sp, tagasiNupp);
        vbox.setAlignment(Pos.CENTER);
        vbox.setStyle("-fx-background-color: black;");
        vbox.setPadding(new javafx.geometry.Insets(20));

        Scene skooriStseen = new Scene(vbox, 400, 350);
        peaLava.setScene(skooriStseen);
    }

    private static void laeSkoorid() {
        // skooride failist lugemine
        Map<String, Integer> sorteerimataSkoorid = new HashMap<>();

        try (BufferedReader br = new BufferedReader(new FileReader(skoori_fail))) {
            String rida;
            while ((rida = br.readLine()) != null) {
                String[] osad = rida.split(" ; ");
                if (osad.length == 2) {
                    sorteerimataSkoorid.put(osad[0], Integer.parseInt(osad[1]));
                }
            }
        } catch (IOException e) {
            System.out.println("Skoori faili ei leitud!");
        }

        skoorid.putAll(sorteerimataSkoorid);
        sorteeriSkoore();
    }

    private static void salvestaSkoorid() {
        // skooride faili kirjutamine
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(skoori_fail))) {
            for (Map.Entry<String, Integer> skoor : skoorid.entrySet()) {
                bw.write(skoor.getKey() + " ; " + skoor.getValue());
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Viga faili kirjutamisel!");
        }
    }

    public static void uuendaSkoori(String mangijaNimi, int uusSkoor) {
        // failis olevate skooride uuendamine
        int praeguneParimSkoor = skoorid.getOrDefault(mangijaNimi, 0);
        if (uusSkoor > praeguneParimSkoor) {
            skoorid.put(mangijaNimi, uusSkoor);
            sorteeriSkoore();
            salvestaSkoorid();
        }
    }

    private static void sorteeriSkoore() {
        // skooride sorteerimine
        List<Map.Entry<String, Integer>> sorteeritud = new ArrayList<>(skoorid.entrySet());
        sorteeritud.sort((a, b) -> b.getValue().compareTo(a.getValue()));

        skoorid.clear();
        for (Map.Entry<String, Integer> skoor : sorteeritud) {
            skoorid.put(skoor.getKey(), skoor.getValue());
        }
    }

    private void validateName(String nimi) throws ViganeNimiErind {
        // kui nimi on tühi
        if (nimi.isEmpty()) {
            throw new ViganeNimiErind("Palun sisesta nimi.");
        }
        // Kui nimi on liiga pikk
        if (nimi.length() > 20) {
            throw new ViganeNimiErind("Nimi on liiga pikk (max 20 tähemärki).");
        }
        if (!nimi.matches("[a-z\\-]+")) {
            // Kui sisaldab suuri tähti
            if (nimi.matches(".*[A-Z].*")) {
                throw new ViganeNimiErind("Nimi võib sisaldada ainult väiketähti!");
            }
            // Kui sisaldab muid sümboleid peale sidekriipsu
            if (!nimi.matches("[a-zA-Z\\-]+")) {
                throw new ViganeNimiErind("Nimes ei tohi olla sümboleid, välja arvatud sidekriips!");
            }
        }
    }
}