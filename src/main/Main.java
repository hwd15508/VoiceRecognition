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
		byte[] seq2 = rec2.getByteArray();
		float[] seq_2 = rec2.getFloatArray();

		System.out.println("Audio Format:");
		System.out.println("Sample1:\t" + rec1.getFormat().toString());
		System.out.println("\n");

		System.out.println("Byte Array Lengths");
		System.out.println("Sample1:\t" + seq1.length);
		System.out.println("Sample2:\t" + seq2.length);
		System.out.println("\n");
		
		DTW dtw = new DTW(seq_1,seq_2);
		System.out.println(dtw.getLimitedInfo());
	}

}
