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

public class MyCamera{
	private static GripPipeline gripProcessor;
    private static UsbCamera camServer;
	private static CvSink camSink;
	private static CvSource liveFeed;
	private static Mat mat;
	private static int frameNumber = 0;
	private static int frameWidth = 640;
	private static int frameHeight = 480;
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

    protected static void cameraOperation() {
		frameNumber = 0;
		camServer = CameraServer.getInstance().startAutomaticCapture("cam",0);
		camServer.setResolution(640,480);
		gripProcessor = new GripPipeline();
		camSink = CameraServer.getInstance().getVideo(camServer);
		liveFeed = CameraServer.getInstance().putVideo("Live", frameWidth, frameHeight);
		camServer.setBrightness(10);
		camServer.setExposureManual(10);
		mat = new Mat();

		while (!Thread.interrupted()) {
			if (camSink.grabFrame(mat) == 0) {
				liveFeed.notifyError(camSink.getError());
			} else {
				//liveFeed.putFrame(mat);
				SmartDashboard.putNumber("Frame#:",++frameNumber);
			}
			if (!imageTracking) {
				liveFeed.putFrame(mat); 
			} else {
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