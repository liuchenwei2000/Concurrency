package chapter5.item6;

import java.util.HashMap;
import java.util.Map;

/**
 * 第一个版本的缓存实现
 * <p>
 *     使用 HashMap 和同步机制来初始化缓存。
 * <p>
 * Created by liuchenwei on 2016/4/26.
 */
public class CachedComputableV1 implements Computable{

    private final Map<Integer,Integer> cache = new HashMap<>();

    private final Computable realComputable;

    public CachedComputableV1(Computable realComputable) {
        this.realComputable = realComputable;
    }

    /**
     * 对整个 compute 方法进行同步，能确保线程安全性，但会带来明显的可伸缩问题：
     * 每次只有一个线程能够执行 compute，如果另一个线程正在计算结果，
     * 那么其他调用 compute 的线程可能被阻塞很长时间。如果有多个线程在排队等待
     * 还未计算出的结果，那么 compute 方法的计算时间可能比没有缓存操作的时间更长。
     */
    @Override
    public synchronized Integer compute(Integer arg) {
        Integer result = cache.get(arg);
        if (result == null) {
            result = realComputable.compute(arg);
            cache.put(arg, result);
        }
        return result;
    }
}
