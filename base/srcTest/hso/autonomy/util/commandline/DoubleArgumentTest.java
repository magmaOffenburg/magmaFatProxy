/*******************************************************************************
 * Copyright 2008 - 2020 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under MIT License (see LICENSE).
 *******************************************************************************/
package hso.autonomy.util.commandline;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class DoubleArgumentTest
{
	private DoubleArgument testee;

	@Before
	public void setUp()
	{
		testee = new DoubleArgument("argument", 5.0, 3, 10, null);
	}

	@Test
	public void testParseAllowedValue()
	{
		testParse(3.0, "--argument=3");
		testParse(7.0, "--argument=7");
		testParse(10.0, "--argument=10");
	}

	@Test(expected = ArgumentParsingException.class)
	public void testParseValueTooSmall()
	{
		testee.parse("--argument=2");
		testee.parse("--argument=-30");
	}

	@Test(expected = ArgumentParsingException.class)
	public void testParseValueTooBig()
	{
		testParse(5.0, "--argument=11");
		testParse(5.0, "--argument=100");
	}

	@Test(expected = ArgumentParsingException.class)
	public void testParseEmptyValue()
	{
		testParse(5.0, "--argument=");
	}

	@Test(expected = ArgumentParsingException.class)
	public void testParseNonInteger()
	{
		testParse(5.0, "--argument=notInt");
	}

	@Test
	public void testConstructorNoBounds()
	{
		testee = new DoubleArgument("argument", 5.0, null);
		testParse(Double.NEGATIVE_INFINITY, "--argument=" + Double.NEGATIVE_INFINITY);
		testParse(Double.POSITIVE_INFINITY, "--argument=" + Double.POSITIVE_INFINITY);
	}

	@Test
	public void testConstructorNoMaxValue()
	{
		testee = new DoubleArgument("argument", 5.0, 3, null);
		testParse(Double.POSITIVE_INFINITY, "--argument=" + Double.POSITIVE_INFINITY);
	}

	private void testParse(Double expected, String... args)
	{
		assertEquals(expected, testee.parse(args));
	}
}
