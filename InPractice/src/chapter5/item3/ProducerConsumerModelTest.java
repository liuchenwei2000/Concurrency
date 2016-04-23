package chapter5.item3;

import java.util.Date;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * 生产者消费者模型示例
 * <p>
 *     生产者-消费者模式能带来许多性能优势，因为生产者和消费者可以并发地执行。
 *     如果一个是 I/O 密集型，另一个是 CPU 密集型，那么并发执行的吞吐率要高于串行执行的吞吐率。
 * <p>
 * Created by liuchenwei on 2016/4/23
 */
public class ProducerConsumerModelTest {

    public static void main(String[] args) {
        BlockingQueue<String> products = new LinkedBlockingQueue<>();

        // 注入 BlockingQueue，使得生产者和消费者共享队列
        new Thread(new Producer(products)).start();

        for (int i = 0; i < 3; i++) {
            new Thread(new Consumer(products)).start();
        }
    }

    /**
     * 生产者
     */
    public static class Producer implements Runnable{

        // 生产者和消费者都持有一个 BlockingQueue，由外部应用负责注入
        private final BlockingQueue<String> products;

        public Producer(BlockingQueue<String> products) {
            this.products = products;
        }

        @Override
        public void run() {
           while(true){
               try {
                   products.put(new Date().toString());
               } catch (InterruptedException e) {
                   e.printStackTrace();
                   Thread.currentThread().interrupt();
               }
           }
        }
    }

    /**
     * 消费者
     */
    public static class Consumer implements Runnable{

        private final BlockingQueue<String> products;

        public Consumer(BlockingQueue<String> products) {
            this.products = products;
        }

        @Override
        public void run() {
            while(true){
                try {
                    System.out.println(products.take());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    Thread.currentThread().interrupt();
                }
            }
        }
    }
}
