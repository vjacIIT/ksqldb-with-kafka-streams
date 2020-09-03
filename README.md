# ksqldb-with-kafka-streams
Project work done during my internship at GE Healthcare.

####       1. This work is partial because GE has the copyright to all the work. This repository only contains content which is available publically. 
####       2. All commands need to be as a super user
####       3. Kafka_projects contains java files for producer and consumer from terminal

#### Prerequisites:
Install docker

#### Go to ksqldb folder
cd ksqldb

#### Remove containers
docker rm ksqldb-cli ksqldb-server broker zookeeper postgres
#### It should say No such container: container_name

#### Run docker file (Terminal 1)
docker-compose up

#### Run postgresql (Terminal 2)
docker exec -it postgresql bash
su postgres
psql

create table book(id integer primary key, author varchar, title varchar, genre varchar, price int, pub_date date, review varchar);
insert into book Values(1, 'Writer_1', 'The First Book', 'Fiction', 44, '2000-10-01', 'Amazing story of nothing'), (2, 'Writer_2', 'The Second Book', 'Fiction', 50, '2001-10-01', 'Good story');
select * from book;               # (Do not close the terminal)

#### Run ksqldb (Terminal 3)
docker exec -it ksqldb-cli ksql http://ksqldb-server:8088

CREATE SOURCE CONNECTOR jdbc_source WITH (
  'connector.class'          = 'io.confluent.connect.jdbc.JdbcSourceConnector',
  'connection.url'           = 'jdbc:postgresql://postgres:5432/postgres',
  'connection.user'          = 'postgres',
  'connection.password'      = 'password',
  'topic.prefix'             = 'topic_',
  'table.whitelist'          = 'book',
  'mode'                     = 'incrementing',
  'numeric.mapping'          = 'best_fit',
  'incrementing.column.name' = 'id',
  'key'                      = 'id',
  'key.converter'            = 'org.apache.kafka.connect.converters.IntegerConverter');
  
#### See if topic_books is created
show topics;

create table book(id integer primary key, author string, title string, genre string, price int, pub_date string, review string) with (kafka_topic='topic_book', value_format='json');
select * from book emit changes;     # (Do not close the terminal)

#### Now whatever changes (update, insertion) we do in book table in postgresql it will be shown in ksqldb table also
#### In postgresql (Terminal 2)
insert into book Values(3, 'Writer_3', 'The Third Book', 'Fiction', 100, '2002-10-02', 'Ordianary book with great language');

#### In ksqldb (Terminal 3)
select * from book emit changes;
