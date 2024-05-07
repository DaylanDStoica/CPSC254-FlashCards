////
//// Course       : CPSC 254
//// Project      : CPSC254 Open Source Systems Project
//// Project Group: 9
////
//// Program Name : FlashCardPlayer.java
////
//// Description  : This program is intended to play the Flash Cards Created by 
////                the FlashCards program. The flashcards are stored as Deck file
////                in a text file format(.txt).
////                The program will show all the Deck files currently residing in
////                the program directory. The user can choose one Deck file and 
////                load the flashcards into the program and can view the questions
////                and answers of each flashcard. The program also allows the user
////                to view a hint to the answer. In addition, the program also helps 
////                the user to shuffle the flashcards.
////
////                This program will work in Linux and Windows operating system
////                where there is a Java runtime available. This program is compiled
////                with Java OpenJDK version 11.x. 
////
////                To compile this program in Linux: javac FlashCardPlayer.java
////                To run this program in Linux: java -cp . FlashCardPlayer
////
////                To compile this program in Windows: javac FlashCardPlayer.java
////                To run this program in Windows: java -cp .; FlashCardPlayer
////


// Add Java support files
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import java.io.*;
import java.util.ArrayList;
import java.util.*;

// filename filter for filtering file names of a directory
class FileExt implements FilenameFilter
{
	String extn;
	public FileExt(String extn)
	{
		this.extn = extn;
	}
	public boolean accept(File dir, String fileName)
	{
		return fileName.endsWith(extn);
	}
}

// Main Class file for Flash Card Player
public class FlashCardPlayer extends JFrame
{
	// To identify OS
	static String OSName = System.getProperties().getProperty("os.name");
	// To identify file separator for paths
	static String OSFs = ""; // OS File Separator
	// To get App path
	static String appPath = "";
	
	// generate random numbers for Card Shuffling
	Random rndm = new Random();

	String deckFileName = "";
	String deckPathAndFileName = "";
	
	// Define Java Swing GUI Objects
	Container cp = getContentPane();
	JLabel JLBLDeckInfo;
	JButton JBTNLoadDeck;
	DefaultListModel<String> deckModel;
	JList JLSTDecks;
	JScrollPane JSPDecks;
	JLabel JLBLClickInfo;
	JButton JBTNHint;
	JLabel JLBLHint;
	JButton JBTNCard;
	JButton JBTNLeft;
	JButton JBTNRight;
	JButton JBTNShuffle;
	
	// for storing Questions
	ArrayList<String> questionList;
	// for storing Answers
	ArrayList<String> answerList;
	// for shuffling Cards
	ArrayList<Integer> randList;

	// to track Cards
	int CardsCount = 0;
	int CurrentCard = 0;
	boolean answerCard = false;
	
	// Character to separate Questions and Answers in the Deck file
	char dataSep = '=';
	char newLine = '\n';
	
	// Method for closing the App
	public void closeApp()
	{
		int exitValue = -1;
		exitValue = JOptionPane.showConfirmDialog(null,
		"Exit Flash Card Player ?",
		"Message",
		JOptionPane.YES_NO_OPTION);
		
		if (exitValue == 0)
		{
			System.exit(0);
		}
		
		if (exitValue == 1)
		{
			return;
		}
	}
	
