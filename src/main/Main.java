package main;

//main class
public class Main {

	/*Records two phrases and analyzes them, 
	returning a warpDistance that shows how far apart two sounds sequences are*/
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Record rec1 = new Record();
		System.out.println("Recording First Phrase:");
		rec1.rec();
		System.out.println("First Phrase Saved.");
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Record rec2 = new Record();
		System.out.println("Recording Second Phrase:");
		rec2.rec();
		System.out.println("Second Phrase Saved.");

		byte[] seq1 = rec1.getByteArray();
		float[] seq_1 = rec1.getFloatArray();
		double[] seq__1 = rec1.getDoubleArray();
		byte[] seq2 = rec2.getByteArray();
		float[] seq_2 = rec2.getFloatArray();
		double[] seq__2 = rec2.getDoubleArray();
		System.out.println("Length of sequence 1:\t" + seq1.length);
		System.out.println("Length of sequence 2:\t" + seq2.length);
		MFCC mfcc1 = new MFCC(seq__1);
		MFCC mfcc2 = new MFCC(seq__2);
		double[][] sequence1 = mfcc1.process();
		double[][] sequence2 = mfcc2.process();
		double[][] input_sequence = new double[sequence1.length][sequence1[0].length];
		for(int i = 0; i < sequence1.length; i++){
			for(int j = 0; j < sequence1[i].length; j++){
				input_sequence[i][j] = sequence1[i][j] - sequence2[i][j];
			}
		}
		DTW dtw = new DTW(input_sequence);
		System.out.println(dtw.getLimitedInfo());
	}

}
