//
// Created by student on 18.11.2018.
//


#include <stack>
#include <algorithm>
#include <fstream>
#include "Graph.h"

Graph::Graph(int _x) {
    this->vertexx=_x;
}

void Graph::sccf(int data) {

    graphList[data-1].index=index;
    graphList[data-1].lowlink=index;
    index++;
    graphList[data-1].onStack=true;
    s.push(data);
    for (int i = 0; i < graphList[data-1].nodeList.size(); i++) {
        if (graphList[graphList[data-1].nodeList[i] - 1].index == -1) {
            sccf(graphList[graphList[data-1].nodeList[i] - 1].data);
            graphList[data-1].lowlink = min(graphList[data-1].lowlink, graphList[graphList[data-1].nodeList[i] - 1].lowlink);
        } else if (graphList[graphList[data-1].nodeList[i] - 1].onStack) {
            graphList[data-1].lowlink = min(graphList[data-1].lowlink, graphList[graphList[data-1].nodeList[i] - 1].index);
        }
    }
    if (graphList[data-1].lowlink == graphList[data-1].index) {
        vector<int> currentScc;
        int w;
        do {
            w = s.top();
            graphList[w - 1].onStack = false;
            graphList[w - 1].sccNum=scc;
            s.pop();
            currentScc.push_back(w);

        } while (w != data);


        sccLists.push_back(currentScc);
        scc++;
    }

}

void Graph::toBreak(string outputF) {
    int a;

    for (int i = 0; i < graphList.size(); i++) {
        for (int k = 0; k < graphList[i].nodeList.size(); k++) {
            if (graphList[graphList[i].nodeList[k] - 1].sccNum != graphList[i].sccNum) {
                a = graphList[graphList[i].nodeList[k] - 1].sccNum;
                breakPoint.push_back(a);
            }
        }
    }
    lastSize=vertexx;
    int k=0;
    while(k!=breakPoint.size()) {
        int current = breakPoint[k];
        for(int i=0; i<sccLists[current-1].size(); i++) {
            if(!graphList[sccLists[current-1][i]-1].broken) {
                k++;
                break;
            }
            graphList[sccLists[current-1][i]-1].broken=false;
            lastSize--;
        }
    }

    if(sccLists[graphList[0].sccNum-1].size()>1 && graphList[sccLists[graphList[0].sccNum - 1][1] - 1].broken) {
        lastSize-=sccLists[graphList[0].sccNum - 1].size()-1;
        for (int i = 1; i < sccLists[graphList[0].sccNum - 1].size(); i++) {
            graphList[sccLists[graphList[0].sccNum - 1][i] - 1].broken = false;
        }
    }
    ofstream myfile;
    myfile.open(outputF);
    myfile << lastSize << " ";

    for(int z=0; z<graphList.size(); z++) {
        if(graphList[z].broken) {
            myfile << graphList[z].data << " ";
        }
    }

    myfile.close();
}
