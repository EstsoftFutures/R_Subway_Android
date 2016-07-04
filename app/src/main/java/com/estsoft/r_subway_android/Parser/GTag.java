package com.estsoft.r_subway_android.Parser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by estsoft on 2016-06-22.
 */
public class GTag {

    private List<CircleTag> circleList = null;
    private String fill = "";

    public List<CircleTag> getCircleList() {
        return circleList;
    }

    public String getFill() {
        return fill;
    }

    public GTag(String fill ) {
        this.fill = fill;
        this.circleList = new ArrayList<>();
    }

    public void addCircle( String[] circleFactors ){

        CircleTag circle = new CircleTag( Float.parseFloat(circleFactors[0]),
                Float.parseFloat(circleFactors[1]),
                Float.parseFloat(circleFactors[2]),
                circleFactors[3],
                fill,
                circleFactors[4] );
        circleList.add( circle );
    }

}
