package ie.icecreamsamwich.pirecogniser;

import static com.googlecode.javacv.cpp.opencv_highgui.*;
import static com.googlecode.javacv.cpp.opencv_core.*;
import static com.googlecode.javacv.cpp.opencv_imgproc.*;
import static com.googlecode.javacv.cpp.opencv_contrib.*;
import java.io.File;
import java.io.FilenameFilter;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/*
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
 */


public class FaceRecognize {
	/*
	 * hashmap holds the unique names, of people
	 * names are associated with integers stored in labels array
	 */
	private Map<Integer, String> names;

	// This filters out all other files that don't end in '.jpg' when applied to a list of files in a directory
	private static FilenameFilter jpgFilter = new FilenameFilter() {
		public boolean accept(File dir, String name) {
			return name.toLowerCase().endsWith(".jpg");
		}
	};

	private FaceRecognizer faceRecognizer;
    // 'grayImg' is a grayscaled version of 'img' which is a jpg
	// from the 'Database' directory
	private IplImage img;

    public FaceRecognize() {
		/*System.out.println("FaceRecognize starting...");
		faceRecognizer = createLBPHFaceRecognizer();
		names = Collections.synchronizedMap(new HashMap<Integer, String>(5));
		names.put(1, "Sam");
		names.put(2, "Jakub");
		names.put(3, "Neil");
		names.put(4, "Stuart");
		names.put(5, "Jess");
		names.put(6, "Margo");*/
	}



    public FaceRecognizer train() {
		System.out.println("Training model....");
		// This takes the 'Database' directory and takes all the images that
		// end in '.jpg' in this directory and adds them to a File Array.
		File root = new File(Constants.FACES_FOLDER);
		File[] trainingImageFiles = root.listFiles(jpgFilter);

		// When the images are taken out of the 'Database' directory
		// they are grayscaled and added to this 'MatVector'
        MatVector images = new MatVector(trainingImageFiles.length);

		// Each image has a label which corresponds to 1 individual
		// Each individual will have a number of images 
        int[] labels = new int[trainingImageFiles.length];

		int counter = 0;
		int label;

		// This for loop takes each jpg in the 'Database' directory
		// changes it to gray and adds it to the MatVector
		// The label is also taken from the jpg's filename and added
		// to the labels array.
		for (File image : trainingImageFiles) {
			img = cvLoadImage(image.getAbsolutePath());
			label = Integer.parseInt(image.getName().split("\\-")[0]);
            IplImage grayImg = IplImage.create(img.width(), img.height(), IPL_DEPTH_8U, 1);
			cvCvtColor(img, grayImg, CV_BGR2GRAY);
			images.put(counter, grayImg);
			labels[counter] = label;
			counter++;
		}
		faceRecognizer.train(images, labels);
		System.out.println("Model trained, saving data");
		this.saveTrainingData();
		System.out.println("Trained Model data saved");
		return faceRecognizer;
	}

	public ImageDetails recognize() {
		// Load images to be recognized
		File imageRoot = new File(Constants.FACES_FOLDER);
		File[] imageFiles = imageRoot.listFiles(jpgFilter);
		ImageDetails detailsObject = null;
		for (File image : imageFiles) {
			img = cvLoadImage(image.getAbsolutePath());
			IplImage greyTestImage = IplImage.create(img.width(), img.height(), IPL_DEPTH_8U, 1);
			cvCvtColor(img, greyTestImage, CV_BGR2GRAY);
			int predictedLabel = faceRecognizer.predict(greyTestImage);
			if (names.containsKey(predictedLabel)) {
				String name = names.get(predictedLabel);
				System.out.println("*********\nImage Name: " + image.getName() + "\nName: " + name + "\nPredicted label: " + predictedLabel);

				detailsObject = new ImageDetails(image.getName(), name, predictedLabel);
			}
		}
		return detailsObject;
	}

	public void saveTrainingData(){
		faceRecognizer.save(Constants.TRAINED_MODEL);
		/*try {
			ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(TRAINED_MODEL));
			os.writeObject(names);
			os.close();
			os.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
	}

	public void loadTrainingData(){
		faceRecognizer.load(Constants.TRAINED_MODEL);
		/*try {
			ObjectInputStream is = new ObjectInputStream(new FileInputStream(TRAINED_MODEL));
			names = (Map<Integer, String>) is.readObject();
			is.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
	}
}
