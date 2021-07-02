#ifndef GAME_H
#define GAME_H

#include <QMainWindow>
#include <QPushButton>
#include <iostream>
#include <QTest>

namespace Ui {
class Game;
}

class Game : public QMainWindow
{
    Q_OBJECT

// The explanation of variables is given in game.cpp file
public:
    explicit Game(QWidget *parent = nullptr);
    int counter;
    int pairn;
    QString prevs;
    int tries;
    QList<QPair<QString, int>> pairs;
    QPushButton *cards[24];
    QPushButton *reset;
    QPushButton *button1;
    QPushButton *button2;
    QString title;
    QString title2;
    bool cond;
    ~Game();

private:
    Ui::Game *ui;

// The explanations of slots are given in the game.cpp file
private slots:
    void resetPressed();
    void xPressed();

};

#endif // GAME_H
