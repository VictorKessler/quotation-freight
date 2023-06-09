package com.victorkessler.quotationfreight.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.wait.strategy.Wait;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;

@ActiveProfiles("test")
@ContextConfiguration(classes = TestContextConfiguration.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class AbstractTest {

    private static final String TEST_RESOURCES = "src/test/resources/";
    private static final DockerComposeContainerWrapper COMPOSE_CONTAINER;

    static {
        final var resourceDirectory = Paths.get("src",  "test", "resources", "docker", "docker-compose.yml");
        COMPOSE_CONTAINER = new DockerComposeContainerWrapper(resourceDirectory.toFile())
                .withLocalCompose(true)
                .withOptions("--compatibility")
                .withExposedService("postgres-db-test", 5432, Wait.forListeningPort().withStartupTimeout(Duration.ofSeconds(5)));

        COMPOSE_CONTAINER.start();
        Runtime.getRuntime().addShutdownHook(new Thread(AbstractTest::stopContainer));
    }

    private static void stopContainer(){
        COMPOSE_CONTAINER.stop();
    }

    protected static String getContent(final String file) throws IOException {
        return String.join("",
                Files.readAllLines(Paths.get(TEST_RESOURCES + file), Charset.forName("ISO-8859-1")));
    }

    protected static <T> T getRequest(final String file, final Class<T> clazz) throws IOException, ClassNotFoundException {
        return new ObjectMapper().readValue(file, clazz);
    }

}
