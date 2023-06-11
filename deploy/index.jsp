<%@ page language="java" session="false" %>
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

<TITLE> SOAP Service Tester </TITLE>

<META NAME="Author" CONTENT="Bogdan D.">
<META NAME="Description" CONTENT="SOAP Service Tester">
<META NAME="Keywords" CONTENT="SOAP, WSDL, service tester">
<META NAME="Language" CONTENT="en">
<META NAME="Generator" CONTENT="KWrite/Kate; www.kate-editor.org">

</HEAD><BODY>

<h1 class="c">SOAP Service Tester 0.1</h1>
<hr>

<p>
This application allows the user to view the operations of the Web service specified by the WSDL
document located at the given URL and to call them using user-provided data (HTTP headers,
SOAP envelope tags, SOAP header and SOAP body data).
</p>
<p>
The user-provided data is not validated - this allows many tests to performed, including tests
with invalid or non-standard HTTP headers, invalid XML data and even invalid SOAP envelope tags.
</p>
<p>
If the document specifies any XML Schema definitions, this application will try to provide
the user with sample XML data for each service's operation. If no such definitions are found,
the SOAP body field will be filled with the operation's name tags.
</p>

<form method="GET" action="show_operations.jsp" class="c">

WSDL location: <input type="text" size="60" name="SOAPTester_WSDL"><br>
<br>
<input type="reset" value="Clear">
<input type="submit" value="Find services">
</form>



<br><br>
<hr>
<h2 class="c">Useful links</h2>
<ul>
 <li><a href="http://rudy.mif.pg.gda.pl/~bogdro/soft" hreflang="en">SOAP Service Tester's homepage</a></li>
 <li><a href="http://www.w3.org/TR/wsdl" hreflang="en">WSDL Specification</a></li>
 <li><a href="http://www.w3.org/TR/xmlschema-1/" hreflang="en">XML Schema Specification</a></li>
 <li><a href="http://www.w3.org/standards/techs/soap" hreflang="en">SOAP page at W3C</a></li>
 <li><a href="http://www.w3.org/TR/2000/NOTE-SOAP-20000508/" hreflang="en">SOAP 1.1 Specification</a></li>
 <li><a href="http://tools.ietf.org/html/rfc2616" hreflang="en">HTTP 1.1 specification</a></li>
 <li><a href="http://hc.apache.org/" hreflang="en">Apache HttpComponents</a></li>
 <li><a href="http://xmlbeans.apache.org/" hreflang="en">Apache XMLBeans</a></li>
 <li><a href="http://axis.apache.org/" hreflang="en">Apache Axis</a></li>
 <li><a href="http://wsdl4j.sourceforge.net/" hreflang="en">WSDL4J</a></li>
 <li><a href="http://wscep.sourceforge.net/" hreflang="en">Web Service Console Eclipse Plugin</a></li>
</ul>

<%@ include file="footer.html" %>

</BODY>
</HTML>
