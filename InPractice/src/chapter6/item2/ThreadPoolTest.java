package chapter6.item2;

import java.util.concurrent.Executors;

/**
 * 线程池
 * <p>
 *     线程池是指管理一组同构工作线程的资源池。通过重用现有的线程而不是创建新线程，
 *     可以在处理多个请求时分摊在线程创建和销毁过程中产生的巨大开销。
 *     另外，当请求到达时，工作线程通常已经存在，因此不会由于等待创建线程而延迟任务的执行，提高了响应性。
 *     通过适当地调整线程池的大小，可以创建足够多的线程以便使处理器保持忙碌状态。
 *     同时还可以防止过多线程相互竞争资源而使应用程序耗尽内存或失败。
 * <p>
 * Created by liuchenwei on 2016/4/26
 */
public class ThreadPoolTest {

    public static void main(String[] args) {
        // 可以通过 Executors 中的静态工厂方法来创建线程池

        /*
        * 创建单个工作线程来执行任务，如果这个线程异常结束，会创建另一个线程来替代。
        */
        Executors.newSingleThreadExecutor();

        /*
        * 创建一个固定长度的线程池，每当提交一个任务时就创建一个线程，直到达到线程池的最大数量。
        * 如果某个线程由于发生了异常而结束，线程池会补充一个新的线程。
        */
        Executors.newFixedThreadPool(10);

        /*
        * 创建一个可缓存的线程池，如果线程池的当前规模超过了处理需求时，将回收空闲的线程，
        * 而当需求增加时，则可以添加新的线程，线程池的规模不存在任何限制。
        */
        Executors.newCachedThreadPool();

        /*
        * 创建一个固定长度的线程池，而且以延迟或定时的方式来执行任务。
        */
        Executors.newScheduledThreadPool(10);
    }
}
