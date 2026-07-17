
## Setup RabbitMQ

Open Command Line(CMD/Terminal) on your PC as Administrator privileges (Right Click on Command Prompt & Run As Administrator)

Create General Network for our docker services if not created yet!

```bash
docker network create kpi-network
```
Change directory to `conf/docker/rabbitmq/` & execute below Command from the RabbitMQ directory to build our custom rabbitmq Image.

And Start RabbitMQ Service.
```bash
docker-compose up -d
```
> You may Find Login Credentials to Connect to RabbitMQ Instance, From File `etc/definitions.json`

## Setup MongoDB

Open Command Line(CMD/Terminal) on your PC as Administrator privileges (Right Click on Command Prompt & Run As Administrator)

Create General Network for our docker services if not created yet!

```bash
docker network create kpi-network
```

Change directory to `conf/docker/mongodb/` & execute below Command from the MongoDB directory to build our custom mongodb Image.
```bash
docker-compose up -d
```
> You may Find Login Credentials to Connect to MongoDB Instance, From File `mongo-init.js`

## Setup ActiveMQ

Open Command Line(CMD/Terminal) on your PC as Administrator privileges (Right Click on Command Prompt & Run As Administrator)

Create General Network for our docker services if not created yet!

```bash
docker network create kpi-network
```

Change directory to `conf/docker/activemq/`

And Start ActiveMQ Service.
```bash
docker-compose up -d
```

## Setup Redis

Open Command Line(CMD/Terminal) on your PC as Administrator privileges (Right Click on Command Prompt & Run As Administrator)

Create General Network for our docker services if not created yet!

```bash
docker network create kpi-network
```

Change directory to `conf/docker/redis/`

And Start Redis Service.
```bash
docker-compose up -d
```
## Setup Solr

Open Command Line(CMD/Terminal) on your PC as Administrator privileges (Right Click on Command Prompt & Run As Administrator)

Create General Network for our docker services if not created yet!

```bash
docker network create kpi-network
```

Change directory to `conf/docker/solr/`

And Start Solr Service.
```bash
docker-compose up -d
```