import numpy as np 
import pandas as pd
import matplotlib.pyplot as plt
import sys
import math
from numpy import log as ln
import random

fileName = "vehicle.csv"
threshold = 0.0001
#stepSizes = [0.4,0.6,0.9]
stepSizes = [0.9]
plotInd = 1
imageName = ""


# sigmoid calculator
def calculateSigmoid(gamma):
    return 1 / (1 + math.exp(-gamma))

def calculateLoss(w,x,y,N):
    loss = 0
    for i in range(N):
        if y[i]=="van":
            yv=-1
        else:
            yv=1 
        loss = loss + ln(1+math.exp(-yv*np.dot(w.transpose(),x[i].transpose())))
    return loss/N               

def calculateMiniGradient(stepSize, w,x,y,N):
    randList = list(range(0, len(x)))
    pointInd = 0
    while len(randList)>0:
        pointInd = random.choice(randList)
        randList.remove(pointInd)
        res = np.zeros((len(w), 1)).transpose()
        yv = 0
        if y[pointInd]=="van":
            yv=-1
        else:
            yv=1              
        res =  res + yv*np.dot(x[pointInd].transpose(),(stepSize / (1 + math.exp(yv*np.dot(w.transpose(),x[pointInd].transpose())))))
        res = res.transpose()
        w = w+res

    return w

def calculateGradient(w,x,y,N):
    gradient = np.zeros((len(w), 1)).transpose()
    yv = 0
    for i in range(N):
        if y[i]=="van":
            yv=-1
        else:
            yv=1             
        gradient = gradient - yv*np.dot(x[i].transpose(),calculateSigmoid(-yv*np.dot(w.transpose(),x[i].transpose())))
    gradient= gradient.transpose()
    return gradient/N  

df = pd.read_csv(fileName, sep=",", header=None)
x = df.to_numpy()
x = np.delete(x, (0), axis=0)          # deleting the first row (names of columns)
x = x[x[:,len(df.columns)-1]!="bus"]   # ignoring "bus" class and its sample data
x = x[x[:,len(df.columns)-1]!="opel"]  # ignoring "opel" class and its sample data
y = x[:,len(df.columns)-1]             # getting y values
x = np.delete(x, np.s_[len(df.columns)-1], axis=1)  # deleting y values from data matrix
x = np.hstack((np.ones((x.shape[0],1)),x))
x = x.astype(float) # making data values float
N = len(x)  # the number of samples
F = len(x[0])  # the number of features
w = np.zeros((F, 1))
numRows,numCols = np.shape(x)
foldRow = int(numRows/5)


# dividing data into 5 groups
batches = []
ybatches = []
group1 = x[0:foldRow*1, :]
y1 = y[0:foldRow*1]
batches.append(group1)
ybatches.append(y1)
group2 = x[foldRow*1:foldRow*2, :]
y2 = y[foldRow*1:foldRow*2]
batches.append(group2)
ybatches.append(y2)
group3 = x[foldRow*2:foldRow*3, :]
y3 = y[foldRow*2:foldRow*3]
batches.append(group3)
ybatches.append(y3)
group4 = x[foldRow*3:foldRow*4, :]
y4 = y[foldRow*3:foldRow*4]
batches.append(group4)
ybatches.append(y4)
group5 = x[foldRow*4:numRows-1, :]
y5 = y[foldRow*4:foldRow*5]
batches.append(group5)
ybatches.append(y5)

# returns trainx, trainy, testx, testy for a fold
def foldData(index):
    if index==0:
        trainx = np.vstack((np.vstack((batches[1],batches[2])),np.vstack((batches[3],batches[4]))))
        trainy = np.concatenate([ybatches[1],ybatches[2],ybatches[3],ybatches[4]])        
    elif index==1:
        trainx = np.vstack((np.vstack((batches[0],batches[2])),np.vstack((batches[3],batches[4]))))
        trainy = np.concatenate([ybatches[0],ybatches[2],ybatches[3],ybatches[4]])   
    elif index==2:
        trainx = np.vstack((np.vstack((batches[0],batches[1])),np.vstack((batches[3],batches[4]))))
        trainy = np.concatenate([ybatches[0],ybatches[1],ybatches[3],ybatches[4]])
    elif index==3:
        trainx = np.vstack((np.vstack((batches[0],batches[1])),np.vstack((batches[2],batches[4]))))
        trainy = np.concatenate([ybatches[0],ybatches[1],ybatches[2],ybatches[4]])   
    elif index==4:
        trainx = np.vstack((np.vstack((batches[0],batches[1])),np.vstack((batches[2],batches[3]))))
        trainy = np.concatenate([ybatches[0],ybatches[1],ybatches[2],ybatches[3]])

    return trainx, trainy, batches[index], ybatches[index]    # trainx, trainy, testx, testy

