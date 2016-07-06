package com.estsoft.r_subway_android.UI.RouteInfo;

/**
 * Created by Administrator on 2016-07-04.
 */
public class Car {
    private int id;
    private String name;
    public Car(int id, String name){
        this.id = id;
        this.name =name;
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

    @Override
    public String toString() {
        return "Car{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
