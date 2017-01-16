/*
  Game Control view class. Show all infomation about the game.
  Created by chi on 2016/10/18.
  Copyright © 2016年 chi. All rights reserved.
*/
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.io.*;
import java.lang.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

public class GameControl extends JPanel {

	public NextBlockPanel nextBlockPanel;
	private JPanel tipPanel, infoPanel, buttonPanel, rankingPanel;
	private JButton playButton, restartButton, quitButton,
                 levelUpButton,levelDownButton;

    private JLabel nextLabel;
    private JLabel level1Label, level2Label, level3Label, 
    				level4Label, level5Label;
    private JLabel score1Label, score2Label, score3Label, 
    				score4Label, score5Label;

    private EtchedBorder border=new EtchedBorder(EtchedBorder.RAISED,Color.WHITE, new Color(148, 145, 140)) ;

    private Tetris tetris;
    private Timer timer;

    private ImageIcon imageIconLevel, pauseIcon, playIcon;
    private ImageIcon zeroIcon, oneIcon, twoIcon, threeIcon, fourIcon, 
    					fiveIcon, sixIcon, sevenIcon, eightIcon, nineIcon; //show start according level

    private int curLevel, curScore; //store level info
    RankingControl rc; // ranking file controller;

	public GameControl(final Tetris tetris, int blockID) {
		setSize(240, 724);
		this.tetris = tetris;
		curLevel = 1;
		curScore = 0;

		setLayout(new GridLayout(4,1));

		nextBlockPanel = new NextBlockPanel(blockID);
		tipPanel = new JPanel( new BorderLayout() );
  		tipPanel.setBackground(new Color(130,39,22));

		nextLabel = new JLabel(new ImageIcon(new ImageIcon("image/next.png").getImage().getScaledInstance(240, 50, Image.SCALE_DEFAULT)));
		// nextLabel = new JLabel("next:");
		tipPanel.add( nextLabel, BorderLayout.NORTH);
		tipPanel.add(nextBlockPanel, BorderLayout.CENTER);
		tipPanel.setBorder(border);


		imageIconLevel = new ImageIcon(new ImageIcon("image/level.png").getImage().getScaledInstance(50, 50, Image.SCALE_DEFAULT));
		pauseIcon = new ImageIcon(new ImageIcon("image/pause.png").getImage().getScaledInstance(120, 60, Image.SCALE_DEFAULT));
		playIcon = new ImageIcon(new ImageIcon("image/play.png").getImage().getScaledInstance(120, 60, Image.SCALE_DEFAULT));
		zeroIcon = new ImageIcon(new ImageIcon("image/number/0.png").getImage().getScaledInstance(50, 50, Image.SCALE_DEFAULT));
		oneIcon = new ImageIcon(new ImageIcon("image/number/1.png").getImage().getScaledInstance(50, 50, Image.SCALE_DEFAULT));
		twoIcon = new ImageIcon(new ImageIcon("image/number/2.png").getImage().getScaledInstance(50, 50, Image.SCALE_DEFAULT));
		threeIcon = new ImageIcon(new ImageIcon("image/number/3.png").getImage().getScaledInstance(50, 50, Image.SCALE_DEFAULT));
		fourIcon = new ImageIcon(new ImageIcon("image/number/4.png").getImage().getScaledInstance(50, 50, Image.SCALE_DEFAULT));
		fiveIcon = new ImageIcon(new ImageIcon("image/number/5.png").getImage().getScaledInstance(50, 50, Image.SCALE_DEFAULT));
		sixIcon = new ImageIcon(new ImageIcon("image/number/6.png").getImage().getScaledInstance(50, 50, Image.SCALE_DEFAULT));
		sevenIcon = new ImageIcon(new ImageIcon("image/number/7.png").getImage().getScaledInstance(50, 50, Image.SCALE_DEFAULT));
		eightIcon = new ImageIcon(new ImageIcon("image/number/8.png").getImage().getScaledInstance(50, 50, Image.SCALE_DEFAULT));
		nineIcon = new ImageIcon(new ImageIcon("image/number/9.png").getImage().getScaledInstance(50, 50, Image.SCALE_DEFAULT));

		//first line
		level1Label = new JLabel(imageIconLevel);
		level2Label = new JLabel("");
		level3Label = new JLabel("");
		level4Label = new JLabel("");
		level5Label = new JLabel("");

		infoPanel = new JPanel(new GridLayout(2,5));
		infoPanel.add(level1Label);
		infoPanel.add(level2Label);
		infoPanel.add(level3Label);
		infoPanel.add(level4Label);
  		infoPanel.add(level5Label);
  		infoPanel.setBackground(new Color(130,39,22));

  		//second line
  		score1Label = new JLabel(zeroIcon);
  		score2Label = new JLabel(zeroIcon);
  		score3Label = new JLabel(zeroIcon);
  		score4Label = new JLabel(zeroIcon);
  		score5Label = new JLabel(zeroIcon);
  		infoPanel.add(score5Label);
  		infoPanel.add(score4Label);
  		infoPanel.add(score3Label);
  		infoPanel.add(score2Label);
  		infoPanel.add(score1Label);
  		infoPanel.setBorder(border);

  		buttonPanel = new JPanel(new BorderLayout());
  		buttonPanel.setBackground(new Color(130,39,22));
  		JPanel conrolPanel = new JPanel(new GridLayout(3,1));
  		conrolPanel.setPreferredSize(new Dimension(100,120));

  		playButton = new JButton("Pause", pauseIcon);
		playButton.setPreferredSize(new Dimension(10,10));
    	playButton.setHorizontalTextPosition(SwingConstants.CENTER);
		playButton.setForeground(new Color(0,0,0,0));
		playButton.setBorder(new LineBorder(new Color(0,0,0,0)));

  		restartButton = new JButton("", new ImageIcon(new ImageIcon("image/start.png").getImage().getScaledInstance(120, 60, Image.SCALE_DEFAULT)));
  		quitButton = new JButton("", new ImageIcon(new ImageIcon("image/stop.png").getImage().getScaledInstance(120, 60, Image.SCALE_DEFAULT)));
		restartButton.setPreferredSize(new Dimension(10,10));
		quitButton.setPreferredSize(new Dimension(10,10));
		restartButton.setBorder(new LineBorder(new Color(0,0,0,0)));
		quitButton.setBorder(new LineBorder(new Color(0,0,0,0)));

		conrolPanel.add(restartButton);
		conrolPanel.add(playButton);
		conrolPanel.add(quitButton);

  		levelUpButton = new JButton("", new ImageIcon(new ImageIcon("image/levelup.png").getImage().getScaledInstance(60, 180, Image.SCALE_DEFAULT)));
  		levelDownButton = new JButton("",new ImageIcon(new ImageIcon("image/leveldown.png").getImage().getScaledInstance(60, 180, Image.SCALE_DEFAULT)));
		levelUpButton.setPreferredSize(new Dimension(60,10));
		levelDownButton.setPreferredSize(new Dimension(60,10));

		buttonPanel.add(conrolPanel, BorderLayout.CENTER);
		buttonPanel.add(levelUpButton, BorderLayout.EAST);
		buttonPanel.add(levelDownButton, BorderLayout.WEST);

        //set ranking pannel
        setRankingPanel();

  		// rankingPanel.setBackground(new Color(130,39,22));
		rankingPanel.setBorder(border);

  		add(tipPanel);
   		add(infoPanel);
   		add(buttonPanel);
   		add(rankingPanel);

	    playButton.addActionListener(
			new ActionListener()
			{
			 public void actionPerformed(ActionEvent event)
			 {
			  if(playButton.getText().equals("Pause")) {
				playButton.setText("Play");
				playButton.setIcon(playIcon);
				tetris.pause();
			  } else if(playButton.getText().equals("Play")) {
				playButton.setText("Pause");
				playButton.setIcon(pauseIcon);
				tetris.play();
			  }
			  requestFocus();
			 }
		}); 

	    levelUpButton.addActionListener(
			new ActionListener()
			{
				public void actionPerformed(ActionEvent event) {
					tetris.levelUp();
			 		requestFocus();
				}
		}); 

	    levelDownButton.addActionListener(
			new ActionListener()
			{
				public void actionPerformed(ActionEvent event) {
					tetris.levelDown();
			 		requestFocus();
				}
		});

	    restartButton.addActionListener(
			new ActionListener() {
			 	public void actionPerformed(ActionEvent event) {
			 		tetris.reStart();
				}
		}); 

		quitButton.addActionListener(
			new ActionListener() {
			 	public void actionPerformed(ActionEvent event) {
			 		tetris.quit();
				}
		}); 		
	}

