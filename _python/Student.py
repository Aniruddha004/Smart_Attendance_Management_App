import numpy as np
class Student():
    def __init__(self, name, _class, add, uid):
        self.name = name;
        self._class = _class;
        self.add = add;
        self.uid = uid;
        self.face_encodings = np.random.randn(1,1024)
        self.face_string = self.face_encodings.tostring()
    def toJson(self):
        return {"name":self.name,
                "_class": self._class,
                "add": self.add, 
                "uid": self.uid, 
                "face_string":self.face_string}