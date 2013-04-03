import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;

import com.googlecode.javacv.cpp.*;
import com.googlecode.javacpp.Loader;
import static com.googlecode.javacv.cpp.opencv_core.*;
import static com.googlecode.javacv.cpp.opencv_imgproc.*;
import static com.googlecode.javacv.cpp.opencv_highgui.*;
import static com.googlecode.javacv.cpp.opencv_objdetect.*;

//
// Detects faces in an image, draws boxes around them, and writes the results
// to "faceDetection.png".
//

class DetectFaceDemo {

	private static final int SCALE = 2;
	private static final String CASCADE_FILE = "/Users/samoleary/Documents/opencv/data/haarcascades/haarcascade_frontalface_alt_tree.xml";
	private static final String OUT_FILE = "/Users/samoleary/Documents/Images/FaceRectangleImages/";
	//private static final String INPUT_FILE = "/Users/samoleary/Documents/workspace/JavaCV/src/resources/wholeGROUP.jpg";
	private static final String FACES_FOLDER = "/Users/samoleary/Documents/Images/OutputFaceImages/";
	private static final String PHOTO_DIR = "/Users/samoleary/Documents/Images/InputImages/";

	// List of extracted faces
	private	static ArrayList<IplImage> FACE_LIST = new ArrayList<IplImage>();
	private	static ArrayList<CvRect> UNTREATED_FACE_LIST = new ArrayList<CvRect>();

	public void run() {
		System.out.println("\nRunning DetectFaceDemo");

		int photoCount = 1;


		// preload the opencv_objdetect module to work around a known bug
		Loader.load(opencv_objdetect.class);

		// load an image
		File root = new File(PHOTO_DIR);

		FilenameFilter jpgFilter = new FilenameFilter() {
			public boolean accept(File dir, String name) {
				return name.toLowerCase().endsWith(".jpg");
			}
		};

		File[] imageFiles = root.listFiles(jpgFilter);

		for (File image : imageFiles) {
			IplImage imageIn = cvLoadImage(image.getAbsolutePath());

			// convert to grayscale
			IplImage grayImage = IplImage.create(imageIn.width(), imageIn.height(), IPL_DEPTH_8U, 1);
			cvCvtColor(imageIn, grayImage, CV_BGR2GRAY);

			// scale the grayscale (to speed up face detection)
			IplImage smallImage = IplImage.create(grayImage.width() /SCALE, grayImage.height() /SCALE, IPL_DEPTH_8U, 1);
			cvResize(grayImage, smallImage, CV_INTER_LINEAR);

			// equalize the small grayscale
			IplImage equImage = IplImage.create(smallImage.width(), smallImage.height(), IPL_DEPTH_8U, 1);
			cvEqualizeHist(smallImage, equImage);

			// create temp storage, used during object detection
			CvMemStorage storage = CvMemStorage.create();

			// Create a face detector from the cascade file in the resources
			// directory.
			CvHaarClassifierCascade cascade = new CvHaarClassifierCascade(cvLoad(CASCADE_FILE));

			System.out.println("Detecting faces...");

			CvSeq faces = cvHaarDetectObjects(equImage, cascade, storage, 1.1, 3, CV_HAAR_DO_CANNY_PRUNING);

			cvClearMemStorage(storage);

			// Draw a bounding box around each face.
			int total = faces.total();
			System.out.println(String.format("Detected %s faces", total));
			for (int i = 0; i < total; i++) {
				CvRect r = new CvRect(cvGetSeqElem(faces, i));
				cvRectangle(imageIn, cvPoint( r.x() *SCALE, r.y() *SCALE), cvPoint( (r.x() + r.width()) *SCALE, (r.y() + r.height()) *SCALE), CvScalar.YELLOW, 6, CV_AA, 0);
				// undo image scaling when calculating rect coordinates
				CvRect x = new CvRect(r.x() *SCALE, r.y() *SCALE, r.width() *SCALE, r.height() *SCALE);
				UNTREATED_FACE_LIST.add(x);
			}

			for (CvRect y : UNTREATED_FACE_LIST) {
				// Add each extracted face to the list
				cvSetImageROI(imageIn, y);
				IplImage temp = cvCreateImage(cvGetSize(imageIn), imageIn.depth(), imageIn.nChannels());
				cvCopy(imageIn, temp, null);
				cvResetImageROI(imageIn);

				IplImage grayImg = IplImage.create(temp.width(), temp.height(), IPL_DEPTH_8U, 1);

				IplImage tempImg = IplImage.create(200,  200, IPL_DEPTH_8U, 1);
				IplImage normalisedImg = IplImage.create(200,  200, IPL_DEPTH_8U, 1);

				cvCvtColor(temp, grayImg, CV_BGR2GRAY);
				cvResize(grayImg, tempImg, CV_INTER_AREA);
				cvEqualizeHist(tempImg, normalisedImg);
				FACE_LIST.add(normalisedImg);
			}

			if (total > 0) {
				// Save the visualized detection.
				//System.out.println(String.format("Writing %s", OUT_FILE));
				String photoPath = OUT_FILE + photoCount + ".jpg";
				cvSaveImage(photoPath, imageIn);
				photoCount++;
				int count = 1;
				for (IplImage img : FACE_LIST) {
					String path = FACES_FOLDER + count + ".jpg";
					cvSaveImage(path, img);
					count++;
				}
			}
		}

	}
}

public class FaceDetect {
	public static void main(String[] args) {
		System.out.println("Hello, OpenCV");

		new DetectFaceDemo().run();
		FaceRecognize myRecog = new FaceRecognize();
		myRecog.loadTrainingData();
		myRecog.recognize();
	}
}
