package chapter10.item1;

/**
 * 1，锁顺序死锁
 * <p>
 *     本例发生死锁的原因是：两个线程试图以不同的顺序来获得相同的锁。
 *     如果按照相同的顺序来请求锁，就不会出现循环的加锁依赖性，也就不会产生死锁。
 *     如果所有线程以固定的顺序来获得锁，那么在程序中就不会出现锁顺序死锁问题。
 * <p>
 *     要想验证锁顺序的一致性，需要对程序中的加锁行为进行全局分析。
 *     如果只是单独地分析每条获取多个锁的代码路径，那是不够的。
 * <p>
 * Created by liuchenwei on 2016/5/1
 */
public class LeftRightDeadLock {

    private final Object left = new Object();
    private final Object right = new Object();

    public void leftRight() {
        // 先获得 left 锁，再获得 right 锁
        synchronized (left) {
            synchronized (right) {
                // do something
            }
        }
    }

    public void rightLeft() {
        // 先获得 right 锁，再获得 left 锁
        synchronized (right) {
            synchronized (left) {
                // do something
            }
        }
    }
}
