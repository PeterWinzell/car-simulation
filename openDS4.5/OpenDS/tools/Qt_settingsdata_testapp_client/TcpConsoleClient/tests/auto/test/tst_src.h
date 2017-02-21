#include <gtest/gtest.h>
#include <gmock/gmock-matchers.h>
#include "../../../src/clienttest.h"
#include "../../../src/clienttest.cpp"

using namespace testing;

TEST(ClientTest, printSpeedAndRpm)
{
    ClientTest ct;
    bool res=ct.printSpeedAndRpm();

    EXPECT_EQ(res, true);
    ASSERT_THAT(true, res);
}
