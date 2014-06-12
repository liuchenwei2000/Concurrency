/**
 * 
 */
package thread.synchronizer.demo;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;

/**
 * 现实使用CountDownLatch的场景
 * <p>
 * 本例使用CountDownLatch实现了国家统计局统计GDP的功能。
 * 
 * @author 刘晨伟
 * 
 * 创建日期：2013-6-25
 */
public class CountDownLatchDemo {

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
		// 任务开始信号
		private CountDownLatch startSignal = new CountDownLatch(1);
		// 任务完成信号
		private CountDownLatch doneSignal = new CountDownLatch(3);

		/**
		 * 合计各省市GDP
		 */
		public void totalGDP() {
			try {
				// 分别计算各省市GDP
				new Thread(new ProvinceStatTask(new BeiJingGDPService())).start();
				new Thread(new ProvinceStatTask(new ShangHaiGDPService())).start();
				new Thread(new ProvinceStatTask(new TianJinGDPService())).start();
				
				System.out.println("各省开始统计GDP......");
				Thread.sleep(1000);
				startSignal.countDown();// 通知各省市开始执行统计任务
				
				doneSignal.await();// 等待各省市任务全部完成
				
				System.out.println("各省GDP收集完毕，开始计算......");
				double total = 0;
				for (Double value : province_gdp_map.values()) {
					total += value;
				}
				System.out.println("Total GDP=" + total);
			} catch (InterruptedException e) {
				e.printStackTrace();
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
				try {
					startSignal.await();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}// 等待统计任务开始
				double gdp = service.getGDP();
				System.out.println(service.getName() + " GDP 统计完毕......");
				province_gdp_map.put(service.getName(), gdp);
				doneSignal.countDown();// 通知自己的统计任务结束
				System.out.println(service.getName() + " 任务结束.");
			}
		}
	}
}