#include <iostream>
#include <vector>
#include <string>
#include <fstream>
#include <algorithm>
#include <unordered_map>


using namespace std;

void search(string target, string sentence, unordered_map<string, vector<int>> &wordList) {

    unsigned long int targetS = target.length();
    unsigned long int sentenceS = sentence.length();
    int targetHash = 0;
    int sentenceHash = 0;
    int h = 1;


    for (int j = 0; j < targetS; j++) {
        targetHash = (97*targetHash + target[j])%523;
        sentenceHash = (97*sentenceHash + sentence[j])%523;
    }

    for (int i = 0; i < targetS-1; i++)
        h = (h*97)%523;

    for (int i = 0; i <= sentenceS - targetS; i++) {
        if (targetHash==sentenceHash) {
            int z=0;
            while(z<targetS) {
                z++;
                if (sentence[i+z] != target[z])
                    break;
            }

            if (z == targetS) {
                wordList[target].push_back(i);
            }
        }

        if ( i < sentenceS-targetS ) {
            sentenceHash = (97 * (sentenceHash - sentence[i] * h) + sentence[i + targetS]) % 523;
        }
        if (sentenceHash < 0)
            sentenceHash += 523;
    }
}


int counter(vector<vector<string>> indexList, unsigned long int index, vector<int> &dyn, unsigned long int totalSize) {
    if (index==totalSize) {
        return 1;
    }

    if (dyn[index] > 0) {
        return dyn[index];
    }
    for(int i=0; i<indexList[index].size(); i++) {
        dyn[index] += counter(indexList, index+indexList[index][i].size(), dyn, totalSize);
        dyn[index] = dyn[index]%1000000007;
    }
    return dyn[index];
}


void readFile(string x, vector<string> &words, string &sentence, int &wordN) {
    ifstream file;
    file.open (x);
    if (!file.is_open())
        return;
    string word;
    int index=0;
    while (file >> word) {
        if(index==0)
            sentence=word;
        if(index==1)
            wordN = stoi(word);
        if(index!=0 && index!=1)
            words.push_back(word);
        index++;
    }
}



int main(int argc, char* argv[]) {
    if (argc != 3) {
        cout << "Run the code with the following command: ./project5 [input_file] [output_file]" << endl;
        return 1;
    }
    vector<string> words;
    string sentence;
    int wordN;
    readFile(argv[1], words, sentence, wordN);
    unordered_map<string, vector<int>> map;
    for (int i = 0; i < words.size(); i++) {
        vector<int> k;
        string word = words[i];
        map.insert(make_pair(word, k));
    }
    for (int i = 0; i < words.size(); i++) {
        search(words[i], sentence, map);
    }

    vector<vector<string>> myVec;
    for (int i = 0; i < sentence.length(); i++) {
        vector<string> x;
        myVec.push_back(x);
    }
    for (unsigned long int k = 0; k < words.size(); k++) {
        vector<int> indexes = map[words[k]];
        for (unsigned long int z = 0; z < indexes.size(); z++) {
            int a = indexes[z];
            myVec[a].push_back(words[k]);
        }
    }
    vector<int> dyn;
    for (int i = 0; i < sentence.length(); i++) {
        dyn.push_back(0);
    }
    int k = counter(myVec, 0, dyn, sentence.length());
    ofstream myfile;
    myfile.open(argv[2]);
    myfile << k % (1000000007) << endl;
    myfile.close();
    return 0;
}
