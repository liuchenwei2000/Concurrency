/**
 * 
 */
package thread.synchronizer.demo;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CyclicBarrier;

/**
 * 现实使用CyclicBarrier的场景
 * 
 * @author 刘晨伟
 * 
 * 创建日期：2013-6-25
 */
public class CyclicBarrierDemo {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new Statistic().totalGDP();
	}
	
	/**
	 * 模拟国家统计局，用来统计各省市(本例只用三个直辖市进行模拟)的GDP总和。
	 * <p>
	 * 因为各省市的GDP都存在自己的数据库中，所以各省市独立统计其GDP，
	 * 然后将数据提交给国家统计局，统计局在所有数据汇集之后对GDP进行合计。
	 */
	static class Statistic {

		// 共享内存，用于存放各省的GDP数据
		private Map<String, Double> province_gdp_map = new ConcurrentHashMap<String, Double>();
		// 栅栏
		private CyclicBarrier barrier;

		public Statistic() {
			// 当各省市GDP计算任务结束时，执行合计GDP任务
			this.barrier = new CyclicBarrier(3, new TotalTask());
		}

		/**
		 * 合计各省市GDP
		 */
		public void totalGDP() {
			// 分别计算各省市GDP
			new Thread(new ProvinceStatTask(new BeiJingGDPService())).start();
			new Thread(new ProvinceStatTask(new ShangHaiGDPService())).start();
			new Thread(new ProvinceStatTask(new TianJinGDPService())).start();
		}

		/**
		 * GDP合计任务
		 */
		class TotalTask implements Runnable {

			@Override
			public void run() {
				System.out.println("各省GDP收集完毕，开始计算......");
				double total = 0;
				for (Double value : province_gdp_map.values()) {
					total += value;
				}
				System.out.println("Total GDP=" + total);
			}
		}

		/**
		 * 各省市GDP统计任务
		 */
		class ProvinceStatTask implements Runnable {

			private ProvinceGDPService service;

			public ProvinceStatTask(ProvinceGDPService service) {
				this.service = service;
			}

			@Override
			public void run() {
				double gdp = service.getGDP();
				System.out.println(service.getName() + " GDP 统计完毕......");
				province_gdp_map.put(service.getName(), gdp);
				try {
					barrier.await();
				} catch (Exception e) {
					e.printStackTrace();
				}
				System.out.println(service.getName() + " 任务结束.");
			}
		}
	}
}

interface ProvinceGDPService {

	public String getName();
	
	public double getGDP();
}

class BeiJingGDPService implements ProvinceGDPService {

	@Override
	public String getName() {
		return "北京市";
	}
	
	@Override
	public double getGDP() {
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return 1548463;
	}
}

class ShangHaiGDPService implements ProvinceGDPService {

	@Override
	public String getName() {
		return "上海市";
	}
	
	@Override
	public double getGDP() {
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return 819425;
	}
}

class TianJinGDPService implements ProvinceGDPService {

	@Override
	public String getName() {
		return "天津市";
	}
	
	@Override
	public double getGDP() {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return 621556;
	}
}
