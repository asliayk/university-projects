#include "game.h"
#include "ui_game.h"
#include <QPushButton>
#include <iostream>
#include <QList>
#include <QPair>

using namespace std;

// The game constructor
Game::Game(QWidget *parent) :
    QMainWindow(parent),
    ui(new Ui::Game)
{
    // a qstring variable to save the first card value
    QString prevs;
    // a boolean variable for the process to run correctly
    this->cond = true;
    // an integer variable to count the pairs found
    this->pairn=0;
    // an integer variable to count the total tries
    this->tries=0;
    // qstring variables to show the pairs and tries on the label
    this->title = "Pairs: " + QString::number(pairn);
    this->title2 = "Tries: " + QString::number(tries);
    ui->setupUi(this);
    ui->Display->setText(title);
    ui->Display2->setText(title2);
    // a list of qpushbuttons which represent the cards
    QPushButton  *cards[24];
    // a qpushbutton variable for reset button
    QPushButton *reset;


    // an integer variable to indicate the card number at each turn (1 or 2, 1 first)
    this->counter=1;
    // a qstring list for the values of cards
    QStringList slist = {"A", "A", "B", "B", "C", "C",
                         "D", "D", "E", "E", "F", "F",
                         "G", "G", "H", "H", "I", "I",
                         "J", "J", "K", "K", "L", "L"};
    // an integer list for the positions of cards
    QList<int> list = {};
    for(int i=1; i<=24; i++) {
    list.append(i);
    }
    // shuffling the list of positions
    srand(time(0));
     std::random_shuffle ( list.begin(), list.end() );
    // a list of QPairs (<QString, int>) in which each pair consists of a value and a number that
    // indicates its position
     this->pairs={};
     for(int i=0; i<24; i++) {
       pairs.append(qMakePair(slist[i], list[i]));
     }

     // a for loop for connecting the card buttons with the function xPressed
    for(int i=0; i<24; i++) {
        QString bname = "Button" + QString::number(i+1);
        cards[i]=Game::findChild<QPushButton *>(bname);
        connect(cards[i], SIGNAL(released()), this, SLOT(xPressed()));
    }

    // connecting the reset button with the function resetPressed
    QString rname = "ButtonReset";
    reset=Game::findChild<QPushButton *>(rname);
    connect(reset, SIGNAL(released()), this, SLOT(resetPressed()));

}

// destructor for game
Game::~Game()
{
    delete ui;
}

// slot function for the cards
void Game::xPressed() {
    if(cond) {
    // if the user opens the first card
    if(this->counter==1) {
        // button is saved in button1 variable and is disabled in order not to push again in the same turn
        this->button1 = (QPushButton *)sender();
        button1->setDisabled(true);
        // some variables to get the position number of the button from its object name
        QString temp = button1->objectName();
        int len = temp.length();
        temp = temp.mid(6,len-6);
        int listNum = temp.toInt();
        // a for loop to get the value of the value in the spesific position with the help of
        // pair list, and that value is saved in prevs variable
        for(int i=0; i<24; i++) {
            if(this->pairs[i].second==listNum) {
                this->prevs = this->pairs[i].first;
                break;
            }
        }
        // 2nd card will be opened
        this->counter++;
        // button shows its value on the window
        button1->setText(prevs);
    }
    // if the user opens the second card
    else {
        cond = false;
        // button is saved in button2 variable and is disabled in order not to push again in the same turn
        this->button2 = (QPushButton *)sender();
        // some variables to get the position number of the button from its object name
        QString temp = button2->objectName();
        button2->setDisabled(true);
        int len = temp.length();
        temp = temp.mid(6,len-6);
        int listNum = temp.toInt();
        // 1st card will be open after this turn finishes
        this->counter=1;
        // a for loop to get the value of the value in the spesific position with the help of
        // pair list, and that value is saved in newVal variable
        QString newVal;
        for(int i=0; i<24; i++) {
            if(this->pairs[i].second==listNum) {
                newVal = this->pairs[i].first;
                break;
            }
        }
        // button shows its value on the window
        button2->setText(newVal);
        // if the first card value is equal to 2nd card value
        if(this->prevs==newVal) {
            // new pair is found
            this->pairn++;
            QTest::qWait(400);
            // some functions to make the buttons invisible and disabled
            button1->setDisabled(true);
            button1->setFlat(true);
            this->button1->setText("");
            button2->setDisabled(true);
            button2->setFlat(true);
            this->button2->setText("");
        } else {
            QTest::qWait(400);
            // functions to make the buttons enable again
            button1->setDisabled(false);
            button2->setDisabled(false);
            // cards will be closed for new turns
            this->button1->setText("X");
            this->button2->setText("X");
         }
        // tries will be increased in each turn
        this->tries++;
        // prevs should be updated
        this->prevs="";
        // title should be updated and displayed on the window
        this->title = "Pairs: " + QString::number(pairn);
        this->title2 = "Tries: " + QString::number(tries);
        ui->Display->setText(title);
        ui->Display2->setText(title2);


    }
    }
    cond=true;
}

// a slot function for reset button
void Game::resetPressed() {
    this->cond = true;
    // an integer variable to count the pairs found
    this->pairn=0;
    // an integer variable to count the total tries
    this->tries=0;
    // a qtsring variable to show the pairs and tries on the label
    this->title = "Pairs: " + QString::number(pairn);
    this->title2 = "Tries: " + QString::number(tries);
    ui->Display->setText(title);
    ui->Display2->setText(title2);

    // an integer variable to indicate the card number at each turn (1 or 2, 1 first)
    this->counter=1;
    // a qstring list for the values of cards
    QStringList slist = {"A", "A", "B", "B", "C", "C",
                         "D", "D", "E", "E", "F", "F",
                         "G", "G", "H", "H", "I", "I",
                         "J", "J", "K", "K", "L", "L"};
    // an integer list for the positions of cards
    QList<int> list = {};
    for(int i=1; i<=24; i++) {
    list.append(i);
    }
    // shuffling the list of positions
    srand(time(0));
     std::random_shuffle ( list.begin(), list.end() );
    // a list of QPairs (<QString, int>) in which each pair consists of a value and a number that
    // indicates its position
     this->pairs={};
     for(int i=0; i<24; i++) {
       pairs.append(qMakePair(slist[i], list[i]));
     }

    // a for loop for connecting the card buttons with the function xPressed
    for(int i=0; i<24; i++) {
        QString bname = "Button" + QString::number(i+1);
        cards[i]=Game::findChild<QPushButton *>(bname);
    }
    for(int i = 0; i < 24; i++){
        cards[i]->setFlat(false);
        cards[i]->setEnabled(true);
        cards[i]->setText("X");
    }
    this->title = "Pairs: " + QString::number(pairn);
    this->title2 = "Tries: " + QString::number(tries);
    ui->Display->setText(title);
    ui->Display2->setText(title2);
}

