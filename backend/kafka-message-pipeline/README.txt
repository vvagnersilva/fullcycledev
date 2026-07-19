Kafka Message Pipeline - Instruções de Teste
========================================

Pré-requisitos
--------------
- Aplicação rodando na porta 8080
- Broker Kafka disponível em localhost:9092 (necessário para o POST /messages,
  pois ele publica no tópico "messages-topic")

Endpoints disponíveis
----------------------
POST /messages  -> publica uma mensagem no Kafka
GET  /messages  -> lista as mensagens persistidas no banco H2

Requisições de teste
---------------------

1) Publicar mensagem (POST)

curl -X POST http://localhost:8080/messages \
  -H "Content-Type: application/json" \
  -d '{"content": "mensagem de teste"}'

Resposta esperada: HTTP 201 Created (sem corpo)

2) Listar mensagens (GET)

curl http://localhost:8080/messages

Resposta esperada: lista JSON com as mensagens salvas no H2

Conexão com o banco H2
------------------------
O banco é em memória (os dados são perdidos ao parar a aplicação).

Passo a passo:
1) Com a aplicação rodando, acesse: http://localhost:8080/h2-console
2) Na tela de login, preencha os campos:
   - Driver Class: org.h2.Driver
   - JDBC URL:     jdbc:h2:mem:messagepipelinedb
   - Usuário:      sa
   - Senha:        (deixe em branco)
3) Clique em "Connect"
4) Para ver as mensagens salvas, execute no editor SQL:
   SELECT * FROM MESSAGE_ENTITY;

Observação: a URL do JDBC deve ser exatamente igual à configurada em
application.yml (spring.datasource.url), senão o H2 abre um banco vazio
diferente do usado pela aplicação.

Kafka Magic (interface para inspecionar o Kafka)
---------------------------------------------------
Kafka Magic é uma ferramenta gráfica para visualizar tópicos e mensagens
do Kafka. Ela roda em um container Docker.

Subir o container:
docker run -e "KMAGIC_ALLOW_SCHEMA_DELETE=true" -d --name kafka-magic --rm --network host digitsy/kafka-magic

Observação: usamos --network host (em vez de mapear porta com -p) porque o
broker Kafka local anuncia a si mesmo como "localhost:9092"
(KAFKA_ADVERTISED_LISTENERS). Com host networking o container compartilha a
rede do host, então "localhost:9092" dentro do container aponta certo para
o broker. Com rede bridge normal a conexão inicial funciona, mas as
requisições seguintes falham.

Acessar a interface:
http://localhost

Cadastrar a conexão com o cluster (primeira vez):
1) Clique em "Add Cluster" (ou similar) na tela inicial
2) Preencha:
   - Cluster Name:      kafka-message-pipeline
   - Bootstrap Servers:  localhost:9092
   - Demais campos (Schema Registry, etc.): deixe em branco
3) Salve e conecte

Parar o container quando não precisar mais:
docker stop kafka-magic
