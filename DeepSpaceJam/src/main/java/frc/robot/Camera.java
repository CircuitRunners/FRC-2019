package frc.robot;
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
import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Camera{
	private static GripPipeline gripProcessor;
	private static UsbCamera trackingCam;
	//private static UsbCamera elevatorCam;
	private static CvSink camSink;
	private static CvSource liveFeed;
	private static Mat mat;
	private static int frameNumber = 0;
	private static int frameWidth = 80;
	private static int frameHeight = 60;
    //private static double frameDelay = 0.05;
    
    public static final double verticalFOV = 33.583;//half of the FOV (center to edge)
	public static final double horizontalFOV = 59.703;//half of the FOV (center to edge)
	public static final double diagonalFOV = 68.5;//used math to find the first two from this, this was on the LIFECAM HD-3000 product page
	
	private static ArrayList<MatOfPoint> contours;
	private static boolean imageTracking = false;
	public static boolean debug = true;
	
	public static double xAngle;
	public static double yAngle;
    
    public static void init(){
        Thread cameraOpThread = new Thread(new Runnable() {
			public void run() {
				cameraOperation();
			}
		});
		cameraOpThread.setName("CamThread");
		cameraOpThread.setDaemon(true);
		cameraOpThread.start();
	}

	static boolean targetFound = false;
	static int numContours = 0;
	static int xCenter = 0;
	static int yCenter = 0;
	static MatOfPoint contour;
	static Rect box; 
	static boolean isElevator = false;

    protected static void cameraOperation() {
		frameNumber = 0;
		trackingCam = CameraServer.getInstance().startAutomaticCapture("cam",0);
		trackingCam.setResolution(frameWidth, frameHeight);
		trackingCam.setFPS(30);

		gripProcessor = new GripPipeline();
		camSink = CameraServer.getInstance().getVideo(trackingCam);
		liveFeed = CameraServer.getInstance().putVideo("Live", frameWidth, frameHeight);
		trackingCam.setBrightness(10);
		trackingCam.setExposureManual(10);
		mat = new Mat();

		while (!Thread.interrupted()) {
			if (camSink.grabFrame(mat) == 0) {
				liveFeed.notifyError(camSink.getError());
			} else {
				//liveFeed.putFrame(mat);
				SmartDashboard.putNumber("Frame#:",++frameNumber);
			}
			if (!imageTracking) {
				trackingCam.setBrightness(50);
				trackingCam.setExposureAuto();
				//liveFeed.putFrame(mat); 
			} else {
				trackingCam.setBrightness(10);
				trackingCam.setExposureManual(10);
                targetFound = false;
                gripProcessor.process(mat);
				contours = gripProcessor.filterContoursOutput();
				numContours = contours.size();
				SmartDashboard.putNumber("NumContours", numContours);
				if (numContours > 2) {
					numContours = 2;
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
					SmartDashboard.putNumber("X Center", xCenter);
					SmartDashboard.putNumber("Y Center", yCenter);

					xAngle = ((xCenter - (frameWidth / 2)) / (frameWidth / 2)) * horizontalFOV;
					yAngle = ((yCenter - (frameHeight / 2)) / (frameHeight / 2)) * verticalFOV;
				
					SmartDashboard.putNumber("X Angle", xAngle);
					SmartDashboard.putNumber("Y Angle", yAngle);
					
					Imgproc.circle(mat, new Point(xCenter, yCenter), 15, new Scalar(235, 55, 15), 2);
					targetFound = true;
				}
			}
			Timer.delay(1/30);
		}
		if(Thread.interrupted()){
			System.out.println("MyCamera: Critical Error, camera thread terminated.");
		}
    }
    public static void startTracking() {
		imageTracking = true;
	}
	public static void stopTracking() {
		imageTracking = false;
	}
	public static boolean isTracking(){
		return imageTracking;
	}

}