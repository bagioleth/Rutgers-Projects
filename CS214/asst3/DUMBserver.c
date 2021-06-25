#include <stdlib.h>
#include <stdio.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <strings.h>
#include <pthread.h>
#include <time.h>

//All code in this file has been completed by Brian Angioletti and Ramit Sharma on or before the due date 
//of 12/11/19

typedef struct _threadArgs//keeps track of socket descripter and ip address which is thread specific
{
	int socketfd;
	char* ipAddr;
}threadArgs;

typedef struct _message//stores messages in a linked list
{
	char * content;
	struct _message * prevMessage;
	struct _message * nextMessage;
}message;

typedef struct _messageBox//Boxes are a linked list stores messages in a queue
{
	char* name;
	struct _messageBox * prevBox;
	struct _messageBox * nextBox;
	struct _message * firstMessage;
	struct _message * lastMessage;
}messageBox;

static messageBox* firstBox = NULL;
pthread_mutex_t lock;
//static time_t t = NULL;//time is not a thing in this project as it overwrites the structure for some unkown reason

messageBox* boxExistsWithName(char* boxName)//checks if there is a box with the given name, returns a pointer to that box
{
	//pthread_mutex_lock(&lock);
	if(firstBox == NULL)
	{
		//pthread_mutex_unlock(&lock);
		return NULL;
	}
	
	messageBox * box = firstBox;

	while (box != NULL)
	{
		if(strcmp((*box).name, boxName) == 0)
		{
			//pthread_mutex_unlock(&lock);
			return box;
		}
		box = (*box).nextBox;
	}	
	//pthread_mutex_unlock(&lock);
	return NULL;
}

