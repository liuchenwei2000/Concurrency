package chapter7.item3;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

/**
 * 未捕获异常的处理
 * <p>
 *     Thread API 中提供了 UncaughtExceptionHandler，它能检测出某个线程由于未捕获的异常而终结的情况。
 *     当一个线程由于未捕获异常而退出时，JVM 会把这个事件报告给应用程序提供的 UncaughtExceptionHandler。
 *     如果没有提供任何未捕获异常处理器，那么默认会将栈追踪信息输出到 System.err。
 * <p>
 *     在运行时间较长的应用程序中，通常会为所有线程的未捕获异常指定同一个
 *     UncaughtExceptionHandler，并且至少会将异常信息记录到日志中。
 * <p>
 * Created by liuchenwei on 2016/4/29.
 */
public class UncaughtExceptionHandlerTest {

    /**
     * 将异常写入日志的 UncaughtExceptionHandler
     */
    public static class LogUncaughtExceptionHandler implements Thread.UncaughtExceptionHandler {

        @Override
        public void uncaughtException(Thread t, Throwable e) {
            System.out.println("Thread[" + t.getName() + "] terminated with exception:" + e.getMessage());
        }
    }

    public static void main(String[] args) {
        /*
        * 为自建线程设置 UncaughtExceptionHandler
        */
        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                double r = 1 / 0;
            }
        }, "computor");

        // 为线程设置未捕获异常处理器
        thread.setUncaughtExceptionHandler(new LogUncaughtExceptionHandler());
        thread.start();

        /*
        * 为线程池工作线程设置 UncaughtExceptionHandler
        *
        * 标准线程池允许当发生未捕获异常时结束线程，因此当线程结束时，将有新的线程来代替它。
        * 如果没有提供 UncaughtExceptionHandler 或者其他故障通知机制，那么任务会悄悄失败。
        */
        Executors.newFixedThreadPool(5, new ThreadFactory() {

            @Override
            public Thread newThread(final Runnable r) {
                // 如果希望在任务由于发生异常而失败时获得通知，
                // 并且执行一些特定于任务的恢复操作，可以将任务封装到能捕获异常的 Runnable 中。
                Thread t = new Thread(new Runnable() {

                    @Override
                    public void run() {
                        try {
                            r.run();
                        } catch (Exception e) {
                            e.printStackTrace();
                            // 执行一些特定于任务的恢复操作
                        }
                    }
                });
                // 为线程设置未捕获异常处理器
                t.setUncaughtExceptionHandler(new LogUncaughtExceptionHandler());
                return t;
            }
        });

        /*
        * 只有通过 execute 提交的任务，才能将它抛出的异常交给未捕获异常处理器，
        * 而通过 submit 提交的任务，无论抛出的是未检查异常还是已检查异常，都将被认为是任务返回状态的一部分。
        * 如果一个由 submit 提交的任务由于抛出了异常而结束，那么这个异常将被
        * Future.get 封装在 ExecutionException 中重新抛出。
        */
    }
}
