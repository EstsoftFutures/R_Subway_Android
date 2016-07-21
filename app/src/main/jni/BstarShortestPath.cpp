//
// Created by GangGongUi on 2016. 7. 13..
//

#include <jni.h>
#include <string>
#include <cstring>
#include <cstdlib>
#include <algorithm>
#include <queue>
#include <vector>
#include "utill/bitQueue.hpp"
#include "utill/Logger.h"
#define MAX 700

using namespace std;

class Station
{

public:
    int index;
    Station() { };
    Station(int index) : index(index) { };

    bool isTransfar() const {
        return transfar;
    }

    void setTransfar(bool transfar) {
        Station::transfar = transfar;
    }

    int getLaneType() const {
        return laneType;
    }

    void setLaneType(int laneType) {
        Station::laneType = laneType;
    }

    const std::string &getStationName() const {
        return stationName;
    }

    void setStationName(const std::string &stationName) {
        Station::stationName = stationName;
    }

private:
    bool transfar;
    int laneType;
    std::string stationName;

};


jintArray shortestPath(JNIEnv *env, Station end, const vector<int>& parent);
void loadAdjByObjArray(JNIEnv *env, jobjectArray obj);
jintArray dijkstra(JNIEnv *env, const Station& start, const Station& end, int weight);
bool hasAdjGraph();
BitQueue<Station> bq(4000);

//BitQueue<Station> bq(4000);
vector<pair<Station, int> > adj[MAX];
Logger logger;

extern "C"
{
JNIEXPORT jintArray JNICALL
Java_com_estsoft_r_1subway_1android_utility_ShortestPath_getShortestPathByIntArray(JNIEnv *env, jclass type, jobjectArray a, jobject start, jobject end)
{
    // TODO Return the shortest path as an integer array receives input the departure station and arrival station.

    // convert the graph
    if(!hasAdjGraph())
        loadAdjByObjArray(env, a);

    // Bring the class type of Station.
    jclass clsStation = env->FindClass("com/estsoft/r_subway_android/Repository/StationRepository/Station");
    // Bring the fieldID of Station.
    jfieldID filedIndex = env->GetFieldID(clsStation, "index", "I");

    // Bring the Station
    int startIdx = env->GetIntField(start, filedIndex);
    int endIdx = env->GetIntField(end, filedIndex);

    // using the dijkstra algorithm.

    // return is testing

    return dijkstra(env ,Station(startIdx), Station(endIdx), 0);

}

JNIEXPORT jintArray JNICALL
Java_com_estsoft_r_1subway_1android_utility_ShortestPath_getMinimumTransferPathByIntArray(
        JNIEnv *env, jclass type, jobjectArray a, jobject start, jobject end)
{

    // TODO Return the minimum transfer path as an integer array receives input the departure station and arrival station.

    if(!hasAdjGraph())
        loadAdjByObjArray(env, a);

    // Bring the class type of Station.
    jclass clsStation = env->FindClass("com/estsoft/r_subway_android/Repository/StationRepository/Station");
    // Bring the fieldID of Station.
    jfieldID filedIndex = env->GetFieldID(clsStation, "index", "I");

    // Bring the Station
    int startIdx = env->GetIntField(start, filedIndex);
    int endIdx = env->GetIntField(end, filedIndex);

    // using the dijkstra algorithm.
    //dijkstra(env ,Station(startIdx), Station(endIdx));
    return dijkstra(env ,Station(startIdx), Station(endIdx), 300);

}
}

jintArray dijkstra(JNIEnv *env, const Station& start, const Station& end, int weight)
{
    vector<int> dist(MAX, INT32_MAX);
    vector<int> parent(MAX, -1);
    dist[start.index] = 0;
    parent[start.index] = start.index;
    bq.push(make_pair(start, 0));

    while(!bq.empty())
    {
        Station here = bq.top().first;
        int cost = bq.top().second;
        bq.pop();



        if(dist[here.index] < cost) continue;

        for(int i = 0; i < adj[here.index].size(); i++)
        {
            Station there = adj[here.index][i].first;
            int nextDist = cost + adj[here.index][i].second;

            if(here.getStationName() == there.getStationName() && weight)
            {
                nextDist += weight;
            }

            if(dist[there.index] > nextDist)
            {
                dist[there.index] = nextDist;
                parent[there.index] = here.index;
                bq.push(make_pair(there, nextDist));
            }
        }
    }

    return shortestPath(env, end, parent);
}

