#include <stdlib.h>
#include <stdio.h>
#include <strings.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <netdb.h>

//Code by Brian Angioletti and Ramit Sharma done on or by 12/11/19 	CS214  Section 7

int connectToServer(struct hostent * server, int portNumber)
{
	int socketfd;
	struct sockaddr_in serverAddress;

	socketfd = socket(AF_INET, SOCK_STREAM, 0);

	if(server == NULL)//no server exists with given name
		return 0;

	bzero((char *) &serverAddress, sizeof(serverAddress));
	serverAddress.sin_family = AF_INET;
	bcopy((char*)server->h_addr, (char*) &serverAddress.sin_addr.s_addr, server->h_length);
	serverAddress.sin_port = htons(portNumber);

	if(connect(socketfd, (struct sockaddr*)&serverAddress, sizeof(serverAddress)) < 0)//connection failed
		return 0;
	
	return socketfd;
}


int main(int argc, char** argv)
{
	if(argc != 3)
	{
		printf("Incorrect number of arguements\n");
		return 0;
	}

//========================establishing connection===================================

	struct hostent *server;
	int portNumber, socketfd, x;
	char numBytes[10];
	int numChars = 0;

	server = gethostbyname(argv[1]);
	portNumber = atoi(argv[2]);

	socketfd = connectToServer(server, portNumber);
	if(socketfd == 0)
	{
		printf("Connection failed\n");
		return 0;
	}

//=========================connection established===================================


	char input[256];
	char output[256];
	bzero(input, 256);
	bzero(output, 256);
	
	write(socketfd, "HELLO", 5);
	read(socketfd, input, 255);
	if(strcmp(input, "HELLO DUMBv0 ready!") == 0)
	{
		printf("You are connected to the server\n");
	}
	else
	{
		printf("Connection to server failed\n");
		close();
	}

	while(1)//infinite loop until a ended by a "quit" being entered
	{
		printf("> ");
		
		bzero(input, 256);
		fgets(input, 255, stdin);//taking only 255 bytes garentees last character is null terminator
		

		if(strcmp(input, "quit\n") == 0)//\n is neccesary
		{
			write(socketfd, "GDBYE", 5);
			if(read(socketfd, input, 255) <= 0)
			{
				printf("Sucessfully disconnected\n");
				close(socketfd);
				break; 
			}
			else
			{
				printf("%s\n", input);
				printf("Failed to disconnect\n");
			}
		}

//------------------------------CREAT------------------------------------------

		else if(strcmp(input, "create\n") == 0)
		{
			printf("Okay, what box do you want to create?\ncreate:>");	
			fgets(input, 255, stdin);

			bzero(output, 256);
			sprintf(output, "CREAT %s", input);
			write(socketfd, output, strlen(output));//-1

			bzero(input, 256);
			read(socketfd, input, 255);  	
			if(strcmp(input, "OK!") == 0)	
			{
				printf("Message box created.\n");
			}
			else if(strcmp(input, "ER:EXIST") == 0)
			{
				printf("Error: Message box already exists.\n");
			}
			else
			{
				printf("Error: Command unsuccessful. Please try again.\n");
			}
				
		}

//------------------------------DELBX------------------------------------------

		else if(strcmp(input, "delete\n") == 0)
		{
			printf("Okay, what box do you want to delete?\ndelete:>");
		//	bzero(input, 256);		// possibly why delete isnt working	
			fgets(input, 255, stdin);

			bzero(output, 256);
			sprintf(output, "DELBX %s", input);
			write(socketfd, output, strlen(output));//-1

			bzero(input, 256);
			read(socketfd, input, 255);  	
			if(strcmp(input, "OK!") == 0)	
			{
				printf("Message box deleted.\n");
			}
			else if(strcmp(input, "ER:NEXST") == 0)
			{
				printf("Error: Message box with that name doesn't exist.\n");
			}
			else if(strcmp(input, "ER:OPEND") == 0)
			{
				printf("Error: Message box with that name is currently opened by another user.\n");
			}
			else if(strcmp(input, "ER:NOTMT") == 0)
			{
				printf("Error: Message box with that name is not empty of messages.\n");
			}
			else
			{
				printf("Error: Command unsuccessful. Please try again.\n");
			}
	
		}

//------------------------------OPNBX------------------------------------------

		else if(strcmp(input, "open\n") == 0)
		{
			printf("Okay, what box do you want to open?\nopen:>");	
			fgets(input, 255, stdin);

			bzero(output, 255);
			sprintf(output, "OPNBX %s", input);
			write(socketfd, output, strlen(output));//-1

			bzero(input, 255);
			read(socketfd, input, 255);
			if(strcmp(input, "ER:EXTCR") == 0)   			//Extra Credit Error that was not in project description
			{
				printf("Error: A message box is already open.\n");
			}  	
			else if(strcmp(input, "OK!") == 0)	
			{
				printf("Opened message box.\n");
			}
			else if(strcmp(input, "ER:NEXST") == 0)
			{
				printf("Error: Message box with that name doesn't exist.\n");
			}
			else if(strcmp(input, "ER:OPEND") == 0)
			{
				printf("Error: Message box with that name is already opened by another user.\n");
			}
			else
			{
				printf("Error: Command unsuccessful. Please try again.\n");
			}
		}

//------------------------------CLSBX------------------------------------------

		else if(strcmp(input, "close\n") == 0)
		{
			printf("Okay, what box do you want to close?\nclose:>");	
			fgets(input, 255, stdin);

			bzero(output, 256);
			sprintf(output, "CLSBX %s", input);
			write(socketfd, output, strlen(output));//-1

			bzero(input, 256);
			read(socketfd, input, 255);  	
			if(strcmp(input, "OK!") == 0)	
			{
				printf("Message box closed.\n");
			}
			else if(strcmp(input, "ER:NOOPN") == 0)
			{
				printf("Error: Specified message box is not currently open.\n");
			}
			else
			{
				printf("Error: Command unsuccessful. Please try again.\n");
			}
		}

//------------------------------NXTMG------------------------------------------

		else if(strcmp(input, "next\n") == 0)
		{	
			write(socketfd, "NXTMG", 5);
			x = 0;
			bzero(input, 256);
			read(socketfd, input, 3);  	
			if(strcmp(input, "OK!") == 0)	
			{
				bzero(input, 256);
				do
				{
					read(socketfd, input, 1);
					numBytes[x] = *input;
					x++;
				}while(strcmp(input, "!") != 0);
				numBytes[strlen(numBytes) - 1] = 0;
				numChars = atoi(numBytes);
				read(socketfd, input, numChars);
				printf("Message: %s\n", input);
			}
			else
			{
				read(socketfd, input, 5);
				if(strcmp(input, "EMPTY") == 0)
				{
					printf("Error: No messages left in this box.\n");
				}
				else if(strcmp(input, "NOOPN") == 0)
				{
					printf("Error: No message box currently open.\n");
				}
				else
				{
					printf("Error: Command unsuccessful. Please try again.\n");
				}
			}
			
		}

//------------------------------PUTMG------------------------------------------

		else if(strcmp(input, "put\n") == 0)
		{
			printf("Okay, what message do you want to put?\nmessage:>");
			fgets(input, 255, stdin);

			bzero(output, 256);
			sprintf(output, "PUTMG!%d!%s", strlen(input), input);
			write(socketfd, output, strlen(output));//-1

			bzero(input, 256);
			read(socketfd, input, 3);  	
			if(strcmp(input, "OK!") == 0)	
			{
				printf("Message stored.\n");
			}
			else
			{
				read(socketfd, input, 5);
				if(strcmp(input, "NOOPN") == 0)
				{
					printf("Error: No message box currently open.\n");
				}
				else
				{
					printf("Error: Command unsuccessful. Please try again.\n");
				}
			}
		}

//------------------------------help------------------------------------------

		else if(strcmp(input, "help\n") == 0)
		{
			printf("client command help menu:\nquit\ncreate\ndelete\nopen\nclose\nnext\nput\n");
		}
		else
		{
			printf("This is not a command, for a command list enter 'help'.\n");
		}

	}




	return 1;
}
