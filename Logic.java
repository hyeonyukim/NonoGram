import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;


public class Logic {
	public static Menu slide1;
	public static Map slide2;
	public static Result slide3;
	
	public static void main(String[] args) {
		// 두 슬라이드 생성. 처음에는 slide1을 보이게 함.
		slide1 = new Menu();
		slide2 = new Map();
		slide3 = new Result();
		slide1.setVisible(true);
		slide2.setVisible(false);
		slide3.setVisible(false);
	}

	public static void resultSlide() {
		// 슬라이드를 바꿔서 보이게 함.
		slide1.setVisible(false);
		slide2.setVisible(false);
		slide3.setVisible(true);
	}
	
	public static void switchSlide() {
		// 슬라이드를 바꿔서 보이게 함.
		slide1.setVisible(slide1.isVisible() ? false : true);
		slide2.setVisible(slide2.isVisible() ? false : true);
	}
}

class Menu extends JFrame implements ActionListener {
	// 가로, 세로 길이에 대한 상수.
	public static final int WIDTH = 400;
	public static final int HEIGHT = 600;
	// 변수.
	private int score = 0; // 점수.
	private JLabel title; // 게임 제목.
	private JLabel scoreLabel; // 점수 표시창.
	private JLabel team; // 조 이름
	private JPanel menuBar; // 메뉴를 담을 panel.
	private JPanel buttonPanel[]; // 버튼을 담을 panel, button에 마진을 주기위해 사용.
	private JButton stageButton[]; // 버튼.

	public Menu() {
		// JFrame 설정.
		setSize(WIDTH, HEIGHT);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("네모네모 로직");
		setLayout(new BorderLayout());

		// JLabel title 생성, JFrame의 BorderLayout.NORTH에 add.
		title = new JLabel("==네모네모 로직==");
		title.setFont(new Font(title.getFont().getName(), Font.PLAIN, 30));
		title.setHorizontalAlignment(SwingConstants.CENTER);
		title.setBorder(BorderFactory.createEmptyBorder(25,0,0,0));
		add(title, BorderLayout.NORTH);

		/*
		 * JLabel scoreLabel 생성, JFrame의 BorderLayout.SOUTH에 add. scoreLabel = new
		 * JLabel("Score : "); add(scoreLabel, BorderLayout.SOUTH);
		 */

		// JLabel team 생성, JFrame의 BorderLayout.SOUTH에 add.
		team = new JLabel("JAVA 프로그래밍 2조 - 최기락, 황정인, 김현유, 박종원");
		team.setHorizontalAlignment(SwingConstants.CENTER);
		team.setBorder(BorderFactory.createEmptyBorder(0,0,10,0));
		add(team, BorderLayout.SOUTH);

		// JPanel menuBar 생성, GridLayout(3,3).
		menuBar = new JPanel();
		menuBar.setLayout(new GridLayout(3, 3));

		stageButton = new JButton[3 * 3];
		for (int i = 1; i < 9; i++) {
			// JPanel buttonPanel[i]의 객체 생성, BorderLayout.
			menuBar.add(stageButton[i] = new JButton("Stage" + i));
			// JButton stageButton[i]의 객체 생성, BorderLayout.CENTER에 add.
			stageButton[i].addActionListener(this);
			stageButton[i].setActionCommand(i+"");
		}
		
		scoreLabel = new JLabel();
		scoreLabel.setText("Choose stage!"); // 초기 화면. 점수 나면 score 표시
		scoreLabel.setHorizontalAlignment(SwingConstants.CENTER);
		menuBar.add(scoreLabel);
		
		// menuBar을 JFramedml BorderLayout.CENTER에 add.
		menuBar.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
		this.add(menuBar, BorderLayout.CENTER);
	}

	public void actionPerformed(ActionEvent e) {
		// slide2로 전환.
		Logic.switchSlide();
		// slide2의 stage 정보를 정해줌(setStage)
		Logic.slide2.setStage(Integer.parseInt(e.getActionCommand()));
	}

	public void scoreAugment() {
		score++;
	}
}

