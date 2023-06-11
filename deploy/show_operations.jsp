<%@ page language="java" session="false"
	import="BogDroSoft.soaptest.WSDLCheck,java.util.Map,java.util.Set" %>
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
	Map<String, String> operationsAndXMLs = w.getOperations ();
	Map<String, String> operationsAndURLs = w.getOperationURLs ();
	Set<String> operationNames = operationsAndXMLs.keySet ();

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

			<input type="hidden" name="SOAPTester_opName" value="<%= opName %>">
			<input type="hidden" name="SOAPTester_WSDL" value="<%= wsdlLocation %>">
			<input type="hidden" name="SOAPTester_opURL"
				value="<%= operationsAndURLs.get (opName) %>">

			Note: the following HTTP headers are always sent and can't be changed
			(any attempt to change them in the <q>Additional HTTP headers</q>
			area below will result in an exception):
			<ul>
				<li><code>Content-Length</code></li>
				<li><code>Host</code></li>
				<li><code>Connection</code></li>
				<li><code>User-Agent</code></li>
				<li><code>Content-Type</code> (also always sent, but can be changed below)</li>
			</ul>

			<br><br>
			HTTP Content-Type (without the charset):
			<input type="text" name="SOAPTester_CType" value="application/soap+xml" size="60">
			<br><br>

			Additional HTTP headers (one per line, example: <code>SOAPAction: "/someaction"</code>):
			<br>
			<textarea name="SOAPTester_httpHeaders" rows="5" cols="80">
SOAPAction:</textarea>

			<br><br>
			SOAP prologue (don't modify unless you know what you're doing):<br>
			<textarea name="SOAPTester_opPrologue" rows="2" cols="80">
&lt;soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/"&gt;
&lt;soapenv:Header&gt;</textarea>

			<br><br>
			SOAP header (usually you don't need to put anything here):<br>
			<textarea name="SOAPTester_opSoapHeader" rows="5" cols="80"></textarea>

			<br><br>
			Middle (don't modify unless you know what you're doing):<br>
			<textarea name="SOAPTester_opMiddle" rows="2" cols="80">
&lt;/soapenv:Header&gt;
&lt;soapenv:Body&gt;</textarea>

			<br><br>
			SOAP body (put your XML data here):<br>
			<textarea name="SOAPTester_opXML" rows="20" cols="80"
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
			<textarea name="SOAPTester_opEpilogue" rows="2" cols="80">
&lt;/soapenv:Body&gt;
&lt;/soapenv:Envelope&gt;</textarea>

			<br><br>
			<input type="checkbox" name="SOAPTester_splitResp" checked="checked"
				>Split response into lines

			<br><br>
			<div class="c"><input type="submit" value="<%= opName %>"></div>

		</form>
<%
	}
%>

<%@ include file="footer.html" %>

</BODY>
</HTML>
