#include "Passenger.h"

Passenger::Passenger(int _toTerminal, int _toFlight, int _luggageTime, int _securityTime,
                     boolean _isVip, boolean _hasLuggage) {
    toTerminal = _toTerminal;
    toFlight = _toFlight;
    luggageTime = _luggageTime;
    securityTime = _securityTime;
    isVip = _isVip;
    hasLuggage = _hasLuggage;
    waitTime = 0;
    toFlight2 = 0;
    secEnter=0;
    lugEnter=0;

}
