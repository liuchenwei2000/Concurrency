package chapter5.item4;

import java.util.concurrent.BlockingQueue;

/**
 * 中断线程示例
 * <p>
 * <p>
 * Created by liuchenwei on 2016/4/23
 */
public class Task implements Runnable {

    private final BlockingQueue<String> queue;

    public Task(BlockingQueue<String> queue) {
        this.queue = queue;
    }

    @Override
    public void run() {
        /*
        * 当某方法抛出 InterruptedException 时，表示该方法是一个阻塞方法，
        * 如果这个方法被中断，那么它将努力提前结束阻塞状态。
        *
        * 当在代码中调用一个将抛出 InterruptedException 的方法时，
        * 你自己的方法也就变成了一个阻塞方法，并且必须要处理对终端的响应：
        *
        * 1，传递 InterruptedException，继续抛出该异常。
        * 2，恢复中断。如本例。
        */
        try {
            System.out.println(queue.take());
        } catch (InterruptedException e) {
            e.printStackTrace();
            // 有时候不能抛出 InterruptedException，比如代码是 Runnable 的一部分时。
            // 这种情况下，必须捕获 InterruptedException，并通过调用当前线程上的 interrupt 方法恢复中断状态，
            // 这样在调用栈中更高层的代码将看到引发了一个中断。
            Thread.currentThread().interrupt();
        }
    }
}