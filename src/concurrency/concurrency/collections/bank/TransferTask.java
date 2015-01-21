/**
 * 
 */
package concurrency.collections.bank;

import java.util.concurrent.BlockingQueue;

import concurrency.bank.Account;
import concurrency.bank.Bank;
import concurrency.bank.UnsynBank;

/**
 * 真正的转账任务
 * <p>
 * 这实际上是一个消费者角色。
 * 
 * @author 刘晨伟
 * 
 * 创建日期：2014年8月3日
 */
public class TransferTask implements Runnable {

	private BlockingQueue<TransferCommand> commands;
	
	private Bank bank;
	
	public TransferTask(int accNumber, BlockingQueue<TransferCommand> commands) {
		this.bank = new UnsynBank(accNumber, 100);
		this.commands = commands;
	}
	
	@Override
	public void run() {
		while(true){
			try {
				// 这个地方虽然控制了并发，但取到 TransferCommand 之后对Bank的操作并没有并发保护，
				// 按理说应该会有问题，但事实没有。
				TransferCommand command = commands.take();
				Account[] accounts = bank.getAccounts();
				bank.transfer(accounts[command.getFrom()], accounts[command.getTo()], command.getAmount());
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
