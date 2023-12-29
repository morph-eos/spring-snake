import json
import requests

# API URL
URL = 'http://localhost:8080' + '/api'

# CRUD functions
def save(value):
    return requests.put(URL + '/put', data=json.dumps({'key': value.getkey(), 'value': value.getvalue()}), headers={'Content-Type': 'application/json'}).text
def saveall(dataset):
    return requests.put(URL + '/putall', data=json.dumps(list(dataset)), headers = {'Content-Type': 'application/json'}).text
def get(k):
    return requests.get(URL + '/get', params={'key': str(k)})
def getfull(k):
    return requests.get(URL + '/getfull', params={'key': str(k)})
def getall():
    return requests.get(URL + '/getall')
def delete(k):
    return requests.delete(URL + '/delete', params={'key': str(k)}).text
def deleteall():
    return requests.delete(URL + '/deleteall').text