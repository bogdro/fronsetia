/*
 * SOAPInterpreter - a class for parsing SOAP responses.
 *
 * Copyright (C) 2011-2023 Bogdan 'bogdro' Drozdowski, bogdro (at) users . sourceforge . net
 *
 * This file is part of Fronsetia (Free Online Service Testing Application),
 *  a web application that allows testing webservices.
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

package BogDroSoft.soaptest;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.impl.dom.jaxp.DOOMDocumentBuilderFactory;
import org.apache.axiom.soap.SOAPEnvelope;

import org.apache.axis2.saaj.util.SAAJUtil;

import org.w3c.dom.Document;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * SOAPInterpreter - a class for parsing SOAP responses.
 * @author Bogdan 'bogdro' Drozdowski, bogdandr <at> op . pl
 */
public class SOAPInterpreter
{
	private boolean hasFault;
	private List<String> bodyElements;

	/**
	 * Parses the given SOAP response and sets private fields.
	 * @param resp the SOAP response to parse.
	 */
	public void parseResponse (String resp)
		throws SAXException, IOException, ParserConfigurationException
	{
		if ( resp == null )
		{
			return;
		}
		DocumentBuilderFactory dbf = new DOOMDocumentBuilderFactory ();
		DocumentBuilder db = dbf.newDocumentBuilder ();
		Document d = db.parse (new InputSource (new StringReader (resp)));
		SOAPEnvelope e = SAAJUtil.toOMSOAPEnvelope (d.getDocumentElement ());
		hasFault = e.hasFault ();
		Iterator<?> i = e.getBody ().getChildElements ();
		if ( i != null )
		{
			bodyElements = new ArrayList<String> (1);
			while (i.hasNext ())
			{
				Object o = i.next ();
				if ( o != null && o instanceof OMElement )
				{
					bodyElements.add ( ((OMElement)o).getLocalName () );
				}
			}
		}
	}

	/**
	 * Tells if there was a SOAP fault in the parsed SOAP response.
	 * @return TRUE if there was a SOAP fault in the parsed SOAP response.
	 */
	public boolean wasFault ()
	{
		return hasFault;
	}

	/**
	 * Returns a list of names of the top-level elements of the SOAP response.
	 * @return a list of names of the top-level elements of the SOAP response.
	 */
	public List<String> getBodyElements ()
	{
		if ( bodyElements != null )
		{
			return new ArrayList<String> (bodyElements);
		}
		return null;
	}
}
