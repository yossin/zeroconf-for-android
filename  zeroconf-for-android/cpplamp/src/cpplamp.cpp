//============================================================================
// Name        : cpplamp.cpp
// Author      : yos
// Version     :
// Copyright   : Your copyright notice
// Description : Basic Lamp App for LINUX, Ansi-style
//============================================================================

#include <iostream>
#include "LampApp.h"
#include "LampInfo.h"
using namespace std;

int main() {
	mta_yos_zeroconf_lamp::Lamp lamp;
	mta_yos_zeroconf_lamp::LampInfo info("CppLamp", 7008, "123456789");
	mta_yos_zeroconf_lamp::LampApp app(info, &lamp);

	app.run();
	return 0;
}
