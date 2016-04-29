package chapter7.item1;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * 3，响应中断
 * <p>
 *     当调用可中断的阻塞函数时，比如 Thread.sleep 或 BlockingQueue.put 等，
 *     有两种实用策略可用于处理 InterruptedException：
 *     <li>传递异常：从而使你的方法也称为可中断的阻塞方法。
 *     <li>恢复中断状态：从而使调用栈中的上层代码能够对其进行处理。
 * <p>
 * Created by liuchenwei on 2016/4/29.
 */
public class InterruptedExceptionTest {

    private final BlockingQueue<String> queue = new ArrayBlockingQueue<>(10);

    // 传递异常
    public String getNextString() throws InterruptedException {
        return queue.take();
    }

    // 传递异常
    public String getNextString2() throws InterruptedException {
        try {
            return queue.take();
        } catch (InterruptedException e) {
            System.out.println("Interrupted");// 执行一些清理工作
            throw e;
        }
    }

    /**
     * 如果不想或无法传递 InterruptedException，则需要寻找另外一种方式来保存中断请求。
     * 标准做法就是再次调用 interrupt 来恢复中断状态。不能屏蔽 InterruptedException，
     * 因为大多数代码并不知道它们将在哪个线程中运行，因此应该保存中断状态。
     */
    public String getNextString3() {
        String string = null;
        try {
            string = queue.take();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            return string;
        }
    }
}

