/*******************************************************************************
 * Copyright 2008 - 2020 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under MIT License (see LICENSE).
 *******************************************************************************/
package hso.autonomy.util.commandline;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

public class EnumArgumentTest
{
	private enum TestEnum { VALUE, OTHER_VALUE }

	@Test
	public void testValidValues()
	{
		EnumArgument<TestEnum> arg = new EnumArgument<>("test", TestEnum.VALUE, "", TestEnum.class);

		assertEquals(arg.parse(""), TestEnum.VALUE);
		assertEquals(arg.parse("--test=value"), TestEnum.VALUE);
		assertEquals(arg.parse("--test=otherValue"), TestEnum.OTHER_VALUE);
	}

	@Test
	public void testNullDefault()
	{
		EnumArgument<TestEnum> arg = new EnumArgument<>("test", null, "", TestEnum.class);
		assertEquals(arg.parse(""), null);
	}

	@Test
	public void testInvalidValue()
	{
		EnumArgument<TestEnum> arg = new EnumArgument<>("test", null, "", TestEnum.class);
		assertThrows(ArgumentParsingException.class, () -> { assertEquals(arg.parse("--test=invalid"), null); });
	}
}
