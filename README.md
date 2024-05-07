# CPSC254-FlashCards
Creating Flash Card Creation Tool, for Open Source class CPSC254 Spring2024

Create a program, making use of Open Source Libraries, to build a Flash Card Tool
1. User gives input string: "[answer], [question]"
2. partition the input string, into 2 parts, 'answer' and 'question' strings
3. append the 2 joined strings, into a database
    each row of the database table will consist of
    1. Word Number ( Unique ID) [integer],
    2. Answer [string],
    3. Question [string]

## Program Instructions
from the program's home, run the loadFlashCards.sh shell file from within the LoadingGui subdirectory.
### go into LoadingGui directory
### run ./loadFlashCards.sh


Will open a GUI window for creating new flash card data. After closing that window, user will be prompted if they would now like to open a new window for viewing and using their flashcards, displaying existing flash cards for their intended use.

To change the deck in use that is being read from or being saved to, user must write the txt file name within the "Deck ID" text entry box at top left of the windows.
