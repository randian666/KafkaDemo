import org.apache.kafka.clients.producer.*;

import java.util.Properties;

public class ProducerTest {
    public static void main(String[] args) {
        Properties props = new Properties();
        props.put("bootstrap.servers", "192.168.38.135:9092");
        props.put("acks", "all");//判别请求是否为完整的条件（就是是判断是不是成功发送了）
        props.put("retries", 0);//如果请求失败，生产者会自动重试，我们指定是0次，如果启用重试，则会有重复消息的可能性。
        props.put("batch.size", 16384);//(生产者)缓存每个分区未发送消息。缓存的大小是通过 batch.size 配置指定的。值较大的话将会产生更大的批。并需要更多的内存（因为每个“活跃”的分区都有1个缓冲区）。
        props.put("linger.ms", 1);//linger(逗留)时间
        props.put("buffer.memory", 33554432);//控制生产者可用的缓存总量，如果消息发送速度比其传输到服务器的快，将会耗尽这个缓存空间。当缓存空间耗尽，其他发送调用将被阻塞，阻塞时间的阈值通过max.block.ms设定，之后它将抛出一个TimeoutException。
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        Producer<String, String> producer = new KafkaProducer<String, String>(props);
        for(int i = 0; i < 100; i++)
            producer.send(new ProducerRecord<String, String>("test", Integer.toString(i), "hello lis"+Integer.toString(i)), new Callback() {
                public void onCompletion(RecordMetadata recordMetadata, Exception e) {
                    if (e!=null)
                        e.printStackTrace();
                    System.out.println("send result offset is :"+recordMetadata.offset());
                }
            });

        producer.close();
    }
}
