package com.mygdx.game;

public class Mandelbrot {
	static int width;
	static int height;
	int aspect_width;
	int aspect_height;
	int iterations;
	static double xmin;
	static double xmax;
	static double ymin;
	static double ymax;
	static double xrange;
	static double yrange;
	double zoomx;
	double zoomy;
	double zoom;
	double xoff;
	double yoff;
	boolean change;
	
	
	double[] oldcoords;
	
	

	public Mandelbrot(int iter, int width, int height) {
		this.iterations = iter;
		this.width = width;
		this.height = height;
		this.aspect_width = this.find_aspect_ratio(width, height)[0];
		this.aspect_height = this.find_aspect_ratio(width, height)[1];
		this.oldcoords = new double[4];
		xmin = -2.5;
		xmax = 1.5;
		ymin = -1.125;
		ymax = 1.125;
		
		oldcoords[0] = xmin;
		oldcoords[1] = xmax;
		oldcoords[2] = ymin;
		oldcoords[3] = ymax;
		

		xrange = xmax - xmin;
		yrange = ymax - ymin;

		zoom = 1;

		xoff = 0;
		yoff = 0;

		zoomx = aspect_width / 10;
		zoomy = aspect_height / 10;

		change = true;

	}

	public Mandelbrot(int iter, int width, int height, double xmin, double xmax, double ymin, double ymax) {
		this.iterations = iter;
		this.width = width;
		this.height = height;
		this.aspect_width = this.find_aspect_ratio(width, height)[0];
		this.aspect_height = this.find_aspect_ratio(width, height)[1];
		this.xmin = xmin;
		this.xmax = xmax;
		this.ymin = ymin;
		this.ymax = ymax;
		xrange = xmax - xmin;
		yrange = ymax - ymin;
		change = true;
	}

	public int gcd(int num, int den) {
		int smaller, larger, gcd = 0;

		if (num > den) {
			smaller = den;
			larger = num;
		} else {
			smaller = num;
			larger = den;
		}
		if (smaller > (int) larger / 2) {
			for (int i = larger / 2; i > 1; i--) {
				if (smaller % i == 0 && larger % i == 0) {
					gcd = i;
					break;
				}
			}
		} else {
			for (int i = smaller; i > 1; i--) {
				if (smaller % i == 0 && larger % i == 0) {
					gcd = i;
					break;
				}
			}
		}
		return gcd;
	}

	public int iterations(int x0, int y0, int maxiter) {

		int iter = 0;
		double x = 0;
		double y = 0;
		double x2 = 0;
		double y2 = 0;
		double re;
		double im;
		double[] coords;
		coords = this.xytoimag(x0, height - y0);
		re = coords[0];
		im = coords[1];
		while (x2 + y2 <= 4 && iter < maxiter) {
			y = 2 * x * y + im;
			x = x2 - y2 + re;

			x2 = x * x;
			y2 = y * y;
			iter++;
		}

		if (iter == maxiter) {
			return 0;
		}
		return iter;
	}

	public int[] find_aspect_ratio(int width, int height) {
		int[] aspect = new int[2];
		int gcd = this.gcd(width, height);
		aspect[0] = width / gcd;
		aspect[1] = height / gcd;
		return aspect;

	}

	public double[] xytoimag(int x, int y) {
		double newx, newy;
		double[] coords = new double[2];

		newx = (xmin + (double) x / width * xrange);
		newy = (ymin + (double) y / height * yrange);

		coords[0] = newx;
		coords[1] = newy;

		return coords;
	}

	public void updateElements() {
		this.aspect_width = this.find_aspect_ratio(width, height)[0];
		this.aspect_height = this.find_aspect_ratio(width, height)[1];
		xrange = xmax - xmin;
		yrange = ymax - ymin;
		change = true;
	}

	public void setDefaultPos() {
		xmin = -2.5;
		xmax = 1.5;
		ymin = -1.125;
		ymax = 1.125;
		this.updateElements();
	}

	public void centerAt(double x, double y) {
		xmin = x - xrange / 2;
		xmax = x + xrange / 2;
		ymin = y - yrange / 2;
		ymax = y + yrange / 2;
		this.updateElements();
	}

	public void zoomMandel(float zoomFactor) {
		zoom /= zoomFactor;

		zoomx /= zoomFactor;

		zoomy /= zoomFactor;
		
		if (zoomFactor > 1) {
			xmin += zoomx;
			xmax -= zoomx;
			
			ymin += zoomy;
			ymax -= zoomy;
		}
		else {
			xmin -= zoomx;
			xmax += zoomx;
			
			ymin -= zoomy;
			ymax += zoomy;
		}
		updateElements();
		change = true;
	}

	public void changeX(double xfac) {
		xmin += xfac;
		xmax += xfac;
		updateElements();
	}

	public void changeY(double yfac) {
		ymin += yfac;
		ymax += yfac;
		updateElements();
	}

}
