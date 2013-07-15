package poker.gui;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public class CardImage {
	
	private final static File IMAGEDIR = new File("pictures");
	
	private HashMap<String, ImageIcon> imageMap = new HashMap<String, ImageIcon>();
	
	private static CardImage INSTANCE;
	
	private CardImage() {
		
	}
	
	public static CardImage getInstance() {
		if(INSTANCE == null) {
			INSTANCE = new CardImage();
		}
		return INSTANCE;
	}
	
	public ImageIcon getCardImage(String cardString) throws FileNotFoundException {
		ImageIcon result = null;
		if(imageMap.containsKey(cardString)) {
			// return already existing image
			result = imageMap.get(cardString);
		} else {
			// store image and then return it
			File imageFile = new File(IMAGEDIR, String.format("%s.jpg",cardString));

			try {
				BufferedImage image = ImageIO.read(imageFile);
				ImageIcon icon = new ImageIcon(image);
				imageMap.put(cardString, icon);
				result = icon;
			} catch (IOException e) {
				throw new FileNotFoundException(String.format("Das Bild %s konnte nicht geöffnet werden.",imageFile.getPath()));
			}
		}
		
		return result;
	}
}