void* handleClient(void* args)//thread method to handle client
{
	int socketfd;
	socketfd = (*(threadArgs*)args).socketfd;
	char* ip = (char*)malloc(20);//more than enough for an ip address
	strcpy(ip, (*(threadArgs*)args).ipAddr);
	//printf("%s\n", ip);
	
	//initialize all varialbes used in function

	messageBox* openedBox = malloc(sizeof(messageBox));
	messageBox* tempBox = malloc(sizeof(messageBox));
	openedBox = NULL;
	tempBox = NULL;

	char* temp;
	char input[256];
	char output[256];
	int numBytes, x;
	char numByte[10];
	int numChars = 0;
	bzero(input, 256);
	bzero(output, 256);
	read(socketfd, input, 5);
	if(strcmp(input, "HELLO") == 0)//connection setup
	{
		write(socketfd, "HELLO DUMBv0 ready!", 19);
		printf("%s\tconnected\n", ip);
		printf("%s\tHELLO\n", ip);
	}

	//mutex locks make sure only one thread is processing a command at a time

	pthread_mutex_lock(&lock);
	while(1)
	{
		pthread_mutex_unlock(&lock);
		bzero(input, 256);
		read(socketfd, input, 5);
		
		pthread_mutex_lock(&lock);
		if(strcmp(input, "GDBYE") == 0)
		{
			printf("%s\tGDBYE\n", ip);
			printf("%s\tdisconnected\n", ip); 
			close(socketfd);
			pthread_mutex_unlock(&lock);
			break;
		}

//-----------------------------Create-------------------------------------------------

		else if(strcmp(input, "CREAT") == 0)
		{
			bzero(input, 256);
			read(socketfd, input, 1);		//ignores the space
			if(*input != ' ')
			{
				printf("%s\tER:WHAT?\n", ip);
				write(socketfd, "ER:WHAT?", 8);
				continue;
			}

			bzero(input, 256);
			read(socketfd, input, 255);		

			if(boxExistsWithName(input))
			{
				printf("%s\tER:EXIST\n", ip);
				write(socketfd, "ER:EXIST", 8);
				continue;			//unnesesary  but makes me feel like the code is more complete
			}
			else if(strlen(input) > 25 || strlen(input) < 5 || ((input[0] > 90 || input[0] < 65) && (input[0] > 122 || input[0] < 97)))
			{
				printf("%s\tER:WHAT?\n", ip);
				write(socketfd, "ER:WHAT?", 8);
				continue;
			}
			else //sets the head of the list of boxes to be the newly created box
			{
				messageBox* newBox;
				newBox = (messageBox*) malloc(sizeof(messageBox));
				char* newName;
				newName = (char*) malloc(sizeof(newName) * 26);
				newName = strcpy(newName, input);
				(*newBox).name = newName;
				(*newBox).firstMessage = NULL;
				(*newBox).lastMessage = NULL;
				(*newBox).nextBox = NULL;
				(*newBox).prevBox = NULL;
				if(firstBox != NULL)
				{
					(*firstBox).prevBox = newBox;
					(*newBox).nextBox = firstBox;
				}
				firstBox = newBox;
				printf("%s\tCREAT\n", ip);
				write(socketfd, "OK!", 3);
				continue;
			}
		}

//-----------------------------Open-------------------------------------------------

		else if(strcmp(input, "OPNBX") == 0)
		{
			bzero(input, 256);
			read(socketfd, input, 1);	//gets rid of the space
      			if(*input != ' ')
			{
				printf("%s\tER:WHAT?\n", ip);
				write(socketfd, "ER:WHAT?", 8);
				continue;
			}
			
			bzero(input, 256);
			read(socketfd, input, 255);

              
			//EEEXXXTTTRRRAAA   CCCRRREEEDDDIIITTT			
			
			if(openedBox != NULL)//a box is already opened
			{
				printf("%s\tER:EXTCR\n", ip);
				write(socketfd, "ER:EXTCR", 8);
				continue;
			}
                        else if(boxExistsWithName(input))
                        {
				openedBox = boxExistsWithName(input);
				printf("%s\tOPNBX\n", ip);
                                write(socketfd, "OK!", 3);
                                continue;           
                        }
			else if(strlen(input) > 25 || strlen(input) < 5 || ((input[0] > 90 || input[0] < 65) && (input[0] > 122 || input[0] < 97)))
			{
				printf("%s\tER:WHAT?\n", ip);
				write(socketfd, "ER:WHAT?", 8);
				continue;
			}
                        else //box doesn't exists
                        {	
				printf("%s\tER:WHAT?\n", ip);
				write(socketfd, "ER:NEXST", 8);
                                continue;
                        }
		}

//-----------------------------Next-------------------------------------------------

		else if(strcmp(input, "NXTMG") == 0)
		{
			if(openedBox != NULL)
			{
				if((*openedBox).lastMessage == NULL)//no messages left
				{
					printf("%s\tER:EMPTY\n", ip);
					write(socketfd, "ER:EMPTY", 8);
					continue;
				}
				else//success
				{	
					bzero(output, 256);
					sprintf(output, "OK!%d!%s", strlen((*(*openedBox).lastMessage).content), (*(*openedBox).lastMessage).content); //doesnt like this line seg faults
					printf("%s\tNXTMG\n", ip);
					write(socketfd, output, strlen(output)); 				//should write "OK!5!Hello" for example
					(*openedBox).lastMessage = (*(*openedBox).lastMessage).prevMessage;
					if((*openedBox).lastMessage == NULL)
						(*openedBox).firstMessage = NULL;
					continue;
				}
			}
			else	//no box is opened
			{
				printf("%s\tER:NOOPN\n", ip);
				write(socketfd, "ER:NOOPN", 8);
				continue;
			}
		}

//-----------------------------Put-------------------------------------------------

		else if(strcmp(input, "PUTMG") == 0)
		{
			bzero(input, 256);
			read(socketfd, input, 1);
			if(*input != '!')
                        {
				read(socketfd, input, 255);
				printf("%s\tER:WHAT?\n", ip);
                                write(socketfd, "ER:WHAT?", 8);
                                continue;
                        }
			x = 0;
                        do{
				read(socketfd, input, 1);
				numByte[x] = *input;
				x++;
			}while(strcmp(input, "!") != 0);

			numByte[strlen(numByte) - 1] = '\0';
			numChars = atoi(numByte);
			bzero(input, 256);
			read(socketfd, input, numChars + 10);

			if(openedBox == NULL)
			{
				printf("%s\tER:NOOPN\n", ip);
				write(socketfd, "ER:NOOPN", 8);
				continue;
			}
			else
			{
				tempBox = openedBox;

				if((*tempBox).firstMessage == NULL)
				{
					(*tempBox).firstMessage = (message*) malloc(sizeof(message) + 10);
					(*tempBox).lastMessage = (*tempBox).firstMessage;//start is the same as end when length = 1
	                                
					char* message = (char*)malloc(numChars + 10);
                                        strcpy(message, input);
					(*(*tempBox).firstMessage).content = message;
					printf("%s\tPUTMG\n", ip);
                                        write(socketfd, "OK!", 3);
                                        continue;

				}else
				{
					message* newMessage = (message*) malloc(sizeof(message));
					(*(*tempBox).firstMessage).prevMessage = newMessage;
                        	        (*newMessage).nextMessage = (*tempBox).firstMessage;
                        	        (*tempBox).firstMessage = newMessage;

					char* message = (char*)malloc(numChars + 10);
                                        strcpy(message, input);
					(*(*tempBox).firstMessage).content = message;
					printf("%s\tPUTMG\n", ip);
                                        write(socketfd, "OK!", 3);
                                        continue;
				}
			}

		}

//-----------------------------Delete-------------------------------------------------

		else if(strcmp(input, "DELBX") == 0)
		{
                        bzero(input, 256);
                        read(socketfd, input, 1);       //gets rid of the space
      			if(*input != ' ')
			{
				printf("%s\tER:WHAT?\n", ip);
				write(socketfd, "ER:WHAT?", 8);
				continue;
			}

			bzero(input, 256);
			read(socketfd, input, 255);

                        if(boxExistsWithName(input))
                        {
                                tempBox = boxExistsWithName(input);
				if((*tempBox).firstMessage != NULL)
				{
					printf("%s\tER:NOTMT\n", ip);
					write(socketfd, "ER:NOTMT", 8);
					continue;
				}
				else//success
				{
					if((*tempBox).prevBox == NULL && (*tempBox).nextBox  == NULL)
					{
						firstBox = NULL;
					}
					else
					{
						if((*tempBox).prevBox != NULL)
						{
							(*(*tempBox).prevBox).nextBox = (*tempBox).nextBox;
						}
						if((*tempBox).nextBox != NULL)
                                	        {
						        (*(*tempBox).nextBox).prevBox = (*tempBox).prevBox;
						}
					}
					printf("%s\tDELBX\n", ip);
					write(socketfd, "OK!", 3);
					continue;				
				}
                        }
			else if(strlen(input) > 25 || strlen(input) < 5 || ((input[0] > 90 || input[0] < 65) && (input[0] > 122 || input[0] < 97)))
			{
				printf("%s\tER:WHAT?\n", ip);
				write(socketfd, "ER:WHAT?", 8);
				continue;
			}
                        else //box doesn't exists
                        {
				printf("%s\tER:NEXST\n", ip);
                                write(socketfd, "ER:NEXST", 8);
                                continue;
                        }

		}

//-----------------------------Close-------------------------------------------------

		else if(strcmp(input, "CLSBX") == 0)
		{
			bzero(input, 256);
			read(socketfd, input, 1);
			if(*input != ' ')
			{
				printf("%s\tER:WHAT?\n", ip);
				write(socketfd, "ER:WHAT?", 8);
				continue;
			}

			bzero(input, 256);
			read(socketfd, input, 255);

			if(openedBox != NULL && strcmp((*openedBox).name, input) == 0)
			{
				openedBox = NULL;
				printf("%s\tCLSBX\n", ip);
				write(socketfd, "OK!", 3);
				continue;
			}
			else if(strlen(input) > 25 || strlen(input) < 5 || ((input[0] > 90 || input[0] < 65) && (input[0] > 122 || input[0] < 97)))
			{
				printf("%s\tER:WHAT?\n", ip);
				write(socketfd, "ER:WHAT?", 8);
				continue;
			}
			else
			{
				printf("%s\tER:NOOPN\n", ip);
				write(socketfd, "ER:NOOPN", 8);
				continue;
			}
		}
		else//message wasnt written properly
		{
			printf("%s\tER:WHAT?\n", ip);
			write(socketfd, "ER:WHAT?", 8);
		}
	}

	return((void*) 1);
}

