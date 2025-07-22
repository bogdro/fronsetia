/*
 * RequestUtilities - a utility class for HTTP requests.
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

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

import jakarta.servlet.ServletRequest;

/**
 * RequestUtilities - a utility class for HTTP requests.
 * @author Bogdan 'bogdro' Drozdowski, bogdro (at) users . sourceforge . net
 */
public class RequestUtilities
{
	/** The name of the parameter which holds the location of the WSDL. */
	public static final String REQ_PARAM_NAME_WSDL = "Fronsetia_WSDL";

	/** The name of the parameter which holds the name of the operation to call. */
	public static final String REQ_PARAM_NAME_OP_NAME = "Fronsetia_opName";

	/** The name of the parameter which holds the URL of the operation to call. */
	public static final String REQ_PARAM_NAME_OP_URL = "Fronsetia_opURL";

	/** The name of the parameter which holds the URL of the operation to call. */
	public static final String REQ_PARAM_NAME_CHARSET = "Fronsetia_Charset";

	/** The name of the parameter which holds the content type to use. */
	public static final String REQ_PARAM_NAME_CONTENT_TYPE = "Fronsetia_CType";

	/** The name of the parameter which holds the HTTP headers to use. */
	public static final String REQ_PARAM_NAME_HTTP_HEADERS = "Fronsetia_httpHeaders";

	/** The name of the parameter which holds the protocol name. */
	public static final String REQ_PARAM_NAME_PROTO_NAME = "Fronsetia_protoName";

	/** The name of the parameter which holds the protocol major version. */
	public static final String REQ_PARAM_NAME_PROTO_MAJOR = "Fronsetia_protoMajorVer";

	/** The name of the parameter which holds the protocol minor version. */
	public static final String REQ_PARAM_NAME_PROTO_MINOR = "Fronsetia_protoMinorVer";

	/** The name of the parameter which holds the protocol method. */
	public static final String REQ_PARAM_NAME_PROTO_METHOD = "Fronsetia_protoMethod";

	/** The name of the parameter which holds the service authentication user name. */
	public static final String REQ_PARAM_NAME_HTTP_USER = "Fronsetia_httpUserName";

	/** The name of the parameter which holds the service authentication password. */
	public static final String REQ_PARAM_NAME_HTTP_PASSWORD = "Fronsetia_httpPassword";

	/** The name of the parameter which holds the service authentication workstation name. */
	public static final String REQ_PARAM_NAME_HTTP_NT_WORKSTATION = "Fronsetia_httpNTWorkstation";

	/** The name of the parameter which holds the service authentication domain name. */
	public static final String REQ_PARAM_NAME_HTTP_NT_DOMAIN = "Fronsetia_httpNTDomain";

	/** The name of the parameter which holds the proxy host name. */
	public static final String REQ_PARAM_NAME_PROXY_HOST = "Fronsetia_proxyHost";

	/** The name of the parameter which holds the proxy port. */
	public static final String REQ_PARAM_NAME_PROXY_PORT = "Fronsetia_proxyPort";

	/** The name of the parameter which holds the proxy authentication user name. */
	public static final String REQ_PARAM_NAME_PROXY_USER = "Fronsetia_proxyUserName";

	/** The name of the parameter which holds the proxy authentication password. */
	public static final String REQ_PARAM_NAME_PROXY_PASSWORD = "Fronsetia_proxyPassword";

	/** The name of the parameter which holds the proxy authentication workstation name. */
	public static final String REQ_PARAM_NAME_PROXY_NT_WORKSTATION = "Fronsetia_proxyNTWorkstation";

	/** The name of the parameter which holds the proxy authentication domain name. */
	public static final String REQ_PARAM_NAME_PROXY_NT_DOMAIN = "Fronsetia_proxyNTDomain";

	/** The name of the parameter which tells if all SSL authentication should be accepted. */
	public static final String REQ_PARAM_NAME_ACCEPT_ALL_SSL = "Fronsetia_acceptAllSSL";

