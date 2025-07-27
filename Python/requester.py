import json
import requests
from requests.exceptions import RequestException, ConnectionError, Timeout

# API URL
URL = 'http://localhost:8080' + '/api'

def handle_request_error(func):
    """Decorator to handle common request errors"""
    def wrapper(*args, **kwargs):
        try:
            return func(*args, **kwargs)
        except ConnectionError:
            return "Error: Cannot connect to the server. Please ensure the Spring Boot service is running."
        except Timeout:
            return "Error: Request timed out. The server might be overloaded."
        except RequestException as e:
            return f"Error: Request failed - {str(e)}"
        except Exception as e:
            return f"Error: Unexpected error - {str(e)}"
    return wrapper

# CRUD functions
@handle_request_error
def save(value):
    response = requests.put(
        URL + '/put', 
        data=json.dumps({'key': value.getkey(), 'value': value.getvalue()}), 
        headers={'Content-Type': 'application/json'},
        timeout=10
    )
    return response.text

@handle_request_error
def saveall(dataset):
    # Convert set to list and handle Value objects
    data = []
    for item in dataset:
        data.append({'key': item.getkey(), 'value': item.getvalue()})
    
    response = requests.put(
        URL + '/putall', 
        data=json.dumps(data), 
        headers={'Content-Type': 'application/json'},
        timeout=30
    )
    return response.text

@handle_request_error
def get(k):
    response = requests.get(URL + '/get', params={'key': str(k)}, timeout=10)
    return response

@handle_request_error
def getfull(k):
    response = requests.get(URL + '/getfull', params={'key': str(k)}, timeout=10)
    return response

@handle_request_error
def getall():
    response = requests.get(URL + '/getall', timeout=15)
    return response

@handle_request_error
def delete(k):
    response = requests.delete(URL + '/delete', params={'key': str(k)}, timeout=10)
    return response.text

@handle_request_error
def deleteall():
    response = requests.delete(URL + '/deleteall', timeout=30)
    return response.text