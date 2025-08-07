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
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.w3c.dom.Element;

import bogdrosoft.fronsetia.EndpointType;
import bogdrosoft.fronsetia.helpers.TestHelper;

/**
 * SoapEndpointParserTest - a test for SoapEndpointParser.
 * @author Bogdan 'bogdro' Drozdowski, bogdro (at) users . sourceforge . net
 */
public class SoapEndpointParserTest
{
	private static final String OPER_NAME = "getPriceList";
	private static final String OPER_URL = "http://localhost:8080/soap/servlet/rpcrouter";

	private SoapEndpointParser checkFile(String fileName)
	{
		SoapEndpointParser p = new SoapEndpointParser();
		p.parse(TestHelper.getFullPathFor(fileName));
		Set<String> operations = p.getListOfOperations();
		assertTrue(operations.contains(OPER_NAME));
		assertEquals(OPER_URL, p.getUrlForOperation(OPER_NAME));
		return p;
	}

	@Test
	public void testBasic()
	{
		SoapEndpointParser p = checkFile("sample.wsdl");
		assertNull(p.getParsingException());
	}

	@Test
	public void testV12()
	{
		checkFile("sample-v1.2.wsdl");
	}

	@Test
	public void testNoServices()
	{
		SoapEndpointParser p = new SoapEndpointParser();
		p.parse(TestHelper.getFullPathFor("sample-nosrv.wsdl"));
		assertNotNull(p.getListOfOperations());
	}

	@Test
	public void testNoTypes()
	{
		checkFile("sample-notypes.wsdl");
	}

	@Test
	public void testNoImports()
	{
		checkFile("sample-noimports.wsdl");
	}

	@Test
	public void testNoIncludes()
	{
		checkFile("sample-noincludes.wsdl");
	}

	@Test
	public void testNoPorts()
	{
		SoapEndpointParser p = new SoapEndpointParser();
		p.parse(TestHelper.getFullPathFor("sample-noports.wsdl"));
		assertNotNull(p.getListOfOperations());
	}

	@Test
	public void testNoOperations()
	{
		SoapEndpointParser p = new SoapEndpointParser();
		p.parse(TestHelper.getFullPathFor("sample-nooperations.wsdl"));
		assertNotNull(p.getListOfOperations());
	}

	@Test
	public void testNoOperInput()
	{
		SoapEndpointParser p = new SoapEndpointParser();
		p.parse(TestHelper.getFullPathFor("sample-noinput.wsdl"));
		assertNotNull(p.getListOfOperations());
	}

	@Test
	public void testNoDefinitions()
	{
		SoapEndpointParser p = new SoapEndpointParser();
		p.parse(TestHelper.getFullPathFor("sample-nodef.wsdl"));
		assertNotNull(p.getListOfOperations());
	}

	@Test
	public void testEmptyTypes()
	{
		SoapEndpointParser p = new SoapEndpointParser();
		p.parse(TestHelper.getFullPathFor("sample-empty-types.wsdl"));
		assertNotNull(p.getListOfOperations());
	}

	@Test
	public void testPortNoBinding()
	{
		SoapEndpointParser p = new SoapEndpointParser();
		p.parse(TestHelper.getFullPathFor("sample-no-bind.wsdl"));
		assertNotNull(p.getListOfOperations());
	}

	@Test
	public void testBindingNoType()
	{
		SoapEndpointParser p = new SoapEndpointParser();
		p.parse(TestHelper.getFullPathFor("sample-bind-no-type.wsdl"));
		assertNotNull(p.getListOfOperations());
	}

	@Test
	public void testOperationNoName()
	{
		SoapEndpointParser p = new SoapEndpointParser();
		p.parse(TestHelper.getFullPathFor("sample-oper-no-name.wsdl"));
		assertNotNull(p.getListOfOperations());
	}

	@Test
	public void testInputNoMsg()
	{
		SoapEndpointParser p = new SoapEndpointParser();
		p.parse(TestHelper.getFullPathFor("sample-in-no-msg.wsdl"));
		assertNotNull(p.getListOfOperations());
	}

