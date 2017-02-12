package main;

//Class that represents Complex numbers
public class Complex {
	/*Variables:
	 * re: the real part of a Complex number
	 * im: the imaginary part of a Complex number
	 */
    private final double re;
    private final double im;
    public Complex(double real, double imag) {
        re = real;
        im = imag;
    }

    //a nice output for Complex numbers
    public String toString() {
        if (im == 0) return re + "";
        if (re == 0) return im + "i";
        if (im <  0) return re + " - " + (-im) + "i";
        return re + " + " + im + "i";
    }

    //magnitude of Complex number
    public double abs() {
        return Math.hypot(re, im);
    }

    //phase of Complex number
    public double phase() {
        return Math.atan2(im, re);
    }

    //sum of Complex number
    public Complex plus(Complex b) {
        Complex a = this;             // invoking object
        double real = a.re + b.re;
        double imag = a.im + b.im;
        return new Complex(real, imag);
    }

    //difference of Complex number
    public Complex minus(Complex b) {
        Complex a = this;
        double real = a.re - b.re;
        double imag = a.im - b.im;
        return new Complex(real, imag);
    }

    //product of Complex number
    public Complex times(Complex b) {
        Complex a = this;
        double real = a.re * b.re - a.im * b.im;
        double imag = a.re * b.im + a.im * b.re;
        return new Complex(real, imag);
    }

    //scale the Complex number up (multiply it by another scalar value)
    public Complex scale(double alpha) {
        return new Complex(alpha * re, alpha * im);
    }

    //find the conjugate of Complex number
    public Complex conjugate() {
        return new Complex(re, -im);
    }

    //find the reciprocal of Complex number
    public Complex reciprocal() {
        double scale = re*re + im*im;
        return new Complex(re / scale, -im / scale);
    }

    //return real part of Complex number
    public double re() { return re; }
    //return imaginary part of Complex number
    public double im() { return im; }

    //quotient of Complex number
    public Complex divides(Complex b) {
        Complex a = this;
        return a.times(b.reciprocal());
    }
    
    //finds e raised to the power of Complex number
    public Complex exp() {
        return new Complex(Math.exp(re) * Math.cos(im), Math.exp(re) * Math.sin(im));
    }

    //finds sine of Complex number
    public Complex sin() {
        return new Complex(Math.sin(re) * Math.cosh(im), Math.cos(re) * Math.sinh(im));
    }

    //finds cosine of Complex number
    public Complex cos() {
        return new Complex(Math.cos(re) * Math.cosh(im), -Math.sin(re) * Math.sinh(im));
    }

    //finds tangent of Complex number
    public Complex tan() {
        return sin().divides(cos());
    }

    //sum of two Complex numbers
    public static Complex plus(Complex a, Complex b) {
        double real = a.re + b.re;
        double imag = a.im + b.im;
        Complex sum = new Complex(real, imag);
        return sum;
    }

    //determines whether two Copmlex numbers are equal
    public boolean equals(Object x) {
        if (x == null) return false;
        if (this.getClass() != x.getClass()) return false;
        Complex that = (Complex) x;
        return (this.re == that.re) && (this.im == that.im);
    }
}
