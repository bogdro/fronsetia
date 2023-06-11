<%@ page language="java" session="false" %>
<%@ page import="java.io.PrintWriter" %>
<%@ page import="java.io.StringWriter" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="java.util.HashSet" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.Set" %>
<%@ page import="BogDroSoft.soaptest.WSDLCheck" %>
<%@ page import="BogDroSoft.soaptest.RequestUtilities" %>
<% String wsdlLocation = request.getParameter (RequestUtilities.reqParNameWSDL); %>

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

<TITLE> SOAP Service Tester - <%= wsdlLocation %> </TITLE>

<META NAME="Author" CONTENT="Bogdan D.">
<META NAME="Description" CONTENT="SOAP Service Tester">
<META NAME="Keywords" CONTENT="SOAP, WSDL, service tester">
<META NAME="Language" CONTENT="en">
<META NAME="Generator" CONTENT="KWrite/Kate; www.kate-editor.org">

</HEAD><BODY>

<h1 class="c">SOAP Service Tester - operations available at<br>
	<a href="<%= wsdlLocation %>"><%= wsdlLocation %></a></h1>

<br>
<%
        /*
         * Predefined variables:
         * "request" is a subclass of javax.servlet.http.HttpServletRequest,
         * "response" is a subclass of javax.servlet.http.HttpServletResponse,
         * "pageContext" is a javax.servlet.jsp.PageContext,
         * "session" is a subclass of javax.servlet.http.HttpSession,
         * "application" is a javax.servlet.ServletContext,
         * "out" is a javax.servlet.jsp.JspWriter,
         * "config" is a javax.servlet.ServletConfig,
         * "page" is a java.lang.Object processing the current page.
         */

	WSDLCheck w = new WSDLCheck (wsdlLocation);
	Map<String, String> operationsAndXMLs = new HashMap<String, String> ();
	Map<String, String> operationsAndURLs = new HashMap<String, String> ();
	Set<String> operationNames = new HashSet<String> ();

	try
	{
		operationsAndXMLs = w.getOperations ();
		operationsAndURLs = w.getOperationURLs ();
		operationNames = operationsAndXMLs.keySet ();
	}
	catch (Exception ex)
	{
%>
		Exception caught while parsing the WSDL:
		<pre>
<%
		StringWriter sw = new StringWriter ();
		ex.printStackTrace (new PrintWriter (sw));
		out.println (ex);
%>

<%
		out.println (sw.toString ());
%>
		</pre>
<%
	}

	for ( String opName : operationNames )
	{
%>
		<a href="#<%= opName %>"><%= opName %></a><br>
<%
	}
	if ( operationNames.isEmpty () )
	{
%>
		<hr>
		<h2 class="c">SOAP Service Tester /
		WSDL4J could not find any operations in the given WSDL file.</h2>
		<br><br>
<%
	}

	for ( String opName : operationNames )
	{
%>
		<hr>
		<h2 class="c"><a name="<%= opName %>"><%= opName %></a></h2>
		<form method="POST" action="perform_operation.jsp">

			<div class="httpdatablock">
			<h3 class="important c">HTTP / HTTPS</h3>

			<input type="hidden" name="<%= RequestUtilities.reqParNameOpName %>"
				value="<%= opName %>">
			<input type="hidden" name="<%= RequestUtilities.reqParNameWSDL %>"
				value="<%= wsdlLocation %>">
			<input type="hidden" name="<%= RequestUtilities.reqParNameOpURL %>"
				value="<%= operationsAndURLs.get (opName) %>">

			The following headers are sent by default. To change them, unselect them
			here and put your vesrions in the <q>Additional headers</q>
			area below. If you put any of the selected headers in the
			<q>Additional headers</q>, you will get an error.
			<ul>
				<li><input type="checkbox"
					name="<%= RequestUtilities.reqParNameSendHdrContentLength %>"
					class="noindent"
					checked="checked"> Send default <code>Content-Length</code> header</li>
				<li><input type="checkbox"
					name="<%= RequestUtilities.reqParNameSendHdrHost %>"
					class="noindent"
					checked="checked"> Send default <code>Host</code> header</li>
				<li><input type="checkbox"
					name="<%= RequestUtilities.reqParNameSendHdrConnection %>"
					class="noindent"
					checked="checked"> Send default <code>Connection</code> header</li>
				<li><input type="checkbox"
					name="<%= RequestUtilities.reqParNameSendHdrUserAgent %>"
					class="noindent"
					checked="checked"> Send default <code>User-Agent</code> header</li>
				<li><input type="checkbox"
					name="<%= RequestUtilities.reqParNameSendHdrContentType %>"
					class="noindent"
					checked="checked"> Send default <code>Content-Type</code> header</li>
			</ul>
			<input type="checkbox" name="<%= RequestUtilities.reqParNameSendNoHdr %>"
				class="noindent"> Remove all default headers (overrides the above list)

			<br><br>
			Protocol properties (note that the protocol will always be sent
			as <code>NAME/MAJOR.MINOR</code>, like <code>HTTP/1.1</code>):<br>
			<div class="inputblock">
			Protocol name: <input type="text"
				name="<%= RequestUtilities.reqParNameProtoName %>"
				value="HTTP" size="60" class="noindent"><br>
			Protocol major version: <input type="text"
				name="<%= RequestUtilities.reqParNameProtoMajor %>"
				value="1" size="60" class="noindent"><br>
			Protocol minor version: <input type="text"
				name="<%= RequestUtilities.reqParNameProtoMinor %>"
				value="1" size="60" class="noindent"><br>
			Protocol method:
				<select name="<%= RequestUtilities.reqParNameProtoMethod %>">
					<option value="DELETE">DELETE</option>
					<option value="GET">GET</option>
					<option value="HEAD">HEAD</option>
					<option value="OPTIONS">OPTIONS</option>
					<option value="POST" selected="selected">POST</option>
					<option value="PUT">PUT</option>
					<option value="TRACE">TRACE</option>
				</select>
			</div>

			<br>
			Content-Type (without the charset):
			<input type="text" name="<%= RequestUtilities.reqParNameCType %>"
				value="<%= RequestUtilities.defContentType %>" size="60">
			<br><br>

			Additional headers (one per line, example: <code>SOAPAction: "/someaction"</code>):
			<br>
			<textarea name="<%= RequestUtilities.reqParNameHTTPHdrs %>" rows="5" cols="80">
SOAPAction:</textarea>

			<br><br>
			Authentication for the service:<br>
			<div class="inputblock">
			Username: <input type="text"
				name="<%= RequestUtilities.reqParNameHTTPUser %>"
				value="" size="60" class="noindent"><br>
			Password: <input type="text"
				name="<%= RequestUtilities.reqParNameHTTPPass %>"
				value="" size="60" class="noindent"><br>
			Workstation name (NT logins only): <input type="text"
				name="<%= RequestUtilities.reqParNameHTTPNTworkstation %>"
				value="" size="60" class="noindent"><br>
			Domain name (NT logins only): <input type="text"
				name="<%= RequestUtilities.reqParNameHTTPNTdomain %>"
				value="" size="60" class="noindent"><br>
			</div>
			<br>
			Proxy server (this is the proxy that <span class="important">THIS SERVER</span>,
			not your browser, should use to connect to the given service):
			<div class="inputblock">
			Host: <input type="text"
				name="<%= RequestUtilities.reqParNameProxyHost %>"
				value="" size="60" class="noindent"><br>
			Port: <input type="text"
				name="<%= RequestUtilities.reqParNameProxyPort %>"
				value="" size="60" class="noindent"><br>
			</div>
			<br>
			Proxy authentication:
			<div class="inputblock">
			Username: <input type="text"
				name="<%= RequestUtilities.reqParNameProxyUser %>"
				value="" size="60" class="noindent"><br>
			Password: <input type="text"
				name="<%= RequestUtilities.reqParNameProxyPass %>"
				value="" size="60" class="noindent"><br>
			Workstation name (NT logins only): <input type="text"
				name="<%= RequestUtilities.reqParNameProxyNTworkstation %>"
				value="" size="60" class="noindent"><br>
			Domain name (NT logins only): <input type="text"
				name="<%= RequestUtilities.reqParNameProxyNTdomain %>"
				value="" size="60" class="noindent"><br>
			</div>

			<input type="checkbox" name="<%= RequestUtilities.reqParNameAcceptAllSSL %>"
				checked="checked" class="noindent">Accept all SSL certificates
				and authentication (applies both to the service and to the proxy)
			<br><br>


			</div><br>

			<div class="soapdatablock">
			<h3 class="important c">SOAP</h3>

			<br><br>
			SOAP prologue (don't modify unless you know what you're doing):<br>
			<textarea name="<%= RequestUtilities.reqParNameSOAPPrologue %>" rows="2" cols="80">
&lt;soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/"&gt;
&lt;soapenv:Header&gt;</textarea>

			<br><br>
			SOAP header (usually you don't need to put anything here):<br>
			<textarea name="<%= RequestUtilities.reqParNameSOAPHeader %>" rows="5"
				cols="80"></textarea>

			<br><br>
			Middle (don't modify unless you know what you're doing):<br>
			<textarea name="<%= RequestUtilities.reqParNameSOAPMiddle %>" rows="2" cols="80">
&lt;/soapenv:Header&gt;
&lt;soapenv:Body&gt;</textarea>

			<br><br>
			SOAP body (put your XML data here):<br>
			<textarea name="<%= RequestUtilities.reqParNameSOAPBody %>" rows="20" cols="80"
				<%
					String operXML = operationsAndXMLs.get (opName);
					if ( operXML != null
						&& ! operXML.isEmpty () )
					{
				%>
><%= operXML %></textarea>
				<%
					}
					else
					{
				%>
>&lt;<%= opName %>&gt;&lt;/<%= opName %>&gt;</textarea>
				<%
					}
				%>

			<br><br>
			SOAP epilogue (don't modify unless you know what you're doing):<br>
			<textarea name="<%= RequestUtilities.reqParNameSOAPEpilogue %>" rows="2" cols="80">
&lt;/soapenv:Body&gt;
&lt;/soapenv:Envelope&gt;</textarea>

			</div>

			<br><br>
			Reply character set (if can't be detected automatically):
			<input type="text" name="<%= RequestUtilities.reqParNameCharset %>"
				value="<%= RequestUtilities.defaultCharset %>" size="60">
			<br><br>
			<input type="checkbox" name="<%= RequestUtilities.reqParNameSOAPRespSplit %>"
				checked="checked">Split response into lines

			<br><br>
			<div class="c"><input type="submit" value="<%= opName %>"></div>

		</form>
<%
	}
%>

<%@ include file="footer.html" %>

</BODY>
</HTML>