	private void setRankingPanel() {

  		//read ranking
        rc = new RankingControl();
        rc.read();

		rankingPanel = new JPanel(new BorderLayout());
  		rankingPanel.setBackground(new Color(130,39,22));

		JLabel rankingLabel = new JLabel(new ImageIcon(new ImageIcon("image/ranking.png").getImage().getScaledInstance(240, 50, Image.SCALE_DEFAULT)));
		rankingLabel.setPreferredSize(new Dimension(240, 50));

  		JPanel namePanel = new JPanel(new GridLayout(4,6));
  		namePanel.setBackground(new Color(130,39,22));

  		//first info
  		for(int i=0; i<4; i++) {
  			String name[] = {"", "", "", "", ""};
  			name = rc.rankInfo[i].getName().split(""); //get ranked user name
  			JLabel r = new JLabel(new ImageIcon(
  				new ImageIcon("image/number/"+(i+1)+".png").getImage().getScaledInstance(50, 50, Image.SCALE_DEFAULT)));
  			JLabel c0 = new JLabel(new ImageIcon(
  				new ImageIcon("image/character/"+name[0]+".png").getImage().getScaledInstance(50, 50, Image.SCALE_DEFAULT)));
  			JLabel c1 = new JLabel(new ImageIcon(
  				new ImageIcon("image/character/"+name[1]+".png").getImage().getScaledInstance(50, 50, Image.SCALE_DEFAULT)));
  			JLabel c2 = new JLabel(new ImageIcon(
  				new ImageIcon("image/character/"+name[2]+".png").getImage().getScaledInstance(50, 50, Image.SCALE_DEFAULT)));
  			JLabel c3 = new JLabel(new ImageIcon(
  				new ImageIcon("image/character/"+name[3]+".png").getImage().getScaledInstance(50, 50, Image.SCALE_DEFAULT)));
  			JLabel c4 = new JLabel(new ImageIcon(
  				new ImageIcon("image/character/"+name[4]+".png").getImage().getScaledInstance(50, 50, Image.SCALE_DEFAULT)));

			namePanel.add(r);
			namePanel.add(c0);
			namePanel.add(c1);
			namePanel.add(c2);
			namePanel.add(c3);
			namePanel.add(c4);
  		}    

  		rankingPanel.add(rankingLabel, BorderLayout.NORTH);
  		rankingPanel.add(namePanel, BorderLayout.CENTER);    
	}

