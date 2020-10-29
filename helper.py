import pymongo
from Student import Student
from flask import jsonify
import numpy as np
import cv2
from mtcnn import MTCNN
from matplotlib.pyplot import imshow
import os


client = pymongo.MongoClient("mongodb://localhost:27017/")
db = client["dbms_database"]

def insert_student(s):
    
    data = s.toJson()
    ret = db.students.insert_one(data)
    
def check_if_face(imagepath):
    image = cv2.imread(imagepath)
    
    print(imagepath)
    img = cv2.cvtColor(image, cv2.COLOR_BGR2RGB)
    detector = MTCNN()
    
    di = detector.detect_faces(img)
    if(len(di)==0):
        os.remove(imagepath)
        return False
    else:
        di = di[0]
    
    if(di['confidence']>0.80):
        x,y,w,h = di['box']
        x-=10
        y-=10
        w+=10
        h+=10
        _slice = image[y:y+h, x:x+w,:]
        cv2.imwrite(imagepath, _slice)
        return True;
    else:
        os.remove(imagepath)
        return False;

    
    
    
    










