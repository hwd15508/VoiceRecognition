package main;

import java.net.URL;

//class that runs Dynamic Time Warping on two sequences
public class DTW implements Comparable<DTW>{

	/*
	 * Variables:
	 * x and y: The sequences to be analyzed 
	 * path: The final path taken that minimizes distance
	 * dist: Distance between sequences at each specific point
	 * sum_dist: Cumulative distance between sequences at a specific point (including all previous points)
	 * steps: Number of steps taken for the path warpDist: Total distance warped
	 */

	private double[][] path, dist, sum_dist;
	private int steps;
	private double warpDist;
	private double totalDist;

	public DTW(double[][] dist) {
		this.dist = dist;
		dtw(this.dist);
	}

	// dtw() runs the Dynamic Time Warp and puts in values for the path and warpDist variables
	public void dtw(double[][] dist) {
		sum_dist = new double[dist.length][dist[0].length];
		sum_dist[0][0] = dist[0][0];
		for (int i = 1; i < dist.length; i++) {
			sum_dist[i][0] = dist[i][0] + sum_dist[i - 1][0];
		}
		for (int i = 1; i < dist[0].length; i++) {
			sum_dist[0][i] = dist[0][i] + sum_dist[0][i - 1];
		}
		for (int j = 1; j < dist.length; j++) {
			for (int k = 1; k < dist[0].length; k++) {
				sum_dist[j][k] = getMin(sum_dist[j - 1][k - 1], sum_dist[j - 1][k], sum_dist[j][k - 1]) + dist[j][k];
			}
		}
		getPath(sum_dist);
		totalDist = sum_dist[dist.length - 1][dist[0].length - 1];
		warpDist = totalDist / (steps + 1);
	}

	// getPath() for organization, separate between finding sum_dist and path
	public void getPath(double[][] sum_dist) {
		steps = 0;
		int i = dist.length - 1;
		int j = dist[0].length - 1;
		path = new double[i + j][2];
		path[steps][0] = i;
		path[steps][1] = j;
		while (i > 0 && j > 0) {
			if (i == 0)
				j = j - 1;
			else if (j == 0)
				i = i - 1;
			else {
				if (getMin(sum_dist[i - 1][j - 1], sum_dist[i][j - 1], sum_dist[i - 1][j]) == sum_dist[i][j - 1]) {
					j = j - 1;
				} else if (getMin(sum_dist[i - 1][j - 1], sum_dist[i][j - 1],
						sum_dist[i - 1][j]) == sum_dist[i - 1][j]) {
					i = i - 1;
				} else {
					j = j - 1;
					i = i - 1;
				}
			}
			steps++;
			path[steps][0] = i;
			path[steps][1] = j;
		}
	}

	// getMin() finds minimum double between 3 doubles
	public double getMin(double x, double y, double z) {
		double min = x;
		min = (x > y) ? y : x;
		min = (x > z) ? z : x;
		return min;
	}

	//return warpingDistance
	public String getLimitedInfo(){
		String stmt =  "Dynamic Time Warp:\n\tWarpingDistance:\t" + warpDist + "\n";
		stmt += "Total Distance:\t" + totalDist;
		return stmt;
	}

	// Nicely displays path (deletes extraneous values) and warpDist after analysis
	public String toString() {
		String stmt = "Dynamic Time Warp:\n\tWarping Distance:\t" + warpDist + "\nPath Taken:";
		for (int i = 0; i < path.length; i++) {
			if (!(path[i][0] == 0 && path[i][1] == 0)) {
				stmt += "(" + path[i][0] + "," + path[i][1] + ")\t";
			}
		}
		return stmt;
	}

	@Override
	//allows for comparison between two pairs of data that DTW has been used on
	public int compareTo(DTW o) {
		// TODO Auto-generated method stub
		if(this.totalDist<o.totalDist){
			return -1;
		}else if(this.totalDist>o.totalDist){
			return 1;
		}
		return 0;
	}
}
