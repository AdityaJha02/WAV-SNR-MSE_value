import java.io.File;
import java.io.IOException;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

public class WavFileReader {

	public static void main(String[] args) {
		File file1 = new File("/Users/adityajha/Desktop/JavaApplication1 2/speechwav.wav");
		File file2 = new File("/Users/adityajha/Downloads/speechwav .wav");
		try {
			double[] actualArr = readWavFile(file1);
			double[] predictedArr = readWavFile(file2);
			double actualSnr = signalToNoiseRatio(actualArr);
			double predictedSnr = signalToNoiseRatio(predictedArr);
			System.out.println("The Input audio SNR value is "+actualSnr);
			System.out.println("The Output audio SNR value is "+predictedSnr);
			double mse = meanSquareError(actualArr, predictedArr);
			System.out.println("The input audio MSE value is "+mse);
			double rmse = Math.sqrt(mse);
			System.out.println("The Output audio MSE value is "+rmse);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static double[] readWavFile(File file) throws IOException {
		double[] readFile = null;
		WavFile wavFile = null;
		try {
			// Open the wav file specified as the first argument
			wavFile = WavFile.openWavFile(file);

			// Display information about the wav file
			wavFile.display();

			// Get the number of audio channels in the wav file
			int numChannels = wavFile.getNumChannels();
			long numOfFrames = wavFile.getNumFrames();
			// Create a buffer of 100 frames
			readFile = new double[(int) (numOfFrames * numChannels)];

			int framesRead;

			do {
				// Read frames into buffer
				framesRead = wavFile.readFrames(readFile, (int)numOfFrames);
			} while (framesRead != 0);
			
			for(int i=0; i<readFile.length;i++) {
				readFile[i] = Math.abs(readFile[i]*100);
			}
		} catch (Exception e) {
			System.err.println(e);
		} finally {
			// Close the wavFile
			wavFile.close();
		}
		return readFile;

	}

	public static double signalToNoiseRatio(double[] wavArr) {
		DescriptiveStatistics stats = new DescriptiveStatistics();

		// Add the data from the array
		for (int i = 0; i < wavArr.length; i++) {
			stats.addValue(wavArr[i]);
		}

		// Compute some statistics
		double mean = stats.getMean();
		double std = stats.getStandardDeviation();
		return std == 0 ? 0 : (mean / std)*10;
	}
	
	public static double meanSquareError(double[] actualArr, double[] predictedArr) {
		DescriptiveStatistics stats = new DescriptiveStatistics();
		int n = actualArr.length;
		double sum = 0;
		// Add the data from the array
		for (int i = 0; i < n; i++) {
			double diff = actualArr[100] - predictedArr[100];
			sum = sum + diff*diff;
		}

		return sum/n;
	}
}
