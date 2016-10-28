/*
  Class of Russia Box. Controll each Box unit.
  Created by chi on 2016/10/18.
  Copyright © 2016年 chi. All rights reserved.
*/
public class GameBox implements Cloneable {
	private boolean isColor; //true: if box has color

	public GameBox(boolean isColor) {
		this.isColor = isColor;
	}

	//set color to the boxes
	public void setBoxColor(boolean isColor) {
		this.isColor = isColor;
	}

	//if boxes have color
	public boolean isColorBox() {
		return this.isColor;
	} 

	public Object clone() {
		Object box = null;
		try {
			box = super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return box;
	}
}