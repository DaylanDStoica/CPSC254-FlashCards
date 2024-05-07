#include <iostream>
#include <vector>
#include <string>

using namespace std;

vector<string> allAnswers;
vector<string> allHints;

void printAnswers(const vector<string>& collection) // prints out contents of vector
{
    for (int i = 0; i < collection.size(); i++)
    {
        cout << collection[i] << endl;
    }
}

int main() {
    string answer;
    string hint;
    cout << "To stop playing, type 'Stop'" << endl;

    while (true)
    {
        cout << "Please choose a word: ";
        getline(cin, answer);
        if (answer == "Stop")
            break;
        else
        {
            allAnswers.push_back(answer);
        }
        cout << "\nPlease give a hint for the answer: ";
        getline(cin, hint);
        if (hint == "Stop")
            break;
        else
        {
            allHints.push_back(hint);
        }
        cout << "---------------------------" << endl;
    }
    // prints out allAnswers and allHints vectors content
    cout << "-----------------Answers------------------" << endl;
    printAnswers(allAnswers);
    cout << "----------------Hints---------------------" << endl;
    printAnswers(allHints);



    return 0;
}
