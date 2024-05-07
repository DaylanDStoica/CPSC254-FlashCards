public void LoadAllDecks() {
    // Set the directory where the text files are stored
    String appPath = "/home/josue/Downloads/";

    // File extension filter to select only .txt files
    FilenameFilter ff = new FilenameFilter() {
        public boolean accept(File dir, String name) {
            return name.toLowerCase().endsWith(".txt");
        }
    };

    // Directory file
    File dir = new File(appPath);
    String[] filteredFiles = {};

    // Check if the directory exists and is a directory, then list files using the filter
    if (dir.isDirectory()) {
        filteredFiles = dir.list(ff);
    }

    // Clear previous entries in the model
    deckModel.clear();

    // Iterate over filtered files and add them to the model
    for (String fileName : filteredFiles) {
        fileName = fileName.trim();
        if (fileName.length() > 0) {
            deckModel.addElement(fileName);
        }
    }
}
