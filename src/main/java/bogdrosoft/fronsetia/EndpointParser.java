/*
 * EndpointParser - an interface for endpoint-processing classes.
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

package bogdrosoft.fronsetia;

import java.util.List;
import java.util.Set;

public interface EndpointParser
{
	/**
	 * Interprets the endpoint with the given URL.
	 * @param url the URL of the endpoint to process.
	 */
	void parse(String url);

	/**
	 * Gets the list of operations found by this parser.
	 * @return the list of operations found by this parser.
	 */
	Set<String> getListOfOperations();

	/**
	 * Gets the list of transport/protocol headers for the given operation.
	 * @param operationName the operation name to get the headers for.
	 * @return the list of transport/protocol headers for the given operation.
	 */
	List<String> getListOfTransportHeadersForOperation(String operationName);

	/**
	 * Gets the default payload content type.
	 * @return the default payload content type.
	 */
	String getDefaultContentType();

	/**
	 * Gets the URL to call for the given operation.
	 * @param operationName the operation name to get the calling address for.
	 * @return the URL to call for the given operation.
	 */
	String getUrlForOperation(String operationName);

	/**
	 * Gets the default payload prologue for the given operation.
	 * @param operationName the operation name to get the default payload prologue for.
	 * @return the default payload prologue for the given operation.
	 */
	String getDefaultPayloadPrologueForOperation(String operationName);

	/**
	 * Gets the default payload header for the given operation.
	 * @param operationName the operation name to get the default payload header for.
	 * @return the default payload header for the given operation.
	 */
	String getDefaultPayloadHeaderForOperation(String operationName);

	/**
	 * Gets the default payload middle (the part between the header and
	 * the user content) for the given operation.
	 * @param operationName the operation name to get the default payload middle for.
	 * @return the default payload middle for the given operation.
	 */
	String getDefaultPayloadMiddleForOperation(String operationName);

	/**
	 * Gets the default payload (user content) for the given operation.
	 * @param operationName the operation name to get the default payload for.
	 * @return the default payload for the given operation.
	 */
	String getDefaultPayloadForOperation(String operationName);

	/**
	 * Gets the default payload footer (the part after the user content) for the given operation.
	 * @param operationName the operation name to get the default payload footer for.
	 * @return the default payload footer for the given operation.
	 */
	String getDefaultPayloadEpilogueForOperation(String operationName);

	/**
	 * Gets the exception that occurred during endpoint parsing, if any.
	 * @return the exception that occurred during endpoint parsing, or null.
	 */
	Exception getParsingException();
}
