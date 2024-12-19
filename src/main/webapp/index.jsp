<%@ page language="java" session="false" %>
<%@ page import="bogdrosoft.fronsetia.RequestUtilities" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
        "http://www.w3.org/TR/html4/loose.dtd">
<!--
Copyright (C) 2011-2024 Bogdan 'bogdro' Drozdowski, bogdro (at) users . sourceforge . net

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
<link rel="stylesheet" href="resources/fronsetia.css" type="text/css">
<link rel="icon" type="image/png" href="resources/img/fronsetia-icon.png">

<title> Fronsetia </title>

<meta name="Author" content="Bogdan D.">
<meta name="Description" content="Fronsetia - Free Online Service Testing Application">
<meta name="Keywords" content="SOAP, WSDL, service tester">
<meta name="Language" content="en">
<meta name="Generator" content="KWrite/Kate; www.kate-editor.org">

</head><body>

<h1 class="c">
<img src="resources/img/fronsetia-icon.png" class="vert_mid" alt="[icon]">
Fronsetia
</h1>
<hr>

<p>
This application allows the user to view the operations of the Web service specified by the
<acronym title="Web Services Description Language" lang="en">WSDL</acronym>
document located at the given URL and to call them using user-provided data
(<acronym title="Hypertext Transfer Protocol" lang="en">HTTP</acronym> headers,
<acronym title="Simple Object Access Protocol" lang="en">SOAP</acronym> envelope tags,
SOAP header and SOAP body data).
</p>
<p>
The user-provided data is not validated - this allows many tests to performed,
including tests with invalid or non-standard HTTP headers, invalid
<acronym title="Extensible Markup Language" lang="en">XML</acronym> data
and even invalid SOAP envelope tags.
</p>
<p>
If the document specifies any XML Schema definitions, this application will try to provide
the user with sample XML data for each service's operation. If no such definitions are found,
the SOAP body field will be filled with the operation's name tags.
</p>

<form method="GET" action="show_operations.jsp" class="c">

WSDL location: <input type="text" size="60" name="<%= RequestUtilities.REQ_PARAM_NAME_WSDL %>"><br>
<br>
<input type="reset" value="Clear">
<input type="submit" value="Find services">
</form>

<br><br>
<hr>
<h2 class="c">Useful links</h2>
<ul>
 <li><a href="https://www.w3.org/TR/wsdl/" hreflang="en">WSDL Specification</a></li>
 <li><a href="https://www.w3.org/TR/xmlschema-1/" hreflang="en">XML Schema Specification</a></li>
 <li><a href="http://www.w3.org/TR/SOAP" hreflang="en">SOAP 1.1 Specification</a></li>
 <li><a href="https://datatracker.ietf.org/doc/html/rfc2616" hreflang="en">HTTP 1.1 specification</a></li>
 <li><a href="https://hc.apache.org/" hreflang="en">Apache HttpComponents</a></li>
 <li><a href="https://xmlbeans.apache.org/" hreflang="en">Apache XMLBeans</a></li>
 <li><a href="https://axis.apache.org/" hreflang="en">Apache Axis</a></li>
 <li><a href="https://ws.apache.org/axiom/" hreflang="en">Apache Axiom</a></li>
 <li><a href="https://axis.apache.org/axis2/java/core/" hreflang="en">Apache Axis2</a></li>
 <li><a href="http://wsdl4j.sourceforge.net/" hreflang="en">WSDL4J</a></li>
 <li><a href="https://wscep.sourceforge.net/" hreflang="en">Web Service Console Eclipse Plugin</a></li>
</ul>

<%@ include file="footer.html" %>

</body>
</html>
