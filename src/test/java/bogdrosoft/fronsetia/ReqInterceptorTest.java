/*
 * ReqInterceptorTest - a test for ReqInterceptor.
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
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.Header;
import org.apache.http.HeaderIterator;
import org.apache.http.HttpException;
import org.apache.http.message.BasicHttpEntityEnclosingRequest;
import org.junit.jupiter.api.Test;

import bogdrosoft.fronsetia.helpers.MockServletRequest;

/**
 * ReqInterceptorTest - a test for ReqInterceptor.
 * @author Bogdan 'bogdro' Drozdowski, bogdro (at) users . sourceforge . net
 */
public class ReqInterceptorTest
{
	private Map<String, String> prepareRequestParams()
	{
		Map<String, String> p = new HashMap<String, String>(1);
		p.put (RequestUtilities.REQ_PARAM_NAME_SOAP_PROLOGUE, "");
		p.put (RequestUtilities.REQ_PARAM_NAME_SOAP_HEADER, "");
		p.put (RequestUtilities.REQ_PARAM_NAME_SOAP_MIDDLE, "");
		p.put (RequestUtilities.REQ_PARAM_NAME_SOAP_BODY, "");
		p.put (RequestUtilities.REQ_PARAM_NAME_SOAP_EPILOGUE, "");
		p.put (RequestUtilities.REQ_PARAM_NAME_CONTENT_TYPE,
			RequestUtilities.DEFAULT_CONTENT_TYPE);
		p.put (RequestUtilities.REQ_PARAM_NAME_CHARSET, "UTF-8");
		p.put (RequestUtilities.REQ_PARAM_NAME_OP_URL, "http://localhost:1234/test");
		p.put (RequestUtilities.REQ_PARAM_NAME_PROTO_METHOD, "POST");
		return p;
	}

	private BasicHttpEntityEnclosingRequest prepareReqWithParams(Map<String, String> p)
	{
		BasicHttpEntityEnclosingRequest req =
			new BasicHttpEntityEnclosingRequest(
				p.get(RequestUtilities.REQ_PARAM_NAME_PROTO_METHOD),
				p.get(RequestUtilities.REQ_PARAM_NAME_OP_URL));
		req.addHeader("Content-Type", "application/soap+xml");
		req.addHeader("Content-Length", "0");
		req.addHeader("Host", "localhost");
		req.addHeader("Connection", "close");
		req.addHeader("User-Agent", "Unittest/1.0");
		return req;
	}

	private boolean checkHeader(HeaderIterator hi, String name, String value)
	{
		while (hi.hasNext ())
		{
			Header h = hi.nextHeader();
			if (name.equals(h.getName()))
			{
				return value.equals(h.getValue());
			}
		}
		return false;
	}

	@Test
	public void testProcess() throws HttpException, IOException
	{
		Map<String, String> p = prepareRequestParams();
		ReqInterceptor ri = new ReqInterceptor(new MockServletRequest(p));
		BasicHttpEntityEnclosingRequest req =
			new BasicHttpEntityEnclosingRequest(
				p.get(RequestUtilities.REQ_PARAM_NAME_PROTO_METHOD),
				p.get(RequestUtilities.REQ_PARAM_NAME_OP_URL));
		ri.process(req, null);
	}

	@Test
	public void testProcessSendNoHdr() throws HttpException, IOException
	{
		Map<String, String> p = prepareRequestParams();
		p.put (RequestUtilities.REQ_PARAM_NAME_SEND_NO_HEADERS, "1");
		ReqInterceptor ri = new ReqInterceptor(new MockServletRequest(p));
		BasicHttpEntityEnclosingRequest req = prepareReqWithParams(p);
		ri.process(req, null);
		assertFalse(checkHeader(ri.getFinalRequestHeaders(), "Content-Type", "application/soap+xml"));
		assertFalse(checkHeader(ri.getFinalRequestHeaders(), "Content-Length", "0"));
		assertFalse(checkHeader(ri.getFinalRequestHeaders(), "Host", "localhost"));
		assertFalse(checkHeader(ri.getFinalRequestHeaders(), "Connection", "close"));
		assertFalse(checkHeader(ri.getFinalRequestHeaders(), "User-Agent", "Unittest/1.0"));
	}

