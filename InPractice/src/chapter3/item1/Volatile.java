package chapter3.item1;

/**
 * volitile 变量
 * <p>
 *     Java 提供了一种稍弱的同步机制，即 volatile 变量，用来确保将变量的更新操作通知到其他线程。
 *     当把变量声明为 volatile 类型后，编译器与运行时都会注意到这个变量是共享的，
 *     因此不会将该变量上的操作与其他内存操作一起重排序，变量也不会被缓存在寄存器或者对其他处理器不可见的地方，
 *     因而在读取 volatile 类型的变量时总会返回最新写入的值。
 * <p>
 *     在访问 volatile 变量时不会执行加锁操作，因此也就不会使执行线程阻塞，
 *     因此 volatile 变量是一种比 synchronized 关键字更轻量级的同步机制。
 * <p>
 *     volatile 变量通常用做某个操作完成、发生中断或者状态的标志，例如本例的 asllep 标志。
 *     加锁机制既可以确保可见性又可以确保原子性，而 volatile 变量只能确保可见性。
 * <p>
 *     当且仅当满足以下所有条件时，才应该使用 volatile 变量：
 *     <li>对变量的写入操作不依赖变量的当前值，或者能确保只有单线程更新变量的值。
 *     <li>该变量不会与其他状态变量一起纳入不变性条件中。
 *     <li>在访问变量时不需要加锁。
 * <p>
 * Created by liuchenwei on 2016/4/20.
 */
public class Volatile {

    private volatile boolean asleep;

    public void starts(){
        while(!asleep){
            System.out.println("sleeping...");
        }
        System.out.println("sleeped...");
    }
}
