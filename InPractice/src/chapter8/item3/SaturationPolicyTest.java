package chapter8.item3;

import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 饱和策略示例
 * <p>
 * <p>
 * Created by liuchenwei on 2016/4/30
 */
public class SaturationPolicyTest {

    public static void main(String[] args) {
        ThreadPoolExecutor threadPool = (ThreadPoolExecutor) Executors.newFixedThreadPool(10);

        /**
         * AbortPolicy（中止策略）
         * <p>
         *     中止策略是默认的饱和策略，当新提交的任务无法保存到队列中等待执行时，
         *     该策略将抛出未检查的 RejectedExecutionException。
         */
        RejectedExecutionHandler policy = new ThreadPoolExecutor.AbortPolicy();
        threadPool.setRejectedExecutionHandler(policy);

        /**
         * DiscardPolicy（丢弃策略）
         * <p>
         *     当新提交的任务无法保存到队列中等待执行时，本策略会悄悄抛弃该任务。
         */
        new ThreadPoolExecutor.DiscardPolicy();

        /**
         * DiscardOldestPolicy（丢弃最旧的策略）
         * <p>
         *     当新提交的任务无法保存到队列中等待执行时，本策略会抛弃下一个将被执行的任务，然后尝试重新提交新的任务。
         *     如果工作队列是一个优先队列，那么本策略会抛弃优先级最高的任务，因此最好不要将本策略和优先队列放在一起使用。
         */
        new ThreadPoolExecutor.DiscardOldestPolicy();

        /**
         * CallerRunsPolicy（调用者运行策略）
         * <p>
         *     本策略实现了一种调节机制，该策略既不会丢弃任务，也不会抛出异常，
         *     而是将任务回退给调用者，从而降低新任务的流量。当线程池中的所有线程都被占用，并且工作队列被填满后，
         *     下一个任务会在调用 execute 方法的线程中执行。由于执行任务需要一定的时间，
         *     因此该线程至少在一段时间内不能提交任何任务，从而使得工作者线程有时间来处理完正在执行的任务。
         */
        new ThreadPoolExecutor.CallerRunsPolicy();
    }
}
