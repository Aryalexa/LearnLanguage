package com.aryalexa.blobtracking;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 *
 * Needs: openCVLibrary310 as module library
 */

public class ColorTracker {


    private final static int CHUNK_SIZE = 512;
    private int num_CHUNKS;

    public ColorTracker(){

    }

    Mat createMap(Double[][] spect){

        num_CHUNKS = spect.length; // number of chunks

        Mat mat1 = new Mat(num_CHUNKS, CHUNK_SIZE, CvType.CV_8UC4);//CV_64FC1 );


        for (int i = 0; i< num_CHUNKS; i++){
            for (int j=0; j<CHUNK_SIZE; j++){
                // 1- To get the magnitude of the sound at a given frequency slice
                //      get the abs() from the complex number.
                // 2 - Or use Math.log to get a more managable number (used for color)
                //      double magnitude = Math.log(results[i][freq].abs()+1);

                mat1.put(i,j, spect[i][j]); // change to complex
            }
        }

        return mat1;


    }

    private Scalar mLowerBound = new Scalar(0);
    private Scalar mUpperBound = new Scalar(0);

    private static double mMinContourArea = 0.1;
    private List<MatOfPoint> mContours = new ArrayList<MatOfPoint>();

    // Cache
    Mat mPyrDownMat = new Mat();
    Mat mHsvMat = new Mat();
    Mat mThreshMat = new Mat();
    Mat mDilatedMask = new Mat();
    Mat mHierarchy = new Mat();

    void process(Mat mat){




        // 1) Get the thresholded image using inRange function,
        // and you can apply some erosion and dilation to remove small noisy particles.
        // It will help to improve the processing speed.

//        # smooth it
//        frame = cv2.blur(frame,(3,3))
        Mat mat2 = new Mat();// 1
        Imgproc.blur(mat, mat2, new Size(num_CHUNKS, CHUNK_SIZE));
        Imgproc.pyrDown(mat, mPyrDownMat);// 2
        Imgproc.pyrDown(mPyrDownMat, mPyrDownMat);


//        # convert to hsv and find range of colors
//        hsvFrame = cv2.cvtColor(frame,cv2.COLOR_BGR2HSV)
//        thresh = cv2.inRange(hsvFrame,np.array((0, 80, 80)), np.array((20, 255, 255)))
//        thresh2 = thresh.copy()
        Imgproc.cvtColor(mPyrDownMat, mHsvMat, Imgproc.COLOR_RGB2HSV_FULL);

        Core.inRange(mHsvMat, mLowerBound, mUpperBound, mThreshMat);
        Imgproc.dilate(mThreshMat, mDilatedMask, new Mat()); // ?


        // 2) find Contours using 'findContours' function
//        # find contours in the threshold image
//        contours,hierarchy = cv2.findContours(thresh,cv2.RETR_LIST,cv2.CHAIN_APPROX_SIMPLE)
        List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
        Imgproc.findContours(mDilatedMask, contours, mHierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);


        // 3) find areas of contours using 'contourArea' function and select one with maximum area.

//        # finding contour with maximum area and store it as best_cnt
//        max_area = 0
//        for cnt in contours:
//          area = cv2.contourArea(cnt)
//          if area > max_area:
//              max_area = area
//              best_cnt = cnt
        double maxArea = 0;
        Iterator<MatOfPoint> each = contours.iterator();
        // Find max contour area
        while (each.hasNext()) {
            MatOfPoint wrapper = each.next(); //next contour
            double area = Imgproc.contourArea(wrapper);
            if (area > maxArea)
                maxArea = area;
        }



        // 4) Now find its center as you did, and track it.

//        # finding centroids of best_cnt and draw a circle there
//        M = cv2.moments(best_cnt)
//        cx,cy = int(M['m10']/M['m00']), int(M['m01']/M['m00'])
//        cv2.circle(frame,(cx,cy),5,255,-1)

        // Filter contours by area and resize to fit the original image size
        mContours.clear();
        each = contours.iterator();
        while (each.hasNext()) {
            MatOfPoint contour = each.next();
            if (Imgproc.contourArea(contour) > mMinContourArea*maxArea) {
                Core.multiply(contour, new Scalar(4,4), contour);
                mContours.add(contour);
            }
        }

    }
}