	//weather ranked in top four
	public int isInRank() {
		for (int i=0; i<4; i++) {
			if(curScore>=rc.rankInfo[i].getScore()) 
				return (i+1);
		}
		return -1;
	}

	public void writeRankingFile(int rank, String name) {
		for (int i=rank; i<rc.rankInfo.length; i++) {
			rc.rankInfo[i].setInfo(
				(rc.rankInfo[i-1].getRank() + 1),
				rc.rankInfo[i-1].getName(),
				rc.rankInfo[i-1].getScore()
				);
		}

		rc.rankInfo[rank-1].setInfo(rank, name, curScore);

		rc.write();
		// for (int i=0; i<rc.rankInfo.length; i++) {
		// 	System.out.println(rc.rankInfo[i].getRank());
		// 	System.out.println(rc.rankInfo[i].getName());
		// 	System.out.println(rc.rankInfo[i].getScore());
		// }
	}

	//if level update is needed, return true
	public boolean setScore(int score) {
		if(score>0) {
			SoundControl bombBgm = new SoundControl();
			bombBgm.play(0, "audio/bgm_bomb.mp3");
		}

		curScore += score*Tetris.SCORE_PER_LINE;
		setScoreLabel();

		if(curScore >= Tetris.SCORE_FOR_UPDATE*curLevel) {
			SoundControl startBgm = new SoundControl();
        	startBgm.play(0, "audio/levelUp.mp3");
			return true;
		} 
		return false;
	}

	public void resetCurScore() {
		curScore = 0;
	}

	private void setScoreLabel() {
		int num, tmp = curScore;
		for(int i=4; i>=0; i--) {
			num = (int) (tmp/Math.pow(10, i));
			getScoreLabel(i+1).setIcon(getUnitNumber(num));
			tmp -= Math.pow(10, i)*num;
		}

	}

	//各単位の数字を返す。例：50だと、10単位の数字は5になるため、5を返す
	private ImageIcon getUnitNumber(int num) {
		switch (num) {
			case 0:
				return zeroIcon;
			case 1:
				return oneIcon;
			case 2:
				return twoIcon;
			case 3:
				return threeIcon;
			case 4:
				return fourIcon;
			case 5:
				return fiveIcon;
			case 6:
				return sixIcon;
			case 7:
				return sevenIcon;
			case 8:
				return eightIcon;
			case 9:
				return nineIcon;
		}
		return zeroIcon;
	}

