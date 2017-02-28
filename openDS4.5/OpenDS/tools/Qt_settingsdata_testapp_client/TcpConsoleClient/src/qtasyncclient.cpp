#include "qtasyncclient.h"


QtAsyncClient::QtAsyncClient(QObject *parent)
    : QObject(parent)
    , tcpSocket(new QTcpSocket(this))
{
    inData.setDevice(tcpSocket);
    connect(tcpSocket, &QIODevice::readyRead, this, &Client::recieveResponse);

    typedef void (QAbstractSocket::*QAbstractSocketErrorSignal)(QAbstractSocket::SocketError);
    connect(tcpSocket, static_cast<QAbstractSocketErrorSignal>(&QAbstractSocket::error),this, &QtAsyncClient::displayError);
}

void QtAsyncClient::sendXmlReq()
{
   connectToSimServer("192.168.31.107", 5678);

   if(tcpSocket->waitForConnected(2000))
   {

   }else
   {

   }
}

void QtAsyncClient::connectToSimServer(QString ip, int port)
{

    //Initiate socket and connect to simulation server (note that IP needs to be changed to match your server IP)
    tcpSocket->abort();
    tcpSocket->connectToHost(ip,port);

}

void QtAsyncClient::displayError(QAbstractSocket::SocketError socketError)
{
    switch (socketError) {
    case QAbstractSocket::RemoteHostClosedError:
        break;
    case QAbstractSocket::HostNotFoundError:
        qDebug() << "The host was not found. Please check the host name and port settings.";
        break;
    case QAbstractSocket::ConnectionRefusedError:
        qDebug() << "The connection was refused by the peer. Make sure the fortune server is running, \n
                    "and check that the host name and port settings are correct.";
        break;
    default:
        qDebug() << "The following error occurred: "+ tcpSocket->errorString();

    }
}

void QtAsyncClient::recieveResponse()
{
    inData.startTransaction();

    QString nextXml;
    inData.Data >> nextXml;

    if (!inData.commitTransaction())
        return;

    if (nextXml == currentXml) {
        QTimer::singleShot(0, this, &QtAsyncClient::recieveResponse);
        return;
    }

    currentFortune = nextFortune;

}

/*
 *Helper function that parse xml data and returns the value for the first requested available xml tag (QString tag)
 */
QString QtAsyncClient::xmlParser(QString xmlData, QString tag)
{

    //Get your xml into xmlText(you can use QString instead og QByteArray)
    QDomDocument doc;
    QString res;
    doc.setContent(xmlData);

    //Parse the input "tag"
    QDomNodeList tagValue=doc.elementsByTagName(tag);


    if(tagValue.count()==0)
    {
        res="Element not found!";
    }
    else
    {
        res=tagValue.at(0).toElement().text();
    }

    return res;
}
