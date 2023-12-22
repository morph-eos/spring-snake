import os
import requester

print('''
                            __..._                 |
                        ..-'      o.             .'|'.
                     .-'            :           /.'|\ \\
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

Hello there, welcome to Spring Snake, a code that allows you to save and retrieve some key-value data with Python and Spring Boot APIs.
Please, tell me, what do you want to do?:
1) I want to save some data
2) I want to get some data''')
option = input()
response = 'y'
while response != 'n':
  while True:
    os.system('cls' if os.name == 'nt' else 'clear')
    if option == '1':
      while True:
        print("Nice! Now, insert the key you want to save:")
        key = input()
        print("Nice! Now, insert the value you want to save:")
        value = input()
        if (not key) or (not value):
          print("Sorry, I didn't understand that. Please, try again.")
        else:
          break
      requester.save(key, value)
      break
    elif option == '2':
      print("Nice! Now, insert the key you want to get:")
      key = input()
      print('The linked value is: ' + requester.get(key))
      break
    else:
      print("Sorry, I didn't understand that. Please, try again.")
  print("Do you want to do something else? (y/n)")
  response = input()
  if response != 'n':
    print('''Nice! Now please, tell me, what do you want to do?:
    1) I want to save some data
    2) I want to get some data''')
    option = input()
  os.system('cls' if os.name == 'nt' else 'clear')