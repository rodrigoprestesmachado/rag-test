# Testes REST Assured para Endpoints RAG com Foco em Vue.js

Este projeto contém testes automatizados usando REST Assured para validar os endpoints da aplicação RAG (Retrieval-Augmented Generation) com foco especial em perguntas relacionadas ao Vue.js.

## Estrutura dos Testes

### `RagServiceTest.java`
Testes completos e abrangentes para todos os endpoints:

#### Endpoints Testados

**1. `/ai/chatbot` - Server-Sent Events**
- ✅ Teste com parâmetros válidos (session + prompt)
- ✅ Teste sem prompt (deve falhar com status 500)
- ✅ Teste com prompt específico sobre Vue.js
- ✅ Teste com caracteres especiais no prompt
- ✅ Teste com prompt longo
- ✅ Teste com sessão vazia

**2. `/ai/ask` - Server-Sent Events**
- ✅ Teste com parâmetros válidos
- ✅ Teste sem prompt (deve falhar com status 500)
- ✅ Teste com pergunta específica sobre Vue.js
- ✅ Teste de URL encoding com caracteres especiais

**3. `/ai/memory` - JSON Response**
- ✅ Teste de recuperação de memória existente
- ✅ Teste com sessão inexistente (status 204)
- ✅ Teste sem parâmetro de sessão (status 204)

#### Cenários Especiais

**Integração Vue.js**
- ✅ Simulação de fluxo frontend completo
- ✅ Tutorial interativo com múltiplas perguntas
- ✅ Verificação de persistência de memória entre requisições

**Performance**
- ✅ Teste de múltiplas requisições simultâneas
- ✅ Teste de timeout e handling de requisições longas

### `RagControllerBasicTest.java`
Testes rápidos e focados especificamente em Vue.js:

#### Testes Específicos Vue.js
- ✅ **Componentes**: "Como criar componentes Vue.js com props?"
- ✅ **Diretivas**: "Explique as diretivas v-if e v-for do Vue.js"
- ✅ **Reatividade**: "Como funciona o sistema de reatividade do Vue.js?"
- ✅ **Composition API**: "Qual a diferença entre Options API e Composition API?"

#### Testes de Validação
- ✅ Validação de headers HTTP
- ✅ Validação de Content-Type (text/event-stream para SSE)
- ✅ Validação de status codes corretos

## Como Executar os Testes

### Executar todos os testes básicos (mais rápido):
```bash
./mvnw verify -Dtest=RagControllerBasicIT
```

### Executar testes específicos do Vue.js:
```bash
./mvnw verify -Dtest="RagControllerBasicIT#testVue*"
```

### Executar um teste específico:
```bash
./mvnw verify -Dtest="RagControllerBasicIT#testVueComponentsQuestion"
```

### Executar todos os testes completos:
```bash
./mvnw verify -Dtest=RagServiceIT
```

## Características dos Testes

### Content-Type Validação
Os testes validam que:
- Endpoints `/ai/chatbot` e `/ai/ask` retornam `text/event-stream` (SSE)
- Endpoint `/ai/memory` retorna `application/json`

### Status Codes Esperados
- **200**: Requisições válidas com prompt
- **204**: Endpoint memory para sessões inexistentes
- **500**: Requisições sem prompt (texto em branco)

### Casos de Teste Vue.js

#### 1. **Componentes Vue.js**
```http
GET /ai/chatbot?session=vue-components-test&prompt=Como criar componentes Vue.js com props?
```

#### 2. **Diretivas Vue.js**
```http
GET /ai/ask?session=vue-directives-test&prompt=Explique as diretivas v-if e v-for do Vue.js
```

#### 3. **Sistema de Reatividade**
```http
GET /ai/chatbot?session=vue-reactivity-test&prompt=Como funciona o sistema de reatividade do Vue.js?
```

#### 4. **Composition API vs Options API**
```http
GET /ai/ask?session=vue-composition-api-test&prompt=Qual a diferença entre Options API e Composition API no Vue.js?
```

### Fluxo de Integração Testado

1. **Pergunta inicial** sobre Vue.js via `/ai/chatbot`
2. **Verificação de memória** via `/ai/memory`
3. **Pergunta de follow-up** via `/ai/ask`
4. **Validação da persistência** da conversa

## Dados de Teste Utilizados

### Base de Conhecimento
Os testes utilizam documentos sobre Vue.js incluindo:
- `instancia.pdf`: Documentação sobre instâncias Vue
- `eventos.txt`: Informações sobre eventos e manipuladores

### Sessões de Teste
Cada teste usa sessões únicas com prefixos identificadores:
- `vue-*`: Testes específicos de Vue.js
- `test-*`: Testes gerais
- `performance-*`: Testes de performance
- Timestamps são usados para garantir unicidade

## Melhores Práticas Implementadas

### ✅ Isolamento de Testes
Cada teste usa uma sessão única para evitar interferência

### ✅ Timeouts Configurados
Testes com timeouts apropriados para evitar execução infinita

### ✅ Validação Completa
- Status codes
- Headers HTTP
- Content-Type
- Estrutura de resposta

### ✅ Casos de Erro
Testes para cenários de falha (prompt vazio, sessão inexistente)

### ✅ Caracteres Especiais
Validação com acentos, emojis e caracteres especiais

### ✅ Performance
Testes de múltiplas requisições simultâneas

## Resultados dos Testes

Exemplo de saída bem-sucedida:
```
[INFO] Tests run: 3, Failures: 0, Errors: 0, Skipped: 0
[INFO] BUILD SUCCESS
```

Os testes validam que a aplicação RAG está funcionando corretamente para:
- ✅ Responder perguntas sobre Vue.js
- ✅ Manter contexto de conversação
- ✅ Lidar com caracteres especiais
- ✅ Retornar formatos corretos (SSE/JSON)
- ✅ Gerenciar sessões de usuário

## Observações Importantes

### Server-Sent Events (SSE)
Os endpoints de chat retornam streams de eventos, não texto simples. Os testes validam:
- Content-Type: `text/event-stream`
- Status 200 para início do stream
- Headers apropriados para SSE

### Dependências de Serviços
Os testes dependem de:
- Ollama (modelo de IA)
- ChromaDB (banco vetorial)
- Redis (cache/memória)

Porém, estes serviços são iniciados automaticamente via Testcontainers durante os testes.
