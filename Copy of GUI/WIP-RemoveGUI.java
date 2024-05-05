////
//// Program Name: FlashCards.java
//// Date:
//// Author:
//// Remark:

// Add Java support files
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
import java.util.ArrayList;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
// Main Class file for FlashCards

public class DatabaseConnector {
    private static Connection conn = null;

    public static Connection getConnection() {
        if (conn == null) {
            try {
                // Change these details according to your database configuration
                String url = "jdbc:mysql://localhost:3306/yourDatabaseName";
                String user = "yourUsername";
                String password = "yourPassword";
                conn = DriverManager.getConnection(url, user, password);
                System.out.println("Connected to the database successfully.");
            } catch (SQLException e) {
                System.out.println("SQL Exception: " + e.getMessage());
                e.printStackTrace();
            }
        }
        return conn;
    }
}
public class FlashCards extends JFrame
{
    // To identify OS
    static String OSName = System.getProperties().getProperty("os.name");
    // To identify file separator for paths
    static String OSFs = ""; // OS File Separator
    // To get App path
    static String appPath = "";

    // Define Java Swing GUI Objects
    Container cp = getContentPane();
    JLabel JLBLDeckID;
    JTextField JTXFDeckID;
    JButton JBTNLoadDeck;
    JLabel JLBLQuestion;
    JTextField JTXFQuestion;
    JLabel JLBLAnswer;
    JTextField JTXFAnswer;
    JButton JBTNAdd;
    JButton JBTNRemove;
    JLabel JLBLFlashCardList;
    DefaultListModel<String> flashCardListModel;
    JList<String> JLSTFlashCardList;
    JScrollPane JSPFlashCardList;
    JButton JBTNSaveDeck;
    JButton JBTNClose;

    // for storing Questions
    ArrayList<String> questionList;
    // for storing Answers
    ArrayList<String> answerList;
    // Character to separate Questions and Answers in the Deck file
    char dataSep = '=';
    char newLine = '\n';


