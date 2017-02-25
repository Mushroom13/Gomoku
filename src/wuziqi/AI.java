package wuziqi;

import java.util.ArrayList;
import java.util.Collections;

//机器人类
public class AI {
	int depth;
	Chessboard board;
	static int count;
	//final static int nodeNum = 25;// 每层希望展开的节点数
	final static int MAXLAYER = 0;
	final static int MINLAYER = 1;

	public AI(int d, Chessboard c) {
		depth = d;
		board = c;
	}
	public void tips(int newDepth) {
		System.out.println("AI帮你作弊，决策树深度："+newDepth);
		int temp = depth;
		depth = newDepth;
		if (Morpion.AI == 1) {
			Morpion.AI = 2;
			Morpion.HUMAN = 1;
		} else {
			Morpion.AI = 1;
			Morpion.HUMAN = 2;
		}
		long startMili = System.currentTimeMillis();// 当前时间对应的毫秒数
		count = 0;
		Chessboard virtualBoard = board.copy();
		Coord bestCoord = analyze(virtualBoard, 0, Integer.MAX_VALUE);
		long endMili = System.currentTimeMillis();
		System.out.println("AI提示你，最佳下点是：(" + bestCoord.x + "," + bestCoord.y + ")");
		System.out.println("本轮得分=" + bestCoord.score + " and 节点数=" + count + " and 耗时=" + (endMili - startMili) + "毫秒");
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
		System.out.println("AI思考中，决策树深度："+depth);
		long startMili = System.currentTimeMillis();// 当前时间对应的毫秒数
		count = 0;
		Chessboard virtualBoard = board.copy();
		Coord bestCoord = analyze(virtualBoard, 0, Integer.MAX_VALUE);

		long endMili = System.currentTimeMillis();
		System.out.println("AI下点：(" + bestCoord.x + "," + bestCoord.y + ")");
		System.out.println("本轮得分=" + bestCoord.score + " and 节点数=" + count + " and 耗时=" + (endMili - startMili) + "毫秒");
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
		// System.out.println("现在思考第"+nowDepth+"层");
		ArrayList<Coord> coords = vb.getAllCoords(); // 获取当前棋盘所有棋子
		int[][] tempMap = vb.getMap();// 获取棋盘下子的状况
		ArrayList<Coord> preCoords = new ArrayList<Coord>();// 本轮玩家可下的点
		for (Coord c : coords) {
			ArrayList<Coord> nearbyChess = vb.getNearbyChess(c, tempMap);// 遍历该棋子附近所有未尝试的空位
			for (Coord nc : nearbyChess) {
				nc.player = getPlayer(nowDepth);// 将该位置的player设置为本轮玩家
				// nc.score=vb.getScore(nc);//计算得分
				preCoords.add(nc);
				// System.out.println("("+nc.x+","+nc.y+","+nc.player+"):"+nc.score);
				tempMap[nc.x][nc.y] = -1;// 将该位置标记为已经尝试过
			}
		}

		if (nowDepth == depth - 1)// 如果深度满足了，直接返回分数最大的点
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
		} else// 深度未满，展开剪枝树
		{
			for (Coord c : preCoords) {
				c.score = vb.getScore(c);
			}
			Collections.sort(preCoords);// 将所有得分进行排序(从小到大)
			if (getLayer(nowDepth) == MAXLAYER)
				Collections.reverse(preCoords);

			//int i = 0;
			ArrayList<Coord> bestPoint = new ArrayList<Coord>();// 该层需要计算的点
			int bestScore = getLayer(nowDepth) == MINLAYER ? Integer.MAX_VALUE
					: Integer.MIN_VALUE;
			for (Coord c : preCoords)// 针对该轮所有可下的点，展开计算下一层
			{
				// if(i==nodeNum) break;//达到每层希望展开的节点数上限了，提前终止循环
				if (Math.abs(c.score) >= 500000) {
					c.score = vb.getFullScore(c);
					return c;
				}
				vb.setCoord(c);// 如果我下这个点的话
				Coord newcoord = analyze(vb, nowDepth + 1, bestScore);
				vb.removeCoord(c);// 去掉该点
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
						break;// alpha剪枝
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
						break;// beta剪枝
				}
				//i++;
			}
			return bestPoint.get((int) (Math.random() * bestPoint.size()));
		}
	}

	// private Coord multiThreadAnalyze(int threadNum,Chessboard vb) {}

}
