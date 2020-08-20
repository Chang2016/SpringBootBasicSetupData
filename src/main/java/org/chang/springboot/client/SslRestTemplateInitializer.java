package org.chang.springboot.client;

import java.io.IOException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.Optional;
import javax.net.ssl.SSLContext;
import org.apache.http.client.HttpClient;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

public class SslRestTemplateInitializer {

  public Optional<RestTemplate> initClient(String keystorePath) {
    Optional<SSLContext> maybeSslContext = tryToInitSSLContext(keystorePath);
    if (maybeSslContext.isPresent()) {
      SSLContext sslContext = maybeSslContext.get();
      SSLConnectionSocketFactory socketFactory = new SSLConnectionSocketFactory(sslContext);
      HttpClient httpClient = HttpClients.custom()
          .setSSLSocketFactory(socketFactory)
          .build();
      HttpComponentsClientHttpRequestFactory factory =
          new HttpComponentsClientHttpRequestFactory(httpClient);
      return Optional.of(new RestTemplate(factory));
    }
    return Optional.empty();
  }

  private Optional<SSLContext> tryToInitSSLContext(String keystorePath) {
    try {
      URL url = new URL(keystorePath);
      SSLContext sslContext = new SSLContextBuilder()
          .loadTrustMaterial(url, "123456".toCharArray())
          .build();
      return Optional.of(sslContext);
    } catch (NoSuchAlgorithmException | KeyManagementException | CertificateException | KeyStoreException | IOException e) {
      e.printStackTrace();
    }
    return Optional.empty();
  }
}
