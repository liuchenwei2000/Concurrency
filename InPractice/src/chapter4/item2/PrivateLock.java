package chapter4.item2;

import java.util.HashSet;
import java.util.Set;

/**
 * 通过一个私有锁来保护状态
 * <p>
 *     遵循 Java 监视器模式的对象会把对象的所有可变状态都封装起来，并由对象自己的内置锁来保护。
 *     这种模式的优势在于它的简单性。对于任何一种锁对象，只要自始至终都使用该锁对象，都可以用来保护对象的状态。
 * <p>
 *     私有的锁对象可以将锁封装起来，使客户代码无法获得锁，但客户代码可以通过
 *     公有的方法来访问锁，以便（正确或不正确地）参与到它的同步策略中。
 *
 * <p>
 * Created by liuchenwei on 2016/4/22.
 */
public class PrivateLock {

    private final Set<String> set = new HashSet<>();

    // 私有锁对象
    private final Object lock = new Object();

    public void add(String s) {
        synchronized (lock) {
            set.add(s);
        }
    }

    public boolean contains(String s) {
        synchronized (lock) {
            return set.contains(s);
        }
    }
}
