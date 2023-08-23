/*
 * WSDLCheckTest - a test for WSDLCheck.
 *
 * Copyright (C) 2023 Bogdan 'bogdro' Drozdowski, bogdro (at) users . sourceforge . net
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
import java.util.Map;

import org.junit.jupiter.api.Test;

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
}
