package chapter6.item2;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * 3，ExecutorService 示例
 * <p>
 *     Executor 的实现通常会创建线程来执行任务，但 JVM 只有在所有（非守护）线程全部终止后才会退出。
 *     因此，如果无法正确地关闭 Executor，那么 JVM 就无法结束。
 * <p>
 *     由于 Executor 以异步方式来执行任务，因此在任何时刻，之前提交任务的状态不是立即可见的。
 *     有些任务可能已经完成，有些可能正在运行，而其他的任务可能在队列中等待执行。
 *     当关闭应用程序时，可能采用最平缓的关闭形式（完成所有已经启动的任务，并且不再接受任何新的任务），
 *     也可能采用最粗暴的关闭方式（直接断电）以及其他各种可能的形式。
 * <p>
 *     为了解决执行服务的生命周期问题，ExecutorService 扩展了 Executor 接口，
 *     添加了一些用于生命周期管理的方法（还有一些用于任务提交的便利方法）。
 * <p>
 * Created by liuchenwei on 2016/4/27.
 */
public class ExecutorServiceTest {

    public static void main(String[] args) throws InterruptedException {
        // ExecutorService 的生命周期有3种状态：运行、关闭和已终止，它被创建时处于运行状态。
        ExecutorService executorService = Executors.newFixedThreadPool(5);

        // shutdown 方法将执行平缓的关闭过程：
        // 不再接受新的任务，同时等待已经提交的任务执行完成——包括哪些还未开始执行的任务。
        executorService.shutdown();

        // shutdownNow 方法将执行粗暴的关闭过程：
        // 它将尝试取消所有运行中的任务，并且不再启动队列中尚未开始执行的任务。
        executorService.shutdownNow();

        // 等所有任务都完成后，ExecutorService 将转入终止状态。
        // 可以调用 awaitTermination 来等待 ExecutorService 到达终止状态。
        executorService.awaitTermination(10, TimeUnit.SECONDS);

        // 通过 isTerminated 方法来判断 ExecutorService 是否已经终止。
        // 注意，除非首先调用 shutdown 或 shutdownNow，否则 isTerminated 永不为 true。
        System.out.println(executorService.isTerminated());

        // 通过 isShutdown 方法来判断 ExecutorService 执行程序是否已关闭。
        System.out.println(executorService.isShutdown());
    }
}
