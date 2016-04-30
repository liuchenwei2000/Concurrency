package chapter8.item3;

import net.jcip.annotations.ThreadSafe;

import java.util.concurrent.*;

/**
 * 使用 Semaphore 来控制任务的提交速率
 * <p>
 *     这实际上是一种自定义的饱和策略。
 * <p>
 * Created by liuchenwei on 2016/4/30
 */
@ThreadSafe
public class BoundedExecutor {

    /** 线程池固定线程数 */
    private static final int N_THREADS = 10;

    private final Executor executor;

    private final Semaphore semaphore;

    /**
     * @param tasks 任务队列最大任务数
     */
    public BoundedExecutor(int tasks) {
        // 固定大小的线程池，使用无界队列存放任务
        this.executor = new ThreadPoolExecutor(N_THREADS, N_THREADS, 0L,
                TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());
        // 信号量的上界设置为 线程池的大小 + 可排队任务的数量
        this.semaphore = new Semaphore(N_THREADS + tasks);
    }

    public void submitTask(final Runnable runnable) throws InterruptedException {
        semaphore.acquire();

        executor.execute(new Runnable() {

            @Override
            public void run() {
                try {
                    runnable.run();
                } finally {
                    semaphore.release();
                }
            }
        });
    }
}
