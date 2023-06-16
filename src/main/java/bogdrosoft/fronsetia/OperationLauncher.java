/*
 * OperationLauncher - a class that calls SOAP operations.
 *
 * Copyright (C) 2011-2023 Bogdan 'bogdro' Drozdowski, bogdro (at) users . sourceforge . net
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

package bogdrosoft.fronsetia;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletRequest;

import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HeaderIterator;
import org.apache.http.HttpEntity;
import org.apache.http.HttpException;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.HttpResponse;
import org.apache.http.ProtocolVersion;
import org.apache.http.StatusLine;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.NTCredentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.auth.params.AuthPNames;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.client.methods.HttpOptions;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.methods.HttpTrace;
import org.apache.http.client.params.AuthPolicy;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;

/**
 * OperationLauncher - a class that calls SOAP operations.
 * @author Bogdan 'bogdro' Drozdowski, bogdro (at) users . sourceforge . net
 */
public class OperationLauncher
{
	private static final String LF_PLUS = RequestUtilities.NEWLINE_LF + "+";
	private static final String PARAM_NAME_CHARSET = "charset";

	private HttpRequestBase hreq;
	private HttpResponse hr;
	private HttpParams par;
	private HeaderIterator reqHeaders;
	private StatusLine respStatus;
	private HttpClient hc;
	private String soapPrologue;
	private String soapHeader;
	private String soapMiddle;
	private String soapXML;
	private String soapEpilogue;
	private String soapCType;
	private String responseBody;
	private String respCharset;

