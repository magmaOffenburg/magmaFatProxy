/*******************************************************************************
 * Copyright 2008 - 2020 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under MIT License (see LICENSE).
 *******************************************************************************/
package hso.autonomy.util.commandline;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class BooleanArgumentTest
{
	private BooleanArgument testee;

	@Before
	public void setUp()
	{
		testee = new BooleanArgument("flag", null);
	}

	@Test
	public void testParseIsPresent()
	{
		testParse(true, "--flag");
	}

	@Test
	public void testParseIsNotPresent()
	{
		testParse(false, "--flags");
		testParse(false, "--flag=value");
	}

	private void testParse(Boolean expected, String... args)
	{
		assertEquals(expected, testee.parse(args));
	}
}
