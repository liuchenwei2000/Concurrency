package chapter7.item1;

import net.jcip.annotations.ThreadSafe;

import java.math.BigInteger;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * 2，中断
 * <p>
 * <p>
 * Created by liuchenwei on 2016/4/28.
 */
public class InterruptTest {

    /**
     * 不可靠的取消操作把生产者置于阻塞的操作中
     * <p>
     *     本例使用的取消机制无法与可阻塞的库函数实现良好的交互：
     *     如果使用这种机制的任务调用了一个阻塞方法（本例是 BlockingQueue.put），
     *     那么任务可能永远不会检查取消标志，因此任务永远不会结束。
     */
    @ThreadSafe
    private static class BadPrimeProducer implements Runnable {

        private final BlockingQueue<BigInteger> primes;

        // 取消标志
        private volatile boolean cancelled;

        public BadPrimeProducer(BlockingQueue<BigInteger> primes) {
            this.primes = primes;
        }

        @Override
        public void run() {
            BigInteger prime = BigInteger.ONE;
            while (!cancelled) {// 检查是否已请求取消
                prime = prime.nextProbablePrime();
                try {
                    // 如果方法阻塞到此处（比如队列被填满了），而消费者希望取消任务，
                    // 调用了 cancel 方法来设置取消标志，但生产者却永远不能检查这个标志。
                    // 因为它无法从阻塞的 put 方法中恢复过来（消费者也已停止从队列中取数，所以 put 方法将一直阻塞）
                    primes.put(prime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        /**
         * 请求取消任务
         */
        public void cancel(){
            this.cancelled = true;
        }
    }

    /**
     * 通过中断来取消
     * <p>
     *     线程中断是一种协作机制，线程可以通过这种机制来通知另一个线程，
     *     告诉它在合适的或者可能的情况下停止当前工作，并转而执行其他的工作。
     *     如果任务代码能够响应终端，那么可以使用中断作为取消机制，
     *     并且利用许多库类中提供的中断支持。通常，中断是实现取消的最合理方式。
     * <p>
     *     每个线程都有一个 boolean 类型的中断状态。当中断线程时，线程的中断状态将被设置为 true。
     *     Thread 类中包含了中断线程以及查询线程中断状态的方法：
     *     <li>interrupt 方法能中断目标线程。
     *     <li>isInterrupted 方法能返回目标线程的中断状态。
     *     <li>静态的 interrupted 方法将清除当前线程的中断状态，并返回它之前的值，这也是清除中断状态的唯一方法。
     *     使用这个方法应该小心，因为它会清除当前线程的中断状态。
     *     如果在调用 interrupted 时返回了 true，那么除非想屏蔽这个中断，否则必须对它进行处理：
     *     可以抛出 InterruptedException 或者再次调用 interrupt 来恢复中断状态。
     */
    @ThreadSafe
    private static class GoodPrimeProducer extends Thread {

        private final BlockingQueue<BigInteger> primes;

        public GoodPrimeProducer(BlockingQueue<BigInteger> primes) {
            this.primes = primes;
        }

        @Override
        public void run() {
            BigInteger prime = BigInteger.ONE;
            /*
            * 在每次循环中，有两个位置可以检测出中断：
            * 在阻塞的 put 方法调用中，以及在循环开始处查询中断状态。
            * 由于调用了阻塞的 put 方法，因此在这里并不一定需要进行显式的检测，
            * 但执行检测却会使 GoodPrimeProducer 对中断具有更高的响应性。
            * 因为这是在寻找素数任务之前检查中断的，而不是在任务完成之后。
            * 如果可中断的阻塞方法的调用频率并不高，不足以获得足够的响应性，
            * 那么像这样显式地检测中断状态能起到了一定的帮助作用。
            */
            while (!isInterrupted()) {
                prime = prime.nextProbablePrime();
                try {
                    primes.put(prime);
                } catch (InterruptedException e) {
                    // 响应中断，允许线程退出
                    System.out.println("Interrupted");
                    return;
                }
            }
        }

        /**
         * 请求取消任务
         */
        public void cancel(){
            /*
            * 调用 interrupt 并不意味着立即停止目标线程正在进行的工作，即不会真正地中断一个正在运行的线程。
            * 它只是传递了请求中断的消息，然后由线程在下一个合适的时刻中断自己。
            * 阻塞库的方法，如 Thread.sleep 和 Object.wait 等，将严格地处理这种请求，
            * 当它们收到中断请求或者在开始执行时发现某个已被设置好的中断状态时，将抛出
            * InterruptedException，表示阻塞操作由于中断而提前结束。
            */
            interrupt();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        BlockingQueue<BigInteger> primes = new LinkedBlockingQueue<>(100);

        GoodPrimeProducer producer = new GoodPrimeProducer(primes);

        producer.start();

        System.out.println(primes.take());
        try {
            TimeUnit.SECONDS.sleep(1);
        } finally {
            producer.cancel();
        }
    }
}
