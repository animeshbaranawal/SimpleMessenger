# Simple Messenger
A simple messenger service using Apache Thrift and Java Swing.<br/>
The messenger supports:<br/>
a) P2P messaging<br/>
b) Multicast messaging<br/>
c) File Transfer across clients<br/>

### Details
1. Multicast messaging <br/>
Multi cast messaging is supported in the form of chatrooms. Any message sent to a
chatroom are multicasted to all its members. A client can join or leave a chatroom
through the UI interface. For now, there are 3 chatrooms and visible to all connected users.
Anyone can join. An extension would be to have private chatrooms.

2. File Transfer <br/>
To start a server, it needs a storagePath. To start a client, it needs two paths: inputPath and outputPath.
Files which are present in the inputPath directory can be sent to other clients. They will first be sent to the server
where it will be stored in storagePath directory. File which are received by a client are automatically stored in
outputPath directory.

### Prerequisites

1. java
2. javac
3. thrift

### Installing

IntelliJ was used as IDE support for building the project on Ubuntu18.04<br/>
However, command line can be used as well:<br/>
1. javac -cp .:libs/\* src/\* gen-java/fileTransfer/\* gen-java/simpleMessenger/\*
2. mkdir out; cd out; mkdir fileTransfer; mkdir simpleMessenger; cd ..
3. mv src/\*.class out/
4. mv gen-java/fileTransfer/\*.class out/fileTransfer/
5. mv gen-java/simpleMessenger/\*.class out/simpleMessenger/

## Deployment

To run the server:<br/>
java -cp out/:libs/\* JavaServer \<numServerThreads\> \<storagePath\> <br/><br/>

To run the client:<br/>
java -cp out/:libs/\* JavaClient \<serverIP\> \<clientIP\> \<clientID\> \<clientPort\> \<inputPath\> \<outputPath\> <br/><br/>

If both server and client run on localhost, the client cannot use ports 9089 and 9090 since they are used by the server.<br/>
If two clients run on same IP, they cannot use the same ports.

## Running the tests

JavaClient also supports a non interactive interface, where given a file, it runs all the commands in the file.<br/>
This interface is only used for testing the application via scripts.<br/>

1. runClient.sh : bash script to run client
2. runServer.sh : bash script to run server
3. generateCommands.py : takes two arguments - 1) number of serverThreads 2) number of clients to simulate<br/>
   Generates commands for clients.<br/>
   Runs the server and clients.<br/>
   Generates bash script test.sh to run the simulation.<br/>
   Generates bash script clean.sh to kill the processes and clean the directory.
4. generateMulticastCommands.py : Same as generateCommands.py<br/>
   Generates commands for multicast mode.<br/>
   Runs the server and clients.<br/>
   Generates bash script test.sh to run the simulation.<br/>
   Generates bash script clean.sh to kill the processes and clean the directory.
5. analyseLogs.py : Validates the logs. Takes multicast boolean as argument.
6. runScripts.py : Runs the entire simulation for different clients and different threads. <br/>
   Stores the results of latency in results.txt <br/>
