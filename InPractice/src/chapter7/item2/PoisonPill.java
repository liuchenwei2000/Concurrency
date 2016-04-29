package chapter7.item2;

import java.io.File;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * 2，毒药对象
 * <p>
 *     另一种关闭生产者-消费者服务的方式就是使用毒药对象。
 *     毒药指的是一个放在队列上的对象，含义是“当得到这个对象时，立即停止。”
 *     毒药对象将确保消费者在关闭之前首先完成队列中的所有工作，在提交毒药对象之前
 *     提交的所有工作都会被处理，而生产者在提交了毒药对象后将不会再提交任何工作。
 * <p>
 *     只有在生产者和消费者的数量都已知的情况下，才可以使用毒药对象。
 *     另外，只有在无界队列中，毒药对象才能可靠地工作。
 * <p>
 * Created by liuchenwei on 2016/4/29.
 */
public class PoisonPill {

    public static class IndexingService {

        /** 毒药对象 */
        private static final File POISON = new File("");

        private final BlockingQueue<File> queue = new LinkedBlockingQueue<>();

        private final CrawlerThread producer = new CrawlerThread();
        private final IndexerThread consumer = new IndexerThread();

        public void start() {
            producer.start();
            consumer.start();
        }

        public void stop() {
            producer.interrupt();
        }

        public void awaitTermination() throws InterruptedException {
            consumer.join();
        }

        private class CrawlerThread extends Thread {

            @Override
            public void run() {
                try {
                    // 爬取文件
                    queue.put(new File("..."));
                } catch (InterruptedException e) {
                    // 发生异常
                } finally {
                    while (true) {
                        try {
                            queue.put(POISON);// 放入毒药对象
                            break;
                        } catch (InterruptedException e) {
                            // 如果放入 POISON 失败则继续尝试
                            e.printStackTrace();
                        }
                    }
                }
            }
        }

        private class IndexerThread extends Thread {

            @Override
            public void run() {
                try {
                    while (true) {
                        File file = queue.take();
                        if (file == POISON) {// 遇到毒药对象则终止任务
                            break;
                        } else {
                            // 索引文件
                        }
                    }
                } catch (InterruptedException e) {
                }
            }
        }
    }
}
