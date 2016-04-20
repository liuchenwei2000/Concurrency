package chapter3.item3;

import java.util.ArrayList;
import java.util.List;

/**
 * 栈封闭
 * <p>
 *     栈封闭是线程封闭的一个特例，在栈封闭中，只能通过局部变量才能访问对象。
 *     局部变量的固有属性之一就是封闭在执行线程中，它们位于执行线程的栈中，其他线程无法访问这个栈。
 * <p>
 * Created by liuchenwei on 2016/4/20
 */
public class StackConfinement {

    public int testLocalVariable(){
        // 基本类型的局部变量，无论如何都不会破坏栈封闭性
        // 由于任何方法都无法获得对基本类型的引用，所以 Java 语义确保了基本类型的局部变量始终封闭在线程中。
        int counter = 0;

        // 在维持对象引用的栈封闭性时，程序员需要确保被引用的对象不会逸出（被本方法外的作用域获得该对象引用）。
        // 即便在这里使用非线程安全的对象，作为局部变量它也是线程安全的。
        List<String> numbers;
        counter++;

        numbers = new ArrayList<>();
        numbers.add("Hello");
        numbers.add("World");

        return counter;
    }
}
