#include <iostream>

using namespace std;


// create and develop the fucntions to take input and parse into storable formats
string take_input();
string* parse_input( string in_str);
int get_answer_length( string answer_str);

string parse_char = ",";
int main(){
	string user_input = take_input();
	cout << user_input << endl;
	string* str_arr = parse_input( user_input); // input.cpp:14:40: error: array must be initialized with a brace-enclosed initializer

	string answer = str_arr[0];
	string clue = str_arr[1];
	cout << "length of answer: " << get_answer_length(str_arr[0] ) << endl;
	return 0;
}

string take_input(){
	string out_str;
	cout << "enter your answer and clue, separated by a comma (,): ";
//	cin >> out_str;
	getline(cin, out_str);
	return out_str;
}

string* parse_input(string in_str){
	// parse the given user input string, into string array of {answer, clue}
	// and return the array
	string answer = "";
	string clue = "";

	//parse the user input string, into substrings for answer and clues
	int parse_char_index = in_str.find(",");
	
	// string.substr( start_index, length_of_substring)
	answer = in_str.substr(0, parse_char_index);
	clue = in_str.substr(parse_char_index + 1, in_str.length() - parse_char_index);
	
	cout << "within parsing function \n";
	cout << "answer: " << answer << endl;
	cout << "clue: " << clue << endl;


	//TODO: implement a whitespace remover for the answer and clue strings.
	static string out_str_arr[2] = { answer, clue};
	//static array for ease of warnings
	
	return out_str_arr;
	
}

int get_answer_length ( string answer_str){
	int answer_len = answer_str.length();
	return answer_len;
}
