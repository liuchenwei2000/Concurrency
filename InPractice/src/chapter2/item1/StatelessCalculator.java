package chapter2.item1;

import net.jcip.annotations.ThreadSafe;

/**
 * 无状态的类
 * <p>
 * 它既不包含任何域，也不包含任何对其他类中域的引用。
 * 计算过程中的临时状态仅存在于线程栈上的局部变量中，并且只能由正在执行的线程访问。
 * 访问 StatelessCalculator 实例的线程不会影响另一个访问同一个 StatelessCalculator
 * 实例的线程的计算结果，因为这两个线程并没有共享状态，就像它们在访问不同的实例。
 * 由于线程访问无状态对象的行为并不会影响其他线程中操作的正确性，因此无状态对象是线程安全的。
 * <p>
 * Created by liuchenwei on 2016/4/20.
 */
@ThreadSafe
public class StatelessCalculator {

    public int add(int a, int b) {
        int result = a + b;
        return result;
    }
}