int main(int argc, char** argv)
{
	if(argc != 2)
	{
		printf("Invalid number of parameters\n");
		return 0;
	}
	int portNum;
	portNum = atoi(argv[1]);//establishes what the port number should be

//CODE SETUP IS BASED OFF OF http://www.cs.rpi.edu/~moorthy/Courses/os98/Pgms/socket.html


//Sets the socket

	int socketfd;
	socketfd = -1;
	while (socketfd < 0)//in case it fails to create a socket (unlikely)
		socketfd = socket(AF_INET, SOCK_STREAM, 0);//stream means TCP i think which is what we want
	printf("Setting up server, please wait...\n");

//Sets up the struct for the address
	
	struct sockaddr_in serverAddress, clientAddress; //struct is defined in netinet/in.h
	bzero((char*) &serverAddress, sizeof(serverAddress)); //sets struct to all zeros(not sure if neccessary)

	serverAddress.sin_family = AF_INET;
	serverAddress.sin_port = htons(portNum);//htons converts portNum to network byte order
	serverAddress.sin_addr.s_addr = INADDR_ANY;

//Binds

	int bindNum;
	bindNum = -1;
	while	(bindNum < 0)
		bindNum = bind(socketfd, (struct sockaddr *) &serverAddress, sizeof(serverAddress)); 
	printf("Server setup complete\n\n");

//Starts listening

	listen(socketfd, 5);

//Handles the clients

	int clientLength;
	int clientfd;
	pthread_t pid;
	threadArgs* tArgs;
	long clientAddressLong;
	char * clientAddressString = (char*)malloc(16);
	clientLength = sizeof(clientAddress); //not sure if necessary to make new variable
	while (1)
	{
		clientfd = accept(socketfd, (struct sockaddr *) &clientAddress, &clientLength);
		if(clientfd == 0)
			continue;
		
		//sets up arguements for the thread(socket and ip address)

		tArgs = (threadArgs*)malloc(sizeof(tArgs));
		(*tArgs).socketfd = clientfd;
		clientAddressLong = (long) (clientAddress).sin_addr.s_addr;
		sprintf(clientAddressString, "%d.%d.%d.%d", 
			(int)((clientAddressLong) & 0xff),
                        (int)((clientAddressLong) >> 8 & 0xff),
                        (int)((clientAddressLong) >> 16 & 0xff),
                        (int)((clientAddressLong) >> 24 & 0xff));
		//printf("%s\n", clientAddressString);
		(*tArgs).ipAddr = clientAddressString;
		pthread_create(&pid, NULL, handleClient, tArgs);
	}


	return 1;
}


