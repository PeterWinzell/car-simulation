include(../gtest_dependency.pri)

TEMPLATE = app
CONFIG += console c++11
CONFIG -= app_bundle
CONFIG += thread

QT -= gui
QT += network
QT += xml

HEADERS += tst_src.h
HEADERS += ../../../src/clienttest.h

SOURCES += main.cpp
SOURCES += ../../../src/clienttest.cpp


INCLUDEPATH += $$PWD/../../../src
