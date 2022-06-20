# -*- coding: utf-8 -*-
"""
Created on Mon Mar 28 22:49:01 2022

@author: HP
"""

#http://10.117.156.30:5000/                  #student_sec
#kapilsz.pythonanywhere.com/                 #online server

from flask import Flask
from multiprocessing import Value
import time
import pandas as pd

g = Value('i',0)

app = Flask(__name__)

Data = pd.read_csv('C:/Users/HP/Downloads/ISP/flask/Node_2_30d.csv')

@app.route("/")
def showHomePage():
	return "Hello THere"

@app.route("/Temperature",methods=["POST"])
def Temperature():
    
    reading_id = Data.iloc[g.value,0]
    Temperature = Data.iloc[g.value,4]
    
    print(reading_id,Temperature)
    g.value += 1
    p = str(Temperature)
    return p;
    time.sleep(500);
    
    
@app.route("/co2ppm",methods=["POST"])
def co2ppm():
    reading_id = Data.iloc[g.value,0]
    co2ppm = Data.iloc[g.value,6]
    print(reading_id,co2ppm)
    g.value +=1;
    p = str(co2ppm)
    return p;
    time.sleep(500);
    
@app.route("/Humidity", methods=["POST"])
def Humidity():
    reading_id = Data.iloc[g.value,0]
    Humidity = Data.iloc[g.value,5]
    print(reading_id,Humidity)
    g.value+=1;
    p=str(Humidity)
    return p;
    time.sleep(500);    

@app.route("/Humidity11", methods=["POST"])                                                     #outliers for humidity
def Humidity11():
    entry = g.value
    threshold=3
    mean = np.mean(Data['Humidity'][entry-500:entry+500])
    std =np.std(Data['Humidity'][entry-500:entry+500])
    z_score= (Data['Humidity'][entry] - mean)/std
    print(entry)
    if np.abs(z_score) > threshold:
        print("outlier" + "@" + str(entry))
        return str(1)
    else:
        return str(0)    
    
@app.route("/Ethylene", methods=["POST"])
def Ethylene():
    reading_id = Data.iloc[g.value,0]
    Ethylene = Data.iloc[g.value,7]
    print(reading_id,Ethylene)
    g.value+=1;
    p=str(Ethylene)
    return p;
    time.sleep(500);      

    
#if __name__ == "__main__":
#    port=5000
#    app.run(host="0.0.0.0",port=port)
    
    

