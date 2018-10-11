import java.awt.Color;
import java.awt.image.BufferedImage;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

public class MathematicalGradient {

	public static void main(String[] args) {
		BufferedImage img = new BufferedImage(510, 510, BufferedImage.TYPE_INT_RGB);
		double xratio = 255.0 / img.getWidth();
		double yratio = 255.0 / img.getHeight();
		double midX = img.getWidth() / 2;
		double midY = img.getHeight() / 2;
		double radius = 255;
		int arg0 = 0;
		int arg1 = 0;
		for (int y = 1; y < img.getHeight(); y++) {
			for (int x = 1; x < img.getWidth(); x++) {
				arg0 = x / y * (y % x);
				arg1 = y / x * (x % y);
				//arg0 = x >> y << x;
				//arg1 = y >> x << y;
				// arg0 = Math.sqrt(Math.pow(x-midX, 2) + Math.pow(y - midY, 2))
				// > radius ? 255 : 0;
				// arg0 = 0;//x^y;
				// arg1 = (int)(Math.tan((y*x) / 180.0)*127+128);
				// arg1 = Math.sqrt(Math.pow(x-midX, 2) + Math.pow(y - midY, 2))
				// > radius ? 0 : 255;
				// arg0 = (int) (Math.pow(x, 2) + x) / 100;
				// arg1 = (int) (Math.pow(y, 2) + y) / 100;
				// arg0 = x;
				// arg1 = y;
				// arg0 = (x<=y)?(int)(127*Math.cos(x%y * 0.1)+y)%x:0;
				// arg1 = (x>=y)?(int)(127*Math.cos(y%x * 0.1)+x)%y:0;
				// double theta = Math.atan(1.0*y/x);
				// double r = Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
				// arg0 = (int)(r*(1.0/Math.cos(theta)))%255;
				// arg1 = (int)(r*(1.0/Math.sin(theta)))%255;
				// arg0 = y ^ (y << x);
				// arg1 = x ^ (x << y);
				//arg0 = (int) (Math.cos(x/31.4) * Math.cos(y/31.4) * 1000);
				//arg1 = (int) -(Math.cos(x/31.4) * Math.cos(y/31.4) * 1000);
				// arg0 = (int) (10*x) ^ (int) (10*y);
				// arg1 = (int) (10*y) ^ (int) (10*x);
				// arg0 = 100 + (int) (255 * (Math.sin((x * x + y * y))));
				// arg1 = 100 + (int) (255 * (Math.sin((x * x + y * y))));
				// System.out.println(arg0 + " " + arg1);
				// arg0 = (int) (500*Math.sin(2*(Math.pow(x, 2) + Math.pow(y,
				// 2))));
				// arg1 = (int) (500*Math.sin(2*(Math.pow(x, 2) + Math.pow(y,
				// 2))));
				// System.out.println(arg0 + " " + arg1);
				//arg0 = (int) (200 * (1/(Math.sin(Math.abs(x) + x) - Math.cos(Math.abs(y) + y))));
				//arg1 = (int) (200 * (1/(Math.sin(Math.abs(x) + x) - Math.cos(Math.abs(y) + y))));
				//arg0 = (int) (Math.sin(x) * Math.cos(y)); x
				//arg1 = (int) (Math.sin(x) * Math.cos(y));
				//arg0 = x ^ y;
				//arg1 = y ^ x;
				//arg0 = x;
				//arg1 = y;
				img.setRGB(x, y, new Color(limit(xratio * arg0), limit(yratio * arg1), limit(yratio * arg1)).getRGB());
			}
		}
		ImageIcon icon = new ImageIcon(img);
		JLabel label = new JLabel(icon);
		JOptionPane.showMessageDialog(null, label);
	}

	public static int limit(double x) {
		return (x < 0) ? 0 : (x > 255) ? 255 : (int) x;
	}

	public static int inLine(double d) {
		// System.out.println(d);
		return (int) d;
	}
}