    // Method for closing the App
    public void closeApp()
    {
        int exitValue = -1;
        exitValue = JOptionPane.showConfirmDialog(null,
                "Exit Flash Cards ?",
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

    //// Action Listener for adding Answer and Question to Puzzle
    ActionListener alAddToPuzzle = new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            String answer = JTXFAnswer.getText().trim();
            String question = JTXFQuestion.getText().trim();

            if (question.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Invalid Question.", "Message", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (answer.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Invalid Answer.", "Message", JOptionPane.ERROR_MESSAGE);
                return;
            }

            answer = answer.toUpperCase();
            JTXFAnswer.setText(answer);

            if (answerList.contains(answer)) {
                JOptionPane.showMessageDialog(null, answer + " already exists for this Puzzle.", "Message", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Add to the database
            String url = "jdbc:mysql://localhost:3306/FlashCards";
            String username = "FlashCards-Programmer";
            String password = "X$18joanri";
            String sql = "INSERT INTO flashcards (question, answer) VALUES (?, ?)";

            try (Connection conn = DriverManager.getConnection(url, username, password);
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, question);
                pstmt.setString(2, answer);
                pstmt.executeUpdate();
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "Failed to add flashcard to database.", "Database Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Add Question and Answer to the lists and update GUI
            questionList.add(question);
            answerList.add(answer);
            String listEntry = answer + " = " + question;
            flashCardListModel.addElement(listEntry);

            JTXFQuestion.setText("");
            JTXFAnswer.setText("");
        }
    };

    //// Action Listener for Removing entries from the List
//// Action Listener for Removing entries from the List
ActionListener alRemoveFromPuzzle = new ActionListener() {
    public void actionPerformed(ActionEvent e) {
        int listSelection = JLSTFlashCardList.getSelectedIndex();
        if (listSelection >= 0) {
            String listEntry = flashCardListModel.getElementAt(listSelection).toString();
            String[] parts = listEntry.split(" = ");
            String questionToDelete = parts[1];  // assuming question is after "="

            int userResponse = JOptionPane.showConfirmDialog(null,
                    "Remove entry '" + listEntry + "' from Deck ?",
                    "Message",
                    JOptionPane.YES_NO_OPTION);

            if (userResponse == JOptionPane.YES_OPTION) {
                // Establish database connection
                Connection conn = DatabaseConnector.getConnection();
                String sql = "DELETE FROM flashcards WHERE question = ?;";
                try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                    pstmt.setString(1, questionToDelete);
                    int affectedRows = pstmt.executeUpdate();
                    if (affectedRows > 0) {
                        System.out.println("Record deleted successfully from the database.");
                        answerList.remove(listSelection);
                        questionList.remove(listSelection);
                        flashCardListModel.removeElementAt(listSelection);
                    } else {
                        System.out.println("No record found with question: " + questionToDelete);
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        } else {
            JOptionPane.showMessageDialog(null,
                    "Select an entry from 'Answers and Questions' list.",
                    "Message",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
};



    //// to save the Deck entries to a Text file
    void saveDeckToFile()
    {
        String fileContent = "";
        int lSize = 0;
        String s1 = "";
        String s2 = "";
        String deckFileName = "";

        deckFileName = JTXFDeckID.getText();
        if (deckFileName.length() <= 0)
        {
            JOptionPane.showMessageDialog(null,
                    "Invalid Deck ID",
                    "Message",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        else
        {
            // set absolute path and file name (Store the text file in the App directory)
            deckFileName = appPath + OSFs + deckFileName + ".txt";
        }

        // Verify if the file already exists or not
        File fl1 = new File(deckFileName);
        boolean fileExist = false;
        try
        {
            fileExist = fl1.exists();
        }
        catch (SecurityException e)
        {
            fileExist = false;
        }

        // if file exists ask permission to overwrite it
        if (fileExist)
        {
            int overWrite = -1;

            overWrite = JOptionPane.showConfirmDialog(null,
                    "File " + deckFileName + " already Exists." + " Overwrite it ?",
                    "Message",
                    JOptionPane.YES_NO_OPTION);

            // Do not overwrite
            if (overWrite == 1)
            {
                return;
            }
        }

        // Get the List size
        lSize = answerList.size();

        // get entries from the List
        for (int i = 0; i < lSize; i++)
        {
            s1 = answerList.get(i);
            s2 = questionList.get(i);
            fileContent += s1 + "=" + s2 + "\n";
        }

        // Save the entries
        if (fileContent.length() > 0)
        {
            byte b[] = fileContent.getBytes();

            try
            {
                FileOutputStream out = new FileOutputStream(deckFileName);
                out.write(b);
                out.close();
            }
            catch(java.io.IOException e1)
            {
                JOptionPane.showMessageDialog(null,
                        "Could Not save the Deck. " + e1.toString(),
                        "Message",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
            JOptionPane.showMessageDialog(null,
                    "Flash Cards Deck Saved. File Name: " + deckFileName,
                    "Message",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }    // saveDeckToFile()

    //// Action Listener for Saving the Deck of Cards
    ActionListener alSaveDeck = new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            saveDeckToFile();
        }
    };

    // To open and read a Deck file
    void loadDeckFile()
    {
        String deckFileName = "";
        String data1 = "";

        deckFileName = JTXFDeckID.getText();
        if (deckFileName.length() <= 0)
        {
            JOptionPane.showMessageDialog(null,
                    "Invalid Deck ID",
                    "Message",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        else
        {
            // set absolute path and file name (Store the text file in the App directory)
            deckFileName = appPath + OSFs + deckFileName + ".txt";
        }

        try
        {
            File inputFile = new File(deckFileName);

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

        // if file found and read, load data into list
        int overWrite = -1;
        int dataSize = data1.length();
        String listEntry = "";
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
            flashCardListModel.removeAllElements();

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
                        // JOptionPane.showMessageDialog(null,qes,ans,JOptionPane.INFORMATION_MESSAGE);
                        // Add Question and Answer to the lists
                        questionList.add(qes);
                        answerList.add(ans);
                        listEntry = ans + " = " + qes;
                        flashCardListModel.addElement(listEntry);
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
    } //  loadDeckFile(String pathAndfileName)

    //// Action Listener for Loading the Deck file
    ActionListener alLoadDeck = new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            loadDeckFile();
        }
    };

    //// constructor
    public FlashCards() {

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

        cp.setLayout(null);

        JLBLDeckID = new  JLabel();
        JLBLDeckID.setFont( new Font("Dialog",Font.BOLD, 12));
        JLBLDeckID.setBounds(50,5,76,30);
        JLBLDeckID.setForeground( new Color(51,51,51));
        JLBLDeckID.setBackground( new Color(238,238,238));
        JLBLDeckID.setVisible(true);
        JLBLDeckID.setEnabled(true);
        JLBLDeckID.setDoubleBuffered(false);
        JLBLDeckID.setAutoscrolls(false);
        JLBLDeckID.setOpaque(false);
        JLBLDeckID.setRequestFocusEnabled(true);
        JLBLDeckID.setText("Deck ID:");
        JLBLDeckID.setHorizontalAlignment(SwingConstants.LEADING );
        JLBLDeckID.setVerticalAlignment(SwingConstants.CENTER );
        cp.add(JLBLDeckID);

        JTXFDeckID = new  JTextField();
        JTXFDeckID.setFont( new Font("Dialog",Font.PLAIN, 12));
        JTXFDeckID.setBounds(118,6,113,30);
        JTXFDeckID.setBorder(BorderFactory.createEtchedBorder());
        JTXFDeckID.setForeground( new Color(51,51,51));
        JTXFDeckID.setBackground( new Color(255,255,255));
        JTXFDeckID.setVisible(true);
        JTXFDeckID.setEnabled(true);
        JTXFDeckID.setDoubleBuffered(false);
        JTXFDeckID.setAutoscrolls(true);
        JTXFDeckID.setOpaque(true);
        JTXFDeckID.setRequestFocusEnabled(true);
        JTXFDeckID.setText("");
        JTXFDeckID.setHorizontalAlignment(SwingConstants.LEADING );
        cp.add(JTXFDeckID);

        JBTNLoadDeck = new  JButton();
        JBTNLoadDeck.setFont( new Font("Dialog",Font.BOLD, 12));
        JBTNLoadDeck.setBounds(330, 6, 140,30);
        JBTNLoadDeck.setBorder(BorderFactory.createRaisedBevelBorder());
        JBTNLoadDeck.setForeground( new Color(51,51,51));
        JBTNLoadDeck.setBackground( new Color(238,238,238));
        JBTNLoadDeck.setVisible(true);
        JBTNLoadDeck.setEnabled(true);
        JBTNLoadDeck.setDoubleBuffered(false);
        JBTNLoadDeck.setAutoscrolls(false);
        JBTNLoadDeck.setOpaque(true);
        JBTNLoadDeck.setRequestFocusEnabled(true);
        JBTNLoadDeck.setText("Load Deck File");
        JBTNLoadDeck.setHorizontalAlignment(SwingConstants.CENTER );
        JBTNLoadDeck.setVerticalAlignment(SwingConstants.CENTER );
        JBTNLoadDeck.addActionListener(alLoadDeck);
        cp.add(JBTNLoadDeck);

        JBTNSaveDeck = new  JButton();
        JBTNSaveDeck.setFont( new Font("Dialog",Font.BOLD, 12));
        JBTNSaveDeck.setBounds(475, 6, 150, 30);
        JBTNSaveDeck.setBorder(BorderFactory.createRaisedBevelBorder());
        JBTNSaveDeck.setForeground( new Color(51,51,51));
        JBTNSaveDeck.setBackground( new Color(238,238,238));
        JBTNSaveDeck.setVisible(true);
        JBTNSaveDeck.setEnabled(true);
        JBTNSaveDeck.setDoubleBuffered(false);
        JBTNSaveDeck.setAutoscrolls(false);
        JBTNSaveDeck.setOpaque(true);
        JBTNSaveDeck.setRequestFocusEnabled(true);
        JBTNSaveDeck.setText("Save to Deck File");
        JBTNSaveDeck.setHorizontalAlignment(SwingConstants.CENTER );
        JBTNSaveDeck.setVerticalAlignment(SwingConstants.CENTER );
        JBTNSaveDeck.addActionListener(alSaveDeck);
        cp.add(JBTNSaveDeck);

        JLBLQuestion = new  JLabel();
        JLBLQuestion.setFont( new Font("Dialog",Font.BOLD, 12));
        JLBLQuestion.setBounds(40,39,86,30);
        JLBLQuestion.setForeground( new Color(51,51,51));
        JLBLQuestion.setBackground( new Color(238,238,238));
        JLBLQuestion.setVisible(true);
        JLBLQuestion.setEnabled(true);
        JLBLQuestion.setDoubleBuffered(false);
        JLBLQuestion.setAutoscrolls(false);
        JLBLQuestion.setOpaque(false);
        JLBLQuestion.setRequestFocusEnabled(true);
        JLBLQuestion.setText("Question:");
        JLBLQuestion.setHorizontalAlignment(SwingConstants.LEADING );
        JLBLQuestion.setVerticalAlignment(SwingConstants.CENTER );
        cp.add(JLBLQuestion);

        JTXFQuestion = new  JTextField();
        JTXFQuestion.setFont( new Font("Dialog",Font.PLAIN, 12));
        JTXFQuestion.setBounds(117,41,510,30);
        JTXFQuestion.setBorder(BorderFactory.createLoweredBevelBorder());
        JTXFQuestion.setForeground( new Color(51,51,51));
        JTXFQuestion.setBackground( new Color(255,255,255));
        JTXFQuestion.setVisible(true);
        JTXFQuestion.setEnabled(true);
        JTXFQuestion.setDoubleBuffered(false);
        JTXFQuestion.setAutoscrolls(true);
        JTXFQuestion.setOpaque(true);
        JTXFQuestion.setRequestFocusEnabled(true);
        JTXFQuestion.setText("");
        JTXFQuestion.setHorizontalAlignment(SwingConstants.LEADING );
        cp.add(JTXFQuestion);

        JLBLAnswer = new  JLabel();
        JLBLAnswer.setFont( new Font("Dialog",Font.BOLD, 12));
        JLBLAnswer.setBounds(50,73,106,30);
        JLBLAnswer.setForeground( new Color(51,51,51));
        JLBLAnswer.setBackground( new Color(238,238,238));
        JLBLAnswer.setVisible(true);
        JLBLAnswer.setEnabled(true);
        JLBLAnswer.setDoubleBuffered(false);
        JLBLAnswer.setAutoscrolls(false);
        JLBLAnswer.setOpaque(false);
        JLBLAnswer.setRequestFocusEnabled(true);
        JLBLAnswer.setText("Answer:");
        JLBLAnswer.setHorizontalAlignment(SwingConstants.LEADING );
        JLBLAnswer.setVerticalAlignment(SwingConstants.CENTER );
        cp.add(JLBLAnswer);

        JTXFAnswer = new  JTextField();
        JTXFAnswer.setFont( new Font("Dialog",Font.PLAIN, 12));
        JTXFAnswer.setBounds(117,73,510,30);
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
        JBTNAdd.setText("Add to Deck");
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
        JBTNRemove.setText("Remove from Deck");
        JBTNRemove.setHorizontalAlignment(SwingConstants.CENTER );
        JBTNRemove.setVerticalAlignment(SwingConstants.CENTER );
        JBTNRemove.addActionListener(alRemoveFromPuzzle);
        cp.add(JBTNRemove);

        JLBLFlashCardList = new  JLabel();
        JLBLFlashCardList.setFont( new Font("Dialog",Font.BOLD, 12));
        JLBLFlashCardList.setBounds(6,157,175,30);
        JLBLFlashCardList.setForeground( new Color(51,51,51));
        JLBLFlashCardList.setBackground( new Color(238,238,238));
        JLBLFlashCardList.setVisible(true);
        JLBLFlashCardList.setEnabled(true);
        JLBLFlashCardList.setDoubleBuffered(false);
        JLBLFlashCardList.setAutoscrolls(false);
        JLBLFlashCardList.setOpaque(false);
        JLBLFlashCardList.setRequestFocusEnabled(true);
        JLBLFlashCardList.setText("Answers and Questions:");
        JLBLFlashCardList.setHorizontalAlignment(SwingConstants.LEADING );
        JLBLFlashCardList.setVerticalAlignment(SwingConstants.CENTER );
        cp.add(JLBLFlashCardList);

        flashCardListModel = new DefaultListModel<String>();
        JLSTFlashCardList = new JList<String>(flashCardListModel);
        JLSTFlashCardList.setFont( new Font("Dialog",Font.BOLD, 12));
        JLSTFlashCardList.setBorder(BorderFactory.createLoweredBevelBorder());
        JLSTFlashCardList.setForeground( new Color(51,51,51));
        JLSTFlashCardList.setBackground( new Color(255,255,255));
        JLSTFlashCardList.setVisible(true);
        JLSTFlashCardList.setEnabled(true);
        JLSTFlashCardList.setDoubleBuffered(false);
        JLSTFlashCardList.setAutoscrolls(true);
        JLSTFlashCardList.setOpaque(true);
        JLSTFlashCardList.setRequestFocusEnabled(true);
        JLSTFlashCardList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        // create a Java ScrollPane with the List
        JSPFlashCardList = new JScrollPane(JLSTFlashCardList);
        JSPFlashCardList.setBounds(3,188,632,448);
        cp.add(JSPFlashCardList);

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

        // Create ArrayLists for Questions and Answers
        questionList = new ArrayList<>();
        answerList = new ArrayList<>();

        // set properties of the App
        super.setTitle("Flash Cards");
        super.setResizable(false);
    }

    //// Start the App
    public static void main(String[] args)
    {
        // start the app instance
        FlashCards flashCards = new FlashCards();

        // set position and boundaries
        flashCards.setBounds(10, 15, 665, 725);

        // make the App visible
        flashCards.setVisible(true);
    }
}
//// End of FlashCards.java
