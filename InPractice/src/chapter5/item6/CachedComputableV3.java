package chapter5.item6;

import java.util.concurrent.*;

/**
 * 第三个版本的缓存实现
 * <p>
 *     使用 FutureTask 的缓存实现。
 * <p>
 * Created by liuchenwei on 2016/4/26.
 */
public class CachedComputableV3 implements Computable {

    private final ConcurrentHashMap<Integer, Future<Integer>> cache = new ConcurrentHashMap<>();

    private final Computable realComputable;

    public CachedComputableV3(Computable realComputable) {
        this.realComputable = realComputable;
    }

    /**
     * 首先检查某个相应的计算是否已经开始（V2 版本正好相反，先判断某个计算是否已经完成）。
     * 如果还没有开始，就创建一个 FutureTask 并注册到 Map 中，然后启动计算。
     * 如果已经开始计算，则等待现有计算的结果。
     */
    @Override
    public Integer compute(final Integer arg) {
        Future<Integer> result = cache.get(arg);
        if (result == null) {// 尚未开始计算
            FutureTask<Integer> task = new FutureTask(new Callable() {

                @Override
                public Integer call() throws Exception {
                    return realComputable.compute(arg);
                }
            });

            // 复合操作“若没有则添加”是在底层的 Map 对象上执行的，而这个对象无法通过加锁来确保原子性。
            // 所以采用了 ConcurrentHashMap 中的原子方法 putIfAbsent，避免重复计算的漏洞。
            // 返回以前与指定键相关联的值，如果该键没有映射关系，则返回 null
            result = cache.putIfAbsent(arg, task);

            if (result == null) {// 表示从未计算过
                result = task;
                task.run();// 开始执行计算
            }
        }
        try {
            return result.get();// 若结果已经计算出来将立即返回
        } catch (CancellationException e) {
            // 如果计算过程中任务被取消或者失败，那么将把 Future 从缓存中移除。
            // 如果检测到其他 RuntimeException 也将会移除 Future，这样将来的计算才能成功。
            cache.remove(arg, result);
            return null;
        } catch (Exception e) {
            throw new RuntimeException("Not checked" + e.getCause());
        }
    }
}
