<%@ page language="java" session="false" %>
<%@ page import="java.io.PrintWriter" %>
<%@ page import="java.io.StringWriter" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.List" %>
<%@ page import="BogDroSoft.soaptest.RequestUtilities" %>
<%@ page import="BogDroSoft.soaptest.OperationLauncher" %>
<%@ page import="BogDroSoft.soaptest.SOAPInterpreter" %>

<%
String wsdlLocation = request.getParameter (RequestUtilities.reqParNameWSDL);
String opName = RequestUtilities.getParameter (request, RequestUtilities.reqParNameOpName);
String soapPrologue = RequestUtilities.getParameter (request, RequestUtilities.reqParNameSOAPPrologue);
String soapHeader = RequestUtilities.getParameter (request, RequestUtilities.reqParNameSOAPHeader);
String soapMiddle = RequestUtilities.getParameter (request, RequestUtilities.reqParNameSOAPMiddle);
String soapXML = RequestUtilities.getParameter (request, RequestUtilities.reqParNameSOAPBody);
String soapEpilogue = RequestUtilities.getParameter (request, RequestUtilities.reqParNameSOAPEpilogue);
String soapCType = RequestUtilities.getParameter (request, RequestUtilities.reqParNameCType);
String proxyPort = RequestUtilities.getParameter (request, RequestUtilities.reqParNameProxyPort);

OperationLauncher ol = new OperationLauncher ();
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
        "http://www.w3.org/TR/html4/loose.dtd">

<!--
    SOAP Service Tester - an application for low-level testing of SOAP Services.
    Copyright (C) 2011-2012 Bogdan 'bogdro' Drozdowski, bogdandr <at> op . pl

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
<META HTTP-EQUIV="Content-Type"       CONTENT="text/html; charset=UTF-8">
<META HTTP-EQUIV="Content-Language"   CONTENT="en">
<META HTTP-EQUIV="Content-Style-Type" CONTENT="text/css">
<meta HTTP-EQUIV="Cache-Control"      CONTENT="no-cache, no-store, private">
<META HTTP-EQUIV="Pragma"	      CONTENT="no-cache">
<%--<META HTTP-EQUIV="Expires"            CONTENT="">--%>
<LINK rel="stylesheet" href="resources/soaptester.css" type="text/css">

<TITLE> SOAP Service Tester: <%= opName %>: <%= wsdlLocation %> </TITLE>

<META NAME="Author" CONTENT="Bogdan D.">
<META NAME="Description" CONTENT="SOAP Service Tester">
<META NAME="Keywords" CONTENT="SOAP, WSDL, service tester">
<META NAME="Language" CONTENT="en">
<META NAME="Generator" CONTENT="KWrite/Kate; www.kate-editor.org">

</HEAD><BODY>

<h1 class="c">SOAP Service Tester - result of operation</h1>
<hr>
WSDL location: <a href="<%= wsdlLocation %>"
	id="<%= RequestUtilities.reqParNameWSDL %>"><%= wsdlLocation %></a><br>
Operation name: <code id="<%= RequestUtilities.reqParNameOpName %>"><%= opName %></code><br>
Protocol name and version: <code id="<%= RequestUtilities.reqParNameProtoName %>"
	><%= RequestUtilities.getParameter (request, RequestUtilities.reqParNameProtoName)
	%>/<%= RequestUtilities.getParameter (request, RequestUtilities.reqParNameProtoMajor)
	%>.<%= RequestUtilities.getParameter (request, RequestUtilities.reqParNameProtoMinor) %></code><br>
Protocol method: <code id="<%= RequestUtilities.reqParNameProtoMethod %>"
	><%= RequestUtilities.getParameter (request, RequestUtilities.reqParNameProtoMethod) %></code><br>
Protocol authentication: user=<code id="<%= RequestUtilities.reqParNameHTTPUser %>"
	><%= RequestUtilities.getParameter (request, RequestUtilities.reqParNameHTTPUser) %></code>,
	password=<code id="<%= RequestUtilities.reqParNameHTTPPass %>"
	><%= RequestUtilities.getParameter (request, RequestUtilities.reqParNameHTTPPass) %></code>,
	NT workstation=<code id="<%= RequestUtilities.reqParNameHTTPNTworkstation %>"
	><%= RequestUtilities.getParameter (request, RequestUtilities.reqParNameHTTPNTworkstation) %></code>,
	NT domain=<code id="<%= RequestUtilities.reqParNameHTTPNTdomain %>"
	><%= RequestUtilities.getParameter (request, RequestUtilities.reqParNameHTTPNTdomain) %></code>
	<br>
Proxy: <code id="<%= RequestUtilities.reqParNameProxyHost %>"
	><%= RequestUtilities.getParameter (request, RequestUtilities.reqParNameProxyHost) %>
<%
	if ( ! proxyPort.isEmpty () )
	{
%>
		:<%= proxyPort %>
<%
	}
