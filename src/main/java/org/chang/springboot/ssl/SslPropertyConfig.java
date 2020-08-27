package org.chang.springboot.ssl;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "server")
public class SslPropertyConfig {
		
	private String port;
	private Ssl ssl;

	public static class Ssl {
		private String keystoretype;
		private String keystore;
		private String keystorePassword;
		private String keyAlias;
		
		public String getKeystoretype() {
			return keystoretype;
		}
		public String getKeystore() {
			return keystore;
		}
		public String getKeystorePassword() {
			return keystorePassword;
		}
		public String getKeyAlias() {
			return keyAlias;
		}
		public void setKeystoretype(String keystoretype) {
			this.keystoretype = keystoretype;
		}
		public void setKeystore(String keystore) {
			this.keystore = keystore;
		}
		public void setKeystorePassword(String keystorePassword) {
			this.keystorePassword = keystorePassword;
		}
		public void setKeyAlias(String keyAlias) {
			this.keyAlias = keyAlias;
		}
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public Ssl getSsl() { return ssl; }

	public void setSsl(Ssl ssl) { this.ssl = ssl; }
}
