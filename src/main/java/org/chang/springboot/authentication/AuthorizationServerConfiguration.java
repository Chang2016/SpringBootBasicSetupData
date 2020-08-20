package org.chang.springboot.authentication;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.approval.UserApprovalHandler;
import org.springframework.security.oauth2.provider.token.TokenStore;

@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfiguration extends AuthorizationServerConfigurerAdapter {

  private static final String REALM = "MY_OAUTH_REALM";

  private final TokenStore tokenStore;

  private final UserApprovalHandler userApprovalHandler;

  @Qualifier("authenticationManagerBean")
  private final AuthenticationManager authenticationManager;

  public AuthorizationServerConfiguration(
      TokenStore tokenStore,
      UserApprovalHandler userApprovalHandler,
      AuthenticationManager authenticationManager) {
    this.tokenStore = tokenStore;
    this.userApprovalHandler = userApprovalHandler;
    this.authenticationManager = authenticationManager;
  }

  /*
   * Registers a client with client-id ‘my-trusted-client’ and password ‘secret’ and roles
   * & scope he is allowed  for.
   * Specifies that any generated access token will be valid for only 120 seconds
   * Specifies that any generated refresh token will be valid for only 600 seconds
   */
  @Override
  public void configure(ClientDetailsServiceConfigurer clients) throws Exception {

    clients.inMemory()
        .withClient("my-trusted-client")
        .authorizedGrantTypes("password", "authorization_code", "refresh_token", "implicit")
        .authorities("ROLE_CLIENT", "ROLE_TRUSTED_CLIENT")
        .scopes("read", "write", "trust")
        .secret("secret")
        .accessTokenValiditySeconds(86400).
        refreshTokenValiditySeconds(10000);
  }

  @Override
  public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
    endpoints.tokenStore(tokenStore).userApprovalHandler(userApprovalHandler)
        .authenticationManager(authenticationManager);
  }

  @Override
  public void configure(AuthorizationServerSecurityConfigurer oauthServer) {
    oauthServer.realm(REALM + "/client");
  }

}
