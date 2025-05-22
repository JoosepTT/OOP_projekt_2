package com.example.oop_rt2;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Menüüpaneel extends VBox {
    private Stage peaLava;
    private Button mänguNupp;
    private Button skooriNupp;
    private Button sulgemisNupp;
    private static final String skoorideFail = "skoorid.txt";

    public Menüüpaneel(Stage peaLava) {
        this.peaLava = peaLava;

        setAlignment(Pos.CENTER);
        setSpacing(20);

        mänguNupp = new Button("Play");
        skooriNupp = new Button("Scores");
        sulgemisNupp = new Button("Exit");

        setStyle("-fx-background-color: black;");
        mänguNupp.setStyle("-fx-background-color: ORANGE;");
        skooriNupp.setStyle("-fx-background-color: CYAN;");
        sulgemisNupp.setStyle("-fx-background-color: ORANGE;");

        mänguNupp.setPrefWidth(200);
        skooriNupp.setPrefWidth(200);
        sulgemisNupp.setPrefWidth(200);

        mänguNupp.setOnAction(e -> mängijaNimeSisestus());
        skooriNupp.setOnAction(e -> skoorid());
        sulgemisNupp.setOnAction(e -> System.exit(0));

        getChildren().addAll(mänguNupp, skooriNupp, sulgemisNupp);
    }

    private void mängijaNimeSisestus() {
        Stage nimed = new Stage();
        nimed.initModality(Modality.APPLICATION_MODAL);
        nimed.initOwner(peaLava);

        VBox vb = new VBox(10);
        vb.setAlignment(Pos.CENTER);
        vb.setPadding(new javafx.geometry.Insets(20));
        vb.setStyle("-fx-background-color: black;");

        Label mängija1 = new Label("Mängija 1 nimi: ");
        mängija1.setTextFill(Color.ORANGE);
        TextField mängija1Sisestus = new TextField();
        mängija1Sisestus.setPromptText("Sisesta nimi: ");

        Label mängija2 = new Label("Mängija 2 nimi: ");
        mängija2.setTextFill(Color.CYAN);
        TextField mängija2Sisestus = new TextField();
        mängija2Sisestus.setPromptText("Sisesta nimi: ");

        Button alustaMängu = new Button("Alusta mängu");
        alustaMängu.setOnAction(e -> {
            String mängija1Nimi = mängija1Sisestus.getText().isEmpty() ? "Mängija 1" : mängija1Sisestus.getText();
            String mängija2Nimi = mängija2Sisestus.getText().isEmpty() ? "Mängija 2" : mängija2Sisestus.getText();
            alustaMängu(mängija1Nimi, mängija2Nimi);
            nimed.close();
        });

        vb.getChildren().addAll(
                mängija1, mängija1Sisestus,
                mängija2, mängija2Sisestus,
                alustaMängu
        );

        Scene dialogScene = new Scene(vb, 300, 250);
        nimed.setScene(dialogScene);
        nimed.setTitle("Sisesta mängija nimed");
        nimed.showAndWait();
    }

    private void alustaMängu(String player1Name, String player2Name) {
        Mängupaneel mängupaneel = new Mängupaneel(player1Name, player2Name);
        mängupaneel.setPeaLava(peaLava);

        Scene stseen = new Scene(mängupaneel, Mängupaneel.laius, Mängupaneel.kõrgus);

        Klahvihaldur kh = new Klahvihaldur();
        stseen.setOnKeyPressed(kh);
        stseen.setOnKeyReleased(kh);

        peaLava.setScene(stseen);
        peaLava.centerOnScreen();

        mängupaneel.requestFocus();
    }

    private void skoorid() {
        Stage skoorid = new Stage();
        skoorid.initModality(Modality.APPLICATION_MODAL);
        skoorid.initOwner(peaLava);

        VBox vb = new VBox(10);
        vb.setAlignment(Pos.CENTER);
        vb.setPadding(new javafx.geometry.Insets(20));
        vb.setStyle("-fx-background-color: black;");

        Label skoorideEdetabel = new Label("Skoorid");
        skoorideEdetabel.setStyle("-fx-font-size: 20; -fx-font-weight: bold;");
        skoorideEdetabel.setTextFill(Color.ORANGE);

        List<String> skoor = laeSkoorid();
        VBox skoorideList = new VBox(5);
        skoorideList.setAlignment(Pos.CENTER_LEFT);

        if (skoor.isEmpty()) {
            Label tühi = new Label("Veel ei ole skoore!");
            tühi.setTextFill(Color.CYAN);
            skoorideList.getChildren().add(tühi);
        } else {
            for (String skr : skoor) {
                Label skooridOn = new Label(skr);
                skooridOn.setTextFill(Color.CYAN);
                skoorideList.getChildren().add(skooridOn);
            }
        }

        Button sulgemisNupp = new Button("Sulge");
        sulgemisNupp.setOnAction(e -> skoorid.close());

        vb.getChildren().addAll(skoorideEdetabel, skoorideList, sulgemisNupp);

        Scene skoorideStseen = new Scene(vb, 300, 400);
        skoorid.setScene(skoorideStseen);
        skoorid.setTitle("Skoorid");
        skoorid.showAndWait();
    }

    public static void salvestaSkoor(String mängijaNimi, int skoor) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(skoorideFail, true))) {
            bw.write(mängijaNimi + ": " + skoor);
            bw.newLine();
        } catch (IOException e) {
            System.err.println("Salvestamisel tekkis tõrge: " + e.getMessage());
        }
    }

    private List<String> laeSkoorid() {
        List<String> skoorid = new ArrayList<>();
        File file = new File(skoorideFail);

        if (!file.exists()) {
            return skoorid;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(skoorideFail))) {
            String rida;
            while ((rida = br.readLine()) != null) {
                skoorid.add(rida);
            }
        } catch (IOException e) {
            System.err.println("Lugemisel tekkis tõrge: " + e.getMessage());
        }

        return skoorid;
    }
}