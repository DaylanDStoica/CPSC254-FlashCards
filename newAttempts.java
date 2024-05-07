void loadDeckFile()
{
    int listSelection = -1;
    String data1 = "";

    listSelection = JLSTDecks.getSelectedIndex();  // Assuming JLSTDecks is a list component
    if (listSelection >= 0)
    {
        deckFileName = deckModel.get(listSelection); // Assuming deckModel is the model of JLSTDecks
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
        // Set absolute path to /home/josue/Downloads/ and the filename
        deckPathAndFileName = "/home/josue/Downloads/" + deckFileName;
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
            "File " + deckFileName + " Not found in Downloads.",
            "Message",
            JOptionPane.ERROR_MESSAGE);
            return;
        }
    }
    catch(java.io.IOException e1)
    {
        JOptionPane.showMessageDialog(null,
        "Failed to open and read file " + deckFileName + " " + e1.toString(),
        "Message",
        JOptionPane.ERROR_MESSAGE);
        return;
    }
}
