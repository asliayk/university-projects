#include <iostream>
#include <sstream>
#include <fstream>
#include <vector>
#include <iterator>
#include <stack>
#include "Graph.h"

using namespace std;

template <class Container>
void split1(const string& str, Container& cont)
{
    istringstream iss(str);
    copy(istream_iterator<string>(iss),
         istream_iterator<string>(),
         back_inserter(cont));
}

int main(int argc, char* argv[]) {
    if (argc != 3) {
        cout << "Run the code with the following command: ./project3 [input_file] [output_file]" << endl;
        return 1;
    }

    ifstream infile(argv[1]);
    string line;
    getline(infile, line);
    vector<string> words;
    int N = stoi(line);
    Graph x(N);
    for (int i = 0; i < N; i++) {
        getline(infile, line);
        split1(line, words);
        myNode y(i + 1);
        for (int k = 1; k < atoi(words[0].c_str()) + 1; k++) {
            y.nodeList.push_back(atoi(words[k].c_str()));
        }
        x.graphList.push_back(y);
        words.clear();
    }
    int index=0;
    stack<int> s;
    vector<vector<int>> sccLists;
    for(int i=0; i<N; i++) {
        if(x.graphList[i].index==-1) {
            x.sccf(x.graphList[i].data);
        }
    }

    string outputF = argv[2];
    x.toBreak(outputF);

}


