package com.example.oop_rt2;

import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class Klahvihaldur implements EventHandler<KeyEvent> {

    public static boolean klahv_üles, klahv_alla, klahv_vasakule, klahv_paremale;
    public static boolean pausSees = false;

    @Override
    public void handle(KeyEvent e) {
        if (e.getEventType() == KeyEvent.KEY_PRESSED) {
            if (e.getCode() == KeyCode.W) klahv_üles = true;
            if (e.getCode() == KeyCode.A) klahv_vasakule = true;
            if (e.getCode() == KeyCode.S) klahv_alla = true;
            if (e.getCode() == KeyCode.D) klahv_paremale = true;

            // tühiku vajutamine on lüliti funktsioonis
            if (e.getCode() == KeyCode.SPACE) {
                if (!pausSees) {
                    pausSees = true;
                } else if (pausSees) {
                    pausSees = false;
                }
            }
        }

        if (e.getEventType() == KeyEvent.KEY_RELEASED) {
            if (e.getCode() == KeyCode.W) klahv_üles = false;
            if (e.getCode() == KeyCode.A) klahv_vasakule = false;
            if (e.getCode() == KeyCode.S) klahv_alla = false;
            if (e.getCode() == KeyCode.D) klahv_paremale = false;
        }
    }
}