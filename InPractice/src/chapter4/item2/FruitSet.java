package chapter4.item2;

import java.util.HashSet;
import java.util.Set;

/**
 * 通过封闭和加锁等机制使一个类成为线程安全的，即使它的状态变量并不是线程安全的。
 * <p>
 *     实例封闭是构建线程安全类的一个最简单形式，它还使得在锁策略的选择上拥有了更多地灵活性。
 *     不仅可以使用它的内置锁，还可以使用其他形式的锁，只要自始至终都使用同一个锁就可以保护状态。
 *     实例封闭还使得不同的状态变量可以由不同的锁来保护。
 * <p>
 * Created by liuchenwei on 2016/4/22.
 */
public class FruitSet {

    // 非线程安全的 HashSet 是私有的且不会逸出，它被封闭在 FruitSet 类中
    private final Set<String> fruits = new HashSet<>();

    // 唯一能访问 HashSet 的是下面两个方法，执行它们都需要获取 FruitSet 对象的锁。
    // FruitSet 的状态完全由它的内置锁保护，因而它是线程安全的类。

    public synchronized void addFruit(String fruit) {
        fruits.add(fruit);
    }

    public synchronized boolean containsFruit(String fruit) {
        return fruits.contains(fruit);
    }
}
