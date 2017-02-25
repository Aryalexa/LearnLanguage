package testingAudio;


/**
 * 
 * from the waveform library used in android form the drawings
 *
 */

public class Waveform {

	
	private short[] mSamples;
	private int width = 8;
	private float centerY = 0;
	
	
	public Waveform(){}
	  
	public void setSamples(short[] samples) {
	    mSamples = samples;
	    //calculateAudioLength();
	    onSamplesChanged();
	}

	private void onSamplesChanged() {
		  //mHistoricalData = new LinkedList<>();
          //LinkedList<float[]> temp = new LinkedList<>(mHistoricalData);

          // For efficiency, we are reusing the array of points.
		  
	      float[] waveformPoints;
	      waveformPoints = new float[width * 4];
	        

	      drawRecordingWaveform(mSamples, waveformPoints);
	        //temp.addLast(waveformPoints);
	        //mHistoricalData = temp;
	        //postInvalidate();
	        
	  }
	
	void drawRecordingWaveform(short[] buffer, float[] waveformPoints) {
	        float lastX = -1;
	        float lastY = -1;
	        int pointIndex = 0;
	        float max = Short.MAX_VALUE;

	        // For efficiency, we don't draw all of the samples in the buffer, but only the ones
	        // that align with pixel boundaries.
	        for (int x = 0; x < width; x++) {
	            int index = (int) (((x * 1.0f) / width) * buffer.length);
	            short sample = buffer[index];
	            float y = centerY - ((sample / max) * centerY);

	            if (lastX != -1) {
	                waveformPoints[pointIndex++] = lastX;
	                waveformPoints[pointIndex++] = lastY;
	                waveformPoints[pointIndex++] = x;
	                waveformPoints[pointIndex++] = y;
	            }

	            lastX = x;
	            lastY = y;
	        }
	    }
	  
	  
	
	public static void main(String[] args)  {
		
		

	}

}
