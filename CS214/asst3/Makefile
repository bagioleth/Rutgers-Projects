CC = gcc
CFLAGS = -g -Wall -I

all: serve client

client: DUMBclient.c
	$(CC) $(FLAGS) -o DUMBclient DUMBclient.c

serve: DUMBserver.c
	$(CC) $(FLAGS) -o DUMBserve DUMBserver.c -lpthread

clean:
	-rm -f *.o $(objects) DUMBclient DUMBserve

                             

