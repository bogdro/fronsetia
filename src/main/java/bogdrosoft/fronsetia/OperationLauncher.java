/*
 * OperationLauncher - a class that calls endpoint operations.
 *
 * Copyright (C) 2011-2025 Bogdan 'bogdro' Drozdowski, bogdro (at) users . sourceforge . net
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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Locale;

import org.apache.http.Consts;
import org.apache.http.HeaderElement;
import org.apache.http.HeaderIterator;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.ProtocolVersion;
import org.apache.http.StatusLine;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.NTCredentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.DefaultProxyRoutePlanner;
import org.apache.http.message.BasicHttpEntityEnclosingRequest;
import org.apache.http.message.BasicHttpRequest;

import jakarta.servlet.ServletRequest;

/**
 * OperationLauncher - a class that calls endpoint operations.
 * @author Bogdan 'bogdro' Drozdowski, bogdro (at) users . sourceforge . net
 */
public class OperationLauncher
{
	private static final String PARAM_NAME_CHARSET = "charset";

	private BasicHttpEntityEnclosingRequest hreq;
	private HttpResponse hr;
	private HeaderIterator reqHeaders;
	private ReqInterceptor interceptor;
	private StatusLine respStatus;
	private HttpClient hc;
	private HttpHost host;
	private String responseBody;
	private String respCharset;

