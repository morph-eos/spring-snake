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
nice_image='''
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
'''
menu = '''Please, tell me, what do you want to do?:
1) I want to save some data
2) I want to get some data
3) I want to delete some data'''

# Sorry print function
def sorryprint ():
    print("Sorry, I didn't understand that. Please, try again.")