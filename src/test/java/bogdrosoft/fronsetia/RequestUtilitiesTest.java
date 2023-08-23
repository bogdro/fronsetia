/*
 * RequestUtilitiesTest - a test for RequestUtilities.
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
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import bogdrosoft.fronsetia.helpers.MockServletRequest;
import jakarta.servlet.ServletRequest;

/**
 * RequestUtilitiesTest - a test for RequestUtilities.
 * @author Bogdan 'bogdro' Drozdowski, bogdro (at) users . sourceforge . net
 */
public class RequestUtilitiesTest
{
	private static Map<String, String> p;
	private static ServletRequest m;
	private static final String PARAM_NAME = "p_name";
	private static final String PARAM_VALUE = "p_value";
	private static final String EXCEPTION_MSG = "some-exception-message";

	@BeforeAll
	public static void setUp() throws Exception
	{
		p = new HashMap<String, String>(1);
		p.put(PARAM_NAME, PARAM_VALUE);
		// Mockito doesn't work...
		m = new MockServletRequest(p);
		// just for coverage...
		m.getAttribute("");
		m.getAttributeNames();
		m.getCharacterEncoding();
		m.getContentLength();
		m.getContentType();
		m.getInputStream();
		m.getLocalAddr();
		m.getLocalName();
		m.getLocalPort();
		m.getLocale();
		m.getLocales();
		m.getParameterMap();
		m.getParameterNames();
		m.getParameterValues(PARAM_NAME);
		m.getProtocol();
		m.getReader();
		((MockServletRequest)m).getRealPath(""); // Removed in Jakarta EE 10
		m.getRemoteAddr();
		m.getRemoteHost();
		m.getRemotePort();
		m.getRequestDispatcher("");
		m.getScheme();
		m.getServerName();
		m.getServerPort();
		m.isSecure();
		m.removeAttribute("");
		m.setAttribute(PARAM_VALUE, PARAM_NAME);
		m.setCharacterEncoding("");
		m.getContentLengthLong();
		m.getServletContext();
		m.startAsync();
		m.startAsync(m, null);
		m.isAsyncStarted();
		m.isAsyncSupported();
		m.getAsyncContext();
		m.getDispatcherType();
		m.getRequestId();
		m.getProtocolRequestId();
		m.getServletConnection();
	}

	@Test
	public void testGetParameter()
	{
		assertEquals(PARAM_VALUE, RequestUtilities.getParameter(m, PARAM_NAME));
	}

	@Test
	public void testGetParameterNullReq()
	{
		assertEquals("", RequestUtilities.getParameter(null, PARAM_NAME));
	}

	@Test
	public void testGetParameterNullName()
	{
		assertEquals("", RequestUtilities.getParameter(m, null));
	}

	@Test
	public void testGetParameterNotExist()
	{
		assertEquals("", RequestUtilities.getParameter(m, PARAM_NAME + "2"));
	}

	@Test
	public void testMakeHTMLSafe()
	{
		assertEquals("a&lt;b&gt;c&amp;",
				RequestUtilities.makeHTMLSafe("a<b>c&"));
	}

	@Test
	public void testMakeHTMLSafeNull()
	{
		assertEquals("", RequestUtilities.makeHTMLSafe(null));
	}

	@Test
	public void testSplitByTags()
	{
		assertEquals("a&gt;\nb>\n",
				RequestUtilities.splitByTags("a&gt;b>"));
	}

	@Test
	public void testSplitByTagsNull()
	{
		assertEquals("", RequestUtilities.splitByTags(null));
	}

	@Test
	public void testHasParameter()
	{
		assertTrue(RequestUtilities.hasParameter(m, PARAM_NAME));
	}

	@Test
	public void testHasParameterNullReq()
	{
		assertFalse(RequestUtilities.hasParameter(null, PARAM_NAME));
	}

	@Test
	public void testHasParameterNullName()
	{
		assertFalse(RequestUtilities.hasParameter(m, null));
	}

	@Test
	public void testHasParameterNotExist()
	{
		assertFalse(RequestUtilities.hasParameter(m, PARAM_NAME + "2"));
	}

	@Test
	public void testPrintException() throws IOException
	{
		Exception ex = new Exception(EXCEPTION_MSG);
		StringWriter sw = new StringWriter();
		RequestUtilities.printException(ex, sw);
		assertTrue(sw.toString().contains(EXCEPTION_MSG));
	}

	@Test
	public void testPrintExceptionNullEx() throws IOException
	{
		StringWriter sw = new StringWriter();
		RequestUtilities.printException(null, sw);
		assertFalse(sw.toString().contains(EXCEPTION_MSG));
	}

	@Test
	public void testPrintExceptionNullWriter() throws IOException
	{
		Exception ex = new Exception(EXCEPTION_MSG);
		RequestUtilities.printException(ex, null);
	}

	@Test
	public void testPrintExceptionNullWriterNullEx() throws IOException
	{
		RequestUtilities.printException(null, null);
	}
}
