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
package magma.robots.nao.decision.behavior.movement.getup;

import hso.autonomy.agent.decision.behavior.IBehavior;
import hso.autonomy.agent.model.thoughtmodel.IThoughtModel;
import magma.agent.decision.behavior.IBehaviorConstants;
import magma.agent.decision.behavior.IKeepBehavior;
import magma.agent.decision.behavior.movement.Movement;
import magma.agent.decision.behavior.movement.MovementBehavior;
import magma.agent.decision.behavior.movement.MovementPhase;
import magma.robots.nao.INaoJoints;

/**
 * This behavior will get up the agent when it lies on the front.<br>
 * Just don't ask why...
 *
 * @author Stefan Glaser
 *
 */
public class GetUpFromFront extends MovementBehavior implements INaoJoints
{
	/** movement to get on back */
	protected Movement salto;

	private static Movement createMovement()
	{
		Movement getUp = new Movement("getupFront");
		getUp.add(new MovementPhase("phase1", 30)
						  .add(LShoulderPitch, 0, 7f)
						  .add(LShoulderYaw, -10, 7f)
						  .add(LArmYaw, 0, 7f)
						  .add(LArmRoll, 0, 7f)

						  .add(LHipYawPitch, 0, 7f)
						  .add(LHipPitch, 100, 7f)
						  .add(LHipRoll, 0, 7f)
						  .add(LKneePitch, -130, 7f)
						  .add(LFootPitch, 75, 7f)
						  .add(LFootRoll, 0, 7f)

						  .add(RShoulderPitch, 0, 7f)
						  .add(RShoulderYaw, 10, 7f)
						  .add(RArmYaw, 0, 7f)
						  .add(RArmRoll, 0, 7f)

						  .add(RHipYawPitch, 0, 7f)
						  .add(RHipPitch, 100, 7f)
						  .add(RHipRoll, 0, 7f)
						  .add(RKneePitch, -130, 7f)
						  .add(RFootPitch, 75, 7f)
						  .add(RFootRoll, 0, 7f));
		getUp.add(new MovementPhase("phase2", 20)
						  .add(LShoulderPitch, -20, 1)
						  .add(LHipYawPitch, -90, 4)
						  .add(RShoulderPitch, -20, 1)
						  .add(RHipYawPitch, -90, 4));
		getUp.add(new MovementPhase("phase3", 30)
						  .add(LShoulderPitch, -35, 1)
						  .add(LHipYawPitch, -90, 7f)
						  .add(LFootPitch, 0, 4)
						  .add(RShoulderPitch, -35, 1)
						  .add(RHipYawPitch, -90, 7f)
						  .add(RFootPitch, 0, 4));
		getUp.add(new MovementPhase("phase4", 25)
						  .add(LShoulderPitch, 0, 1)
						  .add(LHipYawPitch, 0, 1.4f)
						  .add(LHipPitch, 0, 1.0f)
						  .add(LHipRoll, 0, 1)
						  .add(LKneePitch, 0, 2)
						  .add(LFootPitch, 0, 1)
						  .add(RShoulderPitch, 0, 1)
						  .add(RHipYawPitch, 0, 1.4f)
						  .add(RHipPitch, 0, 1.0f)
						  .add(RHipRoll, 0, 1)
						  .add(RKneePitch, 0, 2)
						  .add(RFootPitch, 0, 1));

		getUp.add(new MovementPhase("phase5", 20)
						  .add(LShoulderPitch, 0, 3.5f)
						  .add(LHipYawPitch, 0, 4.5f)
						  .add(LHipPitch, 35, 4.5f)
						  .add(LHipRoll, 0, 3.5f)
						  .add(LKneePitch, -70, 7)
						  .add(LFootPitch, 35, 3)

						  .add(RShoulderPitch, 0, 3.5f)
						  .add(RHipYawPitch, 0, 4.5f)
						  .add(RHipPitch, 35, 4.5f)
						  .add(RHipRoll, 0, 3.5f)
						  .add(RKneePitch, -70, 7)
						  .add(RFootPitch, 35, 3));

		return getUp;
	}

	public GetUpFromFront(IThoughtModel thoughtModel)
	{
		super(IBehaviorConstants.GET_UP_FRONT, thoughtModel, createMovement());

		salto = new Movement("salto");
		salto.add(new MovementPhase("phase1", 15)
						  .add(LShoulderPitch, 90, 7f)
						  .add(LShoulderYaw, -10, 7f)
						  .add(LArmYaw, 0, 7f)
						  .add(LArmRoll, 0, 7f)

						  .add(RShoulderPitch, 90, 7f)
						  .add(RShoulderYaw, 10, 7f)
						  .add(RArmYaw, 0, 7f)
						  .add(RArmRoll, 0, 7f));
		salto.add(new MovementPhase("phase2", 10)
						  .add(LShoulderPitch, 90, 7f)
						  .add(LShoulderYaw, -10, 7f)
						  .add(LArmYaw, 0, 7f)
						  .add(LArmRoll, 0, 7f)

						  .add(LHipYawPitch, 0, 7f)
						  .add(LHipPitch, 0, 7f)
						  .add(LHipRoll, 0, 7f)
						  .add(LKneePitch, 0, 7f)
						  .add(LFootPitch, 0, 7f)
						  .add(LFootRoll, 0, 7f)

						  .add(RShoulderPitch, 90, 7f)
						  .add(RShoulderYaw, 10, 7f)
						  .add(RArmYaw, 0, 7f)
						  .add(RArmRoll, 0, 7f)

						  .add(RHipYawPitch, 0, 7f)
						  .add(RHipPitch, 0, 7f)
						  .add(RHipRoll, 0, 7f)
						  .add(RKneePitch, 0, 7f)
						  .add(RFootPitch, 0, 7f)
						  .add(RFootRoll, 0, 7f));
	}

	@Override
	public void perform()
	{
		if (!currentMovement.isFinished()) {
			// try to avoid standing on hands
			if (currentMovement == initialMovement) {
				if (currentMovement.getCurrentPhase().getName().equals("phase2")) {
					if (getWorldModel().getThisPlayer().getOrientation().getMatrix()[2][2] < -0.6) {
						switchTo(salto);
					}
				}
			}
		}

		super.perform();
	}

	@Override
	public IBehavior switchFrom(IBehavior actualBehavior)
	{
		IBehavior realBehavior = actualBehavior.getRootBehavior();

		if (realBehavior instanceof GetUpFromBack || realBehavior instanceof IKeepBehavior) {
			// don't switch directly from get up back and keep behaviors
			return super.switchFrom(actualBehavior);
		}

		// by default allow switching to this behavior immediately
		if (actualBehavior.isFinished()) {
			actualBehavior.onLeavingBehavior(this);
		} else {
			actualBehavior.abort();
		}

		return this;
	}
}
