<%@ page language="java" session="false" %>
<%@ page import="bogdrosoft.fronsetia.WSDLCheck" %>
<%@ page import="bogdrosoft.fronsetia.RequestUtilities" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="java.util.HashSet" %>
<%@ page import="java.util.Locale" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.Set" %>
<% String wsdlLocation = request.getParameter(RequestUtilities.REQ_PARAM_NAME_WSDL); %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
        "http://www.w3.org/TR/html4/loose.dtd">
<!--
Copyright (C) 2011-2025 Bogdan 'bogdro' Drozdowski, bogdro (at) users . sourceforge . net

This file is part of Fronsetia (Free Online Service Testing Application),
 a web application that allows testing webservices.

Project homepage: https://fronsetia.sourceforge.io/

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
<html lang="en">
<head profile="http://www.w3.org/2005/10/profile">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="Content-Language" content="en">
<meta http-equiv="Content-Style-Type" content="text/css">
<meta http-equiv="Cache-Control" content="no-cache, no-store, private">
<meta http-equiv="Pragma" content="no-cache">
<link rel="stylesheet" href="resources/fronsetia.css" type="text/css">
<link rel="icon" type="image/png" href="resources/img/fronsetia-icon.png">

<title> Fronsetia: <%= wsdlLocation %> </title>

<meta name="Author" content="Bogdan D.">
<meta name="Description" content="Fronsetia - Free Online Service Testing Application">
<meta name="Keywords" content="SOAP, WSDL, service tester">
<meta name="Language" content="en">
<meta name="Generator" content="KWrite/Kate; www.kate-editor.org">

</head><body>

