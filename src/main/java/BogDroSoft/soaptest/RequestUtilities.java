/*
 * RequestUtilities - a utility class for HTTP requests.
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

package BogDroSoft.soaptest;

import javax.servlet.ServletRequest;

/**
 * RequestUtilities - a utility class for HTTP requests.
 * @author Bogdan 'bogdro' Drozdowski, bogdandr <at> op . pl
 */
public class RequestUtilities
{
	/** The name of the parameter which holds the location of the WSDL. */
	public static final String reqParNameWSDL = "SOAPTester_WSDL";
	/** The name of the parameter which holds the name of the operation to call. */
	public static final String reqParNameOpName = "SOAPTester_opName";
	/** The name of the parameter which holds the URL of the operation to call. */
	public static final String reqParNameOpURL = "SOAPTester_opURL";
	/** The name of the parameter which holds the URL of the operation to call. */
	public static final String reqParNameCharset = "SOAPTester_Charset";

	/** The name of the parameter which holds the content type to use. */
	public static final String reqParNameCType = "SOAPTester_CType";
	/** The name of the parameter which holds the HTTP headers to use. */
	public static final String reqParNameHTTPHdrs = "SOAPTester_httpHeaders";
	/** The name of the parameter which holds the protocol name. */
	public static final String reqParNameProtoName = "SOAPTester_protoName";
	/** The name of the parameter which holds the protocol major version. */
	public static final String reqParNameProtoMajor = "SOAPTester_protoMajorVer";
	/** The name of the parameter which holds the protocol minor version. */
	public static final String reqParNameProtoMinor = "SOAPTester_protoMinorVer";
	/** The name of the parameter which holds the protocol method. */
	public static final String reqParNameProtoMethod = "SOAPTester_protoMethod";
	/** The name of the parameter which holds the service authentication user name. */
	public static final String reqParNameHTTPUser = "SOAPTester_httpUserName";
	/** The name of the parameter which holds the service authentication password. */
	public static final String reqParNameHTTPPass = "SOAPTester_httpPassword";
	/** The name of the parameter which holds the service authentication workstation name. */
	public static final String reqParNameHTTPNTworkstation = "SOAPTester_httpNTWorkstation";
	/** The name of the parameter which holds the service authentication domain name. */
	public static final String reqParNameHTTPNTdomain = "SOAPTester_httpNTDomain";
	/** The name of the parameter which holds the proxy host name. */
	public static final String reqParNameProxyHost = "SOAPTester_proxyHost";
	/** The name of the parameter which holds the proxy port. */
	public static final String reqParNameProxyPort = "SOAPTester_proxyPort";
	/** The name of the parameter which holds the proxy authentication user name. */
	public static final String reqParNameProxyUser = "SOAPTester_proxyUserName";
	/** The name of the parameter which holds the proxy authentication password. */
	public static final String reqParNameProxyPass = "SOAPTester_proxyPassword";
	/** The name of the parameter which holds the proxy authentication workstation name. */
	public static final String reqParNameProxyNTworkstation = "SOAPTester_proxyNTWorkstation";
	/** The name of the parameter which holds the proxy authentication domain name. */
	public static final String reqParNameProxyNTdomain = "SOAPTester_proxyNTDomain";
	/** The name of the parameter which tells if all SSL authentication should be accepted. */
	public static final String reqParNameAcceptAllSSL = "SOAPTester_acceptAllSSL";

	/** The name of the parameter which tells if the Content-Length header should be sent. */
	public static final String reqParNameSendHdrContentLength = "SOAPTester_sendHeaderContentLength";
	/** The name of the parameter which tells if the Host header should be sent. */
	public static final String reqParNameSendHdrHost = "SOAPTester_sendHeaderHost";
	/** The name of the parameter which tells if the Connection header should be sent. */
	public static final String reqParNameSendHdrConnection = "SOAPTester_sendHeaderConnection";
	/** The name of the parameter which tells if the User-Agent header should be sent. */
	public static final String reqParNameSendHdrUserAgent = "SOAPTester_sendHeaderUserAgent";
	/** The name of the parameter which tells if the Content-Type header should be sent. */
	public static final String reqParNameSendHdrContentType = "SOAPTester_sendHeaderContentType";
	/** The name of the parameter which tells if no default headers should be sent. */
	public static final String reqParNameSendNoHdr = "SOAPTester_sendNoDefaultHeader";

