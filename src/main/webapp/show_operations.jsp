<%@ page language="java" session="false" %>
<%@ page import="bogdrosoft.fronsetia.EndpointParser" %>
<%@ page import="bogdrosoft.fronsetia.EndpointProcessor" %>
<%@ page import="bogdrosoft.fronsetia.RequestUtilities" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="java.util.HashSet" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Locale" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.Set" %>
<%
String endpointUrl = request.getParameter(RequestUtilities.REQ_PARAM_NAME_ENDPOINT);
%>
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

<title> Fronsetia: <%= endpointUrl %> </title>

<meta name="Author" content="Bogdan D.">
<meta name="Description" content="Fronsetia - Free Online Service Testing Application">
<meta name="Keywords" content="SOAP, WSDL, REST, service tester">
<meta name="Language" content="en">
<meta name="Generator" content="KWrite/Kate; www.kate-editor.org">

</head><body>

<h1 class="c">Fronsetia - operations available at<br>
	<a href="<%= endpointUrl %>"><%= endpointUrl %></a></h1>

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

	EndpointProcessor processor = new EndpointProcessor();
	EndpointParser parser = processor.parse(endpointUrl);
	Exception parseException = parser.getParsingException();
	if (parseException != null)
	{
%>
		Exception caught while parsing the endpoint:
		<pre><%
		RequestUtilities.printException(parseException, out);
%></pre>
<%
	}
	Set<String> operationNames = parser.getListOfOperations();

	for ( String opName : operationNames )
	{
%>
		<a href="#<%= opName %>"><%= opName %></a><br>
<%
	}
	if (operationNames.isEmpty())
	{
%>
		<hr>
		<h2 class="c">Fronsetia could not find any operations in the given endpoint.</h2>
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
			<input type="hidden" name="<%= RequestUtilities.REQ_PARAM_NAME_ENDPOINT %>"
				value="<%= endpointUrl %>">
			<input type="hidden" name="<%= RequestUtilities.REQ_PARAM_NAME_ENDPOINT_TYPE %>"
				value="<%= parser.getType() %>">
			<input type="hidden" name="<%= RequestUtilities.REQ_PARAM_NAME_OP_URL %>"
				value="<%= parser.getUrlForOperation(opName) %>">

			The following headers are sent by default. To change them, de-select them
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
			- or specify manually (overrides the drop-down value): <input type="text"
				name="<%= RequestUtilities.REQ_PARAM_NAME_PROTO_METHOD_INPUT %>"
				value="POST" size="60" class="noindent"><br>
			</div>

			<br>
			Content-Type (without the character set):
			<input type="text" name="<%= RequestUtilities.REQ_PARAM_NAME_CONTENT_TYPE %>"
				value="<%= parser.getDefaultContentType() %>" size="60">
			<br><br>

<%
			List<String> transportHeaders = parser.getListOfTransportHeadersForOperation(opName);
			int headerCount = transportHeaders.size();
%>
			Additional headers (one per line):
			<br>
			<textarea name="<%= RequestUtilities.REQ_PARAM_NAME_HTTP_HEADERS %>" rows="5" cols="80">
<%
			for (int i = 0; i < headerCount; i++)
			{
				out.println(transportHeaders.get(i));
			}
%>
</textarea>

			<br><br>
			HTTP Authentication for the service:<br>
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
			<br>
			Security protocol:
			<input type="text" name="<%= RequestUtilities.REQ_PARAM_NAME_SECURE_PROTOCOL %>"
				value="TLS" size="60">
			<br><br>


			</div><br>

			<div class="protodatablock">
			<h3 class="important c">Protocol</h3>

<%
			List<String> payloadPrologues = parser.getDefaultPayloadProloguesForOperation(opName);
			if (payloadPrologues != null)
			{
				int size = payloadPrologues.size();
				String prologue = "";
				if (size > 0)
				{
					prologue = payloadPrologues.get(0);
				}
%>
				<br><br>
				Payload prologue (don't modify unless you know what you're doing):<br>
				<textarea name="<%= RequestUtilities.REQ_PARAM_NAME_PAYLOAD_PROLOGUE %>" rows="2" cols="80">
<%= RequestUtilities.makeHTMLSafe(prologue) %></textarea>
<%
				if (size > 1)
				{
%>
					<br><br>
					Alternative payload prologues:<br>
<%
					for (int i = 1; i < size; i++)
					{
%>
						<pre><%= RequestUtilities.makeHTMLSafe(payloadPrologues.get(i)) %></pre>
<%
					}
				}
			}
			String payloadHeader = parser.getDefaultPayloadHeaderForOperation(opName);
			if (payloadHeader != null)
			{
%>
			<br><br>
			Payload header (usually you don't need to put anything here):<br>
			<textarea name="<%= RequestUtilities.REQ_PARAM_NAME_PAYLOAD_HEADER %>" rows="5"
				cols="80"><%= RequestUtilities.makeHTMLSafe(payloadHeader) %></textarea>
<%
			}
			String payloadMiddle = parser.getDefaultPayloadMiddleForOperation(opName);
			if (payloadMiddle != null)
			{
%>
			<br><br>
			Payload middle (don't modify unless you know what you're doing):<br>
			<textarea name="<%= RequestUtilities.REQ_PARAM_NAME_PAYLOAD_MIDDLE %>" rows="2" cols="80">
<%= RequestUtilities.makeHTMLSafe(payloadMiddle) %></textarea>

<%
			}
			String payloadBody = parser.getDefaultPayloadForOperation(opName);
			if (payloadBody != null)
			{
%>
			<br><br>
			Payload body (put your data here):<br>
			<textarea name="<%= RequestUtilities.REQ_PARAM_NAME_PAYLOAD_BODY %>"
				rows="20" cols="80"><%= RequestUtilities.makeHTMLSafe(payloadBody) %></textarea>
<%
			}
			String payloadEpilogue = parser.getDefaultPayloadEpilogueForOperation(opName);
			if (payloadEpilogue != null)
			{
%>
			<br><br>
			Payload epilogue (don't modify unless you know what you're doing):<br>
			<textarea name="<%= RequestUtilities.REQ_PARAM_NAME_PAYLOAD_EPILOGUE %>" rows="2" cols="80">
<%= RequestUtilities.makeHTMLSafe(payloadEpilogue) %></textarea>
<%
			}
%>
			</div>

			<br><br>
			Response character set (if can't be detected automatically):
			<input type="text" name="<%= RequestUtilities.REQ_PARAM_NAME_CHARSET %>"
				value="<%= RequestUtilities.DEFAULT_CHARSET %>" size="60">
			<br><br>
			<input type="checkbox" name="<%= RequestUtilities.REQ_PARAM_NAME_SPLIT_RESP %>"
				checked="checked">Split response into lines

			<br><br>
			<div class="c"><input type="submit" value="Call <%= opName %>"></div>

		</form>
<%
	}
%>

<%@ include file="footer.html" %>

</body>
</html>
