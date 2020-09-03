import java.util.*;
import org.apache.kafka.clients.producer.*;
public class SimpleProducer {
  
   public static void main(String[] args) throws Exception{
           
      String topicName = "SimpleProducerTopic";
	  String key = "Key1";
	  String value = "Value-1";
      
      Properties props = new Properties();
      // bootstrap.servers is a list of kafka brokers, 2 brokers
      props.put("bootstrap.servers", "localhost:9092, localhost:9093");
      
      // It is just an array of bytes for kafka
      // Converting java objects in an array of bytes
      
      // StringSerializer because key and value both are strings
      props.put("key.serializer","org.apache.kafka.common.serialization.StringSerializer");         
      props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
	  
	  // Create a kafka producer object      
      Producer<String, String> producer = new KafkaProducer <>(props);
	
	  // Create a producer record object
	  //ProducerRecord<String, String> record = new ProducerRecord<>(topicName,key,value);
	  
	  // Returns Future<RecordMetadata>, information about message
	  // partition id and offset number where message resides	
	  // producer.send(record)
	  
	  for(int i=0; i<10; i++){
	  	ProducerRecord<String, String> record = new ProducerRecord<>(topicName,"Data: " + Integer.toString(i));
	  	producer.send(record);
	  }
      producer.close();
	  
	  System.out.println("SimpleProducer Completed.");
   }
}
