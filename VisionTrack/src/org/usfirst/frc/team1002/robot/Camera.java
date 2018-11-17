package org.usfirst.frc.team1002.robot;

import org.opencv.core.Mat;

import edu.wpi.cscore.CvSink;
import edu.wpi.cscore.CvSource;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.Timer;

public class Camera {
	private UsbCamera mainCam;
	private CvSink main;
	public int frameNumber = 0;
	// private static double frameDelay = 0.05;

	public void init() {
		Thread cameraOpThread = new Thread(new Runnable() {
			public void run() {
				cameraOperation();
			}
		});
	}

	protected void cameraOperation() {
		frameNumber = 0;

		mainCam = CameraServer.getInstance().startAutomaticCapture("main", 0);// sets up camera
		mainCam.setResolution(640, 480);
		mainCam.setFPS(30);
		mainCam.setExposureHoldCurrent();
		mainCam.setExposureManual(10);

		main = CameraServer.getInstance().getVideo(mainCam);// sets video output

		CvSource outputStream = CameraServer.getInstance().putVideo("DispWin", 640, 480);

		Mat camMat = new Mat();

		while (!Thread.interrupted()) {
			outputStream.putFrame(camMat);
			Timer.delay(1 / 30);
		}
	}
}
