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
package magma.fatproxy.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class AgentFatProxyTest
{
	@Test
	public void testAppendMessages()
	{
		checkAppend("This is first", " and second");
		checkAppend("This is first", "");
		checkAppend("", " and second");
		checkAppend("", "");
	}

	private void checkAppend(String string1, String string2)
	{
		String result = new String(AgentFatProxy.appendMessages(string1.getBytes(), string2.getBytes()));
		assertEquals(string1 + string2, result);
	}

	@Test
	public void testRemoveNodes()
	{
		checkRemove("(was ist)(ein Text)", "(was ist)(ein Text)", new String[] {"Das", "kein"});
		checkRemove("(Das ist)(ein Text)", "(ein Text)", new String[] {"Das", "kein"});
		checkRemove("(Das (und noch) ist)(ein Text)", "(ein Text)", new String[] {"Das", "kein"});
		checkRemove("(Das (und noch) ist)(ein Text", "(ein Text", new String[] {"Das", "kein"});
		checkRemove("(vorne noch)(Das ist)(ein Text)", "(vorne noch)(ein Text)", new String[] {"Das", "kein"});
		checkRemove("(vorne noch)(Das ist)(ein Text)", "(ein Text)", new String[] {"Das", "vorne"});
	}

	private void checkRemove(String text, String expected, String[] keywords)
	{
		String result = new String(AgentFatProxy.removeNodes(text.getBytes(), keywords));
		assertEquals(expected, result);
	}

	@Test
	public void testKeepNodes()
	{
		checkKeep("(was ist)(ein Text)", "", new String[] {"Das", "kein"});
		checkKeep("(Das ist)(ein Text)", "(Das ist)", new String[] {"Das", "kein"});
		checkKeep("(Das (und noch) ist)(ein Text)", "(Das (und noch) ist)", new String[] {"Das", "kein"});
		checkKeep("(Das (und noch) ist)(ein Text", "(ein Text", new String[] {"was", "ein"});
		checkKeep("(vorne noch)(Das ist)(ein Text)", "(Das ist)", new String[] {"Das", "kein"});
		checkKeep("(vorne noch)(Das ist)(ein Text)", "(Das ist)(ein Text)", new String[] {"Das", "ein"});
	}

	private void checkKeep(String text, String expected, String[] keywords)
	{
		String result = new String(AgentFatProxy.keepNodes(text.getBytes(), keywords));
		assertEquals(expected, result);
	}
}