	@Test
	public void testProcessSendContentLen() throws HttpException, IOException
	{
		Map<String, String> p = prepareRequestParams();
		p.put (RequestUtilities.REQ_PARAM_NAME_SEND_HDR_CONTENT_LENGTH, "1");
		ReqInterceptor ri = new ReqInterceptor(new MockServletRequest(p));
		BasicHttpEntityEnclosingRequest req = prepareReqWithParams(p);
		ri.process(req, null);
		assertFalse(checkHeader(ri.getFinalRequestHeaders(), "Content-Type", "application/soap+xml"));
		assertTrue(checkHeader(ri.getFinalRequestHeaders(), "Content-Length", "0"));
		assertFalse(checkHeader(ri.getFinalRequestHeaders(), "Host", "localhost"));
		assertFalse(checkHeader(ri.getFinalRequestHeaders(), "Connection", "close"));
		assertFalse(checkHeader(ri.getFinalRequestHeaders(), "User-Agent", "Unittest/1.0"));
	}

	@Test
	public void testProcessSendHost() throws HttpException, IOException
	{
		Map<String, String> p = prepareRequestParams();
		p.put (RequestUtilities.REQ_PARAM_NAME_SEND_HDR_HOST, "1");
		ReqInterceptor ri = new ReqInterceptor(new MockServletRequest(p));
		BasicHttpEntityEnclosingRequest req = prepareReqWithParams(p);
		ri.process(req, null);
		assertFalse(checkHeader(ri.getFinalRequestHeaders(), "Content-Type", "application/soap+xml"));
		assertFalse(checkHeader(ri.getFinalRequestHeaders(), "Content-Length", "0"));
		assertTrue(checkHeader(ri.getFinalRequestHeaders(), "Host", "localhost"));
		assertFalse(checkHeader(ri.getFinalRequestHeaders(), "Connection", "close"));
		assertFalse(checkHeader(ri.getFinalRequestHeaders(), "User-Agent", "Unittest/1.0"));
	}

	@Test
	public void testProcessSendConnection() throws HttpException, IOException
	{
		Map<String, String> p = prepareRequestParams();
		p.put (RequestUtilities.REQ_PARAM_NAME_SEND_HDR_CONNECTION, "1");
		ReqInterceptor ri = new ReqInterceptor(new MockServletRequest(p));
		BasicHttpEntityEnclosingRequest req = prepareReqWithParams(p);
		ri.process(req, null);
		assertFalse(checkHeader(ri.getFinalRequestHeaders(), "Content-Type", "application/soap+xml"));
		assertFalse(checkHeader(ri.getFinalRequestHeaders(), "Content-Length", "0"));
		assertFalse(checkHeader(ri.getFinalRequestHeaders(), "Host", "localhost"));
		assertTrue(checkHeader(ri.getFinalRequestHeaders(), "Connection", "close"));
		assertFalse(checkHeader(ri.getFinalRequestHeaders(), "User-Agent", "Unittest/1.0"));
	}

	@Test
	public void testProcessSendUserAgent() throws HttpException, IOException
	{
		Map<String, String> p = prepareRequestParams();
		p.put (RequestUtilities.REQ_PARAM_NAME_SEND_HDR_USER_AGENT, "1");
		ReqInterceptor ri = new ReqInterceptor(new MockServletRequest(p));
		BasicHttpEntityEnclosingRequest req = prepareReqWithParams(p);
		ri.process(req, null);
		assertFalse(checkHeader(ri.getFinalRequestHeaders(), "Content-Type", "application/soap+xml"));
		assertFalse(checkHeader(ri.getFinalRequestHeaders(), "Content-Length", "0"));
		assertFalse(checkHeader(ri.getFinalRequestHeaders(), "Host", "localhost"));
		assertFalse(checkHeader(ri.getFinalRequestHeaders(), "Connection", "close"));
		assertTrue(checkHeader(ri.getFinalRequestHeaders(), "User-Agent", "Unittest/1.0"));
	}

	@Test
	public void testProcessSendContentType() throws HttpException, IOException
	{
		Map<String, String> p = prepareRequestParams();
		p.put (RequestUtilities.REQ_PARAM_NAME_SEND_HDR_CONTENT_TYPE, "1");
		ReqInterceptor ri = new ReqInterceptor(new MockServletRequest(p));
		BasicHttpEntityEnclosingRequest req = prepareReqWithParams(p);
		ri.process(req, null);
		assertTrue(checkHeader(ri.getFinalRequestHeaders(), "Content-Type", "application/soap+xml"));
		assertFalse(checkHeader(ri.getFinalRequestHeaders(), "Content-Length", "0"));
		assertFalse(checkHeader(ri.getFinalRequestHeaders(), "Host", "localhost"));
		assertFalse(checkHeader(ri.getFinalRequestHeaders(), "Connection", "close"));
		assertFalse(checkHeader(ri.getFinalRequestHeaders(), "User-Agent", "Unittest/1.0"));
	}

