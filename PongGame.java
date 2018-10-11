import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.awt.TextField;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.Ellipse2D;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.Scanner;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.Timer;

public class PongGame extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 582329078736129491L;
	private int leftScore;
	private int rightScore;
	private boolean rightHit = false;
	private boolean leftHit = false;
	private boolean bottomHit = false;
	private boolean topHit = false;
	private int ballHits = 1;
	private boolean end = false;
	private boolean paused = true;
	private boolean comuterPlayer = false;
	private JLabel win = new JLabel();
	private volatile static boolean TCPMoveUp = false;
	private volatile static  boolean TCPMoveDown = false;
	private static ServerSocket welcomeSocket;

	public PongGame(boolean computerPlayer) {
		try {
			welcomeSocket = new ServerSocket(6789);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.comuterPlayer = computerPlayer;
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JLabel leftScoreLabel;
		JLabel rightScoreLabel;
		// int leftScore = 0;
		// int rightScore = 0;
		class Bat extends JPanel {
			private int y;

			public Bat(Color c, int x) {
				this.setPreferredSize(new Dimension(40, 200));
				this.setBackground(c);
				y = 400;
				this.setBounds(x, y, 40, 200);
			}

			public void setY(int y) {
				this.y = y;
			}

			public int getY() {
				return y;
			}
		}
		class Ball extends JPanel {
			/**
			 * 
			 */
			private static final long serialVersionUID = -6141493276035461189L;
			private int x;
			private int y;
			private Color c;
			private double yInc;
			private double xInc;
			private int yDir;
			private int xDir;

			public Ball(Color c, int x, int y) {
				this.setPreferredSize(new Dimension(50, 50));
				this.c = c;
				this.x = x;
				this.y = y;
				this.setBounds(x, y, 50, 50);
				this.setBackground(new Color(0, 0, 0));
				yInc = 0;
				xInc = 0;
				yDir = 0;
				xDir = 0;
				// this.setComponentZOrder(PongGame.this, 0);
			}

			public void startGame() {
				yInc = (int) (Math.random() * 5) + 5;
				xInc = (int) (Math.random() * 5) + 5;
				yDir = (int) (Math.random() * 2) == 0 ? -1 : 1;
				xDir = (int) (Math.random() * 2) == 0 ? -1 : 1;
			}

			public void update() {
				setX((int) (getX() + xInc * xDir));
				setY((int) (getY() + yInc * yDir));
				repaint();
			}

			public void set(int x, int y) {
				setX(x);
				setY(y);
				yInc = 0;
				xInc = 0;
				yDir = 0;
				xDir = 0;
			}

			public int getX() {
				return x;
			}

			public void setX(int x) {
				this.x = x;
			}

			public double getyInc() {
				return yInc;
			}

			public void setyInc(double yInc) {
				this.yInc = yInc;
			}

			public double getxInc() {
				return xInc;
			}

			public void setxInc(double xInc) {
				this.xInc = xInc;
			}

			public int getyDir() {
				return yDir;
			}

			public void setyDir(int yDir) {
				this.yDir = yDir;
			}

			public int getxDir() {
				return xDir;
			}

			public void setxDir(int xDir) {
				this.xDir = xDir;
			}

			public int getY() {
				return y;
			}

			public void setY(int y) {
				this.y = y;
			}

			public void paint(Graphics g) {
				super.paint(g);
				Graphics2D g2d = (Graphics2D) g;
				g2d.setColor(new Color(0, 255, 0));
				g2d.fill(new Ellipse2D.Double(0, 0, 50, 50));
				// super.paintComponent(g);
				// g2d.drawRect(50, 50, 50, 50);
				// g2d.fillRect(x, y, 40, 40);
			}
		}
		;
		Bat left = new Bat(new Color(255, 0, 0), 300);
		Bat right = new Bat(new Color(0, 0, 255), 1550);
		Ball ball = new Ball(new Color(0, 255, 0), Toolkit.getDefaultToolkit().getScreenSize().width / 2,
				Toolkit.getDefaultToolkit().getScreenSize().height / 2);
		// Ellipse ball = new Ellipse();
		// ball.changeDimension(200, 400, 50, 50);
		leftScore = 0;
		rightScore = 0;
		leftScoreLabel = new JLabel(String.valueOf(leftScore));
		rightScoreLabel = new JLabel(String.valueOf(rightScore));
		this.setContentPane(new JPanel() {

			/**
			 * 
			 */
			private static final long serialVersionUID = 301949020172392026L;
			{
				this.setPreferredSize((Toolkit.getDefaultToolkit().getScreenSize()));
				this.setBackground(new Color(0, 0, 0));
				this.setLayout(null);
				Font f = new Font("serif", Font.PLAIN, 100);
				leftScoreLabel.setFont(f);
				leftScoreLabel.setForeground(new Color(255, 255, 255));
				leftScoreLabel.setBounds(500, 0, 100, 100);
				rightScoreLabel.setFont(f);
				rightScoreLabel.setForeground(new Color(255, 255, 255));
				rightScoreLabel.setBounds(1400, 0, 100, 100);
				this.add(leftScoreLabel);
				this.add(rightScoreLabel);
				this.add(left);
				this.add(right);
				this.add(ball);
			}
		});

		class Listener implements KeyListener {
			private boolean UP;
			private boolean DOWN;
			private boolean U;
			private boolean S;

			@Override
			public void keyPressed(KeyEvent arg0) {
				if (arg0.getKeyCode() == KeyEvent.VK_UP) {
					// ball.setY(ball.getY() - 10);
					UP = true;
				} else if (arg0.getKeyCode() == KeyEvent.VK_DOWN) {
					DOWN = true;
				} else if (arg0.getKeyCode() == KeyEvent.VK_W) {
					U = true;
				} else if (arg0.getKeyCode() == KeyEvent.VK_S) {
					S = true;
				}
				if (end && arg0.getKeyChar() == KeyEvent.VK_SPACE) {
					paused = false;
					end = false;
					ball.startGame();
					rightScore = 0;
					leftScore = 0;
					PongGame.this.remove(win);
				}
				if (paused && arg0.getKeyCode() == KeyEvent.VK_SPACE) {
					ball.startGame();
					paused = false;
				}
			}

			@Override
			public void keyReleased(KeyEvent arg0) {
				if (arg0.getKeyCode() == KeyEvent.VK_UP) {
					UP = false;
				} else if (arg0.getKeyCode() == KeyEvent.VK_DOWN) {
					DOWN = false;
				} else if (arg0.getKeyCode() == KeyEvent.VK_W) {
					U = false;
				} else if (arg0.getKeyCode() == KeyEvent.VK_S) {
					S = false;
				}

			}

			@Override
			public void keyTyped(KeyEvent arg0) {
				// TODO Auto-generated method stub

			}

		}
		Listener listener = new Listener();
		this.addKeyListener(listener);
		Timer timer = new Timer(1000 / 60, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {

				if (listener.UP)
					right.setY(right.getY() - 15);
				else if (listener.DOWN)
					right.setY(right.getY() + 15);
				// if(ball.getY() < right.getY() + 100){
				// right.setY(ball.getY() - 100);
				// }
				// else if(ball.getY() > right.getY() + 100){
				// right.setY(ball.getY() - 100);
				// }
				if (computerPlayer) {
					if (ball.getY() < left.getY() + 100) {
						left.setY(left.getY() - 20);
						//TCPMoveUp = false;
					} else if (ball.getY() > left.getY() + 100) {
						left.setY(left.getY() + 20);
						//TCPMoveDown = false;
					}
				} else {
					if (listener.U) {
						left.setY(left.getY() - 15);
					} else if (listener.S) {
						left.setY(left.getY() + 15);
					}
				}
				// if (/*ball.getY() < left.getY() +
				// 100*/listener.U)
				// left.setY(left.getY() - 15);
				// else if (/*ball.getY() > left.getY() + 100*/listener.S)
				// left.setY(left.getY() + 15);
				if (!rightHit && ball.getX() + 50 > right.getX() && ball.getX() + 50 < right.getX() + 40
						&& ball.getY() < right.getY() + 200 && ball.getY() > right.getY() - 40) {
					ball.setxDir(-ball.getxDir());
					rightHit = true;
					leftHit = false;
					ballHits++;
				} else if (!leftHit && ball.getX() - 40 < left.getX() && ball.getX() - 40 > left.getX() - 40
						&& ball.getY() < left.getY() + 200 && ball.getY() > left.getY() - 40) {
					ball.setxDir(-ball.getxDir());
					rightHit = false;
					leftHit = true;
					ballHits++;
				} else if (!topHit && ball.getY() - 50 < 0) {
					ball.setyDir(-ball.getyDir());
					topHit = true;
					bottomHit = false;
				} else if (!bottomHit
						&& ball.getY() - 50 > Toolkit.getDefaultToolkit().getScreenSize().getHeight() - 170) {
					ball.setyDir(-ball.getyDir());
					bottomHit = true;
					topHit = false;
				} else if (ball.getX() < 0) {
					rightScore++;
					ball.set(Toolkit.getDefaultToolkit().getScreenSize().width / 2,
							Toolkit.getDefaultToolkit().getScreenSize().height / 2);
					ballHits = 1;
					rightHit = false;
					leftHit = false;
					bottomHit = false;
					topHit = false;
					ball.startGame();
				} else if (ball.getX() > Toolkit.getDefaultToolkit().getScreenSize().getWidth()) {
					leftScore++;
					ball.set(Toolkit.getDefaultToolkit().getScreenSize().width / 2,
							Toolkit.getDefaultToolkit().getScreenSize().height / 2);
					ballHits = 1;
					rightHit = false;
					leftHit = false;
					bottomHit = false;
					topHit = false;
					ball.startGame();
				}
				ball.setxInc(ball.getxInc() + .02);
				ball.setyInc(ball.getyInc() + .02);

				if (leftScore == 7) {
					end = true;
					ball.set(Toolkit.getDefaultToolkit().getScreenSize().width / 2,
							Toolkit.getDefaultToolkit().getScreenSize().height / 2);
					Font f = new Font("serif", Font.PLAIN, 100);
					win.setFont(f);
					win.setForeground(new Color(255, 255, 255));
					int inc = computerPlayer ? 400 : 200;
					win.setBounds(Toolkit.getDefaultToolkit().getScreenSize().width / 2 - inc,
							Toolkit.getDefaultToolkit().getScreenSize().height / 2 + 100, 1000, 100);
					String label = computerPlayer ? "Computer Wins!!!" : "Red Wins!!!";
					win.setText(label);
					PongGame.this.add(win);
					paused = true;
				} else if (rightScore == 7) {
					end = true;
					ball.set(Toolkit.getDefaultToolkit().getScreenSize().width / 2,
							Toolkit.getDefaultToolkit().getScreenSize().height / 2);
					Font f = new Font("serif", Font.PLAIN, 100);
					win.setFont(f);
					win.setForeground(new Color(255, 255, 255));
					win.setBounds(Toolkit.getDefaultToolkit().getScreenSize().width / 2 - 200,
							Toolkit.getDefaultToolkit().getScreenSize().height / 2 + 100, 600, 100);
					win.setText("Blue Wins!!!");
					PongGame.this.add(win);
					paused = true;
				}
				leftScoreLabel.setText(String.valueOf(leftScore));
				rightScoreLabel.setText(String.valueOf(rightScore));
				// ball.changeDimension(200, 400, 50, 50);
				ball.update();
				repaint();
			}

		});
		timer.start();
		this.pack();
		this.setVisible(true);
		this.setExtendedState(this.getExtendedState() | JFrame.MAXIMIZED_BOTH);
	}

	static class Init extends JFrame {
		public static boolean player;

		public Init() {
			this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			this.setPreferredSize(new Dimension(600, 400));
			this.setTitle("PongGame Init");
			this.setBounds(Toolkit.getDefaultToolkit().getScreenSize().width / 2 - 300,
					Toolkit.getDefaultToolkit().getScreenSize().height / 2 - 200, 600, 400);
			JRadioButton computer = new JRadioButton("Play Against Computer");
			computer.setSelected(true);
			JRadioButton playerButton = new JRadioButton("Play Against Player");
			JRadioButton TCPButton = new JRadioButton("Play Over TCP");
			ButtonGroup group = new ButtonGroup();
			group.add(computer);
			group.add(playerButton);
			group.add(TCPButton);
			JButton submit = new JButton("Play");
			submit.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					if (TCPButton.isSelected()) {
						Init.this.dispose();
						new Init.TCPSetup();
					} else {
						player = playerButton.isSelected();
						Init.this.dispose();
						new PongGame(!player);
					}
				}
			});
			JPanel panel = new JPanel();
			panel.add(computer);
			panel.add(playerButton);
			panel.add(TCPButton);
			panel.add(submit);
			this.add(panel);
			this.setVisible(true);
		}

		static class TCPSetup extends JFrame {
			private String getIPAddress() throws java.io.IOException {
				Scanner in = new Scanner(Runtime.getRuntime().exec("ipconfig").getInputStream()).useDelimiter("\\A");
				String command = in.next();
				String[] lines = command.split("\n");
				boolean found = false;
				for(String s : lines){
					if(s.contains("Wi-Fi")){
						found = true;
					}
					if(found && s.contains("IPv4")){
						return(s.substring(s.indexOf(':')+2));
					}
				}
		        return "";
		    }
			public TCPSetup() {
				this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				this.setPreferredSize(new Dimension(600, 400));
				this.setTitle("TCP Game Setup");
				this.setBounds(Toolkit.getDefaultToolkit().getScreenSize().width / 2 - 300,
						Toolkit.getDefaultToolkit().getScreenSize().height / 2 - 200, 600, 400);
				JRadioButton host = new JRadioButton("Play as Host");
				host.setSelected(true);
				JRadioButton guest = new JRadioButton("Play as Guest");
				TextField hostIPAddress = new TextField(50);
				hostIPAddress.setEditable(false);
				JButton play = new JButton("Play");
				JLabel IPaddress = new JLabel();
				
				try {
					IPaddress.setText("Your IP Address:" + this.getIPAddress());
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				ButtonGroup group = new ButtonGroup();
				group.add(host);
				group.add(guest);
				guest.addActionListener(new ActionListener(){

					@Override
					public void actionPerformed(ActionEvent e) {
						if(guest.isSelected()){
							hostIPAddress.setEditable(true);
						}
					}
					
				});
				play.addActionListener(new ActionListener(){

					@Override
					public void actionPerformed(ActionEvent arg0) {
						if(host.isSelected()){
							new Thread(() -> {
								while (true) {
									Socket connectionSocket = null;
									try {
										connectionSocket = welcomeSocket.accept();
									} catch (IOException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
									BufferedReader inFromClient = null;
									try {
										inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
									} catch (IOException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
									// DataOutputStream outToClient = new
									// DataOutputStream(connectionSocket.getOutputStream());
									String s = null;
									try {
										s = inFromClient.readLine();
									} catch (IOException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
									if (s.equals("w")) {
										TCPMoveUp = true;
										TCPMoveDown = false;
									} else if (s.equals("s")) {
										TCPMoveDown = true;
										TCPMoveUp = false;
									} else {
										TCPMoveDown = false;
										TCPMoveUp = false;
									}
								}

							}).start();
						}
						else if(guest.isSelected())
						{
							
						}
					}
					
				});
				JPanel jpanel = new JPanel();
				jpanel.setLayout(new GridLayout(0,1));
				jpanel.add(host);
				jpanel.add(guest);
				jpanel.add(hostIPAddress);
				jpanel.add(IPaddress);
				this.add(jpanel);
				this.setVisible(true);
			}

		}
	}

	public static void main(String[] args) {
		new PongGame.Init();
	}

}
