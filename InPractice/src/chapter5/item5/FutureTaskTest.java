package chapter5.item5;

import java.util.concurrent.*;

/**
 * 2，FutureTask 示例
 * <p>
 *     FutureTask 也可以用做闭锁。它实现了 Future 语义，表示一种抽象的可生成结果的计算。
 *     FutureTask 表示的计算是通过 Callable 来实现的，相当于一种可生成结果的 Runnable，
 *     并且可以处于以下三种状态：等待运行、正在运行和运行完成。
 *     “运行完成”表示计算的所有可能结束方式，包括正常结束、由于取消而结束和由于异常而结束。
 *     当 FutureTask 进入完成状态后，它会永远停止在这个状态上。
 * <p>
 *     FutureTask 在 Executor 框架中表示异步任务，还可以用来表示一些时间较长的计算，
 *     这些计算可以在使用计算结果（调用 get 方法）之前启动。
 * <p>
 * Created by liuchenwei on 2016/4/23
 */
public class FutureTaskTest {

    private final FutureTask<String> future;

    public FutureTaskTest() {
        // FutureTask 将计算结果从执行计算的线程传递到获取这个结果的线程，
        // 它的规范确保了这种传递过程实现结果的安全发布。
        this.future = new FutureTask<String>(new Callable<String>() {

            @Override
            public String call() throws Exception {
                // 模拟耗时的计算
                TimeUnit.SECONDS.sleep(5);
                return "Hello World";
            }
        });
    }

    /**
     * 由于在构造函数或静态初始化中启动线程并不是好方法，所以提供本方法来启动线程。
     * 这里模拟在使用计算结果前的提前计算，当程序随后需要计算结果时，可以调用 get 方法。
     */
    public void start(){
        new Thread(future).start();
    }

    public String getResult() throws InterruptedException {
        try {
            // Future.get 方法的行为取决于任务的状态。
            // 如果任务已经完成，那么 get 会立即返回结果，
            // 否则 get 将阻塞直到任务进入完成状态，然后返回结果或者抛出异常。
            return future.get();
        }
        /*
        * Callable 可以抛出受检查的或未受检查的异常，并且任何代码都可能抛出一个 Error。
        * 无论任务代码抛出什么异常，都会被封装到一个 ExecutionException 中，并在 Future.get() 中被重新抛出。
        */
        catch (ExecutionException e) {
            Throwable cause = e.getCause();
            // get 方法抛出的异常可能是：Callable抛出的受检查异常、RuntimeException、Error
            // 这里要对每种情况进行单独处理。
            if (cause instanceof RuntimeException) {
                throw (RuntimeException) cause;
            } else if (cause instanceof Error) {
                throw (Error) cause;
            } else {
                throw new IllegalStateException("Not unchecked", cause);
            }
        }
    }

    public static void main(String[] args) {
        FutureTaskTest task = new FutureTaskTest();
        task.start();// 先提前计算
        // do something else
        try {
            // 主线程需要计算结果
            System.out.println(task.getResult());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
