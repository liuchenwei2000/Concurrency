package chapter6.item3;

import java.util.concurrent.*;

/**
 * Future、Callable、FutureTask 示例
 * <p>
 * <p>
 * Created by liuchenwei on 2016/4/27
 */
public class FutureTest {

    /**
     * 许多任务实际上是存在延迟的计算——执行数据库查询，从网络上获取资源，或者计算某个复杂的功能。
     * 对于这些任务，Callable 是一种更好的抽象：
     * 它认为主入口点（Callable 的 call 方法）将返回一个值，并可能抛出一个异常。
     */
    private static class LongTimeFunction implements Callable<Integer> {

        @Override
        public Integer call() throws Exception {
            Thread.sleep(1000);
            return Integer.MAX_VALUE;
        }
    }

    public static void main(String[] args) {
        ExecutorService executorService = Executors.newSingleThreadExecutor();

        /*
        * ExecutorService 中的所有 submit 方法都将返回一个 Future。
        * 可以讲一个 Callable 或 Runnable 提交给 ExecutorService，
        * 并得到一个 Future 用来获得任务的执行结果或取消任务。
        * Future 表示一个任务的生命周期，并提供了相应的方法来判断是否已经完成或取消，以及获取任务的结果和取消任务等。
        */
        Future<Integer> future = executorService.submit(new LongTimeFunction());
        try {
            /*
            * get 方法的行为取决于任务的状态（尚未开始、正在运行、已完成）。
            * 如果任务已经完成，那么 get 方法会立即返回或者抛出一个异常，
            * 如果任务没有完成，那么 get 方法将阻塞并直到任务完成。
            * 如果任务抛出了异常，那么 get 方法将该异常封装为 ExecutionException 并重新抛出，
            * 可以通过 getCause 方法重新获取被封装的初始异常。
            * 如果任务被取消，那么 get 将抛出 CancellationException。
            */
            System.out.println(future.get());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();// 重设线程的中断状态
        } catch (ExecutionException e) {
            e.printStackTrace();
            Throwable cause = e.getCause();
        }

        Future<?> future1 = executorService.submit(new Runnable() {

            @Override
            public void run() {
                System.out.println("Running...");
            }
        });

        // 在 Executor 框架中，已提交但尚未开始的任务可以被取消，
        // 但对于那些已经开始执行的任务，只有当它们能响应中断时，才能取消。
        future1.cancel(true);

        /*
        * 也可以显式地为某个指定的 Callable 或 Runnable 实例化一个 FutureTask。
        * 由于 FutureTask 实现了 Runnable，因此可以将它提交给 Executor 来执行，
        * 或者直接调用它的 run 方法。
        */
        FutureTask futureTask = new FutureTask(new LongTimeFunction());
        executorService.submit(futureTask);
    }
}
