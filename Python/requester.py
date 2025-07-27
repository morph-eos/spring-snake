"""
Spring Snake HTTP Client Module
===============================

This module handles all HTTP communication with the Spring Snake REST API.
It provides functions for CRUD operations on key-value pairs stored in the 
Spring Boot backend with MongoDB.

The module includes comprehensive error handling, timeout management, and 
detailed logging for debugging and monitoring purposes.

Functions:
    save(value): Save a single key-value pair
    saveall(dataset): Save multiple key-value pairs in batch
    get(key): Retrieve a specific value by key
    getfull(key): Retrieve full object data by key
    getall(): Retrieve all key-value pairs
    delete(key): Delete a specific key-value pair
    deleteall(): Delete all data (with caution)

Configuration:
    API_BASE_URL: Base URL for the Spring Snake API (default: http://localhost:8080/api)
    DEFAULT_TIMEOUT: Default timeout for HTTP requests (10 seconds)
    BATCH_TIMEOUT: Timeout for batch operations (30 seconds)

Author: M04ph3u2
Repository: https://github.com/M04ph3u2/Spring-Snake
"""

import json
import logging
import requests
import sys
from requests.exceptions import RequestException, ConnectionError, Timeout, HTTPError
from typing import Union, Set, Any

# Configure logging based on debug flag
DEBUG_MODE = '--debug' in sys.argv
if DEBUG_MODE:
    logging.basicConfig(level=logging.INFO)
else:
    logging.basicConfig(level=logging.ERROR)

logger = logging.getLogger(__name__)

# Configuration Constants
API_BASE_URL = 'http://localhost:8080/api'
DEFAULT_TIMEOUT = 10  # seconds
BATCH_TIMEOUT = 30    # seconds for batch operations

def handle_http_response(response: requests.Response, operation: str) -> Union[str, requests.Response]:
    """
    Handle HTTP response with proper error checking and logging.
    
    Args:
        response: The requests.Response object
        operation: Description of the operation for logging
        
    Returns:
        The response object or error message string
        
    Raises:
        HTTPError: For HTTP error status codes
    """
    try:
        # Log the request details
        logger.info(f"{operation} - Status: {response.status_code}, URL: {response.url}")
        
        # Check for HTTP errors and handle them appropriately
        if response.status_code >= 400:
            if response.status_code == 404:
                logger.warning(f"{operation} - Resource not found (404)")
                return f"❌ Resource not found: The requested key does not exist"
            elif response.status_code == 400:
                logger.warning(f"{operation} - Bad request (400): {response.text}")
                return f"❌ Bad request: {response.text}"
            elif response.status_code == 500:
                logger.error(f"{operation} - Server error (500): {response.text}")
                return f"❌ Server error: The backend service encountered an error"
            else:
                logger.error(f"{operation} - HTTP error {response.status_code}: {response.text}")
                return f"❌ HTTP error {response.status_code}: {response.text}"
        
        # Success case
        logger.info(f"{operation} - Request completed successfully")
        return response
        
    except Exception as e:
        error_msg = f"❌ Error processing response for {operation}: {str(e)}"
        logger.error(error_msg)
        return error_msg

def handle_request_error(func):
    """
    Decorator to handle common HTTP request errors with detailed logging.
    
    This decorator wraps API functions to provide consistent error handling
    for connection issues, timeouts, and other HTTP-related problems.
    
    Args:
        func: The function to wrap
        
    Returns:
        The wrapped function with error handling
    """
    def wrapper(*args, **kwargs):
        try:
            logger.debug(f"Executing {func.__name__} with args: {args}")
            return func(*args, **kwargs)
            
        except ConnectionError as e:
            error_msg = "❌ Connection Error: Cannot connect to the Spring Snake server. Please ensure the service is running on localhost:8080"
            logger.error(f"{func.__name__} - {error_msg}: {str(e)}")
            return error_msg
            
        except Timeout as e:
            error_msg = f"❌ Timeout Error: The server took too long to respond. Please try again."
            logger.error(f"{func.__name__} - {error_msg}: {str(e)}")
            return error_msg
            
        except HTTPError as e:
            error_msg = f"❌ HTTP Error: {str(e)}"
            logger.error(f"{func.__name__} - {error_msg}")
            return error_msg
            
        except RequestException as e:
            error_msg = f"❌ Request Error: {str(e)}"
            logger.error(f"{func.__name__} - {error_msg}")
            return error_msg
            
        except json.JSONDecodeError as e:
            error_msg = f"❌ JSON Error: Invalid data format - {str(e)}"
            logger.error(f"{func.__name__} - {error_msg}")
            return error_msg
            
        except Exception as e:
            error_msg = f"❌ Unexpected Error: {str(e)}"
            logger.error(f"{func.__name__} - {error_msg}")
            return error_msg
            
    return wrapper

# CRUD Operations
# ===============

@handle_request_error
def save(value) -> str:
    """
    Save a single key-value pair to the database.
    
    Args:
        value: A Value object containing key and value data
        
    Returns:
        str: Success message or error description
        
    Example:
        >>> from helper import Value
        >>> result = save(Value("username", "john_doe"))
        >>> print(result)
        ✅ Value saved successfully
    """
    if not hasattr(value, 'getkey') or not hasattr(value, 'getvalue'):
        return "❌ Invalid value object: must have getkey() and getvalue() methods"
    
    payload = {
        'key': str(value.getkey()).strip(),
        'value': value.getvalue()
    }
    
    # Validate key is not empty
    if not payload['key']:
        return "❌ Error: Key cannot be empty"
    
    logger.info(f"Saving single value with key: '{payload['key']}'")
    
    response = requests.put(
        f"{API_BASE_URL}/put",
        data=json.dumps(payload),
        headers={'Content-Type': 'application/json'},
        timeout=DEFAULT_TIMEOUT
    )
    
    result = handle_http_response(response, "Save single value")
    return result.text if hasattr(result, 'text') else result

