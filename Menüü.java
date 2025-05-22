package com.example.oop_rt2;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.geometry.Pos;

import java.io.*;
import java.util.*;

import javafx.animation.PauseTransition;
import javafx.scene.control.TextField;
import javafx.util.Duration;

public class Menüü extends Application {

    private static final String failinimi = "src/Skoorid.txt";
    private static Map<String, Integer> skoorid = new LinkedHashMap<>(); // linkedhashmap on sõnastik, mis sisaldab nime võtme ja skoori selle väärtusena. Linked tähendab, et elementide järjekord jäetakse meelde, mis võimaldab elemente edaspidi väärtuste järgi sorteerida.
    Scanner scanner = new Scanner(System.in); // skänneri ülesseadmine
    public int mängijaid = 1;

    public static void main(String[] args) {
        laadiSkoorid(); // faili sisselugemine
        launch(args);   // käivitab JavaFX-i
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Mängu menüü");

        // Loo nupud
        Button btn1 = new Button("Alusta üksikmängu");
        Button btn2 = new Button("Alusta duelli");
        Button btn3 = new Button("Vaata skooritabelit");
        Button btn4 = new Button("Välju");

        // Lisa igale nupule tegevus
        btn1.setOnAction(e -> alustaTavamangu(primaryStage));
        btn2.setOnAction(e -> alustaDuelli(primaryStage));
        btn3.setOnAction(e -> kuvaSkoorid(primaryStage));
        btn4.setOnAction(e -> {
            System.out.println("Mäng lõpetatud.");
            salvestaSkoor();
            primaryStage.close();
            javafx.application.Platform.exit();
        });

        // Paiguta nupud vertikaalselt keskele
        VBox vbox = new VBox(10); // 10px vahe nuppude vahel
        vbox.setAlignment(Pos.CENTER);
        vbox.getChildren().addAll(btn1, btn2, btn3, btn4);

        // Loo stseen ja kuva
        Scene scene = new Scene(vbox, 400, 300);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void laadiSkoorid() {
        Map<String, Integer> sorteerimata = new HashMap<>();

        try (BufferedReader br = new BufferedReader(new FileReader(failinimi))) {
            String rida;

            while ((rida = br.readLine()) != null) {
                String[] osad = rida.split(" ; ");
                if (osad.length == 2) {
                    sorteerimata.put(osad[0], Integer.parseInt(osad[1]));
                }
            }

        } catch (IOException e) {
            System.out.println("Skoorifaili ei leitud.");
        }

        skoorid.putAll(sorteerimata); // Esmalt laadime skoorid isendivälja, et teine meetod saaks neid kasutada
        sorteeriSkoorid();  // Seejärel sorteerime need
    }

    public static void salvestaSkoor() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(failinimi))) {
            for (Map.Entry<String, Integer> sisend : skoorid.entrySet()) {
                bw.write(sisend.getKey() + " ; " + sisend.getValue());
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Tekkis viga skoorifaili salvestamisel.");
        }
    }

    public void alustaTavamangu(Stage primaryStage) {
        Label juhis = new Label("Sisesta oma nimi:");
        TextField nimiSisend = new TextField();
        Label tagasiside = new Label();
        Button alustaBtn = new Button("Alusta");

        VBox layout = new VBox(10, juhis, nimiSisend, tagasiside, alustaBtn);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new javafx.geometry.Insets(20));

        Scene nimiScene = new Scene(layout, 400, 250);
        primaryStage.setScene(nimiScene);

        alustaBtn.setOnAction(e -> {
            String mangijaNimi = nimiSisend.getText().trim().toLowerCase();

            if (mangijaNimi.isEmpty()) {
                tagasiside.setText("Palun sisesta nimi:");
                return;
            }

            int korgeimSkoor = skoorid.getOrDefault(mangijaNimi, 0);

            Mängupaneel.mangijaNimi = mangijaNimi;

            if (skoorid.containsKey(mangijaNimi.toLowerCase())) {
                tagasiside.setText("Tere tulemast tagasi, " + mangijaNimi + "! Sinu kõrgeim skoor on " + korgeimSkoor + ".");
            } else {
                tagasiside.setText("Tere tulemast, " + mangijaNimi + "! Alustame mängu...");
            }

            // Ootame 2 sekundit ja alustame mängu
            PauseTransition paus = new PauseTransition(Duration.seconds(2));
            paus.setOnFinished(ev -> {
                Mängupaneel.mängijaid = 1;
                Mängupaneel mängupaneel = new Mängupaneel();
                mängupaneel.setPeaLava(primaryStage);

                Scene stseen = new Scene(mängupaneel, Mängupaneel.laius, Mängupaneel.kõrgus);

                // Lisa klahvihaldur
                Klahvihaldur kh = new Klahvihaldur();
                stseen.setOnKeyPressed(kh);
                stseen.setOnKeyReleased(kh);

                primaryStage.setTitle("Tetris – Üksikmäng");
                primaryStage.setScene(stseen);
                primaryStage.setResizable(false);
                primaryStage.centerOnScreen();
                primaryStage.show();

                mängupaneel.requestFocus();
            });
            paus.play();

        });
    }


    public static void alustaDuelli(Stage primaryStage) {
        Mängupaneel.mängijaid = 2; // Kahe mängijaga mäng

        Mängupaneel mängupaneel = new Mängupaneel();
        mängupaneel.setPeaLava(primaryStage);

        Scene stseen = new Scene(mängupaneel, Mängupaneel.laius, Mängupaneel.kõrgus);

        Klahvihaldur kh = new Klahvihaldur();
        stseen.setOnKeyPressed(kh);
        stseen.setOnKeyReleased(kh);

        primaryStage.setTitle("Tetris – Duell");
        primaryStage.setScene(stseen);
        primaryStage.setResizable(false);
        primaryStage.centerOnScreen();
        primaryStage.show();

        mängupaneel.requestFocus();
    }


    public void kuvaSkoorid(Stage primaryStage) {
        VBox skooriBox = new VBox(5);
        skooriBox.setPadding(new javafx.geometry.Insets(10));

        int number = 1;
        for (Map.Entry<String, Integer> sissekanne : skoorid.entrySet()) {
            String rida = number + ". " + sissekanne.getKey() + " - " + sissekanne.getValue();
            Label skooriLabel = new Label(rida);
            skooriBox.getChildren().add(skooriLabel);
            number++;
        }

        ScrollPane scrollPane = new ScrollPane(skooriBox);
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefHeight(250);

        Button tagasiBtn = new Button("Naase menüüsse");
        tagasiBtn.setOnAction(e -> {
            // Tagasi peamenüüle
            start(primaryStage); // uuesti käivitame menüü
        });

        VBox layout = new VBox(15, scrollPane, tagasiBtn);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new javafx.geometry.Insets(20));

        Scene skooriScene = new Scene(layout, 400, 300);
        primaryStage.setScene(skooriScene);
    }


    // nimede skooride järgi järjestamine
    private static void sorteeriSkoorid() {
        List<Map.Entry<String, Integer>> sorteeritud = new ArrayList<>(skoorid.entrySet());
        sorteeritud.sort((a, b) -> b.getValue().compareTo(a.getValue())); // Väärtuste järgi kahanevas järjekorras

        // Uuendame skooride kaarti, et hoida sorteeritud järjekorda
        skoorid.clear();
        for (Map.Entry<String, Integer> entry : sorteeritud) {
            skoorid.put(entry.getKey(), entry.getValue());
        }
    }

    // skoori värskendamine
    protected static void uuendaSkoori(String mangijaNimi, int uusSkoor) {
        int kõrgemSkoor = Math.max(skoorid.getOrDefault(mangijaNimi, 0), uusSkoor); // kui uus skoor on suurem kui praegune, siis uuendatakse mängija skoori
        skoorid.put(mangijaNimi, kõrgemSkoor);
    }

}