	/** The name of the parameter which tells if the Content-Length header should be sent. */
	public static final String REQ_PARAM_NAME_SEND_HDR_CONTENT_LENGTH = "Fronsetia_sendHeaderContentLength";

	/** The name of the parameter which tells if the Host header should be sent. */
	public static final String REQ_PARAM_NAME_SEND_HDR_HOST = "Fronsetia_sendHeaderHost";

	/** The name of the parameter which tells if the Connection header should be sent. */
	public static final String REQ_PARAM_NAME_SEND_HDR_CONNECTION = "Fronsetia_sendHeaderConnection";

	/** The name of the parameter which tells if the User-Agent header should be sent. */
	public static final String REQ_PARAM_NAME_SEND_HDR_USER_AGENT = "Fronsetia_sendHeaderUserAgent";

	/** The name of the parameter which tells if the Content-Type header should be sent. */
	public static final String REQ_PARAM_NAME_SEND_HDR_CONTENT_TYPE = "Fronsetia_sendHeaderContentType";

	/** The name of the parameter which tells if no default headers should be sent. */
	public static final String REQ_PARAM_NAME_SEND_NO_HEADERS = "Fronsetia_sendNoDefaultHeader";

	/** The name of the parameter which holds the SOAP prologue. */
	public static final String REQ_PARAM_NAME_SOAP_PROLOGUE = "Fronsetia_opPrologue";

	/** The name of the parameter which holds the SOAP header. */
	public static final String REQ_PARAM_NAME_SOAP_HEADER = "Fronsetia_opSoapHeader";

	/** The name of the parameter which holds the elements between the SOAP header and body. */
	public static final String REQ_PARAM_NAME_SOAP_MIDDLE = "Fronsetia_opMiddle";

	/** The name of the parameter which holds the SOAP XML body. */
	public static final String REQ_PARAM_NAME_SOAP_BODY = "Fronsetia_opXML";

	/** The name of the parameter which holds the SOAP epilogue. */
	public static final String REQ_PARAM_NAME_SOAP_EPILOGUE = "Fronsetia_opEpilogue";

	/** The name of the parameter which tells if the response should be split into lines. */
	public static final String REQ_PARAM_NAME_SOAP_SPLIT_RESP = "Fronsetia_splitResp";

	/** The ID of the element that contains the response code. */
	public static final String RESP_FIELD_ID_CODE = "Fronsetia_respCode";

	/** The ID of the  element that contains the response status line. */
	public static final String RESP_FIELD_ID_STATUS_LINE = "Fronsetia_respLine";

	/** The ID of the element that contains the response headers. */
	public static final String RESP_FIELD_ID_HEADERS = "Fronsetia_respHdrs";

	/** The ID of the element that contains the response body. */
	public static final String RESP_FIELD_ID_BODY = "Fronsetia_respBody";

	/** The ID of the element that tells if the response contained a SOAP Fault. */
	public static final String RESP_FIELD_ID_HAS_FAULT = "Fronsetia_respHasFault";

	/** The ID of the element that contains the response body's top-level elements. */
	public static final String RESP_FIELD_ID_BODY_ELEMENTS = "Fronsetia_respBodyElements";

	/** The default content type. */
	public static final String DEFAULT_CONTENT_TYPE = "application/soap+xml";//"text/xml";

	/** Line-Feed end-of-line character (LF). */
	public static final String NEWLINE_LF = "\n";

	/** Carriage-Return end-of-line character (CR). */
	public static final String NEWLINE_CR = "\r";

	/** An empty string "singleton". */
	public static final String EMPTY_STR = "";

	/** A String describing the DELETE HTTP method. */
	public static final String PROTO_METHOD_DELETE = "DELETE";

	/** A String describing the GET HTTP method. */
	public static final String PROTO_METHOD_GET = "GET";

	/** A String describing the HEAD HTTP method. */
	public static final String PROTO_METHOD_HEAD = "HEAD";

