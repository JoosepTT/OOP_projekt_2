package com.example.oop_rt2;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import java.util.ArrayList;
import java.util.Random;

// Mänguhaldur:
// - kuvab mänguala
// - haldab mänuplokkidega seonduvat
// - juhib mängu funktsioone
public class Mänguhaldur {

    // mänguala üldseadistus
    final int laius = 360;
    final int kõrgus = 600;

    private final boolean mängija1;
    private final int abiX;

    // mängija ala
    public int vasak_x;
    public int parem_x;
    public int ülemine_y;
    public static int alumine_y;

    // klotsid
    Klotsid praeguneKlots;
    final int klotsiAlgus_x;
    final int klotsiAlgus_y;
    Klotsid järgmineKlots;
    final int järgmiseKlotsi_x;
    final int järgmiseKlotsi_y;

    // muud
    public ArrayList<Plokk> staatilisedPlokid;
    public static int langemisIntervall = 30;
    public boolean kasMängOnLäbi;

    // skoorid
    int tase = 1;
    int jooni;
    int skoor;

    public Mänguhaldur(int abiX, boolean mängija1) {
        this.mängija1 = mängija1;
        this.abiX = abiX;
        this.staatilisedPlokid = new ArrayList<>();
        // mänguala positsioon
        vasak_x = abiX + 50;
        parem_x = vasak_x + laius;
        ülemine_y = 50;
        alumine_y = ülemine_y + kõrgus;

        // klotside alguspositsioon
        klotsiAlgus_x = vasak_x + (laius / 2) - Plokk.plokiSuurus;
        klotsiAlgus_y = ülemine_y + Plokk.plokiSuurus;

        // järgmise klotsi eelvaade
        if (mängija1) {
            järgmiseKlotsi_x = parem_x + 50;
        } else {
            järgmiseKlotsi_x = abiX + 460;
        }
        järgmiseKlotsi_y = ülemine_y + 150;

        // järgmise klotsi seadistamine
        praeguneKlots = valiKlots();
        praeguneKlots.määraXY(klotsiAlgus_x, klotsiAlgus_y);
        järgmineKlots = valiKlots();
        järgmineKlots.määraXY(järgmiseKlotsi_x, järgmiseKlotsi_y);
    }

    // suvalise klotsi valimine ja loomine
    private Klotsid valiKlots() {
        Klotsid klots = null;
        int i = new Random().nextInt(7);

        switch (i) {
            case 0: klots = new Kujund_1(mängija1); break;
            case 1: klots = new Kujund_2(mängija1); break;
            case 2: klots = new Kujund_3(mängija1); break;
            case 3: klots = new Kujund_4(mängija1); break;
            case 4: klots = new Kujund_5(mängija1); break;
            case 5: klots = new Kujund_6(mängija1); break;
            case 6: klots = new Kujund_7(mängija1); break;
        }
        return klots;
    }

    public void uuenda() {
        // kontrollitakse, kas parasjagu kuvatav klots on endiselt aktiivne
        if (!praeguneKlots.aktiivne) {
            // kui klots pole aktiivne, siis see tõstetakse ümber staatiliste klotside massiivi
            for (Plokk plokk : praeguneKlots.a) {
                staatilisedPlokid.add(new Plokk(plokk.värv) {{
                    x = plokk.x;
                    y = plokk.y;
                }});
            }

            // kontroll, kas mäng on läbi saanud
            if (praeguneKlots.a[0].x == klotsiAlgus_x && praeguneKlots.a[0].y == klotsiAlgus_y) {
                // mäng lõppeb, kui praegu aktiivne klots põrkus kohe peale ilmumist mõne teise plokiga, st et terve mänguala on täidetud
                kasMängOnLäbi = true;
                System.out.println("Mäng on läbi!");
            }

            praeguneKlots.mitteaktiivne = false;

            // kui üks klots on paigas, siis praegune klots asendatakse järgmisega
            praeguneKlots = järgmineKlots;
            praeguneKlots.määraXY(klotsiAlgus_x, klotsiAlgus_y);
            järgmineKlots = valiKlots();
            järgmineKlots.määraXY(järgmiseKlotsi_x, järgmiseKlotsi_y);

            // pärast aktiivse klotsi deaktiveerimist kontrollitakse, kas on rida, mida kustutada
            kasSaabKustutada();
        } else { // vastasel juhul uuendadatkse klotsi parameetreid
            praeguneKlots.kontrolliLiikumisPõrget(staatilisedPlokid, vasak_x, parem_x, alumine_y);
            praeguneKlots.uuenda();
        }
    }

