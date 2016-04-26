package chapter5.item6;

import java.util.concurrent.TimeUnit;

/**
 * 表示一个耗时很长的计算
 * <p>
 *     本例将创建一个 Computable 包装器，帮助记忆之前的计算结果，并将缓存过程封装起来。
 * <p>
 * Created by liuchenwei on 2016/4/26.
 */
public class LongTimeFunction implements Computable {

    @Override
    public Integer compute(Integer arg) {
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return arg * arg * arg;
    }
}
