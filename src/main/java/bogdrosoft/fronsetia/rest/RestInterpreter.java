/*
 * RestInterpreter - a class for parsing REST responses.
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

import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import bogdrosoft.fronsetia.ResponseInterpreter;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;

/**
 * RestInterpreter - a class for parsing REST responses.
 * @author Bogdan 'bogdro' Drozdowski, bogdro (at) users . sourceforge . net
 */
public class RestInterpreter implements ResponseInterpreter
{
	private static final Map<String, String> REPLACEMENTS;

	private List<String> topElems = Collections.emptyList();

	static
	{
		HashMap<String, String> repl = new HashMap<String, String>(2);
		repl.put("\\{", "{\n");
		repl.put("\\}", "\n}\n");
		REPLACEMENTS = Collections.unmodifiableMap(repl);
	}

	@Override
	public void parseResponse(String resp) throws Exception
	{
		if (resp != null)
		{
			try (Reader sr = new StringReader(resp);
				JsonReader r = Json.createReader(sr))
			{
				JsonObject obj = r.readObject();
				topElems = Collections.unmodifiableList(new ArrayList<>(obj.keySet()));
			}
		}
	}

	@Override
	public boolean hasFault()
	{
		return false;
	}

	@Override
	public List<String> getBodyElements()
	{
		return topElems;
	}

	@Override
	public Map<String, String> getReplacemenets()
	{
		return REPLACEMENTS;
	}
}
