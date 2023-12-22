def save(k, v):
    global key, value
    key = k
    value = v
    

def get (k):
    if k == key:
        return value
    else:
        return 'Not found'