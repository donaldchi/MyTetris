/*
  Class of Russia Block. Control each block that 
  consist of boxes.
  Created by chi on 2016/10/18.
  Copyright © 2016年 chi. All rights reserved.
*/

public class GameBlock extends Thread {

    public final static int NEXT_BLOCK_ROWS = 4;
    public final static int NEXT_BLOCK_COLS = 4; //each block consists of 4 boxes.
    public final static int TIME_TERM_BETWEEN_LEVEL = 50;

    public final static int I1 = 0; //棒形, I1: たて　I2: 横
    public final static int I2 = 1;

    public final static int O = 2; //正方形

    public final static int L11 = 3; //L形
    public final static int L12 = 4;
    public final static int L13 = 5;
    public final static int L14 = 6;

    public final static int L21 = 7; //L形
    public final static int L22 = 8;
    public final static int L23 = 9;
    public final static int L24 = 10;

    public final static int S11 = 11; //S形
    public final static int S12 = 12;

    public final static int S21 = 13;
    public final static int S22 = 14;

    public final static int T11 = 15; //T形
    public final static int T12 = 16;
    public final static int T13 = 17;
    public final static int T14 = 18;

    public final static int [][] shape= //19*8
    { //各形の座標
      {0,0,1,0,2,0,3,0},{0,0,0,1,0,2,0,3},//棒形
      {0,0,1,0,0,1,1,1},//正方形
      {0,0,1,0,1,1,1,2},{0,1,1,1,2,0,2,1},{0,0,0,1,0,2,1,2},{0,0,0,1,1,0,2,0},//L形
      {1,0,1,1,1,2,0,2},{0,0,0,1,1,1,2,1},{0,0,0,1,0,2,1,0},{0,0,1,0,2,0,2,1},//L形
      {0,0,0,1,1,1,1,2},{0,1,1,0,1,1,2,0},//S形
      {0,1,0,2,1,0,1,1},{0,0,1,0,1,1,2,1},//S形
      {0,1,1,0,1,1,1,2},{0,1,1,0,1,1,2,1},{0,0,0,1,0,2,1,1},{0,0,1,0,1,1,2,0} //T形
    };//Rectangular coordinates of each shape, x coordinate first

    public final static int [] isMovableCountVertical = {1,4,2,3,2,3,2,3,2,3,2,3,2,3,2,3,2,3,2};
    public final static int [] isMovableCountHorizontal = {4,1,2,2,3,2,3,2,3,2,3,2,3,2,3,2,3,2,3};
}
