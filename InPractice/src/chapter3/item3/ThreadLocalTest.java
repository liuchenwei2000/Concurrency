package chapter3.item3;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * ThreadLocal 类
 * <p>
 *     维持线程封闭性的一种更规范方法是使用 ThreadLocal，这个类能使线程中的某个值与保存值的对象关联起来。
 *     ThreadLocal 提供了 get 和 set 等访问方法，为每个使用该变量的线程都存有一份独立的副本。
 *     因此 get 总是返回由当前执行线程在调用 set 时设置的最新值。
 * <p>
 *     ThreadLocal 对象通常用于防止对可变的单实例变量（Singleton）或全局变量进行共享。
 *     假如需要将一个单线程程序移植到多线程环境中，通过将共享的全局变量转换为 ThreadLocal 对象，可以维持线程安全性。
 * <p>
 *     ThreadLocal 变量类似于全局变量，它能降低代码的可重用性，并在类之间引入隐含的耦合性，使用时要格外小心。
 * <p>
 * Created by liuchenwei on 2016/4/20
 */
public class ThreadLocalTest {

    // 确保每个线程都有属于自己的 Connection
    private static ThreadLocal<Connection> connectionHolder;

    static {
        connectionHolder = new ThreadLocal<Connection>() {

            @Override
            protected Connection initialValue() {
                try {
                    return DriverManager.getConnection("...");
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                return null;
            }
        };
    }

    /**
     * 当某个线程初次调用 ThreadLocal 方法时，就会调用 initialValue() 来获取初始值。
     * 可以将 ThreadLocal<T> 视为包含了 Map<Thread, T> 对象，其中保存了特定于该线程的值。
     * 但 ThreadLocal 的实现并非如此，这些特定于线程的值保存在 Thread 对象中，当线程终止后，这些值会作为垃圾回收。
     */
    public static Connection getConnection() {
        return connectionHolder.get();
    }
}