%>
</code><br>
Proxy authentication: user=<code id="<%= RequestUtilities.reqParNameProxyUser %>"
	><%= RequestUtilities.getParameter (request, RequestUtilities.reqParNameProxyUser) %></code>,
	password=<code id="<%= RequestUtilities.reqParNameProxyPass %>"
	><%= RequestUtilities.getParameter (request, RequestUtilities.reqParNameProxyPass) %></code>,
	NT workstation=<code id="<%= RequestUtilities.reqParNameProxyNTworkstation %>"
	><%= RequestUtilities.getParameter (request, RequestUtilities.reqParNameProxyNTworkstation) %></code>,
	NT domain=<code id="<%= RequestUtilities.reqParNameProxyNTdomain %>"
	><%= RequestUtilities.getParameter (request, RequestUtilities.reqParNameProxyNTdomain) %></code>
	<br>
User-defined request headers:
<pre id="<%= RequestUtilities.reqParNameHTTPHdrs %>">
<%
	try
	{
		ol.prepare (request);
		ol.perform (request);
		Iterator reqhi = ol.getReqHeaders ();
		out.flush ();
		while (reqhi.hasNext ())
		{
			out.println (reqhi.next ());
			out.flush ();
		}
	}
	catch (Exception ex)
	{
%>Exception occurred while performing the operation or displaying input parameters:
<%
		StringWriter sw = new StringWriter ();
		ex.printStackTrace (new PrintWriter (sw));
		out.println (ex);
		out.println (sw.toString ());
	}
%></PRE>
Operation input (HTTP body):
<pre id="<%= RequestUtilities.reqParNameSOAPBody %>">
<%= RequestUtilities.makeHTMLSafe (soapPrologue) +
RequestUtilities.makeHTMLSafe (soapHeader) +
RequestUtilities.makeHTMLSafe (soapMiddle) +
RequestUtilities.makeHTMLSafe (soapXML) +
RequestUtilities.makeHTMLSafe (soapEpilogue) %></pre>

Expected output encoding (used only if can't be detected automatically):
 <code id="<%= RequestUtilities.reqParNameCharset %>"
	><%= RequestUtilities.getParameter (request, RequestUtilities.reqParNameCharset) %></code>





<br>
<hr>
<h2 class="c">Operation result:</h2>


HTTP response code: <code id="<%= RequestUtilities.respFieldIDCode %>"
	><%= (ol.getStatusCode () >= 0)? ol.getStatusCode () : "" %></code><br>
HTTP response line: <code id="<%= RequestUtilities.respFieldIDStatusLine %>"
	><%= ol.getStatusLine () %></code><br>
<%
	// flush the current page output to the user:
	out.flush ();
	try
	{
		String resp = ol.getResponseBody ();
		try
		{
			SOAPInterpreter si = new SOAPInterpreter ();
			si.parseResponse (resp);
%>
			SOAP Fault found in the response:
<%
			if ( si.wasFault () )
			{
%>
				<span id="<%= RequestUtilities.respFieldIDHasFault %>"
					class="soapfault">YES</span><br>
<%
			}
			else
			{
%>
				<span id="<%= RequestUtilities.respFieldIDHasFault %>"
					class="nosoapfault">NO</span><br>
<%
			}
%>
			SOAP response type (elements): <span
				id="<%= RequestUtilities.respFieldIDBodyElements %>">
<%
			List<String> respTypes = si.getBodyElements ();
			if ( respTypes != null )
			{
				int respTypeSize = respTypes.size ();
				for ( int i = 0; i < respTypeSize; i++ )
				{
%>
					<code><%= respTypes.get (i) %></code>
<%
					if ( i < respTypeSize - 1 )
					{
%>
						,
<%
					}
				}
			}
%>
			</span>
<%
		}
		catch (NoClassDefFoundError ncdfe)
		{
%>
			Unable to get reponse SOAP status and type - axiom.jar or axis2-saaj.jar not installed.
<%
		}
		catch (Exception ex)
		{
%>
			Unable to get reponse SOAP status and type.
<%
		}
%>
<br>
HTTP response headers:
<%
		out.flush ();
		Iterator hi = ol.getRespHeaders ();
		if ( hi != null )
		{
%>
<PRE id="<%= RequestUtilities.respFieldIDHeaders %>">
<%
			while (hi.hasNext ())
			{
				out.println (hi.next ());
				out.flush ();
			}
%></PRE>
<%
		}
%>
<br>
HTTP response body:
<PRE id="<%= RequestUtilities.respFieldIDBody %>">
<%
		out.println (RequestUtilities.makeHTMLSafe (resp));
%></PRE>
<%
	}
	catch (Exception ex)
	{
%><br><pre>Exception occurred while displaying output data:
<%
		StringWriter sw = new StringWriter ();
		ex.printStackTrace (new PrintWriter (sw));
		out.println (ex);
		out.println (sw.toString ());
%></pre>
<%
	}
%>


<%@ include file="footer.html" %>

</BODY>
</HTML>
