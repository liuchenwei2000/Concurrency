/**
 * 
 */
package concurrency.collections.bank;

import java.util.Random;
import java.util.concurrent.BlockingQueue;

/**
 * 转账指令任务
 * <p>
 * 这实际上是一个生产者角色。
 * 
 * @author 刘晨伟
 * 
 * 创建日期：2014年8月3日
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
				// 将转账指令放入 阻塞队列
				commands.put(new TransferCommand(accID, to, amount));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
