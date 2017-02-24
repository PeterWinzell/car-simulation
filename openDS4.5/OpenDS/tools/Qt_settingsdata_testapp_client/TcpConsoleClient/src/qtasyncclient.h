#ifndef QTASYNCCLIENT_H
#define QTASYNCCLIENT_H

#include <QObject>
#include <QTcpSocket>
#include <QDebug>
#include <QString>
#include <QDomDocument>

class QtAsyncClient : public QObject
{
    Q_OBJECT

public:
    explicit QtAsyncClient(QWidget *parent = Q_NULLPTR);

private slots:
    bool sendXmlReq(QString msg);
    QString recieveResponse();
    void displayError(QAbstractSocket::SocketError socketError);


private:
    QTcpSocket *tcpSocket;
    QDataStream inData;
    QString currentXml;

    QNetworkSession *networkSession;

    QString xmlParser(QString xmlData, QString tag);
    void connectToSimServer(QString IP, int port);
};

#endif // QTASYNCCLIENT_H
