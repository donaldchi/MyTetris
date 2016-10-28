/*
  Game View class: control the game screen 
  Created by chi on 2016/10/18.
  Copyright © 2016年 chi. All rights reserved.
*/
import java.awt.*;
import java.util.*;
import java.awt.event.*;
import java.io.IOException;
import java.io.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

public class GameView extends JPanel {
	private GameBox [][] boxes;
	private int rows = 20, cols = 12;
	private static GameView gScreen = null;
	private static	int box_width, box_height;
	private Color boxColor = Color.RED, bgColor = new Color(0,0,0, 40);
	private EtchedBorder border=new EtchedBorder(EtchedBorder.RAISED,Color.WHITE, new Color(148, 145, 140));

	private final static int MOVE_RIGHT = 0;
	private final static int MOVE_LEFT = 1;

	private BufferedImage bgImage;

	public GameView() {
		boxes = new GameBox[rows][cols];
		for(int i = 0; i < boxes.length; i ++)
      		for(int j = 0; j < boxes[i].length; j ++)
				boxes[i][j] = new GameBox(false);
		setBorder(border);

		try {
			bgImage = ImageIO.read(new File("image/background.jpg"));
		} catch (IOException e){
			e.printStackTrace();
		}
	}

	public void reset () {
		int i, j;
		for (i=boxes.length-1; i>0; i--) 
			for (j=0; j<boxes[i].length; j++) 
					boxes[i][j].setBoxColor(false);
	}

	public void createBlock(int blockID, int blockPos_y) {
		setBoxColor(blockID);
		int i, nx, ny;
		for (i=0; i<8; i+=2) {
			nx = GameBlock.shape[blockID][i];
			ny = blockPos_y + GameBlock.shape[blockID][i+1];
			boxes[nx][ny].setBoxColor(true); 
		}
		repaint();
	}

	public void setBoxColor(int blockID) {
		if (blockID<2) boxColor = Color.RED;//0-1: 棒 
		else if(blockID<3) boxColor = Color.GREEN;//2: 四角形  
		else if(blockID<11) boxColor = Color.BLUE;//3-10: L型  
		else if(blockID<15) boxColor = Color.YELLOW;  //11-14: S型
		else boxColor = Color.WHITE; //T型
	}

	public boolean isMovable_VERTICAL(int blockID,  int blockPos_x, int blockPos_y) {
		int i, nx, ny;
		int count=0;
		for (i=0; i<8; i+=2) {
			nx = blockPos_x + GameBlock.shape[blockID][i];
			ny = blockPos_y + GameBlock.shape[blockID][i+1];
			
			// System.out.println(nx+1);
			if(nx+1 > 19) return false;
			count += (boxes[nx+1][ny].isColorBox()) ? 0 : 1;
		}

		if(count == GameBlock.isMovableCountVertical[blockID]) return true;

		return false;
	}

	public boolean isMovable_HORIZONTAL(int blockID, int blockPos_x, int blockPos_y, int direct) {
		int i, nx, ny;
		int count=0;
		for (i=0; i<8; i+=2) {
			nx = blockPos_x + GameBlock.shape[blockID][i];
			ny = blockPos_y + GameBlock.shape[blockID][i+1];

			switch(direct) {
				case MOVE_RIGHT:
					if((ny+1)>11) return false;
					else ny += 1;
					break;

				case MOVE_LEFT:
					if((ny-1)<0) return false;
					else ny -= 1;
					break;

				default:
					System.out.println("ERROR!!");
					return false;
			}

			count += (boxes[nx][ny].isColorBox()) ? 0 : 1;
		}
		if(count == GameBlock.isMovableCountHorizontal[blockID]) return true;

		return false;
	}

	public int blockDown(int blockID, int blockPos_x, int blockPos_y) {
		setBoxColor(blockID);

		int i, nx, ny;

		for (i=0; i<8; i+=2) {
			nx = blockPos_x + GameBlock.shape[blockID][i];
			ny = blockPos_y + GameBlock.shape[blockID][i+1];
			boxes[nx][ny].setBoxColor(false);
		}

		for (i=0; i<8; i+=2) {
			nx = blockPos_x + GameBlock.shape[blockID][i];
			ny = blockPos_y + GameBlock.shape[blockID][i+1];
			boxes[nx+1][ny].setBoxColor(true);
		}

		repaint();
		blockPos_x++;
		return blockPos_x;
	}
	//draw box
	public void paintComponent(Graphics g){
		super.paintComponent(g);
        
        //redraw box size according to 
        //the window size if it is changed
		adjustBox();

		g.drawImage(bgImage, 0, 0, getWidth(), getHeight(), this); 

		for(int i = 0; i < boxes.length; i ++)
      		for(int j = 0; j < boxes[i].length; j ++) {
          		Color color = boxes[i][j].isColorBox() ? boxColor : bgColor;
          		g.setColor(color);
          		g.fill3DRect(j * box_width, i * box_height , box_width , box_height , true);
        }
	}

	//adjust box size according to 
	//the window size if it is changed
	public void adjustBox() {
		box_width  = getSize().width / cols;
		box_height = getSize().height / rows;
	}

	/**********
	*  Block control funcitons
	***********/
	public int moveDown(int blockID, int blockPos_x, int blockPos_y) {
		if(isMovable_VERTICAL(blockID, blockPos_x, blockPos_y)) {
            blockPos_x = blockDown(blockID, blockPos_x, blockPos_y);
		} else 
			blockPos_x = -1;

		return blockPos_x;
	}