@handle_request_error 
def saveall(dataset: Set) -> str:
    """
    Save multiple key-value pairs to the database in a batch operation.
    
    Args:
        dataset: A set/collection of Value objects
        
    Returns:
        str: Success message or error description
        
    Example:
        >>> from helper import Value
        >>> values = {Value("key1", "value1"), Value("key2", "value2")}
        >>> result = saveall(values)
        >>> print(result)
        ✅ Batch save completed: 2 values saved
    """
    if not dataset:
        return "❌ Error: No data provided for batch save"
    
    # Convert set to list and validate Value objects
    data = []
    for item in dataset:
        if not hasattr(item, 'getkey') or not hasattr(item, 'getvalue'):
            return "❌ Error: All items must be Value objects with getkey() and getvalue() methods"
        
        key = str(item.getkey()).strip()
        if not key:
            return "❌ Error: All keys must be non-empty"
            
        data.append({
            'key': key,
            'value': item.getvalue()
        })
    
    logger.info(f"Saving batch of {len(data)} values")
    
    response = requests.put(
        f"{API_BASE_URL}/putall",
        data=json.dumps(data),
        headers={'Content-Type': 'application/json'},
        timeout=BATCH_TIMEOUT
    )
    
    result = handle_http_response(response, "Save batch values")
    return result.text if hasattr(result, 'text') else result

@handle_request_error
def get(key: str) -> requests.Response:
    """
    Retrieve a specific value by its key.
    
    Args:
        key: The key to search for
        
    Returns:
        requests.Response: HTTP response object with the value data
        
    Example:
        >>> response = get("username")
        >>> if response.status_code == 200:
        ...     print(f"Value: {response.text}")
    """
    if not key or not str(key).strip():
        logger.warning("Attempted to get value with empty key")
        # Return a mock response for consistency
        class MockResponse:
            status_code = 400
            text = "❌ Error: Key cannot be empty"
        return MockResponse()
    
    key_str = str(key).strip()
    logger.info(f"Retrieving value for key: '{key_str}'")
    
    response = requests.get(
        f"{API_BASE_URL}/get",
        params={'key': key_str},
        timeout=DEFAULT_TIMEOUT
    )
    
    return handle_http_response(response, f"Get value for key '{key_str}'")

@handle_request_error
def getfull(key: str) -> requests.Response:
    """
    Retrieve full object data (including metadata) for a specific key.
    
    Args:
        key: The key to search for
        
    Returns:
        requests.Response: HTTP response with complete object data
        
    Example:
        >>> response = getfull("username")
        >>> if response.status_code == 200:
        ...     data = response.json()
        ...     print(f"Key: {data['key']}, Value: {data['value']}, Last Changed: {data['lastchange']}")
    """
    if not key or not str(key).strip():
        logger.warning("Attempted to get full object with empty key")
        class MockResponse:
            status_code = 400
            text = "❌ Error: Key cannot be empty"
        return MockResponse()
    
    key_str = str(key).strip()
    logger.info(f"Retrieving full object for key: '{key_str}'")
    
    response = requests.get(
        f"{API_BASE_URL}/getfull",
        params={'key': key_str},
        timeout=DEFAULT_TIMEOUT
    )
    
    return handle_http_response(response, f"Get full object for key '{key_str}'")

@handle_request_error
def getall() -> requests.Response:
    """
    Retrieve all key-value pairs from the database.
    
    Returns:
        requests.Response: HTTP response with all data as JSON array
        
    Example:
        >>> response = getall()
        >>> if response.status_code == 200:
        ...     all_data = response.json()
        ...     print(f"Found {len(all_data)} records")
    """
    logger.info("Retrieving all values from database")
    
    response = requests.get(
        f"{API_BASE_URL}/getall",
        timeout=DEFAULT_TIMEOUT
    )
    
    return handle_http_response(response, "Get all values")

@handle_request_error
def delete(key: str) -> str:
    """
    Delete a specific key-value pair from the database.
    
    Args:
        key: The key of the pair to delete
        
    Returns:
        str: Success message or error description
        
    Example:
        >>> result = delete("username")
        >>> print(result)
        ✅ Key 'username' deleted successfully
    """
    if not key or not str(key).strip():
        return "❌ Error: Key cannot be empty"
    
    key_str = str(key).strip()
    logger.info(f"Deleting value for key: '{key_str}'")
    
    response = requests.delete(
        f"{API_BASE_URL}/delete",
        params={'key': key_str},
        timeout=DEFAULT_TIMEOUT
    )
    
    result = handle_http_response(response, f"Delete key '{key_str}'")
    return result.text if hasattr(result, 'text') else result

@handle_request_error
def deleteall() -> str:
    """
    Delete ALL key-value pairs from the database.
    
    ⚠️ WARNING: This operation is irreversible and will remove all data!
    
    Returns:
        str: Success message or error description
        
    Example:
        >>> result = deleteall()
        >>> print(result)
        ✅ All data deleted successfully
    """
    logger.warning("DESTRUCTIVE OPERATION: Deleting all values from database")
    
    response = requests.delete(
        f"{API_BASE_URL}/deleteall",
        timeout=BATCH_TIMEOUT
    )
    
    result = handle_http_response(response, "Delete all values")
    return result.text if hasattr(result, 'text') else result