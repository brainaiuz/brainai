# Kpi.com
> Kpi.com, this is Online ERP Solution to manage your business,
> [_More_](https://kpi.com).

## Technologies Used 
- JDK - version 17
- Postgresql - version 14.10
- Apache-Tomcat - version 9.0.95
- RabbitMQ-Server - version 5.17.0
- Redis - version 3.2
- MongoDB - version 4.2
- Solr - version 8.11.2

## Setup Postgresql

##### 1) Ask from DevOps team to dump below schemas/databases for you from Kpi Dev Environment !
 - 0 
 - public 
 - globalauth
 - multischemafree 
 - qrtzdb
 - logaudit

##### 2) Download & Install Postgresql 14 [_From_](https://www.enterprisedb.com/downloads/postgres-postgresql-downloads)

##### 3) Edit Authentication Methods in pg_hba.conf and postgresql.conf files, from **scram-sha-256** to **md5**

>> On Windows these files are located in

```
C:\Program Files\PostgreSQL\14\data
```

##### Edit pg_hba.conf and change METHOD to md5 for all lines as below.
```
# TYPE  DATABASE        USER            ADDRESS                 METHOD

# "local" is for Unix domain socket connections only
local   all             all                                     md5
# IPv4 local connections:
host    all             all             127.0.0.1/32            md5
# IPv6 local connections:
host    all             all             ::1/128                 md5
# Allow replication connections from localhost, by a user with the
# replication privilege.
local   replication     all                                     md5
host    replication     all             127.0.0.1/32            md5
host    replication     all             ::1/128                 md5
```

##### Edit postgresql.conf file, find **password_encryption** line, set value to **md5** as below.

```
password_encryption = md5               # md5 or scram-sha-256
```


##### 4) Restart PostgresSQL Service.
> Task Manager --> Services --> postgresql-14 --> Right Click --> Restart

##### 5) Connect to PostgreSQL server using Psql Client like PGAdmin.

##### 6) Execute Database Statements

### Create Roles
```bash
CREATE ROLE wfmtest WITH LOGIN NOSUPERUSER INHERIT CREATEDB NOCREATEROLE NOREPLICATION PASSWORD 'wfm';
CREATE ROLE wftauth WITH LOGIN NOSUPERUSER INHERIT CREATEDB NOCREATEROLE NOREPLICATION PASSWORD 'wft';
```

### Create multischemafree database and restore dump of schemas **public,0,23039**
```SQL
CREATE DATABASE multischemafree;
GRANT CONNECT ON DATABASE multischemafree TO wfmtest;
GRANT ALL PRIVILEGES ON DATABASE multischemafree TO wfmtest;
```
### Create globalauth database and restore dump of database **globalauth**
```SQL
CREATE DATABASE globalauth;
GRANT CONNECT ON DATABASE globalauth TO wftauth;
GRANT ALL PRIVILEGES ON DATABASE globalauth TO wftauth;
```
### Create logaudit database and restore dump of database **logaudit**
```SQL
CREATE DATABASE logaudit;
GRANT CONNECT ON DATABASE logaudit TO wfmtest;
GRANT ALL PRIVILEGES ON DATABASE logaudit TO wfmtest;
```
### Create qrtzdb database and restore dump of database **qrtzdb**
```SQL
CREATE DATABASE qrtzdb;
GRANT CONNECT ON DATABASE qrtzdb TO wfmtest;
GRANT ALL PRIVILEGES ON DATABASE qrtzdb TO wfmtest;
```

## Setup Docker

Install Docker Desktop App On your PC
- Windows OS [_Docker Desktop For Windows_](https://hub.docker.com/editions/community/docker-ce-desktop-windows) 
- MacOS [Docker Desktop For Mac](https://hub.docker.com/editions/community/docker-ce-desktop-mac)

>> The Docker Desktop installation includes Docker Engine, Docker CLI client, Docker Compose, Docker Content Trust, Kubernetes, and Credential Helper.

**If you are Using Windows OS, During the Docker Desktop installation it asks to install Windows Subsystems For Linux (WSL) Application, You may install it with below steps.**

<details>
<summary>WSL Installation On Windows.</summary>

Open Windows PowerShell as Administrator (Winkey --> PowerShell --> Right Click --> Run as Administrator) Execute the Commands Below one by one.

> Enable the Windows Subsystem for Linux
```PowerShell
dism.exe /online /enable-feature /featurename:Microsoft-Windows-Subsystem-Linux /all /norestart
```
> Enable Virtual Machine feature
```PowerShell
dism.exe /online /enable-feature /featurename:VirtualMachinePlatform /all /norestart
```
> Set WSL 2 as your default version
```PowerShell
wsl --set-default-version 2
```

Install Alpine Based WSL from [_here_](https://www.microsoft.com/store/apps/9p804crf0395)
 
Or Install your Favourite Linux distribution from Microsoft Store click [_here_](https://aka.ms/wslstore)
> To finalize Linux Distribution Installation it asks to type login and password for your linux, please feel free to type any simple (Non-Standart)login and passwords

> If you want to go through Official Microsoft Steps for Above WSL Installation please click  [_here_](https://docs.microsoft.com/en-us/windows/wsl/install-win10#simplified-installation-for-windows-insiders)
</details>

Please make sure your Docker Desktop Application Up and Running, if not, Please try to restart your PC

## Setup RabbitMQ

Open Command Line(CMD/Terminal) on your PC as Administrator privileges (Right Click on Command Prompt & Run As Administrator)

Create General Network for our docker services if not created yet!

```bash
docker network create kpi
```
Change directory to /YouProjectHomeDirectory/Installation/Docker/RabbitMQ/ & execute below Command from the RabbitMQ directory to build our custom rabbitmq Image.

```bash
docker image build -t kpi/rabbitmq:latest .
```
And Start RabbitMQ Service.
```bash
docker-compose up -d
```
> You may Find Login Credentials to Connect to RabbitMQ Instance, From File /YouProjectHomeDirectory/Installation/Docker/RabbitMQ/etc/definitions.json

## Setup MongoDB

Open Command Line(CMD/Terminal) on your PC as Administrator privileges (Right Click on Command Prompt & Run As Administrator)

Create General Network for our docker services if not created yet!

```bash
docker network create kpi
```

Change directory to /YouProjectHomeDirectory/Installation/Docker/MongoDB/ & execute below Command from the MongoDB directory to build our custom mongodb Image.
```bash
docker image build -t mongodb:kpi .
```
And Start MongoDB Service.
```bash
docker-compose up -d
```
> You may Find Login Credentials to Connect to MongoDB Instance, From File /YouProjectHomeDirectory/Installation/Docker/MongoDB/mongo-init.js

## Setup ActiveMQ

Open Command Line(CMD/Terminal) on your PC as Administrator privileges (Right Click on Command Prompt & Run As Administrator)

Create General Network for our docker services if not created yet!

Change directory to /YouProjectHomeDirectory/Installation/Docker/ActiveMQ/ 

And Start ActiveMQ Service.
```bash
docker-compose up -d
```

## Setup Redis

Open Command Line(CMD/Terminal) on your PC as Administrator privileges (Right Click on Command Prompt & Run As Administrator)

Change directory to /YouProjectHomeDirectory/Installation/Docker/Redis/ 

And Start Redis Service.
```bash
docker-compose up -d
```

## Setup Kafka


Open Command Line(CMD/Terminal) on your PC as Administrator privileges (Right Click on Command Prompt & Run As Administrator)

Change directory to /YouProjectHomeDirectory/Installation/Docker/Kafka/

And Start Kafka Service.

```bash
docker compose -f ./docker-compose.yaml up -d
```