package com.mygdx.game;

import java.util.Random;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.graphics.Color;

public class MyGdxGame extends ApplicationAdapter {
	static Mandelbrot mandel;
	Dragbox box;
	boolean boxrender = false;
	boolean boxstarted = false;
	boolean showtext = true;
	int mouseX;
	int mouseY;
	int realdy;
	int realendY;
	int startMouseX;
	int startMouseY;
	int endMouseX;
	int endMouseY;
	double[] newCoords;
	float[] cols;
	int colormode = 0;
	int width;
	int height;
	int iters;
	public static int maxiters = 1000;
	static double currentZoom = 1;

	Random random = new Random();

	float r;
	float g;
	float b;

	Pixmap pixels;
	SpriteBatch batch;
	BitmapFont text;
	Texture pixelstex;
	ShapeRenderer sr;

	@Override
	public void create() {

		width = Gdx.graphics.getWidth();
		height = Gdx.graphics.getHeight();

		mandel = new Mandelbrot(1000, width, height);

		batch = new SpriteBatch();
		text = new BitmapFont();

		sr = new ShapeRenderer();

	}

	@Override
	public void render() {
		Gdx.gl.glClearColor(0f, 0f, 0f, 0f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		mouseX = Gdx.input.getX();
		mouseY = height - (Gdx.input.getY());

		if (Gdx.input.isKeyPressed(Keys.B)) {
			maxiters = 1000;
			mandel.setDefaultPos();
		}

		else if (Gdx.input.isKeyPressed(Keys.ESCAPE)) {
			Gdx.app.exit();
			System.exit(-1);
		}

		else if (Gdx.input.isKeyPressed(Keys.W)) {
			maxiters += 100;
			mandel.change = true;
		}

		else if (Gdx.input.isKeyPressed(Keys.S)) {
			maxiters -= 100;
			mandel.change = true;
		}

		else if (Gdx.input.isKeyPressed(Keys.T)) {
			maxiters += 10000;
			mandel.change = true;
		}

		else if (Gdx.input.isKeyPressed(Keys.G)) {
			maxiters -= 10000;
			mandel.change = true;
		}

		else if (Gdx.input.isKeyPressed(Keys.A)) {
			maxiters += 1000;
			mandel.change = true;
		}

		else if (Gdx.input.isKeyPressed(Keys.D)) {
			maxiters -= 1000;
			mandel.change = true;
		}

		else if (Gdx.input.isKeyPressed(Keys.Z)) {
			mandel.xmin = mandel.oldcoords[0];
			mandel.xmax = mandel.oldcoords[1];
			mandel.ymin = mandel.oldcoords[2];
			mandel.ymax = mandel.oldcoords[3];
			mandel.updateElements();
		}

		else if (Gdx.input.isKeyJustPressed(Keys.K)) {
			showtext = !showtext;

		} else if (Gdx.input.isKeyJustPressed(Keys.C)) {
			colormode += 1;
			mandel.change = true;
		}

		else if (Gdx.input.isButtonJustPressed(Buttons.LEFT)) {
			startMouseX = mouseX;
			startMouseY = mouseY;
			boxstarted = true;
			boxrender = true;
		}

		else if (Gdx.input.isButtonPressed(Buttons.LEFT)) {
			boxrender = true;
		}

		if (!boxrender && boxstarted) {
			endMouseX = mouseX;
			endMouseY = mouseY;
			boxstarted = false;
			realendY = startMouseY + realdy;
			if (Math.abs(box.w) > 9) {
				newCoords = box.convert_to_coords(mandel, startMouseX, startMouseY, endMouseX, realendY);
				mandel.xmin = newCoords[0];
				mandel.xmax = newCoords[1];
				mandel.ymin = newCoords[2];
				mandel.ymax = newCoords[3];
				mandel.updateElements();
			}
		}

		if (mandel.change) {
			pixels = new Pixmap(width, height, Format.RGBA8888);
			for (int x = 0; x < width; x++) {
				for (int y = 0; y < height; y++) {
					iters = mandel.iterations(x, y, maxiters);
					if (iters == 0) {
						pixels.setColor(Color.BLACK);
						pixels.drawRectangle(x, y, 1, 1);
					} else {

						cols = this.calculateColor(colormode, iters, maxiters);

						r = cols[0];
						g = cols[1];
						b = cols[2];

						pixels.setColor(r, g, b, 1);
						pixels.drawRectangle(x, y, 1, 1);
					}
				}
			}
			pixelstex = new Texture(pixels);
			pixels.dispose();
			mandel.change = false;
			currentZoom = (int) 4 / mandel.xrange;
		}

		batch.begin();
		batch.draw(pixelstex, 0, 0);
		if (showtext) {

			text.draw(batch, "Iterations: " + maxiters, 50, height - 50);
			text.draw(batch, "Current Zoom: " + currentZoom, 50, height - 70);
			text.draw(batch, "B   : Go back to the default position", 50, height - 90);
			text.draw(batch, "W   : Up the iterations by 100", 50, height - 110);
			text.draw(batch, "S   : Reduce the iterations by 100", 50, height - 130);
			text.draw(batch, "A   : Up the iterations by 1000", 50, height - 150);
			text.draw(batch, "D   : Reduce the iterations by 1000", 50, height - 170);
			text.draw(batch, "T   : Up the iterations by 10000", 50, height - 190);
			text.draw(batch, "G   : Reduce the iterations by 10000", 50, height - 210);
			text.draw(batch, "K   : Show/Hide text", 50, height - 230);
			text.draw(batch, "Z   : Go to last position", 50, height - 250);
			text.draw(batch, "C   : Change color palette, current mode: " + (colormode % 4 + 1), 50, height - 270);
			text.draw(batch, "ESC : Exit", 50, height - 290);

		}
		batch.end();

		if (boxrender) {
			int deltax = mouseX - startMouseX;
			int deltay = mouseY - startMouseY;
			realdy = (int) (Math.signum(deltay) * (Math.abs(deltax) * 9 / 16));
			sr.begin(ShapeType.Line);

			box = new Dragbox(sr, startMouseX, startMouseY, mouseX - startMouseX, realdy);
			if (Math.abs(box.w) > 9) {
				box.render();
			}
			sr.end();
			boxrender = false;
		}

	}

	@Override
	public void dispose() {
		batch.dispose();
		sr.dispose();
		text.dispose();

	}

	public float linearInterpolate(int iters, int maxiters, float mode) {
		return mode * ((float) iters / (float) maxiters);
	}

	public float[] calculateColor(int mode, int iters, int maxiters) {
		float r, g, b;
		float[] color = new float[3];

		mode %= 4;

		switch (mode) {
		case 0:
			r = (float) Math.log(iters);
			g = (float) Math.log(Math.pow(iters, 0.5));
			b = this.linearInterpolate(iters, maxiters, 255);
			break;
		case 1:
			r = this.linearInterpolate(iters, maxiters, 1);
			g = 0;
			b = 0;
			break;
		case 2:
			r = (float) (Math.log(iters) * 3.14);
			g = (float) Math.log(Math.pow(iters, 0.5));
			b = this.linearInterpolate(iters, maxiters, 69);
			break;
		case 3:
			float h = (float) iters / maxiters;
			float x = h * 4.0f;
			if (x < 1) {
			    r = 0;
			    g = (int) (x * 255);
			    b = 255;
			} else if (x < 2) {
			    r = 0;
			    g = 255;
			    b = (int) (255 - (x - 1) * 255);
			} else if (x < 3) {
			    r = (int) ((x - 2) * 255);
			    g = 255;
			    b = 0;
			} else {
			    r = 255;
			    g = (int) (255 - (x - 3) * 255);
			    b = 0;
			}
			break;
		default:
			r = 1;
			g = 1;
			b = 1;
			break;
		}

		color[0] = r;
		color[1] = g;
		color[2] = b;

		return color;
	}

	public float[] generateRandomColor() {
		Random random = new Random();
		int red = random.nextInt(256);
		int green = random.nextInt(256);
		int blue = random.nextInt(256);

		float[] colors = new float[3];

		colors[0] = red;
		colors[1] = green;
		colors[2] = blue;

		return colors;
	}
}
