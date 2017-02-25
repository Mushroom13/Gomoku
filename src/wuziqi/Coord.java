package wuziqi;

import java.util.Scanner;

//������
public class Coord implements Comparable<Coord> {
	int x;
	int y;
	int player;
	int score = 0;
	static Scanner in = new Scanner(System.in);

	public Coord(int x, int y, int player) {
		this.x = x;
		this.y = y;
		this.player = player;
	}

	// �ӿ���̨��ȡ���겢����һ������
	public static Coord input(int player) {
		int a = in.nextInt();
		int b = in.nextInt();
		return new Coord(a, b, player);
	}

	@Override
	public int compareTo(Coord o) {
		if (score > o.score)
			return 1;
		else if (score == o.score)
			return 0;
		else
			return -1;
	}
}
