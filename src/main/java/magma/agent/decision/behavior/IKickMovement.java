/*******************************************************************************
 * Copyright 2008 - 2020 Hochschule Offenburg
 *
 * This file is part of magmaOffenburg.
 * magmaOffenburg is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * magmaOffenburg is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with magmaOffenburg. If not, see <http://www.gnu.org/licenses/>.
 *******************************************************************************/
package magma.agent.decision.behavior;

import hso.autonomy.agent.decision.behavior.IBehavior;
import magma.agent.decision.behavior.base.KickDistribution;

/**
 * The subset of the <code>IKick</code> interface which is needed for the kick
 * part of <code>StabilizeAndKick</code> behaviors.
 */
public interface IKickMovement extends IBehavior {
	/**
	 * @return the maximum kick distance this kick is able to kick
	 */
	double getMaxKickDistance();
	/**
	 * @return the minimum kick distance this kick is able to kick
	 */
	double getMinKickDistance();

	/**
	 * @param kickPower the kickPower to set (factor 0.5 means half long)
	 */
	void setKickPower(float kickPower);

	/**
	 * @return whether the agent is expected fall down after the kick is complete
	 */
	boolean isUnstable();

	/**
	 * @return how many cycles it takes until the ball is hit.
	 */
	int getBallHitCycles();

	/**
	 * @return Probability distribution of this kick's distance / angle.
	 */
	KickDistribution getDistribution();
}
