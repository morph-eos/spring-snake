# Spring Snake
<pre>
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
</pre>

## Description
Spring Snake is a project that allows you to save and retrieve key-value data using Python and Spring Boot APIs. It consists of a Python CLI client and a dockerized Spring Boot + MongoDB backend service.
I've been working on this to get myself used to Docker, Python, Spring and Postman API Testing (the collection file has been given too).

## Prerequisites
- Docker
- Python 3.x

## Installation of the Docker component
1. Clone the repository
2. Navigate to the project directory
3. Build and start the Docker containers: `docker-compose up -d`

## Running of the Python CLI client
1. Navigate to the Python code folder: `cd Python`
2. Run the code: `python main.py`

## Usage
The Python CLI client provides a menu for interacting with the Spring Boot backend. Here are the options:

1. Save values: You can save key-value pairs to the database.
2. Get values: You can retrieve specific or all values from the database. You can also choose to save the retrieved values to a JSON or YAML file.
3. Delete values: You can delete a specific value or all values from the database.

## License
This project is open-source and available under the MIT License.
