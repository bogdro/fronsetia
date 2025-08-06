<%@ page language="java" session="false" %>
<%@ page import="bogdrosoft.fronsetia.EndpointType" %>
<%@ page import="bogdrosoft.fronsetia.OperationLauncher" %>
<%@ page import="bogdrosoft.fronsetia.RequestUtilities" %>
<%@ page import="bogdrosoft.fronsetia.ResponseInterpreter" %>
<%@ page import="bogdrosoft.fronsetia.ResponseProcessor" %>
<%@ page import="java.util.List" %>
<%@ page import="org.apache.http.HeaderIterator" %>
<%
String endpointLocation = request.getParameter(RequestUtilities.REQ_PARAM_NAME_ENDPOINT);
EndpointType endpointType = EndpointType.valueOf(request.getParameter(RequestUtilities.REQ_PARAM_NAME_ENDPOINT_TYPE));
String opName = RequestUtilities.getParameter(request, RequestUtilities.REQ_PARAM_NAME_OP_NAME);
String payloadPrologue = RequestUtilities.getParameter(request, RequestUtilities.REQ_PARAM_NAME_PAYLOAD_PROLOGUE);
String payloadHeader = RequestUtilities.getParameter(request, RequestUtilities.REQ_PARAM_NAME_PAYLOAD_HEADER);
String payloadMiddle = RequestUtilities.getParameter(request, RequestUtilities.REQ_PARAM_NAME_PAYLOAD_MIDDLE);
String payloadBody = RequestUtilities.getParameter(request, RequestUtilities.REQ_PARAM_NAME_PAYLOAD_BODY);
String payloadEpilogue = RequestUtilities.getParameter(request, RequestUtilities.REQ_PARAM_NAME_PAYLOAD_EPILOGUE);
String payloadContentType = RequestUtilities.getParameter(request, RequestUtilities.REQ_PARAM_NAME_CONTENT_TYPE);
String proxyPort = RequestUtilities.getParameter(request, RequestUtilities.REQ_PARAM_NAME_PROXY_PORT);
String method = RequestUtilities.getParameter(request, RequestUtilities.REQ_PARAM_NAME_PROTO_METHOD_INPUT);
if (method.isEmpty())
{
	method = RequestUtilities.getParameter(request, RequestUtilities.REQ_PARAM_NAME_PROTO_METHOD);
}

ResponseProcessor processor = new ResponseProcessor();
ResponseInterpreter interpreter = processor.getResponseInterpreter(endpointType);
OperationLauncher ol = new OperationLauncher();
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

<title> Fronsetia: <%= RequestUtilities.makeHTMLSafe(opName) %>:
	<%= RequestUtilities.makeHTMLSafe(endpointLocation) %> </title>

<meta name="Author" content="Bogdan D.">
<meta name="Description" content="Fronsetia - Free Online Service Testing Application">
<meta name="Keywords" content="SOAP, WSDL, REST, service tester">
<meta name="Language" content="en">
<meta name="Generator" content="KWrite/Kate; www.kate-editor.org">

</head><body>

<h1 class="c">Fronsetia - result of operation</h1>
<hr>
Endpoint location: <a href="<%= RequestUtilities.makeHTMLSafe(endpointLocation) %>"
	id="<%= RequestUtilities.REQ_PARAM_NAME_ENDPOINT %>"
	><%= RequestUtilities.makeHTMLSafe(endpointLocation) %></a><br>

Operation name: <code id="<%= RequestUtilities.REQ_PARAM_NAME_OP_NAME %>"
	><%= RequestUtilities.makeHTMLSafe(opName) %></code><br>

Protocol name and version: <code id="<%= RequestUtilities.REQ_PARAM_NAME_PROTO_NAME %>"
	><%= RequestUtilities.makeHTMLSafe(RequestUtilities.getParameter(request, RequestUtilities.REQ_PARAM_NAME_PROTO_NAME))
	%>/<%= RequestUtilities.makeHTMLSafe(RequestUtilities.getParameter(request, RequestUtilities.REQ_PARAM_NAME_PROTO_MAJOR))
	%>.<%= RequestUtilities.makeHTMLSafe(RequestUtilities.getParameter(request, RequestUtilities.REQ_PARAM_NAME_PROTO_MINOR))
	%></code><br>

Protocol method: <code id="<%= RequestUtilities.REQ_PARAM_NAME_PROTO_METHOD %>"
	><%= RequestUtilities.makeHTMLSafe(method) %></code><br>

Protocol authentication: user=<code id="<%= RequestUtilities.REQ_PARAM_NAME_HTTP_USER %>"
	><%= RequestUtilities.makeHTMLSafe(RequestUtilities.getParameter(request, RequestUtilities.REQ_PARAM_NAME_HTTP_USER)) %></code>,
	password=<code id="<%= RequestUtilities.REQ_PARAM_NAME_HTTP_PASSWORD %>"
	><%= RequestUtilities.makeHTMLSafe(RequestUtilities.getParameter(request, RequestUtilities.REQ_PARAM_NAME_HTTP_PASSWORD)) %></code>,
	NT workstation=<code id="<%= RequestUtilities.REQ_PARAM_NAME_HTTP_NT_WORKSTATION %>"
	><%= RequestUtilities.makeHTMLSafe(RequestUtilities.getParameter(request, RequestUtilities.REQ_PARAM_NAME_HTTP_NT_WORKSTATION)) %></code>,
	NT domain=<code id="<%= RequestUtilities.REQ_PARAM_NAME_HTTP_NT_DOMAIN %>"
	><%= RequestUtilities.makeHTMLSafe(RequestUtilities.getParameter(request, RequestUtilities.REQ_PARAM_NAME_HTTP_NT_DOMAIN)) %></code>
	<br>

