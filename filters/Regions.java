package filters;

import java.awt.Color;
import java.awt.image.BufferedImage;

//This class is essentially a struct of 4 Kuwahara regions surrounding a pixel, along with each one's mean, sum and variance.
public class Regions {
	int area[][]; // 4
	int size[];// 4
	double sum[];// 4
	double var[];// 4 - the variance of each area
	int kernel; // kernel size

	public Regions(int kernel) {
		area = new int[4][];
		size = new int[4];
		sum = new double[4];
		var = new double[4];
		for (int i = 0; i < 4; i++) {
			area[i] = new int[kernel * kernel];
			size[i] = 0;
			sum[i] = 0;
			var[i] = 0.0;
		}
	}

	// Update data, increase the size of the area, update the sum
	void sendData(int areaValue, int data) {
		area[areaValue][size[areaValue]] = data;
		sum[areaValue] += data;
		size[areaValue]++;
		System.out.println("area[" + areaValue+"]["+size[areaValue]+"] = "+area[areaValue][size[areaValue]]+", sum["+areaValue+"] = " + sum[areaValue] + "size[" + areaValue+"] = " +size[areaValue] );
	}

	// Calculate the variance of each area
	double var(int areaValue) {
		double __mean = sum[areaValue] / size[areaValue];
		double temp = 0;
		for (int i = 0; i < size[areaValue]; i++) {
			temp += (area[areaValue][i] - __mean) * (area[areaValue][i] - __mean);
		}
		if (size[areaValue] == 1)
			return 1.7e38; // If there is only one pixel inside the region then
							// return the maximum of double
							// So that with this big number, the region will
							// never be considered in the below minVar()
		return Math.pow(temp / ((size[areaValue] - 1)), 2);
	}

	// calc the variances of all 4 areas
	void calcVar() {
		for (int i = 0; i < 4; i++) {
			var[i] = var(i);
			System.out.println("calcVar[i]=" + var[i]);
		}
	}

	// Find out which regions has the least variance
	int minVar() {
		calcVar();
		int i = 0;
		double __var = var[0];
		if (__var > var[1]) {
			__var = var[1];
			i = 1;
		}
		if (__var > var[2]) {
			__var = var[2];
			i = 2;
		}
		if (__var > var[3]) {
			__var = var[3];
			i = 3;
		}
		System.out.println("MINVAR = " + i);
		return i;
	}

	// Return the mean of that regions - CREATE THE COLOR
	int result() {
		int i = minVar();
		System.out.println("result : " + i);
		// return ((double) (sum[i] * 1.0 / size[i]));
		return (int) ((sum[i] * 1.0 / size[i]));

	}
};
