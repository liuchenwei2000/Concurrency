package chapter5.item5;

import common.ConnectionAdaptor;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Semaphore;

/**
 * 3，信号量示例
 * <p>
 *     信号量（Semaphore）用来控制同时访问某个特定资源的操作数量，或者同时执行某个指定操作的数量。
 *     还可以用来实现某种资源池，或者对容器施加边界。
 * <p>
 *     Semaphore 中管理着一组虚拟的许可，许可的初始数量可通过构造函数指定。
 *     在执行操作时可以首先获得许可（只要还有剩余的许可），并在使用以后释放许可。
 *     如果没有许可，那么 acquire 将阻塞直到有许可（或者直到被中断或操作超时）。
 *     release 方法将释放一个许可。
 * <p>
 *     信号量的一种简化形式是二值信号量，即初始值为 1 的 Semaphore。
 *     二值信号量可以用做互斥体（mutex），并具备不可重入的加锁语义：谁拥有这个唯一的许可，谁就拥有了互斥锁。
 * <p>
 * Created by liuchenwei on 2016/4/25
 */
public class SemaphoreTest {

    /**
     * 数据库连接池
     * <p>
     *     构造一个固定长度的资源池，当池为空时，请求资源会失败。
     *     但实际上希望的行为是阻塞而不是失败，并且当池非空时解除阻塞。
     *     可以使用 Semaphore 来实现具有上述行为的资源池。
     */
    public static class ConnectionPool {

        private static final int MAX = 10;// 池默认大小

        private static ConnectionPool instance;// 单例

        private final List<Connection> pool = new ArrayList<>();
        private final Semaphore semaphore;

        private ConnectionPool() {
            this.semaphore = new Semaphore(MAX);
            initPool();
        }

        private void initPool() {
            for (int i = 0; i < MAX; i++) {
                pool.add(createConnection());
            }
        }

        private Connection createConnection() {
            return new ConnectionAdaptor() {

                /**
                 * 关闭 Connection 的时候要释放许可
                 */
                @Override
                public void close() throws SQLException {
                    semaphore.release();
                }
            };
        }

        public static ConnectionPool getInstance() {
            if (instance == null) {
                instance = new ConnectionPool();
            }
            return instance;
        }

        public Connection getConnection() throws InterruptedException {
            // 首先要获取一个许可
            semaphore.acquire();
            // 查找某个空闲中的 Connection 并返回
            return pool.get(5);
        }
    }

    /**
     * 有界 ArrayList
     * <p>
     *     使用 Semaphore 可以将任何一种容器变成有界阻塞容器：
     *     即只能添加指定个数的元素，元素数量到达边界之后，加入操作将会阻塞直到可以加入。
     */
    public static class BoundedArrayList<T> {

        private final List<T> list;
        private final Semaphore semaphore;

        public BoundedArrayList(int bound) {
            this.list = Collections.synchronizedList(new ArrayList<T>());
            this.semaphore = new Semaphore(bound);
        }

        public boolean add(T t) throws InterruptedException {
            // 首先要获取一个许可
            semaphore.acquire();
            boolean wasAdded = false;

            try {
                wasAdded = list.add(t);
                return wasAdded;
            } finally {
                // 没有添加成功则释放许可
                if (!wasAdded) {
                    semaphore.release();
                }
            }
        }

        public boolean remove(T t){
            boolean wasRemoved = list.remove(t);
            if (wasRemoved) {// 删除成功则释放许可
                semaphore.release();
            }
            return wasRemoved;
        }
    }
}
