/*
MIT License

Copyright (c) [2017] [Johan Strand]

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
*/


#include "clienttest.h"
#include <QString>
#include <QDomDocument>

ClientTest::ClientTest(QObject *parent) :
    QObject(parent)
{
}

/*
Test function for connecting to simulation server and sending  one request for exterior data (fuel type, rpm etc). Expected response is a XML structure containing several feature of a virtual car.
The IP-address to your server needs to be added and you might need to allow connection to the OPenDS server in you firewall.
*/
void ClientTest::Test()
{
    //Create a new socket and connect to host
    socket = new QTcpSocket(this);
    socket->connectToHost("Add-your-IP-to-Server-here", 5678);

    //XML message to request all elements under the tag <exterior>
    char msgReq[]="<?xml version=\"1.0\" encoding=\"UTF-8\"?> \r \
                    <Message>\r \
                        <Event Name=\"GetValue\">/root/thisVehicle/exterior</Event>\r \
                    </Message>\r";

    qDebug() << "Trying to connect...";
    if(socket->waitForConnected(3000))
    {
        qDebug() << "Connected!";

        // send the data to server
        int write=socket->write(msgReq);

        qDebug() << "waiting for writing (timeout 1s): " << write;
        if(socket->waitForBytesWritten(1000))
        {
          qDebug() << "Write succeded!";
        }else
        {
          qDebug() << "Write failed!";
        }

        //Wait for data to be ready to read or timeout
        if(socket->waitForReadyRead(3000))
        {
            qDebug() << "Reading: " << socket->bytesAvailable();
            qDebug() << socket->readAll();
        }else
        {
            qDebug() << "Reading failed (timed out at 3s): " << socket->bytesAvailable();
        }

        //Close socket
        socket->close();
    }
    else
    {
        qDebug() << "Not connected! Timed out after 3s!";
    }

}

/*
Test function for subscribing for vehicle speed and RPM with a update intervall of 200ms.
NOTE: The IP-address to your openDS server needs to be added and you might need to allow connection to the OpenDS server in you firewall.
*/
bool ClientTest::printSpeedAndRpm()
{

    //xml message for starting subscribing to RPM and speed
    char xmlMsgSubscribe[]= "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\
                  <Message>\r\
                    <Event Name=\"Unsubscribe\">/root/thisVehicle</Event>\r\
                    <Event Name=\"Subscribe\">/root/thisVehicle/exterior/engineCompartment/engine/Properties/actualRpm</Event>\r\
                    <Event Name=\"Subscribe\">/root/thisVehicle/physicalAttributes/Properties/speed</Event>\r\
                    <Event Name=\"SetUpdateInterval\">200</Event>\r \
                    <Event Name=\"EstablishConnection\"/>\r \
                  </Message>\r";

    bool ready; //used for while loop and reading continously
    bool result;
    QString xml; //used for storing the retrieved XML data

    //Initiate socket and connect to simulation server (note that IP needs to be changed to match your server IP)
    socket = new QTcpSocket(this);
    socket->connectToHost("192.168.31.107", 5678);



    qDebug() << "Trying to connect...";
    if(socket->waitForConnected(2000))
    {
        qDebug() << "Connected!";
        result=true;

        socket->write(xmlMsgSubscribe); //Write xml message to server
        socket->waitForBytesWritten(2000);//Wait at most 2s for the write to finish completly
        ready=true; //Set to true to enter while loop

        while(ready){
            ready=socket->waitForReadyRead(500); //Wait for response from server

            if(ready)
            {
                xml=socket->readAll(); //If data is ready read all data
                xmlParser(xml); //parse out <speed> and <actualRpm> and print the values using qdebug
            }
            else
            {
                qDebug() << "To fast! Not enought time to read data! Raise the waitForReadyRead() time or set update interval in request to higher value.\r";
                result=false;
                break;
            }
        }


    }else
    {
        result=false;
    }
    qDebug() << "Closing connection!";
    socket->close();

    return result;
}

/*
 *Helper function to parse and print the speed and rpm values received from the server.
 */
void ClientTest::xmlParser(QString xmlData) {

    //Get your xml into xmlText(you can use QString instead og QByteArray)
    QDomDocument doc;
    doc.setContent(xmlData);

    //Parse and print speed and rpm
    QDomNodeList speed=doc.elementsByTagName("speed");
    QDomNodeList rpm=doc.elementsByTagName("actualRpm");
    qDebug() << "Speed: "+speed.at(0).toElement().text()+" Rpm: "+rpm.at(0).toElement().text();
}




