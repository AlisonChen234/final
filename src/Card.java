import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Card {
    private String suit;
    private String value;
    private String imageFileName;
    private BufferedImage image;
    private boolean isSelected = false;
    private int xLocation;
    private int yLocation;

    public Card(String suit, String value) {
        this.suit = suit;
        this.value = value;
        this.imageFileName = "card_"+suit+"_"+value+".png";
        this.image = readImage();
    }
    public Card(){
        this.suit = "null";
        this.value = "00";
        this.imageFileName = "card_back.png";
        this.image = readImage();
    }


    public String getSuit() {
        return suit;
    }


    public String getValue() {
        return value;
    }

    public String getImageFileName() {
        return imageFileName;
    }

    public String toString() {
        return suit + " " + value;
    }


    public BufferedImage getImage() {
        return image;
    }

    public BufferedImage readImage() {
        try {
            BufferedImage image;
            image = ImageIO.read(new File("images/" + imageFileName));
            return image;
        }
        catch (IOException e) {
            System.out.println(e);
            return null;
        }
    }
    public void setHitbox(int xLocation, int yLocation){
        this.xLocation = xLocation;
        this.yLocation = yLocation;

    }
    public boolean inHitbox(double x, double y){
        boolean inHitbox = false;
        if (x >= xLocation && x<= xLocation+image.getWidth()){
            if (y >= yLocation && y<= yLocation+image.getHeight()){
                inHitbox = true;
            }
        }
        return inHitbox;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}