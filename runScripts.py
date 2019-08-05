import time
import subprocess
from subprocess import Popen, PIPE
import sys

roomMode = int(sys.argv[1])

#run commands
resultF = open("results.txt",'w')
for sThreads in [1]:#,2,3,5,7]:
    print("Number of threads : "+str(sThreads))
    for clientNum in [2]:#,5,10,15,20,25]:
        print("Number of clients : "+str(clientNum))
        resultF.write("Threads = "+str(sThreads)+", Clients = "+str(clientNum)+"\n")
        for i in range(1):#7):
            commandGenerator = "generateMulticastCommands.py" if (roomMode == 1) else "generateCommands.py"
            subprocess.call(["python", commandGenerator, str(sThreads), str(clientNum)])
            time.sleep(2)
            subprocess.call(["sh", "test.sh"])

            timePerClient = 20
            sleepTime = min(timePerClient*clientNum, 180)
            time.sleep(sleepTime)

            print("Analysing logs...")
            analyse = subprocess.call(["python", "analyseLogs.py", str(roomMode)])
            while analyse != 0:
                if analyse == 1:
                    time.sleep(10)
                    print("Analysing logs...")
                    analyse = subprocess.call(["python", "analyseLogs.py", str(roomMode)])
                else:
                    print("ERROR "+str(analyse))
                    sys.exit(1);

            f = open("result","r")
            lines = f.readlines()
            print("Reading "+str(i+1)+": Avg "+lines[0].split("\n")[0]+", Max "+lines[1].split("\n")[0])
            resultF.write("Reading "+str(i+1)+": Avg "+lines[0].split("\n")[0]+", Max "+lines[1].split("\n")[0]+"\n")
            subprocess.call(["sh", "clean.sh"])
            subprocess.call(["rm", "result"])
            time.sleep(2)
        resultF.write("===============================================================\n")
resultF.close()
