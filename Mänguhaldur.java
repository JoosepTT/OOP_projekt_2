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

    public static int vasak_x;
    public static int parem_x;
    public static int ülemine_y;
    public static int alumine_y;

    // klotsid
    Klotsid praeguneKlots;
    final int klotsiAlgus_x;
    final int klotsiAlgus_y;
    Klotsid järgmineKlots;
    final int järgmiseKlotsi_x;
    final int järgmiseKlotsi_y;
    public static ArrayList<Plokk> staatilisedPlokid = new ArrayList<>(); // massiiv, kuhu pannakse mitteaktiivsed klotsid (mis on juba omal kohal)

    // muud
    public static int langemisIntervall = 60; // plokk kukub iga raami järel madalamale
    public boolean kasMängOnLäbi;

    // skoorid
    int tase = 1;
    int jooni;
    int skoor;

    public Mänguhaldur() {
        // mänguala positsioon
        vasak_x = (Mängupaneel.laius / 2) - (laius / 2);
        parem_x = vasak_x + laius;
        ülemine_y = 50;
        alumine_y = ülemine_y + kõrgus;

        klotsiAlgus_x = vasak_x + (laius / 2) - Plokk.plokiSuurus;
        klotsiAlgus_y = ülemine_y + Plokk.plokiSuurus;

        järgmiseKlotsi_x = parem_x + 175;
        järgmiseKlotsi_y = ülemine_y + 500;

        // järgmise klotsi seadistamine
        praeguneKlots = valiKlots();
        praeguneKlots.määraXY(klotsiAlgus_x, klotsiAlgus_y);
        järgmineKlots = valiKlots();
        järgmineKlots.määraXY(järgmiseKlotsi_x, järgmiseKlotsi_y);
    }

    // suvalise klotsi valimine ja loomine
    private Klotsid valiKlots() {
        Klotsid klots = null;
        int i = new Random().nextInt(7); // klotsi valimine

        switch (i) {
            case 0: klots = new Kujund_1();break;
            case 1: klots = new Kujund_2();break;
            case 2: klots = new Kujund_3();break;
            case 3: klots = new Kujund_4();break;
            case 4: klots = new Kujund_5();break;
            case 5: klots = new Kujund_6();break;
            case 6: klots = new Kujund_7();break;
        }
        return klots;
    }

    public void uuenda() {
        // kontrollitakse, kas parasjagu kuvatav klots on endiselt aktiivne
        if (praeguneKlots.aktiivne == false) {
            // kui klots pole aktiivne, siis see tõstetakse ümber staatiliste klotside massiivi
            staatilisedPlokid.add(praeguneKlots.a[0]);
            staatilisedPlokid.add(praeguneKlots.a[1]);
            staatilisedPlokid.add(praeguneKlots.a[2]);
            staatilisedPlokid.add(praeguneKlots.a[3]);

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
            järgmineKlots = valiKlots(); // omakorda uuele klotsile järgneva valimine
            järgmineKlots.määraXY(järgmiseKlotsi_x, järgmiseKlotsi_y);

            // pärast aktiivse klotsi deaktiveerimist kontrollitakse, kas on rida, mida kustutada
            kasSaabKustutada();

        } else { // vastasel juhul uuendadatkse klotsi parameetreid
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
        gc.setStroke(Color.WHITE);
        gc.setLineWidth(4);
        gc.strokeRect(vasak_x - 4, ülemine_y - 4, laius + 8, kõrgus + 8);

        // plokkide eelvaade
        int x = parem_x + 100;
        int y = alumine_y - 200;
        gc.strokeRect(x, y, 200, 200);

        // skoori kuvamine
        gc.strokeRect(x, ülemine_y, 250, 300);
        x += 40;
        y = ülemine_y + 90;
        gc.fillText("Tase: " + tase, x, y);
        y += 70;
        gc.fillText("Ridu: " + jooni, x, y);
        y += 70;
        gc.fillText("Skoor: " + skoor, x, y);

        // praeguse klotsi kuvamine
        if (praeguneKlots != null) {
            praeguneKlots.kuva(gc);
        }

        // järgmisena tuleva klotsi kuvamine
        järgmineKlots.kuva(gc);

        // staatiliste klotside kuvamine
        for (int i = 0; i < staatilisedPlokid.size(); i++) {
            staatilisedPlokid.get(i).kuva(gc);
        }

        // mängu lõpetamine ja pausile panemine
        gc.setFill(Color.YELLOW);
        gc.setFont(Font.font("Arial", 50));
        x = vasak_x + 70;
        y = parem_x + 320;
        if (kasMängOnLäbi) {
            gc.fillText("MÄNG ON LÄBI", x ,y);
        } else if (Klahvihaldur.pausSees) {
            gc.fillText("PAUSIL", x, y);
        }

        // mängu pealkirja kuvamine

    }

}