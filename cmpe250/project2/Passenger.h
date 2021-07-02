#ifndef PROJECT2_PASSENGER_H
#define PROJECT2_PASSENGER_H

#include <iostream>
#include <vector>
#include <jmorecfg.h>

using namespace std;

class Passenger {
public:
    int toTerminal;
    int toFlight;
    int luggageTime;
    int securityTime;
    boolean isVip;
    boolean hasLuggage;
    int waitTime;
    int toFlight2;
    int secEnter;
    int lugEnter;

    Passenger(int _toTerminal, int _toFlight, int _luggageTime, int _securityTime,
              boolean _isVip, boolean _hasLuggage);
};






#endif //PROJECT2_PASSENGER_H
