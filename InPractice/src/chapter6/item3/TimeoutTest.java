package chapter6.item3;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * 为任务设置时限
 * <p>
 *     如果某个任务无法在指定时间内完成，那么将不再需要它的结果，此时可以放弃此任务。
 *     在有限时间内执行任务的主要困难是要确保得到结果的时间不会超过限定的时间，或者在限定的时间内确信无法获得结果。
 *     使用支持时间限制的 Future.get 方法可以满足需求：
 *     当结果可用时，它将立即返回，如果在指定时限内没有计算出结果，则将抛出 TimeoutException。
 * <p>
 * Created by liuchenwei on 2016/4/27
 */
public class TimeoutTest {

    private static class LongTimeFunction implements Callable<Integer> {

        @Override
        public Integer call() throws Exception {
            Thread.sleep(1000);
            return Integer.MAX_VALUE;
        }
    }

    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(5);

        Future future = executorService.submit(new LongTimeFunction());

        // do something else

        try {
            // 只等待 2 秒
            System.out.println(future.get(2, TimeUnit.SECONDS));
        } catch (TimeoutException e) {// 超时处理
            /*
            * 当任务超时后应该立即停止，从而避免为继续计算一个不再使用的结果而浪费资源。
            * 要实现这个功能，可以由任务本身来管理它的限定时间，并且在超时后中止执行或取消任务。
            * 如果任务是可取消的，那么可以提前中止它，以免消耗过多的资源。
            */
            future.cancel(true);
        } catch (ExecutionException e) {
            e.printStackTrace();
            Throwable cause = e.getCause();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 限定时间的方法可以扩展到任意数量的任务上。创建多个任务，将其提交到一个线程池，
     * 保留多个 Future，并使用限时的 get 方法通过 Future 串行地获取每一个结果。
     * 但还可以使用更简单的方法—— ExecutorService.invokeAll。
     */
    private static void testInvokeAll() throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(5);

        List<LongTimeFunction> tasks = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            tasks.add(new LongTimeFunction());
        }

        /*
        * 使用支持限时的 invokeAll 将多个任务提交到一个 ExecutorService 并获得结果。
        * invokeAll 方法的参数为一组任务，并返回一组 Future。这两个集合有着相同的结构，
        * invokeAll 按照任务集合中迭代器的顺序将所有的 Future 添加到返回的集合中，
        * 从而使调用者能将各个 Future 与其标识的 Callable 关联起来。
        *
        * 当所有任务都执行完毕时，或者调用线程被中断时，或者超过指定时限时，invokeAll 将返回。
        * 当超过指定时限后，任何还未完成的任务都会被取消。当 invokeAll 返回后，
        * 每个任务要么正常地完成，要么被取消，而客户端代码可以调用 get 或 isCancelled 来判断属于哪种情况。
        */
        List<Future<Integer>> futures = executorService.invokeAll(tasks, 2, TimeUnit.SECONDS);
        for (Future<Integer> future : futures) {
            try {
                System.out.println(future.get());
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
    }
}
