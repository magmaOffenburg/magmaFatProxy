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
package magma.agent.model.worldmodel.localizer.impl;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import hso.autonomy.agent.model.worldmodel.localizer.IEnvironmentModel;
import hso.autonomy.agent.model.worldmodel.localizer.ILocalizer;
import hso.autonomy.agent.model.worldmodel.localizer.IReferencePoint;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import magma.agent.model.worldmeta.impl.RCServerMetaModelV62;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Unit test for {@link hso.autonomy.agent.model.worldmodel.localizer.IReferencePoint}
 *
 * @author Klaus Dorer
 */
public class LocalizerBaseTest
{
	ILocalizer testee;

	IEnvironmentModel environment;

	IReferencePoint G1L;

	IReferencePoint G2L;

	IReferencePoint G1R;

	IReferencePoint G2R;

	IReferencePoint F1L;

	IReferencePoint F2L;

	IReferencePoint F1R;

	IReferencePoint F2R;

	List<IReferencePoint> flags = null;

	@BeforeEach
	public void setUp()
	{
		Map<String, Vector3D> landmarks = RCServerMetaModelV62.INSTANCE.getLandmarks();

		G1L = mock(IReferencePoint.class);
		when(G1L.getKnownPosition()).thenReturn(landmarks.get("G1L"));

		G2L = mock(IReferencePoint.class);
		when(G2L.getKnownPosition()).thenReturn(landmarks.get("G2L"));

		G1R = mock(IReferencePoint.class);
		when(G1R.getKnownPosition()).thenReturn(landmarks.get("G1R"));

		G2R = mock(IReferencePoint.class);
		when(G2R.getKnownPosition()).thenReturn(landmarks.get("G2R"));

		F1L = mock(IReferencePoint.class);
		when(F1L.getKnownPosition()).thenReturn(landmarks.get("F1L"));

		F2L = mock(IReferencePoint.class);
		when(F2L.getKnownPosition()).thenReturn(landmarks.get("F2L"));

		F1R = mock(IReferencePoint.class);
		when(F1R.getKnownPosition()).thenReturn(landmarks.get("F1R"));

		F2R = mock(IReferencePoint.class);
		when(F2R.getKnownPosition()).thenReturn(landmarks.get("F2R"));
		flags = new ArrayList<>();

		environment = mock(IEnvironmentModel.class);
		when(environment.getReferencePoints()).thenReturn(flags);
	}

	@Test
	public void emptyDummyTestToKeepJenkinsHappy() throws Exception
	{
	}
}
