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
        String url = "jdbc:mysql://localhost:3306/yourdatabase?useSSL=false";
        String username = "root";
        String password = "yourpassword";
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
