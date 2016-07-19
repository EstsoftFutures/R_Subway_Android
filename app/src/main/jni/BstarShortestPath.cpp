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
#define MAX 600


using namespace std;
class Station
{
public:
    int index;

    Station() { };
    Station(int index) : index(index) { };

    bool operator< (const Station& s) const {
        return false;
    };

};

vector<int> shortestPath(Station end, const vector<int>& parent);
void loadAdjByObjArray(JNIEnv *env, jobjectArray obj);
vector<int> dijkstra(Station start, Station end);

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
    loadAdjByObjArray(env, a);

    // Bring the class type of Station.
    jclass clsStation = env->FindClass("com/estsoft/r_subway_android/Repository/StationRepository/Station");
    // Bring the fieldID of Station.
    jfieldID filedIndex = env->GetFieldID(clsStation, "index", "I");

    // Bring the Station
    int startIdx = env->GetIntField(start, filedIndex);
    int endIdx = env->GetIntField(end, filedIndex);

    // using the dijkstra algorithm.
    vector<int> path = dijkstra(Station(startIdx), Station(endIdx));

    // return is testing
    jintArray shortestPathIntArray = env->NewIntArray(path.size());
    jint tempArr[path.size()];
    for(int i = 0; i < path.size(); i++)
        tempArr[i] = path[i];
    env->SetIntArrayRegion(shortestPathIntArray, 0, path.size(), tempArr);
    return shortestPathIntArray;
}

JNIEXPORT jintArray JNICALL
Java_com_estsoft_r_1subway_1android_utility_ShortestPath_getMinimumTransferPathByIntArray(
        JNIEnv *env, jclass type, jobjectArray adj, jobject start, jobject end) {

    // TODO

}


}

vector<int> dijkstra(Station start, Station end)
{
    vector<int> dist(MAX, INT32_MAX);
    vector<int> parent(MAX, -1);
    priority_queue<pair<int, Station> > q;
    dist[start.index] = 0;
    parent[start.index] = start.index;
    q.push(make_pair(0, start));

    while(!q.empty())
    {
        Station here = q.top().second;
        int cost = -q.top().first;
        q.pop();

        if(dist[here.index] < cost) continue;

        for(int i = 0; i < adj[here.index].size(); i++)
        {
            Station there = adj[here.index][i].first;
            int nextDist = cost + adj[here.index][i].second;

            if(dist[there.index] > nextDist)
            {
                dist[there.index] = nextDist;
                parent[there.index] = here.index;
                q.push(make_pair(-nextDist, there));
            }
        }
    }

    return shortestPath(end, parent);
}

vector<int> shortestPath(Station end, const vector<int>& parent)
{
    int v = end.index;
    vector<int> path(1, v);
    while(parent[v]!= v)
    {
        v = parent[v];
        path.push_back(v);
    }
    reverse(path.begin(), path.end());

    return path;
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

            // Bring the index and the cost.
            int index = env->GetIntField(first, filedIndex);
            int cost = env->CallIntMethod(second, valueGet);

            // Change in c ++ representation.
            adj[i].push_back(make_pair(Station(index),cost));

            // Resource release.
            env->DeleteLocalRef(objPair);
            env->DeleteLocalRef(first);
            env->DeleteLocalRef(second);
        }

        // Resource release.
        env->DeleteLocalRef(objArrayListPair);

    }


}

