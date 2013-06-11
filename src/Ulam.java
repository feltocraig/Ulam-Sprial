import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.BitSet;

import javax.imageio.ImageIO;

/**
 * Creates a specific sized image of an Ulam Spiral.
 * 
 * Larger sized Ulam Spirals will take long to process. A more efficient
 * primality check or multi-threaded approach would be recommended.
 * 
 * @author feltocraig
 */
public class Ulam {

	private BufferedImage img;
	private int[][] layout;
	private int size;
	private BitSet bset;

	/**
	 * Defualt Constructor - Sets dimensions of Ulam Spiral
	 * 
	 * @param size
	 *            Width/Height of image in pixels [1 < size < 46,340]
	 */
	public Ulam(int size) {
		if ((long) size * (long) size > Integer.MAX_VALUE)
			System.err.println("Size calculated is larger than " + Integer.MAX_VALUE);

		img = new BufferedImage(size, size, BufferedImage.TYPE_INT_RGB);
		layout = new int[size][size];
		this.size = size;
	}

	/**
	 * Get a Ulam Sprial BufferedImage
	 * 
	 * @return BufferedImage with height & width = size
	 */
	public BufferedImage getSpiral() {
		int n = size * size;
		int x = 0, y = 0;
		if (size % 2 != 0)
			y = x = size - 1;

		bset = getAllPrimesBelow(n);

		while (n != 1) {
			while (x > 0 && layout[x - 1][y] < n) {
				setImg(x--, y, n--);
			}
			while (y > 0 && layout[x][y - 1] < n) {
				setImg(x, y--, n--);
			}
			while ((x + 1) < layout.length && layout[x + 1][y] < n) {
				setImg(x++, y, n--);
			}
			while ((y + 1) < layout[0].length && layout[x][y + 1] < n) {
				setImg(x, y++, n--);
			}
		}

		return img;
	}

	private void setImg(int x, int y, int n) {
		layout[x][y] = n;
		if (bset.get(n))
			img.setRGB(x, y, -16777216);
		else
			img.setRGB(x, y, -1);
	}

	/**
	 * Get all prime numbers below n
	 * using an optimized Sieve of Eratosthenes
	 * @param n
	 *            the number to get the primes below
	 * @return an array list of all the prime number below n
	 */
	public static BitSet getAllPrimesBelow(int n) {
		if (n <= 2) {
			return new BitSet();
		}
		boolean[] is_composite = new boolean[(n) >> 1];
		BitSet results = new BitSet((int) Math.ceil(1.25506 * n / Math.log(n)));
		results.set(2);
		for (int i = 1; i < is_composite.length; i++) {
			if (!is_composite[i]) {
				int cur = (i * 2) + 1;
				results.set(cur);
				for (int j = i + cur; j < is_composite.length; j += cur) {
					is_composite[j] = true;
				}
			}
		}
		return results;

	}

	/**
	 * Test Use Outputs an image of size 200
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		long duration = System.currentTimeMillis();

		Ulam ulam = new Ulam(2000);
		File outputfile = new File("ulam.png");
		try {
			ImageIO.write(ulam.getSpiral(), "png", outputfile);
		} catch (IOException e) {
			System.err.println("Couldn't create image!");
		}
		duration = System.currentTimeMillis() - duration;
		System.out.println("> " + duration + " ms");
	}
}