jintArray shortestPath(JNIEnv *env, Station end, const vector<int>& parent)
{
    int v = end.index;
    vector<int> path(1, v);
    while(parent[v]!= v)
    {
        v = parent[v];
        path.push_back(v);
    }
    reverse(path.begin(), path.end());

    jintArray shortestPathIntArray = env->NewIntArray(path.size());
    jint tempArr[path.size()];
    for(int i = 0; i < path.size(); i++)
        tempArr[i] = path[i];
    env->SetIntArrayRegion(shortestPathIntArray, 0, path.size(), tempArr);

    return shortestPathIntArray;
}

void loadAdjByObjArray(JNIEnv *env, jobjectArray obj)
{
    // TODO To convert the graph representation of java to c ++.

    // Bring the size of the array of ArrayList<Pair> adj[].
    jsize arr_size =  env->GetArrayLength(obj);

    // Bring the class type of ArrayList.
    jclass clsArrayList = env->FindClass("java/util/ArrayList");

    // Bring the class type of Pair.
    jclass clsPair = env->FindClass("android/support/v4/util/Pair");
    // Bring the class type of Station.
    jclass clsStation = env->FindClass("com/estsoft/r_subway_android/Repository/StationRepository/Station");
    // Bring the class type of Integer.
    jclass clsInteger = env->FindClass("java/lang/Integer");

    // Bring the method ID of ArrayList.
    jmethodID listSize = env->GetMethodID(clsArrayList, "size", "()I");
    jmethodID listGet = env->GetMethodID(clsArrayList, "get", "(I)Ljava/lang/Object;");

    // Bring the method ID of Integer.
    jmethodID valueGet = env->GetMethodID(clsInteger, "intValue", "()I");

    // Bring the fieldID of Pair.
    jfieldID filedFirst = env->GetFieldID(clsPair, "first", "Ljava/lang/Object;");
    jfieldID filedSecond = env->GetFieldID(clsPair, "second", "Ljava/lang/Object;");

    // Bring the fieldID of Station.
    jfieldID filedIndex = env->GetFieldID(clsStation, "index", "I");
    jfieldID filedIsTransFer = env->GetFieldID(clsStation, "isTransfer", "Z");
    jfieldID filedLaneType = env->GetFieldID(clsStation, "laneType", "I");
    jfieldID filedStationName = env->GetFieldID(clsStation, "stationName", "Ljava/lang/String;");

    for(int i = 0; i < arr_size; i++)
    {
        // Come take out one by one the elements of ArrayList<Pair>.
        jobject objArrayListPair = env->GetObjectArrayElement(obj, i);

        // Bring the size of the ArrayList<Pair>[i].
        int lengthListPair = env->CallIntMethod(objArrayListPair, listSize);

        for(int j = 0; j < lengthListPair; j++)
        {
            // Bring the value of the index j of ArrayList<Pair>.
            jobject objPair = env->CallObjectMethod(objArrayListPair, listGet, j);

            // Bring a reference to the field of first and second.
            jobject first = env->GetObjectField(objPair, filedFirst);
            jobject second = env->GetObjectField(objPair, filedSecond);

            // Bring the Station field;
            int index = env->GetIntField(first, filedIndex);
            int cost = env->CallIntMethod(second, valueGet);
            bool transfar = env->GetBooleanField(first, filedIsTransFer);
            int laneType = env->GetIntField(first , filedLaneType);
            jstring jname = (jstring) env->GetObjectField(first, filedStationName);
            jboolean isSucceed;
            string name = env->GetStringUTFChars(jname, &isSucceed);
            //string name = nativeString;

            // make station
            Station station = Station(index);
            station.setTransfar(transfar);
            station.setLaneType(laneType);
            station.setStationName(name);

            // Change in c ++ representation.
            adj[i].push_back(make_pair(station, cost));

            // Resource release.
            env->DeleteLocalRef(objPair);
            env->DeleteLocalRef(first);
            env->DeleteLocalRef(second);
            env->DeleteLocalRef(jname);
        }

        // Resource release.
        env->DeleteLocalRef(objArrayListPair);

    }
}

bool hasAdjGraph()
{
    if(!adj[100].size()) return false;
    if(!adj[200].size()) return false;
    if(!adj[300].size()) return false;
    if(!adj[400].size()) return false;
    if(!adj[500].size()) return false;
    return true;
}

