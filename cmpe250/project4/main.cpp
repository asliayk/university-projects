#include <iostream>
#include <iterator>
#include <sstream>
#include <fstream>
#include <vector>
#include <algorithm>
#include <queue>


using namespace std;

template <class Container>
void split1(const string& str, Container& cont)
{
    istringstream iss(str);
    copy(istream_iterator<string>(iss),
         istream_iterator<string>(),
         back_inserter(cont));
}

struct myNode {
public:
    int data;
    vector<pair<int,int>> edges;
    bool updated=false;
    bool visited=false;
    int key=-1;
    int rown;
    int rowp;
    int colp;
    int columnn;
    myNode(int _row, int _column, int _data) {
        data = _data;
        rown= _row;
        columnn= _column;
    }
};

bool operator<(const myNode &n1, const myNode &n2) {
        return n1.key > n2.key;
}

void primTree(vector<vector<myNode>> &matrix, int size, int a, int b, int c, int d, int &ladder) {
    int k = a;
    int z = b;
    priority_queue<myNode> updated;
    vector<myNode> mstSet;
    matrix[a][b].key = 0;
    matrix[a][b].rowp = -1;
    matrix[a][b].colp = -1;
    mstSet.push_back(matrix[a][b]);
    int index = 0;
    while (mstSet.size() != size) {
        for (int i = 0; i < mstSet[index].edges.size(); i++) {
            if (mstSet[index].edges[i].first == 1 && !matrix[k - 1][z].visited &&
                (matrix[k - 1][z].key > mstSet[index].edges[i].second || matrix[k - 1][z].key == -1)) {
                matrix[k - 1][z].key = mstSet[index].edges[i].second;
                matrix[k - 1][z].rowp = k;
                matrix[k - 1][z].colp = z;
                if (!matrix[k - 1][z].updated) {
                    updated.push(matrix[k - 1][z]);
                    matrix[k - 1][z].updated = true;
                }
            } else if (mstSet[index].edges[i].first == 2 && !matrix[k][z + 1].visited &&
                       (matrix[k][z + 1].key > mstSet[index].edges[i].second || matrix[k][z + 1].key == -1)) {
                matrix[k][z + 1].key = mstSet[index].edges[i].second;
                matrix[k][z + 1].rowp = k;
                matrix[k][z + 1].colp = z;
                if (!matrix[k][z + 1].updated) {
                    updated.push(matrix[k][z + 1]);
                    matrix[k][z + 1].updated = true;
                }
            } else if (mstSet[index].edges[i].first == 3 && !matrix[k + 1][z].visited &&
                       (matrix[k + 1][z].key > mstSet[index].edges[i].second || matrix[k + 1][z].key == -1)) {
                matrix[k + 1][z].key = mstSet[index].edges[i].second;
                matrix[k + 1][z].rowp = k;
                matrix[k + 1][z].colp = z;
                if (!matrix[k + 1][z].updated) {
                    updated.push(matrix[k + 1][z]);
                    matrix[k + 1][z].updated = true;
                }
            } else if (mstSet[index].edges[i].first == 4 && !matrix[k][z - 1].visited &&
                       (matrix[k][z - 1].key > mstSet[index].edges[i].second || matrix[k][z - 1].key == -1)) {
                matrix[k][z - 1].key = mstSet[index].edges[i].second;
                matrix[k][z - 1].rowp = k;
                matrix[k][z - 1].colp = z;
                if (!matrix[k][z - 1].updated) {
                    updated.push(matrix[k][z - 1]);
                    matrix[k][z - 1].updated = true;
                }
            }

        }
        matrix[updated.top().rown][updated.top().columnn].visited = true;
        k = updated.top().rown;
        z = updated.top().columnn;
        mstSet.push_back(updated.top());
        updated.pop();
        index++;

    }


    myNode current = matrix[c][d];
    while (!(current.rown == a && current.columnn == b)) {
        if (abs(matrix[current.rown][current.columnn].data - matrix[current.rowp][current.colp].data) > ladder) {

            ladder = abs(matrix[current.rown][current.columnn].data - matrix[current.rowp][current.colp].data);
        }
        current = matrix[current.rowp][current.colp];
    }

}

int main(int argc, char* argv[]) {
    if (argc != 3) {
        cout << "Run the code with the following command: ./project4 [input_file] [output_file]" << endl;
        return 1;
    }

    ifstream infile(argv[1]);
    string line;
    getline(infile, line);
    vector<string> words;
    split1(line, words);
    int rows = atoi(words[0].c_str());
    int columns = atoi(words[1].c_str());
    int index = 2;
    vector<vector<myNode>> matrix;
    for (int i = 0; i < rows; i++) {
        getline(infile, line);
        split1(line, words);
        vector<myNode> row;
        for (int j = 0; j < columns; j++) {
            myNode x(i, j, atoi(words[index].c_str()));
            row.push_back(x);
            index++;
        }
        matrix.push_back(row);
    }
    getline(infile, line);
    split1(line, words);
    int runN=atoi(words[index].c_str());

    for (int i = 0; i < rows; i++) {
        for (int j = 0; j < columns; j++) {
            if (i != 0)
                matrix[i][j].edges.emplace_back(1, abs(matrix[i - 1][j].data - matrix[i][j].data));
            if (j != (columns - 1))
                matrix[i][j].edges.emplace_back(2, abs(matrix[i][j + 1].data - matrix[i][j].data));
            if (i != (rows - 1))
                matrix[i][j].edges.emplace_back(3, abs(matrix[i + 1][j].data - matrix[i][j].data));
            if (j != 0)
                matrix[i][j].edges.emplace_back(4, abs(matrix[i][j - 1].data - matrix[i][j].data));

        }
    }
    int size=rows*columns;
    index++;
    getline(infile, line);
    split1(line, words);
    int a= atoi(words[index].c_str())-1;
    index++;
    int b= atoi(words[index].c_str())-1;
    index++;
    int c= atoi(words[index].c_str())-1;
    index++;
    int d= atoi(words[index].c_str())-1;
    int ladder=0;
    primTree(matrix, size, a, b, c, d, ladder);
    ofstream myfile;
    myfile.open(argv[2]);
    myfile << ladder << endl;
    myfile.close();


    return 0;
}
