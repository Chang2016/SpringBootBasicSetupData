package org.chang.springboot;

import org.keycloak.adapters.KeycloakConfigResolver;
import org.keycloak.adapters.springboot.KeycloakSpringBootConfigResolver;
import org.keycloak.adapters.springsecurity.KeycloakConfiguration;
import org.keycloak.adapters.springsecurity.authentication.KeycloakAuthenticationProvider;
import org.keycloak.adapters.springsecurity.client.KeycloakClientRequestFactory;
import org.keycloak.adapters.springsecurity.config.KeycloakWebSecurityConfigurerAdapter;
import org.keycloak.adapters.springsecurity.filter.KeycloakAuthenticationProcessingFilter;
import org.keycloak.adapters.springsecurity.filter.KeycloakPreAuthActionsFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.authority.mapping.SimpleAuthorityMapper;
import org.springframework.security.web.authentication.session.NullAuthenticatedSessionStrategy;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;

@KeycloakConfiguration
public class SecurityConfig extends KeycloakWebSecurityConfigurerAdapter {
  private final KeycloakClientRequestFactory keycloakClientRequestFactory;

  public SecurityConfig(KeycloakClientRequestFactory keycloakClientRequestFactory) {
    this.keycloakClientRequestFactory = keycloakClientRequestFactory;
  }

  @Autowired
  public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
    KeycloakAuthenticationProvider keyCloakAuthProvider = keycloakAuthenticationProvider();
    //fügt Präfix ROLE_ zu Keycloak Rollen
    keyCloakAuthProvider.setGrantedAuthoritiesMapper(new SimpleAuthorityMapper());

    auth.authenticationProvider(keyCloakAuthProvider);
  }

  /*
   * By Default, the Spring Security Adapter looks for a keycloak.json configuration file.
   * You can make sure it looks at the configuration provided by the Spring Boot Adapter by
   * adding this bean
   */
  @Bean
  public KeycloakConfigResolver KeyCloakConfigResolver(){
    return new KeycloakSpringBootConfigResolver();
  }

  //Standardeinstellung von SpringSecurity ändern, keine HTTP-Sessions
  // für bearer-only clients
  @Bean
  @Override
  protected SessionAuthenticationStrategy sessionAuthenticationStrategy() {
    return new NullAuthenticatedSessionStrategy();
  }

  /*
   * Spring Boot attempts to eagerly register filter beans with the web application context. #
   * Therefore, when running
   * the Keycloak Spring Security adapter in a Spring Boot environment, it may be
   * necessary to add two
   * FilterRegistrationBeans to your security configuration to prevent the Keycloak filters
   * from being registered twice.
   */
  @Bean
  public FilterRegistrationBean keycloakAuthenticationProcessingFilterRegistrationBean(
      KeycloakAuthenticationProcessingFilter filter) {
    FilterRegistrationBean registrationBean = new FilterRegistrationBean(filter);
    registrationBean.setEnabled(false);
    return registrationBean;
  }

  @Bean
  public FilterRegistrationBean keycloakPreAuthActionsFilterRegistrationBean(
      KeycloakPreAuthActionsFilter filter) {
    FilterRegistrationBean registrationBean = new FilterRegistrationBean(filter);
    registrationBean.setEnabled(false);
    return registrationBean;
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception
  {
    super.configure(http);
    http.cors()
        .and()
        .csrf()
        .disable()
        .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .sessionAuthenticationStrategy(sessionAuthenticationStrategy())
        .and()
        .authorizeRequests()
        .antMatchers("/supporter*").hasRole("user")
        .antMatchers("/public*").permitAll();

    // add this line to use H2 web console
    http.headers().frameOptions().disable();
  }
}
