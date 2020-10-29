from flask import Flask, jsonify
import flask
import werkzeug
import time
from helper import check_if_face

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
    global p
    if p:
        sub = [{"sub_name":"CN", "total_lectures":10, "attended_lectures": 5},
              {"sub_name":"TOC", "total_lectures":10, "attended_lectures": 8},
              {"sub_name":"ISEE", "total_lectures":10, "attended_lectures": 9},
              {"sub_name":"SEPM", "total_lectures":10, "attended_lectures": 10}]
        p = False
    else:
        sub = [{"sub_name":"CN", "total_lectures":53, "attended_lectures": 2},
              {"sub_name":"TOC", "total_lectures":55, "attended_lectures": 6},
              {"sub_name":"ISEE", "total_lectures":56, "attended_lectures": 9},
              {"sub_name":"SEPM", "total_lectures":12, "attended_lectures": 15},
              {"sub_name":"DBMSL", "total_lectures":12, "attended_lectures": 15},
              {"sub_name":"CNL", "total_lectures":12, "attended_lectures": 15},
              {"sub_name":"SDL_TUT", "total_lectures":12, "attended_lectures": 15},
              {"sub_name":"SDL", "total_lectures":12, "attended_lectures": 13},]
        p = True
        
    return jsonify(sub)
@app.route("/posts_info", methods = ['POST'])
def post_info():
    data = flask.request.json
    print(data)
    return jsonify("OK")

if __name__ == "__main__":
    app.run(host = '192.168.43.165', port = 5000, threaded = False)       
