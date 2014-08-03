/**
 * 
 */
package concurrency.queue.bank;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * ����ת�˲�����
 * 
 * @author ����ΰ
 * 
 * �������ڣ�2007-12-12
 */
public class TransferTest {
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		int accNumber = 10;// �������˺���
		
		// ʹ�� �������� ����ͬ��
		BlockingQueue<TransferCommand> commands = new LinkedBlockingQueue<TransferCommand>(20);

		for (int i = 0; i < accNumber; i++) {
			new Thread(new TransferCommandTask(i, accNumber, commands)).start();
			new Thread(new TransferTask(accNumber, commands)).start();
		}
	}
}