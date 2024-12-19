/*
 * WSDLCheckTest - a test for WSDLCheck.
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.net.URL;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.w3c.dom.Element;

/**
 * WSDLCheckTest - a test for WSDLCheck.
 * @author Bogdan 'bogdro' Drozdowski, bogdro (at) users . sourceforge . net
 */
public class WSDLCheckTest
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
		WSDLCheck w = new WSDLCheck(getFullPathFor(fileName));
		Map<String, String> opXmls = w.getOperations();
		assertTrue(opXmls.containsKey(OPER_NAME));
		Map<String, String> opUrls = w.getOperationURLs();
		assertEquals(OPER_URL, opUrls.get(OPER_NAME));
	}

	@Test
	public void testWSDLCheck() throws Exception
	{
		checkFile("sample.wsdl");
	}

	@Test
	public void testWSDLCheckReverse() throws Exception
	{
		WSDLCheck w = new WSDLCheck(getFullPathFor("sample.wsdl"));
		Map<String, String> opUrls = w.getOperationURLs();
		assertEquals(OPER_URL, opUrls.get(OPER_NAME));
		Map<String, String> opXmls = w.getOperations();
		assertTrue(opXmls.containsKey(OPER_NAME));
	}

	@Test
	public void testWSDLCheckV12() throws Exception
	{
		checkFile("sample-v1.2.wsdl");
	}

	@Test
	public void testWSDLCheckNoServices() throws Exception
	{
		WSDLCheck w = new WSDLCheck(getFullPathFor("sample-nosrv.wsdl"));
		assertNotNull(w.getOperations());
		assertNotNull(w.getOperationURLs());
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
		WSDLCheck w = new WSDLCheck(getFullPathFor("sample-noports.wsdl"));
		assertNotNull(w.getOperations());
		assertNotNull(w.getOperationURLs());
	}

	@Test
	public void testWSDLCheckNoOperations() throws Exception
	{
		WSDLCheck w = new WSDLCheck(getFullPathFor("sample-nooperations.wsdl"));
		assertNotNull(w.getOperations());
		assertNotNull(w.getOperationURLs());
	}

	@Test
	public void testWSDLCheckNoOperInput() throws Exception
	{
		WSDLCheck w = new WSDLCheck(getFullPathFor("sample-noinput.wsdl"));
		assertNotNull(w.getOperations());
		assertNotNull(w.getOperationURLs());
	}

	@Test
	public void testWSDLCheckNoDefinitions() throws Exception
	{
		WSDLCheck w = new WSDLCheck(getFullPathFor("sample-nodef.wsdl"));
		assertNotNull(w.getOperations());
		assertNotNull(w.getOperationURLs());
	}

	@Test
	public void testWSDLCheckEmptyTypes() throws Exception
	{
		WSDLCheck w = new WSDLCheck(getFullPathFor("sample-empty-types.wsdl"));
		assertNotNull(w.getOperations());
		assertNotNull(w.getOperationURLs());
	}

	@Test
	public void testWSDLCheckPortNoBinding() throws Exception
	{
		WSDLCheck w = new WSDLCheck(getFullPathFor("sample-no-bind.wsdl"));
		assertNotNull(w.getOperations());
		assertNotNull(w.getOperationURLs());
	}

	@Test
	public void testWSDLCheckBindingNoType() throws Exception
	{
		WSDLCheck w = new WSDLCheck(getFullPathFor("sample-bind-no-type.wsdl"));
		assertNotNull(w.getOperations());
		assertNotNull(w.getOperationURLs());
	}

	@Test
	public void testWSDLCheckOperationNoName() throws Exception
	{
		WSDLCheck w = new WSDLCheck(getFullPathFor("sample-oper-no-name.wsdl"));
		assertNotNull(w.getOperations());
		assertNotNull(w.getOperationURLs());
	}

	@Test
	public void testWSDLCheckInputNoMsg() throws Exception
	{
		WSDLCheck w = new WSDLCheck(getFullPathFor("sample-in-no-msg.wsdl"));
		assertNotNull(w.getOperations());
		assertNotNull(w.getOperationURLs());
	}

	@Test
	public void testWSDLCheckMsgNoQName() throws Exception
	{
		WSDLCheck w = new WSDLCheck(getFullPathFor("sample-msg-no-qname.wsdl"));
		assertNotNull(w.getOperations());
		assertNotNull(w.getOperationURLs());
	}

	@Test
	public void testWSDLCheckInMsgNoQName() throws Exception
	{
		WSDLCheck w = new WSDLCheck(getFullPathFor("sample-in-msg-no-name.wsdl"));
		assertNotNull(w.getOperations());
		assertNotNull(w.getOperationURLs());
	}

	@Test
	public void testWSDLCheckInMsgNoLocalName() throws Exception
	{
		WSDLCheck w = new WSDLCheck(getFullPathFor("sample-in-msg-no-localname.wsdl"));
		assertNotNull(w.getOperations());
		assertNotNull(w.getOperationURLs());
	}

	@Test
	public void testWSDLCheckUnknownSoapVersion() throws Exception
	{
		WSDLCheck w = new WSDLCheck(getFullPathFor("sample-v1.3.wsdl"));
		assertNotNull(w.getOperations());
		assertNotNull(w.getOperationURLs());
	}

	@Test
	public void testProcessXSD() throws Exception
	{
		WSDLCheck w = new WSDLCheck(getFullPathFor("sample.wsdl"));
		Set<String> schemaLocations = new HashSet<>();
		schemaLocations.add(getFullPathFor("shiporder.xsd"));
		w.processXSD(schemaLocations, "shiporder", null);
		// second time, to test globalElems
		String result = w.processXSD(schemaLocations, "shiporder", null);
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
		WSDLCheck w = new WSDLCheck(getFullPathFor("sample.wsdl"));
		assertEquals("", w.processXSD(null, "root", null));
	}

	@Test
	public void testProcessXSDLocationsNull() throws Exception
	{
		WSDLCheck w = new WSDLCheck(getFullPathFor("sample.wsdl"));
		assertEquals("", w.processXSD(null, "root", new HashSet<Element>()));
	}

	@Test
	public void testProcessXSDElementsNull() throws Exception
	{
		WSDLCheck w = new WSDLCheck(getFullPathFor("sample.wsdl"));
		assertEquals("", w.processXSD(new HashSet<String>(), "root", null));
	}

	@Test
	public void testProcessXSDNullSchemaElement() throws Exception
	{
		WSDLCheck w = new WSDLCheck(getFullPathFor("sample.wsdl"));
		Set<Element> schemaElements = new HashSet<>();
		schemaElements.add(null);
		assertEquals("", w.processXSD(null, "root", schemaElements));
	}

	@Test
	public void testProcessXSDWrongRoot() throws Exception
	{
		WSDLCheck w = new WSDLCheck(getFullPathFor("sample.wsdl"));
		Set<String> schemaLocations = new HashSet<>();
		schemaLocations.add(getFullPathFor("shiporder.xsd"));
		assertEquals("", w.processXSD(schemaLocations, "testroot", null));
	}
}
