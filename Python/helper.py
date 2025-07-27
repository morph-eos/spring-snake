"""
Spring Snake Helper Module
==========================

This module contains helper classes and utility functions for the Spring Snake CLI client.
It provides data structures, formatting functions, and UI elements.

Classes:
    Value: Represents a key-value pair for the Spring Snake system

Functions:
    sorryprint(): Displays a user-friendly error message
    prettyjson(): Formats JSON data into a readable pandas DataFrame

Constants:
    nice_image: ASCII art for the application logo
    menu: Main menu text for user interaction

Author: M04ph3u2
Repository: https://github.com/M04ph3u2/Spring-Snake
"""

import pandas
import datetime
import pytz
import logging
import sys

# Configure logging based on debug flag
DEBUG_MODE = '--debug' in sys.argv
if DEBUG_MODE:
    logging.basicConfig(level=logging.INFO)
else:
    logging.basicConfig(level=logging.ERROR)

logger = logging.getLogger(__name__)

class Value:
    """
    Represents a key-value pair for the Spring Snake system.
    
    This class encapsulates a key-value pair with proper validation
    and accessor methods for use with the Spring Snake API.
    
    Attributes:
        key (str): The key identifier (converted to string for consistency)
        value: The associated value (can be any type)
    
    Methods:
        getkey(): Returns the key as a string
        getvalue(): Returns the stored value
    
    Example:
        >>> value_obj = Value("username", "john_doe")
        >>> print(value_obj.getkey())
        username
        >>> print(value_obj.getvalue())
        john_doe
    """
    
    def __init__(self, key, value):
        """
        Initialize a new Value object.
        
        Args:
            key: The key identifier (will be converted to string)
            value: The value to associate with the key
        
        Raises:
            ValueError: If key is None or empty after string conversion
        """
        if key is None:
            raise ValueError("Key cannot be None")
        
        self.key = str(key).strip()
        if not self.key:
            raise ValueError("Key cannot be empty")
            
        self.value = value
        
        logger.debug(f"Created Value object with key: '{self.key}'")
    
    def getkey(self):
        """
        Get the key identifier.
        
        Returns:
            str: The key as a string
        """
        return self.key
    
    def getvalue(self):
        """
        Get the associated value.
        
        Returns:
            The stored value (any type)
        """
        return self.value
    
    def __str__(self):
        """String representation of the Value object."""
        return f"Value(key='{self.key}', value='{self.value}')"
    
    def __repr__(self):
        """Developer-friendly representation of the Value object."""
        return f"Value(key={repr(self.key)}, value={repr(self.value)})"
    
    def __eq__(self, other):
        """Check equality based on key (for set operations)."""
        if not isinstance(other, Value):
            return False
        return self.key == other.key
    
    def __hash__(self):
        """Hash based on key (for set operations)."""
        return hash(self.key)

# ASCII Art and UI Constants
# =========================

nice_image = r'''
                            __..._                 |
                        ..-'      o.             .'|'.
                     .-'            :           /.'|\ \
                 _..'             .'__..--<     | /|'.|
          ...--""                 '-.            \ |\/
      ..-"                       __.'             \|/
    .'                  ___...--'                  `
   :        ____....---'                        
  :       .'                                    
 :       :           _____                      
 :      :    _..--"""     """--..__             
:       :  ."                      ""i--.       
:       '.:                         :    '.     
:         '--...___i---""""--..___.'      :     
 :                 ""---...---""          :     
  '.                                     :      
    '-.                                 :       
       '--...                         .'        
         :   ""---....._____.....---""          
         '.    '.                               
           '-..  '.                             
               '.  :                            
                : .'                            
               /  :                             
             .'   :                             
           .' .--'                              
          '--'
'''

menu = '''
📋 Main Menu - What would you like to do?
==========================================
1) 💾 Save some data to the database
2) 📖 Get/retrieve data from the database  
3) 🗑️  Delete data from the database

Please select an option (1-3):'''

# Utility Functions
# =================

