package chapter3.item4;

import net.jcip.annotations.Immutable;

import java.util.HashSet;
import java.util.Set;

/**
 * 不可变类示例
 * <p>
 *     如果某个对象在被创建后其状态就不能被修改，那么这个对象被称为不可变对象。
 *     线程安全性是不可变对象的固有属性之一，它们的不变性条件是由构造函数创建的，
 *     只要它们的状态不改变，那么这些不变性条件就得以维持。
 * <p>
 *     当满足以下条件时，对象才是不可变的：
 *     <li>对象创建以后其状态就不能修改。
 *     <li>对象所有的域都是 final 类型。
 *     <li>对象是正确创建的（创建期间 this 引用没有逸出）
 * <p>
 * Created by liuchenwei on 2016/4/21.
 */
@Immutable
public class ImmutableClass {

    // 不可变对象内部仍可以使用可变对象来管理状态
    private final Set<String> friuts = new HashSet<>();

    public ImmutableClass() {
        this.friuts.add("apple");
        this.friuts.add("orange");
        this.friuts.add("banana");
    }

    public boolean isFriut(String name) {
        return friuts.contains(name);
    }
}
