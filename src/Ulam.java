import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * Creates a specific sized image of an Ulam Spiral.
 * 
 * Larger sized Ulam Spirals will take long to process. A more efficient 
 * primality check or multi-threaded approach would be recommended.
 * @author feltocraig
 */
public class Ulam {
	
	private BufferedImage img;
	private int[][] layout;
	private int size;
	
	/**
	 * Defualt Constructor - Sets dimensions of Ulam Spiral
	 * @param size Width/Height of image in pixels [1 < size < 46,340]
	 */
	public Ulam(int size){
		img = new BufferedImage(size, size,BufferedImage.TYPE_INT_RGB);
		layout = new int[size][size];
		this.size = size;
	}

	/**
	 * Get a Ulam Sprial BufferedImage
	 * @return BufferedImage with height & width = size
	 */
	public BufferedImage getSpiral() {
		int n = size * size;
		int x = 0, y = 0;
		if(size % 2 != 0)
			y = x = size - 1;
				
		while(n != 1){
			while(x > 0 && layout[x - 1][y] < n){
				setImg(x--, y, n--);
			}
			while(y > 0 && layout[x][y - 1] < n){
				setImg(x, y--, n--);
			}
			while((x + 1) < layout.length && layout[x + 1][y] < n){
				setImg(x++, y, n--);
			}
			while((y + 1) < layout[0].length && layout[x][y + 1] < n){
				setImg(x, y++, n--);
			}
		}
		
		return img;
	}
	
	private void setImg(int x, int y, int n){
		layout[x][y] = n;
		if(isPrime(n))
			img.setRGB(x, y, -16777216);
		else
			img.setRGB(x, y, -1);
	}
	
    private boolean isPrime(int n){
        if(n == 2 || n == 3) return true;
        if(n % 2 == 0 || n % 3 == 0 || n < 2) return false;
        
        int sqrt = (int)Math.sqrt(n) + 1;
        
        for(int i = 6; i <= sqrt; i += 6){
        	if((n % (i - 1) == 0) || (n % (i + 1) == 0)) return false;
        }
        return true;
    }

    /**
     * Test Use
     * Outputs an image of size 200
     * @param args
     */
	public static void main(String[] args) {
		Ulam ulam = new Ulam(200);
		File outputfile = new File("ulam.png");
	    try {
			ImageIO.write(ulam.getSpiral(), "png", outputfile);
		} catch (IOException e) {
			System.err.println("Couldn't create image!");
		}
	}
}