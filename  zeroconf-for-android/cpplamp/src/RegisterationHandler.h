/*
 * RegisterationHandler.h
 *
 *  Created on: 16/08/2011
 *      Author: yos
 */

#ifndef REGISTERATIONHANDLER_H_
#define REGISTERATIONHANDLER_H_

#include "LampInfo.h"

namespace mta_yos_zeroconf_lamp {

class RegisterationHandler {
private:
	const LampInfo info;
public:
	RegisterationHandler(const LampInfo &info);
	virtual ~RegisterationHandler();
	void registerService();
};


}

#endif /* REGISTERATIONHANDLER_H_ */
