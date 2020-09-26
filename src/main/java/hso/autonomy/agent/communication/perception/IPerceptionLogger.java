/*******************************************************************************
 * Copyright 2008 - 2020 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under MIT License (see LICENSE).
 *******************************************************************************/
package hso.autonomy.agent.communication.perception;

import java.util.Map;

public interface IPerceptionLogger {
	void start();

	void stop();

	void log(Map<String, IPerceptor> perceptorMap);
}
