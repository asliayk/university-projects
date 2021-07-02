#include <unistd.h>
#include <stdlib.h>
#include <stdio.h>

#include <sys/types.h>
#include <sys/wait.h>
#include <iostream>
#include<bits/stdc++.h>
#include <cstdlib>
using namespace std;


// Takes a string as parameter and returns a list(vector) of words in it
vector<string> intoWords(string input) 
{ 
    vector<string> words;
    string word; 
    stringstream iss(input); 

    while (iss >> word) 
        words.push_back(word);

  return words; 
} 


int main(){ 

cout << "Welcome to myShell!" << endl;

// list(vector) of commands to save history
vector<string> commands;

// array for pipe
int fd[2];
// char array for read function
char read_msg[999999];


while(true) {  

  // arrays to be parameters for execvp command, which will be used in listdir, listdir -a, and currentpath       commands                                                                                                                             
  char const * const listdir[3] = {"ls", NULL, NULL} ;
  char const * const listdir_a[3] = {"ls", "-a", NULL} ;
  char const * const currentpath[3] = {"pwd", NULL, NULL} ;

  // pid_t values for child and grandchild processes
  pid_t current_pid, current_pid1;

  // outputs current user name   
  char * user_name = getenv("USER");
  cout << user_name << " >>> ";

  // takes new input(command)
  char inp[1000];
  cin.getline(inp,sizeof(inp));
  string input(inp);

  // creates child process
  current_pid = fork();

  // list of words in input(words of the command in shell)
  vector<string> newCommand = intoWords(input);

  // exits the shell if the input(command) is "exit"
  if(find(newCommand.begin(), newCommand.end(), "exit") != newCommand.end()) {
    exit(0);
  }


  // if command contains grep, it detects which word will be searched
  string grepAr;
  if(find(newCommand.begin(), newCommand.end(), "grep") != newCommand.end()) {
    // if the word contains double quotes, it removes them
    if(newCommand[newCommand.size()-1].at(0)=='\"' && newCommand[newCommand.size()-1].at(newCommand[newCommand.size()-1].length()-1)=='\"') {
      grepAr = newCommand[newCommand.size()-1].substr(1, newCommand[newCommand.size()-1].length() - 2);
    } else {
      grepAr = newCommand[newCommand.size()-1];
    }     
  }

  // creates an array for grep command, which will be used as a parameter in execvp function for the command "listdir | grep "argument""
  char const * const grep_args[3] = {"grep", grepAr.c_str(), NULL};

  // if command is printfile, it detects the file name written.
  string fileName = "";
  if(find(newCommand.begin(), newCommand.end(), "printfile") != newCommand.end() && find(newCommand.begin(), newCommand.end(), ">") == newCommand.end()) {
    fileName = newCommand[1];  
  }

  // creates an array for printfile command, which will be used in execvp function for the command "printfile (fileName)"
  char const * const printFile[3] = {"cat", fileName.c_str(), NULL};

  // child process
  if (current_pid == 0){

    // if the command contains grep, it's a pipe. Child and grandchild processes handles the pipe process
    if(find(newCommand.begin(), newCommand.end(), "grep") != newCommand.end()) {

      // starts pipe, returns 1 if it fails
      if (pipe(fd) == -1) {
        cout << "pipe failed" << endl;
        return 1;
      }

      // creates child process(child of the child)
      current_pid1 = fork();

      // grandchild process creates an outcome for "listdir" or "listdir -a" commands to be used in the parent process
      if(current_pid1==0) {
        // closes the unused ends
        close(fd[0]);
        // redirects standard output to fd[1]
        dup2(fd[1], 1);
        close(fd[1]);
 
        // chooses the command for the process(listdir or listdir -a)
        if(find(newCommand.begin(), newCommand.end(), "-a") != newCommand.end()) {
          execvp(listdir_a[0], (char**) listdir_a);
        } else {
          execvp(listdir[0], (char**) listdir);
        }
      }

      // child(parent of the grandchild) process waits for child process and uses the outcome of previous command(listdir -a) for the command "grep"
      else if(current_pid1>0) {
        // parent process waits for its child
        current_pid1 = wait(NULL);
        // closes the unused end
        close(fd[1]); 
        // redirects standard input to fd[0]      
        dup2(fd[0], 0);
        
        // executes grep command on the outcome
        execvp("grep", (char**) grep_args);     
        
      } else {
        perror("fork failed");
      }

      // exits the child(parent of grandparent) process, to go back to the parent process
      exit(0);
    }

    // if the command contains ">" symbol, it's a pipe. Child and grandchild processes handles the pipe process
    else if(input.find(">")!=string::npos) {

      // starts pipe, returns 1 if it fails
      if (pipe(fd) == -1) {
        cout << "pipe failed" << endl;
        return 1;
      }

      // creates child process(child of the child)
      current_pid1 = fork();

      if(current_pid1==0) {
        // Closes the unused end
        close(fd[0]);
        // redirects standard output to fd[1]
        dup2(fd[1], 1); 
        
        // creates an array for printfile command with pipe, which will be used in execvp function
        char const * const cat_args[3] = {"cat", newCommand[1].c_str(), NULL};
        execvp(cat_args[0], (char**) cat_args);
      }
      else if(current_pid1>0) {
        // parent process waits for its child
        current_pid1 = wait(NULL);
        // Closes the unused end
        close(fd[1]);  
        // redirects standard input to fd[0]  
        dup2(fd[0], 0);
        // Reads the message sent by the child
        read(fd[0], read_msg, sizeof(read_msg));

        // writes to the file, opens a new file if it doesn't exist       
        FILE *file = fopen(newCommand[3].c_str(), "w");
        int results = fputs(read_msg, file);
        fclose(file);   
      }
      // returns 1 if fork fails
      else{
        perror("fork failed");
        return 1;
      }

      // exits the child(parent of grandparent) process, to go back to the parent process  
      exit(0); 
    } 

		
    // executes listdir -a command, which is "ls -a" in bash
    if(find(newCommand.begin(), newCommand.end(), "listdir") != newCommand.end() && find(newCommand.begin(), newCommand.end(), "-a") != newCommand.end()) {    
                                                                                                                                     
     execvp( listdir_a[0], (char**) listdir_a);

    // executes listdir command, which is "ls" in bash
    } else if(find(newCommand.begin(), newCommand.end(), "listdir") != newCommand.end()) {   
                                                                                                                                                
      execvp( listdir[0], (char**) listdir);

    // executes printfile command, which is "cat" in bash
    } else if(find(newCommand.begin(), newCommand.end(), "printfile") != newCommand.end() && find(newCommand.begin(), newCommand.end(), ">") == newCommand.end()) {

      execvp(printFile[0], (char**) printFile);

    // executes currentpath command, which is "pwd" in bash
    } else if(find(newCommand.begin(), newCommand.end(), "currentpath") != newCommand.end()) {  
                                                                                                                            
      execvp( currentpath[0], (char**) currentpath);  

    // executes footprint command, which is history in bash
    } else if(find(newCommand.begin(), newCommand.end(), "footprint") != newCommand.end()) { 
      // outputs history number and corresponding command, also deals with the case in which there're more than 15 commands, outputs the last 15 commands at most
      if(commands.size()==0) {
        cout << "1 footprint" << endl;
      } else if(commands.size()<=14) {                                                                                                                               
        for(int i=0; i<commands.size(); i++) {
          cout << i+1 << " " << commands[i] << endl;
        }
        // last command is "footprint"
        cout << commands.size()+1 << " " << "footprint" << endl; 
      } else {
        for(int i=commands.size()-14; i<commands.size(); i++) {
          cout << i+1 << " " << commands[i] << endl;
        }
        // last command is "footprint"
        cout << commands.size()+1 << " " << "footprint" << endl; 
      }

      // goes to parent process
      exit(0);

    // exits(waits for a new command) if command(input) doesn't exist in our shell example
    } else {
      cout << "this command doesn't exist" << endl;
      // goes to parent process
      exit(0);
    }

  // parent process waits for its child, and adds the last written command to the list of commands    
  } else if (current_pid > 0){
    // parent process waits for its child
    current_pid = wait(NULL);
    // adds last written command to the command list
    commands.push_back(input);
 
  // returns 1 if fork failes
  }else{
    perror("fork failed");
    return 1;
  }
}


  return 0; 
}