	/**
	 * Prepare data for the operation specified by the request.
	 * @param request the request to get the parameters from.
	 */
	public void prepare (ServletRequest request)
		throws URISyntaxException, UnsupportedEncodingException
	{
		if ( request == null )
		{
			return;
		}
		//prepare data
		soapPrologue = request.getParameter (RequestUtilities.REQ_PARAM_NAME_SOAP_PROLOGUE);
		soapHeader = request.getParameter (RequestUtilities.REQ_PARAM_NAME_SOAP_HEADER);
		soapMiddle = request.getParameter (RequestUtilities.REQ_PARAM_NAME_SOAP_MIDDLE);
		soapXML = request.getParameter (RequestUtilities.REQ_PARAM_NAME_SOAP_BODY);
		soapEpilogue = request.getParameter (RequestUtilities.REQ_PARAM_NAME_SOAP_EPILOGUE);
		soapCType = request.getParameter (RequestUtilities.REQ_PARAM_NAME_CONTENT_TYPE);
		if ( soapCType == null )
		{
			soapCType = RequestUtilities.DEFAULT_CONTENT_TYPE;
		}
		if ( soapCType.isEmpty () )
		{
			soapCType = RequestUtilities.DEFAULT_CONTENT_TYPE;
		}
		respCharset = request.getParameter (RequestUtilities.REQ_PARAM_NAME_CHARSET);
		par = new BasicHttpParams ();
		String protoName = RequestUtilities.getParameter (request,
			RequestUtilities.REQ_PARAM_NAME_PROTO_NAME);
		String protoMajorVer = RequestUtilities.getParameter (request,
			RequestUtilities.REQ_PARAM_NAME_PROTO_MAJOR);
		String protoMinorVer = RequestUtilities.getParameter (request,
			RequestUtilities.REQ_PARAM_NAME_PROTO_MINOR);
		if ( (! protoName.isEmpty ()) && (! protoMajorVer.isEmpty ())
			&& (! protoMinorVer.isEmpty ()) )
		{
			par.setParameter (CoreProtocolPNames.PROTOCOL_VERSION,
				new ProtocolVersion (protoName,
					Integer.parseInt (protoMajorVer),
					Integer.parseInt (protoMinorVer)));
		}
		String httpAuthUser = RequestUtilities.getParameter (request,
			RequestUtilities.REQ_PARAM_NAME_HTTP_USER);
		String httpAuthPass = RequestUtilities.getParameter (request,
			RequestUtilities.REQ_PARAM_NAME_HTTP_PASSWORD);
		String httpAuthNTstation = RequestUtilities.getParameter (request,
			RequestUtilities.REQ_PARAM_NAME_HTTP_NT_WORKSTATION);
		String httpAuthNTdomain = RequestUtilities.getParameter (request,
			RequestUtilities.REQ_PARAM_NAME_HTTP_NT_DOMAIN);
		List<String> listOfAuth = new ArrayList<String> ();
		listOfAuth.add (AuthPolicy.DIGEST);
		listOfAuth.add (AuthPolicy.SPNEGO);
		listOfAuth.add (AuthPolicy.BASIC);
		listOfAuth.add (AuthPolicy.NTLM);
		CredentialsProvider cp = new BasicCredentialsProvider ();
		String opURL = RequestUtilities.getParameter (request,
			RequestUtilities.REQ_PARAM_NAME_OP_URL);
		int servicePortNumber = -1;
		if ( (! httpAuthUser.isEmpty ()) && (! httpAuthPass.isEmpty ()) )
		{
			Credentials cred;
			if ( (! httpAuthNTstation.isEmpty ()) && (! httpAuthNTdomain.isEmpty ()) )
			{
				// NT credentials provided - use them
				cred = new NTCredentials (httpAuthUser, httpAuthPass,
					httpAuthNTstation, httpAuthNTdomain);
			}
			else
			{
				// NT credentials NOT provided - use basic credentials
				cred = new UsernamePasswordCredentials (httpAuthUser, httpAuthPass);
			}
			URI opURI = new URI (opURL);
			servicePortNumber = opURI.getPort ();
			AuthScope as = new AuthScope (opURI.getHost (), servicePortNumber);
			cp.setCredentials (as, cred);
			par.setParameter (AuthPNames.TARGET_AUTH_PREF, listOfAuth);
		}
		String httpProxy = RequestUtilities.getParameter (request,
			RequestUtilities.REQ_PARAM_NAME_PROXY_HOST);
		String httpProxyPort = RequestUtilities.getParameter (request,
			RequestUtilities.REQ_PARAM_NAME_PROXY_PORT);
		if ( (! httpProxy.isEmpty ()) && (! httpProxyPort.isEmpty ()) )
		{
			par.setParameter (ConnRoutePNames.DEFAULT_PROXY,
				new HttpHost (httpProxy, Integer.parseInt (httpProxyPort)));
		}
		String proxyAuthUser = RequestUtilities.getParameter (request,
			RequestUtilities.REQ_PARAM_NAME_PROXY_USER);
		String proxyAuthPass = RequestUtilities.getParameter (request,
			RequestUtilities.REQ_PARAM_NAME_PROXY_PASSWORD);
		String proxyAuthNTstation = RequestUtilities.getParameter (request,
			RequestUtilities.REQ_PARAM_NAME_PROXY_NT_WORKSTATION);
		String proxyAuthNTdomain = RequestUtilities.getParameter (request,
			RequestUtilities.REQ_PARAM_NAME_PROXY_NT_DOMAIN);
		int proxyPortNumber = -1;
		if ( (! httpProxy.isEmpty ()) && (! httpProxyPort.isEmpty ())
			&& (! proxyAuthUser.isEmpty ()) && (! proxyAuthPass.isEmpty ()) )
		{
			Credentials cred;
			if ( (! proxyAuthNTstation.isEmpty ()) && (! proxyAuthNTdomain.isEmpty ()) )
			{
				// NT credentials provided - use them
				cred = new NTCredentials (proxyAuthUser, proxyAuthPass,
					proxyAuthNTstation, proxyAuthNTdomain);
			}
			else
			{
				// NT credentials NOT provided - use basic credentials
				cred = new UsernamePasswordCredentials (proxyAuthUser, proxyAuthPass);
			}
			proxyPortNumber = Integer.parseInt (httpProxyPort);
			AuthScope as = new AuthScope (httpProxy, proxyPortNumber);
			cp.setCredentials (as, cred);
			par.setParameter (AuthPNames.PROXY_AUTH_PREF, listOfAuth);
		}
		hc = new DefaultHttpClient ();
		((AbstractHttpClient)hc).setParams (par);
		((AbstractHttpClient)hc).setCredentialsProvider (cp);
		if ( RequestUtilities.hasParameter (request,
			RequestUtilities.REQ_PARAM_NAME_ACCEPT_ALL_SSL) )
		{
			hc = HttpsWrapper.wrapClientForSSL (hc, new int[] {servicePortNumber, proxyPortNumber});
		}
		String method = RequestUtilities.getParameter (request,
			RequestUtilities.REQ_PARAM_NAME_PROTO_METHOD).toUpperCase ();
		if ( method.equals (RequestUtilities.PROTO_METHOD_DELETE) )
		{
			hreq = new HttpDelete (opURL);
		}
		else if ( method.equals (RequestUtilities.PROTO_METHOD_GET) )
		{
			hreq = new HttpGet (opURL);
		}
		else if ( method.equals (RequestUtilities.PROTO_METHOD_HEAD) )
		{
			hreq = new HttpHead (opURL);
		}
		else if ( method.equals (RequestUtilities.PROTO_METHOD_OPTIONS) )
		{
			hreq = new HttpOptions (opURL);
		}
		else if ( method.equals (RequestUtilities.PROTO_METHOD_PUT) )
		{
			hreq = new HttpPut (opURL);
		}
		else if ( method.equals (RequestUtilities.PROTO_METHOD_TRACE) )
		{
			hreq = new HttpTrace (opURL);
		}
		else
		{
			// "POST" and anything unknown defaults to "POST" - the default SOAP method
			hreq = new HttpPost (opURL);
		}
		if ( hreq instanceof HttpEntityEnclosingRequestBase )
		{
			StringEntity se = new StringEntity (
				soapPrologue + soapHeader + soapMiddle + soapXML + soapEpilogue,
				soapCType, HTTP.UTF_8);
			((HttpEntityEnclosingRequestBase)hreq).setEntity (se);
		}
	}

