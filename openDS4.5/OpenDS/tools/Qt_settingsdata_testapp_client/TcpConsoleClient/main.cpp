#include <QCoreApplication>
#include <clienttest.h>
int main(int argc, char *argv[])
{
    QCoreApplication a(argc, argv);
    //ClientTest cTest;
    //cTest.Test();

    ClientTest cGetData;
    cGetData.getSpeedAndRpm();

    return a.exec();
}