	/** The name of the parameter which holds the SOAP prologue. */
	public static final String reqParNameSOAPPrologue = "SOAPTester_opPrologue";
	/** The name of the parameter which holds the SOAP header. */
	public static final String reqParNameSOAPHeader = "SOAPTester_opSoapHeader";
	/** The name of the parameter which holds the elements between the SOAP header and body. */
	public static final String reqParNameSOAPMiddle = "SOAPTester_opMiddle";
	/** The name of the parameter which holds the SOAP XML body. */
	public static final String reqParNameSOAPBody = "SOAPTester_opXML";
	/** The name of the parameter which holds the SOAP epilogue. */
	public static final String reqParNameSOAPEpilogue = "SOAPTester_opEpilogue";
	/** The name of the parameter which tells if the response should be split into lines. */
	public static final String reqParNameSOAPRespSplit = "SOAPTester_splitResp";

	/** The ID of the element that contains the response code. */
	public static final String respFieldIDCode = "SOAPTester_respCode";
	/** The ID of the  element that contains the response status line. */
	public static final String respFieldIDStatusLine = "SOAPTester_respLine";
	/** The ID of the element that contains the response headers. */
	public static final String respFieldIDHeaders = "SOAPTester_respHdrs";
	/** The ID of the element that contains the response body. */
	public static final String respFieldIDBody = "SOAPTester_respBody";
	/** The ID of the element that tells if the response contained a SOAP Fault. */
	public static final String respFieldIDHasFault = "SOAPTester_respHasFault";
	/** The ID of the element that contains the response body's top-level elements. */
	public static final String respFieldIDBodyElements = "SOAPTester_respBodyElements";

	/** The default content type. */
	public static final String defContentType = "application/soap+xml";//"text/xml";
	/** Unix end-of-line (LF). */
	public static final String newLineLF = "\n";
	/** MAC end-of-line (CR). */
	public static final String newLineCR = "\r";
	/** An empty string "singleton". */
	public static final String empty = "";

	/** A String describing the DELETE HTTP method. */
	public static final String protoMethodDelete = "DELETE";
	/** A String describing the GET HTTP method. */
	public static final String protoMethodGet = "GET";
	/** A String describing the HEAD HTTP method. */
	public static final String protoMethodHead = "HEAD";
	/** A String describing the OPTIONS HTTP method. */
	public static final String protoMethodOptions = "OPTIONS";
	/** A String describing the POST HTTP method. */
	public static final String protoMethodPost = "POST";
	/** A String describing the PUT HTTP method. */
	public static final String protoMethodPut = "PUT";
	/** A String describing the TRACE HTTP method. */
	public static final String protoMethodTrace = "TRACE";

	/** The default character set to use when reading server replies. */
	public static final String defaultCharset = "UTF-8";

	private static final String leftAngBr = "<";
	private static final String rightAngBr = ">";
	private static final String ampersand = "&";
	private static final String leftAngBrEnt = "&lt;";
	private static final String rightAngBrEnt = "&gt;";
	private static final String ampersandEnt = "&amp;";

	private static final String rightAngBrLF = ">\n";
	private static final String rightAngBrEntLF = "&gt;\n";

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
		if ( req == null || name == null ) return empty;
		String ret = req.getParameter (name);
		if ( ret == null ) return empty;
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
		if ( s == null ) return empty;
		return s.replaceAll (ampersand, ampersandEnt)
			.replaceAll (leftAngBr, leftAngBrEnt)
			.replaceAll (rightAngBr, rightAngBrEnt);
	}

	/**
	 * Makes each tag in the given String appear on its own line.
	 * @param s The String to correct.
	 * @return The corrected String.
	 */
	public static String splitByTags (String s)
	{
		if ( s == null ) return empty;
		return s.replaceAll (rightAngBr, rightAngBrLF)
			.replaceAll (rightAngBrEnt, rightAngBrEntLF);
	}

	/**
	 * Tells if the given parameter was present in the request.
	 * @param req the request to check the parameter in for.
	 * @param name the name of the parameter to check.
	 * @return TRUE, if the parameter was present.
	 */
	public static boolean hasParameter (ServletRequest req, String name)
	{
		if ( req == null || name == null ) return false;
		return req.getParameter (name) != null;
	}
}
