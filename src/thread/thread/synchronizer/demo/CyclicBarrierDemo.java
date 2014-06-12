/**
 * 
 */
package thread.synchronizer.demo;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CyclicBarrier;

/**
 * ��ʵʹ��CyclicBarrier�ĳ���
 * 
 * @author ����ΰ
 * 
 * �������ڣ�2013-6-25
 */
public class CyclicBarrierDemo {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new Statistic().totalGDP();
	}
	
	/**
	 * ģ�����ͳ�ƾ֣�����ͳ�Ƹ�ʡ��(����ֻ������ֱϽ�н���ģ��)��GDP�ܺ͡�
	 * <p>
	 * ��Ϊ��ʡ�е�GDP�������Լ������ݿ��У����Ը�ʡ�ж���ͳ����GDP��
	 * Ȼ�������ύ������ͳ�ƾ֣�ͳ�ƾ����������ݻ㼯֮���GDP���кϼơ�
	 */
	static class Statistic {

		// �����ڴ棬���ڴ�Ÿ�ʡ��GDP����
		private Map<String, Double> province_gdp_map = new ConcurrentHashMap<String, Double>();
		// դ��
		private CyclicBarrier barrier;

		public Statistic() {
			// ����ʡ��GDP�����������ʱ��ִ�кϼ�GDP����
			this.barrier = new CyclicBarrier(3, new TotalTask());
		}

		/**
		 * �ϼƸ�ʡ��GDP
		 */
		public void totalGDP() {
			// �ֱ�����ʡ��GDP
			new Thread(new ProvinceStatTask(new BeiJingGDPService())).start();
			new Thread(new ProvinceStatTask(new ShangHaiGDPService())).start();
			new Thread(new ProvinceStatTask(new TianJinGDPService())).start();
		}

		/**
		 * GDP�ϼ�����
		 */
		class TotalTask implements Runnable {

			@Override
			public void run() {
				System.out.println("��ʡGDP�ռ���ϣ���ʼ����......");
				double total = 0;
				for (Double value : province_gdp_map.values()) {
					total += value;
				}
				System.out.println("Total GDP=" + total);
			}
		}

		/**
		 * ��ʡ��GDPͳ������
		 */
		class ProvinceStatTask implements Runnable {

			private ProvinceGDPService service;

			public ProvinceStatTask(ProvinceGDPService service) {
				this.service = service;
			}

			@Override
			public void run() {
				double gdp = service.getGDP();
				System.out.println(service.getName() + " GDP ͳ�����......");
				province_gdp_map.put(service.getName(), gdp);
				try {
					barrier.await();
				} catch (Exception e) {
					e.printStackTrace();
				}
				System.out.println(service.getName() + " �������.");
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
		return "������";
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
		return "�Ϻ���";
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
		return "�����";
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
