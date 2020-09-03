import java.util.*;
import org.apache.kafka.clients.producer.*;
public class StreamProducer {
  
   public static void main(String[] args) throws Exception{
           
      String topicName = "streams-plaintext-input";
      String key = "Key1";
      
      Properties props = new Properties();
      props.put("bootstrap.servers", "localhost:9092");
      props.put("key.serializer","org.apache.kafka.common.serialization.StringSerializer");         
      props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
	        
      Producer<String, String> producer = new KafkaProducer <>(props);

		Scanner scanner = new Scanner(System.in);

		for(int i=0; i<10; i++) {
			String[] tokens = scanner.nextLine().split(" ");
			for(int l=0; l<tokens.length; l++){
				ProducerRecord<String, String> record = new ProducerRecord<>(topicName,key,tokens[l]);
		    	producer.send(record);
				//System.out.println(tokens[l]);
			}
		}
		scanner.close();	
      producer.close();
	  
	  System.out.println("SimpleProducer Completed.");
   }
}
