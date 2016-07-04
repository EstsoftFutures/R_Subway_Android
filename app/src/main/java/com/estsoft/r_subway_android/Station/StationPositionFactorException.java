package com.estsoft.r_subway_android.Station;

/**
 * Created by estsoft on 2016-06-24.
 */
public class StationPositionFactorException extends Exception {
    public StationPositionFactorException(String msg ) {
        super( msg );
    }
    public StationPositionFactorException( ) {
        super( "station position factor should be 2 values(X,Y)" );
    }
}
