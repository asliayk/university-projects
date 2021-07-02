import random
import matplotlib.pyplot as plt
import numpy as np 
import pandas as pd
import sys
import time
import math


if sys.argv[1]=="part1":
    pointNum = 0
    imageName = ""
    if sys.argv[2]=="step1":
        pointNum=50
        imageName = "part1_step1.png"
    elif sys.argv[2]=="step2":    
        pointNum = 100
        imageName = "part1_step2.png"
    elif sys.argv[2]=="step3":    
        pointNum = 5000
        imageName = "part1_step3.png"

    x1 = [random.uniform(-10.0, 10.0) for x in range(pointNum//2)]
    x2 = [random.uniform(-10.0, 10.0) for x in range(pointNum//2)]
    xall = x1 + x2

    y1 = [random.uniform(-50.0, 1-3*x-1) for x in x1]
    y2 = [random.uniform(1-3*x+1,50.0) for x in x2]
    yall = y1 + y2

    def getMisclassifiedPoint(first, second, third):
        for x, y in zip(xall, yall):
            if first*x+second*y+third<=0 and 3*x-1+y>0:
                return x,y,1
            elif first*x+second*y+third>=0 and 3*x-1+y<0:
                return x,y,-1
        return None

    xline = [random.randint(-15, 15) for x in range(25)]
    yline = [-3*x+1 for x in xline]
    """
    plt.figure(0)
    plt.plot(xline, yline, c='green')
    plt.xlabel("x")
    plt.ylabel("y")
    plt.scatter(x1, y1, c='red', s=10)
    plt.scatter(x2, y2, c='blue', s=10)
    """

    first = 0
    second = 0
    third = 0
    misPoint = getMisclassifiedPoint(first,second,third)
    index = 1
    y = 0
    while(misPoint!=None):
        nfirst = misPoint[0]
        nsecond = misPoint[1]
        first = first + nfirst*misPoint[2]
        second = second + nsecond*misPoint[2]
        third = third + 1*misPoint[2]
        x = np.linspace(-15,15,100)
        if second!=0:
            y = (-first*x-third)/second 
        """    
        plt.figure(index)
        plt.plot(x, y, c='purple', label='h(x)')
        plt.plot(xline, yline, c='green', label='f(x)')
        plt.xlabel("x")
        plt.title("DB: " + str(round(first,4)) + "x + " + str(round(second,4)) + "y + " + str(round(third,4)) + " = 0 \n Iteration " + str(index), fontsize =10)
        plt.ylabel("y")
        plt.scatter(x1, y1, c='red', label='0', s=10)
        plt.scatter(x2, y2, c='blue', label='1', s=10)
        plt.legend(loc='best')
        """
        misPoint = getMisclassifiedPoint(first,second,third)
        index=index+1
        if misPoint==None:
            plt.figure(index)
            if second!=0:   
                plt.plot(x, y, c='purple', label='h(x)')
            plt.plot(xline, yline, c='green', label='f(x)')
            plt.xlabel("x")
            plt.title("DB: " + str(round(first,4)) + "x + " + str(round(second,4)) + "y + " + str(round(third,4)) + " = 0 \n Iteration " + str(index), fontsize =10)
            plt.ylabel("y")
            plt.scatter(x1, y1, c='red', label='0', s=10)
            plt.scatter(x2, y2, c='blue', label='1', s=10)
            plt.legend(loc='best')

    plt.savefig(imageName, dpi=300, bbox_inches='tight', format="png")
    plt.show()


elif sys.argv[1]=="part2":

    if sys.argv[2]=="step1":
        fileName = "ds1.csv"
    elif sys.argv[2]=="step2" or sys.argv[2]=="step3":
        fileName = "ds2.csv" 

    if sys.argv[2]!="step3":
        startseconds = int(round(time.time() * 1000))     
        df = pd.read_csv(fileName, sep=",", header=None)
        df = df.apply(pd.to_numeric)
        x = df.to_numpy()
        np.set_printoptions(suppress=True)
        yvalues = x[:,len(df.columns)-1]
        x = np.delete(x, np.s_[len(df.columns)-1], axis=1)
        x = np.hstack((np.ones((x.shape[0],1)),x))
        x_transpose = x.transpose()
        xt_mul_x = np.dot(x_transpose,x)
        xt_mul_x_inverse = np.linalg.inv(xt_mul_x)
        inverse_mul_xtranspose = np.dot(xt_mul_x_inverse,x_transpose)
        w = np.dot(inverse_mul_xtranspose,yvalues)
        print(w)
        res = np.dot(x,w)

        finishseconds = int(round(time.time() * 1000)) 
        print("Time passed: " + str(finishseconds-startseconds) + " msec")

    else:
        startseconds = int(round(time.time() * 1000)) 
        regParams = [300, 320, 340, 360, 1000]
        errors = [[],[],[],[],[]]
        startseconds = int(round(time.time() * 1000))     
        df = pd.read_csv(fileName, sep=",", header=None)
        df = df.apply(pd.to_numeric)
        x = df.to_numpy()
        np.set_printoptions(suppress=True)
        yvalues = x[:,len(df.columns)-1]
        yvalues = yvalues.reshape(-1,1)
        x = np.delete(x, np.s_[len(df.columns)-1], axis=1)
        x = np.hstack((np.ones((x.shape[0],1)),x))
        totRows,totCols = np.shape(x)
        groupRows = int(totRows/5)
        groups = []
        ys = []
        group1 = x[0:groupRows*1, :]
        y1 = yvalues[0:groupRows*1]
        groups.append(group1)
        ys.append(y1)
        group2 = x[groupRows*1:groupRows*2, :]
        y2 = yvalues[groupRows*1:groupRows*2]
        groups.append(group2)
        ys.append(y2)
        group3 = x[groupRows*2:groupRows*3, :]
        y3 = yvalues[groupRows*2:groupRows*3]
        groups.append(group3)
        ys.append(y3)
        group4 = x[groupRows*3:groupRows*4, :]
        y4 = yvalues[groupRows*3:groupRows*4]
        groups.append(group4)
        ys.append(y4)
        group5 = x[groupRows*4:groupRows*5, :]
        y5 = yvalues[groupRows*4:groupRows*5]
        groups.append(group5)
        ys.append(y5)
        np.set_printoptions(suppress=True)
        for i,regParam in enumerate(regParams):
            error = 0
            for index in range(0,5):
                if index==0:
                    testx = groups[index]
                    testy = ys[index]
                    trainx = np.vstack((np.vstack((groups[1],groups[2])),np.vstack((groups[3],groups[4]))))
                    trainy = np.vstack((np.vstack((ys[1],ys[2])),np.vstack((ys[3],ys[4]))))
                elif index==1:
                    testx = groups[index]
                    testy = ys[index]
                    trainx = np.vstack((np.vstack((groups[0],groups[2])),np.vstack((groups[3],groups[4]))))
                    trainy = np.vstack((np.vstack((ys[0],ys[2])),np.vstack((ys[3],ys[4]))))    
                elif index==2:
                    testx = groups[index]
                    testy = ys[index]
                    trainx = np.vstack((np.vstack((groups[0],groups[1])),np.vstack((groups[3],groups[4]))))
                    trainy = np.vstack((np.vstack((ys[0],ys[1])),np.vstack((ys[3],ys[4]))))
                elif index==3:
                    testx = groups[index]
                    testy = ys[index]
                    trainx = np.vstack((np.vstack((groups[0],groups[1])),np.vstack((groups[2],groups[4]))))
                    trainy = np.vstack((np.vstack((ys[0],ys[1])),np.vstack((ys[2],ys[4]))))    
                elif index==4:
                    testx = groups[index]
                    testy = ys[index]
                    trainx = np.vstack((np.vstack((groups[0],groups[1])),np.vstack((groups[2],groups[3]))))
                    trainy = np.vstack((np.vstack((ys[0],ys[1])),np.vstack((ys[2],ys[3]))))

            
                 
                x_transpose = trainx.transpose()
                xt_mul_x = np.dot(x_transpose,trainx)

                I = np.identity(501)
                x_add_res = np.add(xt_mul_x,regParam*I)
                xt_mul_x_inverse = np.linalg.inv(x_add_res)
                inverse_mul_xtranspose = np.dot(xt_mul_x_inverse,x_transpose)
                w = np.dot(inverse_mul_xtranspose,trainy)
                np.set_printoptions(suppress=True)
                res = np.dot(testx,w)
                diff = np.square(np.subtract(res,testy))
                errors[i].append(np.sum(diff)) 

        errorRes = []
        errorRes.append(sum(errors[0]))
        errorRes.append(sum(errors[1]))
        errorRes.append(sum(errors[2]))
        errorRes.append(sum(errors[3]))
        errorRes.append(sum(errors[4]))
        min = min(errorRes)
        minInd = errorRes.index(min)
        """
        print(errors[0])
        print(errors[1])
        print(errors[2])
        print(errors[3])
        print(errors[4]) 
        """
        x_transpose = x.transpose()
        xt_mul_x = np.dot(x_transpose,x)
        I = np.identity(501)
        x_add_res = np.add(xt_mul_x,regParams[minInd]*I)
        xt_mul_x_inverse = np.linalg.inv(x_add_res)
        inverse_mul_xtranspose = np.dot(xt_mul_x_inverse,x_transpose)
        w = np.dot(inverse_mul_xtranspose,yvalues)
        
        print(w)
        finishseconds = int(round(time.time() * 1000)) 
        print("The selected lambda value is: " + str(regParams[minInd]))
        print("Time passed for step3 of part2: " + str(finishseconds-startseconds) + " msec")
        
           
          