<h1 class="c">Fronsetia - operations available at<br>
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

	WSDLCheck w = new WSDLCheck(wsdlLocation);
	Map<String, String> operationsAndXMLs = new HashMap<String, String>();
	Map<String, String> operationsAndURLs = new HashMap<String, String>();
	Set<String> operationNames = new HashSet<String>();

	try
	{
		operationsAndXMLs = w.getOperations();
		operationsAndURLs = w.getOperationURLs();
		operationNames = operationsAndXMLs.keySet();
	}
	catch (Throwable ex)
	{
		if (! wsdlLocation.toLowerCase(Locale.ENGLISH).endsWith("wsdl"))
		{
			try
			{
				w = new WSDLCheck(wsdlLocation + "?WSDL");
				operationsAndXMLs = w.getOperations();
				operationsAndURLs = w.getOperationURLs();
				operationNames = operationsAndXMLs.keySet();
			}
			catch (Throwable ex2)
			{
%>
				Exception caught while parsing the WSDL:
				<pre><%
				RequestUtilities.printException(ex2, out);
%></pre>
<%
			}
		}
		else
		{
			%>
			Exception caught while parsing the WSDL:
			<pre><%
			RequestUtilities.printException(ex, out);
%></pre>
<%
		}
	}

	for ( String opName : operationNames )
	{
%>
		<a href="#<%= opName %>"><%= opName %></a><br>
<%
	}
	if ( operationNames.isEmpty() )
	{
%>
		<hr>
		<h2 class="c">Fronsetia /
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

			<input type="hidden" name="<%= RequestUtilities.REQ_PARAM_NAME_OP_NAME %>"
				value="<%= opName %>">
			<input type="hidden" name="<%= RequestUtilities.REQ_PARAM_NAME_WSDL %>"
				value="<%= wsdlLocation %>">
			<input type="hidden" name="<%= RequestUtilities.REQ_PARAM_NAME_OP_URL %>"
				value="<%= operationsAndURLs.get(opName) %>">

			The following headers are sent by default. To change them, unselect them
			here and put your versions in the <q>Additional headers</q>
			area below. If you put any of the selected headers in the
			<q>Additional headers</q>, you will get an error.
			<ul>
				<li><input type="checkbox"
					name="<%= RequestUtilities.REQ_PARAM_NAME_SEND_HDR_CONTENT_LENGTH %>"
					class="noindent"
					checked="checked"> Send default <code>Content-Length</code> header</li>
				<li><input type="checkbox"
					name="<%= RequestUtilities.REQ_PARAM_NAME_SEND_HDR_HOST %>"
					class="noindent"
					checked="checked"> Send default <code>Host</code> header</li>
				<li><input type="checkbox"
					name="<%= RequestUtilities.REQ_PARAM_NAME_SEND_HDR_CONNECTION %>"
					class="noindent"
					checked="checked"> Send default <code>Connection</code> header</li>
				<li><input type="checkbox"
					name="<%= RequestUtilities.REQ_PARAM_NAME_SEND_HDR_USER_AGENT %>"
					class="noindent"
					checked="checked"> Send default <code>User-Agent</code> header</li>
				<li><input type="checkbox"
					name="<%= RequestUtilities.REQ_PARAM_NAME_SEND_HDR_CONTENT_TYPE %>"
					class="noindent"
					checked="checked"> Send default <code>Content-Type</code> header</li>
			</ul>
			<input type="checkbox" name="<%= RequestUtilities.REQ_PARAM_NAME_SEND_NO_HEADERS %>"
				class="noindent"> Remove all default headers (overrides the above list)

			<br><br>
			Protocol properties (note that the protocol will always be sent
			as <code>NAME/MAJOR.MINOR</code>, like <code>HTTP/1.1</code>):<br>
			<div class="inputblock">
			Protocol name: <input type="text"
				name="<%= RequestUtilities.REQ_PARAM_NAME_PROTO_NAME %>"
				value="HTTP" size="60" class="noindent"><br>
			Protocol major version: <input type="text"
				name="<%= RequestUtilities.REQ_PARAM_NAME_PROTO_MAJOR %>"
				value="1" size="60" class="noindent"><br>
			Protocol minor version: <input type="text"
				name="<%= RequestUtilities.REQ_PARAM_NAME_PROTO_MINOR %>"
				value="1" size="60" class="noindent"><br>
			Protocol method:
				<select name="<%= RequestUtilities.REQ_PARAM_NAME_PROTO_METHOD %>">
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
			<input type="text" name="<%= RequestUtilities.REQ_PARAM_NAME_CONTENT_TYPE %>"
				value="<%= RequestUtilities.DEFAULT_CONTENT_TYPE %>" size="60">
			<br><br>

			Additional headers (one per line, example: <code>SOAPAction: "/<%= opName %>"</code>):
			<br>
			<textarea name="<%= RequestUtilities.REQ_PARAM_NAME_HTTP_HEADERS %>" rows="5" cols="80">
SOAPAction: "/<%= opName %>"</textarea>

			<br><br>
			Authentication for the service:<br>
			<div class="inputblock">
			Username: <input type="text"
				name="<%= RequestUtilities.REQ_PARAM_NAME_HTTP_USER %>"
				value="" size="60" class="noindent"><br>
			Password: <input type="text"
				name="<%= RequestUtilities.REQ_PARAM_NAME_HTTP_PASSWORD %>"
				value="" size="60" class="noindent"><br>
			Workstation name (NT logins only): <input type="text"
				name="<%= RequestUtilities.REQ_PARAM_NAME_HTTP_NT_WORKSTATION %>"
				value="" size="60" class="noindent"><br>
			Domain name (NT logins only): <input type="text"
				name="<%= RequestUtilities.REQ_PARAM_NAME_HTTP_NT_DOMAIN %>"
				value="" size="60" class="noindent"><br>
			</div>
			<br>
			Proxy server (this is the proxy that <span class="important">THIS SERVER</span>,
			not your browser, should use to connect to the given service):
			<div class="inputblock">
			Host: <input type="text"
				name="<%= RequestUtilities.REQ_PARAM_NAME_PROXY_HOST %>"
				value="" size="60" class="noindent"><br>
			Port: <input type="text"
				name="<%= RequestUtilities.REQ_PARAM_NAME_PROXY_PORT %>"
				value="" size="60" class="noindent"><br>
			</div>
			<br>
			Proxy authentication:
			<div class="inputblock">
			Username: <input type="text"
				name="<%= RequestUtilities.REQ_PARAM_NAME_PROXY_USER %>"
				value="" size="60" class="noindent"><br>
			Password: <input type="text"
				name="<%= RequestUtilities.REQ_PARAM_NAME_PROXY_PASSWORD %>"
				value="" size="60" class="noindent"><br>
			Workstation name (NT logins only): <input type="text"
				name="<%= RequestUtilities.REQ_PARAM_NAME_PROXY_NT_WORKSTATION %>"
				value="" size="60" class="noindent"><br>
			Domain name (NT logins only): <input type="text"
				name="<%= RequestUtilities.REQ_PARAM_NAME_PROXY_NT_DOMAIN %>"
				value="" size="60" class="noindent"><br>
			</div>

			<input type="checkbox" name="<%= RequestUtilities.REQ_PARAM_NAME_ACCEPT_ALL_SSL %>"
				checked="checked" class="noindent">Accept all SSL certificates
				and authentication (applies both to the service and to the proxy)
			<br><br>


			</div><br>

			<div class="soapdatablock">
			<h3 class="important c">SOAP</h3>

			<br><br>
			SOAP prologue (don't modify unless you know what you're doing):<br>
			<textarea name="<%= RequestUtilities.REQ_PARAM_NAME_SOAP_PROLOGUE %>" rows="2" cols="80">
&lt;soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/"&gt;
&lt;soapenv:Header&gt;</textarea>

			<br><br>
			SOAP header (usually you don't need to put anything here):<br>
			<textarea name="<%= RequestUtilities.REQ_PARAM_NAME_SOAP_HEADER %>" rows="5"
				cols="80"></textarea>

			<br><br>
			Middle (don't modify unless you know what you're doing):<br>
			<textarea name="<%= RequestUtilities.REQ_PARAM_NAME_SOAP_MIDDLE %>" rows="2" cols="80">
&lt;/soapenv:Header&gt;
&lt;soapenv:Body&gt;</textarea>

			<br><br>
			SOAP body (put your XML data here):<br>
			<%
				String operXML = operationsAndXMLs.get(opName);
				if ( operXML == null || operXML.isEmpty() )
				{
					operXML = "&lt;" + opName + "&gt;&lt;/" + opName + "&gt;";
				}
			%>
			<textarea name="<%= RequestUtilities.REQ_PARAM_NAME_SOAP_BODY %>"
				rows="20" cols="80"><%= operXML %></textarea>
			<br><br>
			SOAP epilogue (don't modify unless you know what you're doing):<br>
			<textarea name="<%= RequestUtilities.REQ_PARAM_NAME_SOAP_EPILOGUE %>" rows="2" cols="80">
&lt;/soapenv:Body&gt;
&lt;/soapenv:Envelope&gt;</textarea>

			</div>

			<br><br>
			Response character set (if can't be detected automatically):
			<input type="text" name="<%= RequestUtilities.REQ_PARAM_NAME_CHARSET %>"
				value="<%= RequestUtilities.DEFAULT_CHARSET %>" size="60">
			<br><br>
			<input type="checkbox" name="<%= RequestUtilities.REQ_PARAM_NAME_SOAP_SPLIT_RESP %>"
				checked="checked">Split response into lines

			<br><br>
			<div class="c"><input type="submit" value="<%= opName %>"></div>

		</form>
<%
	}
%>

<%@ include file="footer.html" %>

</body>
</html>
