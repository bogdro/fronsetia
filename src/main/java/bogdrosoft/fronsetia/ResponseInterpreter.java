/*
 * ResponseInterpreter - an interface for response parsers.
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

/**
 * ResponseInterpreter - an interface for response parsers.
 * @author Bogdan 'bogdro' Drozdowski, bogdro (at) users . sourceforge . net
 */
public interface ResponseInterpreter
{
	/**
	 * Parses the given response.
	 * @param resp the response to parse.
	 * @throws Exception
	 */
	void parseResponse(String resp) throws Exception;

	/**
	 * Tells if there was a fault detected in the parsed response.
	 * @return TRUE if there was a fault in the parsed response.
	 */
	boolean hasFault();

	/**
	 * Returns a list of names of the top-level elements of the response.
	 * @return a list of names of the top-level elements of the response.
	 */
	List<String> getBodyElements();
}
