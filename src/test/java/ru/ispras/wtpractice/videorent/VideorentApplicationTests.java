package ru.ispras.wtpractice.videorent;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class VideorentApplicationTests extends AbstractTestcontainers {

	@Test
	void contextLoads() {
	}

}
