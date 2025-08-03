# Fronsetia #

## Description ##

Fronsetia (Free Online Service Testing Application) is a web application that allows testing webservices.

Fronsetia allows you to:

-   send customised requests to a webservice,
-   read WSDL definitions,
-   override or remove the standard HTTP headers from the request to be sent,
-   set any other HTTP header you wish,
-   set the protocol name and version,
-   use protocol methods other than the regular POST,
-   set the sent Content-Type,
-   authenticate to the webservice,
-   use a proxy and authenticate to the proxy,
-   accept any TLS/SSL certificate (useful for test environments),
-   manually set the SOAP content (prologue, headers, body and the XML before them, after them and in between),
-   set the character set of the request,
-   view the SOAP response,
-   view the response's HTTP code and status line,
-   view the response's HTTP headers,
-   verify if there was a SOAP Fault in the response,
-   pretty-print the response by splitting it after closing tags.

You can think of Fronsetia as a simple version of SoapUI
(<https://www.soapui.org/>) on the web, or a simple and free and
open-source version of the wls_utc utility (but less powerful -
for example, it doesn't interpret the service's schema to provide
separate fields for various parameters).

Features:

-   supports REST Web Services,
-   supports Web Services Description Language (WSDL),
-   supports Simple Object Access Protocol (SOAP),
-   supports Hypertext Transfer Protocol (HTTP),
-   supports HTTP Authentication,
-   supports the same versions of Transport Layer Security (TLS) and Secure Sockets Layer (SSL) as the server the application will be running on,
-   supports HTTP Proxying,
-   supports the the Jakarta EE platform version 10 (modern Java servers),
-   portable and system-independent.

Fronsetia was previously called SOAPServiceTester.

Author: Bogdan Drozdowski, bogdro &AT% users . sourceforge . net

License: AGPLv3+

Project homepage: <https://fronsetia.sourceforge.io/>

[![SonarQube Cloud](https://sonarcloud.io/images/project_badges/sonarcloud-light.svg)](https://sonarcloud.io/summary/new_code?id=bogdro_fronsetia)

# WARNING #

The `dev` branch may contain code which is a work in progress and committed just for tests. The code here may not work properly or even compile.

The `master` branch may contain code which is committed just for quality tests.

The tags, matching the official packages on SourceForge, should be the most reliable points.
