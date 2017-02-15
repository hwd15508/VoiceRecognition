package main;

//static class that finds Fast Fourier Transform of a sequence
public class FFT {

	private double[] windowFunction;
	private double windowFunctionSum;
	private int windowSize;
	private Complex[] input;

	// finds the FFT of a complex sequence
	public FFT(Complex[] x,int windowSize) {
		this.input = x;
		this.windowSize = windowSize;
		this.windowFunction = new double[this.windowSize];
		int start = (windowFunction.length - this.windowSize) / 2;
		int stop = (windowFunction.length + this.windowSize) / 2;
		double factor = 2 * Math.PI / (this.windowSize - 1.0d);
		for (int i = 0; start < stop; start++, i++)
			windowFunction[i] = 0.5 * (1 - Math.cos(factor * i));
		this.windowFunctionSum = 0;
		for (int i = 0; i < windowFunction.length; i++)
			windowFunctionSum += windowFunction[i];
		for (int i = 0; i < input.length; i++){
			input[i] = new Complex(input[i].re()*windowFunction[i],input[i].im());
		}
	}
	
	public Complex[] getInput(){
		return input;
	}

	public double[] getNormalizedPower(Complex[] x){
		double[] output = new double[x.length];
		double r,i;
		for(int j = 0; j<x.length; j++){
			r = x[j].re()/(windowFunctionSum*2);
			i = x[j].im()/(windowFunctionSum*2);
			output[j] = r*r + i*i;
		}
		return output;
	}
	
	public Complex[] fft(Complex[] x) {
		int n = x.length;
		if (n == 1) {
			return new Complex[] { x[0] };
		}
		if (n % 2 != 0) {
			throw new RuntimeException("n is not a power of 2");
		}
		Complex[] even = new Complex[n / 2];
		for (int k = 0; k < n / 2; k++) {
			even[k] = x[2 * k];
		}
		Complex[] q = fft(even);
		Complex[] odd = even;
		for (int k = 0; k < n / 2; k++) {
			odd[k] = x[2 * k + 1];
		}
		Complex[] r = fft(odd);
		Complex[] y = new Complex[n];
		for (int k = 0; k < n / 2; k++) {
			double kth = -2 * k * Math.PI / n;
			Complex wk = new Complex(Math.cos(kth), Math.sin(kth));
			y[k] = q[k].plus(wk.times(r[k]));
			y[k + n / 2] = q[k].minus(wk.times(r[k]));
		}
		return y;
	}

	// formatted output for sequence
	public static void output(Complex[] x) {
		for (int i = 0; i < x.length; i++) {
			System.out.println(x[i]);
		}
	}
}