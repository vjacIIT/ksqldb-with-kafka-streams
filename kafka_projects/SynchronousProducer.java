import java.util.*;
import org.apache.kafka.clients.producer.*;
import com.typesafe.config.ConfigFactory;

public class SynchronousProducer {
	
	public static void main(String[] args) throws Exception{
	
		String topicName = "SynchronousProducerTopic";
		String key = "key1";
		String value = "value1";
		
		Properties props = new Properties();
		props.put("bootstrap.servers","localhost:9092,localhost:9093");
		props.put("key.serializer","org.apache.kafka.common.serialization.StringSerializer");
		props.put("value.serializer","org.apache.kafka.common.serialization.StringSerializer");
		
		Producer<String,String> producer = new KafkaProducer <>(props);
		
		ProducerRecord<String,String> record = new ProducerRecord<>(topicName,key,value);
		
		try{
			RecordMetadata metadata = producer.send(record).get();
			System.out.println("Message is sent to Partition no " + metadata.partition() + " and offset " + metadata.offset());
		}
		catch(Exception e){
			e.printStackTrace();
			System.out.println("SynchronousProducer failed with an exception");
		}
		finally{
			producer.close();
		}
		
	}
		
}
