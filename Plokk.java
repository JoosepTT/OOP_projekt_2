package com.example.oop_rt2;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Plokk extends Rectangle {

    public int x, y;
    public static final int plokiSuurus = 30; // üks plokk on 30 x 30 pikslit
    public Color värv;

    public Plokk(Color värv) {
        this.värv = värv;
    }

    public void kuva(GraphicsContext gc) {
        int margin = 2;
        gc.setFill(värv);
        gc.strokeRect(x + margin, y + margin, plokiSuurus - (margin * 2), plokiSuurus - (margin * 2));
    }

}