	private JLabel getScoreLabel(int num) {
		switch (num) {
			case 1:
				return score1Label;
			case 2:
				return score2Label;
			case 3:
				return score3Label;
			case 4:
				return score4Label;
			case 5:
				return score5Label;
		}
		return score1Label;
	}

	public int getScore() {
		return curScore;
	}

	// 1: level up, -1: level down
	public void updateLevel(int isLevelUp) {
		// levelField.setText(""+(isLevelUp+curLevel));
		curLevel = isLevelUp + curLevel;
		System.out.println("Level: "+curLevel);
		setLevelLabel();
	}

	public int getLevel() {
		return curLevel;
	}

	private void setLevelLabel() {
		for(int i=1; i<=5; i++) {
			if(i<=curLevel) 
				getLevelLabel(i).setIcon(imageIconLevel);
			else 
				getLevelLabel(i).setIcon(null);
		}
	}

	private JLabel getLevelLabel(int curLevel) {
		switch (curLevel) {
			case 1:
				return level1Label;
			case 2:
				return level2Label;
			case 3:
				return level3Label;
			case 4:
				return level4Label;
			case 5:
				return level5Label;
		}
		return level1Label;
	}

	public void setNextBlock(int blockID) {
  		nextBlockPanel.setNextBlock(blockID);
  		nextBlockPanel.repaint();
 	}
/*
	Internal class.
	Control next block class. 
*/
	public class NextBlockPanel extends JPanel {
		private Color bgColor = new Color(0,0,0, 40),
					  boxColor = Color.lightGray;
		private GameBox [][] boxes = new GameBox[GameBlock.NEXT_BLOCK_ROWS][GameBlock.NEXT_BLOCK_COLS];
		private int box_width, box_height;
		private boolean isTiled = false; //??
		private int blockID;

		private BufferedImage bgImage;

		public NextBlockPanel(int blockID) {
			for(int i = 0; i < boxes.length; i ++)
	      		for(int j = 0; j < boxes[i].length; j ++)
					boxes[i][j] = new GameBox(false);
			setBorder(border);
			this.blockID = blockID;

			try {
				bgImage = ImageIO.read(new File("image/next_background.png"));
			} catch (IOException e){
				e.printStackTrace();
			}
		}

		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			setBoxColor(blockID);

		    if(!isTiled)
		    	adjustBox();

			g.drawImage(bgImage, 0, 0, getSize().width, getSize().height, this); 

		    for(int i = 0; i < boxes.length; i++)
		      	for(int j = 0; j<boxes[i].length; j++) {
		         	Color color = boxes[i][j].isColorBox() ?  boxColor : bgColor;
		         	g.setColor(color);
		         	g.fill3DRect(j * box_width, i * box_height, box_width, box_height, true);  
		        }

		    int i, nx, ny;
			for (i=0; i<8; i+=2) {
				nx = GameBlock.shape[blockID][i];
				ny = GameBlock.shape[blockID][i+1];
				boxes[nx][ny].setBoxColor(true); 
			}
		}

		//adjust box size according to 
		//the window size if it is changed
		public void adjustBox() {
			box_width  = getSize().width / GameBlock.NEXT_BLOCK_COLS;
			box_height = getSize().height / GameBlock.NEXT_BLOCK_ROWS;
		}

		public void setBoxColor(int blockID) {
			if (blockID<2) boxColor = Color.RED;//0-1: 棒 
			else if(blockID<3) boxColor = Color.GREEN;//2: 四角形  
			else if(blockID<11) boxColor = Color.BLUE;//3-10: L型  
			else if(blockID<15) boxColor = Color.YELLOW;  //11-14: S型
			else boxColor = Color.WHITE; //T型
		}

		//Blockを消したり、描いたりする関数
		public void updateBlock() {
			int i, j;
			for (i=0; i<GameBlock.NEXT_BLOCK_ROWS; i++) {
				for (j=0; j<GameBlock.NEXT_BLOCK_COLS; j++) {
					boxes[i][j].setBoxColor(false);					
				}
			}
		}

		public void setNextBlock(int blockID) {
			updateBlock();
			setBoxColor(blockID);
			// boxes[0][0].setBoxColor(true);
			int i, nx, ny;
			for (i=0; i<8; i+=2) {
				nx = GameBlock.shape[blockID][i];
				ny = GameBlock.shape[blockID][i+1];
				boxes[nx][ny].setBoxColor(true); 
			}
		        
			this.repaint();
		}
	}
}