	/**
	 * Prepare data for the operation specified by the request.
	 * @param request the request to get the parameters from.
	 */
	public void prepare (ServletRequest request)
		throws URISyntaxException
	{
		if ( request == null )
		{
			return;
		}
		//prepare data
		String payloadPrologue = request.getParameter(RequestUtilities.REQ_PARAM_NAME_PAYLOAD_PROLOGUE);
		String payloadHeader = request.getParameter(RequestUtilities.REQ_PARAM_NAME_PAYLOAD_HEADER);
		String payloadMiddle = request.getParameter(RequestUtilities.REQ_PARAM_NAME_PAYLOAD_MIDDLE);
		String payloadXML = request.getParameter(RequestUtilities.REQ_PARAM_NAME_PAYLOAD_BODY);
		String payloadEpilogue = request.getParameter(RequestUtilities.REQ_PARAM_NAME_PAYLOAD_EPILOGUE);
		String payloadCType = request.getParameter(RequestUtilities.REQ_PARAM_NAME_CONTENT_TYPE);
		if ( payloadCType == null || payloadCType.isEmpty() )
		{
			payloadCType = RequestUtilities.DEFAULT_CONTENT_TYPE;
		}
		respCharset = request.getParameter(RequestUtilities.REQ_PARAM_NAME_CHARSET);
		String protoName = RequestUtilities.getParameter(request,
			RequestUtilities.REQ_PARAM_NAME_PROTO_NAME);
		String protoMajorVer = RequestUtilities.getParameter(request,
			RequestUtilities.REQ_PARAM_NAME_PROTO_MAJOR);
		String protoMinorVer = RequestUtilities.getParameter(request,
			RequestUtilities.REQ_PARAM_NAME_PROTO_MINOR);
		String httpAuthUser = RequestUtilities.getParameter(request,
			RequestUtilities.REQ_PARAM_NAME_HTTP_USER);
		String httpAuthPass = RequestUtilities.getParameter(request,
			RequestUtilities.REQ_PARAM_NAME_HTTP_PASSWORD);
		String httpAuthNTstation = RequestUtilities.getParameter(request,
			RequestUtilities.REQ_PARAM_NAME_HTTP_NT_WORKSTATION);
		String httpAuthNTdomain = RequestUtilities.getParameter(request,
			RequestUtilities.REQ_PARAM_NAME_HTTP_NT_DOMAIN);
		CredentialsProvider cp = new BasicCredentialsProvider();
		String opURL = RequestUtilities.getParameter(request,
			RequestUtilities.REQ_PARAM_NAME_OP_URL);
		URI operUri = new URI(opURL);
		if ( (! httpAuthUser.isEmpty()) && (! httpAuthPass.isEmpty()) )
		{
			Credentials cred;
			if ( (! httpAuthNTstation.isEmpty()) && (! httpAuthNTdomain.isEmpty()) )
			{
				// NT credentials provided - use them
				cred = new NTCredentials(httpAuthUser, httpAuthPass,
					httpAuthNTstation, httpAuthNTdomain);
			}
			else
			{
				// NT credentials NOT provided - use basic credentials
				cred = new UsernamePasswordCredentials (httpAuthUser, httpAuthPass);
			}
			AuthScope as = new AuthScope(operUri.getHost(), operUri.getPort());
			cp.setCredentials(as, cred);
		}
		String httpProxy = RequestUtilities.getParameter(request,
			RequestUtilities.REQ_PARAM_NAME_PROXY_HOST);
		String httpProxyPort = RequestUtilities.getParameter(request,
			RequestUtilities.REQ_PARAM_NAME_PROXY_PORT);
		String proxyAuthUser = RequestUtilities.getParameter(request,
			RequestUtilities.REQ_PARAM_NAME_PROXY_USER);
		String proxyAuthPass = RequestUtilities.getParameter(request,
			RequestUtilities.REQ_PARAM_NAME_PROXY_PASSWORD);
		String proxyAuthNTstation = RequestUtilities.getParameter(request,
			RequestUtilities.REQ_PARAM_NAME_PROXY_NT_WORKSTATION);
		String proxyAuthNTdomain = RequestUtilities.getParameter(request,
			RequestUtilities.REQ_PARAM_NAME_PROXY_NT_DOMAIN);
		if ( (! httpProxy.isEmpty()) && (! httpProxyPort.isEmpty())
			&& (! proxyAuthUser.isEmpty()) && (! proxyAuthPass.isEmpty()) )
		{
			Credentials cred;
			if ( (! proxyAuthNTstation.isEmpty()) && (! proxyAuthNTdomain.isEmpty()) )
			{
				// NT credentials provided - use them
				cred = new NTCredentials(proxyAuthUser, proxyAuthPass,
					proxyAuthNTstation, proxyAuthNTdomain);
			}
			else
			{
				// NT credentials NOT provided - use basic credentials
				cred = new UsernamePasswordCredentials(proxyAuthUser, proxyAuthPass);
			}
			AuthScope as = new AuthScope(httpProxy, Integer.parseInt(httpProxyPort));
			cp.setCredentials(as, cred);
		}
		HttpClientContext context = HttpClientContext.create();
		context.setCredentialsProvider(cp);
		interceptor = new ReqInterceptor(request);
		HttpClientBuilder b = HttpClients.custom();
		b.addInterceptorFirst(interceptor);
		if ( (! httpProxy.isEmpty()) && (! httpProxyPort.isEmpty()) )
		{
			DefaultProxyRoutePlanner routePlanner = new DefaultProxyRoutePlanner(
					new HttpHost(httpProxy, Integer.parseInt(httpProxyPort)));
			b.setRoutePlanner(routePlanner);
		}
		if ( RequestUtilities.hasParameter(request,
				RequestUtilities.REQ_PARAM_NAME_ACCEPT_ALL_SSL) )
		{
			b.setConnectionManager(
				HttpsWrapper.createSecureConnManager(
					RequestUtilities.getParameter(request,
						RequestUtilities.REQ_PARAM_NAME_SECURE_PROTOCOL)
				)
			);
		}
		hc = b.build();

		String method = RequestUtilities.getParameter(request,
			RequestUtilities.REQ_PARAM_NAME_PROTO_METHOD_INPUT);
		if (method.isEmpty())
		{
			method = RequestUtilities.getParameter(request,
					RequestUtilities.REQ_PARAM_NAME_PROTO_METHOD);
			if (method.isEmpty())
			{
				method = "POST";
			}
			else
			{
				method = method.toUpperCase(Locale.ENGLISH);
			}
		}
		else
		{
			method = method.toUpperCase(Locale.ENGLISH);
		}
		if ( (! protoName.isEmpty()) && (! protoMajorVer.isEmpty())
				&& (! protoMinorVer.isEmpty()) )
		{
			hreq = new BasicHttpEntityEnclosingRequest(method, opURL,
				new ProtocolVersion (protoName,
					Integer.parseInt (protoMajorVer),
					Integer.parseInt (protoMinorVer)));
		}
		else
		{
			hreq = new BasicHttpEntityEnclosingRequest(method, opURL);
		}
		host = new HttpHost(operUri.getHost(), operUri.getPort(), operUri.getScheme());
		hreq.setEntity(new StringEntity(
			payloadPrologue + payloadHeader + payloadMiddle + payloadXML + payloadEpilogue,
			ContentType.create(payloadCType, Consts.UTF_8)));
	}

