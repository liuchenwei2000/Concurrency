/**
 * 
 */
package thread.timer;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

/**
 * Swing定时器Timer类演示
 * <p>
 * 它实际上是观察这模式的应用。
 * 
 * @author 刘晨伟
 * 
 * 创建日期：2010-6-10
 */
public class SwingTimerDemo {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
//		Displayer.createAndShowGUI("Swing Timer Demo", new ClockPanel());
	}
}

class ClockPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	private JLabel label;

	// 定时器Timer是具体被观察者角色
	private Timer timer;

	private SimpleDateFormat format = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");

	public ClockPanel() {
		super();
		add(getLabel());
		// 当调用Timer对象的start()方法时，定时就开始了
		getTimer().start();
	}

	private JLabel getLabel() {
		if (label == null) {
			label = new JLabel(format.format(new Date()));
			label.setFont(new Font("宋体", Font.BOLD, 22));
			label.setForeground(Color.RED);
		}
		return label;
	}

	private Timer getTimer() {
		if (timer == null) {
			// 1秒钟做一次时间重设
			timer = new Timer(1000, null);
			// 添加监听器
			timer.addActionListener(new TimeResetAction());
		}
		return timer;
	}

	/**
	 * 时间重设监听器
	 * <p>
	 * ActionListener是抽象观察者角色，TimeResetAction是具体观察者角色。
	 */
	private class TimeResetAction implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			String time = format.format(new Date());
			getLabel().setText(time);
		}
	}
}