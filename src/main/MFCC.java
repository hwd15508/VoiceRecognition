package main;

public class MFCC {
	
	/*Variables:
	 * minFreq: minimum Frequency bin
	 * maxFreq: maximum Frequency bin
	 * windowSize: sample size of each frame (must be 2^n for FFT)
	 * numberCoefficients: number of MFCCs per frame
	 * sampleRate: samples per second of audio format
	 */
	
	double minFreq = 20;
	double maxFreq = 16000;
	int numberFilters = 40;
	int windowSize = 512;
	int numberCoefficients = 20;
	float sampleRate = 4000;
	int hopSize = null;
	double baseFreq = null;
	
	public MFCC(double minFreq, double maxFreq, int numberFilters, int windowSize,
			int numberCoefficients, float sampleRate){
		this.minFreq = minFreq;
		this.maxFreq = maxFreq;
		this.numberFilters = numberFilters;
		this.windowSize = windowSize;
		this.numberCoefficients = numberCoefficients;
		this.sampleRate = sampleRate;
		this.hopSize = this.windowSize/2;
		this.baseFreq = sampleRate/windowSize;
	}
	
	//converts linear frequencies to the mel scale
	private double linToMelFreq(double linFreq){
		return (2595.0 * (Math.log(1.0 + linFreq / 700.0) / Math.log(10.0)));
	}
	
	//converts mel frequencies to the linear scale
	private double melToLinFreq(double melFreq){
		return (700.0 * (Math.pow(10.0, (melFreq / 2595.0)) - 1.0));
	}
	
	//finds the Boundaries for the Mel Filter Bank
	private double[] getMelFilterBankBoundaries(double minFreq, double maxFreq, int numberFilters){
		/*variables needed for method:
		 * centers: array with list of boundary points
		 * maxFreqMel: max Frequency on mel scale
		 * minFreqMel: min Frequency on mel scale
		 * deltaFreqMel: size of each filter bank
		 * nextCenterMel: next boundary point
		 */
	    double[] centers = new double[numberFilters + 2];
	    double maxFreqMel, minFreqMel, deltaFreqMel, nextCenterMel;

	    //compute mel min/max frequency
	    maxFreqMel = linToMelFreq(maxFreq);
	    minFreqMel = linToMelFreq(minFreq);
	    deltaFreqMel = (maxFreqMel - minFreqMel)/(numberFilters + 1);

	    //create (numberFilters + 2) equidistant points for the triangles
	    nextCenterMel = minFreqMel;
	    for(int i = 0; i < centers.length; i++)
	    {
	      //transform the points back to linear scale
	      centers[i] = melToLinFreq(nextCenterMel);
	      nextCenterMel += deltaFreqMel;
	    }

	    //adjust boundaries to exactly fit the given min/max frequency
	    centers[0] = minFreq;
	    centers[numberFilters + 1] = maxFreq;

	    return centers;
	}
	
	//
}
