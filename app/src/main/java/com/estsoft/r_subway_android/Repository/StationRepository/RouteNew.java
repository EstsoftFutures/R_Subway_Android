package com.estsoft.r_subway_android.Repository.StationRepository;

import android.util.Log;

import java.util.List;

/**
 * Created by estsoft on 2016-07-21.
 */
public class RouteNew {

    private static final int[] expressStationIDs = {
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

    private static final String[][] terminalStationNames = {
            { "소요산", "신창", "신창(순천향대)", "인천", "의정부", "광운대", "용산", "청량리", "서동탄", "동두천",
                    "천안", "부평", "광명", "양주", "영등포", "구로", "병점", "동인천"  },
//            { "성수(외선)", "성수(내선)", "신설동", "신도림", "까치산"},
            { "성수", "신설동", "신도림", "까치산", "서울대입구"},
            { "대화", "구파발", "오금", "수서" },
            { "당고개", "오이도", "안산" },
            { "방화", "상일동", "마천", },
            { "응암", "봉화산", "봉화산(서울의료원)", "새절", "한강진", "공덕", "안암", "독바위", "봉화산-새절", "봉화산-역촌"  },
            { "장암", "부평구청", "온수", "도봉산", "태릉입구", "건대입구", "내방"},
            { "암사", "모란", "잠실", },
            { "종합운동장", "신논현", "동작", "개화", "당산"},
            { "강남", "광교" }, // 신분당선
            { "왕십리", "수원", "죽전" }, //분당선
            { "오이도", "인천" }, //수인선
            { "계양", "박촌", "국제업무지구", "신연수" }, //인천 1호선
            { "상봉", "광운대", "평내호평", "춘천", "마석", "마석" }, //경춘선
            { "문산", "청량리", "능곡", "용산", "용문", "일산", "덕소", "수색", "서울역", "팔당", "대곡", "신촌",  }, //경의중앙
            { "서울역", "디지털미디어시티", "인천국제공항", "검암",  }, //공항선
            { "발곡", "탑석" }, //경전철
            { "기흥", "전대.에버랜드" } //에버라인
    };
    private static int[] crazy2laneStationIDs = {
            251, 252, 253, 254, // 신설동 방면
             261, 262, 263, 264  // 까치산 방면
    };
    public static boolean isCrazyStation( int stationID ) {
        for (int i = 0; i < crazy2laneStationIDs.length; i ++ ) {
            if (crazy2laneStationIDs[i] == stationID ) return true;
        }
        return false;
    }
    public static boolean isExpressStation( int stationID ) {
        for (int i = 0; i < expressStationIDs.length; i ++ ) {
            if ( expressStationIDs[i] == stationID ) {
                return true;
            }
        }
        return false;
    }
    public static int getLineIndex(int laneType ) {
        switch ( laneType ) {
            case 1 : return 0;
            case 2 : return 1;
            case 3 : return 2;
            case 4 : return 3;
            case 5 : return 4;
            case 6 : return 5;
            case 7 : return 6;
            case 8 : return 7;
            case 9 : return 8;
            case 109 : return 9;
            case 100 : return 10;
            case 111 : return 11;
            case 21 : return 12;
            case 108 : return 13;
            case 104 : return 14;
            case 101 : return 15;
            case 110 : return 16;
            case 107 : return 17;
            default : return -1;
        }
    }
    public static boolean compareTerminalName( String stationName, int laneType ) {
        String[] laneTerminalNames = terminalStationNames[ getLineIndex(laneType) ];
        for (int i = 0; i < laneTerminalNames.length; i ++ ) {
            if (laneTerminalNames[i].equals( stationName )) return true;
        }
        return false;
    }

    private static final String TAG = "RouteNew";

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
