package com.autohome_api.commons;

import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;

import javax.net.ssl.SSLContext;
import javax.security.cert.CertificateException;
import javax.security.cert.X509Certificate;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

/**
 * Created by zhanghuibin on 2016/12/24.
 */
public class HttpClientUtil {
    public static CloseableHttpClient createSSLClientDefault(){
        try {
            SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {
                @Override
                public boolean isTrusted(java.security.cert.X509Certificate[] chain, String authType) throws java.security.cert.CertificateException {
                    return true;
                }

                //信任所有
                public boolean isTrusted(X509Certificate[] chain,
                                         String authType) throws CertificateException {
                    return false;
                }
            }).build();
            SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext);
            return HttpClients.custom().setSSLSocketFactory(sslsf).build();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        }
        return  HttpClients.createDefault();
    }
}
