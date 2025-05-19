package com.example.oop_rt2;

import javafx.scene.paint.Color;

public class Kujund_5 extends Klotsid {

    public Kujund_5() {
        looKujund(Color.YELLOW);
    }

    public void määraXY(int x, int y) {
        // o o
        // o o
        //
        a[0].x = x;
        a[0].y = y;
        a[1].x = a[0].x;
        a[1].y = a[0].y + Plokk.plokiSuurus;
        a[2].x = a[0].x + Plokk.plokiSuurus;
        a[2].y = a[0].y;
        a[3].x = a[0].x + Plokk.plokiSuurus;
        a[3].y = a[0].y + Plokk.plokiSuurus;
    }

    // Ruut on igas orientatsioonis sama kujuga, seega pole vaja positsioone ümnber defineerida.

}