package org.chang.springboot.integration;

import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.ActiveProfiles;
import org.chang.springboot.DatabaseConfiguration;

@ActiveProfiles({"test"})
@AutoConfigureDataJpa
@SpringBootTest(
    webEnvironment = WebEnvironment.NONE,
    classes = {DatabaseConfiguration.class}
)
public abstract class DatabaseIntegrationTest extends MySQLContainerTest {}
