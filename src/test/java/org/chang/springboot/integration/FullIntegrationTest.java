package org.chang.springboot.integration;

import org.chang.springboot.DatabaseConfiguration;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles({"test"})
@AutoConfigureDataJpa
@SpringBootTest(
    classes = {DatabaseConfiguration.class}
)
public abstract class FullIntegrationTest extends MySQLContainerTest {}
