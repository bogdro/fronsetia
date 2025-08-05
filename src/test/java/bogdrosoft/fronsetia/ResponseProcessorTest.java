/*
 * ResponseProcessorTest - a test for ResponseProcessor.
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

package bogdrosoft.fronsetia;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import bogdrosoft.fronsetia.rest.RestInterpreter;
import bogdrosoft.fronsetia.soap.SoapInterpreter;

/**
 * ResponseProcessorTest - a test for ResponseProcessor.
 * @author Bogdan 'bogdro' Drozdowski, bogdro (at) users . sourceforge . net
 */
public class ResponseProcessorTest
{
	@Test
	public void testResponseInterpreterSoapType()
	{
		ResponseProcessor p = new ResponseProcessor();
		assertTrue(p.getResponseInterpreter(EndpointType.SOAP) instanceof SoapInterpreter);
	}

	@Test
	public void testResponseInterpreterRestType()
	{
		ResponseProcessor p = new ResponseProcessor();
		assertTrue(p.getResponseInterpreter(EndpointType.REST) instanceof RestInterpreter);
	}
}
