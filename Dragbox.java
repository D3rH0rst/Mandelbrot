package com.mygdx.game;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class Dragbox {
	int x;
	int y;
	int w;
	int h;
	ShapeRenderer sr;

	Dragbox(ShapeRenderer sr, int x, int y, int w, int h) {
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		this.sr = sr;
	}

	public void render() {

		sr.rect(x, y, w, h);

	}

	public double[] convert_to_coords(Mandelbrot mandel, int startX, int startY, int endX, int endY) {

		double newCoords[] = new double[4];
		double[] coords1;
		double[] coords2;
		mandel.oldcoords[0] = mandel.xmin;
		mandel.oldcoords[1] = mandel.xmax;
		mandel.oldcoords[2] = mandel.ymin;
		mandel.oldcoords[3] = mandel.ymax;

		if (startX < endX) {
			if (startY < endY) {
				coords1 = mandel.xytoimag(startX, startY);
				coords2 = mandel.xytoimag(endX, endY);
			} else {
				coords1 = mandel.xytoimag(startX, endY);
				coords2 = mandel.xytoimag(endX, startY);
			}
		} else {
			if (startY < endY) {
				coords1 = mandel.xytoimag(endX, startY);
				coords2 = mandel.xytoimag(startX, endY);
			} else {
				coords1 = mandel.xytoimag(endX, endY);
				coords2 = mandel.xytoimag(startX, startY);
			}
		}

		newCoords[0] = coords1[0];
		newCoords[1] = coords2[0];
		newCoords[2] = coords1[1];
		newCoords[3] = coords2[1];

		return newCoords;

	}
}
