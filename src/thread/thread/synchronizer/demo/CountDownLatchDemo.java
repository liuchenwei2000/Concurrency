/**
 * 
 */
package thread.synchronizer.demo;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;

/**
 * ��ʵʹ��CountDownLatch�ĳ���
 * <p>
 * ����ʹ��CountDownLatchʵ���˹���ͳ�ƾ�ͳ��GDP�Ĺ��ܡ�
 * 
 * @author ����ΰ
 * 
 * �������ڣ�2013-6-25
 */
public class CountDownLatchDemo {

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
		// ����ʼ�ź�
		private CountDownLatch startSignal = new CountDownLatch(1);
		// ��������ź�
		private CountDownLatch doneSignal = new CountDownLatch(3);

		/**
		 * �ϼƸ�ʡ��GDP
		 */
		public void totalGDP() {
			try {
				// �ֱ�����ʡ��GDP
				new Thread(new ProvinceStatTask(new BeiJingGDPService())).start();
				new Thread(new ProvinceStatTask(new ShangHaiGDPService())).start();
				new Thread(new ProvinceStatTask(new TianJinGDPService())).start();
				
				System.out.println("��ʡ��ʼͳ��GDP......");
				Thread.sleep(1000);
				startSignal.countDown();// ֪ͨ��ʡ�п�ʼִ��ͳ������
				
				doneSignal.await();// �ȴ���ʡ������ȫ�����
				
				System.out.println("��ʡGDP�ռ���ϣ���ʼ����......");
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
		 * ��ʡ��GDPͳ������
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
				}// �ȴ�ͳ������ʼ
				double gdp = service.getGDP();
				System.out.println(service.getName() + " GDP ͳ�����......");
				province_gdp_map.put(service.getName(), gdp);
				doneSignal.countDown();// ֪ͨ�Լ���ͳ���������
				System.out.println(service.getName() + " �������.");
			}
		}
	}
}