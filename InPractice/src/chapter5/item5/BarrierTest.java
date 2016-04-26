package chapter5.item5;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.TimeUnit;

/**
 * 4，栅栏示例
 * <p>
 *     栅栏类似于闭锁，它能阻塞一组线程直到某个事件发生。
 *     栅栏和闭锁的关键区别在于，所有线程必须同时到达栅栏位置才能继续执行。
 *     闭锁用于等待时间，而栅栏用于等待其他线程。
 * <p>
 *     闭锁可以用来实现一些协议，例如：所有人都到了麦当劳之后再讨论下一步要做的事情。
 * <p>
 *     CyclicBarrier 可以使一定数量的参与方反复地在栅栏位置汇集，它在并行迭代算法中非常有用：
 *     这种算法通常将一个问题拆分成一系列相互独立的子问题。
 *     当线程到达栅栏位置时将调用 await 方法，这个方法将阻塞直到所有线程都到达栅栏位置。
 *     如果所有线程都到达了栅栏位置，那么栅栏将打开，此时所有线程都被释放，而栅栏将被重置以便下次使用。
 *     如果对 await 的调用超时，或者 await 阻塞的线程被中断，那么栅栏就被认为是打破了，
 *     所有阻塞的 await 调用都将终止并抛出 BrokenBarrierException。
 *
 *     还可以将一个 Runnable 对象传递给 CyclicBarrier 构造函数，当成功通过栅栏时会（在一个子任务线程中）
 *     执行它，但在阻塞线程被释放之前是不能执行的。
 * <p>
 * Created by liuchenwei on 2016/4/26.
 */
public class BarrierTest {

    private final CyclicBarrier barrier;

    // 在不涉及 I/O 操作或共享数据访问的计算问题中，当线程数量为 Ncpu 或 Ncpu + 1 时将获得最优的吞吐量。
    // 更多的线程并不会带来任何帮助，甚至在某种程度上会降低性能，因为多个线程将会在 CPU 和内存等资源上发生竞争。
    private final int count = Runtime.getRuntime().availableProcessors() + 1;

    public BarrierTest() {
        this.barrier = new CyclicBarrier(count, new Runnable() {

            @Override
            public void run() {
                System.out.println("全部到齐。出发！");
            }
        });
    }

    public void start() {
        for (int i = 0; i < count; i++) {
            new Thread(new Task(), "T" + i).start();
        }
    }

    private class Task implements Runnable {

        @Override
        public void run() {
            try {
                TimeUnit.MILLISECONDS.sleep((int) (Math.random() * 5000));
                System.out.println(Thread.currentThread().getName() + " 来了。");
                barrier.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (BrokenBarrierException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        new BarrierTest().start();
    }
}
