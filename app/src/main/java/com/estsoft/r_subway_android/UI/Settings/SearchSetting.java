package com.estsoft.r_subway_android.UI.Settings;

import android.widget.ExpandableListView;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016-07-07.
 */
public class SearchSetting {
    List<String> groupList;
    List<String> childList;
    Map<String, List<String>> settingCollection;


    public SearchSetting() {
        createGroupList();
        createCollection();

    }

    public void createGroupList() {
        groupList = new ArrayList<String>();
        groupList.add("시간혼잡도");
        groupList.add("위험역 제거");
        groupList.add("급행 열차");
        groupList.add("검색노선 설정");

    }

    public List<String> getGroupList() {
        return groupList;
    }

    public List<String> getChildList() {
        return childList;
    }

    public Map<String, List<String>> getSettingCollection() {
        return settingCollection;
    }

    public void createCollection() {

        String[] lines = {"1호선", "2호선", "3호선", "4호선", "5호선", "6호선", "7호선", "8호선", "9호선", "분당선", "인천", "신분당선", "경의중앙선", "경춘선", "공항", "의정부", "수인선", "에버라인", "자기부상"};
        settingCollection = new LinkedHashMap<String, List<String>>();
        for (String setting : groupList) {
            childList = new ArrayList<String>();
            if (setting.equals("검색노선 설정")) {
                loadChild(lines);
            }
            settingCollection.put(setting, childList);
        }

    }

    private void loadChild(String[] settingChild) {

        for (String settingcontent : settingChild) {
            childList.add(settingcontent);
        }
    }

}