	@Test
	public void testMsgNoQName()
	{
		SoapEndpointParser p = new SoapEndpointParser();
		p.parse(TestHelper.getFullPathFor("sample-msg-no-qname.wsdl"));
		assertNotNull(p.getListOfOperations());
	}

	@Test
	public void testInMsgNoQName()
	{
		SoapEndpointParser p = new SoapEndpointParser();
		p.parse(TestHelper.getFullPathFor("sample-in-msg-no-name.wsdl"));
		assertNotNull(p.getListOfOperations());
	}

	@Test
	public void testInMsgNoLocalName()
	{
		SoapEndpointParser p = new SoapEndpointParser();
		p.parse(TestHelper.getFullPathFor("sample-in-msg-no-localname.wsdl"));
		assertNotNull(p.getListOfOperations());
	}

	@Test
	public void testInMsgPartNoType()
	{
		SoapEndpointParser p = new SoapEndpointParser();
		p.parse(TestHelper.getFullPathFor("sample-msg-part-no-type.wsdl"));
		assertNotNull(p.getListOfOperations());
	}

	@Test
	public void testUnknownSoapVersion()
	{
		SoapEndpointParser p = new SoapEndpointParser();
		p.parse(TestHelper.getFullPathFor("sample-v1.3.wsdl"));
		assertNotNull(p.getListOfOperations());
	}

