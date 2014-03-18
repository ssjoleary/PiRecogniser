package ie.icecreamsamwich.pirecogniser;

import com.googlecode.javacv.cpp.opencv_core;

import java.util.ArrayList;

/**
 * Created by ssjoleary on 18/03/2014.
 */
public class Constants {
    public static final int SCALE = 2;
    public static final String CASCADE_FILE = "/Users/ssjoleary/Documents/workspace/PiRecogniser/res/data/haarcascades/haarcascade_frontalface_alt_tree.xml";
    public static final String OUT_FILE = "/Users/ssjoleary/Documents/workspace/PiRecogniser/res/FaceRectangleImages/";
  // Directory where the image(s) to be recognized are taken from
    public static final String FACES_FOLDER = "/Users/ssjoleary/Documents/workspace/PiRecogniser/res/OutputFaceImages/";
    public static final String PHOTO_DIR = "/Users/ssjoleary/Documents/workspace/PiRecogniser/res/InputImages/";

    // List of extracted faces
    public static ArrayList<opencv_core.IplImage> FACE_LIST = new ArrayList<opencv_core.IplImage>();
    public static ArrayList<opencv_core.CvRect> UNTREATED_FACE_LIST = new ArrayList<opencv_core.CvRect>();

    // 'Database' of images in the format <label>-<firstname>_<lastname>_<number>.jpg
    public static final String trainingDir = "/Users/samoleary/Documents/Images/FaceImages/";
    public static final String TRAINED_MODEL = "/Users/samoleary/Documents/Images/Model/TRModel.xml";
}
