/*
 * Copyright 2012-2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.chang.springboot.ssl;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import javax.net.ssl.SSLContext;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingClientConnectionManager;
import org.apache.http.ssl.SSLContextBuilder;
import org.chang.springboot.topic.TopicList;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;
import org.chang.springboot.SpringBootBasicDataMain;

@RunWith(SpringRunner.class)
@DirtiesContext
@SpringBootTest(classes = SpringBootBasicDataMain.class,
		webEnvironment = WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = {"timezone = GMT"})
public class SslIT {

  @LocalServerPort
  private int port;

  @Value("${classpath:server.ssl.key-store}")
  private String keystore;

  @Autowired
  private Environment env;

  @Test
  public void retrieveTopicsUsingSsl() {
    String keyStoreType = env.getProperty("server.ssl.key-store-type");
    String timezone = env.getProperty("timezone");
    assertThat(keystore, is(notNullValue()));
    assertThat(timezone, is("GMT"));
    assertThat(keyStoreType, is("PKCS12"));

    RestTemplate restTemplate = initSslClient();
    final String uri = String.format("https://localhost:%d/topics/", port);
    ResponseEntity<TopicList> response = restTemplate.getForEntity(uri, TopicList.class);
    assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));
  }

  /*
   * Da mein selbstsigniertes Zertifikat im JDK Keystore ist wird dieser Test nicht benötigt.
   * Der Test konfiguriert, dass dem Zertifikat vertraut wird, obwohl es selbstsigniert ist.
   */
  @Test//(expected = SSLPeerUnverifiedException.class)
  public void givenAcceptingAllCertificates_whenHttpsUrlIsConsumed_thenException()
      throws ClientProtocolException, IOException,
      UnrecoverableKeyException, KeyStoreException, KeyManagementException, NoSuchAlgorithmException {
    TrustStrategy acceptingTrustStrategy = (cert, authType) -> true;
    SSLSocketFactory sf = new SSLSocketFactory(
        acceptingTrustStrategy, SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
    SchemeRegistry registry = new SchemeRegistry();
    registry.register(new Scheme("https", port, sf));
    ClientConnectionManager ccm = new PoolingClientConnectionManager(registry);

    DefaultHttpClient httpClient = new DefaultHttpClient(ccm);

    String urlOverHttps = "https://localhost:" + port + "/topics/";
    HttpGet getMethod = new HttpGet(urlOverHttps);
    HttpResponse response = httpClient.execute(getMethod);
    assertThat(response.getStatusLine().getStatusCode(), equalTo(200));
  }

  private RestTemplate initSslClient() {
    String pathToKeystore = "file:///Users/michaelstrupp/projects/training/springboot/SpringBootBasicSetupData/src/main/resources/keystore.p12";
    SSLContext sslContext = null;
    try {
      URL url = new URL(pathToKeystore);
      sslContext = new SSLContextBuilder()
          .loadTrustMaterial(url, "123456".toCharArray())
          .build();
    } catch (MalformedURLException e) {
      e.printStackTrace();
    } catch (NoSuchAlgorithmException e) {
      e.printStackTrace();
    } catch (KeyManagementException e) {
      e.printStackTrace();
    } catch (CertificateException e) {
      e.printStackTrace();
    } catch (KeyStoreException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
    SSLConnectionSocketFactory socketFactory = new SSLConnectionSocketFactory(sslContext);
    HttpClient httpClient = HttpClients.custom()
        .setSSLSocketFactory(socketFactory)
        .build();
    HttpComponentsClientHttpRequestFactory factory =
        new HttpComponentsClientHttpRequestFactory(httpClient);
    return new RestTemplate(factory);
  }
}
