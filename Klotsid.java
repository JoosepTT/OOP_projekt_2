package com.example.oop_rt2;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Klotsid {

    public Plokk[] a = new Plokk[4];
    public Plokk[] b = new Plokk[4];
    int langemisLuger = 0;
    public int suund = 1; // igal plokil on 4 orientatsiooni (1/2/3/4)
    boolean vasakPõrge, paremPõrge, põhjaPõrge;
    public boolean aktiivne = true;
    public boolean mitteaktiivne;
    int mitteaktiivseteLuger = 0;

    public void looKujund(Color värv) {
        //
        a[0] = new Plokk(värv);
        a[1] = new Plokk(värv);
        a[2] = new Plokk(värv);
        a[3] = new Plokk(värv);

        //
        b[0] = new Plokk(värv);
        b[1] = new Plokk(värv);
        b[2] = new Plokk(värv);
        b[3] = new Plokk(värv);
    }

    public void määraXY(int x, int y) {

    }

    public void uuendaXY(int suund) {

        kontrolliPöördePõrget();
        if (vasakPõrge == false && paremPõrge == false && põhjaPõrge == false) {
            this.suund = suund;
            // klotse saab keerata alles siis, kui parasjagu ühtegi põrget ei toimu
            a[0].x = b[0].x;
            a[0].y = b[0].y;
            a[1].x = b[1].x;
            a[1].y = b[1].y;
            a[2].x = b[2].x;
            a[2].y = b[2].y;
            a[3].x = b[3].x;
            a[3].y = b[3].y;
        }

    }

    public void getSuund1() {

    }
    public void getSuund2() {

    }
    public void getSuund3() {

    }
    public void getSuund4() {

    }

    public void kontrolliLiikumisPõrget() {
        vasakPõrge = false;
        paremPõrge = false;
        põhjaPõrge = false;

        // kokkupõrge staatiliste plokkidega
        kontrolliStaatilistepõrget();

        // kokkupõrke kontroll
        // vasak sein
        for (int i = 0; i < a.length; i++) {
            if (a[i].x == Mänguhaldur.vasak_x) {
                vasakPõrge = true;
            }
        }
        // parem sein
        for (int i = 0; i < a.length; i++) {
            if (a[i].x + Plokk.plokiSuurus == Mänguhaldur.parem_x) {
                paremPõrge = true;
            }
        }
        // põhi
        for (int i = 0; i < a.length; i++) {
            if (a[i].y + Plokk.plokiSuurus == Mänguhaldur.alumine_y) {
                põhjaPõrge = true;
            }
        }
    }

    // laias laastus sama mis liikumispõrke puhul
    public void kontrolliPöördePõrget() {
        vasakPõrge = false;
        paremPõrge = false;
        põhjaPõrge = false;

        // kokkupõrge staatiliste plokkidega
        kontrolliStaatilistepõrget();

        // kokkupõrke kontroll
        // vasak sein
        for (int i = 0; i < a.length; i++) {
            if (b[i].x < Mänguhaldur.vasak_x) {
                vasakPõrge = true;
            }
        }
        // parem sein
        for (int i = 0; i < a.length; i++) {
            if (b[i].x + Plokk.plokiSuurus > Mänguhaldur.parem_x) {
                vasakPõrge = true;
            }
        }
        // põhi
        for (int i = 0; i < a.length; i++) {
            if (b[i].y + Plokk.plokiSuurus > Mänguhaldur.alumine_y) {
                põhjaPõrge = true;
            }
        }
    }

    private void kontrolliStaatilistepõrget() {

        for (int i = 0; i < Mänguhaldur.staatilisedPlokid.size(); i++) {
            int sihtmärk_x = Mänguhaldur.staatilisedPlokid.get(i).x;
            int sihtmärk_y = Mänguhaldur.staatilisedPlokid.get(i).y;

            // alumise osa kontroll
            for (int j = 0; j < a.length; j++) {
                if (a[j].y + Plokk.plokiSuurus == sihtmärk_y && a[j].x == sihtmärk_x) {
                    põhjaPõrge = true;
                }
            }

            // vasaku poole kontroll
            for (int j = 0; j < a.length; j++) {
                if (a[j].y + Plokk.plokiSuurus == sihtmärk_y && a[j].x == sihtmärk_x) {
                    vasakPõrge = true;
                }
            }

            // parema poole kontroll
            for (int j = 0; j < a.length; j++) {
                if (a[j].y + Plokk.plokiSuurus == sihtmärk_y && a[j].x == sihtmärk_x) {
                    paremPõrge = true;
                }
            }
        }
    }

    public void uuenda() {

        if (mitteaktiivne) {
            mitteaktiivne();
        }

        if (Klahvihaldur.klahv_üles) {
            switch (suund) {
                case 1: getSuund2(); break;
                case 2: getSuund3(); break;
                case 3: getSuund4(); break;
                case 4: getSuund1(); break;
            }
            Klahvihaldur.klahv_üles = false;
        }

        kontrolliLiikumisPõrget();

        if (Klahvihaldur.klahv_alla) {
            // kui klotsi küljed ei puuduta põhja, siis lubatakse klotisl allapoole liikuda
            if (põhjaPõrge == false) {
                a[0].y += Plokk.plokiSuurus;
                a[1].y += Plokk.plokiSuurus;
                a[2].y += Plokk.plokiSuurus;
                a[3].y += Plokk.plokiSuurus;

                // pärast allapoole liikumist luger taasseatakse
                langemisLuger = 0;
            }
            Klahvihaldur.klahv_alla = false;
        }

        if (Klahvihaldur.klahv_vasakule) {
            if (vasakPõrge == false) {
                a[0].x -= Plokk.plokiSuurus;
                a[1].x -= Plokk.plokiSuurus;
                a[2].x -= Plokk.plokiSuurus;
                a[3].x -= Plokk.plokiSuurus;
            }
            Klahvihaldur.klahv_vasakule = false;
        }

        if (Klahvihaldur.klahv_paremale) {
            if (paremPõrge == false) {
                a[0].x += Plokk.plokiSuurus;
                a[1].x += Plokk.plokiSuurus;
                a[2].x += Plokk.plokiSuurus;
                a[3].x += Plokk.plokiSuurus;
            }
            Klahvihaldur.klahv_paremale = false;
        }

        if (põhjaPõrge) { // põhjapõrke korral klots deaktiveeritakse ning automaatne allapoole liikumine peatub
            mitteaktiivne = true;
        } else {
            if (langemisLuger == Mänguhaldur.langemisIntervall) {
                // pärast intervalli plokk langeb sammuvõrra
                a[0].y += Plokk.plokiSuurus;
                a[1].y += Plokk.plokiSuurus;
                a[2].y += Plokk.plokiSuurus;
                a[3].y += Plokk.plokiSuurus;
                langemisLuger = 0;
            } else {
                langemisLuger++;
            }
        }
    }

    private void mitteaktiivne() {
        mitteaktiivseteLuger++;

        // klots deaktiveeritakse peale 45 raami möödumist
        if (mitteaktiivseteLuger == 45) {
            mitteaktiivseteLuger = 0;
            kontrolliLiikumisPõrget(); // kontrollitakse, kas põhi on endiselt kokkupuutes
            // deaktiveerimisluger läheb käima alates põhjakokkupuute hetkest
            if (põhjaPõrge) {
                aktiivne = false;
            }
        }
    }

    public void kuva(GraphicsContext gc) {
        int margin = 2;
        gc.setFill(a[0].värv);
        gc.fillRect(a[0].x, a[0].y, Plokk.plokiSuurus - (margin * 2), Plokk.plokiSuurus - (margin * 2));
        gc.fillRect(a[1].x, a[1].y, Plokk.plokiSuurus - (margin * 2), Plokk.plokiSuurus - (margin * 2));
        gc.fillRect(a[2].x, a[2].y, Plokk.plokiSuurus - (margin * 2), Plokk.plokiSuurus - (margin * 2));
        gc.fillRect(a[3].x, a[3].y, Plokk.plokiSuurus - (margin * 2), Plokk.plokiSuurus - (margin * 2));
    }

}
