import os
import yaml
import helper
import requester

# Print the welcome message and menu
print('+-----------------------------------+\n| Welcome to Spring Snake CLI Client |\n+-----------------------------------+\n' + helper.nice_image + '''
Hello there, welcome to Spring Snake, a code that allows you to save and retrieve some key-value data with Python and Spring Boot APIs.''' + helper.menu)

# Get the user's option from the menu
option = input()

# Initialize the response variable
response = 'y'

# Main loop, continues until the user enters 'n'
while response != 'n':
  while True:
    # Clear the console
    os.system('cls' if os.name == 'nt' else 'clear')

    # Option 1: Save values
    if option == '1':
      values = set()
      while True:
        # Get the key and value from the user
        print("Insert the key of the value you want to save:")
        key = input()
        print("Now, insert the value you want to save:")
        value = input()

        # If the key or value is empty, print an error message
        if (not key) or (not value):
          helper.sorryprint()
        else:
          # Ask the user if they want to save more values
          print("Do you want to save more values? (y/n)")
          response = input()
          if response != 'y' and len(values) == 0:
            # If not, save the current value and break the loop
            print(requester.save(helper.Value(key, value)))
            break
          else:
            # If yes, add the current value to the set
            values.add(helper.Value(key, value))
          if response != 'y' and len(values) != 0:
            break
        if len(values) == 0 and (key != '' or value != ''):
          break
        else:
          # Save all values in the set
          print(requester.saveall(values))
      break

    # Option 2: Get values
    elif option == '2':
      print("Do you want to get a specific value or all values? (s/a)")
      response = input()
      if response == 's':
        # Get a specific value
        print("Now, insert the key you want to get:")
        key = input()
        print('The linked value is: ' + requester.get(key).text)
      elif response == 'a':
        # Get all values
        print('Do you want to get all of them here or in a file? (h/f)')
        response = input()
        if response == 'h':
          # Print all values
          print(helper.prettyjson(requester.getall().json()))
        elif response == 'f':
          # Save all values to a file
          print('Now, do you prefer to have them in a JSON format or a YAML format? (j/y)')
          response = input()
          if response == 'j':
            # Save as JSON
            with open("output.json", "w") as outfile:
              outfile.write(requester.getall().json)
          elif response == 'y':
            # Save as YAML
            with open("output.yaml", "w") as outfile:
              outfile.write(yaml.dump(requester.getall().json()))
        else:
          helper.sorryprint()
      else:
        helper.sorryprint()

    # Option 3: Delete values
    elif option == '3':
      print("Do you want to delete just one value or all of them? (o/a)")
      response = input()
      if response == 'o':
        # Delete a specific value
        print("Now, insert the key of the value you want to delete:")
        key = input()
        print(requester.delete(key))
      elif response == 'a':
        # Delete all values
        print("Are you sure? (y/n)")
        response = input()
        if response == 'y':
          print(requester.deleteall())
        elif response == 'n':
          print("Ok, then.")
        else:
          helper.sorryprint()
      else:
        helper.sorryprint()
    break

  # Ask the user if they want to do something else
  print("Do you want to do something else? (y/n)")
  response = input()

  # Clear the console
  os.system('cls' if os.name == 'nt' else 'clear')

  # If the user wants to do something else, print the menu and get the new option
  if response != 'n':
    print(helper.menu)
    option = input()

  # Clear the console
  os.system('cls' if os.name == 'nt' else 'clear')