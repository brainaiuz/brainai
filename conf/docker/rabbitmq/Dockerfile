FROM rabbitmq:3.8.16-management

ARG DEBIAN_FRONTEND=noninteractive

RUN apt-get update

RUN apt-get install -y curl

COPY rabbitmq_delayed_message_exchange-3.8.0.ez $RABBITMQ_HOME/plugins/rabbitmq_delayed_message_exchange-3.8.0.ez

RUN rabbitmq-plugins enable --offline rabbitmq_delayed_message_exchange

RUN rabbitmq-plugins enable --offline rabbitmq_consistent_hash_exchange