	@Test
	public void testProcessSendUserHeaders() throws HttpException, IOException
	{
		Map<String, String> p = prepareRequestParams();
		String header1Name = "X-Header1";
		String header2Name = "X-Header2";
		String header3Name = "X-Header3";
		String header1Value = "value1";
		p.put (RequestUtilities.REQ_PARAM_NAME_HTTP_HEADERS,
			header1Name + ": " + header1Value + "\n"
			+ header2Name + ": \n"
			+ header3Name + "\n\n \n");
		ReqInterceptor ri = new ReqInterceptor(new MockServletRequest(p));
		BasicHttpEntityEnclosingRequest req =
			new BasicHttpEntityEnclosingRequest(
				p.get(RequestUtilities.REQ_PARAM_NAME_PROTO_METHOD),
				p.get(RequestUtilities.REQ_PARAM_NAME_OP_URL));
		ri.process(req, null);
		HeaderIterator hi = ri.getFinalRequestHeaders();
		boolean gotHeader1 = false;
		boolean gotHeader2 = false;
		boolean gotHeader3 = false;
		while (hi.hasNext ())
		{
			Header h = hi.nextHeader();
			if (header1Name.equals(h.getName()))
			{
				gotHeader1 = true;
				assertEquals(header1Value, h.getValue());
			}
			if (header2Name.equals(h.getName()))
			{
				gotHeader2 = true;
				assertEquals("", h.getValue());
			}
			if (header3Name.equals(h.getName()))
			{
				gotHeader3 = true;
				assertEquals("", h.getValue());
			}
		}
		assertTrue(gotHeader1);
		assertTrue(gotHeader2);
		assertTrue(gotHeader3);
	}

	@Test
	public void testRemoveHeaderAllNull()
	{
		ReqInterceptor.removeHeader(null, null, null);
	}

	@Test
	public void testRemoveHeaderWithReq()
	{
		ReqInterceptor.removeHeader(
			new BasicHttpEntityEnclosingRequest("POST",
					"http://localhost:1234/test"),
			null, null);
	}

	@Test
	public void testRemoveHeaderWithList()
	{
		ReqInterceptor.removeHeader(null, new ArrayList<Header>(), null);
	}

	@Test
	public void testRemoveHeaderWithHeaderName()
	{
		ReqInterceptor.removeHeader(null, null, "Content-Type");
	}

	@Test
	public void testRemoveHeaderWithReqAndList()
	{
		ReqInterceptor.removeHeader(
			new BasicHttpEntityEnclosingRequest("POST",
					"http://localhost:1234/test"),
			new ArrayList<Header>(), null);
	}

	@Test
	public void testRemoveHeaderWithReqAndHeaderName()
	{
		ReqInterceptor.removeHeader(
			new BasicHttpEntityEnclosingRequest("POST",
					"http://localhost:1234/test"),
			null, "Content-Type");
	}

	@Test
	public void testRemoveHeaderWithListAndHeaderName()
	{
		ReqInterceptor.removeHeader(null, new ArrayList<Header>(), "Content-Type");
	}

	@Test
	public void testRemoveHeaderWithNullInList() throws Exception
	{
		Map<String, String> p = prepareRequestParams();
		ReqInterceptor ri = new ReqInterceptor(new MockServletRequest(p));
		BasicHttpEntityEnclosingRequest req =
			new BasicHttpEntityEnclosingRequest(
				p.get(RequestUtilities.REQ_PARAM_NAME_PROTO_METHOD),
				p.get(RequestUtilities.REQ_PARAM_NAME_OP_URL));
		ri.process(req, null);
		ArrayList<Header> ahe = new ArrayList<Header> (10);
		ahe.add(null);
		ReqInterceptor.removeHeader(req,
			ahe, "Content-Type");
		assertFalse(checkHeader(ri.getFinalRequestHeaders(),
			"Content-Type", "application/soap+xml"));
	}
}
