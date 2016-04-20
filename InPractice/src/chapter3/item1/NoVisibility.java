package chapter3.item1;

/**
 * 在没有同步的情况下共享变量
 * <p>
 *     在没有使用同步机制的情况下，无法保证主线程写入的 ready 值和 number 值对于读线程是可见的。
 *     另外，读线程也可能看到了写入 ready 的值，但却没有看到写入 number 的值，这种现象叫 重排序。
 *     在缺少同步的情况下，Java 内存模型允许编译器对操作顺序进行重排序。
 * <p>
 *     在没有同步的额情况下，编译器、处理器以及运行时都可能对操作的执行顺序进行意想不到的调整。
 *     在缺乏同步的多线程程序中，要相对内存操作的执行顺序进行判断几乎是不可能的。
 *     避免这个问题的方法是：只要有数据在多个线程之间共享，就使用正确的同步。
 * <p>
 * Created by liuchenwei on 2016/4/20.
 */
public class NoVisibility {

    private static boolean ready;
    private static int number;

    private static class ReaderThread extends Thread {

        @Override
        public void run() {
            while (!ready) {
                Thread.yield();
            }
            System.out.println(number);
        }
    }

    public static void main(String[] args) {
        new ReaderThread().start();
        number = 45;
        ready = true;
    }
}
