package chapter7.item1;

import net.jcip.annotations.ThreadSafe;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 1，任务取消
 * <p>
 *     如果外部代码能在某个操作正常完整之前将其置位“完成”状态，则称为该操作是可取消的。
 *     取消某个操作的原因很多：用户请求取消、有时间限制的操作（超时）、
 *     应用程序事件（如多任务寻找答案，当某个任务找到答案时需要取消其他任务）、
 *     错误（如多线程下载图片到硬盘而硬盘空间已满）、关闭（主应用程序关闭）。
 * <p>
 *     Java 只有一些协作式的机制，使请求取消的任务和代码都遵循一种协商好的协议。
 *     比如设置某个“已请求取消”标志，而任务将定期地查看该标志，如果设置了该标志，任务将提前结束。
 * <p>
 * Created by liuchenwei on 2016/4/28.
 */
public class CancellableTest {

    /**
     * 素数生成器
     * <p>
     *     本类使用了一种简单的取消策略：客户代码通过调用 cancel 来请求取消，
     *     PrimeGenerator 在每次搜索下一个素数前首先检查是否存在取消请求，如果存在则退出。
     */
    @ThreadSafe
    private static class PrimeGenerator implements Runnable {

        private final List<BigInteger> primes = new ArrayList<>();

        // 使用此变量作为取消标志，必须为 volatile 类型
        private volatile boolean cancelled;

        @Override
        public void run() {
            BigInteger prime = BigInteger.ONE;
            while (!cancelled) {// 检查是否已请求取消
                prime = prime.nextProbablePrime();
                synchronized (this) {
                    primes.add(prime);
                }
            }
        }

        /**
         * 请求取消任务
         */
        public void cancel(){
            this.cancelled = true;
        }

        public synchronized List<BigInteger> get(){
            return new ArrayList<>(primes);
        }
    }

    public static void main(String[] args) {
        PrimeGenerator generator = new PrimeGenerator();
        new Thread(generator).start();

        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {// cancel 方法在 finally 块中调用，确保任务可被取消
            generator.cancel();
        }

        System.out.println(generator.get());
    }
}
