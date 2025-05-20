package com.example.oop_rt2;

import javafx.event.EventHandler;
import javafx.scene.input.KeyEvent;

public class Klahvihaldur implements EventHandler<KeyEvent> {
    // Mängija 1 (WASD)
    public static boolean klahv_üles, klahv_alla, klahv_vasakule, klahv_paremale;
    // Mängija 2 (Nooled)
    public static boolean klahv_üles2, klahv_alla2, klahv_vasakule2, klahv_paremale2;

    public static boolean pausSees = false;

    @Override
    public void handle(KeyEvent e) {
        if (e.getEventType() == KeyEvent.KEY_PRESSED) {
            switch (e.getCode()) {
                // Mängija 1
                case W: klahv_üles = true; break;
                case A: klahv_vasakule = true; break;
                case S: klahv_alla = true; break;
                case D: klahv_paremale = true; break;

                // Mängija 2
                case UP: klahv_üles2 = true; break;
                case LEFT: klahv_vasakule2 = true; break;
                case DOWN: klahv_alla2 = true; break;
                case RIGHT: klahv_paremale2 = true; break;

                case SPACE: pausSees = !pausSees; break;
            }
        }

        if (e.getEventType() == KeyEvent.KEY_RELEASED) {
            switch (e.getCode()) {
                // Mängija 1
                case W: klahv_üles = false; break;
                case A: klahv_vasakule = false; break;
                case S: klahv_alla = false; break;
                case D: klahv_paremale = false; break;

                // Mängija 2
                case UP: klahv_üles2 = false; break;
                case LEFT: klahv_vasakule2 = false; break;
                case DOWN: klahv_alla2 = false; break;
                case RIGHT: klahv_paremale2 = false; break;
            }
        }
    }
}