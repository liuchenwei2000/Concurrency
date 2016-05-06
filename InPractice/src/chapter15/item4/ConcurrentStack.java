package chapter15.item4;

import net.jcip.annotations.ThreadSafe;

import java.util.concurrent.atomic.AtomicReference;

/**
 * 1，非阻塞的栈
 * <p>
 *     本类给出了如何通过原子引用来构建非阻塞栈的示例。栈是由 Node 元素构成的一个链表，
 *     其中栈顶作为根节点，并且在每个元素中都包含了一个值以及指向下一个元素的引用。
 * <p>
 *     像本类这样的非阻塞算法都能确保线程安全性，因为 compareAndSet 像锁定机制一样，
 *     既能提供原子性，又能提供内存可见性。当一个线程需要改变栈的状态时，将调用 compareAndSet，
 *     这个方法与写入 volatile 类型的变量有着相同的内存效果。
 *     当线程检查栈的状态时，将在同一个 AtomicReference 上调用 get 方法，
 *     这个方法与读取 volatile 类型的变量有着相同的内存效果。
 *     因此，一个线程执行的任何修改结构都可以安全地发布给其他正在查看状态的线程。
 *     并且，这个栈是通过 compareAndSet 来修改的，因此将采用原子操作来更新 topNode 的引用，
 *     或者在发现存在其他线程干扰的情况下，修改操作将失败。
 * <p>
 * Created by liuchenwei on 2016/5/6
 */
@ThreadSafe
public class ConcurrentStack<E> {

    /**
     * Node 元素
     */
    private static class Node<E> {

        private E item;// 值
        private Node<E> next;// 下一个元素

        public Node(E item) {
            this.item = item;
        }
    }

    // 栈顶 Node
    private final AtomicReference<Node<E>> topNode;

    public ConcurrentStack() {
        this.topNode = new AtomicReference<>();
    }

    /**
     * push 方法创建一个新的 Node，该 Node 的 next 域指向当前的栈顶 Node，
     * 然后使用 CAS 把这个新 Node 压入栈顶。如果在开始插入 Node 时，位于栈顶的 Node 没有发生变化，
     * 那么 CAS 就会成功，如果栈顶 Node 发生了变化（例如由于其他线程在本线程之前插入或移除了元素），
     * 那么 CAS 将会失败，而 push 方法会根据栈的当前状态来更新节点，并且再次尝试。
     * 无论哪种情况，在 CAS 执行完成后，栈仍然会处于一致的状态。
     */
    public void push(E item) {
        Node<E> newHead = new Node(item);
        Node<E> oldHead;
        do {
            oldHead = topNode.get();
            newHead.next = oldHead;
        } while (!topNode.compareAndSet(oldHead, newHead));
    }

    public E take() {
        Node<E> oldHead;
        Node<E> newHead;
        do {
            oldHead = topNode.get();
            if (oldHead == null) {
                return null;
            }
            newHead = oldHead.next;
        } while (!topNode.compareAndSet(oldHead, newHead));
        return oldHead.item;
    }
}
