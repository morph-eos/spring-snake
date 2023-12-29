# Value class
class Value:
    def __init__(self, key, value):
        self.key = str(key)
        self.value = value
    def getkey(self):
        return self.key
    def getvalue(self):
        return self.value