///////
/////// Program Name: CrosswordPuzzleMaker.java
///////

// Add Java support files
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import java.io.*;
import java.util.ArrayList;

// Main Class file for Crossword Puzzle Maker
public class CrosswordPuzzleMaker extends JFrame {
	
	// Define variables for GUI Objects
	Container cp = getContentPane();
	JLabel JLBLClue;
	JTextField JTXFClue;
	JLabel JLBLAnswer;
	JTextField JTXFAnswer;
	JButton JBTNAdd;
	JLabel JLBLPuzzleID;
	JTextField JTXFPuzzleID;
	JButton JBTNRemove;
	JLabel JLBLPuzzleList;
	DefaultListModel<String> puzzleListModel;
	JList<String> JLSTPuzzleList;
	JScrollPane JSPPuzzleList;
	JButton JBTNSavePuzzle;
	JButton JBTNClose;
	
	// for storing Clues
	ArrayList<String> clueList; 

	// for storing Answers
	ArrayList<String> answerList; 

	// Method for closing the App
	public void closeApp()
	{
		int exitValue = -1;
		exitValue = JOptionPane.showConfirmDialog(null,
		"Exit Crossword Puzzle Maker ?",
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

	//// Action Listener for adding Answer and Clue to Puzzle
	ActionListener alAddToPuzzle = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			String answer = "";
			String clue = "";
			String listEntry = "";

			// validate user entered Clue
			clue = JTXFClue.getText();
			clue = clue.trim();
			if (clue.length() <= 0)
			{
				JOptionPane.showMessageDialog(null, 
                                      "Invalid Clue.", 
                                      "Message", 
                                       JOptionPane.ERROR_MESSAGE);
				return;
			}

			// validate user entered Answer
			answer = JTXFAnswer.getText();
			answer = answer.trim();
			if (answer.length() <= 0)
			{
				JOptionPane.showMessageDialog(null, 
                                      "Invalid Answer.", 
                                      "Message", 
                                       JOptionPane.ERROR_MESSAGE);
				return;
			}

			answer = answer.toUpperCase();
			JTXFAnswer.setText(answer);

			if (answerList.contains(answer))
			{
				JOptionPane.showMessageDialog(null, 
                                      answer + " already exists for this Puzzle.", 
                                      "Message", 
                                       JOptionPane.ERROR_MESSAGE);
				return;
			}

			// Add Clue and Answer to the lists
			clueList.add(clue);
			answerList.add(answer);
			listEntry = answer + " = " + clue;
			puzzleListModel.addElement(listEntry);

			JTXFClue.setText("");
			JTXFAnswer.setText("");
		}
	};
	
	//// Action Listener for Removing entries from the List
	ActionListener alRemoveFromPuzzle = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			int listSelection = -1;
			String listEntry = "";
			int userResponse = -1;