class Map extends JFrame {
	// 가로, 세로 길이에 대한 상수.
	public static final int WIDTH = 400;
	public static final int HEIGHT = 600;
	public static final int MAX_STAGE = 8;
	public static final int ROW = 5;
	public static final int COL = 5;

	// 변수.
	private int answer[][][]; // 각 스테이지에 대한 해답을 담고 있는 배열.
	private int user[][]; // 스테이지에 대한 유저의 답을 담고 있는 배열.
	private int hint[][]; // 맵 옆에 표시되는 힌트들의 정보를 담고 있는 배열.
	private int stage; // 몇 스테이지인지 저장.
	private JLabel stageLabel; // 제목란에 스테이지를 표시함.
	private JPanel mapPanel; // 게임 창을 담는 Panel.
	private JPanel submitPanel; // submit 버튼을 담는 Panel.
	private JPanel buttonPanel;
	private JButton gridButton[][]; // 게임 버튼.
	private JButton submitButton; // 제출 버튼.
	private JPanel menuBar; // 게임 버튼 및 힌트 담는 Panel.

	public Map() {
		// JFrame 설정.
		setSize(WIDTH, HEIGHT);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("Visibility Demonstration");
		setLayout(new BorderLayout());
		
		gridButton = new JButton[ROW][];
		
		// Answer 객체 생성.
		answer = new int[MAX_STAGE][ROW][COL];
		answerInit();
		
		user = new int[ROW][COL];
		for(int i=0; i<ROW; i++) 
			user[i]=new int [COL];
		userErase();		//user배열의 값을 0으로 초기화. stage를 시작할 때마다 실행해줘야 함.
		
		// hint 객체 생성. hint의 정보는 다음과 같이 채워넣으면 됩니다!
		hint = new int[MAX_STAGE][ROW + COL]; 
		hintInit();
		/*
		 * 파워포인트에 있는 맵을 기준으로 상단에 표시된 hint를 왼쪽부터 먼저 저장하고, 이후에 좌측의 hint를 저장합니다. 따라서
		 * hint배열의 col 수는 ROW+COL이 됩니다. 이런 방향으로 하는 이유는 for문에 대응하기 쉽기 때문입니다. 하나의 hint에는
		 * 여러 정보가 포함될 수 있습니다. 이런 경우 편의상 클래스를 사용하지 않고 자릿수로 구분하여 씁니다. 예를 들어 hint[0][0]의
		 * 정보가 (1, 1)이라고 한다면 hint배열에는 자릿수로 구분해 11을 저장합니다. 이를 적용해 파워포인트의 맵에 대한 hint정보를
		 * 저장한다면 hint[2]={31,12,12,31,11,4,12,11,2,5}가 됩니다.
		 * 
		 */
		for (int i = 0; i < ROW; i++) {
			gridButton[i] = new JButton[COL];
			for (int j = 0; j < COL; j++) {
				gridButton[i][j] = new JButton();
				gridButton[i][j].setActionCommand(i + "" + j);
				gridButton[i][j].addActionListener(new Grid());
				gridButton[i][j].setOpaque(true);
			}
		}

		// JLabel stageLabel 생성, JFrame의 BorderLayout.NORTH에 add.
		stageLabel = new JLabel();
		stageLabel.setFont(new Font(stageLabel.getFont().getName(), Font.PLAIN, 20));
		stageLabel.setHorizontalAlignment(SwingConstants.CENTER);
		stageLabel.setBorder(BorderFactory.createEmptyBorder(20,0,0,0));
		add(stageLabel, BorderLayout.NORTH);
		
		// JPanel submitPanel 생성, BorderLayout.
		submitPanel = new JPanel();
		
		// JButton submitButton 생성. submitPanel에 add.
		submitButton= new JButton("Submit");
		submitButton.addActionListener(new Submit());
		submitButton.setPreferredSize(new Dimension(300, 45));
		submitPanel.add(submitButton);
		
		// submitPanel을 JFrame의 BorderLayout.SOUTH에 add.
		add(submitPanel, BorderLayout.SOUTH);

		// JPanel menuBar 생성, GridLayout(6,6).
		menuBar = new JPanel();
		menuBar.setLayout(new GridLayout(6, 6));
		
		int hintcnt = 0;
		for (int i = 0; i < 6; i++) {
			for (int j = 0; j < 6; j++) {
				if (i == 0 && j == 0)
					menuBar.add(new JLabel(""));
				else if (i == 0 ) {
					JLabel hintLabel;
					if((hint[stage][hintcnt] / 10) % 10==0) {
						hintLabel = new JLabel(""+hint[stage][hintcnt] % 10);
					}
					else if(hint[stage][hintcnt] / 100 ==0) {
						hintLabel = new JLabel("<html>"+(hint[stage][hintcnt] / 10) % 10
								+ "<br>" + hint[stage][hintcnt] % 10 + "</html>");
					}
					else{
						hintLabel = new JLabel("<html>"+hint[stage][hintcnt] / 100 + "<br>" + (hint[stage][hintcnt] / 10) % 10
								+ "<br>" + hint[stage][hintcnt] % 10 + "</html>");
					}
					hintLabel.setHorizontalAlignment(SwingConstants.CENTER);
					menuBar.add(hintLabel);
					hintcnt++;
				}
				else if (j == 0) {
					JLabel hintLabel;
					if((hint[stage][hintcnt] / 10) % 10==0) {
						hintLabel = new JLabel(""+hint[stage][hintcnt] % 10);
					}
					else if(hint[stage][hintcnt] / 100 ==0) {
						hintLabel = new JLabel("<html>"+(hint[stage][hintcnt] / 10) % 10
								+ " " + hint[stage][hintcnt] % 10 + "</html>");
					}
					else{
						hintLabel = new JLabel("<html>"+hint[stage][hintcnt] / 100 + " " + (hint[stage][hintcnt] / 10) % 10
								+ " " + hint[stage][hintcnt] % 10 + "</html>");
					}
					hintLabel.setHorizontalAlignment(SwingConstants.CENTER);
					menuBar.add(hintLabel);
					hintcnt++;
				}
				else {
					menuBar.add(gridButton[i - 1][j - 1]);
				}

			}
		}
		add(menuBar, BorderLayout.CENTER);
	}

