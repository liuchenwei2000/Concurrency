package chapter10.item1;

/**
 * 2，动态的锁顺序死锁
 * <p>
 *     有时候，并不能清楚地知道是否在锁顺序上有足够的控制权来避免死锁的发生。
 * <p>
 * Created by liuchenwei on 2016/5/1
 */
public class DynamicDeadLock {

    /**
     * 银行接口
     */
    public interface Bank {

        /**
         * 转账
         *
         * @param from   转出账户
         * @param to     转入账户
         * @param amount 转账金额
         */
        void transferMoney(Account from, Account to, double amount)
                throws InsufficientFundsException;
    }

    /**
     * 余额不足异常
     */
    public static class InsufficientFundsException extends Exception {
    }

    /**
     * 会造成动态的锁顺序死锁的 Bank
     * <p>
     *     这里的实现似乎是按照相同的顺序来获得锁，但事实上锁的顺序取决于传递进来的参数顺序。
     *     如果两个线程同时调用本方法，其中一个线程从 X 向 Y 转账，另一个线程从 Y 向 X 转账，如下：
     *     线程1 ： transferMoney(x, y, 10);
     *     线程2 ： transferMoney(y, x, 20);
     *     这时就会产生死锁。由于无法控制参数的顺序，因此要解决此问题，
     *     必须重新定义锁的顺序，并在整个应用程序中都按照这个顺序来获取锁。详见 Bank2
     */
    public static class Bank1 implements Bank {

        public void transferMoney(Account from, Account to, double amount)
                throws InsufficientFundsException {
            synchronized (from) {
                synchronized (to) {
                    if (from.getBalance() < amount) {
                        throw new InsufficientFundsException();
                    }
                    from.debit(amount);
                    to.credit(amount);
                }
            }
        }
    }

    /**
     * 通过锁顺序来避免死锁的 Bank
     * <p>
     *     制定锁顺序时，可以使用 System.identityHashCode 方法，
     *     该方法将返回由 Object.hashCode 返回的值，无论给定对象的类是否重写 hashCode()。
     */
    public static class Bank2 implements Bank {

        // 加时赛锁
        private static final Object tieLock = new Object();

        public void transferMoney(final Account from, final Account to, final double amount)
                throws InsufficientFundsException {

            class Helper {

                public void transfer() throws InsufficientFundsException {
                    if (from.getBalance() < amount) {
                        throw new InsufficientFundsException();
                    }
                    from.debit(amount);
                    to.credit(amount);
                }
            }

            int fromHash = System.identityHashCode(from);
            int toHash = System.identityHashCode(to);

            // 通过比较参数对象的 hashCode 制定加锁顺序
            if (fromHash < toHash) {
                synchronized (from) {
                    synchronized (to) {
                        new Helper().transfer();
                    }
                }
            } else if (fromHash > toHash) {
                synchronized (to) {
                    synchronized (from) {
                        new Helper().transfer();
                    }
                }
            } else {
                /*
                * 在极少数情况下，两个对象可能拥有相同的散列值，此时必须通过某种任意的方法来决定锁的顺序，
                * 而这可能又会引入死锁。为了避免这种情况，可以使用额外的加时赛锁，
                * 在获得两个 Account 锁之前，首先要先获取加时赛锁，从而保证每次只有一个线程以未知的顺序获得这两个锁，从而消除死锁。
                * 由于 System.identityHashCode 中出现散列冲突的频率非常低，因此这项技术以最小代价换来了最大的安全性。
                *
                * 如果在 Account 中包含一个唯一的、不可变的，并且具备可比性的属性，例如账号，
                * 那么要制定锁的顺序就更加容易了：通过该属性对对象进行排序，因而不需要使用加时赛锁。
                */
                synchronized (tieLock) {
                    synchronized (from) {
                        synchronized (to) {
                            new Helper().transfer();
                        }
                    }
                }
            }
        }
    }

    /**
     * 模拟银行账户
     */
    public static class Account {

        private double balance;// 余额

        public double getBalance() {
            return balance;
        }

        /**
         * 取款
         */
        public void debit(double amount) {
            this.balance -= amount;
        }

        /**
         * 存款
         */
        public void credit(double amount) {
            this.balance += amount;
        }
    }
}
