package main;

public class MFCC {

	/*
	 * Variables: minFreq: minimum Frequency bin maxFreq: maximum Frequency bin
	 * windowSize: sample size of each frame (must be 2^n for FFT)
	 * numberCoefficients: number of MFCCs per frame sampleRate: samples per
	 * second of audio format
	 */

	private double minFreq = 20;
	private double maxFreq = 8000;
	private int numberFilters = 40;
	private int windowSize = 128;
	private int numberCoefficients = 20;
	private float sampleRate = 4000;
	private int hopSize;
	private double baseFreq;
	private double[] buffer;
	private Matrix melFilterBanks;
	private Matrix dctMatrix;
	private double scale;
	private double[] input;

	public MFCC(double[] input) {
		this.hopSize = windowSize / 2;
		this.baseFreq = sampleRate / windowSize;
		this.buffer = new double[windowSize];
		this.input = input;
		melFilterBanks = getMelFilterBanks();
		dctMatrix = getDCTMatrix();
		scale = (Math.pow(10, 96/20));
	}
	
	public double[][] process(){
		double[][] mfcc = new double[input.length/hopSize-1][numberCoefficients];
		for(int i = 0, pos = 0; pos < input.length - hopSize; i++, pos+=hopSize){
			mfcc[i] = processWindow(input, pos);
		}
		return mfcc;
	}
	
	public double[] processWindow(double[] window, int start){
		int fftSize = (windowSize / 2) + 1;
		for(int i = 0; i< windowSize; i++){
			buffer[i] = window[i+start]*scale;
		}
		Complex[] input = new Complex[buffer.length];
		for(int i = 0; i< buffer.length; i++){
			input[i] = new Complex(buffer[i],0);
		}
		FFT fft = new FFT(input, (int)sampleRate);
		buffer = fft.getNormalizedPower(fft.fft(fft.getInput()));
		Matrix x = new Matrix(buffer, windowSize);
		x = x.getMatrix(0, fftSize-1,0,0);
		x = melFilterBanks.times(x);
		double log10 = 10*(1/Math.log(10));
		x.thrunkAtLowerBoundary(1);
		x.logEquals();
		x.timesEquals(log10);
		x = dctMatrix.times(x);
		return x.getColumnPackedCopy();
	}

	private Matrix getDCTMatrix() {
		// compute constants
		double k = Math.PI / numberFilters;
		double w1 = 1.0 / (Math.sqrt(numberFilters));// 1.0/(Math.sqrt(numberFilters/2));
		double w2 = Math.sqrt(2.0 / numberFilters);// Math.sqrt(2.0/numberFilters)*(Math.sqrt(2.0)/2.0);

		// create new matrix
		Matrix matrix = new Matrix(numberCoefficients, numberFilters);

		// generate dct matrix
		for (int i = 0; i < numberCoefficients; i++) {
			for (int j = 0; j < numberFilters; j++) {
				if (i == 0)
					matrix.set(i, j,  w1 * Math.cos(k * i * (j + 0.5d)));
				else
					matrix.set(i, j, w2 * Math.cos(k * i * (j + 0.5d)));
			}
		}

		return matrix;
	}

	// converts linear frequencies to the mel scale
	private double linToMelFreq(double linFreq) {
		return (2595.0 * (Math.log(1.0 + linFreq / 700.0) / Math.log(10.0)));
	}

	// converts mel frequencies to the linear scale
	private double melToLinFreq(double melFreq) {
		return (700.0 * (Math.pow(10.0, (melFreq / 2595.0)) - 1.0));
	}

	// finds the Boundaries for the Mel Filter Bank
	private double[] getMelFilterBankBoundaries(double minFreq, double maxFreq, int numberFilters) {
		/*
		 * variables needed for method: centers: array with list of boundary
		 * points maxFreqMel: max Frequency on mel scale minFreqMel: min
		 * Frequency on mel scale deltaFreqMel: size of each filter bank
		 * nextCenterMel: next boundary point
		 */
		double[] centers = new double[numberFilters + 2];
		double maxFreqMel, minFreqMel, deltaFreqMel, nextCenterMel;

		// compute mel min/max frequency
		maxFreqMel = linToMelFreq(maxFreq);
		minFreqMel = linToMelFreq(minFreq);
		deltaFreqMel = (maxFreqMel - minFreqMel) / (numberFilters + 1);

		// create (numberFilters + 2) equidistant points for the triangles
		nextCenterMel = minFreqMel;
		for (int i = 0; i < centers.length; i++) {
			// transform the points back to linear scale
			centers[i] = melToLinFreq(nextCenterMel);
			nextCenterMel += deltaFreqMel;
		}

		// adjust boundaries to exactly fit the given min/max frequency
		centers[0] = minFreq;
		centers[numberFilters + 1] = maxFreq;

		return centers;
	}

	private Matrix getMelFilterBanks() {
		// get boundaries of the different filters
		double[] boundaries = getMelFilterBankBoundaries(minFreq, maxFreq, numberFilters);

		// create the filter bank matrix
		double[][] matrix = new double[numberFilters][];

		// delete the boundary points outside of the spectrum
		for (int i = 1; i < boundaries.length - 1; i++) {
			if (boundaries[i] > sampleRate / 2) {
				numberFilters = i - 1;
				break;
			}
		}

		// fill each row of the filter bank matrix with one triangular mel
		// filter
		for (int i = 1; i <= numberFilters; i++) {
			double[] filter = new double[(windowSize / 2) + 1];

			// for each frequency of the fft
			for (int j = 0; j < filter.length; j++) {
				// compute the filter weight of the current triangular mel
				// filter
				double freq = baseFreq * j;
				filter[j] = getMelFilterWeight(i, freq, boundaries);
			}

			// add the computed mel filter to the filter bank
			matrix[i - 1] = filter;
		}

		// return the filter bank
		return new Matrix(matrix, numberFilters, (windowSize/2)+1);
	}

	private double getMelFilterWeight(int filterBank, double freq, double[] boundaries) {
		// for most frequencies the filter weight is 0
		double result = 0;

		// compute start- , center- and endpoint as well as the height of the
		// filter
		double start = boundaries[filterBank - 1];
		double center = boundaries[filterBank];
		double end = boundaries[filterBank + 1];
		double height = 2.0d / (end - start);

		// is the frequency within the triangular part of the filter
		if (freq >= start && freq <= end) {
			// depending on frequency position within the triangle
			if (freq < center) {
				// ...use a ascending linear function
				result = (freq - start) * (height / (center - start));
			} else {
				// ..use a descending linear function
				result = height + ((freq - center) * (-height / (end - center)));
			}
		}

		return result;
	}
}
