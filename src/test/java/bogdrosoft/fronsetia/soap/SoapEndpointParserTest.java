/*
 * SoapEndpointParserTest - a test for SoapEndpointParser.
 *
 * Copyright (C) 2023-2025 Bogdan 'bogdro' Drozdowski, bogdro (at) users . sourceforge . net
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

package bogdrosoft.fronsetia.soap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.net.URL;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.w3c.dom.Element;

/**
 * SoapEndpointParserTest - a test for SoapEndpointParser.
 * @author Bogdan 'bogdro' Drozdowski, bogdro (at) users . sourceforge . net
 */
public class SoapEndpointParserTest
{
	private static final String OPER_NAME = "getPriceList";
	private static final String OPER_URL = "http://localhost:8080/soap/servlet/rpcrouter";

	private static String getFullPathFor(String file)
	{
		String dir = System.getProperty("test.rsrc.dir");
		if (dir != null) {
			// maven build
			dir += "/" + file;
		} else {
			// IDE unit test run
			URL u = ClassLoader.getSystemResource(file);
			dir = u.toString().replace("file:", "");
		}
		return dir;
	}

	private void checkFile(String fileName) throws Exception
	{
		SoapEndpointParser p = new SoapEndpointParser();
		p.parse(getFullPathFor(fileName));
		Set<String> operations = p.getListOfOperations();
		assertTrue(operations.contains(OPER_NAME));
		assertEquals(OPER_URL, p.getUrlForOperation(OPER_NAME));
	}

	@Test
	public void testWSDLCheck() throws Exception
	{
		checkFile("sample.wsdl");
	}

	@Test
	public void testWSDLCheckV12() throws Exception
	{
		checkFile("sample-v1.2.wsdl");
	}

	@Test
	public void testWSDLCheckNoServices() throws Exception
	{
		SoapEndpointParser p = new SoapEndpointParser();
		p.parse(getFullPathFor("sample-nosrv.wsdl"));
		assertNotNull(p.getListOfOperations());
	}

	@Test
	public void testWSDLCheckNoTypes() throws Exception
	{
		checkFile("sample-notypes.wsdl");
	}

	@Test
	public void testWSDLCheckNoImports() throws Exception
	{
		checkFile("sample-noimports.wsdl");
	}

	@Test
	public void testWSDLCheckNoIncludes() throws Exception
	{
		checkFile("sample-noincludes.wsdl");
	}

	@Test
	public void testWSDLCheckNoPorts() throws Exception
	{
		SoapEndpointParser p = new SoapEndpointParser();
		p.parse(getFullPathFor("sample-noports.wsdl"));
		assertNotNull(p.getListOfOperations());
	}

	@Test
	public void testWSDLCheckNoOperations() throws Exception
	{
		SoapEndpointParser p = new SoapEndpointParser();
		p.parse(getFullPathFor("sample-nooperations.wsdl"));
		assertNotNull(p.getListOfOperations());
	}

	@Test
	public void testWSDLCheckNoOperInput() throws Exception
	{
		SoapEndpointParser p = new SoapEndpointParser();
		p.parse(getFullPathFor("sample-noinput.wsdl"));
		assertNotNull(p.getListOfOperations());
	}

	@Test
	public void testWSDLCheckNoDefinitions() throws Exception
	{
		SoapEndpointParser p = new SoapEndpointParser();
		p.parse(getFullPathFor("sample-nodef.wsdl"));
		assertNotNull(p.getListOfOperations());
	}

	@Test
	public void testWSDLCheckEmptyTypes() throws Exception
	{
		SoapEndpointParser p = new SoapEndpointParser();
		p.parse(getFullPathFor("sample-empty-types.wsdl"));
		assertNotNull(p.getListOfOperations());
	}

	@Test
	public void testWSDLCheckPortNoBinding() throws Exception
	{
		SoapEndpointParser p = new SoapEndpointParser();
		p.parse(getFullPathFor("sample-no-bind.wsdl"));
		assertNotNull(p.getListOfOperations());
	}

	@Test
	public void testWSDLCheckBindingNoType() throws Exception
	{
		SoapEndpointParser p = new SoapEndpointParser();
		p.parse(getFullPathFor("sample-bind-no-type.wsdl"));
		assertNotNull(p.getListOfOperations());
	}

	@Test
	public void testWSDLCheckOperationNoName() throws Exception
	{
		SoapEndpointParser p = new SoapEndpointParser();
		p.parse(getFullPathFor("sample-oper-no-name.wsdl"));
		assertNotNull(p.getListOfOperations());
	}

	@Test
	public void testWSDLCheckInputNoMsg() throws Exception
	{
		SoapEndpointParser p = new SoapEndpointParser();
		p.parse(getFullPathFor("sample-in-no-msg.wsdl"));
		assertNotNull(p.getListOfOperations());
	}

