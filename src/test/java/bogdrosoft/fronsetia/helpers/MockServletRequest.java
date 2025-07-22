/*
 * MockServletRequest - a mock ServletRequest for tests.
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

package bogdrosoft.fronsetia.helpers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Locale;
import java.util.Map;

import jakarta.servlet.AsyncContext;
import jakarta.servlet.DispatcherType;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletConnection;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;

/**
 * MockServletRequest - a mock ServletRequest for tests.
 * @author Bogdan 'bogdro' Drozdowski, bogdro (at) users . sourceforge . net
 */
public class MockServletRequest implements ServletRequest
{
	private Hashtable<String, String> params;

	public MockServletRequest(Map<String, String> p)
	{
		params = new Hashtable<String, String>(p);
	}

	@Override
	public Object getAttribute(String arg0)
	{
		return null;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Enumeration getAttributeNames()
	{
		return null;
	}

	@Override
	public String getCharacterEncoding()
	{
		return null;
	}

	@Override
	public int getContentLength()
	{
		return 0;
	}

	@Override
	public String getContentType()
	{
		return null;
	}

	@Override
	public ServletInputStream getInputStream() throws IOException
	{
		return null;
	}

	@Override
	public String getLocalAddr()
	{
		return null;
	}

	@Override
	public String getLocalName()
	{
		return null;
	}

	@Override
	public int getLocalPort()
	{
		return 0;
	}

	@Override
	public Locale getLocale()
	{
		return null;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Enumeration getLocales()
	{
		return null;
	}

	@Override
	public String getParameter(String arg0)
	{
		return params.get(arg0);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Map getParameterMap()
	{
		return params;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Enumeration getParameterNames()
	{
		return params.keys();
	}

	@Override
	public String[] getParameterValues(String arg0)
	{
		return new String[] {params.get(arg0)};
	}

	@Override
	public String getProtocol()
	{
		return "http";
	}

	@Override
	public BufferedReader getReader() throws IOException
	{
		return null;
	}

	//@Override // Removed in Jakarta EE 10
	public String getRealPath(String arg0)
	{
		return null;
	}

	@Override
	public String getRemoteAddr()
	{
		return null;
	}

	@Override
	public String getRemoteHost()
	{
		return null;
	}

	@Override
	public int getRemotePort()
	{
		return 0;
	}

	@Override
	public RequestDispatcher getRequestDispatcher(String arg0)
	{
		return null;
	}

	@Override
	public String getScheme()
	{
		return null;
	}

	@Override
	public String getServerName()
	{
		return null;
	}

	@Override
	public int getServerPort()
	{
		return 0;
	}

	@Override
	public boolean isSecure()
	{
		return false;
	}

	@Override
	public void removeAttribute(String arg0)
	{
		/* not needed */
	}

	@Override
	public void setAttribute(String arg0, Object arg1)
	{
		/* not needed */
	}

	@Override
	public void setCharacterEncoding(String arg0) throws UnsupportedEncodingException
	{
		/* not needed */
	}

	@Override
	public long getContentLengthLong()
	{
		return 0;
	}

	@Override
	public ServletContext getServletContext()
	{
		return null;
	}

	@Override
	public AsyncContext startAsync() throws IllegalStateException
	{
		return null;
	}

	@Override
	public AsyncContext startAsync(ServletRequest servletRequest, ServletResponse servletResponse)
			throws IllegalStateException
	{
		return null;
	}

	@Override
	public boolean isAsyncStarted()
	{
		return false;
	}

	@Override
	public boolean isAsyncSupported()
	{
		return false;
	}

	@Override
	public AsyncContext getAsyncContext()
	{
		return null;
	}

	@Override
	public DispatcherType getDispatcherType()
	{
		return null;
	}

	@Override
	public String getRequestId()
	{
		return null;
	}

	@Override
	public String getProtocolRequestId()
	{
		return null;
	}

	@Override
	public ServletConnection getServletConnection()
	{
		return null;
	}
}
