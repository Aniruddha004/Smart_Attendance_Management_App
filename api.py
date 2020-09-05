from flask import Flask, jsonify

app = Flask(__name__)


li = [{"model":"omkar", "age": 20},{"model":"rada", "age": 20},{"model":"ultra rada", "age": 20}]

@app.route('/posts')
def method():
    return jsonify(li)

if __name__ == "__main__":
    app.run(host = '192.168.43.165')