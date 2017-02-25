package wuziqi;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.*;

public class Draw extends JFrame implements ActionListener {
	JFrame frame;
	JButton star, regret, tips, stop, exit;
	JPanel ctr, top, left, right;
	public static Checker checker;
	public static int regretFlag = 0;
	public static int begin = 0;
	public static int now = 1;
	public static int stopFlag = 0;
	public static int result=-1;
	public Draw(int first) {
		now = first;
		frame = new JFrame("五子棋");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Container contentPane = frame.getContentPane();
		contentPane.setBackground(Color.black);
		checker = new Checker();
		top = new JPanel();
		top.setPreferredSize(new Dimension(600, 30));
		top.setBackground(new Color(228, 190, 160));
		left = new JPanel();
		left.setPreferredSize(new Dimension(40, 10));
		left.setBackground(new Color(228, 190, 160));
		right = new JPanel();
		right.setPreferredSize(new Dimension(40, 500));
		right.setBackground(new Color(228, 190, 160));
		ctr = new JPanel();
		ctr.setBackground(new Color(228, 190, 160));
		ctr.setPreferredSize(new Dimension(600, 60));

		ImageIcon icon1 = new ImageIcon(getClass().getResource("bt52.png"));
		icon1 = new ImageIcon(icon1.getImage().getScaledInstance(66, 75,
				Image.SCALE_SMOOTH));
		star = new JButton(icon1);
		// star=new JButton("开始");
		star.setContentAreaFilled(false);
		star.setFocusPainted(false);
		star.setBorderPainted(false);
		star.addActionListener(this);
		ctr.add(star);
		// regret=new JButton("悔棋");
		ImageIcon icon2 = new ImageIcon(getClass().getResource("bt32.png"));
		icon2 = new ImageIcon(icon2.getImage().getScaledInstance(45, 75,
				Image.SCALE_SMOOTH));
		regret = new JButton(icon2);
		regret.setBorderPainted(false);
		regret.setContentAreaFilled(false);
		regret.setFocusPainted(false);
		regret.addActionListener(this);
		regret.setEnabled(false);
		ctr.add(regret);
		// tips=new JButton("提示");
		ImageIcon icon3 = new ImageIcon(getClass().getResource("bt62.png"));
		icon3 = new ImageIcon(icon3.getImage().getScaledInstance(45, 75,
				Image.SCALE_SMOOTH));
		tips = new JButton(icon3);
		tips.setBorderPainted(false);
		tips.setContentAreaFilled(false);
		tips.setFocusPainted(false);
		tips.addActionListener(this);
		tips.setEnabled(false);
		ctr.add(tips);
		// stop=new JButton("暂停");
		ImageIcon icon4 = new ImageIcon(getClass().getResource("bt72.png"));
		icon4 = new ImageIcon(icon4.getImage().getScaledInstance(45, 70,
				Image.SCALE_SMOOTH));
		stop = new JButton(icon4);
		stop.setBorderPainted(false);
		stop.setContentAreaFilled(false);
		stop.setFocusPainted(false);
		stop.addActionListener(this);
		stop.setEnabled(false);
		ctr.add(stop);
		// exit=new JButton("退出");
		ImageIcon icon5 = new ImageIcon(getClass().getResource("bt42.png"));
		icon5 = new ImageIcon(icon5.getImage().getScaledInstance(50, 73,
				Image.SCALE_SMOOTH));
		exit = new JButton(icon5);
		exit.setBorderPainted(false);
		exit.setContentAreaFilled(false);
		exit.setFocusPainted(false);
		exit.addActionListener(this);
		ctr.add(exit);
		contentPane.add(checker, BorderLayout.CENTER);
		contentPane.add(ctr, BorderLayout.SOUTH);
		contentPane.add(left, BorderLayout.WEST);
		// contentPane.add(right,BorderLayout.EAST);
		contentPane.add(top, BorderLayout.NORTH);

		frame.setSize(Checker.checkerSize * Checker.gap + 100,
				(Checker.checkerSize * Checker.gap + 100 + Checker.gap));
		frame.setVisible(true);
		// frame.setResizable(false);
	}

	public void actionPerformed(ActionEvent e) {
		JButton click = (JButton) e.getSource();
		if (click == star) {
			star();
		}
		if (click == regret) {
			regret();
		}
		if (click == stop) {
			stops();
		}
		if (click == tips) {
			tips();
		}
		if (click == exit) {
			exit();
		}
	}

	public static void exit() {
		System.exit(1);
	}

	private void tips() {
		if (Morpion.now == 1) {
			Object[] options ={ "4层", "6层" };  
			int option = JOptionPane.showOptionDialog(null, "请选择思考强度（6层较慢）", "思考强度",JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);  
			switch (option) {
			case JOptionPane.YES_NO_OPTION: {
				Morpion.ai.tips(4);
				break;
			}
			case JOptionPane.NO_OPTION:
				Morpion.ai.tips(6);
			}
		}
	}

	private void stops() {
		if (stopFlag == 0)
			stopFlag = 1;
		else
			stopFlag = 0;
		checker.repaint();
	}

	private void regret() {
		if (Morpion.now == 1) {
			Morpion.board.regret();
			regretFlag = 1;
			checker.repaint();
		}
	}

