package org.apache.kafka.streams.examples.wordcount;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.Produced;

import java.util.Arrays;
import java.util.Locale;
import java.util.Properties;
import java.util.concurrent.CountDownLatch;

public class wordCount {

	public static void main(String[] args){
	
		String INPUT_TOPIC = "streams-plaintext-input";
		String OUTPUT_TOPIC = "streams-wordcount-output";
	
		Properties config = new Properties();
		config.put(StreamsConfig.APPLICATION_ID_CONFIG,"wordcount");
		config.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG,"localhost:9092");
		config.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG,"earliest");
		config.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG,Serdes.String().getClass());
		config.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG,Serdes.String().getClass());		
	
		StreamsBuilder builder = new StreamsBuilder();
		
		// 1. stream from kafka
		KStream<String,String> source = builder.stream(INPUT_TOPIC);
		
		// 2. map values to lowercase
		KTable<String, Long> wordCounts = 
											source.mapValues(value->value.toLowerCase())
											
											// 3. flatmap values split by space
											.flatMapValues(value->Arrays.asList(value.split(" ")))
											
											// 4. select key to apply a key (discard the old key)
											.selectKey((key,value)->value)
											
											// 5. group by key before aggregation
											.groupByKey()
											
											// 6. count occurences
											.count();					
										
		// 7. to in order to write the results back to kafka
		wordCounts.toStream().to(OUTPUT_TOPIC, Produced.with(Serdes.String(), Serdes.Long()));
		
		KafkaStreams streams = new KafkaStreams(builder.build(),config);
		streams.start();
	}

}
