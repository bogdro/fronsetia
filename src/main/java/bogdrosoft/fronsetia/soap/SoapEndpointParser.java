/*
 * SoapEndpointParser - a class that checks WSDL files.
 *
 * Copyright (C) 2011-2025 Bogdan 'bogdro' Drozdowski, bogdro (at) users . sourceforge . net
 *
 * This file is part of Fronsetia (Free Online Service Testing Application),
 *  a web application that allows testing webservices.
 *
 * Project homepage: https://fronsetia.sourceforge.io/
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see http://www.gnu.org/licenses/.
 */

package bogdrosoft.fronsetia.soap;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.namespace.QName;

import javax.wsdl.Binding;
import javax.wsdl.Definition;
import javax.wsdl.Input;
import javax.wsdl.Message;
import javax.wsdl.Operation;
import javax.wsdl.Port;
import javax.wsdl.PortType;
import javax.wsdl.Service;
import javax.wsdl.Types;
import javax.wsdl.WSDLException;
import javax.wsdl.extensions.schema.Schema;
import javax.wsdl.extensions.schema.SchemaReference;
import javax.wsdl.extensions.soap.SOAPAddress;
import javax.wsdl.extensions.soap12.SOAP12Address;
import javax.wsdl.factory.WSDLFactory;
import javax.wsdl.xml.WSDLReader;

import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.SchemaTypeSystem;
import org.apache.xmlbeans.XmlBeans;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
import org.apache.xmlbeans.impl.xb.xsdschema.SchemaDocument;
import org.apache.xmlbeans.impl.xsd2inst.SampleXmlUtil;

import org.w3c.dom.Element;

import bogdrosoft.fronsetia.EndpointParser;
import bogdrosoft.fronsetia.EndpointType;
import bogdrosoft.fronsetia.RequestUtilities;

/**
 * SoapEndpointParser - a class that checks WSDL files.
 * @author Bogdan 'bogdro' Drozdowski, bogdro (at) users . sourceforge . net
 */
public class SoapEndpointParser implements EndpointParser
{
	private WSDLReader wsdlReader = null;
	private SchemaType[] globalElems = null;
	private String wsdlLocation = null;
	private Map<String, String> operationsAndXMLs = Collections.emptyMap();
	private Map<String, String> operationsAndURLs = Collections.emptyMap();
	private Exception parseException;

	@Override
	public Set<String> getListOfOperations()
	{
		return operationsAndURLs.keySet();
	}

	@Override
	public List<String> getListOfTransportHeadersForOperation(String operationName)
	{
		return Collections.singletonList("SOAPAction: \"/" + operationName + "\"");
	}

	@Override
	public String getUrlForOperation(String operationName)
	{
		return operationsAndURLs.get(operationName);
	}

	@Override
	public List<String> getDefaultPayloadProloguesForOperation(String operationName)
	{
		return Arrays.asList(
			"<soapenv:Envelope xmlns:soapenv=\"http://www.w3.org/2003/05/soap-envelope\">\n<soapenv:Header>",
			"<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\">\n<soapenv:Header>"
		);
	}

	@Override
	public String getDefaultPayloadHeaderForOperation(String operationName)
	{
		return "";
	}

	@Override
	public String getDefaultPayloadMiddleForOperation(String operationName)
	{
		return "</soapenv:Header>\n<soapenv:Body>";
	}

	@Override
	public String getDefaultPayloadForOperation(String operationName)
	{
		String operXML = operationsAndXMLs.get(operationName);
		if ( operXML == null || operXML.isEmpty() )
		{
			operXML = "<" + operationName + "></" + operationName + ">";
		}
		return operXML;
	}

	@Override
	public String getDefaultPayloadEpilogueForOperation(String operationName)
	{
		return "</soapenv:Body>\n</soapenv:Envelope>";
	}

	@Override
	public Exception getParsingException()
	{
		return parseException;
	}

	@Override
	public void parse(String url)
	{
		try
		{
			wsdlLocation = url;
			parseException = null;
			operationsAndURLs = new HashMap<String, String>();
			operationsAndXMLs = processWSDL();
		}
		catch (WSDLException e)
		{
			parseException = e;
		}
	}

	@Override
	public String getDefaultContentType()
	{
		return "application/soap+xml";//"text/xml";
	}

	@Override
	public EndpointType getType()
	{
		return EndpointType.SOAP;
	}

