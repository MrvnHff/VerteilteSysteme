/*
    C ECHO client example using sockets
*/
#include<stdio.h> //printf
#include<string.h>    //strlen
#include<sys/socket.h>    //socket
#include<arpa/inet.h> //inet_addr

int main(int argc , char *argv[])
{
    int sock;
    struct sockaddr_in server;
    char filenames[1000] , server_reply[2000];

    //Create socket
    sock = socket(AF_INET , SOCK_STREAM , 0);
    if (sock == -1)
    {
        printf("Could not create socket");
    }
    puts("Socket created");

    server.sin_addr.s_addr = inet_addr("127.0.0.1");
    server.sin_family = AF_INET;
    server.sin_port = htons( 8888 );

    //Connect to remote server
    if (connect(sock , (struct sockaddr *)&server , sizeof(server)) < 0)
    {
        perror("connect failed. Error");
        return 1;
    }

    puts("Connected\n");
    printf("Enter Filenames : ");

    scanf("%s" , filenames);

    //Send some data
    if( send(sock , filenames , strlen(filenames) , 0) < 0)
    {
        puts("Send failed");
        return 1;
    }
    if( recv(sock , server_reply , 2000 , 0) < 0)
    {
        puts("ByteCount recv failed");

    }
    //keep communicating with server
    while(1)
    {

        scanf("%s" , filenames);

        //Send some data
        if( send(sock , filenames , strlen(filenames) , 0) < 0)
        {
            puts("Send failed");
            return 1;
        }
        memset(filenames, '\0', sizeof filenames);

        //Receive a reply from the server
        memset(server_reply, '\0', sizeof server_reply);
        if( recv(sock , server_reply , 2000 , 0) < 0)
        {
            puts("recv failed");
            break;
        }

        puts("Server reply :");
        puts(server_reply);
    }

    shutdown(sock, 2);
    return 0;
}