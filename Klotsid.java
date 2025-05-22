package oop2;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import java.util.ArrayList;

public class Klotsid {
    public Plokk[] a = new Plokk[4];
    public Plokk[] b = new Plokk[4];
    protected boolean mängija1;
    int langemisLuger = 0;
    public int suund = 1;
    boolean vasakPõrge, paremPõrge, põhjaPõrge;
    public boolean aktiivne = true;
    public boolean mitteaktiivne;
    int mitteaktiivseteLuger = 0;

    public Klotsid(boolean mängija1) {
        this.mängija1 = mängija1;
        looKujund(mängijaVärv());
    }

    protected Color mängijaVärv() {
        return mängija1 ? Color.ORANGE : Color.CYAN;
    }

    public void looKujund(Color värv) {
        for (int i = 0; i < 4; i++) {
            a[i] = new Plokk(värv);
            b[i] = new Plokk(värv);
        }
    }

    public void määraXY(int x, int y) {

    }

    public void uuendaXY(int suund) {

        if (!vasakPõrge && !paremPõrge && !põhjaPõrge) {
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

    public void kontrolliLiikumisPõrget(ArrayList<Plokk> staatilisedPlokid, int vasak_x, int parem_x, int alumine_y) {
        vasakPõrge = false;
        paremPõrge = false;
        põhjaPõrge = false;

        // Seina kokkupõrge
        for (Plokk plokk : a) {
            if (plokk.x <= vasak_x) {
                vasakPõrge = true;
            }
            if (plokk.x + Plokk.plokiSuurus >= parem_x) {
                paremPõrge = true;
            }
            if (plokk.y + Plokk.plokiSuurus >= alumine_y) {
                põhjaPõrge = true;
            }
        }

        // Staatilise klotsi kokkupõrge
        for (Plokk staatilineKlots : staatilisedPlokid) {
            for (Plokk liikuvKlots : a) {
                if (liikuvKlots.x == staatilineKlots.x && liikuvKlots.y + Plokk.plokiSuurus == staatilineKlots.y) {
                    põhjaPõrge = true;
                }
            }
        }
    }

    public void uuenda() {
        if (mitteaktiivne) {
            mitteaktiivne();
        }

        // Pööra
        if ((mängija1 && Klahvihaldur.klahv_üles) || (!mängija1 && Klahvihaldur.klahv_üles2)) {
            pööra();
            if (mängija1) Klahvihaldur.klahv_üles = false;
            else Klahvihaldur.klahv_üles2 = false;
        }

        // Liikumine
        if (!põhjaPõrge) {
            // Alla
            if ((mängija1 && Klahvihaldur.klahv_alla) || (!mängija1 && Klahvihaldur.klahv_alla2)) {
                liiguAlla();
                if (mängija1) Klahvihaldur.klahv_alla = false;
                else Klahvihaldur.klahv_alla2 = false;
            }

            // Vasakule
            if ((mängija1 && Klahvihaldur.klahv_vasakule && !vasakPõrge) ||
                    (!mängija1 && Klahvihaldur.klahv_vasakule2 && !vasakPõrge)) {
                liiguVasakule();
                if (mängija1) Klahvihaldur.klahv_vasakule = false;
                else Klahvihaldur.klahv_vasakule2 = false;
            }

            // Paremale
            if ((mängija1 && Klahvihaldur.klahv_paremale && !paremPõrge) ||
                    (!mängija1 && Klahvihaldur.klahv_paremale2 && !paremPõrge)) {
                liiguParemale();
                if (mängija1) Klahvihaldur.klahv_paremale = false;
                else Klahvihaldur.klahv_paremale2 = false;
            }
        }

        // Automaatne kukkumine
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

    private void pööra() {
        switch (suund) {
            case 1: getSuund2(); break;
            case 2: getSuund3(); break;
            case 3: getSuund4(); break;
            case 4: getSuund1(); break;
        }
    }

    private void liiguAlla() {
        for (Plokk plokk : a) {
            plokk.y += Plokk.plokiSuurus;
        }
    }

    private void liiguVasakule() {
        for (Plokk plokk : a) {
            plokk.x -= Plokk.plokiSuurus;
        }
    }

    private void liiguParemale() {
        for (Plokk plokk : a) {
            plokk.x += Plokk.plokiSuurus;
        }
    }

    private void mitteaktiivne() {
        mitteaktiivseteLuger++;

        // klots deaktiveeritakse peale 45 raami möödumist
        if (mitteaktiivseteLuger == 45) {
            mitteaktiivseteLuger = 0;
            // kontrollitakse, kas põhi on endiselt kokkupuutes
            // deaktiveerimisluger läheb käima alates põhjakokkupuute hetkest
            if (põhjaPõrge) {
                aktiivne = false;
                vasakPõrge = false;
                paremPõrge = false;
                põhjaPõrge = false;
            }
        }
    }

    public void kuva(GraphicsContext gc){
        int margin = 2;
        gc.setFill(a[0].värv);
        gc.fillRect(a[0].x, a[0].y, Plokk.plokiSuurus - (margin * 2), Plokk.plokiSuurus - (margin * 2));
        gc.fillRect(a[1].x, a[1].y, Plokk.plokiSuurus - (margin * 2), Plokk.plokiSuurus - (margin * 2));
        gc.fillRect(a[2].x, a[2].y, Plokk.plokiSuurus - (margin * 2), Plokk.plokiSuurus - (margin * 2));
        gc.fillRect(a[3].x, a[3].y, Plokk.plokiSuurus - (margin * 2), Plokk.plokiSuurus - (margin * 2));
    }
}