	/**
	 * Performs the operation and sets the response fields.
	 * @param request the request to get the parameters from.
	 */
	public void perform (final ServletRequest request, ResponseInterpreter interpreter)
		throws IOException
	{
		hr = hc.execute(host, hreq);
		reqHeaders = interceptor.getFinalRequestHeaders();
		processEntity(hr.getEntity(), request, interpreter);
	}

	/**
	 * Gets the headers that will be sent with the request.
	 * @return a HeaderIterator for the headers that will be sent with the request.
	 */
	public HeaderIterator getReqHeaders ()
	{
		return reqHeaders;
	}

	/**
	 * Gets the headers that were received with the response.
	 * @return a HeaderIterator for the headers that were received with the response.
	 */
	public HeaderIterator getRespHeaders ()
	{
		if ( hr != null )
		{
			return hr.headerIterator();
		}
		return null;
	}

	/**
	 * Gets the response status line.
	 * @return the response status line.
	 */
	public String getStatusLine ()
	{
		StatusLine sl = getResponseStatusLine();
		if (sl == null)
		{
			return RequestUtilities.EMPTY_STR;
		}
		return sl.toString();
	}

	/**
	 * Gets the response status code.
	 * @return the response status code.
	 */
	public int getStatusCode ()
	{
		StatusLine sl = getResponseStatusLine();
		if (sl == null)
		{
			return -1;
		}
		return respStatus.getStatusCode();
	}

	/**
	 * Gets the response status body.
	 * @return the response status body.
	 */
	public String getResponseBody ()
	{
		return responseBody;
	}

	/**
	 * Returns the current HTTP response status line.
	 * @return the current HTTP response status line.
	 */
	private StatusLine getResponseStatusLine()
	{
		if ( respStatus == null && hr == null )
		{
			return null;
		}
		if ( respStatus == null )
		{
			respStatus = hr.getStatusLine();
		}
		return respStatus;
	}

	// separate method just for unit tests
	/**
	 * Processes the given entity into a response body.
	 * @param ent the entity to process.
	 * @param request the configuration request.
	 * @throws IOException
	 */
	void processEntity(HttpEntity ent, ServletRequest request, ResponseInterpreter interpreter)
		throws IOException
	{
		// a ByteArrayOutputStream will grow anyway, so we can cast the length to int
		int len = (int)ent.getContentLength();
		if ( len <= 0 )
		{
			len = 1024;
		}
		ByteArrayOutputStream baos = new ByteArrayOutputStream(len);
		ent.writeTo(baos);
		try
		{
			if ( respCharset != null && ! respCharset.isEmpty() )
			{
				responseBody = baos.toString(respCharset);
			}
			else
			{
				String encoding = RequestUtilities.DEFAULT_CHARSET;
				try
				{
					HeaderElement[] headers = ent.getContentType().getElements();
					for ( HeaderElement he : headers )
					{
						if ( he.getParameterByName(PARAM_NAME_CHARSET) != null )
						{
							encoding = he.getParameterByName(PARAM_NAME_CHARSET).getValue();
						}
					}
				}
				catch (Exception ex)
				{
					encoding = RequestUtilities.DEFAULT_CHARSET;
				}
				responseBody = baos.toString(encoding);
			}
		}
		catch (Exception ex)
		{
			responseBody = baos.toString(RequestUtilities.DEFAULT_CHARSET);
		}
		if ( RequestUtilities.hasParameter(request, RequestUtilities.REQ_PARAM_NAME_SPLIT_RESP) )
		{
			responseBody = RequestUtilities.splitByTags(responseBody, interpreter.getReplacemenets());
		}
	}

	// separate method just for unit tests
	BasicHttpRequest getRequest()
	{
		return hreq;
	}
}
