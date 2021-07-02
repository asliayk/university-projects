#include "game.h"
#include <QApplication>

int main(int argc, char *argv[])
{
    // app will be open, game object will be created and displayed on the window
    QApplication app(argc, argv);
    Game game;
    game.show();

    return app.exec();
}
