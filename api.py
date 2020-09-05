from flask import Flask, jsonify

app = Flask(__name__)


li = [{"model":"omkar", "age": 20},{"model":"modellll", "age": 20},{"model":"ultra modellll", "age": 20}]    # data you want to pass

@app.route('/posts')    # route path
def method():           # method will be called for above route
    return jsonify(li)  # will create json return..............

if __name__ == "__main__":
    app.run(host = '192.168.43.165')  # add ip address of your laptop         
    
    
   
# run this server first
# install android app