	//// Action Listener for closing the App
	ActionListener alCloseApp = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			closeApp();
		}
	};
	
	//// Function to load all Deck Files (*.txt) from the App directory
	public void LoadAllDecks()
	{
		// for Deck File names
		String filterExtn = "txt";
		String [] filteredFiles = {""};
		FilenameFilter ff = new FileExt(filterExtn);
		File dir = new File(appPath);
		if (dir.isDirectory()) {
			filteredFiles = dir.list(ff);
		}
		
		// clear entries
		deckModel.clear();
		
		// get file names
		String temp1 = "";
		for (int fn = 0; fn < filteredFiles.length; fn++ )
		{
			temp1 = filteredFiles[fn];
			temp1 = temp1.trim();
			if (temp1.length() > 0)
			{
				// add file name
				deckModel.addElement(temp1);
			}
		}
		
	} // public void LoadAllDecks()
	
	// Function to show the Card
	void showCardInfo()
	{
		int cardNum = 0;
		String status = "";
		if (CardsCount > 0)
		{
			status = "Deck: " + deckFileName + "    Card " + (CurrentCard + 1) + " of " + CardsCount;
			JLBLDeckInfo.setText(status);
			
			String s1 = "";
			if (answerCard)
			{
				cardNum = randList.get(CurrentCard);
				s1 = answerList.get(cardNum);
				s1 = "<html>" + s1.replaceAll("\n", "<br>") + "</html>";
				JBTNCard.setText(s1);
				// Card Background color: mistyrose RGB(255, 228, 225)
				JBTNCard.setBackground(new Color(225, 228, 225));
				answerCard = false;
				return;
			}
			else
			{
				cardNum = randList.get(CurrentCard);
				s1 = questionList.get(cardNum);
				s1 = "<html>" + s1.replaceAll("\n", "<br>") + "</html>";
				JBTNCard.setText(s1);
				// Card Background color: lightskyblue RGB(135, 206, 250)
				JBTNCard.setBackground(new Color(135, 206, 250));
				answerCard = true;
			}
		}
	} // void showCardInfo()
	
	// To open and read a Deck file
	void loadDeckFile()
	{
		int listSelection = -1;
		String data1 = "";
		
		listSelection = JLSTDecks.getSelectedIndex();
		if (listSelection >= 0)
		{
			deckFileName = deckModel.get(listSelection);
			deckFileName = deckFileName.trim();
		}
		
		if (deckFileName.length() <= 0)
		{
			JOptionPane.showMessageDialog(null,
			"Select a Deck File",
			"Message",
			JOptionPane.ERROR_MESSAGE);
			return;
		}
		else
		{
			// set absolute path and file name (Store the text file in the App directory)
			deckPathAndFileName = appPath + OSFs + deckFileName;
		}
		
		try
		{
			File inputFile = new File(deckPathAndFileName);
			
			if (inputFile.exists()) {
				FileInputStream in = new FileInputStream(inputFile);
				byte bt[] = new byte[(int)inputFile.length()];
				in.read(bt);
				data1 = new String(bt);
				in.close();
			}
			else
			{
				JOptionPane.showMessageDialog(null,
				"File " + deckFileName +
				" Not found.",
				"Message",
				JOptionPane.ERROR_MESSAGE);
				return;
			}
		}
		catch(java.io.IOException e1)
		{
			JOptionPane.showMessageDialog(null,
			"Failed to open and read file " + deckFileName +
			e1.toString(),
			"Message",
			JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		// if file found and read, load data
		String listEntry = "";
		int overWrite = -1;
		int dataSize = data1.length();
		
		if (dataSize > 0)
		{
			overWrite = JOptionPane.showConfirmDialog(null,
			"Load File " + deckFileName + " contents ?",
			"Message",
			JOptionPane.YES_NO_OPTION);
			
			// Do not load
			if (overWrite == 1)
			{
				return;
			}
			
			// clear existing values from components
			questionList.clear();
			answerList.clear();
			randList.clear();
			CardsCount = 0;
			CurrentCard = 0;
			
			// load data into components
			String ans = "";
			String qes = "";
			char c1 = 0;
			boolean dataFlag = false;
			
			for (int i = 0; i < dataSize; i++)
			{
				c1 = data1.charAt(i);
				
				if (dataFlag)
				{
					if (c1 == newLine)
					{
						// Add Question and Answer to the lists
						questionList.add(qes);
						answerList.add(ans);
						randList.add(CardsCount);
						CardsCount++;
						
						// reset vars
						ans = "";
						qes = "";
						dataFlag = false;
						continue;
					}
					qes = qes + c1;
				}
				else
				{
					if (c1 == dataSep)
					{
						dataFlag = true;
						continue;
					}
					ans = ans + c1;
				}
			}
		} // if (dataSize > 0)
		
		// Show Card
		JLBLHint.setText("");
		answerCard = false;

		// enable controls
		if (CardsCount > 0)
		{
			JBTNCard.setEnabled(true);
			JBTNHint.setEnabled(true);
			JBTNLeft.setEnabled(true);
			JBTNRight.setEnabled(true);
			JBTNShuffle.setEnabled(true);

			showCardInfo();
		}
	} //  loadDeckFile(String pathAndfileName)
	
	//// Action Listener for Loading the Deck file
	ActionListener alLoadDeck = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			loadDeckFile();
		}
	};
	
	//// Action Listener for Card
	ActionListener alCardShow = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			JLBLHint.setText("");
			showCardInfo();
		}
	};
	
	//// Action Listener for Previous Card
	ActionListener alPrevCard = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			JLBLHint.setText("");
			if (CardsCount > 0)
			{
				if (CurrentCard > 0)
				{
					CurrentCard--;
					answerCard = false;
					showCardInfo();
				}
			}
		}
	};
	
	//// Action Listener for Next Card
	ActionListener alNextCard = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			JLBLHint.setText("");
			if (CardsCount > 0)
			{
				if (CurrentCard < (CardsCount - 1))
				{
					CurrentCard++;
					answerCard = false;
					showCardInfo();
				}
			}
		}
	};
	
	//// Action Listener for Show Hint
	ActionListener alShowHint = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			if (CardsCount > 0)
			{
				String s1 = "";
				int ansLen = 0;
				int showSize = 0;
				String s2 = "";
				int cardNum = 0;
				
				cardNum = randList.get(CurrentCard);
				s1 = answerList.get(cardNum);
				s1 = s1.trim();
				ansLen = s1.length();
				if (ansLen > 0)
				{
					showSize = (ansLen / 2);
					if (showSize <= 0)
					{
						showSize = 1;
					}
					s2 = s1.substring(0, showSize);
					JLBLHint.setText(s2+"...");
				}
			}
		}
	};

	//// Action Listener for Card Shuffle
	ActionListener alCardShuffle = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			if (CardsCount > 0)
			{
				// clear list pointers
				randList.clear();

				// shuffle and get random pointers
				int tCount = 0;
				int newNum = 0;
				while (tCount < CardsCount)
				{
					newNum = rndm.nextInt(CardsCount);
					if (!(randList.contains(newNum)))
					{
						randList.add(newNum);
						tCount++;
					}
				}

				CurrentCard = 0;
				JLBLHint.setText("");
				answerCard = false;

				JOptionPane.showMessageDialog(null, 
                                      "Flash Cards Shuffled.", 
                                      "Message", 
                                      JOptionPane.INFORMATION_MESSAGE);
				showCardInfo();
			} 
		}
	};
	
	/////////////////////////////////// constructor
	public FlashCardPlayer() {
		
		// Disable closing the App
		super.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		
		// Listener for closing the App
		super.addWindowListener(
		new WindowAdapter()
		{
			public void windowClosing(WindowEvent e)
			{
				closeApp();
			}
		}
		);
		
		// for Linux
		if (OSName.startsWith("Lin"))
		{
			OSFs = "/";
			appPath = System.getProperties().getProperty("java.class.path");
		}
		//// for windows
		else if (OSName.startsWith("Win"))
		{
			OSFs = "\\";
			File fl1 = new File("");
			appPath = fl1.getAbsolutePath();
		}
		//// other OS
		else
		{
			JOptionPane.showMessageDialog(null,
			"Unsupported Operating System.",
			"Flash Cards",
			JOptionPane.ERROR_MESSAGE);
			System.exit(0);
		}
		
		// Create ArrayLists for Questions and Answers
		questionList = new ArrayList<>();
		answerList = new ArrayList<>();
		randList = new ArrayList<>();
		
		cp.setLayout(null);
		
		JBTNLoadDeck = new  JButton();
		JBTNLoadDeck.setFont( new Font("Dialog",Font.BOLD, 12));
		JBTNLoadDeck.setBounds(3,1,129,30);
		JBTNLoadDeck.setBorder(BorderFactory.createRaisedBevelBorder());
		JBTNLoadDeck.setForeground( new Color(51,51,51));
		JBTNLoadDeck.setBackground( new Color(238,238,238));
		JBTNLoadDeck.setVisible(true);
		JBTNLoadDeck.setEnabled(true);
		JBTNLoadDeck.setDoubleBuffered(false);
		JBTNLoadDeck.setAutoscrolls(false);
		JBTNLoadDeck.setOpaque(true);
		JBTNLoadDeck.setRequestFocusEnabled(true);
		JBTNLoadDeck.setText("Load Deck");
		JBTNLoadDeck.setToolTipText("Load the Deck of Cards from the selected Deck file below");
		JBTNLoadDeck.setHorizontalAlignment(SwingConstants.CENTER );
		JBTNLoadDeck.setVerticalAlignment(SwingConstants.CENTER );
		JBTNLoadDeck.addActionListener(alLoadDeck);
		cp.add(JBTNLoadDeck);
		
		deckModel = new DefaultListModel<String>();
		JLSTDecks = new JList<String>(deckModel);
		JLSTDecks.setFont( new Font("Dialog",Font.BOLD, 12));
		JLSTDecks.setBorder(BorderFactory.createLoweredBevelBorder());
		JLSTDecks.setForeground( new Color(51,51,51));
		JLSTDecks.setBackground( new Color(255,255,255));
		JLSTDecks.setVisible(true);
		JLSTDecks.setEnabled(true);
		JLSTDecks.setDoubleBuffered(false);
		JLSTDecks.setAutoscrolls(true);
		JLSTDecks.setOpaque(true);
		JLSTDecks.setRequestFocusEnabled(true);
		JLSTDecks.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		JSPDecks = new JScrollPane(JLSTDecks);
		JSPDecks.setBounds(2,35,128,300);
		cp.add(JSPDecks);
		
		JLBLDeckInfo = new  JLabel();
		JLBLDeckInfo.setFont( new Font("Dialog",Font.BOLD, 12));
		JLBLDeckInfo.setBounds(135, 2, 500, 30);
		JLBLDeckInfo.setBorder(BorderFactory.createEtchedBorder());
		JLBLDeckInfo.setVisible(true);
		JLBLDeckInfo.setEnabled(true);
		JLBLDeckInfo.setDoubleBuffered(false);
		JLBLDeckInfo.setAutoscrolls(false);
		JLBLDeckInfo.setOpaque(false);
		JLBLDeckInfo.setRequestFocusEnabled(true);
		JLBLDeckInfo.setText("Deck:");
		JLBLDeckInfo.setHorizontalAlignment(SwingConstants.LEADING );
		JLBLDeckInfo.setVerticalAlignment(SwingConstants.CENTER );
		cp.add(JLBLDeckInfo);
		
		JBTNHint = new  JButton();
		JBTNHint.setFont( new Font("Dialog",Font.BOLD, 18));
		JBTNHint.setBounds(650, 2, 50, 30);
		JBTNHint.setBorder(BorderFactory.createRaisedBevelBorder());
		JBTNHint.setForeground( new Color(51,51,51));
		JBTNHint.setBackground( new Color(238,238,238));
		JBTNHint.setVisible(true);
		JBTNHint.setEnabled(false);
		JBTNHint.setDoubleBuffered(false);
		JBTNHint.setAutoscrolls(false);
		JBTNHint.setOpaque(true);
		JBTNHint.setRequestFocusEnabled(true);
		JBTNHint.setText("?");
		JBTNHint.setToolTipText("Show a Hint...");
		JBTNHint.setHorizontalAlignment(SwingConstants.CENTER );
		JBTNHint.setVerticalAlignment(SwingConstants.CENTER );
		JBTNHint.addActionListener(alShowHint);
		cp.add(JBTNHint);
		
		JLBLHint = new  JLabel();
		JLBLHint.setFont( new Font("Dialog",Font.BOLD, 12));
		JLBLHint.setBounds(702, 2, 130, 30);
		JLBLHint.setBorder(BorderFactory.createEtchedBorder());
		JLBLHint.setForeground( new Color(255,0,0));
		JLBLHint.setBackground(new Color(238,238,238));
		JLBLHint.setVisible(true);
		JLBLHint.setEnabled(true);
		JLBLHint.setDoubleBuffered(false);
		JLBLHint.setAutoscrolls(false);
		JLBLHint.setOpaque(false);
		JLBLHint.setRequestFocusEnabled(true);
		JLBLHint.setText("");
		JLBLHint.setHorizontalAlignment(SwingConstants.LEADING );
		JLBLHint.setVerticalAlignment(SwingConstants.CENTER );
		cp.add(JLBLHint);
		
		JBTNCard = new  JButton();
		JBTNCard.setFont( new Font("Dialog",Font.BOLD, 22));
		JBTNCard.setBounds(134, 35, 700, 300);
		JBTNCard.setBorder(BorderFactory.createRaisedBevelBorder());
		JBTNCard.setForeground( new Color(0,0,0));
		JBTNCard.setBackground(new Color(225, 228, 225));
		JBTNCard.setVisible(true);
		JBTNCard.setEnabled(false);
		JBTNCard.setDoubleBuffered(false);
		JBTNCard.setAutoscrolls(false);
		JBTNCard.setOpaque(true);
		JBTNCard.setRequestFocusEnabled(true);
		JBTNCard.setText("");
		JBTNCard.setHorizontalAlignment(SwingConstants.CENTER );
		JBTNCard.setVerticalAlignment(SwingConstants.CENTER );
		JBTNCard.addActionListener(alCardShow);
		cp.add(JBTNCard);
		
		JLBLClickInfo = new  JLabel();
		JLBLClickInfo.setFont( new Font("DIALOG",Font.BOLD, 14));
		JLBLClickInfo.setBounds(324, 340,258,30);
		JLBLClickInfo.setForeground( new Color(51,51,51));
		JLBLClickInfo.setBackground( new Color(238,238,238));
		JLBLClickInfo.setVisible(true);
		JLBLClickInfo.setEnabled(true);
		JLBLClickInfo.setDoubleBuffered(false);
		JLBLClickInfo.setAutoscrolls(false);
		JLBLClickInfo.setOpaque(false);
		JLBLClickInfo.setRequestFocusEnabled(true);
		JLBLClickInfo.setText("Click on the Card to flip");
		JLBLClickInfo.setHorizontalAlignment(SwingConstants.CENTER );
		JLBLClickInfo.setVerticalAlignment(SwingConstants.CENTER );
		cp.add(JLBLClickInfo);
		
		JBTNLeft = new  JButton();
		JBTNLeft.setFont( new Font("DIALOG",Font.BOLD, 18));
		JBTNLeft.setBounds(246, 340,55,30);
		JBTNLeft.setBorder(BorderFactory.createRaisedBevelBorder());
		JBTNLeft.setForeground( new Color(51,51,51));
		JBTNLeft.setBackground( new Color(238,238,238));
		JBTNLeft.setVisible(true);
		JBTNLeft.setEnabled(false);
		JBTNLeft.setDoubleBuffered(false);
		JBTNLeft.setAutoscrolls(false);
		JBTNLeft.setOpaque(true);
		JBTNLeft.setRequestFocusEnabled(true);
		JBTNLeft.setText("<--");
		JBTNLeft.setToolTipText("Show Previous Flash Card");
		JBTNLeft.setHorizontalAlignment(SwingConstants.CENTER );
		JBTNLeft.setVerticalAlignment(SwingConstants.CENTER );
		JBTNLeft.addActionListener(alPrevCard);
		cp.add(JBTNLeft);
		
		JBTNRight = new  JButton();
		JBTNRight.setFont( new Font("DIALOG",Font.BOLD, 18));
		JBTNRight.setBounds(605, 340, 55,30);
		JBTNRight.setBorder(BorderFactory.createRaisedBevelBorder());
		JBTNRight.setForeground( new Color(51,51,51));
		JBTNRight.setBackground( new Color(238,238,238));
		JBTNRight.setVisible(true);
		JBTNRight.setEnabled(false);
		JBTNRight.setDoubleBuffered(false);
		JBTNRight.setAutoscrolls(false);
		JBTNRight.setOpaque(true);
		JBTNRight.setRequestFocusEnabled(true);
		JBTNRight.setText("-->");
		JBTNRight.setToolTipText("Show Next Flash Card");
		JBTNRight.setHorizontalAlignment(SwingConstants.CENTER );
		JBTNRight.setVerticalAlignment(SwingConstants.CENTER );
		JBTNRight.addActionListener(alNextCard);
		cp.add(JBTNRight);
		
		JBTNShuffle = new  JButton();
		JBTNShuffle.setFont( new Font("Dialog",Font.BOLD, 12));
		JBTNShuffle.setBounds(3, 338, 120, 30);
		JBTNShuffle.setBorder(BorderFactory.createRaisedBevelBorder());
		JBTNShuffle.setForeground( new Color(51,51,51));
		JBTNShuffle.setBackground( new Color(238,238,238));
		JBTNShuffle.setVisible(true);
		JBTNShuffle.setEnabled(false);
		JBTNShuffle.setDoubleBuffered(false);
		JBTNShuffle.setAutoscrolls(false);
		JBTNShuffle.setOpaque(true);
		JBTNShuffle.setRequestFocusEnabled(true);
		JBTNShuffle.setText("Shuffle Cards");
		JBTNShuffle.setToolTipText("Shuffle Cards in Random order");
		JBTNShuffle.setHorizontalAlignment(SwingConstants.CENTER );
		JBTNShuffle.setVerticalAlignment(SwingConstants.CENTER );
		JBTNShuffle.addActionListener(alCardShuffle);
		cp.add(JBTNShuffle);
		
		// set properties of the App
		super.setTitle("Flash Card Player");
		super.setResizable(false);
		
		// Load all Deck File Name
		LoadAllDecks();
	}
	
	//// run the program
	public static void main(String[] args) {
		FlashCardPlayer Fcp = new FlashCardPlayer();
		Fcp.setBounds(5, 120, 850, 430);
		Fcp.setVisible(true);
	}
}
// end of FlashCardPlayer.java
