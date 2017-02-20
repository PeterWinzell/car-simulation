#ifndef CLIENTTEST_H
#define CLIENTTEST_H

#include <QObject>
#include <QTcpSocket>
#include <QDebug>

class ClientTest : public QObject
{
    Q_OBJECT
public:
    explicit ClientTest(QObject *parent = 0);

    void Test();
    void getSpeedAndRpm();


signals:

public slots:

private:
    QTcpSocket *socket;
    void xmlParser(QString);

};

#endif // CLIENTTEST_H
