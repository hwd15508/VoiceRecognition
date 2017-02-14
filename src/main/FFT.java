package main;

//static class that finds Fast Fourier Transform of a sequence
public class FFT {

	//finds the FFT of a complex sequence
    public static Complex[] fft(Complex[] x) {
        int n = x.length;
        if(n == 1){
        	return new Complex[] { x[0] };
        }
        if(n % 2 != 0){
        	throw new RuntimeException("n is not a power of 2");
        }
        Complex[] even = new Complex[n/2];
        for(int k = 0; k < n/2; k++) {
            even[k] = x[2*k];
        }
        Complex[] q = fft(even);
        Complex[] odd  = even;
        for(int k = 0; k < n/2; k++) {
            odd[k] = x[2*k + 1];
        }
        Complex[] r = fft(odd);
        Complex[] y = new Complex[n];
        for(int k = 0; k < n/2; k++) {
            double kth = -2 * k * Math.PI / n;
            Complex wk = new Complex(Math.cos(kth), Math.sin(kth));
            y[k]       = q[k].plus(wk.times(r[k]));
            y[k + n/2] = q[k].minus(wk.times(r[k]));
        }
        return y;
    }

    //finds the inverse FFT of a complex sequence
    public static Complex[] inverse(Complex[] x) {
        int n = x.length;
        Complex[] y = new Complex[n];
        for(int i = 0; i < n; i++) {
            y[i] = x[i].conjugate();
        }
        y = fft(y);
        for(int i = 0; i < n; i++) {
            y[i] = y[i].conjugate();
        }
        for(int i = 0; i < n; i++) {
            y[i] = y[i].scale(1.0 / n);
        }
        return y;
    }

    //formatted output for sequence
    public static void output(Complex[] x) {
        for (int i = 0; i < x.length; i++) {
            System.out.println(x[i]);
        }
    }
}