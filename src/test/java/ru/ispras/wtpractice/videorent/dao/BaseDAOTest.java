package ru.ispras.wtpractice.videorent.dao;

import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import ru.ispras.wtpractice.videorent.AbstractTestcontainers;

@DataJpaTest
@ActiveProfiles("test")
@Sql(scripts = {"/sql/test-data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public abstract class BaseDAOTest extends AbstractTestcontainers { }
