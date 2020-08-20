package org.chang.springboot.integration;

import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
@ContextConfiguration(initializers = {Initializer.class})
public abstract class MySQLContainerTest {}
