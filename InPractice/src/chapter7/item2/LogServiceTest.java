package chapter7.item2;

import java.io.PrintStream;
import java.util.concurrent.*;

/**
 * 1，日志服务
 * <p>
 *     产生日志消息的线程并不会将消息直接写入输出流，而是通过调用
 *     log 方法将日志消息放入某个队列中，并由其他线程来处理。
 *     这是一种多生产者单消费者的设计模式。
 * <p>
 * Created by liuchenwei on 2016/4/29.
 */
public class LogServiceTest {

    /**
     * 不支持关闭的生产者-消费者日志服务
     * <p>
     * 如果不能终止日志线程，则 JVM 可能无法正常关闭。
     * 如果只是使日志线程退出，则会丢失那些正在等待被写入到日志的信息，
     * 不仅如此，其他线程将在调用 log 时被阻塞，因为日志消息队列是满的。
     */
    public static class LogServiceV1{
        
        private final BlockingQueue<String> queue;
        private final LoggerThread logger;

        public LogServiceV1() {
            this.queue = new LinkedBlockingQueue<>(100);
            this.logger = new LoggerThread();
        }
        
        public void start(){
            logger.start();
        }

        public void log(String message) throws InterruptedException {
            queue.put(message);
        }

        /**
         * 专门的线程来写日志
         */
        private class LoggerThread extends Thread {

            private final PrintStream writer = System.out;

            @Override
            public void run() {
                try {
                    while (true) {
                        writer.println(queue.take());
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    writer.close();
                }
            }
        }
    }

    /**
     * 使用 ExecutorService 的日志服务
     * <p>
     * ExecutorService 提供了两种关闭方法：shutdown 和 shutdownNow。
     * 前者会一直等到队列中的所有任务都执行完成后才关闭，在其他拥有线程的服务中也应该考虑类似的关闭方式。
     * 在复杂程序中，通常会将 ExecutorService 封装在某个更高级别的服务中，并且该服务能提供其自己的生命周期方法。
     */
    public static class LogServiceV2 {

        private final BlockingQueue<String> contents;

        private final ExecutorService threadPool;

        private final PrintStream writer = System.out;

        public LogServiceV2() {
            this.contents = new LinkedBlockingQueue<>(100);
            this.threadPool = Executors.newSingleThreadExecutor();
        }

        public void start() {
            threadPool.execute(new LoggerTask());
        }

        public void stop() throws InterruptedException {
            try {
                threadPool.shutdown();
                threadPool.awaitTermination(100, TimeUnit.SECONDS);
            } finally {
                writer.close();
            }
        }

        public void log(String content){
            try {
                contents.put(content);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        private class LoggerTask implements Runnable {

            @Override
            public void run() {
                while (true) {
                    try {
                        writer.println(contents.take());
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                }
            }
        }
    }
}
