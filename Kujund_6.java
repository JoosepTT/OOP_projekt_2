package oop2;

import javafx.scene.paint.Color;

public class Kujund_6 extends Klotsid {

    public Kujund_6(boolean isPlayer1) {
        super(isPlayer1);
        looKujund(Color.RED);
    }

    public void määraXY(int x, int y) {
        aktiivne = true;
        mitteaktiivne = false;
        //   o
        // o o
        // o
        a[0].x = x;
        a[0].y = y;
        a[1].x = a[0].x;
        a[1].y = a[0].y - Plokk.plokiSuurus;
        a[2].x = a[0].x - Plokk.plokiSuurus;
        a[2].y = a[0].y;
        a[3].x = a[0].x - Plokk.plokiSuurus;
        a[3].y = a[0].y + Plokk.plokiSuurus;
    }

    public void getSuund1() {
        //   o
        // o o
        // o
        b[0].x = a[0].x;
        b[0].y = a[0].y;
        b[1].x = a[0].x;
        b[1].y = a[0].y - Plokk.plokiSuurus;
        b[2].x = a[0].x - Plokk.plokiSuurus;
        b[2].y = a[0].y;
        b[3].x = a[0].x - Plokk.plokiSuurus;
        b[3].y = a[0].y + Plokk.plokiSuurus;

        uuendaXY(1);
    }

    public void getSuund2() {
        // o o
        //   o o
        //
        b[0].x = a[0].x;
        b[0].y = a[0].y;
        b[1].x = a[0].x + Plokk.plokiSuurus;
        b[1].y = a[0].y;
        b[2].x = a[0].x;
        b[2].y = a[0].y - Plokk.plokiSuurus;
        b[3].x = a[0].x - Plokk.plokiSuurus;
        b[3].y = a[0].y - Plokk.plokiSuurus;

        uuendaXY(2);
    }

    public void getSuund3() {
        getSuund1();
    }

    public void getSuund4() {
        getSuund2();
    }

}
