package chapter7.item4;

import java.util.concurrent.TimeUnit;

/**
 * JVM 关闭钩子
 * <p>
 *     在正常关闭中，JVM 首先调用所有已注册的关闭钩子（Shutdown Hook）。
 *     关闭钩子是指通过 Runtime.getRuntime().addShutdownHook 注册的但尚未开始的线程。
 *     JVM 并不能保证关闭钩子的调用顺序。
 * <p>
 *     关闭钩子应该是线程安全的：它们在访问共享数据时必须要使用同步机制，并且小心地避免发生死锁。
 *     而且，关闭钩子不应该对应用程序的状态（如其他服务是否已关闭）或者 JVM 的关闭原因做出任何假设。
 *     最后，关闭钩子必须尽快退出，因为它们会延迟 JVM 的结束时间。
 * <p>
 * Created by liuchenwei on 2016/4/30
 */
public class ShutdownHookTest {

    public static void main(String[] args) {
        /*
         * 关闭钩子可以用于实现服务或应用程序的清理工作，例如删除临时文件。
         */
        Runtime.getRuntime().addShutdownHook(new Thread() {

            @Override
            public void run() {
                System.out.println("Shutting down......");
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("exit");
            }
        });

        System.out.println("Main thread starts to exit.");
    }
}
