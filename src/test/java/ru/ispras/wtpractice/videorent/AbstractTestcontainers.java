package ru.ispras.wtpractice.videorent;

import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.postgresql.PostgreSQLContainer;

public abstract class AbstractTestcontainers {
    @ServiceConnection
    static PostgreSQLContainer postgres = new PostgreSQLContainer(
            "postgres:16-alpine"
    ).withInitScript("sql/01-init_db.sql");

    static {
        postgres.start();
    }
}
