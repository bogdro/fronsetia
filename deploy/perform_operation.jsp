<%@ page language="java" session="false" %>
<%@ page import="java.io.ByteArrayOutputStream" %>
<%@ page import="org.apache.http.HeaderElement" %>
<%@ page import="org.apache.http.HeaderIterator" %>
<%@ page import="org.apache.http.HttpEntity" %>
<%@ page import="org.apache.http.HttpResponse" %>
<%@ page import="org.apache.http.entity.StringEntity" %>
<%@ page import="org.apache.http.client.HttpClient" %>
<%@ page import="org.apache.http.client.methods.HttpPost" %>
<%@ page import="org.apache.http.impl.client.DefaultHttpClient" %>
<%@ page import="org.apache.http.protocol.HTTP" %>

<% String wsdlLocation = request.getParameter ("SOAPTester_WSDL"); %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
        "http://www.w3.org/TR/html4/loose.dtd">

<!--
    SOAP Service Tester - an application for low-level testing of SOAP Services.
    Copyright (C) 2011 Bogdan 'bogdro' Drozdowski, bogdandr <at> op . pl

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License as
    published by the Free Software Foundation, either version 3 of the
    License, or (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License
    along with this program.  If not, see http://www.gnu.org/licenses/.
-->

<HTML lang="en">
<HEAD>
<META HTTP-EQUIV="Content-Type"     CONTENT="text/html; charset=UTF-8">
<META HTTP-EQUIV="Content-Language" CONTENT="en">
<META http-equiv="Content-Style-Type" content="text/css">
<LINK rel="stylesheet" href="resources/soaptester.css" type="text/css">

<TITLE> SOAP Service Tester - <%= wsdlLocation %> </TITLE>

<META NAME="Author" CONTENT="Bogdan D.">
<META NAME="Description" CONTENT="SOAP Service Tester">
<META NAME="Keywords" CONTENT="SOAP, WSDL, service tester">
<META NAME="Language" CONTENT="en">
<META NAME="Generator" CONTENT="KWrite/Kate; www.kate-editor.org">

</HEAD><BODY>

<%
	final String newLineLF = "\n";
	final String leftAngBr = "<";
	final String rightAngBr = ">";
	final String leftAngBrEnt = "&lt;";
	final String rightAngBrEnt = "&gt;";
	final String charsetParamName = "charset";
	final String emptyString = "";

	String soapPrologue = request.getParameter ("SOAPTester_opPrologue");
	String soapHeader = request.getParameter ("SOAPTester_opSoapHeader");
	String soapMiddle = request.getParameter ("SOAPTester_opMiddle");
	String soapXML = request.getParameter ("SOAPTester_opXML");
	String soapEpilogue = request.getParameter ("SOAPTester_opEpilogue");
	String soapCType = request.getParameter ("SOAPTester_CType");
	if ( soapCType == null || soapCType.isEmpty () )
	{
		soapCType = "text/xml";
	}
	HttpPost hp = new HttpPost (request.getParameter ("SOAPTester_opURL"));
	String soapHttpHeaders = request.getParameter ("SOAPTester_httpHeaders");
	if ( soapHttpHeaders != null )
	{
		String[] soapHttpHdrs = soapHttpHeaders.replaceAll ("\r", newLineLF)
			.replaceAll (newLineLF + "+", newLineLF).split (newLineLF);
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
					hp.addHeader (soapHttpHdrs[i], emptyString);
				}
				else
				{
					String headerName = soapHttpHdrs[i].substring (0, colonIndex);
					if ( soapHttpHdrs[i].length () > colonIndex+1 )
					{
						// header content present
						hp.addHeader (headerName, // header name
							soapHttpHdrs[i].substring (colonIndex+1)); // header value
					}
					else
					{
						// just "HeaderName:" provided
						hp.addHeader (headerName, // header name
							emptyString);
					}
				}
			}
		}
	}
%>
<h1 class="c">SOAP Service Tester - result of operation</h1>
<hr>
WSDL location: <a href="<%= wsdlLocation %>"><%= wsdlLocation %></a><br>
Operation name: <code><%= request.getParameter ("SOAPTester_opName") %></code><br>
HTTP request user-defined headers:
<pre>
<%
	try
	{
		HeaderIterator reqhi = hp.headerIterator ();
		out.flush ();
		while (reqhi.hasNext ())
		{
			out.println (reqhi.next ());
			out.flush ();
		}
	}
	catch (Exception ex)
	{
%>Exception occurred:<%
		out.println (ex);
	}
%></PRE>
Operation input (HTTP body):
<pre>
<%= soapPrologue.replaceAll (leftAngBr, leftAngBrEnt).replaceAll (rightAngBr, rightAngBrEnt) +
soapHeader.replaceAll (leftAngBr, leftAngBrEnt).replaceAll (rightAngBr, rightAngBrEnt) +
soapMiddle.replaceAll (leftAngBr, leftAngBrEnt).replaceAll (rightAngBr, rightAngBrEnt) +
soapXML.replaceAll (leftAngBr, leftAngBrEnt).replaceAll (rightAngBr, rightAngBrEnt) +
soapEpilogue.replaceAll (leftAngBr, leftAngBrEnt).replaceAll (rightAngBr, rightAngBrEnt) %></pre>



<br>
<hr>
<h2 class="c">Operation result:</h2>
<%
	// flush the current page output to the user:
	out.flush ();

	HttpClient hc = new DefaultHttpClient ();
	StringEntity se = new StringEntity (
		soapPrologue + soapHeader + soapMiddle + soapXML + soapEpilogue,
		soapCType, HTTP.UTF_8);
	//SOAPAction: "Some-URI"
	hp.setEntity (se);
	try
	{
		HttpResponse hr = hc.execute (hp);
		HeaderIterator hi = hr.headerIterator ();
%>
HTTP response code: <code><%= hr.getStatusLine ().getStatusCode () %></code><br>
HTTP response line: <code><%= hr.getStatusLine () %></code><br>
HTTP response headers:
<PRE>
<%
		out.flush ();
		while (hi.hasNext ())
		{
			out.println (hi.next ());
			out.flush ();
		}
%></PRE>
HTTP response body:
<PRE>
<%
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
		String responseBody;
		String replacement = rightAngBrEnt;
		if ( request.getParameter ("SOAPTester_splitResp") != null )
		{
			replacement += newLineLF;
		}
		try
		{
			responseBody = baos.toString (encoding);
		}
		catch (Exception ex)
		{
			responseBody = baos.toString ();
		}
		out.println (responseBody.replaceAll (leftAngBr, leftAngBrEnt)
				.replaceAll (rightAngBr, replacement));
	}
	catch (Exception ex)
	{
%>Exception occurred:<%
		out.println (ex);
	}
%></pre>


<%@ include file="footer.html" %>

</BODY>
</HTML>
