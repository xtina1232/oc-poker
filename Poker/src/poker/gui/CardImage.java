package poker.gui;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public class CardImage {
	
	private final static String IMAGEPATH = "pictures\\";
	
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
			// return allready exisiting image
			result = imageMap.get(cardString);
		} else {
			// store image and than return it
			try {
				String path = String.format("%s%s.jpg",IMAGEPATH,cardString);
				BufferedImage image = ImageIO.read(new File(path));
				ImageIcon icon = new ImageIcon(image);
				imageMap.put(cardString, icon);
				result = icon;
			} catch (IOException e) {
				throw new FileNotFoundException(String.format("Das Bild %s konnte nicht geöffnet werden.",cardString));
			}
		}
		
		return result;
	}
}
