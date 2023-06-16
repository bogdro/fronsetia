/*
 * HttpsWrapper - a class for providing TLS/SSL to HttpClients.
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

package bogdrosoft.fronsetia;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.client.HttpClient;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;

/**
 * HttpsWrapper - a class for providing TLS/SSL to HttpClients.
 * @author Bogdan 'bogdro' Drozdowski, bogdro (at) users . sourceforge . net
 */
public class HttpsWrapper
{
	private static final X509TrustManager ACCEPT_ALL_TM = new X509TrustManager()
	{
		@Override
		public void checkClientTrusted (X509Certificate[] xcs, String string)
			throws CertificateException
		{
		}

		@Override
		public void checkServerTrusted (X509Certificate[] xcs, String string)
			throws CertificateException
		{
		}

		@Override
		public X509Certificate[] getAcceptedIssuers ()
		{
			return null;
		}
	};

	/**
	 * Creates an 'accept all TLS/SSL' HttpClient.
	 * See http://tech.chitgoks.com/2011/04/24/how-to-avoid-javax-net-ssl-sslpeerunverifiedexception-peer-not-authenticated-problem-using-apache-httpclient/
	 * @param base the instance to base on.
	 * @param ports the ports to apply the connection scheme to.
	 * @return a HttpClient that accepts all TLS/SSL connections, or NULL in case of errors.
	 */
	public static HttpClient wrapClientForSSL (HttpClient base, int[] ports)
	{
		if ( base == null )
		{
			base = new DefaultHttpClient ();
		}
		try
		{
			SSLContext ctx = SSLContext.getInstance ("TLS");
			ctx.init (null, new TrustManager[]{ACCEPT_ALL_TM}, null);
			SSLSocketFactory ssf = new SSLSocketFactory (ctx,
				SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
			ClientConnectionManager ccm = base.getConnectionManager ();
			SchemeRegistry sr = ccm.getSchemeRegistry ();
			// always include the default port, because the WSDL can provide
			// just the "https" scheme without the port:
			sr.register (new Scheme ("https", 443, ssf));
			// add any additional provided ports:
			for ( int i = 0; i < ports.length; i++ )
			{
				if ( ports[i] != -1 && ports[i] != 443 )
				{
					sr.register (new Scheme ("https", ports[i], ssf));
				}
			}
			HttpClient ret = new DefaultHttpClient (ccm);
			// copy the already-set fields:
			((AbstractHttpClient)ret).setParams (base.getParams());
			if ( base instanceof AbstractHttpClient )
			{
				((AbstractHttpClient)ret).setCredentialsProvider (
					((AbstractHttpClient)base).getCredentialsProvider ());
			}
			return ret;
		}
		catch (NoSuchAlgorithmException nsaex)
		{
			return null;
		}
		catch (KeyManagementException kmex)
		{
			return null;
		}
	}
}
