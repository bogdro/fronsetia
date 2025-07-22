/*
 * HttpsWrapperTest - a test for HttpsWrapper.
 *
 * Copyright (C) 2023-2025 Bogdan 'bogdro' Drozdowski, bogdro (at) users . sourceforge . net
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

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

/**
 * HttpsWrapperTest - a test for HttpsWrapper.
 * @author Bogdan 'bogdro' Drozdowski, bogdro (at) users . sourceforge . net
 */
public class HttpsWrapperTest
{
	@Test
	public void testCreateSecureConnManager()
	{
		assertNotNull(HttpsWrapper.createSecureConnManager());
	}

	@Test
	public void testTrustManager() throws Exception
	{
		HttpsWrapper.AcceptAllTrustManager.INSTANCE.checkClientTrusted(null, null);
		HttpsWrapper.AcceptAllTrustManager.INSTANCE.checkServerTrusted(null, null);
		assertNotNull(HttpsWrapper.AcceptAllTrustManager.INSTANCE.getAcceptedIssuers());
	}
}
