#include <iostream>
#include <fstream>
#include <vector>
#include <iterator>
#include <sstream>
#include "Passenger.h"
#include <algorithm>
#include <deque>
#include <chrono>

using namespace std;
using namespace std::chrono;

template <class Container>
void split1(const string& str, Container& cont)
{
    istringstream iss(str);
    copy(istream_iterator<string>(iss),
         istream_iterator<string>(),
         back_inserter(cont));
}

bool compare(const Passenger &p1, const Passenger &p2) {
    if(p1.toFlight!=p2.toFlight) {
        return p1.toFlight > p2.toFlight;
    } else {
        return p1.toTerminal>p2.toTerminal;
    }
}

void event(vector<Passenger> passengerList, int passengers, int luggages, int security, bool firstFly, bool isVip, bool onlineTicket,
           int &missedp, double &timeavg) {
    deque<Passenger> firstHeap;
    deque<Passenger> inSecurity;
    deque<Passenger> inLuggage;
    deque<Passenger> secondHeap;
    vector<Passenger> toFlight;
    int time = passengerList[0].toTerminal-1;
    int i=0;
    int missed = 0;
    int totalTime=0;
    while (true) {
        time++;
        for (int i = 0; i < inSecurity.size(); i++) {
            if(time-inSecurity[i].secEnter==inSecurity[i].securityTime) {
                Passenger exit = inSecurity[i];
                inSecurity.erase(inSecurity.begin() + i);
                security++;
                i--;
                exit.waitTime = time-exit.toTerminal;
                if(exit.waitTime>exit.toFlight2) {
                    missed++;
                }
                totalTime+=exit.waitTime;
                toFlight.push_back(exit);
            }
        }
        if (passengers == toFlight.size())
            break;

        while (security != 0 && secondHeap.size() != 0) {
            Passenger secured = secondHeap.front();
            secured.waitTime += secured.securityTime;
            secured.secEnter=time;
            inSecurity.push_back(secured);
            secondHeap.erase(secondHeap.begin() + 0);
            if(firstFly) {
                make_heap(begin(secondHeap), end(secondHeap), compare);
            }
            security--;
        }

        for (int i = 0; i < inLuggage.size(); i++) {

              if(time-inLuggage[i].lugEnter==inLuggage[i].luggageTime) {
                Passenger exit = inLuggage[i];
                inLuggage.erase(inLuggage.begin() + i);
                if (isVip && exit.isVip) {
                    toFlight.push_back(exit);
                    exit.waitTime = time - exit.toTerminal;
                    totalTime += exit.waitTime;
                    if (exit.waitTime > exit.toFlight2)
                        missed++;
                } else {
                    secondHeap.push_back(exit);
                    if (firstFly) {
                        make_heap(begin(secondHeap), end(secondHeap), compare);
                    }
                }
                luggages++;
                i--;
            }

        }
        if (passengers == toFlight.size())
            break;
        while (security != 0 && secondHeap.size() != 0) {
            Passenger secured = secondHeap.front();
            secured.waitTime += secured.securityTime;
            secured.secEnter=time;
            inSecurity.push_back(secured);
            secondHeap.erase(secondHeap.begin() + 0);
            if(firstFly) {
                make_heap(begin(secondHeap), end(secondHeap), compare);
            }
            security--;
        }

        while (luggages != 0 && firstHeap.size() != 0) {
            Passenger luggaged = firstHeap.front();
            luggaged.waitTime += luggaged.luggageTime;
            luggaged.lugEnter=time;
            inLuggage.push_back(luggaged);
            firstHeap.erase(firstHeap.begin() + 0);
            if(firstFly) {
                make_heap(begin(firstHeap), end(firstHeap), compare);
            }
            luggages--;
        }
        if (i < passengerList.size() && passengerList[i].toTerminal == time) {
             passengerList[i].toFlight2 = passengerList[i].toFlight-time;
            if (onlineTicket && isVip && !passengerList[i].hasLuggage && passengerList[i].isVip) {
                toFlight.push_back(passengerList[i]);
            }  else if(onlineTicket && !passengerList[i].hasLuggage) {
                secondHeap.push_back(passengerList[i]);
                if(firstFly) {
                    make_heap(begin(secondHeap), end(secondHeap), compare);
                }
            }  else {
                firstHeap.push_back(passengerList[i]);
                if(firstFly) {
                    make_heap(begin(firstHeap), end(firstHeap), compare);
                }
            }
            i++;
        }

        while (security != 0 && secondHeap.size() != 0) {
            Passenger secured = secondHeap.front();
            secured.waitTime += secured.securityTime;
            secured.secEnter=time;
            inSecurity.push_back(secured);
            secondHeap.erase(secondHeap.begin() + 0);
            if(firstFly) {
                make_heap(begin(secondHeap), end(secondHeap), compare);
            }
            security--;
        }
        while (luggages != 0 && firstHeap.size() != 0) {
            Passenger luggaged = firstHeap.front();
            luggaged.waitTime += luggaged.luggageTime;
            luggaged.lugEnter=time;
            inLuggage.push_back(luggaged);
            firstHeap.erase(firstHeap.begin() + 0);
            if(firstFly) {
                make_heap(begin(firstHeap), end(firstHeap), compare);
            }
            luggages--;
        }

        if (passengers == toFlight.size())
            break;

    }

    missedp=missed;
    timeavg=(double) totalTime/toFlight.size();

}