	@Test
	public void testWSDLCheckMsgNoQName() throws Exception
	{
		SoapEndpointParser p = new SoapEndpointParser();
		p.parse(getFullPathFor("sample-msg-no-qname.wsdl"));
		assertNotNull(p.getListOfOperations());
	}

	@Test
	public void testWSDLCheckInMsgNoQName() throws Exception
	{
		SoapEndpointParser p = new SoapEndpointParser();
		p.parse(getFullPathFor("sample-in-msg-no-name.wsdl"));
		assertNotNull(p.getListOfOperations());
	}

	@Test
	public void testWSDLCheckInMsgNoLocalName() throws Exception
	{
		SoapEndpointParser p = new SoapEndpointParser();
		p.parse(getFullPathFor("sample-in-msg-no-localname.wsdl"));
		assertNotNull(p.getListOfOperations());
	}

	@Test
	public void testWSDLCheckInMsgPartNoType() throws Exception
	{
		SoapEndpointParser p = new SoapEndpointParser();
		p.parse(getFullPathFor("sample-msg-part-no-type.wsdl"));
		assertNotNull(p.getListOfOperations());
	}

	@Test
	public void testWSDLCheckUnknownSoapVersion() throws Exception
	{
		SoapEndpointParser p = new SoapEndpointParser();
		p.parse(getFullPathFor("sample-v1.3.wsdl"));
		assertNotNull(p.getListOfOperations());
	}

	@Test
	public void testProcessXSD() throws Exception
	{
		SoapEndpointParser p = new SoapEndpointParser();
		p.parse(getFullPathFor("sample.wsdl"));
		Set<String> schemaLocations = new HashSet<>();
		schemaLocations.add(getFullPathFor("shiporder.xsd"));
		p.processXSD(schemaLocations, "shiporder", null);
		// second time, to test globalElems
		String result = p.processXSD(schemaLocations, "shiporder", null);
		assertEquals("<shiporder orderid=\"string\">\n"
				+ "  <orderperson>string</orderperson>\n"
				+ "  <shipto>\n"
				+ "    <name>string</name>\n"
				+ "    <address>string</address>\n"
				+ "    <city>string</city>\n"
				+ "    <country>string</country>\n"
				+ "  </shipto>\n"
				+ "  <!--1 or more repetitions:-->\n"
				+ "  <item>\n"
				+ "    <title>string</title>\n"
				+ "    <!--Optional:-->\n"
				+ "    <note>string</note>\n"
				+ "    <quantity>201</quantity>\n"
				+ "    <price>1000.00</price>\n"
				+ "  </item>\n"
				+ "</shiporder>", result);
	}

	@Test
	public void testProcessXSDBothNull() throws Exception
	{
		SoapEndpointParser p = new SoapEndpointParser();
		p.parse(getFullPathFor("sample.wsdl"));
		assertEquals("", p.processXSD(null, "root", null));
	}

	@Test
	public void testProcessXSDLocationsNull() throws Exception
	{
		SoapEndpointParser p = new SoapEndpointParser();
		p.parse(getFullPathFor("sample.wsdl"));
		assertEquals("", p.processXSD(null, "root", new HashSet<Element>()));
	}

	@Test
	public void testProcessXSDElementsNull() throws Exception
	{
		SoapEndpointParser p = new SoapEndpointParser();
		p.parse(getFullPathFor("sample.wsdl"));
		assertEquals("", p.processXSD(new HashSet<String>(), "root", null));
	}

	@Test
	public void testProcessXSDNullSchemaElement() throws Exception
	{
		SoapEndpointParser p = new SoapEndpointParser();
		p.parse(getFullPathFor("sample.wsdl"));
		Set<Element> schemaElements = new HashSet<>();
		schemaElements.add(null);
		assertEquals("", p.processXSD(null, "root", schemaElements));
	}

	@Test
	public void testProcessXSDWrongRoot() throws Exception
	{
		SoapEndpointParser p = new SoapEndpointParser();
		p.parse(getFullPathFor("sample.wsdl"));
		Set<String> schemaLocations = new HashSet<>();
		schemaLocations.add(getFullPathFor("shiporder.xsd"));
		assertEquals("", p.processXSD(schemaLocations, "testroot", null));
	}

	@Test
	public void testProcessXSDNoRoot() throws Exception
	{
		SoapEndpointParser p = new SoapEndpointParser();
		p.parse(getFullPathFor("sample.wsdl"));
		Set<String> schemaLocations = new HashSet<>();
		schemaLocations.add(getFullPathFor("shiporder.xsd"));
		assertEquals("", p.processXSD(schemaLocations, null, null));
	}
}
