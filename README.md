# Programming Assignment 4: Chatty Chat Chat

## Goals

In order to complete this assignment, you will need to:
- Understand how to model network connections using sequence diagrams
  - Describe the operations required for a complete network interaction.
  - Model the communications between client and server processes, and between the threads supporting those processes.
- Understand how to use Java sockets to create and sustain network connnections.
  - Create a server that accepts multiple simultaneous connections.
  - Implement a design to handle interactions with many clients at once.
- Understand how to use Java threads to allow for nondeterministic execution.
  - Allow for both client and server processes to handle multiple simultaneous tasks.
  - Use proper object locks and/or wait-notify signaling to collaborate on a shared object.

## Description

In this assignment you will implement your very own internet chat protocol -- the ChattyChatChat protocol (CCC). The protocol governs how a single ChattyChatChat server mediates connections between any number of ChattyChatChat clients as they communicate with each other.

Implementing this protocol will require that you create at least two Java classes:
- The ChattyChatChatServer, which is run on a single computer at a specified port and receives connections from clients.
- The ChattyChatChatClient, which is run by each client computer and connects to the server.

#### Boring Historical Context

The idea of text-based messaging has been around (almost) as long as networked computers have, with early bullitin board servers (BBS) and Usenet protocols allowing threaded discussion. The first widely-used real-time chat protocol was [Internet Relay Chat (IRC)](https://en.wikipedia.org/wiki/Internet_Relay_Chat), which was developed in 1988 and [standardized in 1993](https://tools.ietf.org/html/rfc1459).

## Specification and requirements

### Specification

Your implementation of the ChattyChatChat protocol requires that you write two programs -- a ChattyChatChat server and a ChattyChatChat client.

#### ChattyChatChatServer

The server program must be a class named `ChattyChatChatServer`; this program should accept a single command-line argument describing the port for the server to listen on. For example, to start the server and have it listen to port `9876`, the command-line invocation would be:
```
java ChattyChatChatServer 9876
```
A single instance of the ChattyChatChat server will server as the common point of connection for all clients wanting to interact with the chat server.

#### ChattyChatChatClient

The client program must be a class named `ChattyChatChatClient`; this program should accept two command-line arguments describing the server name and port to connect to. For example, to start a client and connect to a server running on port `9876` on `cs-class`, the command-line invocation would be:
```
java ChattyChatChatClient cs-class.uis.georgetown.edu 9876
```
Note that the server must be running in order for any client to successfully connect.

#### Testing hostnames, ports, and IDE settings

You may choose to test your server/client code locally (with both processes on the same machine). A few tips for doing so are provided below:
- If you are running your server and client on your local machine, you can instead use the hostname `localhost` which is short-hand for the IP address `127.0.0.1` and refers to the currrent machine.
- If you are running your code from within an IDE, you will need to specify the command-line arguments to pass when your program is invoked. To do this in Eclipse:
  - Open Eclipse chose the menu option "Run -> Run Configurations"
  - In the list on the left, locate your program (either ChattyChatChatClient or ChattyChatChatServer).
  - Once selected (so the class name appears to the right in the "Name" field) click the "Arguments" tab just below.
  - In the "Program arguments" box, enter everything that would come *after* the invocation `java ChattyChatChatClient`

Note that, in Java, the command-line arguments are provided in the parameter `String[] args` given to main; unlike C++, the first element of this array is the first *true* command-line argument (i.e., no worrying about the name of the command in this array).  

#### Chat protocol and commands

The communication protocol for the chat clients and server should obey the following rules:
- A "normal" message is text sent by one client to the server; this message should be relayed to all other clients, who will print it to standard out (e.g., using `System.out.println()`) upon receipt.
- The CCC protocol also provides the following *chat commands* which should be interpreted by the server to perform a special task:
  - `/nick <name>` : Set this client's nickname to be the string `<name>`. For example:
  ```
  /nick cosc150student
  ```
  would set the user's nickname to `cosc150student`.
    - The nickname command may be used more than once per session by any user; the current nickname is retained unless/until a subsequent `/nick` command is received.
    - Nicknames do *not* need to be unique on the server.
    - Nicknames are single-words and do not contain spaces.
    - Any additional characters beyond the first word may be ignored; that is, the above and below commands would have identical effect:
  ```
  /nick cosc150student these words may be discarded
  ```
  - `/dm <name> <msg>` : Send a message to user(s) with the specified nickname. For example:
  ```
  /dm cosc150student This is a "secret" message
  ```
  should deliver the message "This is a "secret" message" only to user(s) who have the nickname "cosc150student".
    - Only clients with the correct nickname should receive this message; nothing should be sent to any other clients.
    - If no client has the specified nickname, this message may be ignored.
    - If multiple clients have the specified nickname, *all* of them should receive the message.
  - `/quit` : Disconnect from the server and end the client program.
    - When a client enters this message, it should still be sent to the server as a notice that the client will disconnect; the server may then safely close this socket connection and clean-up details related to the client.
    - The client program should disconnect, clean up, and end when this string is entered.
    - Any additional characters after the `/quit` may be safely ignored.
- Any other input (including one beginning with a slash, but not exactly matching the above) should be considered a regular message.

#### A helpful String member method

In implementing the chat commands above, you may be interested in reading about the `String.split()` member method, which parses a `String` into a `String[]` array on a specified delimiter (e.g., a space). For full details, see [the Java documentation](https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/lang/String.html#split(java.lang.String,int)).


### Requirements

You will create the following design document files and submit them via Canvas:
- `ClassDiagram.pdf`, a UML class diagram which models the structure of your chat software, including both client-side and server-side classes.
  - This diagram will certainly contain at least two classes, named `ChattyChatChatClient` and `ChattyChatChatServer`
  - It should also contain any additional classes (including any inner or anonymous classes) you use to supplement the function of the two classes above.
- `Sequence Diagram.pdf`, a UML sequence diagram describing the following use case:
  - At the beginning of the diagram, the CCC Server is running and two clients are connected.
  - Client 1 sends a message to the server, which is properly routed to client 2.
  Your diagram should demonstrate how the message is routed from client 1 and arrives at client 2. Think about which class(es) on client 1's computer interact with the message; which class(es) on the server are involved, and which class(es) on client 2's computer receive the message. *Even within a single thread of execution, methods from more than one class may be invoked in this process*.

These diagrams will be *hand-graded* by me after the design document due date; the Travis script will only test for their existence.

You will also create the following Java source code files and commit them to your repository:
- `ChattyChatChatServer.java`, which contains a `main()` method and serves as the program to run on the server.
- `ChattyChatChatClient.java`, which contains a `main()` method and serves as the program to run on the clients.
You should also commit any additional `.java` source files which support the two above (this will depend on your implementation of the programs).      
