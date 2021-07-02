import numpy as np 
import pandas as pd
import matplotlib.pyplot as plt
from libsvm.svmutil import *
import sys
import math
from io import StringIO

irisFileName = "iris.csv"
wbcdFileName = "wbcd.csv"

if sys.argv[1]=="part1":
    df = pd.read_csv(irisFileName, sep=",", header=None)
    featureNames = df.iloc[0].to_numpy()
    df = df.iloc[1:]
    df[0] = pd.to_numeric(df[0])
    df[1] = pd.to_numeric(df[1])
    df[2] = pd.to_numeric(df[2])
    df[3] = pd.to_numeric(df[3])
    x = df.to_numpy()
    arrS = x[x[:,4] == "Iris-setosa"]
    arrV = x[x[:,4] == "Iris-versicolor"]
    trainSetosa = arrS[0:40, :]
    testSetosa = arrS[40:50, :]
    trainVersi = arrV[0:40, :]
    testVersi= arrV[40:50, :]
    testData = np.concatenate((testSetosa, testVersi))
    trainData = np.concatenate((trainSetosa, trainVersi))
   
   
    initEntropies = []
    initEntropy = -(1/2)*math.log2(1/2)-(1/2)*math.log2(1/2)
    initEntropies.append(initEntropy)

    nums = []
    num = 80
    nums.append(num)
    levels = []
    level = 0
    levels.append(level)
    features = [0,1,2,3]
    dataLists = []
    dataLists.append(trainData)
    meanValues = np.mean(dataLists[0][:,0:4], axis = 0)
    gainDenoms = []
    nodesList = []
    fromIds = []
    fromIds.append(-1)  # root node has no parent node
    nodeId = 0
    while(level!=5 and len(dataLists)!=0):
        entropies = []
        igains = []
        node = []
        node.append(str(nodeId))
        meanValues = np.mean(dataLists[0][:,0:4], axis = 0)
        num = nums[0]
        level = levels[0]
        node.append(str(level))
        for feature in features:
            fdata1 = dataLists[0][dataLists[0][:,feature] <= meanValues[feature]]
            f1 = len(fdata1)
           
            f1_s = len(fdata1[fdata1[:,4] == "Iris-setosa"])
            f1_v = len(fdata1[fdata1[:,4] == "Iris-versicolor"])
            fdata2 = dataLists[0][dataLists[0][:,feature] > meanValues[feature]]           
            f2 = len(fdata2)
            f2_s = len(fdata2[fdata2[:,4] == "Iris-setosa"])
            f2_v = len(fdata2[fdata2[:,4] == "Iris-versicolor"])

            f1_val = 0
            f2_val = 0
            if f1_s!=0 and f1_v!=0:
                f1_val = (f1/num)*(((-f1_s/f1)*math.log2(f1_s/f1)) + ((-f1_v/f1)*math.log2(f1_v/f1)))
            elif f1_s!=0 and f1_v==0:    
                f1_val = (f1/num)*(((-f1_s/f1)*math.log2(f1_s/f1)))
            elif f1_s==0 and f1_v!=0:  
                f1_val = (f1/num)*(((-f1_v/f1)*math.log2(f1_v/f1)))

            if f2_s!=0 and f2_v!=0:
                f2_val = (f2/num)*(((-f2_s/f2)*math.log2(f2_s/f2)) + ((-f2_v/f2)*math.log2(f2_v/f2)))
            elif f2_s!=0 and f2_v==0:    
                f2_val = (f2/num)*(((-f2_s/f2)*math.log2(f2_s/f2)))
            elif f2_s==0 and f2_v!=0:  
                f2_val = (f2/num)*(((-f2_v/f2)*math.log2(f2_v/f2)))    

            
            if f2!=0 and f1!=0:
                gainDenoms.append((((-f1/num)*math.log2(f1/num)) + ((-f2/num)*math.log2(f2/num))))
            elif f2!=0 and f1==0:
                gainDenoms.append((((-f2/num)*math.log2(f2/num))))
            elif f2==0 and f1!=0:
                gainDenoms.append((((-f1/num)*math.log2(f1/num)))) 
            else:
                gainDenoms.append(0) 
            entropies.append(f1_val + f2_val)
            igains.append(initEntropies[0]-(f1_val + f2_val))
        
        index = -1 
        if sys.argv[2]=="step1":   
            index = igains.index(max(igains))     # applying decision tree with information gain
        if sys.argv[2]=="step2":
            gainRatios = [ig / split for ig, split in zip(igains, gainDenoms)]  # applying decision tree with gain ratio
            index = gainRatios.index(max(gainRatios))
        node.append(featureNames[index])    
        f1_val = 0
        f2_val = 0
        fdata1 = dataLists[0][dataLists[0][:,index] <= meanValues[index]]
        fdata2 = dataLists[0][dataLists[0][:,index] > meanValues[index]]
        f2 = len(fdata2)
       
        f2_s = len(fdata2[fdata2[:,4] == "Iris-setosa"])
        f2_v = len(fdata2[fdata2[:,4] == "Iris-versicolor"])
        f1 = len(fdata1)
        f1_s = len(fdata1[fdata1[:,4] == "Iris-setosa"])
        f1_v = len(fdata1[fdata1[:,4] == "Iris-versicolor"])
        
        if f1_s!=0 and f1_v!=0:
            f1_val = (f1/num)*(((-f1_s/f1)*math.log2(f1_s/f1)) + ((-f1_v/f1)*math.log2(f1_v/f1)))
        elif f1_s!=0 and f1_v==0:    
            f1_val = (f1/num)*(((-f1_s/f1)*math.log2(f1_s/f1)))
        elif f1_s==0 and f1_v!=0:  
            f1_val = (f1/num)*(((-f1_v/f1)*math.log2(f1_v/f1)))

        if f2_s!=0 and f2_v!=0:
            f2_val = (f2/num)*(((-f2_s/f2)*math.log2(f2_s/f2)) + ((-f2_v/f2)*math.log2(f2_v/f2)))
        elif f2_s!=0 and f2_v==0:    
            f2_val = (f2/num)*(((-f2_s/f2)*math.log2(f2_s/f2)))
        elif f2_s==0 and f2_v!=0:  
            f2_val = (f2/num)*(((-f2_v/f2)*math.log2(f2_v/f2)))    
      
        dataLists.pop(0)
        initEntropies.pop(0)
        nums.pop(0)
    
        if f1_val !=0:         
            dataLists.append(fdata1) 
            fromIds.append(nodeId)
            initEntropies.append(f1_val) 
            nums.append(len(fdata1)) 
            levels.append(level+1)
            node.append("Cont.")
        else:
            if f1_s==0:
                node.append("V")  
            else:
                node.append("S")      
        if f2_val !=0:         
            dataLists.append(fdata2)
            fromIds.append(nodeId)
            initEntropies.append(f2_val)  
            nums.append(len(fdata2)) 
            levels.append(level+1)
            node.append("Cont.")
        else:
            if f2_s==0:
                node.append("V")  
            else:
                node.append("S")      
       

        levels.pop(0)
        node.append(meanValues[index])
        nodesList.append(node)
        nodeId = nodeId + 1

    #print(nodesList)
  
    trueVal = 0
    total = 0
    counter = 0
    node = nodesList[0] 
    for row in testData:
        while True:
            fInd = np.where(featureNames == node[2])
            threshold = node[5]
            tVal = row[fInd]
         
            if tVal <=threshold and node[3]=='Cont.':
                nInd = fromIds.index(int(node[0]))
                node = nodesList[nInd] 
            elif tVal <= threshold and (node[3]=='S' or node[3]=='V'):
                if node[3]=='S' and row[4]=="Iris-setosa":
                    trueVal = trueVal + 1
                elif node[3]=='V' and row[4]=="Iris-versicolor":
                    trueVal = trueVal + 1    
                break    
            elif tVal > threshold and node[4]=='Cont.':
                nInd = fromIds.index(int(node[0]))
                if fromIds.count(int(node[0])) > 1:
                    nInd = nInd + 1
                node = nodesList[nInd]   
            elif tVal > threshold and (node[4]=='S' or node[4]=='V'):
                if node[4]=='S' and row[4]=="Iris-setosa":
                    trueVal = trueVal + 1
                elif node[4]=='V' and row[4]=="Iris-versicolor":
                    trueVal = trueVal + 1  
                break     

    print("DT " + nodesList[0][2] + " " + str(trueVal/len(testData)))  
           