	/**
	 * Performs the operation and sets the response fields.
	 * @param request the request to get the parameters from.
	 */
	public void perform (final ServletRequest request)
		throws IOException
	{
		((AbstractHttpClient)hc).addRequestInterceptor (new HttpRequestInterceptor ()
		{
			@Override
			public void process (HttpRequest httpRequest,
				HttpContext context)
				throws HttpException, IOException
			{
				HeaderIterator he = httpRequest.headerIterator ();
				ArrayList<Header> ahe = new ArrayList<Header> (10);
				while (he.hasNext ())
				{
					ahe.add (he.nextHeader ());
				}
				if ( RequestUtilities.hasParameter (request,
					RequestUtilities.REQ_PARAM_NAME_SEND_NO_HEADERS) )
				{
					// no default headers should be sent - delete them all
					while (! ahe.isEmpty ())
					{
						httpRequest.removeHeader (ahe.get (0));
						ahe.remove (0);
					}
				}
				else
				{
					// remove not all default headers - check which, if any
					if ( ! RequestUtilities.hasParameter (request,
						RequestUtilities.REQ_PARAM_NAME_SEND_HDR_CONTENT_LENGTH) )
					{
						removeHeader (httpRequest, ahe, HTTP.CONTENT_LEN);
					}
					if ( ! RequestUtilities.hasParameter (request,
						RequestUtilities.REQ_PARAM_NAME_SEND_HDR_HOST) )
					{
						removeHeader (httpRequest, ahe, HTTP.TARGET_HOST);
					}
					if ( ! RequestUtilities.hasParameter (request,
						RequestUtilities.REQ_PARAM_NAME_SEND_HDR_CONNECTION) )
					{
						removeHeader (httpRequest, ahe, HTTP.CONN_DIRECTIVE);
					}
					if ( ! RequestUtilities.hasParameter (request,
						RequestUtilities.REQ_PARAM_NAME_SEND_HDR_USER_AGENT) )
					{
						removeHeader (httpRequest, ahe, HTTP.USER_AGENT);
					}
					if ( ! RequestUtilities.hasParameter (request,
						RequestUtilities.REQ_PARAM_NAME_SEND_HDR_CONTENT_TYPE) )
					{
						removeHeader (httpRequest, ahe, HTTP.CONTENT_TYPE);
					}
				}
				// now add the user-provided headers, like >SOAPAction: "Some-URI"<
				String soapHttpHeaders = request.getParameter
					(RequestUtilities.REQ_PARAM_NAME_HTTP_HEADERS);
				if ( soapHttpHeaders != null )
				{
					String[] soapHttpHdrs = soapHttpHeaders
						.replaceAll (RequestUtilities.NEWLINE_CR,
							RequestUtilities.NEWLINE_LF)
						.replaceAll (LF_PLUS, RequestUtilities.NEWLINE_LF)
						.split (RequestUtilities.NEWLINE_LF);
					if ( soapHttpHdrs != null )
					{
						for ( int i = 0; i < soapHttpHdrs.length; i++ )
						{
							if ( soapHttpHdrs[i] == null )
							{
								continue;
							}
							soapHttpHdrs[i] = soapHttpHdrs[i].trim ();
							if ( soapHttpHdrs[i].isEmpty () )
							{
								continue;
							}
							int colonIndex = soapHttpHdrs[i].indexOf (':');
							if ( colonIndex == -1 )
							{
								// no colon - just the header name
								httpRequest.addHeader (soapHttpHdrs[i],
									RequestUtilities.EMPTY_STR);
							}
							else
							{
								String headerName = soapHttpHdrs[i]
									.substring (0, colonIndex);
								if ( soapHttpHdrs[i].length () > colonIndex+1 )
								{
									// header content present
									httpRequest.addHeader (headerName, // header name
										soapHttpHdrs[i]
										.substring (colonIndex+1)); // header value
								}
								else
								{
									// just "HeaderName:" provided
									httpRequest.addHeader (headerName, // header name
										RequestUtilities.EMPTY_STR);
								}
							}
						}
					}
				}
				reqHeaders = httpRequest.headerIterator ();
			}
		});
		hr = hc.execute (hreq);
		HttpEntity ent = hr.getEntity ();
		// a ByteArrayOutputStream will grow anyway, so we can cast the length to int
		int len = (int)ent.getContentLength ();
		if ( len <= 0 )
		{
			len = 1024;
		}
		ByteArrayOutputStream baos = new ByteArrayOutputStream (len);
		ent.writeTo (baos);

		String encoding = HTTP.UTF_8;
		try
		{
			HeaderElement headers[] = ent.getContentType ().getElements ();
			for ( HeaderElement he : headers )
			{
				if ( he.getParameterByName (PARAM_NAME_CHARSET) != null )
				{
					encoding = he.getParameterByName (PARAM_NAME_CHARSET).getValue ();
				}
			}
		}
		catch (Exception ex)
		{
			encoding = HTTP.UTF_8;
		}
		try
		{
			if ( respCharset != null && ! respCharset.isEmpty () )
			{
				responseBody = baos.toString (respCharset);
			}
			else
			{
				responseBody = baos.toString (encoding);
			}
		}
		catch (Exception ex)
		{
			responseBody = baos.toString (RequestUtilities.DEFAULT_CHARSET);
		}
		if ( RequestUtilities.hasParameter (request, RequestUtilities.REQ_PARAM_NAME_SOAP_SPLIT_RESP) )
		{
			responseBody = RequestUtilities.splitByTags (responseBody);
		}
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
			return hr.headerIterator ();
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
		return sl.toString ();
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
		return respStatus.getStatusCode ();
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
			respStatus = hr.getStatusLine ();
		}
		return respStatus;
	}

	/**
	 * Removes the given header from the request.
	 * @param httpRequest the request to remove the header from.
	 * @param ahe the current list of headers.
	 * @param header the header to remove.
	 */
	private static void removeHeader(HttpRequest httpRequest,
			List<Header> ahe, String header)
	{
		if (httpRequest == null || ahe == null || header == null)
		{
			return;
		}
		int size = ahe.size();
		for ( int i = 0; i < size; i++ )
		{
			Header h = ahe.get (i);
			if ( h == null )
			{
				continue;
			}
			if ( header.equals(h.getName()) )
			{
				httpRequest.removeHeader(h);
				break;
			}
		}
	}
}