	private void userErase() {
		// TODO Auto-generated method stub
		for(int i=0; i<ROW; i++) {
			for(int j=0; j<COL; j++)
				user[i][j]=0;
		}
	}

	private boolean compareMap() {
		// submit 버튼을 누르면 answer과 비교해서 정답인지 아닌지 비교.
		// 정답이라면 slide1의 scoreAugment() 실행
		int i,j;
		
		for(i=0; i<ROW; i++)
			for(j=0; j<COL; j++)
			{
				if(answer[stage-1][i][j] != user[i][j])
					return false;
			}
		
		return true;
	}

	public void setStage(int s) {
		// slide1에서 2로 넘어올 때 stage정보를 설정해줌.
		stage=s;
		stageLabel.setText("==Stage "+stage+"==");
		
	}

	private class Grid implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			// 버튼을 눌러줬을 때, 버튼의 배경을 바꿔줌.
			int i = Integer.parseInt(e.getActionCommand())/10;
			int j = Integer.parseInt(e.getActionCommand())%10;
			if(gridButton[i][j].getBackground()==Color.BLUE) {
				gridButton[i][j].setBackground(null);
				user[i][j] = 0;
				
			}
			else {
				gridButton[i][j].setBackground(Color.BLUE);
				user[i][j] = 1;
				
				//버튼을 누를때 i랑 j값을 받아와서 해당하는 버튼의 색갈을 바꿀수 있게 해야함.
				// 또한 user배열에 이 정보를 저장해야 함.
			}
		}
	}
	
	private class Submit implements ActionListener {
		
		public void actionPerformed(ActionEvent e) {
			// submit 버튼을 눌렀을 때 compareMap()을 실행해서
			// slide1의 score을 바꿔줌.
			if(compareMap())
			{
				Logic.slide1.scoreAugment();
				userErase();
				colorErase();
				Logic.resultSlide();
			}
			else
			{
				stageLabel.setText("Do it again!");
			}
			//점수를 맨처음 초기화면에서 부터 이 맵까지 사용할 수 있는 전역변수가 필요.
		}
		
		private void colorErase()
		{
			int i,j;
			for(i=0; i<ROW; i++)
				for(j=0; j<COL; j++)
					gridButton[i][j].setBackground(null);
		}

	}
	
	private void answerInit() {
		// TODO Auto-generated method stub
		for(int i=0; i<MAX_STAGE; i++) {
			answer[i] = new int[ROW][COL]; 
			for(int j=0; j<ROW; j++) {
				answer[i][j] = new int [COL];
			}
		}
		int i=0;
		answer[i][0][0]=0;	answer[i][0][1]=0;	answer[i][0][2]=1;	answer[i][0][3]=0;	answer[i][0][4]=0;
		answer[i][1][0]=0;	answer[i][1][1]=1;	answer[i][1][2]=1;	answer[i][1][3]=1;	answer[i][1][4]=0;
		answer[i][2][0]=0;	answer[i][2][1]=1;	answer[i][2][2]=0;	answer[i][2][3]=1;	answer[i][2][4]=0;
		answer[i][3][0]=0;	answer[i][3][1]=1;	answer[i][3][2]=1;	answer[i][3][3]=1;	answer[i][3][4]=0;
		answer[i][4][0]=1;	answer[i][4][1]=0;	answer[i][4][2]=1;	answer[i][4][3]=0;	answer[i][4][4]=1;
		//
		//  * 
		// ***
		// * *
		// ***
		//* * *
		//Rocket
		
		i++;
		answer[i][0][0]=0;	answer[i][0][1]=0;	answer[i][0][2]=1;	answer[i][0][3]=0;	answer[i][0][4]=0;
		answer[i][1][0]=1;	answer[i][1][1]=0;	answer[i][1][2]=1;	answer[i][1][3]=0;	answer[i][1][4]=0;
		answer[i][2][0]=1;	answer[i][2][1]=1;	answer[i][2][2]=1;	answer[i][2][3]=0;	answer[i][2][4]=1;
		answer[i][3][0]=0;	answer[i][3][1]=0;	answer[i][3][2]=1;	answer[i][3][3]=1;	answer[i][3][4]=1;
		answer[i][4][0]=0;	answer[i][4][1]=0;	answer[i][4][2]=1;	answer[i][4][3]=0;	answer[i][4][4]=0;
		//
		//  *
		//* *
		//*** *
		//  ***
		//  * 
		//Cactus
		
		i++;
		answer[i][0][0]=0;	answer[i][0][1]=1;	answer[i][0][2]=1;	answer[i][0][3]=0;	answer[i][0][4]=0;
		answer[i][1][0]=1;	answer[i][1][1]=1;	answer[i][1][2]=1;	answer[i][1][3]=0;	answer[i][1][4]=0;
		answer[i][2][0]=0;	answer[i][2][1]=1;	answer[i][2][2]=1;	answer[i][2][3]=1;	answer[i][2][4]=1;
		answer[i][3][0]=0;	answer[i][3][1]=1;	answer[i][3][2]=1;	answer[i][3][3]=1;	answer[i][3][4]=0;
		answer[i][4][0]=0;	answer[i][4][1]=0;	answer[i][4][2]=1;	answer[i][4][3]=0;	answer[i][4][4]=0;
		//
		// **
		//***
		// ****
		// ***
		//  *
		//Bird
		
		i++;
		answer[i][0][0]=1;	answer[i][0][1]=0;	answer[i][0][2]=1;	answer[i][0][3]=0;	answer[i][0][4]=1;
		answer[i][1][0]=1;	answer[i][1][1]=0;	answer[i][1][2]=1;	answer[i][1][3]=0;	answer[i][1][4]=1;
		answer[i][2][0]=1;	answer[i][2][1]=1;	answer[i][2][2]=1;	answer[i][2][3]=1;	answer[i][2][4]=1;
		answer[i][3][0]=0;	answer[i][3][1]=0;	answer[i][3][2]=1;	answer[i][3][3]=0;	answer[i][3][4]=0;
		answer[i][4][0]=0;	answer[i][4][1]=0;	answer[i][4][2]=1;	answer[i][4][3]=0;	answer[i][4][4]=0;
		//
		//* * *
		//* * *
		//*****
		//  *
		//  *
		//Fork
		
		i++;
		answer[i][0][0]=0;	answer[i][0][1]=0;	answer[i][0][2]=1;	answer[i][0][3]=1;	answer[i][0][4]=0;
		answer[i][1][0]=0;	answer[i][1][1]=0;	answer[i][1][2]=1;	answer[i][1][3]=1;	answer[i][1][4]=1;
		answer[i][2][0]=0;	answer[i][2][1]=0;	answer[i][2][2]=1;	answer[i][2][3]=0;	answer[i][2][4]=0;
		answer[i][3][0]=1;	answer[i][3][1]=1;	answer[i][3][2]=1;	answer[i][3][3]=1;	answer[i][3][4]=1;
		answer[i][4][0]=0;	answer[i][4][1]=1;	answer[i][4][2]=1;	answer[i][4][3]=1;	answer[i][4][4]=0;
		//
		//  **
		//  ***
		//  *
		//*****
		// ***
		//Boat
		
		i++;
		answer[i][0][0]=0;	answer[i][0][1]=1;	answer[i][0][2]=1;	answer[i][0][3]=1;	answer[i][0][4]=0;
		answer[i][1][0]=1;	answer[i][1][1]=0;	answer[i][1][2]=1;	answer[i][1][3]=0;	answer[i][1][4]=1;
		answer[i][2][0]=1;	answer[i][2][1]=0;	answer[i][2][2]=1;	answer[i][2][3]=1;	answer[i][2][4]=1;
		answer[i][3][0]=1;	answer[i][3][1]=0;	answer[i][3][2]=0;	answer[i][3][3]=0;	answer[i][3][4]=1;
		answer[i][4][0]=0;	answer[i][4][1]=1;	answer[i][4][2]=1;	answer[i][4][3]=1;	answer[i][4][4]=0;
		//
		// ***
		//* * *
		//* ***
		//*   *
		// ***
		//Clock
		
		i++;
		answer[i][0][0]=1;	answer[i][0][1]=0;	answer[i][0][2]=0;	answer[i][0][3]=0;	answer[i][0][4]=1;
		answer[i][1][0]=1;	answer[i][1][1]=1;	answer[i][1][2]=0;	answer[i][1][3]=1;	answer[i][1][4]=1;
		answer[i][2][0]=1;	answer[i][2][1]=1;	answer[i][2][2]=1;	answer[i][2][3]=1;	answer[i][2][4]=1;
		answer[i][3][0]=1;	answer[i][3][1]=1;	answer[i][3][2]=0;	answer[i][3][3]=1;	answer[i][3][4]=1;
		answer[i][4][0]=1;	answer[i][4][1]=0;	answer[i][4][2]=0;	answer[i][4][3]=0;	answer[i][4][4]=1;
		//
		//*   *
		//** **
		//*****
		//** **
		//*   *
		//Ribbon
		
		i++;
		answer[i][0][0]=1;	answer[i][0][1]=1;	answer[i][0][2]=0;	answer[i][0][3]=1;	answer[i][0][4]=1;
		answer[i][1][0]=0;	answer[i][1][1]=0;	answer[i][1][2]=1;	answer[i][1][3]=0;	answer[i][1][4]=0;
		answer[i][2][0]=0;	answer[i][2][1]=0;	answer[i][2][2]=1;	answer[i][2][3]=0;	answer[i][2][4]=0;
		answer[i][3][0]=1;	answer[i][3][1]=0;	answer[i][3][2]=0;	answer[i][3][3]=0;	answer[i][3][4]=1;
		answer[i][4][0]=0;	answer[i][4][1]=1;	answer[i][4][2]=1;	answer[i][4][3]=1;	answer[i][4][4]=0;
		//
		//** **
		//  *
		//  *
		//*   *
		// ***
		//Smile
	}
	
	private void hintInit() {
		for(int i=0; i<MAX_STAGE; i++) {
			hint[i] = new int[ROW+COL];
		}
		int i=0, j=0;
		hint[i][j++]=1;		hint[i][j++]=3;		hint[i][j++]=22;	hint[i][j++]=3;		hint[i][j++]=1;
		hint[i][j++]=1;		hint[i][j++]=3;		hint[i][j++]=11;	hint[i][j++]=3;		hint[i][j++]=111;
		//Rocket
		
		i++;	j=0;
		hint[i][j++]=2;		hint[i][j++]=5;		hint[i][j++]=5;		hint[i][j++]=1;		hint[i][j++]=2;
		hint[i][j++]=1;		hint[i][j++]=11;	hint[i][j++]=31;	hint[i][j++]=3;		hint[i][j++]=1;
		//Cactus
		
		i++;	j=0;
		hint[i][j++]=1;		hint[i][j++]=4;		hint[i][j++]=5;		hint[i][j++]=2;		hint[i][j++]=1;
		hint[i][j++]=2;		hint[i][j++]=3;		hint[i][j++]=4;		hint[i][j++]=3;		hint[i][j++]=1;
		//Bird
		
		i++;	j=0;
		hint[i][j++]=3;		hint[i][j++]=11;	hint[i][j++]=5;		hint[i][j++]=11;	hint[i][j++]=3;
		hint[i][j++]=111;	hint[i][j++]=111;	hint[i][j++]=5;		hint[i][j++]=1;		hint[i][j++]=3;
		//Fork
		
		i++;	j=0;
		hint[i][j++]=1;		hint[i][j++]=2;		hint[i][j++]=5;		hint[i][j++]=22;	hint[i][j++]=11;
		hint[i][j++]=2;		hint[i][j++]=3;		hint[i][j++]=1;		hint[i][j++]=5;		hint[i][j++]=3;
		//Boat
		
		i++;	j=0;
		hint[i][j++]=3;		hint[i][j++]=11;	hint[i][j++]=31;	hint[i][j++]=111;	hint[i][j++]=3;
		hint[i][j++]=3;		hint[i][j++]=111;	hint[i][j++]=13;	hint[i][j++]=11;	hint[i][j++]=3;
		//Clock
		
		i++;	j=0;
		hint[i][j++]=5;		hint[i][j++]=3;		hint[i][j++]=1;		hint[i][j++]=3;		hint[i][j++]=5;
		hint[i][j++]=11;	hint[i][j++]=22;	hint[i][j++]=5;		hint[i][j++]=22;	hint[i][j++]=11;
		//Ribbon
		
		i++;	j=0;
		hint[i][j++]=11;	hint[i][j++]=11;	hint[i][j++]=21;	hint[i][j++]=11;	hint[i][j++]=11;
		hint[i][j++]=22;	hint[i][j++]=1;		hint[i][j++]=1;		hint[i][j++]=11;	hint[i][j++]=3;
		//Smile
	}
}

