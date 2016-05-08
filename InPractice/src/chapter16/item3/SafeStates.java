package chapter16.item3;

import java.util.HashMap;
import java.util.Map;

/**
 * 4，初始化过程中的安全性
 * <p>
 *     初始化安全性将确保，对于被正确构造的对象，所有线程都能看到
 *     由构造函数为对象给各个 final 域设置的正确值，而不管采用何种方式来发布对象。
 *     而且，对于可以通过被正确构造对象中某个 final 域到达的任意变量（例如某个 final 数组中的元素，
 *     或者由一个 final 域引用的 HashMap 的元素）将同样对其他线程是可见的。
 * <p>
 *     对于含有 final 域的对象，初始化安全性可以防止对对象的初始引用被重排序到构造过程之前。
 *     当构造函数完成时，构造函数对 final 域的所有写入操作，以及对通过这些域可以到达的任何变量的写入操作，
 *     都将被“冻结”，并且任何获得该对象引用的线程都至少能确保看到被冻结的值。
 *     对于通过 final 域可到达的初始变量的写入操作，将不会与构造过程后的操作一起被重排序。
 * <p>
 *     初始化安全性意味着，SafeStates 可以安全地发布，即便通过不安全的延迟初始化，
 *     或者在没有同步的情况下将 SafeStates 的引用放到一个公有的静态域，
 *     或者没有使用同步以及依赖于非线程安全的 HashSet。
 * <p>
 *     初始化安全性只能保证通过 final 域可达的值从构造过程完成时开始的可见性。
 *     对于通过非 final 域可达的值，或者在构造过程后可能改变的值，必须采用同步来确保可见性。
 * <p>
 * Created by liuchenwei on 2016/5/8
 */
public class SafeStates {

    private final Map<String, String> states;

    public SafeStates() {
        this.states = new HashMap<>();
        states.put("200", "OK");
        states.put("500", "ERROR");
        states.put("404", "NOT FOUND");
    }

    public String get(String key) {
        return states.get(key);
    }
}