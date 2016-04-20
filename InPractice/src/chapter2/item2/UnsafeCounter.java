package chapter2.item2;

import net.jcip.annotations.NotThreadSafe;

/**
 * 不安全的计数器实现
 * <p>
 * <p>
 * Created by liuchenwei on 2016/4/20.
 */
@NotThreadSafe
public class UnsafeCounter {

    private int counter = 0;

    public void service() {
        // do something
        ++counter;
    }
}
