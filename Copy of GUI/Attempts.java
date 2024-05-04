import javax.swing.*;
import java.awt.event.*;
import java.sql.*;

public class YourClassName {
    private FlashcardDAO flashcardDAO;

    public YourClassName() {
        try {
            this.flashcardDAO = new FlashcardDAO(getConnection());
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, 
                                          "Failed to connect to the database: " + ex.getMessage(), 
                                          "Database Error", 
                                           JOptionPane.ERROR_MESSAGE);
            return;  // Exit if connection fails
        }
    }

    private Connection getConnection() throws SQLException {
        // Implement your connection logic here (or use a connection pool)
        return DriverManager.getConnection("jdbc:mysql://localhost:3306/FlashCards", "FlashCards-Programmer", "X$18joanri");
    }

    private void setupUI() {
        // Your UI setup code here
        JButton addButton = new JButton("Add Flashcard");
        JTextField JTXFQuestion = new JTextField(20);
        JTextField JTXFAnswer = new JTextField(20);

        ActionListener alAddToPuzzle = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String answer = JTXFAnswer.getText().trim().toUpperCase();
                String question = JTXFQuestion.getText().trim();

                if (question.isEmpty() || answer.isEmpty()) {
                    JOptionPane.showMessageDialog(null, 
                                                  "Question or Answer cannot be empty.", 
                                                  "Validation Error", 
                                                   JOptionPane.ERROR_MESSAGE);
                    return;
                }

                try {
                    flashcardDAO.addFlashcard(question, answer);
                    JOptionPane.showMessageDialog(null, 
                                                  "Flashcard added successfully!", 
                                                  "Success", 
                                                   JOptionPane.INFORMATION_MESSAGE);
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, 
                                                  "Error adding flashcard: " + ex.getMessage(), 
                                                  "Database Error", 
                                                   JOptionPane.ERROR_MESSAGE);
                }
            }
        };

        addButton.addActionListener(alAddToPuzzle);
        // Add other UI components and setup
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new YourClassName().setupUI());
    }
}