int main(int argc, char* argv[]) {
    if (argc != 3) {
        cout << "Run the code with the following command: ./project1 [input_file] [output_file]" << endl;
        return 1;
    }

    ifstream infile(argv[1]);
    string line;
    vector<string> words;
    getline(infile, line);
    split1(line, words);
    int passengers = atoi(words[0].c_str());
    int luggages = atoi(words[1].c_str());
    int security = atoi(words[2].c_str());

    vector<string> wordss;
    vector<Passenger> passengerList;
    for (int i = 0; i < passengers; i++) {
        getline(infile, line);
        split1(line, wordss);
        int x = 4;
        int a = atoi(wordss[0].c_str());
        int b = atoi(wordss[1].c_str());
        int c = atoi(wordss[2].c_str());
        int d = atoi(wordss[3].c_str());
        boolean e = false;
        boolean f = false;
        if (wordss[4] == "V") {
            e = true;
        }
        if (wordss[5] == "L") {
            f = true;
        }
        wordss.clear();
        Passenger newOne(a, b, c, d, e, f);
        passengerList.push_back(newOne);

    }
    int missedp=0;
    double timeavg=0;
    ofstream myfile;
    myfile.open(argv[2]);
    event(passengerList, passengers, luggages, security, false, false, false, missedp, timeavg);
    myfile << timeavg << " " << missedp << endl;
    event(passengerList, passengers, luggages, security, true, false, false, missedp, timeavg);
    myfile << timeavg << " " << missedp << endl;
    event(passengerList, passengers, luggages, security, false, true, false, missedp, timeavg);
    myfile << timeavg << " " << missedp << endl;
    event(passengerList, passengers, luggages, security, true, true, false, missedp, timeavg);
    myfile << timeavg << " " << missedp << endl;
    event(passengerList, passengers, luggages, security, false, false, true, missedp, timeavg);
    myfile << timeavg << " " << missedp << endl;
    event(passengerList, passengers, luggages, security, true, false, true, missedp, timeavg);
    myfile << timeavg << " " << missedp << endl;
    event(passengerList, passengers, luggages, security, false, true, true, missedp, timeavg);
    myfile << timeavg << " " << missedp << endl;
    event(passengerList, passengers, luggages, security, true, true, true, missedp, timeavg);
    myfile << timeavg << " " << missedp << endl;
    myfile.close();
    return 0;
}