def sorryprint():
    """
    Display a user-friendly error message for invalid input.
    
    This function provides consistent error messaging throughout the application
    when users provide invalid input or make incorrect menu selections.
    
    Example:
        >>> sorryprint()
        ❌ Sorry, I didn't understand that. Please try again.
    """
    print("❌ Sorry, I didn't understand that. Please try again.")
    logger.warning("User provided invalid input")

def prettyjson(data):
    """
    Format JSON data into a readable pandas DataFrame with proper date formatting.
    
    This function takes raw JSON data from the Spring Snake API and formats it
    into a user-friendly table format. It handles empty datasets gracefully
    and properly formats ISO 8601 dates with nanosecond precision.
    
    Args:
        data (list): List of dictionaries containing key-value data from the API.
                    Expected format:
                    [
                        {
                            "key": "example_key",
                            "value": "example_value", 
                            "lastchange": "2025-07-27T09:48:50.883175427Z[Etc/UTC]"
                        }
                    ]
    
    Returns:
        str: If data is empty, returns a user-friendly message
        pandas.DataFrame: If data exists, returns a formatted DataFrame with:
                         - Keys as index
                         - Values and formatted timestamps as columns
    
    Raises:
        Exception: Logs errors but doesn't crash - returns error message instead
    
    Example:
        >>> data = [{"key": "test", "value": "hello", "lastchange": "2025-07-27T09:48:50.883175427Z[Etc/UTC]"}]
        >>> print(prettyjson(data))
                  value               lastchange
        key                                     
        test      hello   2025-07-27 09:48:50 UTC
    """
    try:
        # Handle empty dataset gracefully
        if not data or len(data) == 0:
            logger.info("No data found in database - returning empty message")
            return "📭 No data found in the database."
        
        logger.info(f"Formatting {len(data)} records for display")
        
        # Create DataFrame from API data
        df = pandas.DataFrame(data)
        
        # Validate required columns exist
        required_columns = ['key', 'value', 'lastchange']
        missing_columns = [col for col in required_columns if col not in df.columns]
        
        if missing_columns:
            error_msg = f"❌ Data format error: missing columns {missing_columns}"
            logger.error(error_msg)
            return error_msg
        
        # Set the DataFrame index to the 'key' column for better readability
        df.set_index('key', inplace=True)
        
        # Format timestamps for better readability
        # Handle the Spring Boot format: "2025-07-27T09:48:50.883175427Z[Etc/UTC]"
        formatted_dates = []
        
        for timestamp in df['lastchange']:
            try:
                # Extract timezone information from format like "[Etc/UTC]"
                if '[' in timestamp and ']' in timestamp:
                    timezone_part = timestamp[timestamp.find('[') + 1:timestamp.find(']')]
                else:
                    timezone_part = 'UTC'  # Default fallback
                
                # Extract datetime part (remove Z and timezone bracket info)
                dt_part = timestamp[:timestamp.find('Z')] if 'Z' in timestamp else timestamp
                
                # Handle nanosecond precision by truncating to microseconds (Python limitation)
                if '.' in dt_part:
                    base_dt, microseconds = dt_part.split('.')
                    # Python datetime only supports up to 6 digits for microseconds
                    microseconds = microseconds[:6].ljust(6, '0')
                    dt_part = f"{base_dt}.{microseconds}"
                
                # Parse the ISO 8601 datetime string
                parsed_dt = datetime.datetime.strptime(dt_part, "%Y-%m-%dT%H:%M:%S.%f")
                
                # Convert to UTC timezone and format for display
                utc_dt = pytz.timezone('UTC').localize(parsed_dt)
                formatted_date = utc_dt.strftime('%Y-%m-%d %H:%M:%S %Z')
                formatted_dates.append(formatted_date)
                
            except Exception as date_error:
                logger.warning(f"Error parsing timestamp '{timestamp}': {date_error}")
                # Fallback to original timestamp if parsing fails
                formatted_dates.append(str(timestamp))
        
        # Update DataFrame with formatted dates
        df['lastchange'] = formatted_dates
        
        logger.info("Data formatting completed successfully")
        return df
        
    except Exception as e:
        error_msg = f"❌ Error formatting data: {str(e)}"
        logger.error(error_msg)
        return error_msg