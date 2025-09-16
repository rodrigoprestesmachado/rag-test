/**
 * This file contains confidential and proprietary information.
 * Unauthorized copying, distribution, or use of this file or its contents is
 * strictly prohibited.
 *
 * 2025 Rodrigo Prestes Machado. All rights reserved.
 */

package dev.rpmhub;

import static io.restassured.RestAssured.given;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusIntegrationTest;

@QuarkusIntegrationTest
class RagControllerBasicIT {

    @Test
    @DisplayName("Teste básico de endpoint - status code e content type")
    void testBasicEndpointResponse() {
        // Teste simples do endpoint de memory que é mais rápido
        given()
            .when()
                .queryParam("session", "basic-test-session")
                .get("/ai/memory")
            .then()
                .statusCode(204); // Esperamos 204 para sessão não existente
    }

    @Test
    @DisplayName("Teste de endpoint chatbot - apenas validação de headers")
    void testChatbotEndpointHeaders() {
        given()
            .when()
                .queryParam("session", "header-test")
                .queryParam("prompt", "Teste simples")
                .get("/ai/chatbot")
            .then()
                .statusCode(200)
                .header("Content-Type", "text/event-stream");
    }

    @Test
    @DisplayName("Teste de endpoint ask - apenas validação de headers")
    void testAskEndpointHeaders() {
        given()
            .when()
                .queryParam("session", "ask-header-test")
                .queryParam("prompt", "Pergunta sobre Vue.js")
                .get("/ai/ask")
            .then()
                .statusCode(200)
                .header("Content-Type", "text/event-stream");
    }

    @Test
    @DisplayName("Teste específico Vue.js - pergunta sobre componentes")
    void testVueComponentsQuestion() {
        given()
            .when()
                .queryParam("session", "vue-components-test")
                .queryParam("prompt", "Como criar componentes Vue.js com props?")
                .get("/ai/chatbot")
            .then()
                .statusCode(200)
                .header("Content-Type", "text/event-stream");
    }

    @Test
    @DisplayName("Teste Vue.js - pergunta sobre diretivas")
    void testVueDirectivesQuestion() {
        given()
            .when()
                .queryParam("session", "vue-directives-test")
                .queryParam("prompt", "Explique as diretivas v-if e v-for do Vue.js")
                .get("/ai/ask")
            .then()
                .statusCode(200)
                .header("Content-Type", "text/event-stream");
    }

    @Test
    @DisplayName("Teste Vue.js - pergunta sobre reatividade")
    void testVueReactivityQuestion() {
        given()
            .when()
                .queryParam("session", "vue-reactivity-test")
                .queryParam("prompt", "Como funciona o sistema de reatividade do Vue.js?")
                .get("/ai/chatbot")
            .then()
                .statusCode(200)
                .header("Content-Type", "text/event-stream");
    }

    @Test
    @DisplayName("Teste Vue.js - pergunta sobre Composition API")
    void testVueCompositionApiQuestion() {
        given()
            .when()
                .queryParam("session", "vue-composition-api-test")
                .queryParam("prompt", "Qual a diferença entre Options API e Composition API no Vue.js?")
                .get("/ai/ask")
            .then()
                .statusCode(200)
                .header("Content-Type", "text/event-stream");
    }

    @Test
    @DisplayName("Teste de caracteres especiais em sessão")
    void testSpecialCharactersInSession() {
        given()
            .when()
                .queryParam("session", "test-session-àçéíõú")
                .queryParam("prompt", "Teste com caracteres especiais")
                .get("/ai/chatbot")
            .then()
                .statusCode(200)
                .header("Content-Type", "text/event-stream");
    }

    @Test
    @DisplayName("Teste de sessão com UUID")
    void testUuidSession() {
        String uuid = java.util.UUID.randomUUID().toString();
        given()
            .when()
                .queryParam("session", uuid)
                .queryParam("prompt", "Vue.js test with UUID session")
                .get("/ai/ask")
            .then()
                .statusCode(200)
                .header("Content-Type", "text/event-stream");
    }

    @Test
    @DisplayName("Teste de prompt com JSON-like content")
    void testJsonLikePrompt() {
        given()
            .when()
                .queryParam("session", "json-test")
                .queryParam("prompt", "Como passar dados JSON para componentes Vue.js: {name: 'test', value: 123}?")
                .get("/ai/chatbot")
            .then()
                .statusCode(200)
                .header("Content-Type", "text/event-stream");
    }
}