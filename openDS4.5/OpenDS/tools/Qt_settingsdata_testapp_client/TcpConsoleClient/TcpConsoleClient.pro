QT += core
QT -= gui
QT += network
QT += xml

CONFIG += c++11

TARGET = TcpConsoleClient
CONFIG += console
CONFIG -= app_bundle

TEMPLATE = app

SOURCES += main.cpp \
    clienttest.cpp

HEADERS += \
    clienttest.h

target.path = /opt/build-TcpConsoleClient-GDP_SDK_for_RaspberryPi_2_3-Debug
INSTALLS += target
