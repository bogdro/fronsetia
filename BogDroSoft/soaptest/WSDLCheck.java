/*
 * WSDLCheck - a class that checks WSDL files.
 *
 * Author: Bogdan 'bogdro' Drozdowski, bogdandr <at> op . pl
 *
 *    SOAP Service Tester - an application for low-level testing of SOAP Services.
 *    Copyright (C) 2011 Bogdan 'bogdro' Drozdowski, bogdandr <at> op . pl
 *
 *    This program is free software: you can redistribute it and/or modify
 *    it under the terms of the GNU Affero General Public License as
 *    published by the Free Software Foundation, either version 3 of the
 *    License, or (at your option) any later version.
 *
 *    This program is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *    GNU Affero General Public License for more details.
 *
 *    You should have received a copy of the GNU Affero General Public License
 *    along with this program.  If not, see http://www.gnu.org/licenses/.
 *
 */

package BogDroSoft.soaptest;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
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

/**
 * WSDLCheck - a class that checks WSDL files.
 * @author Bogdan 'bogdro' Drozdowski, bogdandr <at> op . pl
 */
public class WSDLCheck
{
	private static final String empty = "";

	private WSDLReader wsdlReader = null;
	private SchemaType[] globalElems = null;
	private static String wsdlLocation = null;
	private Map<String, String> operationsAndXMLs = null;
	private Map<String, String> operationsAndURLs = null;

	/**
	 * Creates an instance of WSDLCheck.
	 * @param wsdlAddress the location of the WSDL file to process.
	 */
	public WSDLCheck (String wsdlAddress)
	{
		wsdlLocation = wsdlAddress;
	}

	/**
	 * Gets the oprations from the given WSDL file.
	 * @return a Map of the operations. Operation names are the keys,
	 * 	skeleton XML strings are the values.
	 */
	public Map<String, String> getOperations () throws WSDLException
	{
		if ( operationsAndXMLs == null )
		{
			if ( operationsAndURLs == null )
			{
				operationsAndURLs = new HashMap<String, String> (10);
			}
			operationsAndXMLs = processWSDL ();
		}
		return operationsAndXMLs;
	}

	/**
	 * Gets the oprations' URLs from the given WSDL file.
	 * @return a Map of the operations. Operation names are the keys,
	 * 	operation URLs are the values.
	 */
	public Map<String, String> getOperationURLs () throws WSDLException
	{
		if ( operationsAndURLs == null )
		{
			operationsAndURLs = new HashMap<String, String> (10);
			operationsAndXMLs = processWSDL ();
		}
		return operationsAndURLs;
	}

