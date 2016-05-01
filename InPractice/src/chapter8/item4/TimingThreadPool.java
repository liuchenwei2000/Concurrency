package chapter8.item4;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 扩展 ThreadPoolExecutor
 * <p>
 *     ThreadPoolExecutor 是可扩展的，它提供了可以在子类中覆盖的方法：
 *     beforeExecute、afterExecute 和 terminated，这些方法可以用于扩展行为。
 * <p>
 *     在执行任务的线程中将调用 beforeExecute 和 afterExecute 方法，
 *     在这两个方法中可以添加日志、计时、监视或统计信息收集的功能。
 *     无论任务是从 run 中正常返回，还是抛出一个异常返回，afterExecute 都会被调用。
 *     如果 beforeExecute 抛出一个运行时异常，那么任务将不被执行，并且 afterExecute 也不会被调用。
 *     在线程池完成关闭操作时调用 terminated，也就是在所有任务都已经完成并且所有工作线程也已经关闭后。
 *     terminated 可以用来释放 Executor 在其生命周期里分配的各种资源，
 *     此外，还可以执行发送通知、记录日志或者手机 finalize 统计信息等操作。
 * <p>
 *     本例通过扩展上述三个方法来添加日志记录和统计信息收集。
 * <p>
 * Created by liuchenwei on 2016/5/1
 */
public class TimingThreadPool extends ThreadPoolExecutor {

    /*
    * 记录任务开始时间
    *
    * beforeExecute 必须记录开始时间并把它保存在一个 afterExecute 可以访问的地方。
    * 因为这些方法将在执行任务的线程中调用，因此需要使用 ThreadLocal。
    */
    private ThreadLocal<Long> startTime = new ThreadLocal<>();

    private AtomicLong tasks = new AtomicLong();// 已处理任务数
    private AtomicLong totalTime = new AtomicLong();// 总任务数

    public TimingThreadPool() {
        super(10, 20, 60, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());
    }

    @Override
    protected void beforeExecute(Thread t, Runnable r) {
        super.beforeExecute(t, r);
        startTime.set(System.nanoTime());
    }

    @Override
    protected void afterExecute(Runnable r, Throwable t) {
        try {
            tasks.incrementAndGet();

            long endTime = System.nanoTime();
            long taskTime = endTime - startTime.get();

            totalTime.addAndGet(taskTime);
        } finally {
            super.afterExecute(r, t);
        }

    }

    /**
     * 输出包含平均任务时间的日志
     */
    @Override
    protected void terminated() {
        try {
            System.out.printf("Terminated: total task=%d %n", tasks.get());
            System.out.printf("Terminated: total time=%d ns %n", totalTime.get());
            System.out.printf("Terminated: avg time=%d ns %n", (totalTime.get() / tasks.get()));
        } finally {
            super.terminated();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        TimingThreadPool timingThreadPool = new TimingThreadPool();

        for (int i = 0; i < 5; i++) {
            timingThreadPool.execute(new Runnable() {

                @Override
                public void run() {
                    try {
                        TimeUnit.SECONDS.sleep((long) Math.random());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
        }

        timingThreadPool.shutdown();
        timingThreadPool.awaitTermination(5,TimeUnit.SECONDS);
    }
}
