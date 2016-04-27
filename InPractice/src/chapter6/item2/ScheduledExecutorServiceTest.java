package chapter6.item2;

/**
 * 5，延迟任务与周期任务
 * <p>
 *     Timer 类负责管理延迟任务以及周期任务，然而它存在一些缺陷：
 *     <li>1，Timer 在执行所有定时任务时只会创建一个线程
 *     如果某个人物的执行时间过长，那么将破坏其他 TimerTask 的定时精确性。
 *     <li>2，如果 TimerTask 抛出一个 RuntimeException，Timer 线程并不捕获异常，将会终止定时线程。
 * <p>
 *     因此应该考虑使用 ScheduledThreadPoolExecutor 来代替 Timer，JDK5 之后将很少使用 Timer。
 *     线程池能弥补 Timer 的两个缺陷，它可以提供多个线程来执行延时任务和周期任务。
 *     还能正确处理抛出 RuntimeException 的任务。
 * <p>
 * Created by liuchenwei on 2016/4/27.
 */
public class ScheduledExecutorServiceTest {
}
