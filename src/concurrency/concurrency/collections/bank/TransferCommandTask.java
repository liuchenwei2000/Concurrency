/**
 * 
 */
package concurrency.collections.bank;

import java.util.Random;
import java.util.concurrent.BlockingQueue;

/**
 * ת��ָ������
 * <p>
 * ��ʵ������һ�������߽�ɫ��
 * 
 * @author ����ΰ
 * 
 * �������ڣ�2014��8��3��
 */
public class TransferCommandTask implements Runnable {
	
	private static final Random RANDOM = new Random();

	private int accID;
	private int accNumber;
	private BlockingQueue<TransferCommand> commands;
	
	public TransferCommandTask(int accID, int accNumber, BlockingQueue<TransferCommand> commands) {
		this.accID = accID;
		this.accNumber = accNumber;
		this.commands = commands;
	}

	@Override
	public void run() {
		while (true) {
			try {
				int to = RANDOM.nextInt(accNumber);
				int amount = RANDOM.nextInt(100);
				// ��ת��ָ����� ��������
				commands.put(new TransferCommand(accID, to, amount));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
