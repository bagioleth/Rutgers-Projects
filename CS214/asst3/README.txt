To Run DUMBclient and DUMBserver, first set up DUMBserver with the following command:

./DUMBserve portNumber

where portNumber is the port you want the server to listen for connections.

Then, set up DUMBclient with the following command:

./DUMBclient serverName portNumber

where serverName is the machine running the server (localhost if its the same machine)
and portNumber is the same port number as the server is listening for