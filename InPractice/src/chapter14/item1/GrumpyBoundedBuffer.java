package chapter14.item1;

import net.jcip.annotations.ThreadSafe;

import java.util.concurrent.TimeUnit;

/**
 * 1，将前提条件的失败传递给调用者
 * <p>
 *     本类实现了一个简单的有界缓存：
 *     put 和 take 方法都进行了同步以确保实现对缓存状态的独占访问，
 *     因此这两个方法在访问缓存时都采用“先检查后运行”的逻辑策略。
 * <p>
 * Created by liuchenwei on 2016/5/4.
 */
@ThreadSafe
public class GrumpyBoundedBuffer<V> extends BaseBoundedBuffer<V> {

    public GrumpyBoundedBuffer(int capacity) {
        super(capacity);
    }

    public synchronized void put(V v) throws BufferFullException {
        // 当不满足前提条件时，有界缓存不会执行相应的操作，而是直接抛出异常
        // 实际上，缓存已满并不是有界缓存的一个异常条件。
        if (isFull()) {
            throw new BufferFullException();
        }
        doPut(v);
    }

    public synchronized V take() throws BufferEmptyException {
        if (isEmpty()) {
            throw new BufferEmptyException();
        }
        return doTake();
    }

    public static void main(String[] args) throws InterruptedException {
        // 在实现缓存时得到的简化并不能抵消在使用时存在的复杂性，
        // 因为调用者必须捕获异常，并且在每次缓存操作时都需要重试。
        GrumpyBoundedBuffer<String> buffer = new GrumpyBoundedBuffer(10);
        while (true) {
            try {
                System.out.println(buffer.take());
                break;
            } catch (BufferEmptyException e) {
                /*
                * 调用者也可以不进入休眠状态，而直接重新调用 take 方法，这被称为忙等待或自旋等待。
                * 如果缓存的状态在很长一段时间内都不会发生变化，那么该方式会消耗大量的 CPU 时间。
                * 客户代码必须要在二者之间进行选择：
                * 要么容忍自旋导致的 CPU 时钟周期浪费，要么容忍由于休眠而导致的地响应性。
                */
                TimeUnit.NANOSECONDS.sleep(10);
            }
        }
    }
}
