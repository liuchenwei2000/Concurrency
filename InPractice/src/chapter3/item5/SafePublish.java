package chapter3.item5;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 要安全地发布一个对象，对象的引用以及对象的状态必须同时对其他线程可见。
 * 一个正确构造的对象可以通过 4 种方式来安全地发布，见本例。
 * <p>
 * <p>
 * Created by liuchenwei on 2016/4/21.
 */
public class SafePublish {

    // 1，在静态初始化函数中初始化一个对象引用。
    // 静态初始化由 JVM 在类的初始化阶段执行，由于在 JVM 内部存在着同步机制，
    // 因此通过这种方式初始化的任何对象都可以被安全地发布。
    public static Holder instance = new Holder(42);

    // 2，将对象的引用保存到 volatile 类型的域或者 AtomicReferance 对象中。
    public volatile Holder holder;

    // 3，将对象的引用保存到某个正确构造对象的 final 类型域中。
    public final Holder h;

    public SafePublish() {
        this.h = new Holder(42);
    }

    public static void main(String[] args) {
        // 4，将对象的引用保存到一个由锁保护的域中，比如将对象放入到某个线程安全容器。
        List<Holder> syncList = Collections.synchronizedList(new ArrayList<Holder>());
        syncList.add(new Holder(42));
        // 如果线程 T1 将对象 O 放入一个线程安全的容器，随后线程 T2 读取这个对象，
        // 那么可以确保 T2 看到 T1 设置的 O 对象状态，即便在这段读写 O 对象的代码中没有包含显式的同步。
    }
}
