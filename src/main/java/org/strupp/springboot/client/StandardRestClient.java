package org.strupp.springboot.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.Optional;

import java.util.Properties;
import javax.net.ssl.SSLContext;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.strupp.springboot.topic.Topic;

public class StandardRestClient {
	private static final Logger log = LoggerFactory.getLogger(StandardRestClient.class);

	public Optional<Topic> findTopicHavingId(long id) {
		try {
			RestTemplate restTemplate = initClient();
			Topic topic = restTemplate.getForObject("https://localhost:8443/topics/" + id, Topic.class);
			log.info(topic.getName());
			return Optional.of(topic);
		} catch (RestClientException e) {
			e.printStackTrace();
			return Optional.empty();
		}
	}

	public void findTopics() {
		try {
			try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
				final String uri = "https://localhost:8443/topics/";
				HttpGet getMethod = new HttpGet(uri);
				HttpResponse httpResponse = httpClient.execute(getMethod);
				log.info("StatusCode: " + httpResponse.getStatusLine().getStatusCode());
				try (BufferedReader reader = new BufferedReader(
						new InputStreamReader(httpResponse.getEntity().getContent()))) {
					String inputLine;
					while ((inputLine = reader.readLine()) != null) {
						System.out.println(inputLine);
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private RestTemplate initClient() {
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

	private Properties initProperties() {
		Properties properties = new Properties();
		try (final InputStream stream =
				this.getClass().getResourceAsStream("application.properties")) {
			properties.load(stream);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return properties;
	}

	public static void main(String args[]) {
		StandardRestClient client = new StandardRestClient();
//		client.findTopicHavingId(1).orElse(new Topic());
		client.findTopicHavingId(1);

	}
}