	/** A String describing the OPTIONS HTTP method. */
	public static final String PROTO_METHOD_OPTIONS = "OPTIONS";

	/** A String describing the POST HTTP method. */
	public static final String PROTO_METHOD_POST = "POST";

	/** A String describing the PUT HTTP method. */
	public static final String PROTO_METHOD_PUT = "PUT";

	/** A String describing the TRACE HTTP method. */
	public static final String PROTO_METHOD_TRACE = "TRACE";

	/** The default character set to use when reading server replies. */
	public static final String DEFAULT_CHARSET = "UTF-8";

	private static final String LEFT_ANGLE_BRACE = "<";
	private static final String RIGHT_ANGLE_BRACE = ">";
	private static final String AMPERSAND = "&";
	private static final String LEFT_ANGLE_BRACE_ENTITY = "&lt;";
	private static final String RIGHT_ANGLE_BRACE_ENTITY = "&gt;";
	private static final String AMPERSAND_ENTITY = "&amp;";

	private static final String RIGHT_ANGLE_BRACE_LF = ">\n";
	private static final String RIGHT_ANGLE_BRACE_ENTITY_LF = "&gt;\n";

	// this is a utility class
	private RequestUtilities() {}

	/**
	 * Gets the value of the given parameter or returns an empty String.
	 * @param req the request to get the parameter from.
	 * @param name the name of the parameter to get.
	 * @return the value of the given parameter or an empty String.
	 */
	public static String getParameter (ServletRequest req, String name)
	{
		if ( req == null || name == null )
		{
			return EMPTY_STR;
		}
		String ret = req.getParameter (name);
		if ( ret == null )
		{
			return EMPTY_STR;
		}
		return ret;
	}

	/**
	 * Returns a HTML-safe version of the given String. This means replacing
	 * all "&lt;", "&gt;" and "&amp;" characters to their corresponding entities.
	 * @param s The String to correct.
	 * @return The corrected (HTML-safe) String.
	 */
	public static String makeHTMLSafe (String s)
	{
		if ( s == null )
		{
			return EMPTY_STR;
		}
		return s.replaceAll (AMPERSAND, AMPERSAND_ENTITY)
			.replaceAll (LEFT_ANGLE_BRACE, LEFT_ANGLE_BRACE_ENTITY)
			.replaceAll (RIGHT_ANGLE_BRACE, RIGHT_ANGLE_BRACE_ENTITY);
	}

	/**
	 * Makes each tag in the given String appear on its own line.
	 * @param s The String to correct.
	 * @return The corrected String.
	 */
	public static String splitByTags (String s)
	{
		if ( s == null )
		{
			return EMPTY_STR;
		}
		return s.replaceAll (RIGHT_ANGLE_BRACE, RIGHT_ANGLE_BRACE_LF)
			.replaceAll (RIGHT_ANGLE_BRACE_ENTITY, RIGHT_ANGLE_BRACE_ENTITY_LF);
	}

	/**
	 * Tells if the given parameter was present in the request.
	 * @param req the request to check the parameter in for.
	 * @param name the name of the parameter to check.
	 * @return TRUE, if the parameter was present.
	 */
	public static boolean hasParameter (ServletRequest req, String name)
	{
		if ( req == null || name == null )
		{
			return false;
		}
		return req.getParameter (name) != null;
	}

	/**
	 * Prints the given exception on a JSP page.
	 * @param ex the exception to print.
	 * @param out the writer associated with the JSP page to display the exception on.
	 */
	public static void printException (Throwable ex, Writer out)
	{
		if (ex == null || out == null)
		{
			return;
		}
		StringWriter sw = new StringWriter();
		ex.printStackTrace(new PrintWriter(sw));
		try
		{
			out.write(ex.toString());
			out.write("\n");
			out.write(sw.toString());
			out.write("\n");
		}
		catch (IOException e)
		{
			// don't display exceptions about displaying exceptions
		}
	}
}
