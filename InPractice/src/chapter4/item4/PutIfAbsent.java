package chapter4.item4;

import net.jcip.annotations.ThreadSafe;

import java.util.*;

/**
 * 使用现有的线程安全类添加新功能
 * <p>
 *     假设需要一个线程安全的链表，它需要提供一个原子的“若没有则添加”（put-if-absent）操作。
 * <p>
 * Created by liuchenwei on 2016/4/22.
 */
public class PutIfAbsent {

    /**
     * 1，客户端加锁机制实现
     * <p>
     *     客户端加锁是指，对于使用某个对象 X 的客户端代码，使用 X 本身用于保护其状态的锁来保护客户代码。
     *     要使用客户端加锁，必须知道对象 X 使用的是哪一个锁。这种加锁是脆弱的，将依赖于对象 X 的加锁策略。
     */
    @ThreadSafe
    public class ListHelper<E> {

        private List<E> list = Collections.synchronizedList(new ArrayList<E>());

        public void putIfAbsent(E e) {
            synchronized (list) {
                if (!list.contains(e)) {
                    list.add(e);
                }
            }
        }
    }

    /**
     *,2，组合
     * <p>
     *     本类通过自身的内置锁增加了一层额外的加锁，并不关心底层的 List 是否是线程安全的。
     *     即使 List 不是线程安全的或者修改了它的加锁实现，本类也会提供一致的加锁机制来实现线程安全性。
     *     虽然额外的同步层可能导致轻微的性能损失。只要使用 Java 监视器模式来封装现有的 List，
     *     并且只要在类中拥有指向底层 List 的唯一外部引用，就能确保线程安全性。
     */
    @ThreadSafe
    public class SyncList<E> implements List<E> {

        private final List<E> list;

        public SyncList() {
            this.list = new ArrayList<>();
        }

        public synchronized void putIfAbsent(E e) {
            if (!list.contains(e)) {
                list.add(e);
            }
        }

        // 其他方法全部使用 synchronized ，并将实现委托给底层 List
        @Override
        public synchronized int size() {
            return list.size();
        }

        @Override
        public synchronized boolean isEmpty() {
            return list.isEmpty();
        }

        @Override
        public synchronized boolean contains(Object o) {
            return list.contains(o);
        }

        // 下略

        @Override
        public Iterator<E> iterator() {
            return null;
        }

        @Override
        public Object[] toArray() {
            return new Object[0];
        }

        @Override
        public <T> T[] toArray(T[] a) {
            return null;
        }

        @Override
        public boolean add(E e) {
            return false;
        }

        @Override
        public boolean remove(Object o) {
            return false;
        }

        @Override
        public boolean containsAll(Collection<?> c) {
            return false;
        }

        @Override
        public boolean addAll(Collection<? extends E> c) {
            return false;
        }

        @Override
        public boolean addAll(int index, Collection<? extends E> c) {
            return false;
        }

        @Override
        public boolean removeAll(Collection<?> c) {
            return false;
        }

        @Override
        public boolean retainAll(Collection<?> c) {
            return false;
        }

        @Override
        public void clear() {

        }

        @Override
        public E get(int index) {
            return null;
        }

        @Override
        public E set(int index, E element) {
            return null;
        }

        @Override
        public void add(int index, E element) {

        }

        @Override
        public E remove(int index) {
            return null;
        }

        @Override
        public int indexOf(Object o) {
            return 0;
        }

        @Override
        public int lastIndexOf(Object o) {
            return 0;
        }

        @Override
        public ListIterator<E> listIterator() {
            return null;
        }

        @Override
        public ListIterator<E> listIterator(int index) {
            return null;
        }

        @Override
        public List<E> subList(int fromIndex, int toIndex) {
            return null;
        }
    }
}
