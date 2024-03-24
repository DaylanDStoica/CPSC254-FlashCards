# CPSC254-CrosswordProject
Creating Crossword Project, for Open Source class CPSC254 Spring2024

Create a program, making use of Open Source Libraries, to build a CrossWord Puzzle Generator
1. User gives input string: "[answer], [clue]"
2. partition the input string, into 2 parts, 'answer' and 'clue' strings
3. append the 2 joined strings, into a database
    each row of the database table will consist of
    1. Word Number ( Unique ID) [integer], 2. Answer [string], 3. Clue [string], 4. Answer character length [integer]

    answer char legnth does not include the spaces of the official answer

4. from the data of the database, will draw the cells for a crossword puzzle
5. Draw the crossword puzzle live, as the user adds their own Answers and Clues, until user exits the program.
6. The crossword cells overlap perpendicular, at points of shared characters.


![image](https://github.com/DaylanDStoica/CPSC254-CrosswordProject/assets/56146054/c59d4569-855e-4dbc-94ea-b9a70f6b9c68)
