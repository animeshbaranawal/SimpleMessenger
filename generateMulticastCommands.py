import numpy as np
import random
import string
import sys

letters = string.ascii_lowercase
clients = int(sys.argv[2])
serverThreads = int(sys.argv[1])

## generate usernames
activeUsers = []
for i in range(clients):
    userName = ''.join(random.choice(letters) for i in range(5))
    while(userName in activeUsers):
        userName = ''.join(random.choice(letters) for i in range(5))
    activeUsers.append(userName)

## generate commands
groupID = "room1"
for i in range(clients):
    user = activeUsers[i] ## current user

    f = open(user+"_roomCommands.txt",'w')

    numberOfMessages = random.randint(200,1000)
    for k in range(numberOfMessages):
        messageLength = random.randint(10,30)
        message = ''.join(random.choice(letters) for i in range(messageLength))
        f.write("SENDGROUPMESSAGE "+groupID+" "+message+"\n")

    f.close()

## generate testScript
f = open("test.sh",'w')

f.write("gnome-terminal -- ./runServer.sh "+str(serverThreads)+" /home/animeshbaranawal/Downloads \nsleep 2\n\n")
startingPort = 9091
sleepTime = clients
for user in activeUsers:
    f.write("gnome-terminal -- ./runClient.sh localhost localhost "+user+" "+str(startingPort)+" /home/animeshbaranawal/Downloads/Input /home/animeshbaranawal/Desktop "+user+"_roomCommands.txt "+str(sleepTime)+"\n")
    startingPort += 1
    sleepTime -= 1
f.close()

## generate clean script
f = open("clean.sh",'w')
f.write("kill $(ps aux | grep '[r]unServer.sh' | awk '{print $2}')\n")
f.write("kill $(ps aux | grep '[r]unClient.sh' | awk '{print $2}')\n")
f.write("rm test.sh\n\n")

f.write("mkdir -p logs\n")
f.write("mv server.txt logs/\n")
for user in activeUsers:
    f.write("mv "+user+"_client.txt logs/\n")
    f.write("rm "+user+"_roomCommands.txt\n")
    f.write("rm -rf /home/animeshbaranawal/Downloads/"+user+"\n\n")
f.close()
