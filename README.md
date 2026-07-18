[[_TOC_]]
## Technologies Used 
- JDK - version 17
- Spring - version 5.3
- Hibernate - version 5.6
- Postgresql - version 14
- Apache-Tomcat - version 9
- ActiveMQ - version 5.14
- RabbitMQ-Server - version 3.7
- Redis - version 3.8
- MongoDB - version 4.2
- Solr - version 8.11
- GWT - version 2.10
- GWT Material - version 2.8

## Configuration IntelliJ IDEA
- File > Settings > Editor > Code Style > Java > Imports 
> increase "Class count to use import with" to 60 and "Names count to use static import with" to 30

- File > Settings > Version Control > Commit Dialog > Before Commit
> put tick to: "Reformat code", "Rearrange code", "Optimize imports", "Cleanup"

## Setup Postgresql

1) Ask from DevOps team to dump below schemas/databases for you from  Dev Environment !
 - 0
 - public
 - 23039
 - globalauth
 - qrtzdb
 - logaudit

2) Download & Install Postgresql 13 [_From_](https://www.enterprisedb.com/downloads/postgres-postgresql-downloads)

3) Edit Authentication Methods in pg_hba.conf and postgresql.conf files, from **scram-sha-256** to **md5**

>>>
On Windows these files are located in

```
C:\Program Files\PostgreSQL\13\data
```

Edit pg_hba.conf and change METHOD to md5 for all lines as below.
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

Edit postgresql.conf file, find **password_encryption** line, set value to **md5** as below.

```
password_encryption = md5               # md5 or scram-sha-256
```
>>>

4) Restart PostgresSQL Service.
> Task Manager --> Services --> postgresql-13 --> Right Click --> Restart

5) Connect to PostgreSQL server using Psql Client like PGAdmin.

6) Execute Database Statements

Create Roles
```bash
CREATE ROLE wfmtest WITH LOGIN NOSUPERUSER INHERIT CREATEDB NOCREATEROLE NOREPLICATION PASSWORD 'wfm';
CREATE ROLE wftauth WITH LOGIN NOSUPERUSER INHERIT CREATEDB NOCREATEROLE NOREPLICATION PASSWORD 'wft';
```

Create multischemafree database and restore dump of schemas **public,0,23039**
```SQL
CREATE DATABASE multischemafree;
GRANT CONNECT ON DATABASE multischemafree TO wfmtest;
GRANT ALL PRIVILEGES ON DATABASE multischemafree TO wfmtest;
```
Create globalauth database and restore dump of database **globalauth**
```SQL
CREATE DATABASE globalauth;
GRANT CONNECT ON DATABASE globalauth TO wftauth;
GRANT ALL PRIVILEGES ON DATABASE globalauth TO wftauth;
```
Create logaudit database and restore dump of database **logaudit**
```SQL
CREATE DATABASE logaudit;
GRANT CONNECT ON DATABASE logaudit TO wfmtest;
GRANT ALL PRIVILEGES ON DATABASE logaudit TO wfmtest;
```
Create qrtzdb database and restore dump of database **qrtzdb**
```SQL
CREATE DATABASE qrtzdb;
GRANT CONNECT ON DATABASE qrtzdb TO wfmtest;
GRANT ALL PRIVILEGES ON DATABASE qrtzdb TO wfmtest;
```

## Setup Docker

Install Docker Desktop App On your PC
- Windows OS [_Docker Desktop For Windows_](https://hub.docker.com/editions/community/docker-ce-desktop-windows) 
- MacOS [Docker Desktop For Mac](https://hub.docker.com/editions/community/docker-ce-desktop-mac)

> The Docker Desktop installation includes Docker Engine, Docker CLI client, Docker Compose, Docker Content Trust, Kubernetes, and Credential Helper.

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

## Setup standard docker environment (MongoDB, Redis, RabbitMQ, Solr, Kafka)

Open Command Line(CMD/Terminal) on your PC as Administrator privileges (Right Click on Command Prompt & Run As Administrator)

### Option 1: Using Makefile (Recommended) *

**Prerequisites:** Make installed (comes with macOS/Linux, see [setup guide](https://medium.com/@samsorrahman/how-to-run-a-makefile-in-windows-b4d115d7c516) for Windows)

```bash
# Start (MongoDB, Redis, RabbitMQ, Solr, Kafka)
make up

# Check status
make ps

# View logs
make logs

# Stop services
make down
```

📖 **Full documentation:** [Makefile Guide](docs/makefile.md)

---

### Option 2: Using Docker Compose Directly

**Prerequisites:** Docker & Docker Compose installed

```bash
# Start all services (standard environment)
docker compose -f conf/docker/docker-compose.standard.yml up -d

# Check status
docker compose -f conf/docker/docker-compose.standard.yml ps

# View logs
docker compose -f conf/docker/docker-compose.standard.yml logs -f

# Stop services
docker compose -f conf/docker/docker-compose.standard.yml down
```

📖 **Full documentation:** [Docker Compose Guide](docs/docker.md) or [Docker Manual Guide](docs/docker-manual.md)

---

## Service Credentials

After starting services, you can connect using these credentials:

| Service        | Credentials Location                        | Default Access            |
|----------------|---------------------------------------------|---------------------------|
| **RabbitMQ**   | `conf/docker/rabbitmq/etc/definitions.json` | http://localhost:15672    |
| **MongoDB**    | `conf/docker/mongodb/mongo-init.js`         | mongodb://localhost:27017 |
| **Redis**      | No auth by default                          | redis://localhost:6379    |
| **MinIO**      | Environment file                            | http://localhost:9001     |
| **Solr**       | No auth by default                          | http://localhost:8983     |

---

Also, do not forget to change hibernate.properties file in your project 

cluster.solr.free.url=http://127.0.0.1:8983/solr/
cluster.solr.paid.url=http://127.0.0.1:8983/solr/
cluster.solr.paid1.url=http://127.0.0.1:8983/solr/

must look like this and do not commit this file.
