package com.estsoft.r_subway_android.UI.StationInfo;

/**
 * Created by Administrator on 2016-07-04.
 */
public class Car {
    private int id;
    private String name;
    private boolean isClicked;
    public Car(int id, String name){
        this.id = id;
        this.name =name;
        this.isClicked = false;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isclicked() {
        return isClicked;
    }

    public void setClicked(boolean clicked) {
        this.isClicked = clicked;
    }

    @Override
    public String toString() {
        return "Car{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", isClicked=" + isClicked +
                '}';
    }
}
