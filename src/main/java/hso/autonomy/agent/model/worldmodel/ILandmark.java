/*******************************************************************************
 * Copyright 2008 - 2020 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under MIT License (see LICENSE).
 *******************************************************************************/
package hso.autonomy.agent.model.worldmodel;

import hso.autonomy.agent.model.worldmodel.localizer.IReferencePoint;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

/**
 * Base interface for all Landmarks on the filed (goal posts, flags)
 * @author Klaus Dorer
 */
public interface ILandmark extends IVisibleObject, IReferencePoint
{
	/**
	 * @return the known position of the landmark (global coordinates)
	 */
	Vector3D getKnownPosition();
}