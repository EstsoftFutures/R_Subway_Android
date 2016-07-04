package com.estsoft.r_subway_android.Parser;

/**
 * Created by estsoft on 2016-06-22.
 */
public class CircleTag {

    private Float positionX;
    private Float positionY;
    private Float radius;
    private String fill;
    private String name;
    private String otherFactor;

    public Float getPositionX() {
        return positionX;
    }

    public Float getPositionY() {
        return positionY;
    }

    public String getName() { return name; }

    public Float getRadius() {
        return radius;
    }

    public String getFill() {
        return fill;
    }

    public String getOtherFactor() {
        return otherFactor;
    }

    public CircleTag(Float positionX, Float positionY,Float radius, String name, String fill, String otherFactor) {
        this.positionX = positionX;
        this.positionY = positionY;
        this.name = name;
        this.radius = radius;
        this.fill = fill;
        this.otherFactor = otherFactor;
    }

    @Override
    public String toString() {
        return "CircleTag{" +
                "positionX=" + positionX +
                ", positionY=" + positionY +
                ", radius=" + radius +
                ", fill='" + fill + '\'' +
                ", otherFactor='" + otherFactor + '\'' +
                '}';
    }
}