if sys.argv[1]=="part2":
    df = pd.read_csv(wbcdFileName, sep=",", header=None)
    x = df.to_numpy()
    x = np.delete(x, np.s_[len(df.columns)-1], axis=1)  # deleting nan values from data matrix
    x = np.delete(x, np.s_[0], axis=1)  # deleting id values from data matrix
    x = np.delete(x, (0), axis=0)  # deleting the first row (names of columns)
    
    train = x[0:400, :]
    trainy = train[:,0]             # getting y values
    trainy = np.array([y.replace('M','0') for y in trainy])
    trainy = np.array([y.replace('B','1') for y in trainy])
    trainy = trainy.astype(float)
    train = np.delete(train, np.s_[0], axis=1)  # deleting y values from data matrix
    train = train.astype(float)

    test = x[400:len(x), :]
    testy = test[:,0]  # getting y values
    testy = np.array([y.replace('M','0') for y in testy])
    testy = np.array([y.replace('B','1') for y in testy])
    test = np.delete(test, np.s_[0], axis=1)  # deleting y values from data matrix
    test = test.astype(float)
    testy = testy.astype(float)

    # normalizing data
    for i,row in enumerate(train):
      train[i] = (train[i] - train[i].min()) / (np.ptp(train[i]))
    for i,row in enumerate(test):
      test[i] = (test[i] - test[i].min()) / (np.ptp(test[i]))

    problem = svm_problem(trainy, train)
    param = svm_parameter('-q')

    if sys.argv[2]=="step1":
        param.kernel_type = LINEAR
        cvalues = [0.01,0.1,1,100,1000]
        
        for c in cvalues:
            param.C = c
            svmModel = svm_train(problem, param,'-q')
            vNum = svmModel.l
            so, sys.stdout = sys.stdout, StringIO()
            [_,accuracy,_] = svm_predict(testy,test,svmModel)
            sys.stdout = so
            accuracyVal = accuracy[0]

            print("SVM kernel=linear C=" + str(c) + " acc=" + str(accuracyVal/100) + " n=" + str(vNum))
    
    elif sys.argv[2]=="step2":
        k_types = [LINEAR, RBF, SIGMOID, POLY]
        param.C = 100
        for type in k_types:
            param.kernel_type = type
            svmModel = svm_train(problem, param,'-q')
            vNum = svmModel.l
            so, sys.stdout = sys.stdout, StringIO()
            [_,accuracy,_] = svm_predict(testy,test,svmModel)
            sys.stdout = so
            accuracyVal = accuracy[0]

            typeVal = ""
            if type==0:
                typeVal="linear"
            elif type==1:
                typeVal = "polynomial"
            elif type==2:
                typeVal="rbf"
            elif type==3:
                typeVal="sigmoid"            
            print("SVM kernel=" + typeVal + " C=" + str(100) + " acc=" + str(accuracyVal/100) + " n=" + str(vNum)) 

