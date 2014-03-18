package ie.icecreamsamwich.pirecogniser;

public class PiMain {
	public static void main(String[] args) {
		System.out.println("Hello, OpenCV");

		ImageDetails details;
		
		new FaceDetect().run();
		FaceRecognize myRecog = new FaceRecognize();
        myRecog.train();
		/*myRecog.loadTrainingData();
		details = myRecog.recognize();
		
		Speaker kevin = new Speaker();
		kevin.initialize();
		kevin.speak("Eureka! The Person Identified is: " + details.getPersonName());*/
	}
}
