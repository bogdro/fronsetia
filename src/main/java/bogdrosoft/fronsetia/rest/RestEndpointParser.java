/*
 * RestEndpointParser - a class that checks REST/HTTP endpoints.
 *
 * Copyright (C) 2025 Bogdan 'bogdro' Drozdowski, bogdro (at) users . sourceforge . net
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

package bogdrosoft.fronsetia.rest;

import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import bogdrosoft.fronsetia.EndpointParser;
import bogdrosoft.fronsetia.EndpointType;

/**
 * RestEndpointParser - a class that checks REST/HTTP endpoints.
 * @author Bogdan 'bogdro' Drozdowski, bogdro (at) users . sourceforge . net
 */
public class RestEndpointParser implements EndpointParser
{
	private static final Set<String> OPERATIONS = Collections.singleton("HTTP endpoint");

	private String operationUrl = "";
	private Exception parseException;

	@Override
	public void parse(String url)
	{
		try
		{
			new URL(url);
			operationUrl = url;
		}
		catch (Exception ex)
		{
			parseException = ex;
		}
	}

	@Override
	public EndpointType getType()
	{
		return EndpointType.REST;
	}

	@Override
	public Set<String> getListOfOperations()
	{
		return OPERATIONS;
	}

	@Override
	public List<String> getListOfTransportHeadersForOperation(String operationName)
	{
		return Collections.emptyList();
	}

	@Override
	public String getDefaultContentType()
	{
		return "application/json";
	}

	@Override
	public String getUrlForOperation(String operationName)
	{
		return operationUrl;
	}

	@Override
	public List<String> getDefaultPayloadProloguesForOperation(String operationName)
	{
		return Collections.singletonList("");
	}

	@Override
	public String getDefaultPayloadHeaderForOperation(String operationName)
	{
		return "";
	}

	@Override
	public String getDefaultPayloadMiddleForOperation(String operationName)
	{
		return "";
	}

	@Override
	public String getDefaultPayloadForOperation(String operationName)
	{
		return "";
	}

	@Override
	public String getDefaultPayloadEpilogueForOperation(String operationName)
	{
		return "";
	}

	@Override
	public Exception getParsingException()
	{
		return parseException;
	}
}
