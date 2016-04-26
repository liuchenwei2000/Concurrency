package chapter5.item6;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 第二个版本的缓存实现
 * <p>
 *     使用 ConcurrentHashMap 替换 HashMap。
 * <p>
 * Created by liuchenwei on 2016/4/26.
 */
public class CachedComputableV2 implements Computable{

    private final Map<Integer,Integer> cache = new ConcurrentHashMap<>();

    private final Computable realComputable;

    public CachedComputableV2(Computable realComputable) {
        this.realComputable = realComputable;
    }

    /**
     * 这个版本比 V1 版本有更好的并发行为，但当两个线程同时调用 compute 时可能会导致重复计算。
     * 比如某个线程启动了一个开销很大的计算，而其他线程并不知道这个计算正在进行，那么很可能会重复这个计算。
     * 因为缓存的作用是避免相同的数据被计算多次，这种实现还是比较糟糕。
     */
    @Override
    public Integer compute(Integer arg) {
        Integer result = cache.get(arg);
        if (result == null) {
            result = realComputable.compute(arg);
            cache.put(arg, result);
        }
        return result;
    }
}
