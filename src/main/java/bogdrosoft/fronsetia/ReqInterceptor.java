package bogdrosoft.fronsetia;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletRequest;

import org.apache.http.Header;
import org.apache.http.HeaderIterator;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;

public class ReqInterceptor implements HttpRequestInterceptor
{
	private static final String LF_PLUS = RequestUtilities.NEWLINE_LF + "+";
	private ServletRequest confRequest;
	private HttpRequest requestToSend;

	/**
	 * Constructs a ReqInterceptor.
	 * @param origRequest the original configuration request with parameters.
	 */
	public ReqInterceptor(ServletRequest origRequest)
	{
		confRequest = origRequest;
	}

	/**
	 * Called when a request is about to be made.
	 * @param request the request to intercept.
	 * @param context the request's context.
	 */
	@Override
	public void process(HttpRequest httpRequest, HttpContext context)
			throws HttpException, IOException
	{
		HeaderIterator he = httpRequest.headerIterator ();
		ArrayList<Header> ahe = new ArrayList<Header> (10);
		while (he.hasNext ())
		{
			ahe.add (he.nextHeader ());
		}
		if ( RequestUtilities.hasParameter (confRequest,
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
			if ( ! RequestUtilities.hasParameter (confRequest,
				RequestUtilities.REQ_PARAM_NAME_SEND_HDR_CONTENT_LENGTH) )
			{
				removeHeader (httpRequest, ahe, HTTP.CONTENT_LEN);
			}
			if ( ! RequestUtilities.hasParameter (confRequest,
				RequestUtilities.REQ_PARAM_NAME_SEND_HDR_HOST) )
			{
				removeHeader (httpRequest, ahe, HTTP.TARGET_HOST);
			}
			if ( ! RequestUtilities.hasParameter (confRequest,
				RequestUtilities.REQ_PARAM_NAME_SEND_HDR_CONNECTION) )
			{
				removeHeader (httpRequest, ahe, HTTP.CONN_DIRECTIVE);
			}
			if ( ! RequestUtilities.hasParameter (confRequest,
				RequestUtilities.REQ_PARAM_NAME_SEND_HDR_USER_AGENT) )
			{
				removeHeader (httpRequest, ahe, HTTP.USER_AGENT);
			}
			if ( ! RequestUtilities.hasParameter (confRequest,
				RequestUtilities.REQ_PARAM_NAME_SEND_HDR_CONTENT_TYPE) )
			{
				removeHeader (httpRequest, ahe, HTTP.CONTENT_TYPE);
			}
		}
		// now add the user-provided headers, like >SOAPAction: "Some-URI"<
		String soapHttpHeaders = confRequest.getParameter
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
								.substring (colonIndex+1) // header value
								.trim());
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
		requestToSend = httpRequest;
	}

	public HeaderIterator getFinalRequestHeaders()
	{
		return requestToSend.headerIterator();
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
