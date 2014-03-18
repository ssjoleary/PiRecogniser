package ie.icecreamsamwich.pirecogniser;

public class ImageDetails {
	private String imageName;
	private String personName;
	private int label;
	private String[] details = new String[3];
	
	public ImageDetails() {
		imageName = "Image Name Not Set";
		personName = "Person Name Not Set";
		label = 0;
	}
	
	public ImageDetails(String image, String person, int labelIn) {
		imageName = image;
		personName = person;
		label = labelIn;
	}
	
	public String toString() {
        return "The name of the image is " + imageName + ", the person identified is " + personName + " and their label is " + Integer.toString(label);
	}
	
	public String[] getDetails() {
		details[0] = imageName;
		details[1] = personName;
		details[2] = Integer.toString(label);
		return details;
	}
	
	public String getPersonName() {
		return personName;
	}
	
	public String getImageName() {
		return imageName;
	}
	
	public int getLabel() {
		return label;
	}
}
