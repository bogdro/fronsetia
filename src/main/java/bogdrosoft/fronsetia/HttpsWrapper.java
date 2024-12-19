/*
 * HttpsWrapper - a class for providing TLS/SSL to HttpClients.
 *
 * Copyright (C) 2011-2024 Bogdan 'bogdro' Drozdowski, bogdro (at) users . sourceforge . net
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

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;

/**
 * HttpsWrapper - a class for providing TLS/SSL to HttpClients.
 * @author Bogdan 'bogdro' Drozdowski, bogdro (at) users . sourceforge . net
 */
public class HttpsWrapper
{
	private HttpsWrapper() {/* utility class */}

	/**
	 * Creates an 'accept all TLS/SSL' HttpClient.
	 * See http://tech.chitgoks.com/2011/04/24/how-to-avoid-javax-net-ssl-sslpeerunverifiedexception-peer-not-authenticated-problem-using-apache-httpclient/
	 * @param base the instance to base on.
	 * @param ports the ports to apply the connection scheme to.
	 * @return a HttpClientConnectionManager that accepts all TLS/SSL connections, or NULL in case of errors.
	 */
	public static HttpClientConnectionManager createSecureConnManager()
	{
		try
		{
			PlainConnectionSocketFactory plainsf =
					PlainConnectionSocketFactory.getSocketFactory();
			SSLContext ctx = SSLContext.getInstance ("TLS");
			ctx.init (null, new TrustManager[]{AcceptAllTrustManager.INSTANCE}, null);
			SSLConnectionSocketFactory ssf = new SSLConnectionSocketFactory
					(ctx, NoopHostnameVerifier.INSTANCE);
			Registry<ConnectionSocketFactory> r =
					RegistryBuilder.<ConnectionSocketFactory>create()
			        .register("http", plainsf)
			        .register("https", ssf)
			        .build();
			return new PoolingHttpClientConnectionManager(r);
		}
		catch (Exception e)
		{
			return null;
		}
	}

	/**
	 * An "accept all TLS/SSL certificates" trust manager.
	 */
	static class AcceptAllTrustManager implements X509TrustManager
	{
		public static final AcceptAllTrustManager INSTANCE = new AcceptAllTrustManager();
		private static final X509Certificate[] ACCEPTED_ISSUERS = new X509Certificate[0];

		private AcceptAllTrustManager() {}

		@Override
		public void checkClientTrusted (X509Certificate[] xcs, String string)
			throws CertificateException
		{
			/* no need */
		}

		@Override
		public void checkServerTrusted (X509Certificate[] xcs, String string)
			throws CertificateException
		{
			/* no need */
		}

		@Override
		public X509Certificate[] getAcceptedIssuers ()
		{
			return ACCEPTED_ISSUERS;
		}
	}
}