# normalizing data
for i,row in enumerate(x):
   x[i] = (x[i] - x[i].min()) / (np.ptp(x[i]))



if sys.argv[1]=="part1" and sys.argv[2]=="step1":
    imageName = "part1_step1.png"
    for i,stepSize in enumerate(stepSizes):
        avgLoss = 0
        avgIterateNum = 0
        nums = []
        losses = []
        for index in range(0,5):
            fnums = []
            flosses = []
            dataarray = foldData(index)  
            N = len(dataarray[0]) 
            w = np.zeros((F, 1))
            iloss = calculateLoss(w,dataarray[0],dataarray[1],N)
            nums.append(1)
            losses.append(iloss)
            iterateNum=1
            difference = 1  

            while(difference>threshold):
                gradient = calculateGradient(w,dataarray[0],dataarray[1],N)
                w = np.dot(stepSize,-gradient) + w
                loss = calculateLoss(w,dataarray[0],dataarray[1],N)
                difference = abs(iloss-loss)
                iloss = loss
                iterateNum=iterateNum+1
                flosses.append(loss)
                fnums.append(iterateNum) 
            

            #print(str(loss) + " is the trait loss for step size: " + str(stepSize) + " at fold " + str(index))
            N = len(dataarray[2])
            loss = calculateLoss(w,dataarray[2],dataarray[3],N)
            #print(str(loss) + " is the test loss for step size: " + str(stepSize) + " at fold " + str(index))
            #print(str(iterateNum) + " is the iteration number for step size: " + str(stepSize) + " at fold " + str(index))
            avgLoss = avgLoss + loss
            avgIterateNum = avgIterateNum + iterateNum
            if index==1:
                plt.figure(plotInd)
                plotInd = plotInd + 1
                plt.title("Loss Plot For Fold 1 \n Step Size: " + str(stepSize))
                plt.xlabel("Iteration")
                plt.ylabel("Loss Value")
                plt.plot(fnums, flosses, c='green')
                if stepSize==0.9:
                    plt.savefig(imageName, dpi=300, bbox_inches='tight', format="png")         
                
        print("average loss for step size " + str(stepSize) + " is " + str(avgLoss/5))
        print("average iterate number for step size " + str(stepSize) + " is " + str(avgIterateNum/5.0))
        
       
    plt.show()
        

elif sys.argv[1]=="part1" and sys.argv[2]=="step2":  
    imageName = "part1_step2.png" 
    for i,stepSize in enumerate(stepSizes):
        avgLoss = 0
        avgIterateNum = 0
        nums = []
        losses = []
        for index in range(0,5):
            fnums = []
            flosses = []
            dataarray = foldData(index) 
            N = len(dataarray[0]) 
            w = np.zeros((F, 1))
            iloss = calculateLoss(w,dataarray[0],dataarray[1],N)
            fnums.append(1)
            flosses.append(iloss)
            iterateNum=1
            difference = 1 

            while(difference>threshold):
                w = calculateMiniGradient(stepSize,w,dataarray[0],dataarray[1],N)
                loss = calculateLoss(w,dataarray[0],dataarray[1],N)
                difference = abs(iloss-loss)
                iloss = loss
                iterateNum=iterateNum+1
                flosses.append(loss)
                fnums.append(iterateNum) 

            #print(str(loss) + " is the trait loss for step size: " + str(stepSize) + " at fold " + str(index))
            N = len(dataarray[2]) 
            loss = calculateLoss(w,dataarray[2],dataarray[3],N)
            #print(str(loss) + " is the test loss for step size: " + str(stepSize) + " at fold " + str(index))
            #print(str(iterateNum) + " is the iteration number for step size: " + str(stepSize) + " at fold " + str(index))
            avgLoss = avgLoss + loss
            avgIterateNum = avgIterateNum + iterateNum
            if index==1:
                plt.figure(plotInd)
                plt.title("Loss Plot For Fold 1 \n Step Size: " + str(stepSize))
                plt.xlabel("Iteration")
                plt.ylabel("Loss Value")
                plotInd = plotInd + 1
                plt.plot(fnums, flosses, c='green')
                if stepSize==0.9:
                    plt.savefig(imageName, dpi=300, bbox_inches='tight', format="png") 
                
        print("average loss for step size " + str(stepSize) + " is " + str(avgLoss/5))
        print("average iterate number for step size " + str(stepSize) + " is " + str(avgIterateNum/5.0)) 
    plt.show()    



 