	private void star() {
		Object[] obj2 ={ "简单", "普通", "困难" };  
		String s = (String) JOptionPane.showInputDialog(null,"请选择难度:\n", "难度", JOptionPane.PLAIN_MESSAGE, null, obj2, "普通");
		switch(s)
		{
		case "简单":Morpion.IQ=2;break;
		case "普通":Morpion.IQ=4;break;
		case "困难":Morpion.IQ=6;break;
		}
		Object[] options ={ "黑色", "白色" };  
		int option = JOptionPane.showOptionDialog(null, "请选择棋子颜色（黑色为先手）", "颜色",JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);  
		switch (option) {
		case JOptionPane.YES_NO_OPTION: {
			Morpion.AI = 2;
			Morpion.HUMAN = 1;
			break;
		}
		case JOptionPane.NO_OPTION:
			Morpion.AI = 1;
			Morpion.HUMAN = 2;

		}
		begin = 1;
		star.setEnabled(false);
		regret.setEnabled(true);
		tips.setEnabled(true);
		stop.setEnabled(true);
		System.out.println("游戏开始");
	}
	public void finish() {
		stopFlag = 2;
		regret.setEnabled(false);
		tips.setEnabled(false);
		stop.setEnabled(false);
		checker.repaint();
		
	}
}

@SuppressWarnings("serial")
class Checker extends JPanel {
	public static int checkerSize = Morpion.size;
	public static int gap = Morpion.gap;

	public Checker() {
		setPreferredSize(new Dimension(gap * checkerSize + gap, gap
				* checkerSize + gap));
		setBackground(new Color(228, 190, 160));
		setLayout(null);
		this.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (Morpion.now == 1 && Draw.stopFlag == 0) {
					int locX = e.getX();
					int locY = e.getY();
					int locI = (((locX - 0.0 - (gap / 2)) / gap - (locX) / gap > 0.5 ? 1
							: 0) + (locX) / gap);
					int locJ = (((locY - 0.0 - (gap / 2)) / gap - (locY) / gap > 0.5 ? 1
							: 0) + (locY) / gap);
					Coord newCoord = new Coord(locJ, locI, Morpion.HUMAN);
					if (Morpion.board.isPassed(newCoord)) {
						Morpion.board.setCoord(newCoord);
						Morpion.board.saveHumanLast(newCoord);
						Draw.checker.repaint();
						Morpion.now = 0;
						Draw.now = Morpion.AI;
					}
				}
			}
		});
	}

	public void paint(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;

		for (int i = 0; i < checkerSize; i++) {
			g2d.drawLine(0 + (gap / 2), i * gap + (gap / 2), gap
					* (checkerSize - 1) + (gap / 2), i * gap + (gap / 2));
			g2d.drawLine(i * gap + (gap / 2), 0 + (gap / 2), i * gap
					+ (gap / 2), gap * (checkerSize - 1) + (gap / 2));
		}
		if (checkerSize == 15) {
			g2d.fillOval(3 * gap - 4 + (gap / 2), 3 * gap - 4 + (gap / 2), 8, 8);
			g2d.fillOval(3 * gap - 4 + (gap / 2), 11 * gap - 4 + (gap / 2), 8,
					8);
			g2d.fillOval(11 * gap - 4 + (gap / 2), 3 * gap - 4 + (gap / 2), 8,
					8);
			g2d.fillOval(11 * gap - 4 + (gap / 2), 11 * gap - 4 + (gap / 2), 8,
					8);
			g2d.fillOval(7 * gap - 4 + (gap / 2), 7 * gap - 4 + (gap / 2), 8, 8);
		}
		for (int i = 0; i < checkerSize; i++) {
			for (int j = 0; j < checkerSize; j++) {
				if (Morpion.board.map[i][j] == 1) {
					try {
						g.drawImage(ImageIO.read(getClass().getResource("chessb.png")), j * gap - (gap - 6) / 2 + (gap / 2), i * gap
								- (gap - 6) / 2 + (gap / 2), gap - 6, gap - 6, null);
					} catch (IOException e) {
						e.printStackTrace();
					}
				} else if (Morpion.board.map[i][j] == 2) {	
					try {
						g.drawImage(ImageIO.read(getClass().getResource("chessw.png")), j * gap - (gap - 6) / 2 + (gap / 2), i * gap
								- (gap - 6) / 2 + (gap / 2), gap - 6, gap - 6, null);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
		if (Draw.begin == 1 && Draw.regretFlag == 0) {
			g.setColor(new Color(0xFF0000));
			if (Draw.now == Morpion.AI) {
				try {
					g.drawImage(ImageIO.read(getClass().getResource("pos1.png")), Chessboard.humanLastStep[1] * gap - (gap / 4) / 2
							+ (gap / 2), Chessboard.humanLastStep[0] * gap
							- (gap / 4) / 2 + (gap / 2), gap / 4, gap / 4, null);
				} catch (IOException e) {
					e.printStackTrace();
				}
				Draw.now = Morpion.HUMAN;
			} else {
				try {
					g.drawImage(ImageIO.read(getClass().getResource("pos1.png")), Chessboard.aiLastStep[1] * gap - (gap / 4) / 2
							+ (gap / 2), Chessboard.aiLastStep[0] * gap - (gap / 4)
							/ 2 + (gap / 2), gap / 4, gap / 4, null);
				} catch (IOException e) {
					e.printStackTrace();
				}
				Draw.now = Morpion.AI;
			}
		}
		if(Draw.stopFlag==1 && Draw.result!=1 && Draw.result!=2)
		{
			try {
				g.drawImage(ImageIO.read(getClass().getResource("b333.png")), (gap * checkerSize + gap)/2-130, (gap * checkerSize + gap)/2-280, 220, 500, null);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if(Draw.result==1)
		{
			try {
				g.drawImage(ImageIO.read(getClass().getResource("black1.png")), (gap * checkerSize + gap)/2-175, (gap * checkerSize + gap)/2-105, 350, 210, null);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if(Draw.result==2)
		{
			try {
				g.drawImage(ImageIO.read(getClass().getResource("white1.png")),(gap * checkerSize + gap)/2-175, (gap * checkerSize + gap)/2-105, 350, 210, null);
			} catch (IOException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}
		}
		g2d.dispose();
	}
}