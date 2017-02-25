package wuziqi;

import java.util.ArrayList;
import java.util.Collections;

//��������
public class AI {
	int depth;
	Chessboard board;
	static int count;
	//final static int nodeNum = 25;// ÿ��ϣ��չ���Ľڵ���
	final static int MAXLAYER = 0;
	final static int MINLAYER = 1;

	public AI(int d, Chessboard c) {
		depth = d;
		board = c;
	}
	public void tips(int newDepth) {
		System.out.println("AI�������ף���������ȣ�"+newDepth);
		int temp = depth;
		depth = newDepth;
		if (Morpion.AI == 1) {
			Morpion.AI = 2;
			Morpion.HUMAN = 1;
		} else {
			Morpion.AI = 1;
			Morpion.HUMAN = 2;
		}
		long startMili = System.currentTimeMillis();// ��ǰʱ���Ӧ�ĺ�����
		count = 0;
		Chessboard virtualBoard = board.copy();
		Coord bestCoord = analyze(virtualBoard, 0, Integer.MAX_VALUE);
		long endMili = System.currentTimeMillis();
		System.out.println("AI��ʾ�㣬����µ��ǣ�(" + bestCoord.x + "," + bestCoord.y + ")");
		System.out.println("���ֵ÷�=" + bestCoord.score + " and �ڵ���=" + count + " and ��ʱ=" + (endMili - startMili) + "����");
		depth = temp;
		if (Morpion.AI == 1) {
			Morpion.AI = 2;
			Morpion.HUMAN = 1;
		} else {
			Morpion.AI = 1;
			Morpion.HUMAN = 2;
		}
	}

	public void input() {
		System.out.println("AI˼���У���������ȣ�"+depth);
		long startMili = System.currentTimeMillis();// ��ǰʱ���Ӧ�ĺ�����
		count = 0;
		Chessboard virtualBoard = board.copy();
		Coord bestCoord = analyze(virtualBoard, 0, Integer.MAX_VALUE);

		long endMili = System.currentTimeMillis();
		System.out.println("AI�µ㣺(" + bestCoord.x + "," + bestCoord.y + ")");
		System.out.println("���ֵ÷�=" + bestCoord.score + " and �ڵ���=" + count + " and ��ʱ=" + (endMili - startMili) + "����");
		board.setCoord(bestCoord);
		Morpion.board.saveAILast(bestCoord);

	}

	private int getLayer(int nowDepth) {
		return nowDepth % 2;
	}

	private int getPlayer(int nowDepth) {
		return getLayer(nowDepth) == MINLAYER ? Morpion.HUMAN : Morpion.AI;
	}

	private Coord analyze(Chessboard vb, int nowDepth, int lastScore) {
		count++;
		// System.out.println("����˼����"+nowDepth+"��");
		ArrayList<Coord> coords = vb.getAllCoords(); // ��ȡ��ǰ������������
		int[][] tempMap = vb.getMap();// ��ȡ�������ӵ�״��
		ArrayList<Coord> preCoords = new ArrayList<Coord>();// ������ҿ��µĵ�
		for (Coord c : coords) {
			ArrayList<Coord> nearbyChess = vb.getNearbyChess(c, tempMap);// ���������Ӹ�������δ���ԵĿ�λ
			for (Coord nc : nearbyChess) {
				nc.player = getPlayer(nowDepth);// ����λ�õ�player����Ϊ�������
				// nc.score=vb.getScore(nc);//����÷�
				preCoords.add(nc);
				// System.out.println("("+nc.x+","+nc.y+","+nc.player+"):"+nc.score);
				tempMap[nc.x][nc.y] = -1;// ����λ�ñ��Ϊ�Ѿ����Թ�
			}
		}

		if (nowDepth == depth - 1)// �����������ˣ�ֱ�ӷ��ط������ĵ�
		{
			for (Coord c : preCoords) {
				c.score = vb.getFullScore(c);
				// System.out.println("("+c.x+","+c.y+","+c.player+")fullscore:"+c.score);
			}
			Collections.sort(preCoords);
			Coord rCoord = null;
			if (getLayer(nowDepth) == MINLAYER) {
				rCoord = preCoords.get(0);
			} else {
				rCoord = preCoords.get(preCoords.size() - 1);
			}
			// System.out.println("("+rCoord.x+","+rCoord.y+","+rCoord.player+")fullscore:"+rCoord.score);
			return rCoord;
		} else// ���δ����չ����֦��
		{
			for (Coord c : preCoords) {
				c.score = vb.getScore(c);
			}
			Collections.sort(preCoords);// �����е÷ֽ�������(��С����)
			if (getLayer(nowDepth) == MAXLAYER)
				Collections.reverse(preCoords);

			//int i = 0;
			ArrayList<Coord> bestPoint = new ArrayList<Coord>();// �ò���Ҫ����ĵ�
			int bestScore = getLayer(nowDepth) == MINLAYER ? Integer.MAX_VALUE
					: Integer.MIN_VALUE;
			for (Coord c : preCoords)// ��Ը������п��µĵ㣬չ��������һ��
			{
				// if(i==nodeNum) break;//�ﵽÿ��ϣ��չ���Ľڵ��������ˣ���ǰ��ֹѭ��
				if (Math.abs(c.score) >= 500000) {
					c.score = vb.getFullScore(c);
					return c;
				}
				vb.setCoord(c);// ������������Ļ�
				Coord newcoord = analyze(vb, nowDepth + 1, bestScore);
				vb.removeCoord(c);// ȥ���õ�
				if (getLayer(nowDepth) == MINLAYER) {
					if (newcoord.score < bestScore) {
						bestScore = newcoord.score;
						bestPoint.clear();
						c.score = bestScore;
						bestPoint.add(c);
					} else if (newcoord.score == bestScore) {
						c.score = bestScore;
						bestPoint.add(c);
					}
					if (bestScore < lastScore)
						break;// alpha��֦
				} else {
					if (newcoord.score > bestScore) {
						bestScore = newcoord.score;
						bestPoint.clear();
						c.score = bestScore;
						bestPoint.add(c);
					} else if (newcoord.score == bestScore) {
						c.score = bestScore;
						bestPoint.add(c);
					}
					if (bestScore > lastScore)
						break;// beta��֦
				}
				//i++;
			}
			return bestPoint.get((int) (Math.random() * bestPoint.size()));
		}
	}

	// private Coord multiThreadAnalyze(int threadNum,Chessboard vb) {}

}
