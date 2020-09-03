Remove containers
docker rm ksqldb-cli ksqldb-server broker zookeeper postgres connect schema_registry

Run ksqldb
docker exec -it ksqldb-cli ksql http://ksqldb-server:8088

Run postgresql
docker exec -it postgres bash
su postgres
psql

create table book(id integer primary key, author varchar, title varchar, genre varchar, price int, pub_date date, review varchar);

insert into book Values(1, 'Writer_1', 'The First Book', 'Fiction', 44, '2000-10-01', 'Amazing story of nothing'), (2, 'Writer_2', 'The Second Book', 'Fiction', 50, '2001-10-01', 'Good story');

select * from book;

select * from pg_replication_slots;
select pg_drop_replication_slot('debezium')


#Create topic
docker exec broker kafka-topics --create --zookeeper zookeeper:2181 --replication-factor 1 --partitions 1 --topic books

#See the topic
docker run --tty --network ksqldb_default edenhill/kafkacat:1.5.0 kafkacat -b broker:9092 -L
				OR
docker exec broker kafka-topics --list --zookeeper zookeeper:2181

#Delete topic
docker exec broker kafka-topics --delete --zookeeper zookeeper:2181 --topic topic_name

CHECK THIS FOR OUTPUT
curl -H "Accept:application/json" localhost:8083
curl -H "Accept:application/json" localhost:8083/connectors


curl -X POST -H "Accept:application/json" -H "Content-Type:application/json" --data @postgres-source-connector.json http://localhost:8083/connectors

curl -X POST -H "Accept:application/json" -H "Content-Type:application/json" --data @xml-source-connector.json http://localhost:8083/connectors


curl -X DELETE localhost:8083/connectors/postgres-source-connector
curl -X DELETE localhost:8083/connectors/xml-source-connector

# See all the connectors available
curl http://localhost:8083/connector-plugins | jq '.'

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
  
create table book(id integer primary key, author string, title string, genre string, price int, pub_date string, review string) with (kafka_topic='topic_book', value_format='json');

# In postgresql
insert into book Values(3, 'Writer_3', 'The Third Book', 'Fiction', 100, '2002-10-02', 'Ordianary book with great language');
