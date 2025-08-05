/*
 * TestHelper - a helper class for test classes.
 *
 * Copyright (C) 2022-2025 Bogdan 'bogdro' Drozdowski, bogdro (at) users . sourceforge . net
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

package bogdrosoft.fronsetia.helpers;

import java.net.URL;

/**
 * TestHelper - a helper class for test classes.
 * @author Bogdan 'bogdro' Drozdowski, bogdro (at) users . sourceforge . net
 */
public class TestHelper
{
	public static String getFullPathFor(String file)
	{
		String dir = System.getProperty("test.rsrc.dir");
		if (dir != null) {
			// maven build
			dir += "/" + file;
		} else {
			// IDE unit test run
			URL u = ClassLoader.getSystemResource(file);
			dir = u.toString().replace("file:", "");
		}
		return dir;
	}
}
