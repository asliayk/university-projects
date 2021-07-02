#include "SurveyClass.h"

SurveyClass::SurveyClass() {
    this->members = new LinkedList();
}

SurveyClass::SurveyClass(const SurveyClass& other) {
    this->members = other.members;
}

SurveyClass& SurveyClass::operator=(const SurveyClass& list) {
   this->members = list.members;
   return *this;
}

SurveyClass::SurveyClass(SurveyClass&& other) {
    this->members = new LinkedList();
    this->members->head=move(other.members->head);
    this->members->length = move(other.members->length);
    other.members->head = NULL;
    other.members->tail = NULL;

}

SurveyClass& SurveyClass::operator=(SurveyClass&& list) {
    this->members->head=move(list.members->head);
    this->members->length = move(list.members->length);
    list.members->head = NULL;
    list.members->tail = NULL;
    return *this;
}

SurveyClass::~SurveyClass() {
    members = NULL;
}

void SurveyClass::handleNewRecord(string _name, float _amount) {
    if(members->length == 0) {
        members->pushTail(_name,_amount);
        members->tail=members->head;
        return;
    }
    Node *current = members->head;
    while(current!=NULL && current->name!=_name) {
        current = current->next;
    }
    if(current==NULL) {
        members->pushTail(_name,_amount);
    } else {
        members->updateNode(_name, _amount);
    }
}

float SurveyClass::calculateMinimumExpense() {
    Node *current = members->head;
    float currentFloat = current->amount;
    while(current->next!=NULL) {
        if(currentFloat > current->next->amount) {
            currentFloat = current->next->amount;
        }
        current = current->next;
    }
    return currentFloat;
}

float SurveyClass::calculateMaximumExpense() {
    Node *current = members->head;
    float currentFloat = members->head->amount;
    while(current->next!=NULL) {
        if(currentFloat < current->next->amount) {
            currentFloat = current->next->amount;
        }
        current = current->next;
    }
    return currentFloat;
}

float SurveyClass::calculateAverageExpense() {
    Node *current = members->head;
    float totalFloat =0;
    int count=0;
    while(current!=NULL) {
        totalFloat+=current->amount;
        count++;
        current=current->next;
    }
    float x = totalFloat/count;
    if(int(x)==x) {
        return x;
    } else {
        float y = x-int(x);
        int count=0;
        while(true) {
            y=y*10;
            count++;
            if(y==int(y)) {
                break;
            }
        }
        if(count<=2) {
            return x;
        } else {
            float y = x-int(x);
            int a=2;
            while(a>0) {
                y=y*10;
                a--;
            }
            int newDecimal = int(y);
            double lastDecimal = (double)newDecimal/100;
            return float(int(x) + lastDecimal);

        }
    }
}
