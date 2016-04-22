package chapter4.item3;

import net.jcip.annotations.NotThreadSafe;
import net.jcip.annotations.ThreadSafe;

import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 线程安全性的委托
 * <p>
 * <p>
 * Created by liuchenwei on 2016/4/22.
 */
public class ThreadSafeDelegating {

    /**
     * 本类将它的线程安全性委托给 AtomicInteger 来保证，因此本类是线程安全的。
     */
    @ThreadSafe
    public class Cache {

        private final AtomicInteger count = new AtomicInteger(0);

        public void put() {
            count.incrementAndGet();
        }
    }

    /**
     * 不光可以委托给单个线程安全的状态变量，还可以将线程安全性委托给多个状态变量，
     * 只要这些变量时彼此独立的，即组合而成的类并不会在其包含的多个状态变量上增加任何不变性条件。
     */
    @ThreadSafe
    public class UIComponent {

        // 使用 CopyOnWriteArrayList 来保存各个监听器列表，它是线程安全的List，特别适用于管理监听器列表。
        // 每个 List 都是线程安全的，由于各个状态之间不存在耦合关系，所以本类可以将线程安全性委托给这两个 List。
        private final List<KeyListener> keyListeners = new CopyOnWriteArrayList<>();
        private final List<MouseListener> mouseListeners = new CopyOnWriteArrayList<>();

        public void addKeyListener(KeyListener listener) {
            keyListeners.add(listener);
        }

        public void removeKeyListener(KeyListener listener) {
            keyListeners.remove(listener);
        }

        public void addMouseListener(MouseListener listener) {
            mouseListeners.add(listener);
        }

        public void removeMouseListener(MouseListener listener) {
            mouseListeners.remove(listener);
        }
    }

    /**
     * 大多数组合对象的状态变量之间存在着某些不变性条件，仅靠委托并不足以保护它的不变性条件。
     * 本类不是线程安全的，因为没有维持对下界和上届进行约束的不变性条件。
     * setLower/setUpper 方法都是“先检查后执行”操作，没有使用加锁机制保证操作的原子性。
     * 如果一个线程调用 setLower(5)，另一个线程调用 setUpper(4)，
     * 在一些错误的执行时序中，这两个调用都会通过检查，并且设置成功，造成不变性条件被破坏。
     * <p>
     * 虽然 AtomicInteger 是线程安全的，但经过组合得到的类却不是。
     * 因为状态变量 lower 和 upper 不是彼此独立的，因此本类不能将线程安全性委托给它们。
     * 可以通过加锁机制来维护不变性条件以确保线程安全性，例如使用同一个锁来保护 lower 和 upper。
     * <p>
     * 如果某个类含有复合操作，那么仅靠委托并不足以实现线程安全性。
     * 在这种情况下，这个类必须提供自己的加锁机制以保证复合操作是原子操作，除非整个复合操作都可以委托给状态变量。
     */
    @NotThreadSafe
    public class NumberRange {

        // 不变性条件：lower <= upper
        private final AtomicInteger lower = new AtomicInteger(0);
        private final AtomicInteger upper = new AtomicInteger(0);

        public void setLower(int l) {
            // 不安全的“先检查后执行”
            if(l > upper.get()){
                throw new IllegalArgumentException("lower cannot greater than upper");
            }
            lower.set(l);
        }

        public void setUpper(int u) {
            if(u < lower.get()){
                throw new IllegalArgumentException("upper cannot less than lower");
            }
            upper.set(u);
        }
    }
}
