gRPC Message Pipeline - Instruções de Teste
============================================

Pré-requisitos
--------------
- Aplicação rodando (servidor gRPC na porta 9090)
- grpcurl instalado (https://github.com/fullstorydev/grpcurl) para testar os
  RPCs via linha de comando

RPCs disponíveis (service MessageService, proto em src/main/proto/message.proto)
---------------------------------------------------------------------------------
PublishMessage -> publica e persiste uma mensagem (RPC unário)
ListMessages   -> lista as mensagens persistidas no banco H2 (server streaming)

Como o serviço não usa reflection do gRPC habilitada, use a opção -proto do
grpcurl apontando para o arquivo .proto (execute os comandos a partir da raiz
do projeto).

Requisições de teste
---------------------

1) Publicar mensagem

grpcurl -plaintext \
  -import-path src/main/proto -proto message.proto \
  -d '{"content": "mensagem de teste"}' \
  localhost:9090 grpcmessagepipeline.MessageService/PublishMessage

Resposta esperada: {} (objeto vazio, RPC concluído com sucesso)

2) Listar mensagens

grpcurl -plaintext \
  -import-path src/main/proto -proto message.proto \
  localhost:9090 grpcmessagepipeline.MessageService/ListMessages

Resposta esperada: uma linha JSON por mensagem persistida, por exemplo:
{
  "id": "1",
  "content": "mensagem de teste",
  "receivedAt": "2026-07-20T20:41:47.123Z"
}

Conexão com o banco H2
------------------------
O banco é em memória (os dados são perdidos ao parar a aplicação). Como este
projeto não usa spring-boot-starter-web, o console H2 não roda embutido no
Tomcat/Spring MVC: em vez disso, a aplicação sobe o mini servidor web próprio
do H2 (H2ConsoleConfig) na porta 8082.

Passo a passo:
1) Com a aplicação rodando, acesse: http://localhost:8082
2) Na tela de login, preencha os campos:
   - Driver Class: org.h2.Driver
   - JDBC URL:     jdbc:h2:mem:grpcmessagepipelinedb
   - Usuário:      sa
   - Senha:        (deixe em branco)
3) Clique em "Connect"
4) Para ver as mensagens salvas, execute no editor SQL:
   SELECT * FROM MESSAGE_ENTITY;

Observação: a URL do JDBC deve ser exatamente igual à configurada em
application.yml (spring.datasource.url), senão o H2 abre um banco vazio
diferente do usado pela aplicação.

Sobre o fluxo (comparado ao projeto kafka-message-pipeline)
-------------------------------------------------------------
No projeto Kafka, o POST publicava no tópico e um listener assíncrono
consumia e persistia a mensagem. Aqui não há broker: o RPC PublishMessage já
entrega e persiste a mensagem na mesma chamada, assumindo o papel de
producer + consumer. O RPC ListMessages substitui o GET /messages.
