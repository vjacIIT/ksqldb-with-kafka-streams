package kafka.tools;
import java.util.*;
import java.io.*;
import org.apache.kafka.common.*;
import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.ConsumerRecord;

public class StreamConsumer {
    
    public static void main(String[] args) throws Exception{

		String topicName = "streams-wordcount-output";
		String groupName = "SimpleGroup";
		
		Properties props = new Properties();
		props.put("bootstrap.servers", "localhost:9092");
		props.put("group.id", groupName);
		props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
		props.put("value.deserializer", "org.apache.kafka.common.serialization.LongDeserializer");
		props.put("enable.auto.commit", "true");
		props.put("session.timeout.ms","30000");


        KafkaConsumer<String,Long> consumer = new KafkaConsumer<>(props);
        
        consumer.subscribe(Arrays.asList(topicName));
        try{
            while (true){
                ConsumerRecords<String, Long> records = consumer.poll(100);
                for (ConsumerRecord<String, Long> record : records){
                   System.out.printf(record.key()+" ");
				   System.out.println(record.value());
                }
            }
        }catch(Exception ex){
            System.out.println("Exception.");
            ex.printStackTrace();
        }
        finally{
                consumer.close();
        }
    }
    
}
