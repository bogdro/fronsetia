/*
 * MockServletRequest - a mock ServletRequest for tests.
 *
 * Copyright (C) 2023 Bogdan 'bogdro' Drozdowski, bogdro (at) users . sourceforge . net
 *
 * This file is part of Fronsetia (Free Online Service Testing Application),
 *  a web application that allows testing webservices.
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
import java.util.Locale;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletRequest;

/**
 * MockServletRequest - a mock ServletRequest for tests.
 * @author Bogdan 'bogdro' Drozdowski, bogdro (at) users . sourceforge . net
 */
public class MockServletRequest implements ServletRequest
{
	private Map<String, String> params;

	public MockServletRequest(Map<String, String> p)
	{
		params = p;
	}

	@Override
	public Object getAttribute(String arg0)
	{
		return null;
	}

	@SuppressWarnings({ "rawtypes" })
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

	@SuppressWarnings({ "rawtypes" })
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

	@SuppressWarnings({ "rawtypes" })
	@Override
	public Map getParameterMap()
	{
		return params;
	}

	@SuppressWarnings({ "rawtypes" })
	@Override
	public Enumeration getParameterNames()
	{
		return null;
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

	@Override
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
	}

	@Override
	public void setAttribute(String arg0, Object arg1)
	{
	}

	@Override
	public void setCharacterEncoding(String arg0) throws UnsupportedEncodingException
	{
	}

	/*
	@Override
	public long getContentLengthLong()
	{
		return 0;
	}

	@Override
	public javax.servlet.ServletContext getServletContext()
	{
		return null;
	}

	@Override
	public javax.servlet.AsyncContext startAsync() throws IllegalStateException
	{
		return null;
	}

	@Override
	public javax.servlet.AsyncContext startAsync(ServletRequest servletRequest, javax.servlet.ServletResponse servletResponse)
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
	public javax.servlet.AsyncContext getAsyncContext()
	{
		return null;
	}

	@Override
	public javax.servlet.DispatcherType getDispatcherType()
	{
		return null;
	}
	// */
}
