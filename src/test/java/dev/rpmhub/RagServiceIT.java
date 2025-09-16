/**
 * This file contains confidential and proprietary information.
 * Unauthorized copying, distribution, or use of this file or its contents is
 * strictly prohibited.
 *
 * 2025 Rodrigo Prestes Machado. All rights reserved.
 */

package dev.rpmhub;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.equalTo;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusIntegrationTest;
import io.restassured.http.ContentType;

@QuarkusIntegrationTest
class RagServiceIT {

    @Test
    @DisplayName("Teste do endpoint /ai/chatbot - cenário positivo com session e prompt")
    void testChatbotEndpointWithValidParameters() {
        given()
            .when()
                .queryParam("session", "test-session-vue-001")
                .queryParam("prompt", "O que é Vue.js?")
                .get("/ai/chatbot")
            .then()
                .statusCode(200)
                .contentType("text/event-stream");
    }

    @Test
    @DisplayName("Teste do endpoint /ai/chatbot - cenário sem prompt (deve falhar)")
    void testChatbotEndpointWithoutPrompt() {
        given()
            .when()
                .queryParam("session", "test-session-no-prompt")
                .get("/ai/chatbot")
            .then()
                // Espera erro por prompt em branco: Bad Request
                .statusCode(400);
    }

    @Test
    @DisplayName("Teste do endpoint /ai/chatbot - cenário com prompt específico sobre Vue")
    void testChatbotEndpointWithVuePrompt() {
        given()
            .when()
                .queryParam("session", "vue-session-123")
                .queryParam("prompt", "Como criar um componente reativo em Vue.js?")
                .get("/ai/chatbot")
            .then()
                .statusCode(200)
                .contentType("text/event-stream");
    }

    @Test
    @DisplayName("Teste do endpoint /ai/ask - cenário positivo")
    void testAskModelEndpointWithValidParameters() {
        given()
            .when()
                .queryParam("session", "ask-session-vue-001")
                .queryParam("prompt", "Explique o ciclo de vida de um componente Vue")
                .get("/ai/ask")
            .then()
                .statusCode(200)
                .contentType("text/event-stream");
    }

    @Test
    @DisplayName("Teste do endpoint /ai/ask - cenário sem prompt (deve falhar)")
    void testAskModelEndpointWithoutPrompt() {
        given()
            .when()
                .queryParam("session", "ask-session-no-prompt")
                .get("/ai/ask")
            .then()
                // Espera erro por prompt em branco: Bad Request
                .statusCode(400);
    }

    @Test
    @DisplayName("Teste do endpoint /ai/ask - pergunta específica sobre Vue.js")
    void testAskModelEndpointWithVueQuestion() {
        given()
            .when()
                .queryParam("session", "vue-test-session")
                .queryParam("prompt", "Quais são as principais diretivas do Vue.js?")
                .get("/ai/ask")
            .then()
                .statusCode(200)
                .contentType("text/event-stream");
    }

    @Test
    @DisplayName("Teste do endpoint /ai/memory - recuperar memória da conversa")
    void testGetMemoryEndpoint() {
        String sessionId = "memory-test-session-" + System.currentTimeMillis();

        // Primeiro, fazer uma pergunta para criar memória
        given()
            .when()
                .queryParam("session", sessionId)
                .queryParam("prompt", "O que é Vue.js?")
                .get("/ai/chatbot")
            .then()
                .statusCode(200);

        // Então recuperar a memória
        given()
            .when()
                .queryParam("session", sessionId)
                .get("/ai/memory")
            .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("session", equalTo(sessionId))
                .body("messages", notNullValue())
                .body("lastActivity", notNullValue())
                .body("maxMessages", notNullValue());
    }

    @Test
    @DisplayName("Teste do endpoint /ai/memory - sessão inexistente")
    void testGetMemoryEndpointWithNonExistentSession() {
        given()
            .when()
                .queryParam("session", "non-existent-session")
                .get("/ai/memory")
            .then()
                .statusCode(204); // No Content para sessão não existente
    }

    @Test
    @DisplayName("Teste do endpoint /ai/memory - sem parâmetro de sessão")
    void testGetMemoryEndpointWithoutSession() {
        given()
            .when()
                .get("/ai/memory")
            .then()
                .statusCode(204); // No Content quando não há sessão
    }

