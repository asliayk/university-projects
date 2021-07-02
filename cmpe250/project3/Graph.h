// Created by student on 18.11.2018.
//

#ifndef PROJECT3_GRAPH_H
#define PROJECT3_GRAPH_H

#include <iostream>
#include <vector>
using namespace std;

struct myNode {
    int data;
    bool broken=true;
    vector<int> nodeList;
    int sccNum=0;
    int index=-1;
    int lowlink=-1;
    bool onStack=false;
    myNode(int data) {
        this->data=data;
    }
};

class Graph {
public:
    int vertexx;
    int index=0;
    vector<vector<int>> sccLists;
    int scc=1;
    stack<int> s;
    int lastSize=0;
    vector<myNode> graphList;
    vector<int> breakPoint;
    Graph(int vertex);
    void sccf(int data);
    void toBreak(string outputF);

};


#endif //PROJECT3_GRAPH_H
