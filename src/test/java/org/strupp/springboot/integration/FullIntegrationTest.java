package org.strupp.springboot.integration;

import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.strupp.springboot.DatabaseConfiguration;

@ActiveProfiles({"test"})
@AutoConfigureDataJpa
@SpringBootTest(
    classes = {DatabaseConfiguration.class}
)
public abstract class FullIntegrationTest extends MySQLContainerTest {}
