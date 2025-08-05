/*
 * OperationLauncherTest - a test for OperationLauncher.
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

package bogdrosoft.fronsetia;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.entity.StringEntity;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import bogdrosoft.fronsetia.helpers.MockServletRequest;
import bogdrosoft.fronsetia.soap.SoapInterpreter;
import jakarta.servlet.ServletRequest;

/**
 * OperationLauncherTest - a test for OperationLauncher.
 * @author Bogdan 'bogdro' Drozdowski, bogdro (at) users . sourceforge . net
 */
public class OperationLauncherTest
{
	private static StringEntity RESP;
	private static ResponseInterpreter INTERPRETER = new SoapInterpreter();

	@BeforeAll
	public static void setUp() throws Exception
	{
		RESP = new StringEntity(
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
	}

	private Map<String, String> prepareRequestParams()
	{
		Map<String, String> p = new HashMap<String, String>(1);
		p.put(RequestUtilities.REQ_PARAM_NAME_PAYLOAD_PROLOGUE,
			"<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\">\n"
			+ "<soapenv:Header>");
		p.put(RequestUtilities.REQ_PARAM_NAME_PAYLOAD_HEADER, "");
		p.put(RequestUtilities.REQ_PARAM_NAME_PAYLOAD_MIDDLE,
			"</soapenv:Header><soapenv:Body>");
		p.put(RequestUtilities.REQ_PARAM_NAME_PAYLOAD_BODY, "");
		p.put(RequestUtilities.REQ_PARAM_NAME_PAYLOAD_EPILOGUE,
			"</soapenv:Body></soapenv:Envelope>");
		p.put(RequestUtilities.REQ_PARAM_NAME_CONTENT_TYPE,
			RequestUtilities.DEFAULT_CONTENT_TYPE);
		p.put(RequestUtilities.REQ_PARAM_NAME_CHARSET, "UTF-8");
		p.put(RequestUtilities.REQ_PARAM_NAME_OP_URL, "http://localhost:1234/test");
		p.put(RequestUtilities.REQ_PARAM_NAME_PROTO_METHOD, "POST");
		return p;
	}

	@Test
	public void testPrepare() throws URISyntaxException
	{
		Map<String, String> p = prepareRequestParams();
		// Mockito doesn't work...
		ServletRequest m = new MockServletRequest(p);
		new OperationLauncher().prepare(m);
	}

	@Test
	public void testPrepareNullRequest() throws URISyntaxException
	{
		new OperationLauncher().prepare(null);
	}

	@Test
	public void testPrepareContentTypeEmpty() throws URISyntaxException
	{
		Map<String, String> p = prepareRequestParams();
		p.put(RequestUtilities.REQ_PARAM_NAME_CONTENT_TYPE, "");
		ServletRequest m = new MockServletRequest(p);
		new OperationLauncher().prepare(m);
	}

	@Test
	public void testPrepareNoContentType() throws URISyntaxException
	{
		Map<String, String> p = prepareRequestParams();
		p.remove(RequestUtilities.REQ_PARAM_NAME_CONTENT_TYPE);
		ServletRequest m = new MockServletRequest(p);
		new OperationLauncher().prepare(m);
	}

	@Test
	public void testPrepareWithHttpAuth() throws URISyntaxException
	{
		Map<String, String> p = prepareRequestParams();
		p.put(RequestUtilities.REQ_PARAM_NAME_HTTP_USER, "user");
		p.put(RequestUtilities.REQ_PARAM_NAME_HTTP_PASSWORD, "pass");
		ServletRequest m = new MockServletRequest(p);
		new OperationLauncher().prepare(m);
	}

	@Test
	public void testPrepareWithHttpAuthNoUser() throws URISyntaxException
	{
		Map<String, String> p = prepareRequestParams();
		p.put(RequestUtilities.REQ_PARAM_NAME_HTTP_PASSWORD, "pass");
		ServletRequest m = new MockServletRequest(p);
		new OperationLauncher().prepare(m);
	}

	@Test
	public void testPrepareWithHttpAuthNoPass() throws URISyntaxException
	{
		Map<String, String> p = prepareRequestParams();
		p.put(RequestUtilities.REQ_PARAM_NAME_HTTP_USER, "user");
		ServletRequest m = new MockServletRequest(p);
		new OperationLauncher().prepare(m);
	}

	@Test
	public void testPrepareWithHttpAuthNT() throws URISyntaxException
	{
		Map<String, String> p = prepareRequestParams();
		p.put(RequestUtilities.REQ_PARAM_NAME_HTTP_USER, "user");
		p.put(RequestUtilities.REQ_PARAM_NAME_HTTP_PASSWORD, "pass");
		p.put(RequestUtilities.REQ_PARAM_NAME_HTTP_NT_WORKSTATION, "ntwork");
		p.put(RequestUtilities.REQ_PARAM_NAME_HTTP_NT_DOMAIN, "ntdomain");
		ServletRequest m = new MockServletRequest(p);
		new OperationLauncher().prepare(m);
	}

	@Test
	public void testPrepareWithHttpAuthNTNoWorkstation() throws URISyntaxException
	{
		Map<String, String> p = prepareRequestParams();
		p.put(RequestUtilities.REQ_PARAM_NAME_HTTP_USER, "user");
		p.put(RequestUtilities.REQ_PARAM_NAME_HTTP_PASSWORD, "pass");
		p.put(RequestUtilities.REQ_PARAM_NAME_HTTP_NT_DOMAIN, "ntdomain");
		ServletRequest m = new MockServletRequest(p);
		new OperationLauncher().prepare(m);
	}

	@Test
	public void testPrepareWithHttpAuthNTNoDomain() throws URISyntaxException
	{
		Map<String, String> p = prepareRequestParams();
		p.put(RequestUtilities.REQ_PARAM_NAME_HTTP_USER, "user");
		p.put(RequestUtilities.REQ_PARAM_NAME_HTTP_PASSWORD, "pass");
		p.put(RequestUtilities.REQ_PARAM_NAME_HTTP_NT_WORKSTATION, "ntwork");
		ServletRequest m = new MockServletRequest(p);
		new OperationLauncher().prepare(m);
	}

	@Test
	public void testPrepareWithProxy() throws URISyntaxException
	{
		Map<String, String> p = prepareRequestParams();
		p.put(RequestUtilities.REQ_PARAM_NAME_PROXY_HOST, "proxy");
		p.put(RequestUtilities.REQ_PARAM_NAME_PROXY_PORT, "8080");
		p.put(RequestUtilities.REQ_PARAM_NAME_PROXY_USER, "proxyuser");
		p.put(RequestUtilities.REQ_PARAM_NAME_PROXY_PASSWORD, "proxypass");
		p.put(RequestUtilities.REQ_PARAM_NAME_PROXY_NT_WORKSTATION, "proxyworkstation");
		p.put(RequestUtilities.REQ_PARAM_NAME_PROXY_NT_DOMAIN, "proxydomain");
		ServletRequest m = new MockServletRequest(p);
		new OperationLauncher().prepare(m);
	}

	@Test
	public void testPrepareWithProxyNoHost() throws URISyntaxException
	{
		Map<String, String> p = prepareRequestParams();
		p.put(RequestUtilities.REQ_PARAM_NAME_PROXY_PORT, "8080");
		p.put(RequestUtilities.REQ_PARAM_NAME_PROXY_USER, "proxyuser");
		p.put(RequestUtilities.REQ_PARAM_NAME_PROXY_PASSWORD, "proxypass");
		p.put(RequestUtilities.REQ_PARAM_NAME_PROXY_NT_WORKSTATION, "proxyworkstation");
		p.put(RequestUtilities.REQ_PARAM_NAME_PROXY_NT_DOMAIN, "proxydomain");
		ServletRequest m = new MockServletRequest(p);
		new OperationLauncher().prepare(m);
	}

	@Test
	public void testPrepareWithProxyNoPort() throws URISyntaxException
	{
		Map<String, String> p = prepareRequestParams();
		p.put(RequestUtilities.REQ_PARAM_NAME_PROXY_HOST, "proxy");
		p.put(RequestUtilities.REQ_PARAM_NAME_PROXY_USER, "proxyuser");
		p.put(RequestUtilities.REQ_PARAM_NAME_PROXY_PASSWORD, "proxypass");
		p.put(RequestUtilities.REQ_PARAM_NAME_PROXY_NT_WORKSTATION, "proxyworkstation");
		p.put(RequestUtilities.REQ_PARAM_NAME_PROXY_NT_DOMAIN, "proxydomain");
		ServletRequest m = new MockServletRequest(p);
		new OperationLauncher().prepare(m);
	}

	@Test
	public void testPrepareWithProxyNoUser() throws URISyntaxException
	{
		Map<String, String> p = prepareRequestParams();
		p.put(RequestUtilities.REQ_PARAM_NAME_PROXY_HOST, "proxy");
		p.put(RequestUtilities.REQ_PARAM_NAME_PROXY_PORT, "8080");
		p.put(RequestUtilities.REQ_PARAM_NAME_PROXY_PASSWORD, "proxypass");
		p.put(RequestUtilities.REQ_PARAM_NAME_PROXY_NT_WORKSTATION, "proxyworkstation");
		p.put(RequestUtilities.REQ_PARAM_NAME_PROXY_NT_DOMAIN, "proxydomain");
		ServletRequest m = new MockServletRequest(p);
		new OperationLauncher().prepare(m);
	}

	@Test
	public void testPrepareWithProxyNoPassword() throws URISyntaxException
	{
		Map<String, String> p = prepareRequestParams();
		p.put(RequestUtilities.REQ_PARAM_NAME_PROXY_HOST, "proxy");
		p.put(RequestUtilities.REQ_PARAM_NAME_PROXY_PORT, "8080");
		p.put(RequestUtilities.REQ_PARAM_NAME_PROXY_USER, "proxyuser");
		p.put(RequestUtilities.REQ_PARAM_NAME_PROXY_NT_WORKSTATION, "proxyworkstation");
		p.put(RequestUtilities.REQ_PARAM_NAME_PROXY_NT_DOMAIN, "proxydomain");
		ServletRequest m = new MockServletRequest(p);
		new OperationLauncher().prepare(m);
	}

	@Test
	public void testPrepareWithProxyNoNTWorkstation() throws URISyntaxException
	{
		Map<String, String> p = prepareRequestParams();
		p.put(RequestUtilities.REQ_PARAM_NAME_PROXY_HOST, "proxy");
		p.put(RequestUtilities.REQ_PARAM_NAME_PROXY_PORT, "8080");
		p.put(RequestUtilities.REQ_PARAM_NAME_PROXY_USER, "proxyuser");
		p.put(RequestUtilities.REQ_PARAM_NAME_PROXY_PASSWORD, "proxypass");
		p.put(RequestUtilities.REQ_PARAM_NAME_PROXY_NT_DOMAIN, "proxydomain");
		ServletRequest m = new MockServletRequest(p);
		new OperationLauncher().prepare(m);
	}

	@Test
	public void testPrepareWithProxyNoNTDomain() throws URISyntaxException
	{
		Map<String, String> p = prepareRequestParams();
		p.put(RequestUtilities.REQ_PARAM_NAME_PROXY_HOST, "proxy");
		p.put(RequestUtilities.REQ_PARAM_NAME_PROXY_PORT, "8080");
		p.put(RequestUtilities.REQ_PARAM_NAME_PROXY_USER, "proxyuser");
		p.put(RequestUtilities.REQ_PARAM_NAME_PROXY_PASSWORD, "proxypass");
		p.put(RequestUtilities.REQ_PARAM_NAME_PROXY_NT_WORKSTATION, "proxyworkstation");
		ServletRequest m = new MockServletRequest(p);
		new OperationLauncher().prepare(m);
	}

	@Test
	public void testPrepareWithAllSSL() throws URISyntaxException
	{
		Map<String, String> p = prepareRequestParams();
		p.put(RequestUtilities.REQ_PARAM_NAME_ACCEPT_ALL_SSL, "1");
		ServletRequest m = new MockServletRequest(p);
		new OperationLauncher().prepare(m);
	}

	@Test
	public void testPrepareNoMethod() throws URISyntaxException
	{
		Map<String, String> p = prepareRequestParams();
		p.remove(RequestUtilities.REQ_PARAM_NAME_PROTO_METHOD);
		ServletRequest m = new MockServletRequest(p);
		new OperationLauncher().prepare(m);
	}

	@Test
	public void testPrepareMethodFromInput() throws URISyntaxException
	{
		Map<String, String> p = prepareRequestParams();
		p.put(RequestUtilities.REQ_PARAM_NAME_PROTO_METHOD_INPUT, "POST");
		ServletRequest m = new MockServletRequest(p);
		new OperationLauncher().prepare(m);
	}

	@Test
	public void testPrepareWithProtocol() throws URISyntaxException
	{
		Map<String, String> p = prepareRequestParams();
		p.put(RequestUtilities.REQ_PARAM_NAME_PROTO_NAME, "HTTP");
		p.put(RequestUtilities.REQ_PARAM_NAME_PROTO_MAJOR, "1");
		p.put(RequestUtilities.REQ_PARAM_NAME_PROTO_MINOR, "0");
		ServletRequest m = new MockServletRequest(p);
		new OperationLauncher().prepare(m);
	}

	@Test
	public void testPrepareWithProtocolNoName() throws URISyntaxException
	{
		Map<String, String> p = prepareRequestParams();
		p.put(RequestUtilities.REQ_PARAM_NAME_PROTO_MAJOR, "1");
		p.put(RequestUtilities.REQ_PARAM_NAME_PROTO_MINOR, "0");
		ServletRequest m = new MockServletRequest(p);
		new OperationLauncher().prepare(m);
	}

	@Test
	public void testPrepareWithProtocolNoMajor() throws URISyntaxException
	{
		Map<String, String> p = prepareRequestParams();
		p.put(RequestUtilities.REQ_PARAM_NAME_PROTO_NAME, "HTTP");
		p.put(RequestUtilities.REQ_PARAM_NAME_PROTO_MINOR, "0");
		ServletRequest m = new MockServletRequest(p);
		new OperationLauncher().prepare(m);
	}

	@Test
	public void testPrepareWithProtocolNoMinor() throws URISyntaxException
	{
		Map<String, String> p = prepareRequestParams();
		p.put(RequestUtilities.REQ_PARAM_NAME_PROTO_NAME, "HTTP");
		p.put(RequestUtilities.REQ_PARAM_NAME_PROTO_MAJOR, "1");
		ServletRequest m = new MockServletRequest(p);
		new OperationLauncher().prepare(m);
	}

	/*
	@Test
	public void testPerform()
	{
		fail("Not yet implemented");
	}
	*/

	@Test
	public void testProcessEntity() throws Exception
	{
		Map<String, String> p = prepareRequestParams();
		// Mockito doesn't work...
		ServletRequest m = new MockServletRequest(p);
		OperationLauncher ol = new OperationLauncher();
		ol.prepare(m);
		ol.processEntity(RESP, m, INTERPRETER);
	}

	@Test
	public void testProcessEntityWithSplit() throws Exception
	{
		Map<String, String> p = prepareRequestParams();
		p.put(RequestUtilities.REQ_PARAM_NAME_SPLIT_RESP, "1");
		// Mockito doesn't work...
		ServletRequest m = new MockServletRequest(p);
		OperationLauncher ol = new OperationLauncher();
		ol.prepare(m);
		ol.processEntity(RESP, m, INTERPRETER);
	}


	@Test
	public void testProcessEntityEmpty() throws Exception
	{
		Map<String, String> p = prepareRequestParams();
		// Mockito doesn't work...
		ServletRequest m = new MockServletRequest(p);
		OperationLauncher ol = new OperationLauncher();
		ol.prepare(m);
		ol.processEntity(new StringEntity(""), m, INTERPRETER);
	}

	@Test
	public void testProcessEntityNoCharset() throws Exception
	{
		Map<String, String> p = prepareRequestParams();
		p.remove(RequestUtilities.REQ_PARAM_NAME_CHARSET);
		// Mockito doesn't work...
		ServletRequest m = new MockServletRequest(p);
		OperationLauncher ol = new OperationLauncher();
		ol.prepare(m);
		ol.processEntity(RESP, m, INTERPRETER);
	}

	@Test
	public void testProcessEntityEmptyCharset() throws Exception
	{
		Map<String, String> p = prepareRequestParams();
		p.put(RequestUtilities.REQ_PARAM_NAME_CHARSET, "");
		// Mockito doesn't work...
		ServletRequest m = new MockServletRequest(p);
		OperationLauncher ol = new OperationLauncher();
		ol.prepare(m);
		ol.processEntity(RESP, m, INTERPRETER);
	}

	@Test
	public void testProcessEntityInvalidCharset() throws Exception
	{
		Map<String, String> p = prepareRequestParams();
		p.put(RequestUtilities.REQ_PARAM_NAME_CHARSET, "fail");
		// Mockito doesn't work...
		ServletRequest m = new MockServletRequest(p);
		OperationLauncher ol = new OperationLauncher();
		ol.prepare(m);
		ol.processEntity(RESP, m, INTERPRETER);
	}

	@Test
	public void testProcessEntityNoContentType() throws Exception
	{
		Map<String, String> p = prepareRequestParams();
		p.remove(RequestUtilities.REQ_PARAM_NAME_CHARSET);
		// Mockito doesn't work...
		ServletRequest m = new MockServletRequest(p);
		OperationLauncher ol = new OperationLauncher();
		ol.prepare(m);
		StringEntity se = new StringEntity(RESP.toString());
		se.setContentType((String)null);
		ol.processEntity(se, m, INTERPRETER);
	}

	@Test
	public void testProcessEntityContentTypeInvalidCharset() throws Exception
	{
		Map<String, String> p = prepareRequestParams();
		p.remove(RequestUtilities.REQ_PARAM_NAME_CHARSET);
		// Mockito doesn't work...
		ServletRequest m = new MockServletRequest(p);
		OperationLauncher ol = new OperationLauncher();
		ol.prepare(m);
		StringEntity se = new StringEntity(RESP.toString());
		se.setContentType("application/soap+xml; charset=fail");
		ol.processEntity(se, m, INTERPRETER);
	}

	@Test
	public void testProcessEntityContentTypeNoCharset() throws Exception
	{
		Map<String, String> p = prepareRequestParams();
		p.remove(RequestUtilities.REQ_PARAM_NAME_CHARSET);
		// Mockito doesn't work...
		ServletRequest m = new MockServletRequest(p);
		OperationLauncher ol = new OperationLauncher();
		ol.prepare(m);
		StringEntity se = new StringEntity(RESP.toString());
		se.setContentType("application/soap+xml");
		ol.processEntity(se, m, INTERPRETER);
	}

	@Test
	public void testGetReqHeaders()
	{
		assertNull(new OperationLauncher().getReqHeaders());
	}

	@Test
	public void testGetRespHeaders()
	{
		assertNull(new OperationLauncher().getRespHeaders());
	}

	@Test
	public void testGetStatusLine()
	{
		assertEquals("", new OperationLauncher().getStatusLine());
	}

	@Test
	public void testGetStatusCode()
	{
		assertEquals(-1, new OperationLauncher().getStatusCode());
	}

	@Test
	public void testGetResponseBody()
	{
		assertNull(new OperationLauncher().getResponseBody());
	}
}
