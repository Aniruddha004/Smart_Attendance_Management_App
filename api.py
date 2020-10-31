from flask import Flask, jsonify, request
import flask
import werkzeug
import time
from helper import check_if_face, db_verify, new_user, helper_geta

app = Flask(__name__)

p = True;

li = {"model":"omkar", "age": 20}

@app.route('/posts_image',  methods = ['POST'])
def method():
    files_ids = list(flask.request.files)
    file_id = files_ids[0]   
    imagefile = flask.request.files[file_id]
    filename = werkzeug.utils.secure_filename(imagefile.filename)
    imagefile.save(filename)
    
    if(check_if_face(filename)):    
        return jsonify("OK")
    else:
        return jsonify("No face detected")
    
@app.route('/get_attendance', methods = ['GET'])
def return_atten():
    uid = request.args.get("uid")
    sub = helper_geta(uid);
        
    return jsonify(sub)
@app.route("/posts_info", methods = ['POST'])
def post_info():
    data = flask.request.json
    if new_user(data):
        return jsonify("OK")
    else:
        return jsonify("not OK")
@app.route("/verify", methods = ['GET'])
def verify():
    username = request.args.get("username")
    password = request.args.get("password")
    
    result = db_verify(username, password)
    
    return jsonify(result)
    

if __name__ == "__main__":
    app.run(host = '192.168.43.165', port = 5000, threaded = False)       
