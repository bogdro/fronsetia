/*
 * RestEndpointParserTest - a test for RestEndpointParser.
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

import bogdrosoft.fronsetia.EndpointType;

/**
 * RestEndpointParserTest - a test for RestEndpointParser.
 * @author Bogdan 'bogdro' Drozdowski, bogdro (at) users . sourceforge . net
 */
public class RestEndpointParserTest
{
	@Test
	public void testParse()
	{
		String url = "http://www.example.com/service";
		RestEndpointParser p = new RestEndpointParser();
		p.parse(url);
		assertEquals(url, p.getUrlForOperation(""));
		assertNotNull(p.getListOfOperations());
		assertNotNull(p.getDefaultPayloadEpilogueForOperation(""));
		assertNotNull(p.getDefaultPayloadForOperation(""));
		assertNotNull(p.getDefaultPayloadHeaderForOperation(""));
		assertNotNull(p.getDefaultPayloadMiddleForOperation(""));
		assertNotNull(p.getDefaultPayloadProloguesForOperation(""));
		assertNotNull(p.getListOfTransportHeadersForOperation(""));
		assertNull(p.getParsingException());
	}

	@Test
	public void testParseInvalidUrl()
	{
		String url = "service";
		RestEndpointParser p = new RestEndpointParser();
		p.parse(url);
		assertEquals("", p.getUrlForOperation(""));
		assertNotNull(p.getParsingException());
	}

	@Test
	public void testType()
	{
		RestEndpointParser p = new RestEndpointParser();
		assertEquals(EndpointType.REST, p.getType());
	}

	@Test
	public void testContentType()
	{
		RestEndpointParser p = new RestEndpointParser();
		assertEquals("application/json", p.getDefaultContentType());
	}
}