	@Test
	public void testProcessXSD()
	{
		SoapEndpointParser p = new SoapEndpointParser();
		p.parse(TestHelper.getFullPathFor("sample.wsdl"));
		Set<String> schemaLocations = new HashSet<>();
		schemaLocations.add(TestHelper.getFullPathFor("shiporder.xsd"));
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
	public void testProcessXSDBothNull()
	{
		SoapEndpointParser p = new SoapEndpointParser();
		p.parse(TestHelper.getFullPathFor("sample.wsdl"));
		assertEquals("", p.processXSD(null, "root", null));
	}

	@Test
	public void testProcessXSDLocationsNull()
	{
		SoapEndpointParser p = new SoapEndpointParser();
		p.parse(TestHelper.getFullPathFor("sample.wsdl"));
		assertEquals("", p.processXSD(null, "root", new HashSet<Element>()));
	}

	@Test
	public void testProcessXSDElementsNull()
	{
		SoapEndpointParser p = new SoapEndpointParser();
		p.parse(TestHelper.getFullPathFor("sample.wsdl"));
		assertEquals("", p.processXSD(new HashSet<String>(), "root", null));
	}

	@Test
	public void testProcessXSDNullSchemaElement()
	{
		SoapEndpointParser p = new SoapEndpointParser();
		p.parse(TestHelper.getFullPathFor("sample.wsdl"));
		Set<Element> schemaElements = new HashSet<>();
		schemaElements.add(null);
		assertEquals("", p.processXSD(null, "root", schemaElements));
	}

	@Test
	public void testProcessXSDWrongRoot()
	{
		SoapEndpointParser p = new SoapEndpointParser();
		p.parse(TestHelper.getFullPathFor("sample.wsdl"));
		Set<String> schemaLocations = new HashSet<>();
		schemaLocations.add(TestHelper.getFullPathFor("shiporder.xsd"));
		assertEquals("", p.processXSD(schemaLocations, "testroot", null));
	}

	@Test
	public void testProcessXSDNoRoot()
	{
		SoapEndpointParser p = new SoapEndpointParser();
		p.parse(TestHelper.getFullPathFor("sample.wsdl"));
		Set<String> schemaLocations = new HashSet<>();
		schemaLocations.add(TestHelper.getFullPathFor("shiporder.xsd"));
		assertEquals("", p.processXSD(schemaLocations, null, null));
	}

	@Test
	public void testListOfTransportHeadersForOperation()
	{
		String operName = "TestOper";
		SoapEndpointParser p = new SoapEndpointParser();
		List<String> headers = p.getListOfTransportHeadersForOperation(operName);
		assertNotNull(headers);
		assertTrue(headers.contains("SOAPAction: \"/" + operName + "\""));
	}

	@Test
	public void testDefaultPayloadProloguesForOperation()
	{
		String operName = "TestOper";
		SoapEndpointParser p = new SoapEndpointParser();
		List<String> payloadPrologues = p.getDefaultPayloadProloguesForOperation(operName);
		assertNotNull(payloadPrologues);
		assertTrue(payloadPrologues.contains(
				"<soapenv:Envelope xmlns:soapenv=\"http://www.w3.org/2003/05/soap-envelope\">\n<soapenv:Header>"
		));
	}

	@Test
	public void testDefaultPayloadHeaderForOperation()
	{
		String operName = "TestOper";
		SoapEndpointParser p = new SoapEndpointParser();
		String payloadHeader = p.getDefaultPayloadHeaderForOperation(operName);
		assertNotNull(payloadHeader);
	}

	@Test
	public void testDefaultPayloadMiddleForOperation()
	{
		String operName = "TestOper";
		SoapEndpointParser p = new SoapEndpointParser();
		String payloadHeader = p.getDefaultPayloadMiddleForOperation(operName);
		assertTrue(payloadHeader.contains("</soapenv:Header>"));
		assertTrue(payloadHeader.contains("<soapenv:Body>"));
	}

	@Test
	public void testDefaultPayloadForOperation()
	{
		String operName = "TestOper";
		SoapEndpointParser p = new SoapEndpointParser();
		String payloadHeader = p.getDefaultPayloadForOperation(operName);
		assertTrue(payloadHeader.contains("<" + operName + ">"));
		assertTrue(payloadHeader.contains("</" + operName + ">"));
	}

	@Test
	public void testDefaultPayloadForOperationWithParse()
	{
		String operName = "getPriceList";
		SoapEndpointParser p = new SoapEndpointParser();
		p.parse(TestHelper.getFullPathFor("sample.wsdl"));
		String payloadHeader = p.getDefaultPayloadForOperation(operName);
		assertTrue(payloadHeader.contains("<" + operName + ">"));
		assertTrue(payloadHeader.contains("</" + operName + ">"));
	}

	@Test
	public void testDefaultPayloadEpilogueForOperation()
	{
		String operName = "TestOper";
		SoapEndpointParser p = new SoapEndpointParser();
		String payloadHeader = p.getDefaultPayloadEpilogueForOperation(operName);
		assertTrue(payloadHeader.contains("</soapenv:Body>"));
		assertTrue(payloadHeader.contains("</soapenv:Envelope>"));
	}

	@Test
	public void testDefaultContentType()
	{
		SoapEndpointParser p = new SoapEndpointParser();
		assertEquals("application/soap+xml", p.getDefaultContentType());
	}

	@Test
	public void testType()
	{
		SoapEndpointParser p = new SoapEndpointParser();
		assertEquals(EndpointType.SOAP, p.getType());
	}

	@Test
	public void testAddSchemaReferenceNonList()
	{
		Set<String> schemaLocations = new HashSet<String>();
		SoapEndpointParser p = new SoapEndpointParser();
		p.addSchemaReference(schemaLocations, "test");
		assertTrue(schemaLocations.isEmpty());
	}

	@Test
	public void testAddSchemaReferenceNullList()
	{
		Set<String> schemaLocations = new HashSet<String>();
		SoapEndpointParser p = new SoapEndpointParser();
		p.addSchemaReference(schemaLocations, null);
		assertTrue(schemaLocations.isEmpty());
	}

	@Test
	public void testAddSchemaReferenceBadTypeList()
	{
		Set<String> schemaLocations = new HashSet<String>();
		SoapEndpointParser p = new SoapEndpointParser();
		p.addSchemaReference(schemaLocations, Collections.singletonList("test"));
		assertTrue(schemaLocations.isEmpty());
	}
}
