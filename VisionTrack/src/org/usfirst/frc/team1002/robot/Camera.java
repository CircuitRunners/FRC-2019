package org.usfirst.frc.team1002.robot;

import java.util.ArrayList;

import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import edu.wpi.cscore.CvSink;
import edu.wpi.cscore.CvSource;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.Timer;

public class Camera {
	private UsbCamera mainCam;
	private CvSink main;
	private int frameNumber = 0;
	private int frameWidth = 640;
	private int frameHeight = 480;
	private static double frameDelay = 0.05;
	private static ArrayList<MatOfPoint> contours;
	private boolean imageTracking = false;
	
	public void init() {
		Thread cameraOpThread = new Thread(new Runnable() {
			public void run() {
				cameraOperation();
			}
		});
		cameraOpThread.setName("CamThread");
		cameraOpThread.start();
	}

	protected void cameraOperation() {
		frameNumber = 0;
		
		GripPipeline gripProcessor = new GripPipeline();
		
		boolean targetFound = false;
		int numContours = 0;
		int xCenter = 0;
		double xAngle = 0;
		int yCenter = 0;
		double yAngle = 0;
		MatOfPoint contour;
		Rect box;

		mainCam = CameraServer.getInstance().startAutomaticCapture("main", 0);// sets up camera
		mainCam.setResolution(frameWidth, frameHeight);
		mainCam.setFPS(30);
		mainCam.setExposureHoldCurrent();
		mainCam.setExposureManual(10);

		main = CameraServer.getInstance().getVideo(mainCam);// sets video output

		CvSource outputStream = CameraServer.getInstance().putVideo("DispWin", frameWidth, frameHeight);

		Mat camMat = new Mat();

		while (!Thread.interrupted()) {
			if (!imageTracking) {
				outputStream.putFrame(camMat);
			} else {
				System.out.println("Camera is now vision tracking!");
				targetFound = false;
				gripProcessor.process(camMat);
				contours = gripProcessor.filterContoursOutput();
				numContours = contours.size();
				if (numContours > 1) {
					numContours = 1;
				}
				xCenter = yCenter = 0;
				if (numContours > 0) {
					for (int i = 0; i < numContours; i++) {
						contour = contours.get(i);
						box = Imgproc.boundingRect(contour);
						xCenter += box.x + (box.width / 2);
						yCenter += box.y + (box.height / 2);
					}
					xCenter /= numContours;
					yCenter /= numContours;
					
					xAngle = ((xCenter - (frameWidth / 2)) / (frameWidth / 2)) * CamData.horizontalFOV;
					yAngle = ((yCenter - (frameWidth / 2)) / (frameWidth / 2)) * CamData.verticalFOV;
					
					Imgproc.circle(camMat, new Point(xCenter, yCenter), 15, new Scalar(235, 55, 15), 2);
					targetFound = true;
				}
			}
			Timer.delay(frameDelay);
		}
	}
	public void startTracking() {
		imageTracking = true;
	}
	public void stopTracking() {
		imageTracking = false;
	}

}
