package com.estsoft.r_subway_android.TouchMapping;

import android.app.Activity;
import android.content.Context;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ImageView;

import com.estsoft.r_subway_android.R;
import com.estsoft.r_subway_android.Station.Station;
import com.estsoft.r_subway_android.Station.Stations;
import com.estsoft.r_subway_android.listener.TtfMapImageViewListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by estsoft on 2016-06-24.
 */
public class TtfMapImageView extends MapTouchImageView {

    private Activity mainActivity = null;
    private TtfMapImageViewListener ttfMapImageViewListener;

    public void setMainAct( Activity activity ) {
        this.mainActivity = activity;
    }

    public void setTtfMapImageViewListener(TtfMapImageViewListener ttfMapImageViewListener) {
        this.ttfMapImageViewListener = ttfMapImageViewListener;
    }

    private static final String TAG = "TtfMapImageView";
    private static final int TOUCH_RADIUS = 50;

    private Stations stations = null;
    private List<PointF> savedStationPointList = null;
    private List<Station> stationList  = null;
    private Station activeStation = null;

    private int currentImageWidth = 0;
    private int currentImageHeight = 0;
    private int currentImageX = 0;
    private int currentImageY = 0;
    private int svgWidth = 0;
    private int svgHeight = 0;


    //context, attr, defStyleAttr 이 뭔지 궁금함
    public TtfMapImageView(Context context, AttributeSet attrs, int defStyleAttr)  throws Exception  {

        super(context, attrs, defStyleAttr);
        setScaleType( ImageView.ScaleType.MATRIX );

        stations = new Stations( getResources().openRawResource( R.raw.example_curve ) );
        stationList = stations.getStationList();


        saveStationPosition();

        setMaxMagnification( 5 );
        svgWidth = stations.getMapWidth();
        svgHeight = stations.getMapHeight();


    }

    private void saveStationPosition() {
        savedStationPointList = new ArrayList<>();
        for ( Station station : stationList ) {
            savedStationPointList.add(station.getPosition());
        }
    }

    public TtfMapImageView(Context context, AttributeSet attrs)  throws Exception  {
        this(context, attrs, 0);
    }

    public TtfMapImageView(Context context) throws Exception {
        this( context, null );
    }

    private void setStationsPosition( Matrix matrix ) {

        currentImageWidth = getScaledImageWidth();
        currentImageHeight = getScaledImageHeight();
        currentImageX = getMovedImageX();
        currentImageY = getMovedImageY();

        Log.e(TAG, currentImageX + " / " + currentImageY);


        for (int i = 0; i < stationList.size() ; i ++ ) {
            Station station = stationList.get(i);
            PointF originStationPoint = savedStationPointList.get(i);
            station.setPositionX(
                    ((float) currentImageWidth / svgWidth) * originStationPoint.x + currentImageX );
            station.setPositionY(
                    ((float) currentImageHeight / svgHeight) * originStationPoint.y + currentImageY );

//            Log.d(TAG, "Y : " + value[5]);
            Log.d(TAG, "using : " + station.getPosition().toString());
//            Log.d(TAG, "saved : " + originStationPoint.toString());
        }

    }

    private Station checkSelectStation( List<Station> stationList, MotionEvent event ) {

        for (Station station  : stationList) {
            if ( station.getPosition().x + TOUCH_RADIUS > event.getX() &&
                    station.getPosition().x - TOUCH_RADIUS < event.getX() &&
                    station.getPosition().y + TOUCH_RADIUS > event.getY() &&
                    station.getPosition().y - TOUCH_RADIUS < event.getY() ) {

                touchedStationAction( station );
                return station;
            }
        }
        unTouchedStationAction();
        return null;
    }

    @Override
    public void afterTouch(int mode, Matrix matrix, MotionEvent event) {
        super.afterTouch(mode, matrix, event);

//        Log.d(TAG, getMovedImageX() + " / " + getMovedImageY());
//        Log.d(TAG, getScaledImageWidth() + " / " + getScaledImageHeight());

        if ( select == DONE ) {
            setStationsPosition( matrix );
            activeStation = checkSelectStation( stationList, event );
        }

    }

    @Override
    public void beforeTouch(int mode, Matrix matrix, MotionEvent event) {
        super.beforeTouch(mode, matrix, event);

//        Log.d(TAG, event.getX() + " : " + event.getY() );
//        Log.d(TAG, d.getIntrinsicWidth() + " : " + d.getIntrinsicHeight() );
//        Log.d(TAG, getWidth() + " : " + getHeight());
//        Log.d(TAG, matrix.toString());
//        Log.d(TAG, stations.toString());
//        Log.d(TAG, "Touch : " + event.getX() + " / " + event.getY() );
    }


    private void unTouchedStationAction() {

    }

    private void touchedStationAction( Station station ) {

//        Toast.makeText(getContext(), station.getName(), Toast.LENGTH_SHORT ).show();
        ttfMapImageViewListener.runBottomsheet( station );

    }
}
