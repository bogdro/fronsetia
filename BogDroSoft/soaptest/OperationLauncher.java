/*
 * OperationLauncher - a class that calls SOAP operations.
 *
 * Author: Bogdan 'bogdro' Drozdowski, bogdandr <at> op . pl
 *
 *    SOAP Service Tester - an application for low-level testing of SOAP Services.
 *    Copyright (C) 2011 Bogdan 'bogdro' Drozdowski, bogdandr <at> op . pl
 *
 *    This program is free software: you can redistribute it and/or modify
 *    it under the terms of the GNU Affero General Public License as
 *    published by the Free Software Foundation, either version 3 of the
 *    License, or (at your option) any later version.
 *
 *    This program is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *    GNU Affero General Public License for more details.
 *
 *    You should have received a copy of the GNU Affero General Public License
 *    along with this program.  If not, see http://www.gnu.org/licenses/.
 *
 */

package BogDroSoft.soaptest;

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
 * @author Bogdan 'bogdro' Drozdowski, bogdandr <at> op . pl
 */
public class OperationLauncher
{
	private static final String LFplus = RequestUtilities.newLineLF + "+";
	private static final String charsetParamName = "charset";

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

	/**
	 * Prepare data for the operation specified by the request.
	 * @param request the request to get the parameter from.
	 */
	public void prepare (ServletRequest request)
		throws URISyntaxException, UnsupportedEncodingException
	{
		if ( request == null ) return;
		//prepare data
		soapPrologue = request.getParameter (RequestUtilities.reqParNameSOAPPrologue);
		soapHeader = request.getParameter (RequestUtilities.reqParNameSOAPHeader);
		soapMiddle = request.getParameter (RequestUtilities.reqParNameSOAPMiddle);
		soapXML = request.getParameter (RequestUtilities.reqParNameSOAPBody);
		soapEpilogue = request.getParameter (RequestUtilities.reqParNameSOAPEpilogue);
		soapCType = request.getParameter (RequestUtilities.reqParNameCType);
		if ( soapCType == null )
		{
			soapCType = RequestUtilities.defContentType;
		}
		if ( soapCType.isEmpty () )
		{
			soapCType = RequestUtilities.defContentType;
		}
		par = new BasicHttpParams ();
		String protoName = RequestUtilities.getParameter (request,
			RequestUtilities.reqParNameProtoName);
		String protoMajorVer = RequestUtilities.getParameter (request,
			RequestUtilities.reqParNameProtoMajor);
		String protoMinorVer = RequestUtilities.getParameter (request,
			RequestUtilities.reqParNameProtoMinor);
		if ( (! protoName.isEmpty ()) && (! protoMajorVer.isEmpty ())
			&& (! protoMinorVer.isEmpty ()) )
		{
			par.setParameter (CoreProtocolPNames.PROTOCOL_VERSION,
				new ProtocolVersion (protoName,
					Integer.parseInt (protoMajorVer),
					Integer.parseInt (protoMinorVer)));
		}
		String httpAuthUser = RequestUtilities.getParameter (request,
			RequestUtilities.reqParNameHTTPUser);
		String httpAuthPass = RequestUtilities.getParameter (request,
			RequestUtilities.reqParNameHTTPPass);
		String httpAuthNTstation = RequestUtilities.getParameter (request,
			RequestUtilities.reqParNameHTTPNTworkstation);
		String httpAuthNTdomain = RequestUtilities.getParameter (request,
			RequestUtilities.reqParNameHTTPNTdomain);
		List<String> listOfAuth = new ArrayList<String> ();
		listOfAuth.add (AuthPolicy.DIGEST);
		listOfAuth.add (AuthPolicy.SPNEGO);
		listOfAuth.add (AuthPolicy.BASIC);
		listOfAuth.add (AuthPolicy.NTLM);
		CredentialsProvider cp = new BasicCredentialsProvider ();
		String opURL = RequestUtilities.getParameter (request,
			RequestUtilities.reqParNameOpURL);
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
			AuthScope as = new AuthScope (opURI.getHost (), opURI.getPort ());
			cp.setCredentials (as, cred);
			par.setParameter (AuthPNames.TARGET_AUTH_PREF, listOfAuth);
		}
		String httpProxy = RequestUtilities.getParameter (request,
			RequestUtilities.reqParNameProxyHost);
		String httpProxyPort = RequestUtilities.getParameter (request,
			RequestUtilities.reqParNameProxyPort);
		if ( (! httpProxy.isEmpty ()) && (! httpProxyPort.isEmpty ()) )
		{
			par.setParameter (ConnRoutePNames.DEFAULT_PROXY,
				new HttpHost (httpProxy, Integer.parseInt (httpProxyPort)));
		}
		String proxyAuthUser = RequestUtilities.getParameter (request,
			RequestUtilities.reqParNameProxyUser);
		String proxyAuthPass = RequestUtilities.getParameter (request,
			RequestUtilities.reqParNameProxyPass);
		String proxyAuthNTstation = RequestUtilities.getParameter (request,
			RequestUtilities.reqParNameProxyNTworkstation);
		String proxyAuthNTdomain = RequestUtilities.getParameter (request,
			RequestUtilities.reqParNameProxyNTdomain);
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
			AuthScope as = new AuthScope (httpProxy, Integer.parseInt (httpProxyPort));
			cp.setCredentials (as, cred);
			par.setParameter (AuthPNames.PROXY_AUTH_PREF, listOfAuth);
		}
		hc = new DefaultHttpClient ();
		((AbstractHttpClient)hc).setParams (par);
		((AbstractHttpClient)hc).setCredentialsProvider (cp);
		String method = RequestUtilities.getParameter (request,
			RequestUtilities.reqParNameProtoMethod).toUpperCase ();
		if ( method.equals (RequestUtilities.protoMethodDelete) )
		{
			hreq = new HttpDelete (opURL);
		}
		else if ( method.equals (RequestUtilities.protoMethodGet) )
		{
			hreq = new HttpGet (opURL);
		}
		else if ( method.equals (RequestUtilities.protoMethodHead) )
		{
			hreq = new HttpHead (opURL);
		}
		else if ( method.equals (RequestUtilities.protoMethodOptions) )
		{
			hreq = new HttpOptions (opURL);
		}
		else if ( method.equals (RequestUtilities.protoMethodPut) )
		{
			hreq = new HttpPut (opURL);
		}
		else if ( method.equals (RequestUtilities.protoMethodTrace) )
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
	 * Performs the operation.
	 * @return a HeaderIterator for the headers that will be sent with the request.
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
					RequestUtilities.reqParNameSendNoHdr) )
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
						RequestUtilities.reqParNameSendHdrContentLength) )
					{
						for ( int i = 0; i < ahe.size (); i++ )
						{
							Header h = ahe.get (i);
							if ( h == null ) continue;
							if ( h.getName ().equals (HTTP.CONTENT_LEN) )
							{
								httpRequest.removeHeader (ahe.get (i));
								break;
							}
						}
					}
					if ( ! RequestUtilities.hasParameter (request,
						RequestUtilities.reqParNameSendHdrHost) )
					{
						for ( int i = 0; i < ahe.size (); i++ )
						{
							Header h = ahe.get (i);
							if ( h == null ) continue;
							if ( h.getName ().equals (HTTP.TARGET_HOST) )
							{
								httpRequest.removeHeader (ahe.get (i));
								break;
							}
						}
					}
					if ( ! RequestUtilities.hasParameter (request,
						RequestUtilities.reqParNameSendHdrConnection) )
					{
						for ( int i = 0; i < ahe.size (); i++ )
						{
							Header h = ahe.get (i);
							if ( h == null ) continue;
							if ( h.getName ().equals (HTTP.CONN_DIRECTIVE) )
							{
								httpRequest.removeHeader (ahe.get (i));
								break;
							}
						}
					}
					if ( ! RequestUtilities.hasParameter (request,
						RequestUtilities.reqParNameSendHdrUserAgent) )
					{
						for ( int i = 0; i < ahe.size (); i++ )
						{
							Header h = ahe.get (i);
							if ( h == null ) continue;
							if ( h.getName ().equals (HTTP.USER_AGENT) )
							{
								httpRequest.removeHeader (ahe.get (i));
								break;
							}
						}
					}
					if ( ! RequestUtilities.hasParameter (request,
						RequestUtilities.reqParNameSendHdrContentType) )
					{
						for ( int i = 0; i < ahe.size (); i++ )
						{
							Header h = ahe.get (i);
							if ( h == null ) continue;
							if ( h.getName ().equals (HTTP.CONTENT_TYPE) )
							{
								httpRequest.removeHeader (ahe.get (i));
								break;
							}
						}
					}
				}
				// now add the user-provided headers, like >SOAPAction: "Some-URI"<
				String soapHttpHeaders = request.getParameter
					(RequestUtilities.reqParNameHTTPHdrs);
				if ( soapHttpHeaders != null )
				{
					String[] soapHttpHdrs = soapHttpHeaders
						.replaceAll (RequestUtilities.newLineCR,
							RequestUtilities.newLineLF)
						.replaceAll (LFplus, RequestUtilities.newLineLF)
						.split (RequestUtilities.newLineLF);
					if ( soapHttpHdrs != null )
					{
						for ( int i = 0; i < soapHttpHdrs.length; i++ )
						{
							if ( soapHttpHdrs[i] == null ) continue;
							soapHttpHdrs[i] = soapHttpHdrs[i].trim ();
							if ( soapHttpHdrs[i].isEmpty () ) continue;
							int colonIndex = soapHttpHdrs[i].indexOf (':');
							if ( colonIndex == -1 )
							{
								// no colon - just the header name
								httpRequest.addHeader (soapHttpHdrs[i],
									RequestUtilities.empty);
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
										RequestUtilities.empty);
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
		if ( len <= 0 ) len = 1024;
		ByteArrayOutputStream baos = new ByteArrayOutputStream (len);
		ent.writeTo (baos);

		String encoding = null;
		try
		{
			HeaderElement headers[] = ent.getContentType ().getElements ();
			for ( HeaderElement he : headers )
			{
				if ( he.getParameterByName (charsetParamName) != null )
				{
					encoding = he.getParameterByName (charsetParamName).getValue ();
				}
			}
		}
		catch (Exception ex)
		{
			encoding = HTTP.UTF_8;
		}
		if ( encoding == null )
		{
			encoding = HTTP.UTF_8;
		}
		try
		{
			responseBody = baos.toString (encoding);
		}
		catch (Exception ex)
		{
			responseBody = baos.toString ();
		}
		if ( RequestUtilities.hasParameter (request, RequestUtilities.reqParNameSOAPRespSplit) )
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
		return hr.headerIterator ();
	}

	/**
	 * Gets the response status line.
	 * @return the response status line.
	 */
	public String getStatusLine ()
	{
		if ( respStatus == null && hr == null ) return RequestUtilities.empty;
		if ( respStatus == null )
		{
			respStatus = hr.getStatusLine ();
			if ( respStatus == null ) return RequestUtilities.empty;
		}
		return respStatus.toString ();
	}

	/**
	 * Gets the response status code.
	 * @return the response status code.
	 */
	public int getStatusCode ()
	{
		if ( respStatus == null && hr == null ) return -1;
		if ( respStatus == null )
		{
			respStatus = hr.getStatusLine ();
			if ( respStatus == null ) return -1;
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
}

