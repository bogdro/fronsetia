/*
 * RestInterpreterTest - a test for RestInterpreter.
 *
 * Copyright (C) 2025 Bogdan 'bogdro' Drozdowski, bogdro (at) users . sourceforge . net
 *
 * This file is part of Fronsetia (Free Online Service Testing Application),
 *  a web application that allows testing webservices.
 *
 * Project homepage: https://fronsetia.sourceforge.io/
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see http://www.gnu.org/licenses/.
 */

package bogdrosoft.fronsetia.rest;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

/**
 * RestInterpreterTest - a test for RestInterpreter.
 * @author Bogdan 'bogdro' Drozdowski, bogdro (at) users . sourceforge . net
 */
public class RestInterpreterTest
{
	@Test
	public void testParseResponse() throws Exception
	{
		RestInterpreter ri = new RestInterpreter();
		ri.parseResponse("{ \"a\": { \"b\": 1 }, \"c\": 2 }");
		assertFalse(ri.hasFault());
		assertTrue(ri.getBodyElements().contains("a"));
	}

	@Test
	public void testParseResponseFault() throws Exception
	{
		RestInterpreter ri = new RestInterpreter();
		ri.parseResponse("{ \"a\": { \"b\": 1 }, \"c\": 2 }");
		assertFalse(ri.hasFault());
	}

	@Test
	public void testParseResponseNull() throws Exception
	{
		RestInterpreter ri = new RestInterpreter();
		ri.parseResponse(null);
		assertFalse(ri.hasFault());
		assertNotNull(ri.getBodyElements());
	}
}
