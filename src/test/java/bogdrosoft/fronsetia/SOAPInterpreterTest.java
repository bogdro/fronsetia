/*
 * SOAPInterpreterTest - a test for SOAPInterpreter.
 *
 * Copyright (C) 2023-2024 Bogdan 'bogdro' Drozdowski, bogdro (at) users . sourceforge . net
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

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

/**
 * SOAPInterpreterTest - a test for SOAPInterpreter.
 * @author Bogdan 'bogdro' Drozdowski, bogdro (at) users . sourceforge . net
 */
public class SOAPInterpreterTest
{
	@Test
	public void testParseResponse() throws Exception
	{
		SOAPInterpreter si = new SOAPInterpreter();
		si.parseResponse(
			"<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
			+ "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">\n"
			+ "<soapenv:Body>\n"
			+ "<getVersionResponse soapenv:encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\">\n"
			+ "<getVersionReturn xsi:type=\"xsd:string\">\n"
			+ "Apache Axis version: 1.4\n"
			+ "Built on Apr 22, 2006 (06:55:48 PDT)</getVersionReturn>\n"
			+ "</getVersionResponse>\n"
			+ "</soapenv:Body>\n"
			+ "</soapenv:Envelope>");
		assertFalse(si.wasFault());
		assertTrue(si.getBodyElements().contains("getVersionResponse"));
	}

	@Test
	public void testParseResponseFault() throws Exception
	{
		SOAPInterpreter si = new SOAPInterpreter();
		si.parseResponse(
			"<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
			+ "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">\n"
			+ "<soapenv:Body>\n"
			+ "<soapenv:Fault>\n"
			+ "<faultcode>\n"
			+ "Server.userException</faultcode>\n"
			+ "<faultstring>\n"
			+ "No such operation 'getVersion'</faultstring>\n"
			+ "<detail>\n"
			+ "<ns1:hostname xmlns:ns1=\"http://xml.apache.org/axis/\">\n"
			+ "cepheus</ns1:hostname>\n"
			+ "</detail>\n"
			+ "</soapenv:Fault>\n"
			+ "</soapenv:Body>\n"
			+ "</soapenv:Envelope>");
		assertTrue(si.wasFault());
	}

	@Test
	public void testParseResponseNull() throws Exception
	{
		SOAPInterpreter si = new SOAPInterpreter();
		si.parseResponse(null);
		assertFalse(si.wasFault());
		assertNull(si.getBodyElements());
	}
}