	public int moveRight(int blockID, int blockPos_x, int blockPos_y) {

		if(isMovable_HORIZONTAL(blockID, blockPos_x, blockPos_y, MOVE_RIGHT)) {
			setBoxColor(blockID);

			int i, nx, ny;
			for (i=0; i<8; i+=2) {
				nx = blockPos_x + GameBlock.shape[blockID][i];
				ny = blockPos_y + GameBlock.shape[blockID][i+1];
				boxes[nx][ny].setBoxColor(false);
			}

			for (i=0; i<8; i+=2) {
				nx = blockPos_x + GameBlock.shape[blockID][i];
				ny = blockPos_y + GameBlock.shape[blockID][i+1];
				boxes[nx][ny+1].setBoxColor(true);
			}
			blockPos_y += 1;
			repaint();
		} 

		return blockPos_y;
	}

	public int moveLeft(int blockID, int blockPos_x, int blockPos_y) {

		if(isMovable_HORIZONTAL(blockID, blockPos_x, blockPos_y, MOVE_LEFT)) {
			setBoxColor(blockID);

			int i, nx, ny;
			for (i=0; i<8; i+=2) {
				nx = blockPos_x + GameBlock.shape[blockID][i];
				ny = blockPos_y + GameBlock.shape[blockID][i+1];
				boxes[nx][ny].setBoxColor(false);
			}

			for (i=0; i<8; i+=2) {
				nx = blockPos_x + GameBlock.shape[blockID][i];
				ny = blockPos_y + GameBlock.shape[blockID][i+1];
				boxes[nx][ny-1].setBoxColor(true);
			}
			blockPos_y -= 1;
			repaint();
		}

		return blockPos_y;		
	}

	//Blockを消したり、描いたりする関数
	public void updateBlock(int blockID, int blockPos_x, int blockPos_y, boolean isDraw) {
		int i, nx, ny;
		for (i=0; i<8; i+=2) {
			nx = blockPos_x + GameBlock.shape[blockID][i];
			ny = blockPos_y + GameBlock.shape[blockID][i+1];
			if(isDraw) boxes[nx][ny].setBoxColor(true);
			else boxes[nx][ny].setBoxColor(false);
		}
		repaint();
	}

	public boolean isTurnable(int blockID, int blockPos_x, int blockPos_y) {
		int i, nx, ny;

		for (i=0; i<8; i+=2) {
			nx = blockPos_x + GameBlock.shape[blockID][i];
			ny = blockPos_y + GameBlock.shape[blockID][i+1];
			if(boxes[nx][ny].isColorBox()) 
				return false;
		}
		return true;
	}

	public int moveUP(int blockID, int blockPos_x, int blockPos_y) {
		int ID = blockID;
		switch(blockID) {  
			case GameBlock.I1: blockID=GameBlock.I2; break;  
			case GameBlock.I2: blockID=GameBlock.I1; break;  

			case GameBlock.O: blockID=GameBlock.O; break;  

			case GameBlock.L11: blockID=GameBlock.L12; break;  
			case GameBlock.L12: blockID=GameBlock.L13; break;  
			case GameBlock.L13: blockID=GameBlock.L14; break;  
			case GameBlock.L14: blockID=GameBlock.L11; break;  

			case GameBlock.L21: blockID=GameBlock.L22; break;  
			case GameBlock.L22: blockID=GameBlock.L23; break;  
			case GameBlock.L23: blockID=GameBlock.L24; break;  
			case GameBlock.L24: blockID=GameBlock.L21; break;  
			  
			case GameBlock.S11: blockID=GameBlock.S12; break;  
			case GameBlock.S12: blockID=GameBlock.S11; break;  

			case GameBlock.S21: blockID=GameBlock.S22; break;  
			case GameBlock.S22: blockID=GameBlock.S21; break; 

			case GameBlock.T11: blockID=GameBlock.T12; break; 
			case GameBlock.T12: blockID=GameBlock.T13; break; 
			case GameBlock.T13: blockID=GameBlock.T14; break; 
			case GameBlock.T14: blockID=GameBlock.T11; break; 
		}

		//delete original block
		updateBlock(ID, blockPos_x, blockPos_y, false);

		if(isTurnable(blockID, blockPos_x, blockPos_y)) {
			setBoxColor(blockID); 
			updateBlock(blockID, blockPos_x, blockPos_y, true);
		} else 
			updateBlock(ID, blockPos_x, blockPos_y, true);
		return blockID;
	}

	public void reDrawScreen(int row) {
		int i, j;
		for (i=row-1; i>0; i--) {
			for (j=0; j<boxes[i].length; j++) {
				if(boxes[i][j].isColorBox()) {
					boxes[i][j].setBoxColor(false);
					boxes[i+1][j].setBoxColor(true);
				}
			}
		}
	}

	public int clearLine() {
		int i, j;
		int temp=0, clearedLines=0;

		for (i=boxes.length-1; i>0; i--) {
			for (j=0; j<boxes[i].length; j++) {
				temp += boxes[i][j].isColorBox() ? 1 : 0;
			}
			if(temp == 12) {
				for (j=0; j<boxes[i].length; j++) 
					boxes[i][j].setBoxColor(false);
				reDrawScreen(i);
				clearedLines++;
			}
				temp=0;
		}
		return clearedLines;
	}

	/**********
	*  Game Notice Function
	***********/
	public boolean isGameOver() {
		for(int j=0; j<boxes[0].length; j++)
			if(boxes[0][j].isColorBox()) return true;
		return false;
	}
}
