package chapter8.item3;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 线程工厂示例
 * <p>
 *     在许多情况下都需要使用定制的线程工厂：
 *     例如希望为线程池中的每个线程制定一个 UncaughtExceptionHandler；
 *     或者实例化一个定制的 Thread 类用于执行调试信息的记录；
 *     或许只是希望给线程取一个更有意义的名字，用来解释线程的转储信息和错误日志。
 * <p>
 * Created by liuchenwei on 2016/4/30
 */
public class ThreadFactoryTest {

    public static void main(String[] args) {
        ExecutorService threadPool = Executors.newCachedThreadPool(new MyThreadFactory("TEST"));
        // 在调用完 ThreadPoolExecutor 的构造函数后，仍然可以通过 setter 函数来修改
        // ThreadPoolExecutor 对象的参数，如基本大小、最大大小、存活时间、线程工厂等。
        ((ThreadPoolExecutor) threadPool).setKeepAliveTime(10, TimeUnit.SECONDS);

        for (int i = 0; i < 10; i++) {
            threadPool.execute(new Runnable() {

                @Override
                public void run() {
                    try {
                        TimeUnit.SECONDS.sleep((int) (Math.random() * 10));
                        String name = Thread.currentThread().getName();
                        System.out.println("[" + name + "] created=" + MyThread.getCreated() + ", alive=" + MyThread.getAlive());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
        }

        try {
            TimeUnit.SECONDS.sleep(20);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("created=" + MyThread.getCreated() + ", alive=" + MyThread.getAlive());

        threadPool.shutdown();
    }

    public static class MyThreadFactory implements ThreadFactory {

        private final String poolName;

        public MyThreadFactory(String poolName) {
            this.poolName = poolName;
        }

        @Override
        public Thread newThread(Runnable r) {
            return new MyThread(r, poolName);
        }
    }

    public static class MyThread extends Thread {

        /** 创建的线程计数器 */
        private static final AtomicInteger created = new AtomicInteger(0);
        /** 尚运行的线程计数器 */
        private static final AtomicInteger alive = new AtomicInteger(0);

        public MyThread(Runnable r, String poolName) {
            super(r, poolName + "-" + created.incrementAndGet());
            setUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {

                @Override
                public void uncaughtException(Thread t, Throwable e) {
                    System.out.println("Uncaught in thread " + t.getName() + ":" + e.getMessage());
                }
            });
        }

        @Override
        public void run() {
            try {
                alive.incrementAndGet();
                super.run();
            } finally {
                alive.decrementAndGet();
            }
        }

        public static int getCreated() {
            return created.get();
        }

        public static int getAlive() {
            return alive.get();
        }
    }
}