	/**
	 * Processes the WSDL file and returns the operations from the file.
	 * @return a Map of the operations. Operation names are the keys,
	 * 	skeleton XML strings are the values.
	 */
	private Map<String, String> processWSDL() throws WSDLException
	{
		Map<String, String> ret = new HashMap<String, String>(10);
		if (wsdlReader == null)
		{
			wsdlReader = WSDLFactory.newInstance().newWSDLReader();
			wsdlReader.setFeature("javax.wsdl.verbose", false);
			wsdlReader.setFeature("javax.wsdl.importDocuments", true);
		}
		Definition def = wsdlReader.readWSDL(wsdlLocation);
		if (def == null)
		{
			return ret;
		}
		Map<?,?> servicesMap = def.getAllServices();
		if (servicesMap == null)
		{
			return ret;
		}
		Types t = def.getTypes();
		Set<String> schemaLocations = new HashSet<String>(10);
		Set<Element> schemaElements = new HashSet<Element>(10);
		if (t != null)
		{
			List<?> extElems = t.getExtensibilityElements();
			if (extElems != null)
			{
				for (Object o : extElems)
				{
					if (o == null)
					{
						continue;
					}
					if (o instanceof SchemaReference)
					{
						schemaLocations.add(((SchemaReference) o).getSchemaLocationURI());
					}
					else if (o instanceof Schema)
					{
						Schema si = (Schema) o;
						schemaElements.add(si.getElement());
						Map<?,?> schImports = si.getImports();
						if (schImports != null)
						{
							Collection<?> schImportsValues = schImports.values();
							if (schImportsValues != null)
							{
								for (Object el : schImportsValues)
								{
									addSchemaReference(schemaLocations, el);
								}
							}
						}
						List<?> schIncludes = si.getIncludes();
						addSchemaReference(schemaLocations, schIncludes);
					}
				}
			}
		}
		for (Map.Entry<?,?> sn : servicesMap.entrySet())
		{
			Object s = sn.getValue();
			if (s instanceof Service)
			{
				Map<?,?> ports = ((Service) s).getPorts();
				for (Map.Entry<?,?> pn : ports.entrySet())
				{
					Object p = pn.getValue();
					if (p instanceof Port)
					{
						Port currPort = (Port) p;
						Binding b = currPort.getBinding();
						if (b == null)
						{
							continue;
						}
						PortType pt = b.getPortType();
						if (pt == null)
						{
							continue;
						}
						List<?> operations = pt.getOperations();
						if (operations == null)
						{
							continue;
						}
						List<?> portExtElems = currPort.getExtensibilityElements();
						for (Object op : operations)
						{
							if (op instanceof Operation)
							{
								Operation oper = (Operation) op;
								// first, put just the operation names
								// and root elements
								String opName = oper.getName();
								Input opInput = oper.getInput();
								if (opName == null
									|| opInput == null)
								{
									continue;
								}
								Message mess = opInput.getMessage();
								if (mess == null)
								{
									continue;
								}
								QName messName = mess.getQName();
								if (messName == null)
								{
									continue;
								}
								String messLocalName = messName.getLocalPart();
								if (messLocalName == null)
								{
									continue;
								}
								ret.put (opName, messLocalName);
								if (portExtElems != null)
								{
									for (Object extel : portExtElems)
									{
										if (extel instanceof SOAPAddress)
										{
											operationsAndURLs.put (
												opName,
												((SOAPAddress) extel).getLocationURI());
										}
										else if (extel instanceof SOAP12Address)
										{
											operationsAndURLs.put (
												opName,
												((SOAP12Address) extel).getLocationURI());
										}
									}
								}
							}
						}
					}
				}
			}
		}
		// now change the root elements in the map to XML templates
		for (Map.Entry<String, String> opName : ret.entrySet())
		{
			ret.put(
				opName.getKey(),
				processXSD(schemaLocations, ret.get(opName.getValue()), schemaElements)
			);
		}
		return ret;
	}

	/**
	 * Processes the schema files and generates a template XML with the given root element.
	 * @param schemaLocations The schema files' locations.
	 * @param rootElem The name of the root element in the generated XML template.
	 * @return the generated XML template.
	 */
	String processXSD(Set<String> schemaLocations,
			String rootElem, Set<Element> schemaElements)
	{
		if (globalElems == null)
		{
			List<XmlObject> sdocs = new ArrayList<XmlObject>();
			XmlOptions parseOptions = new XmlOptions();
			parseOptions.setLoadLineNumbers();
			parseOptions.setLoadMessageDigest();

			if (schemaLocations != null)
			{
				for (String schemaFile : schemaLocations)
				{
					try
					{
						sdocs.add(XmlObject.Factory.parse(
								new File(schemaFile),
								parseOptions));
					}
					catch (Exception e)
					{
						/* ignore, proceed with the next one */
					}
				}
			}
			if (schemaElements != null)
			{
				for (Element schemaElement : schemaElements)
				{
					try
					{
						sdocs.add(SchemaDocument.Factory.parse(
								schemaElement,
								parseOptions));
					}
					catch (Exception e)
					{
						/* ignore, proceed with the next one */
					}
				}
			}

			XmlObject[] schemas = sdocs.toArray(new XmlObject[] {});
			SchemaTypeSystem sts = null;
			if (schemas.length != 0)
			{
				XmlOptions compileOptions = new XmlOptions();
				compileOptions.setCompileDownloadUrls();

				try
				{
					sts = XmlBeans.compileXsd(schemas,
						XmlBeans.getBuiltinTypeSystem(),
						compileOptions);
				}
				catch (Exception e)
				{
					/* ignore for now, use the default */
				}
			}

			if (sts == null)
			{
				return RequestUtilities.EMPTY_STR;
			}
			globalElems = sts.documentTypes();
		}
		if (rootElem != null && globalElems != null)
		{
			for (SchemaType st : globalElems)
			{
				QName elementName = st.getDocumentElementName();
				if (elementName == null)
				{
					continue;
				}
				String nameLocalPart = elementName.getLocalPart();
				if (nameLocalPart != null && nameLocalPart.equals(rootElem))
				{
					return SampleXmlUtil.createSampleForType(st);
				}
			}
		}
		return RequestUtilities.EMPTY_STR;
	}

	void addSchemaReference(Set<String> schemaLocations, Object list)
	{
		if (list instanceof List)
		{
			List<?> schemaList = (List<?>)list;
			int size = schemaList.size();
			for (int i = 0; i < size; i++)
			{
				Object ref = schemaList.get(i);
				if (ref instanceof SchemaReference)
				{
					schemaLocations.add(
						((SchemaReference) ref).getSchemaLocationURI()
					);
				}
			}
		}
	}
}
