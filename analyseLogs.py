from os import listdir, getcwd
from os.path import isfile, join
import sys

mypath = getcwd()
users = [f.split("_")[0] for f in listdir(mypath) if isfile(join(mypath, f)) and f.find("client") != -1 and f.find("lck") == -1]

for u in users:
    f = join(mypath,u+"_client.txt")
    file = open(f,'r')
    lines = file.readlines()
    file.close()
    if("INFO: commands over\n" not in lines):
        print(u+" Client running")
        sys.exit(1)

# validate
roomMode = True if (int(sys.argv[1]) == 1) else False
if not roomMode:
    transactions = []
    for u in users:
        f = join(mypath,u+"_client.txt")
        file = open(f,'r')
        lines = file.readlines()
        file.close()

        #check for exceptions
        exceptionLines = [i for i in lines if i.find("caught") != -1]
        if(len(exceptionLines) != 0):
            print("Exception caught in "+u)
            sys.exit(2)

        # check for messages
        lines = [i for i in lines if i.find("->") != -1]
        messages = [i.split("::")[1].split("(")[0] for i in lines]

        for m in messages:
            senderReceiver = m.split(": ")[0]
            if(senderReceiver not in transactions):
                transactions.append(senderReceiver)

    # print(messageDict.keys())
    for t in transactions:
        sender = t.split("->")[0]
        receiver = t.split("->")[1]

        senderLogFile = open(join(mypath,sender+"_client.txt"), 'r')
        senderLogs = senderLogFile.readlines()
        senderLogs = [i for i in senderLogs if i.find(t) != -1]
        senderMessages = [i.split("::")[1].split("(")[0] for i in senderLogs]
        senderMessages.sort()

        receiverLogFile = open(join(mypath,receiver+"_client.txt"), 'r')
        receiverLogs = receiverLogFile.readlines()
        receiverLogs = [i for i in receiverLogs if i.find(t) != -1]
        receiverMessages = [i.split("::")[1].split("(")[0] for i in receiverLogs]
        receiverMessages.sort()

        if(senderMessages != receiverMessages):
            print("Message error in "+sender+"->"+receiver)
            sys.exit(3)
else:
    messageDict = dict()
    for u in users:
        f = join(mypath,u+"_client.txt")
        file = open(f,'r')
        lines = file.readlines()
        file.close()

        #check for exceptions
        exceptionLines = [i for i in lines if i.find("caught") != -1]
        if(len(exceptionLines) != 0):
            print("Exception caught in "+u)
            sys.exit(2)

        # check for messages
        lines = [i for i in lines if i.find("->") != -1]
        messages = [i.split("::")[1].split("(")[0] for i in lines]
        for m in messages:
            senderReceiver = m.split(": ")[0]
            messageString = m.split(": ")[1]
            if senderReceiver not in messageDict.keys():
                messageDict[senderReceiver] = []
            messageDict[senderReceiver].append(messageString)

    for u in users:
        receivedMessages = messageDict["room1->"+u]
        receivedMessages.sort()

        sentMessages = []
        for v in users:
            if u != v:
                sentMessages = sentMessages + messageDict[v+"->room1"]
        sentMessages.sort()

        if sentMessages != receivedMessages:
            print(set(sentMessages).symmetric_difference(set(receivedMessages)))
            print("Message error in "+u+"->room1")
            sys.exit(3)


# calculate latency
globalMaxLatency = -1
globalMaxLatencyLine = ""
averageMaxLatency = 0.
for u in users:
    f = u + "_client.txt"
    # print(f)
    file = open(f,'r')
    lines = file.readlines()
    file.close()

    lines = [i for i in lines if i.find("->") != -1]
    # print("Number of messages: "+str(len(lines)))
    maxLatency = -1
    maxLatencyLine = ""
    for msg in lines:
        logData = msg.split("INFO: ")[1]
        receivedTime = int(logData.split("::")[0])
        sentTime = int(logData.split("(")[1].split(")")[0])
        observedLatency = receivedTime - sentTime

        if observedLatency < 0:
            print("Negative latency observed in "+msg)
            sys.exit(5)

        if(observedLatency > maxLatency):
            maxLatency = observedLatency
            maxLatencyLine = msg

    # print("Latency: "+str(maxLatency)+" :: "+maxLatencyLine+"\n")
    averageMaxLatency += maxLatency

    if(maxLatency > globalMaxLatency):
        globalMaxLatency = maxLatency
        globalMaxLatencyLine = maxLatencyLine

# print("Overall Latency: "+str(globalMaxLatency)+" :: "+globalMaxLatencyLine+"\n")
averageMaxLatency /= len(users)
f = open("result","w")
f.write(str(averageMaxLatency)+"\n")
f.write(str(globalMaxLatency)+"\n")
sys.exit(0)
# print("Average Max Latency: "+str(averageMaxLatency))
