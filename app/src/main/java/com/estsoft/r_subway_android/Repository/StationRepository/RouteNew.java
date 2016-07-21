package com.estsoft.r_subway_android.Repository.StationRepository;

import java.util.List;

/**
 * Created by estsoft on 2016-07-21.
 */
public class RouteNew {

    public static final int[] expressStationIDs = {
            // 1 호선
            133, 139, 174, 177, 180, 182, 1543, 186, 188, 1402, 1404, 1405, 1407, 1408,
            // 4 호선
            409, 410, 411, 412, 413, 414, 415, 416, 417, 418, 419, 420, 421, 422, 423, 424, 425, 426,
            427, 428, 429, 430, 431, 432, 433, 434, 435, 436, 437, 438, 439, 440, 441, 442, 443, 444,
            448, 450, 453,
            // 9 호선
            930, 929, 927, 925, 923, 920, 917, 915, 913, 910, 907, 902,
            // 경의중앙
            1613, 1609, 1607, 1318, 198, 197, 196, 195, 193, 192, 191, 124, 1316, 1390, 1315, 1314,
            1313, 1312, 1311, 1310,
            // 경춘선
            1810, 1815, 1816, 1818, 1820, 1822, 1824, 1827, 1829, 1830,
            // 공항철도
            4001, 4010,
            // 분당선
            1545, 1543, 1541, 1537, 1534, 1532, 1531, 1530, 1529, 1528, 1527, 1526, 1524, 1523, 1522,
            1521, 1522, 1521, 1520, 1519, 1518, 1517, 1516, 1515, 1514, 1513, 1512, 1511, 1510
    };

    public static boolean isExpressStation( int stationID ) {
        for (int i = 0; i < expressStationIDs.length; i ++ ) {
            if ( expressStationIDs[i] == stationID ) {
                return true;
            }
        }
        return false;
    }


    private List<List<Station>> sections;
    private List<Boolean> ExpressSectionIndex;
    private int totalSize;

    public RouteNew(List<List<Station>> sections, List<Boolean> expressSectionIndex) {
        this.sections = sections;
        ExpressSectionIndex = expressSectionIndex;
        totalSize = -1;
    }

    public Station getStationByOrder( int index ) {
        int i = 0;
        for ( List<Station> section : sections ) {
            if ( i + section.size() <= index ) {
                i = i + section.size();
            } else {
                return section.get( index - i );
            }
        }
        return null;
    }
    public int getTotalSize(){
        if ( totalSize == -1 ) {
            totalSize = 0;
            for ( List<Station> section : sections ) {
                for ( Station st : section ) {
                    totalSize ++;
                }
            }
        }
        return  totalSize;
    }
    public List<List<Station>> getSections() {
        return sections;
    }
    public List<Boolean> getExpressSectionIndex() {
        return ExpressSectionIndex;
    }
}
