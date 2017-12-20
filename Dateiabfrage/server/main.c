/*
    C socket server example
*/

#include<stdio.h>
#include<stdlib.h>
#include<unistd.h>
#include<string.h>    //strlen
#include<sys/socket.h>
#include<arpa/inet.h> //inet_addr

typedef int bool;
#define true 1
#define false 0

int createSocket() {
    int socket_desc = socket(AF_INET , SOCK_STREAM , 0);
    if (socket_desc == -1)
    {
        return -1;
    }
    puts("Socket created");
    return socket_desc;
}

void prepareSockAddrIn(struct sockaddr_in *server) {
    server->sin_family = AF_INET;
    server->sin_addr.s_addr = INADDR_ANY;
    server->sin_port = htons( 8888 );
}

int bindSocket(int socket_desc, struct sockaddr_in* server) {
    if( bind(socket_desc,(struct sockaddr *)server , sizeof(*server)) < 0)
    {
        return 1;
    }
    puts("bind done");
    return 0;
}

int fileReadable(char file[]) {
    if (access(file, R_OK) == -1) {
        return false;
    } else {
        return true;
    }
}

void sendErrorFileNotReadable(int clientSocket, char client_message[]) {
    char buffer[200];
    memset(buffer, 0, sizeof buffer);
    strcpy(buffer, "Fehler beim öffnen der Datei/en: ");
    strcat(buffer, client_message);
    strcat(buffer, "\n");
    send(clientSocket, buffer, strlen(buffer), 0);
    memset(buffer, '\0', sizeof buffer);
}

int readNBytesFromFile(int byteCount, char client_message[], char buffer[]) {
    int i = 0;
    bool endOfFile = false;
    int c;
    FILE *pFile = fopen(client_message, "r");
    memset(buffer, '\0', sizeof buffer);

    if (pFile == NULL) {
        fclose(pFile);
        return 1;
    } else {

        while (endOfFile == false && i < byteCount) {
            c = fgetc(pFile);
            if (feof(pFile)) {
                endOfFile = true;
            } else {
                buffer[i] = (char) c;
                i++;
            }
        }
        buffer[i - 1] = '\0';
        fclose(pFile);
        return 0;
    }
}

int main(int argc , char *argv[]) {
    struct sockaddr_in server, client;
    int socket_desc;
    int clientSocket;
    int byteCount;
    char client_message[2000];
    char buffer[2000];
    char* files[2000];

    socket_desc = createSocket();
    if (socket_desc == -1) {
        perror("Could not create socket");
        return 1;
    }
    prepareSockAddrIn(&server);
    if (bindSocket(socket_desc, &server) == 1) {
        perror("bind failed. Error");
        return 1;
    }

    while(true) {
        listen(socket_desc, 5);
        puts("Waiting for incoming connections...");

        int addrSize = sizeof(struct sockaddr_in);
        clientSocket = accept(socket_desc, (struct sockaddr *) &client, (socklen_t *) &addrSize);

        if (clientSocket < 0) {
            perror("accept failed");
            return 1;
        }
        puts("Connection accepted");

        memset(client_message, 0, sizeof client_message);
        recv(clientSocket, client_message, 2000, 0);
        byteCount = atoi(client_message);
        send(clientSocket, "ByteCount", 500, 0);
        memset(client_message, 0, sizeof client_message);

        while ((recv(clientSocket, client_message, 2000, 0) > 0)) {

            printf("Client message: %s\n", client_message);

                printf("token: %s\n", client_message);


                if (!fileReadable(client_message)) {
                    sendErrorFileNotReadable(clientSocket, client_message);

                } else {

                    readNBytesFromFile(byteCount, client_message, buffer);

                    send(clientSocket, buffer, strlen(buffer), 0);
                }

                memset(buffer, 0, sizeof buffer);
                memset(client_message, 0, sizeof client_message);
        }
    }
}