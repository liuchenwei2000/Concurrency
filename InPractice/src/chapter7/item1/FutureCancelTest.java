package chapter7.item1;

import java.util.concurrent.*;

/**
 * 4，通过 Future 来实现取消
 * <p>
 *     Future 是一种抽象机制，来管理任务的生命周期，处理异常，以及实现取消。
 * <p>
 *     Future 拥有一个 cancel 方法，该方法带有一个 boolean 类型的参数 mayInterruptIfRunning
 *     如果 mayInterruptIfRunning 为 true 并且任务当前正在某个线程中运行，那么这个线程能被中断。
 *     如果为 false，那就意味着若任务还没有启动，就不要运行它，这种方式应该用于那些不处理中断的任务中。
 * <p>
 *     如果执行任务的线程是由标准的 Executor 创建的，它实现了一种中断策略使得任务可以通过中断被取消，
 *     所以如果任务在标准的 Executor 中运行，并通过它们的 Future 来取消任务，
 *     则可以设置 mayInterruptIfRunning 为 true。当尝试取消某个任务时，不宜直接中断线程池，
 *     因为并不知道当中断请求到达时正在运行什么任务——只能通过任务的 Future 来实现取消。
 * <p>
 * Created by liuchenwei on 2016/4/29.
 */
public class FutureCancelTest {

    public static void main(String[] args) throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(5);

        // submit 方法将返回一个 Future 来描述任务。
        Future<?> task = executorService.submit(new Runnable() {

            @Override
            public void run() {
                System.out.println("start running...");
                try {
                    TimeUnit.SECONDS.sleep(2);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("stop running...");
            }
        });

        try {
            task.get(1, TimeUnit.SECONDS);
        } catch (TimeoutException e) {
            e.printStackTrace();
            // 接下来任务将被取消
        } catch (ExecutionException e) {
            e.printStackTrace();
            // 如果在任务中抛出了异常则重新抛出该异常
            throw new RuntimeException(e.getCause());
        }
        /*
        * 当抛出 TimeoutException 或 InterruptedException 时，
        * 如果确定不再需要结果，那么就可以调用 cancel 方法来取消任务。
        */
        finally {
            // 如果任务已经结束，那么执行取消操作也不会带来任何影响
            // 如果任务正在运行，那么将被中断。
            task.cancel(true);
        }
    }
}
