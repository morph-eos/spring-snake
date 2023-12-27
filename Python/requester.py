import requests

def save(k, v):
    requests.post('http://localhost:8081', data={'key': k, 'value': v})
    

def get(k):
    return requests.get('http://localhost:8081', params={'key': k})
