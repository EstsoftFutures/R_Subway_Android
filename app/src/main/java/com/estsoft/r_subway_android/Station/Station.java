package com.estsoft.r_subway_android.Station;

import android.graphics.PointF;

/**
 * Created by estsoft on 2016-06-24.
 */
public class Station {
    private Float positionX;
    private Float positionY;
    private String name;
    private String otherFactor;

    public Station(Float positionX, Float positionY, String name, String otherFactor) {
        this.positionX = positionX;
        this.positionY = positionY;
        this.name = name;
        this.otherFactor = otherFactor;
    }

    public Float getPositionX() {
        return positionX;
    }

    public Float getPositionY() {
        return positionY;
    }

    public String getName() {
        return name;
    }

    public String getOtherFactor() {
        return otherFactor;
    }

    public PointF getPosition() {
        return new PointF( this.positionX, this.positionY );
    }

    public void setPositionX(Float positionX) {
        this.positionX = positionX;
    }

    public void setPositionY(Float positionY) {
        this.positionY = positionY;
    }

    @Override
    public String toString() {
        return "Station{" +
                "positionX=" + positionX +
                ", positionY=" + positionY +
                ", name='" + name + '\'' +
                ", otherFactor='" + otherFactor + '\'' +
                '}';
    }
}