class Result extends JFrame implements ActionListener {
	// 가로, 세로 길이에 대한 상수.
	public static final int WIDTH = 400;
	public static final int HEIGHT = 600;
	// 변수.
	
	private JLabel result; // 게임 제목.
	private JPanel buttonPanel; // 버튼을 담을 panel, button에 마진을 주기위해 사용.
	private JButton backButton; // 버튼.

	public Result() {
		// JFrame 설정.
		setSize(WIDTH, HEIGHT);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("네모네모 로직");
		setLayout(new BorderLayout());

		// JLabel title 생성, JFrame의 BorderLayout.NORTH에 add.
		result = new JLabel("Stage Clear!");
		result.setFont(new Font(result.getFont().getName(), Font.PLAIN, 30));
		result.setHorizontalAlignment(SwingConstants.CENTER);
		result.setBorder(BorderFactory.createEmptyBorder(25,0,0,0));
		add(result, BorderLayout.CENTER);

		buttonPanel = new JPanel();
		buttonPanel.add(backButton = new JButton("Back"));
		backButton.addActionListener(this);
		add(buttonPanel, BorderLayout.SOUTH);
		
	}

	public void actionPerformed(ActionEvent e) {
		// slide2로 전환.
		Logic.slide1.setVisible(true);
		Logic.slide3.setVisible(false);
	}
}