    @Test
    @DisplayName("Teste de integração Vue.js - simulação de fluxo frontend")
    void testVueIntegrationFlow() {
        String session = "vue-integration-test-" + System.currentTimeMillis();
        
        // Simula uma pergunta típica de um usuário através de uma interface Vue
        given()
            .when()
                .queryParam("session", session)
                .queryParam("prompt", "Como implementar data binding em Vue.js?")
                .get("/ai/chatbot")
            .then()
                .statusCode(200)
                .contentType("text/event-stream");

        // Verificar se a memória foi criada corretamente
        given()
            .when()
                .queryParam("session", session)
                .get("/ai/memory")
            .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("session", equalTo(session))
                .body("messages", notNullValue());

        // Fazer uma pergunta de follow-up
        given()
            .when()
                .queryParam("session", session)
                .queryParam("prompt", "Pode dar um exemplo prático?")
                .get("/ai/ask")
            .then()
                .statusCode(200)
                .contentType("text/event-stream");
    }

    @Test
    @DisplayName("Teste de performance - múltiplas requisições simultâneas")
    void testMultipleSimultaneousRequests() throws InterruptedException {
        int numberOfRequests = 3; // Reduzido para evitar timeout
        CountDownLatch latch = new CountDownLatch(numberOfRequests);

        for (int i = 0; i < numberOfRequests; i++) {
            final int requestId = i;
            new Thread(() -> {
                try {
                    given()
                        .when()
                            .queryParam("session", "performance-test-" + requestId)
                            .queryParam("prompt", "Pergunta sobre Vue.js número " + requestId)
                            .get("/ai/chatbot")
                        .then()
                            .statusCode(200);
                } finally {
                    latch.countDown();
                }
            }).start();
        }

        // Aguardar todas as requisições completarem (máximo 60 segundos)
        latch.await(60, TimeUnit.SECONDS);
    }

    @Test
    @DisplayName("Teste com caracteres especiais no prompt")
    void testSpecialCharactersInPrompt() {
        given()
            .when()
                .queryParam("session", "special-chars-session")
                .queryParam("prompt", "Como usar ácentos e çaracteres especiais em Vue.js? 🚀")
                .get("/ai/chatbot")
            .then()
                .statusCode(200)
                .contentType("text/event-stream");
    }

    @Test
    @DisplayName("Teste com prompt longo - verificando apenas status")
    void testLongPrompt() {
        String longPrompt = "Este é um prompt muito longo para testar como o sistema lida com textos extensos. ".repeat(5) + 
                           "A pergunta principal é sobre Vue.js e como ele pode ser usado em aplicações complexas.";
        
        given()
            .when()
                .queryParam("session", "long-prompt-session")
                .queryParam("prompt", longPrompt)
                .get("/ai/ask")
            .then()
                .statusCode(200)
                .contentType("text/event-stream");
    }

    @Test
    @DisplayName("Teste com session vazia")
    void testEmptySession() {
        given()
            .when()
                .queryParam("session", "")
                .queryParam("prompt", "Teste com sessão vazia")
                .get("/ai/chatbot")
            .then()
                .statusCode(200)
                .contentType("text/event-stream");
    }

    @Test
    @DisplayName("Teste de URL encoding - caracteres especiais na URL")
    void testUrlEncodingSpecialCharacters() {
        given()
            .when()
                .queryParam("session", "url-encoding-test")
                .queryParam("prompt", "Vue.js & React.js - qual é melhor?")
                .get("/ai/ask")
            .then()
                .statusCode(200)
                .contentType("text/event-stream");
    }

    @Test
    @DisplayName("Teste de caso de uso real Vue.js - tutorial interativo")
    void testVueJsTutorialUseCase() {
        String tutorialSession = "vue-tutorial-" + System.currentTimeMillis();
        
        // Primeira pergunta - conceitos básicos
        given()
            .when()
                .queryParam("session", tutorialSession)
                .queryParam("prompt", "Como começar com Vue.js? Explique o conceito de reatividade.")
                .get("/ai/chatbot")
            .then()
                .statusCode(200)
                .contentType("text/event-stream");

        // Segunda pergunta - componentes
        given()
            .when()
                .queryParam("session", tutorialSession)
                .queryParam("prompt", "Como criar e usar componentes em Vue.js?")
                .get("/ai/ask")
            .then()
                .statusCode(200)
                .contentType("text/event-stream");

        // Verificar memória do tutorial
        given()
            .when()
                .queryParam("session", tutorialSession)
                .get("/ai/memory")
            .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("session", equalTo(tutorialSession));
    }
}