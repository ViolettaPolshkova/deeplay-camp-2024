package entity;

import enums.Color;

public class Player {
    private String id;
    private Color color;

    public Player(String id, Color color) {
        this.id = id;
        this.color = color;
    }

    public String getId() {
        return id;
    }

    public Color getColor() {
        return color;
    }
}