			listSelection = JLSTPuzzleList.getSelectedIndex();
			if (listSelection >= 0)
			{
				listEntry = answerList.get(listSelection);				
				userResponse = JOptionPane.showConfirmDialog(null,
                      		"Remove entry '" + listEntry + "' from Puzzle ?",
                      		"Message",
                       	JOptionPane.YES_NO_OPTION);
		
				if (userResponse == 0)
				{
					// remove entry
					answerList.remove(listSelection);
					clueList.remove(listSelection);
					puzzleListModel.removeElementAt(listSelection);
				}
				if (userResponse == 1)
				{
					// No
				}
			}
			else
			{
				JOptionPane.showMessageDialog(null, 
                                      "Select an entry from 'Answers and Clues' list.", 
                                      "Message", 
                                       JOptionPane.ERROR_MESSAGE);
				return;				
			}
		}
	};

	//// constructor
	public CrosswordPuzzleMaker() {
		
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

		cp.setLayout(null);
		
		JLBLPuzzleID = new  JLabel();
		JLBLPuzzleID.setFont( new Font("Dialog",Font.BOLD, 12));
		JLBLPuzzleID.setBounds(38,5,76,30);
		JLBLPuzzleID.setForeground( new Color(51,51,51));
		JLBLPuzzleID.setBackground( new Color(238,238,238));
		JLBLPuzzleID.setVisible(true);
		JLBLPuzzleID.setEnabled(true);
		JLBLPuzzleID.setDoubleBuffered(false);
		JLBLPuzzleID.setAutoscrolls(false);
		JLBLPuzzleID.setOpaque(false);
		JLBLPuzzleID.setRequestFocusEnabled(true);
		JLBLPuzzleID.setText("Puzzle ID:");
		JLBLPuzzleID.setHorizontalAlignment(SwingConstants.LEADING );
		JLBLPuzzleID.setVerticalAlignment(SwingConstants.CENTER );
		cp.add(JLBLPuzzleID);
		
		JTXFPuzzleID = new  JTextField();
		JTXFPuzzleID.setFont( new Font("Dialog",Font.PLAIN, 12));
		JTXFPuzzleID.setBounds(118,6,113,30);
		JTXFPuzzleID.setBorder(BorderFactory.createEtchedBorder());
		JTXFPuzzleID.setForeground( new Color(51,51,51));
		JTXFPuzzleID.setBackground( new Color(255,255,255));
		JTXFPuzzleID.setVisible(true);
		JTXFPuzzleID.setEnabled(true);
		JTXFPuzzleID.setDoubleBuffered(false);
		JTXFPuzzleID.setAutoscrolls(true);
		JTXFPuzzleID.setOpaque(true);
		JTXFPuzzleID.setRequestFocusEnabled(true);
		JTXFPuzzleID.setText("");
		JTXFPuzzleID.setHorizontalAlignment(SwingConstants.LEADING );
		cp.add(JTXFPuzzleID);

		JLBLClue = new  JLabel();
		JLBLClue.setFont( new Font("Dialog",Font.BOLD, 12));
		JLBLClue.setBounds(31,39,86,30);
		JLBLClue.setForeground( new Color(51,51,51));
		JLBLClue.setBackground( new Color(238,238,238));
		JLBLClue.setVisible(true);
		JLBLClue.setEnabled(true);
		JLBLClue.setDoubleBuffered(false);
		JLBLClue.setAutoscrolls(false);
		JLBLClue.setOpaque(false);
		JLBLClue.setRequestFocusEnabled(true);
		JLBLClue.setText("Enter Clue:");
		JLBLClue.setHorizontalAlignment(SwingConstants.LEADING );
		JLBLClue.setVerticalAlignment(SwingConstants.CENTER );
		cp.add(JLBLClue);
		
		JTXFClue = new  JTextField();
		JTXFClue.setFont( new Font("Dialog",Font.PLAIN, 12));
		JTXFClue.setBounds(117,41,510,30);
		JTXFClue.setBorder(BorderFactory.createLoweredBevelBorder());
		JTXFClue.setForeground( new Color(51,51,51));
		JTXFClue.setBackground( new Color(255,255,255));
		JTXFClue.setVisible(true);
		JTXFClue.setEnabled(true);
		JTXFClue.setDoubleBuffered(false);
		JTXFClue.setAutoscrolls(true);
		JTXFClue.setOpaque(true);
		JTXFClue.setRequestFocusEnabled(true);
		JTXFClue.setText("");
		JTXFClue.setHorizontalAlignment(SwingConstants.LEADING );
		cp.add(JTXFClue);
		
		JLBLAnswer = new  JLabel();
		JLBLAnswer.setFont( new Font("Dialog",Font.BOLD, 12));
		JLBLAnswer.setBounds(11,73,106,30);
		JLBLAnswer.setForeground( new Color(51,51,51));
		JLBLAnswer.setBackground( new Color(238,238,238));
		JLBLAnswer.setVisible(true);
		JLBLAnswer.setEnabled(true);
		JLBLAnswer.setDoubleBuffered(false);
		JLBLAnswer.setAutoscrolls(false);
		JLBLAnswer.setOpaque(false);
		JLBLAnswer.setRequestFocusEnabled(true);
		JLBLAnswer.setText("Enter Answer:");
		JLBLAnswer.setHorizontalAlignment(SwingConstants.LEADING );
		JLBLAnswer.setVerticalAlignment(SwingConstants.CENTER );
		cp.add(JLBLAnswer);
		
		JTXFAnswer = new  JTextField();
		JTXFAnswer.setFont( new Font("Dialog",Font.PLAIN, 12));
		JTXFAnswer.setBounds(117,73,260,30);
		JTXFAnswer.setBorder(BorderFactory.createLoweredBevelBorder());
		JTXFAnswer.setForeground( new Color(51,51,51));
		JTXFAnswer.setBackground( new Color(255,255,255));
		JTXFAnswer.setVisible(true);
		JTXFAnswer.setEnabled(true);
		JTXFAnswer.setDoubleBuffered(false);
		JTXFAnswer.setAutoscrolls(true);
		JTXFAnswer.setOpaque(true);
		JTXFAnswer.setRequestFocusEnabled(true);
		JTXFAnswer.setText("");
		JTXFAnswer.setHorizontalAlignment(SwingConstants.LEADING );
		cp.add(JTXFAnswer);
		
		JBTNAdd = new  JButton();
		JBTNAdd.setFont( new Font("Dialog",Font.BOLD, 12));
		JBTNAdd.setBounds(330,106,125,30);
		JBTNAdd.setBorder(BorderFactory.createRaisedBevelBorder());
		JBTNAdd.setForeground( new Color(51,51,51));
		JBTNAdd.setBackground( new Color(238,238,238));
		JBTNAdd.setVisible(true);
		JBTNAdd.setEnabled(true);
		JBTNAdd.setDoubleBuffered(false);
		JBTNAdd.setAutoscrolls(false);
		JBTNAdd.setOpaque(true);
		JBTNAdd.setRequestFocusEnabled(true);
		JBTNAdd.setText("Add to Puzzle");
		JBTNAdd.setHorizontalAlignment(SwingConstants.CENTER );
		JBTNAdd.setVerticalAlignment(SwingConstants.CENTER );
		JBTNAdd.addActionListener(alAddToPuzzle);
		cp.add(JBTNAdd);
		
		JBTNRemove = new  JButton();
		JBTNRemove.setFont( new Font("Dialog",Font.BOLD, 12));
		JBTNRemove.setBounds(460,106,166,30);
		JBTNRemove.setBorder(BorderFactory.createRaisedBevelBorder());
		JBTNRemove.setForeground( new Color(51,51,51));
		JBTNRemove.setBackground( new Color(238,238,238));
		JBTNRemove.setVisible(true);
		JBTNRemove.setEnabled(true);
		JBTNRemove.setDoubleBuffered(false);
		JBTNRemove.setAutoscrolls(false);
		JBTNRemove.setOpaque(true);
		JBTNRemove.setRequestFocusEnabled(true);
		JBTNRemove.setText("Remove from Puzzle");
		JBTNRemove.setHorizontalAlignment(SwingConstants.CENTER );
		JBTNRemove.setVerticalAlignment(SwingConstants.CENTER );
		JBTNRemove.addActionListener(alRemoveFromPuzzle);
		cp.add(JBTNRemove);
		
		JLBLPuzzleList = new  JLabel();
		JLBLPuzzleList.setFont( new Font("Dialog",Font.BOLD, 12));
		JLBLPuzzleList.setBounds(6,157,152,30);
		JLBLPuzzleList.setForeground( new Color(51,51,51));
		JLBLPuzzleList.setBackground( new Color(238,238,238));
		JLBLPuzzleList.setVisible(true);
		JLBLPuzzleList.setEnabled(true);
		JLBLPuzzleList.setDoubleBuffered(false);
		JLBLPuzzleList.setAutoscrolls(false);
		JLBLPuzzleList.setOpaque(false);
		JLBLPuzzleList.setRequestFocusEnabled(true);
		JLBLPuzzleList.setText("Answers and Clues:");
		JLBLPuzzleList.setHorizontalAlignment(SwingConstants.LEADING );
		JLBLPuzzleList.setVerticalAlignment(SwingConstants.CENTER );
		cp.add(JLBLPuzzleList);
		
		puzzleListModel = new DefaultListModel<String>();
		JLSTPuzzleList = new JList<String>(puzzleListModel);
		JLSTPuzzleList.setFont( new Font("Dialog",Font.BOLD, 12));
		JLSTPuzzleList.setBorder(BorderFactory.createLoweredBevelBorder());
		JLSTPuzzleList.setForeground( new Color(51,51,51));
		JLSTPuzzleList.setBackground( new Color(255,255,255));
		JLSTPuzzleList.setVisible(true);
		JLSTPuzzleList.setEnabled(true);
		JLSTPuzzleList.setDoubleBuffered(false);
		JLSTPuzzleList.setAutoscrolls(true);
		JLSTPuzzleList.setOpaque(true);
		JLSTPuzzleList.setRequestFocusEnabled(true);
		// create a Java ScrollPane with the List
		JSPPuzzleList = new JScrollPane(JLSTPuzzleList);
		JSPPuzzleList.setBounds(3,188,632,448);
		cp.add(JSPPuzzleList);
		
		JBTNSavePuzzle = new  JButton();
		JBTNSavePuzzle.setFont( new Font("Dialog",Font.BOLD, 12));
		JBTNSavePuzzle.setBounds(444,640,101,30);
		JBTNSavePuzzle.setBorder(BorderFactory.createRaisedBevelBorder());
		JBTNSavePuzzle.setForeground( new Color(51,51,51));
		JBTNSavePuzzle.setBackground( new Color(238,238,238));
		JBTNSavePuzzle.setVisible(true);
		JBTNSavePuzzle.setEnabled(false);
		JBTNSavePuzzle.setDoubleBuffered(false);
		JBTNSavePuzzle.setAutoscrolls(false);
		JBTNSavePuzzle.setOpaque(true);
		JBTNSavePuzzle.setRequestFocusEnabled(true);
		JBTNSavePuzzle.setText("Save Puzzle");
		JBTNSavePuzzle.setHorizontalAlignment(SwingConstants.CENTER );
		JBTNSavePuzzle.setVerticalAlignment(SwingConstants.CENTER );
		cp.add(JBTNSavePuzzle);
		
		JBTNClose = new  JButton();
		JBTNClose.setFont( new Font("Dialog",Font.BOLD, 12));
		JBTNClose.setBounds(554,640,80,30);
		JBTNClose.setBorder(BorderFactory.createRaisedBevelBorder());
		JBTNClose.setForeground( new Color(51,51,51));
		JBTNClose.setBackground( new Color(238,238,238));
		JBTNClose.setVisible(true);
		JBTNClose.setEnabled(true);
		JBTNClose.setDoubleBuffered(false);
		JBTNClose.setAutoscrolls(false);
		JBTNClose.setOpaque(true);
		JBTNClose.setRequestFocusEnabled(true);
		JBTNClose.setText("Exit");
		JBTNClose.setHorizontalAlignment(SwingConstants.CENTER );
		JBTNClose.setVerticalAlignment(SwingConstants.CENTER );
		// Attach the Action Listener to the button
		JBTNClose.addActionListener(alCloseApp);
		cp.add(JBTNClose);

		// Create ArrayLists for Clues and Answers
		clueList = new ArrayList<>(); 
		answerList = new ArrayList<>(); 

		// set properties of the App
		super.setTitle("Crossword Puzzle Maker");

		// Disable Resizing the Application Screen
		super.setResizable(false);
	}
	
	//// run the program
	public static void main(String[] args) 
	{
		// Initiate the Application
		CrosswordPuzzleMaker CrosswordPuzzle = new CrosswordPuzzleMaker();

		// Set Application X, Y Positions and Width and Height attributes.
		CrosswordPuzzle.setBounds(10, 15, 665, 725);

		// Show the Application
		CrosswordPuzzle.setVisible(true);
	}
}
//// End of CrosswordPuzzleMaker.java
