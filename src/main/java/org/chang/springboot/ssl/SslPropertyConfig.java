package org.chang.springboot.ssl;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "server")
public class SslPropertyConfig {
		
	private String port;
	
	public class Ssl {
		private String keystoretype;
		private String keystorePath;
		private String keystorePassword;
		private String keyAlias;
		
		public String getKeystoretype() {
			return keystoretype;
		}
		public String getKeystorePath() {
			return keystorePath;
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
		public void setKeystorePath(String keystorePath) {
			this.keystorePath = keystorePath;
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
}
