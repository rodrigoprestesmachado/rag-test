package dev.rpmhub;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

@QuarkusTest
class RagServiceTest {

    @Test
    void testMime() {
        given()
            .queryParam("prompt", "o que é um mime?")
            .when().get("http://localhost:8081/ai/rag")
            .then();
    }

}