    public void kasSaabKustutada() {
        int x = vasak_x;
        int y = ülemine_y;
        int plokke = 0;
        int joonteArv = 0;

        while (x < parem_x && y < alumine_y) {
            for (int i = 0; i < staatilisedPlokid.size(); i++) {
                if (staatilisedPlokid.get(i).x == x && staatilisedPlokid.get(i).y == y) {
                    // kui leitakse staatiline plokk, siis suurendatakse staatiliste plokkide arvu
                    plokke++;
                }
            }

            x += Plokk.plokiSuurus;

            if (x == parem_x) {

                // kui staatiliste plokkide arv reas kasvab 12 plokini ehk kui terve rida on täidetud, siis kustutatakse vastava y rea kõik plokid x-i väärtutel
                if (plokke == 12) {
                    for (int i = staatilisedPlokid.size() - 1; i > -1; i--) {
                        if (staatilisedPlokid.get(i).y == y) {
                            staatilisedPlokid.remove(i);
                        }
                    }

                    joonteArv++;
                    jooni++;
                    // klotside langemiskiiruse seadistus
                    // kui mängija skoor jõuab teatud tasemeni, siis kiireneb klotside langemiskiirus (suurim kiirus on 1)
                    if (jooni % 10 == 0 && langemisIntervall > 1) { // iga 10 täidetud rea järel saab mängija uuele tasemele
                        tase++;
                        if (langemisIntervall > 10) {
                            langemisIntervall -= 10;
                        } else { // kui intervall läheb all 10, siis järgmine aste oleks 0 raami, kuid selleasemel hakatakse raame vähendama ühe võrra kuni on järgi ainult 1
                            langemisIntervall -= 1;
                        }
                    }

                    // kui on tuvastatud täisrida, siis nihutatakse ülevalpool olevaid plokke allapoole
                    for (int i = 0; i < staatilisedPlokid.size(); i++) {
                        // kui plokk on praegusest y-väärtusest ülevalpool, siis liigutatakse seda ploki võrra alla
                        if (staatilisedPlokid.get(i).y < y) {
                            staatilisedPlokid.get(i).y += Plokk.plokiSuurus;
                        }
                    }
                }

                plokke = 0;
                x = vasak_x;
                y += Plokk.plokiSuurus;
            }
        }
        // skoori suurendamine
        if (joonteArv > 0) {
            int jooneSkoor = 10 * tase;
            skoor += jooneSkoor * joonteArv;
        }
    }

    public void joonista(GraphicsContext gc) {
        // mänguala kuvamine
        gc.setStroke(mängija1 ? Color.ORANGE : Color.CYAN);
        gc.setLineWidth(4);
        gc.strokeRect(vasak_x - 2, ülemine_y - 2, laius + 4, kõrgus + 4);

        // praeguse klotsi kuvamine
        if (praeguneKlots != null) {
            praeguneKlots.kuva(gc);
        }

        // järgmisena tuleva klotsi kuvamine
        gc.setStroke(Color.WHITE);
        gc.strokeRect(järgmiseKlotsi_x - 37, järgmiseKlotsi_y - 37,
                Plokk.plokiSuurus * 4 - 20, Plokk.plokiSuurus * 4 - 20);
        järgmineKlots.kuva(gc);

        // staatiliste klotside kuvamine
        for (Plokk plokk : staatilisedPlokid) {
            plokk.kuva(gc);
        }

        // skoori kuvamine
        int scoreX = mängija1 ? parem_x + 20 : abiX + 430;
        gc.setFill(Color.WHITE);
        gc.setFont(new Font(20));
        gc.fillText("Player " + (mängija1 ? "1" : "2"), scoreX, ülemine_y + 30);
        gc.fillText("Score: " + skoor, scoreX, ülemine_y + 60);
        gc.fillText("Lines: " + jooni, scoreX, ülemine_y + 90);

        // mängu lõpetamine ja pausile panemine
        gc.setFill(Color.YELLOW);
        gc.setFont(Font.font("Arial", 50));
        int x = vasak_x + 70;
        int y = ülemine_y + 320;
        if (kasMängOnLäbi) {
            gc.fillText("MÄNG ON LÄBI", x ,y);
        } else if (Klahvihaldur.pausSees) {
            gc.fillText("PAUSIL", x, y);
        }

        // mängu pealkirja kuvamine
    }

}