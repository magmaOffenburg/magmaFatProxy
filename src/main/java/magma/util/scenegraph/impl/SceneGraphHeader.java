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
package magma.util.scenegraph.impl;

import magma.util.scenegraph.ISceneGraphHeader;

public class SceneGraphHeader implements ISceneGraphHeader
{
	private String type;

	private int majorVersion;

	private int minorVersion;

	public SceneGraphHeader()
	{
	}

	public SceneGraphHeader(ISceneGraphHeader other)
	{
		this.type = other.getType();
		this.majorVersion = other.getMajorVersion();
		this.minorVersion = other.getMinorVersion();
	}

	public SceneGraphHeader(String type, int majorVersion, int minorVersion)
	{
		this.type = type;
		this.majorVersion = majorVersion;
		this.minorVersion = minorVersion;
	}

	@Override
	public String getType()
	{
		return type;
	}

	@Override
	public int getMajorVersion()
	{
		return majorVersion;
	}

	@Override
	public int getMinorVersion()
	{
		return minorVersion;
	}

	public void setType(String type)
	{
		this.type = type;
	}

	public void setMajorVersion(int majorVersion)
	{
		this.majorVersion = majorVersion;
	}

	public void setMinorVersion(int minorVersion)
	{
		this.minorVersion = minorVersion;
	}

	@Override
	public void update(ISceneGraphHeader other)
	{
		setType(other.getType());
		setMajorVersion(other.getMajorVersion());
		setMinorVersion(other.getMinorVersion());
	}
}