Proxy: <code id="<%= RequestUtilities.REQ_PARAM_NAME_PROXY_HOST %>"
	><%= RequestUtilities.makeHTMLSafe(RequestUtilities.getParameter(request, RequestUtilities.REQ_PARAM_NAME_PROXY_HOST)) %>
<%
	if (! proxyPort.isEmpty())
	{
%>
		:<%= proxyPort %>
<%
	}
%>
</code><br>
Proxy authentication: user=<code id="<%= RequestUtilities.REQ_PARAM_NAME_PROXY_USER %>"
	><%= RequestUtilities.makeHTMLSafe(RequestUtilities.getParameter(request, RequestUtilities.REQ_PARAM_NAME_PROXY_USER)) %></code>,
	password=<code id="<%= RequestUtilities.REQ_PARAM_NAME_PROXY_PASSWORD %>"
	><%= RequestUtilities.makeHTMLSafe(RequestUtilities.getParameter(request, RequestUtilities.REQ_PARAM_NAME_PROXY_PASSWORD)) %></code>,
	NT workstation=<code id="<%= RequestUtilities.REQ_PARAM_NAME_PROXY_NT_WORKSTATION %>"
	><%= RequestUtilities.makeHTMLSafe(RequestUtilities.getParameter(request, RequestUtilities.REQ_PARAM_NAME_PROXY_NT_WORKSTATION)) %></code>,
	NT domain=<code id="<%= RequestUtilities.REQ_PARAM_NAME_PROXY_NT_DOMAIN %>"
	><%= RequestUtilities.makeHTMLSafe(RequestUtilities.getParameter(request, RequestUtilities.REQ_PARAM_NAME_PROXY_NT_DOMAIN)) %></code>
	<br>
User-defined request headers:
<pre id="<%= RequestUtilities.REQ_PARAM_NAME_HTTP_HEADERS %>">
<%
	try
	{
		ol.prepare(request);
		ol.perform(request, interpreter);
		HeaderIterator reqhi = ol.getReqHeaders();
		out.flush();
		while (reqhi.hasNext())
		{
			out.println(reqhi.next());
			out.flush();
		}
	}
	catch (Throwable ex)
	{
%>
		Exception occurred while performing the operation or displaying input parameters:
<%
		RequestUtilities.printException(ex, out);
	}
%></pre>
Operation input (HTTP body):
<pre id="<%= RequestUtilities.REQ_PARAM_NAME_PAYLOAD_BODY %>">
<%= RequestUtilities.makeHTMLSafe(payloadPrologue) +
RequestUtilities.makeHTMLSafe(payloadHeader) +
RequestUtilities.makeHTMLSafe(payloadMiddle) +
RequestUtilities.makeHTMLSafe(payloadBody) +
RequestUtilities.makeHTMLSafe(payloadEpilogue) %></pre>

Expected output encoding (used only if can't be detected automatically):
 <code id="<%= RequestUtilities.REQ_PARAM_NAME_CHARSET %>"
	><%= RequestUtilities.makeHTMLSafe(RequestUtilities.getParameter(request, RequestUtilities.REQ_PARAM_NAME_CHARSET)) %></code>





<br>
<hr>
<h2 class="c">Operation result:</h2>


HTTP response code: <code id="<%= RequestUtilities.RESP_FIELD_ID_CODE %>"
	><%= (ol.getStatusCode() >= 0)? String.valueOf(ol.getStatusCode()) : "" %></code><br>
HTTP response line: <code id="<%= RequestUtilities.RESP_FIELD_ID_STATUS_LINE %>"
	><%= ol.getStatusLine() %></code><br>
<%
	// flush the current page output to the user:
	out.flush();
	try
	{
		String resp = ol.getResponseBody();
		try
		{
			interpreter.parseResponse(resp);
%>
			Fault found in the response:
<%
			if (interpreter.hasFault())
			{
%>
				<span id="<%= RequestUtilities.RESP_FIELD_ID_HAS_FAULT %>"
					class="fault">YES</span><br>
<%
			}
			else
			{
%>
				<span id="<%= RequestUtilities.RESP_FIELD_ID_HAS_FAULT %>"
					class="nofault">NO</span><br>
<%
			}
%>
			Response top elements: <span
				id="<%= RequestUtilities.RESP_FIELD_ID_BODY_ELEMENTS %>">
<%
			List<String> respTypes = interpreter.getBodyElements();
			if (respTypes != null)
			{
				int respTypeSize = respTypes.size();
				for ( int i = 0; i < respTypeSize; i++ )
				{
%>
					<code><%= respTypes.get(i) %></code>
<%
					if (i < respTypeSize - 1)
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
			Unable to get response status and type - parser libraries not installed or not usable.
<%
		}
		catch (Throwable ex)
		{
%>
			Unable to get response status and type.
<%
		}
%>
<br>
HTTP response headers:
<%
		out.flush();
		HeaderIterator hi = ol.getRespHeaders();
		if (hi != null)
		{
%>
<pre id="<%= RequestUtilities.RESP_FIELD_ID_HEADERS %>">
<%
			while (hi.hasNext())
			{
				out.println(hi.next());
				out.flush();
			}
%></pre>
<%
		}
%>
<br>
HTTP response body:
<pre id="<%= RequestUtilities.RESP_FIELD_ID_BODY %>">
<%
		out.println (RequestUtilities.makeHTMLSafe(resp));
%></pre>
<%
	}
	catch (Throwable ex)
	{
%><br><pre>Exception occurred while displaying output data:
<%
		RequestUtilities.printException(ex, out);
%></pre>
<%
	}
%>

<%@ include file="footer.html" %>

</body>
</html>
