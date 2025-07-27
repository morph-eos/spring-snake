import pandas
import datetime
import pytz

# Value class
class Value:
    def __init__(self, key, value):
        self.key = str(key)
        self.value = value
    def getkey(self):
        return self.key
    def getvalue(self):
        return self.value

# Some useful text variables
nice_image=r'''
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
menu = '''Please, tell me, what do you want to do?:
1) I want to save some data
2) I want to get some data
3) I want to delete some data'''

# Sorry print function
def sorryprint ():
    print("Sorry, I didn't understand that. Please, try again.")

# JSON Prettifier function
def prettyjson(data):
    if not data:  # Check if data is empty
        return "No data found in the database."
    
    df = pandas.DataFrame(data)
    # Set the DataFrame index to the 'key' column
    df.set_index('key', inplace=True)
    
    # Convert the 'lastchange' column to different format
    # Handle the format: "2025-07-27T09:48:50.883175427Z[Etc/UTC]"
    formatted_dates = []
    for dt in df['lastchange']:
        # Extract timezone name from format like "[Etc/UTC]"
        timezone_part = dt[dt.find('[') + 1:dt.find(']')]
        # Extract datetime part (remove Z and timezone info)
        dt_part = dt[:dt.find('Z')]
        # Truncate microseconds to 6 digits (Python datetime limit)
        if '.' in dt_part:
            base_dt, microseconds = dt_part.split('.')
            microseconds = microseconds[:6].ljust(6, '0')  # Ensure 6 digits
            dt_part = f"{base_dt}.{microseconds}"
        
        # Parse and format the datetime
        parsed_dt = datetime.datetime.strptime(dt_part, "%Y-%m-%dT%H:%M:%S.%f")
        # Convert to UTC timezone and then format
        utc_dt = pytz.timezone('UTC').localize(parsed_dt)
        formatted_dates.append(utc_dt.strftime('%Y-%m-%d %H:%M:%S %Z'))
    
    df['lastchange'] = formatted_dates
    return df