	/**
	 * Processes the WSDL file and returns the oprations from the file.
	 * @return a Map of the operations. Operation names are the keys,
	 * 	skeleton XML strings are the values.
	 */
	private Map<String, String> processWSDL () throws WSDLException
	{
		Map<String, String> ret = new HashMap<String, String> (10);
		if ( wsdlReader == null )
		{
			wsdlReader = WSDLFactory.newInstance ().newWSDLReader ();
			wsdlReader.setFeature ("javax.wsdl.verbose", false);
			wsdlReader.setFeature ("javax.wsdl.importDocuments", true);
		}
		Definition def = wsdlReader.readWSDL (wsdlLocation);
		if ( def == null )
		{
			return ret;
		}
		Map servicesMap = def.getAllServices ();
		if ( servicesMap == null )
		{
			return ret;
		}
		Types t = def.getTypes ();
		Set<String> schemaLocations = new HashSet<String> (10);
		Set<Element> schemaElements = new HashSet<Element> (10);
		if ( t != null )
		{
			List extElems = t.getExtensibilityElements ();
			if ( extElems != null )
			{
				for ( Object o : t.getExtensibilityElements ())
				{
					if ( o != null && (o instanceof SchemaReference) )
					{
						schemaLocations.add (((SchemaReference) o).getSchemaLocationURI ());
					}
					else if ( o != null && (o instanceof Schema) )
					{
						Schema si = ((Schema) o);
						schemaElements.add (si.getElement ());
						Map schImports = si.getImports ();
						if ( schImports != null )
						{
							Collection schImportsValues = schImports.values ();
							if ( schImportsValues != null )
							{
								for (Object el : schImportsValues)
								{
									if ( el != null && (el instanceof List) )
									{
										for ( Object lel : (List)el )
										{
											if ( lel != null &&
												(lel instanceof SchemaReference) )
											{
												schemaLocations.add (
													((SchemaReference) lel)
													.getSchemaLocationURI ());
											}
										}
									}
								}
							}
						}
						List schIncludes = si.getIncludes ();
						if ( schIncludes != null )
						{
							for (Object el : schIncludes)
							{
								if ( el != null && (el instanceof SchemaReference) )
								{
									schemaLocations.add (((SchemaReference) el)
										.getSchemaLocationURI ());
								}
							}
						}
					}
				}
			}
		}
		for ( Object sn : servicesMap.keySet () )
		{
			Object s = servicesMap.get (sn);
			if ( s != null && (s instanceof Service) )
			{
				Map ports = ((Service) s).getPorts ();
				for ( Object pn : ports.keySet () )
				{
					Object p = ports.get (pn);
					if ( p != null && (p instanceof Port) )
					{
						Port currPort = (Port) p;
						Binding b = ((Port) p).getBinding();
						if ( b == null ) continue;
						PortType pt = b.getPortType();
						if ( pt == null ) continue;
						List operations = pt.getOperations ();
						if ( operations == null ) continue;
						List portExtElems = currPort.getExtensibilityElements ();
						for ( Object op : operations )
						{
							if ( op != null && (op instanceof Operation) )
							{
								Operation oper = (Operation) op;
								// first put just the operation names
								// and root elements
								String opName = oper.getName ();
								Input opInput = oper.getInput ();
								if ( opName == null
									|| opInput == null )
								{
									continue;
								}
								Message mess = opInput.getMessage ();
								if ( mess == null ) continue;
								QName messName = mess.getQName ();
								if ( messName == null ) continue;
								String messLocalName = messName.getLocalPart ();
								if ( messLocalName == null ) continue;
								ret.put (opName, messLocalName);
								if ( portExtElems != null )
								{
									for ( Object extel : currPort.getExtensibilityElements () )
									{
										if ( extel != null && (extel instanceof SOAPAddress) )
										{
											operationsAndURLs.put (
												opName,
												((SOAPAddress) extel).getLocationURI ());
										}
										else if ( extel != null && (extel instanceof SOAP12Address) )
										{
											operationsAndURLs.put (
												opName,
												((SOAP12Address) extel).getLocationURI ());
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
		for ( String opName : ret.keySet () )
		{
			ret.put (opName, processXSD (schemaLocations, ret.get (opName), schemaElements));
		}
		return ret;
	}

	/**
	 * Processes the schema files and generates a template XML with the given root element.
	 * @param schemaLocations The schema files' locations.
	 * @param rootElem The name of the root element in the generated XML template.
	 * @return the generated XML template.
	 */
	private String processXSD (Set<String> schemaLocations, String rootElem, Set<Element> schemaElements)
	{
		if ( globalElems == null )
		{
			List<XmlObject> sdocs = new ArrayList<XmlObject> ();
			XmlOptions parseOptions = new XmlOptions();
			parseOptions.setLoadLineNumbers();
			parseOptions.setLoadMessageDigest();

			if ( schemaLocations != null )
			{
				for ( String schemaFile : schemaLocations )
				{
					if ( schemaFile == null ) continue;
					try
					{
						sdocs.add (XmlObject.Factory.parse (new File (schemaFile),
							parseOptions));
					}
					catch (Exception e)
					{
						continue;
					}
				}
			}
			if ( schemaElements != null )
			{
				for ( Element schemaElement : schemaElements )
				{
					if ( schemaElement == null ) continue;
					try
					{
						sdocs.add (SchemaDocument.Factory.parse(schemaElement,
							parseOptions));
					}
					catch (Exception e)
					{
						continue;
					}
				}
			}

			XmlObject[] schemas = sdocs.toArray(new XmlObject[] {});
			SchemaTypeSystem sts = null;
			if ( schemas.length > 0 )
			{
				XmlOptions compileOptions = new XmlOptions ();
				compileOptions.setCompileDownloadUrls ();

				try
				{
					sts = XmlBeans.compileXsd (schemas,
						XmlBeans.getBuiltinTypeSystem (), compileOptions);
				}
				catch (Exception e)
				{
				}
			}

			if ( sts == null )
			{
				return empty;
			}
			globalElems = sts.documentTypes();
		}
		for (SchemaType st : globalElems)
		{
			if ( rootElem.equals (st.getDocumentElementName ().getLocalPart ()) )
			{
				return SampleXmlUtil.createSampleForType (st);
			}
		}
		return empty;
	}
}
