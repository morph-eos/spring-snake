"""
Spring Snake CLI Client
======================

Interactive command-line interface for the Spring Snake Key-Value Store API.
This client provides a user-friendly way to interact with the Spring Snake backend,
offering full CRUD operations with comprehensive error handling and input validation.

Features:
- Interactive menu-driven interface
- Comprehensive error handling and validation
- Support for single and batch operations
- File export capabilities (JSON, CSV, TXT)
- Graceful keyboard interrupt handling
- Optional detailed logging with --debug flag

Usage:
    python main.py           # Normal operation
    python main.py --debug   # Enable detailed logging

Dependencies:
    - requests: HTTP client library
    - pandas: Data manipulation and analysis
    - pytz: Timezone handling

Author: Stefano Sciacovelli
Repository: https://github.com/morph-eos/Spring-Snake
Version: 2.0
"""

import sys
import json
import logging
from helper import Value, sorryprint, prettyjson, nice_image, menu
from requester import save, saveall, get, getfull, getall, delete, deleteall

# Check for debug flag
DEBUG_MODE = '--debug' in sys.argv

# Configure logging based on debug flag
if DEBUG_MODE:
    logging.basicConfig(
        level=logging.INFO,
        format='%(asctime)s - %(name)s - %(levelname)s - %(message)s'
    )
    print("🐛 Debug mode enabled - detailed logging active")
else:
    logging.basicConfig(level=logging.ERROR)

logger = logging.getLogger(__name__)

import os
import sys
import yaml
import json
import helper
import requester

def clear_screen():
    """Clear the console screen for better user experience."""
    os.system('cls' if os.name == 'nt' else 'clear')

def get_user_input(prompt, valid_options=None, allow_empty=False):
    """
    Get user input with validation.
    
    Args:
        prompt (str): The prompt message to display
        valid_options (list): List of valid options (case-insensitive)
        allow_empty (bool): Whether to allow empty input
    
    Returns:
        str: The validated user input
    """
    while True:
        try:
            user_input = input(prompt).strip()
            
            # Check for empty input
            if not user_input and not allow_empty:
                print("❌ Input cannot be empty. Please try again.")
                continue
            
            # Check against valid options if provided
            if valid_options and user_input.lower() not in [opt.lower() for opt in valid_options]:
                print(f"❌ Invalid option. Please choose from: {', '.join(valid_options)}")
                continue
                
            return user_input.lower() if valid_options else user_input
            
        except KeyboardInterrupt:
            print("\n\n👋 Goodbye! Exiting Spring Snake CLI.")
            sys.exit(0)
        except EOFError:
            print("\n\n👋 Goodbye! Exiting Spring Snake CLI.")
            sys.exit(0)

def save_to_file(data, filename, file_format):
    """
    Save data to a file in the specified format.
    
    Args:
        data: The data to save
        filename (str): The output filename
        file_format (str): 'json' or 'yaml'
    
    Returns:
        bool: True if successful, False otherwise
    """
    try:
        with open(filename, "w", encoding='utf-8') as outfile:
            if file_format == 'json':
                json.dump(data, outfile, indent=2, ensure_ascii=False)
            elif file_format == 'yaml':
                yaml.dump(data, outfile, default_flow_style=False, allow_unicode=True)
        
        print(f"✅ Data successfully saved to {filename}")
        return True
    except Exception as e:
        print(f"❌ Error saving file {filename}: {str(e)}")
        return False

def handle_save_operation():
    """Handle the save data operation with improved error handling."""
    print("\n📝 Save Data Operation")
    print("=" * 30)
    
    values = set()
    
    while True:
        try:
            # Get key and value from user
            key = get_user_input("🔑 Enter the key: ")
            value = get_user_input("💾 Enter the value: ")
            
            # Create value object and attempt to save
            value_obj = helper.Value(key, value)
            
            # Ask if user wants to save more values
            save_more = get_user_input(
                "➕ Do you want to save more values? (y/n): ", 
                ['y', 'n']
            )
            
            if save_more == 'n' and len(values) == 0:
                # Save single value
                try:
                    result = requester.save(value_obj)
                    print(f"✅ Result: {result}")
                except Exception as e:
                    print(f"❌ Error saving data: {str(e)}")
                break
            else:
                # Add to batch save
                values.add(value_obj)
                if save_more == 'n':
                    # Save all values in batch
                    try:
                        result = requester.saveall(values)
                        print(f"✅ Batch save result: {result}")
                    except Exception as e:
                        print(f"❌ Error saving batch data: {str(e)}")
                    break
                    
        except Exception as e:
            print(f"❌ Unexpected error during save operation: {str(e)}")
            break

def handle_get_operation():
    """Handle the get data operation with improved error handling."""
    print("\n📖 Get Data Operation")
    print("=" * 30)
    
    try:
        get_type = get_user_input(
            "🔍 Get specific value or all values? (s/a): ", 
            ['s', 'a']
        )
        
        if get_type == 's':
            # Get specific value
            key = get_user_input("🔑 Enter the key to retrieve: ")
            try:
                response = requester.get(key)
                if response.status_code == 200:
                    print(f"✅ Value for '{key}': {response.text}")
                elif response.status_code == 404:
                    print(f"❌ Key '{key}' not found in database")
                else:
                    print(f"❌ Error retrieving key '{key}': HTTP {response.status_code}")
            except Exception as e:
                print(f"❌ Error retrieving data: {str(e)}")
                
        elif get_type == 'a':
            # Get all values
            output_location = get_user_input(
                "📍 Display here or save to file? (h/f): ", 
                ['h', 'f']
            )
            
            try:
                response = requester.getall()
                if response.status_code == 200:
                    data = response.json()
                    
                    if output_location == 'h':
                        # Display in console
                        result = helper.prettyjson(data)
                        print(f"\n📊 All Data:\n{result}")
                    elif output_location == 'f':
                        # Save to file
                        file_format = get_user_input(
                            "📄 File format - JSON or YAML? (j/y): ", 
                            ['j', 'y']
                        )
                        
                        if file_format == 'j':
                            save_to_file(data, "output.json", "json")
                        elif file_format == 'y':
                            save_to_file(data, "output.yaml", "yaml")
                else:
                    print(f"❌ Error retrieving data: HTTP {response.status_code}")
            except Exception as e:
                print(f"❌ Error retrieving all data: {str(e)}")
                
    except Exception as e:
        print(f"❌ Unexpected error during get operation: {str(e)}")

def handle_delete_operation():
    """Handle the delete data operation with improved error handling."""
    print("\n🗑️  Delete Data Operation")
    print("=" * 30)
    
    try:
        delete_type = get_user_input(
            "🎯 Delete one value or all values? (o/a): ", 
            ['o', 'a']
        )
        
        if delete_type == 'o':
            # Delete specific value
            key = get_user_input("🔑 Enter the key to delete: ")
            try:
                result = requester.delete(key)
                print(f"✅ Delete result: {result}")
            except Exception as e:
                print(f"❌ Error deleting key '{key}': {str(e)}")
                
        elif delete_type == 'a':
            # Delete all values with confirmation
            print("⚠️  WARNING: This will delete ALL data from the database!")
            confirm = get_user_input("❓ Are you absolutely sure? (y/n): ", ['y', 'n'])
            
            if confirm == 'y':
                try:
                    result = requester.deleteall()
                    print(f"✅ All data deleted: {result}")
                except Exception as e:
                    print(f"❌ Error deleting all data: {str(e)}")
            else:
                print("✅ Operation cancelled. No data was deleted.")
                
    except Exception as e:
        print(f"❌ Unexpected error during delete operation: {str(e)}")

def main():
    """Main application loop with comprehensive error handling."""
    try:
        # Print welcome message and initial menu
        print('+-----------------------------------+')
        print('| Welcome to Spring Snake CLI Client |')
        print('+-----------------------------------+\n')
        print(helper.nice_image)
        print('\nHello there! Welcome to Spring Snake, a tool that allows you to save and retrieve key-value data using Python and Spring Boot APIs.')
        
        # Main application loop
        while True:
            try:
                print(helper.menu)
                option = get_user_input("👉 Select an option (1-3): ", ['1', '2', '3'])
                
                clear_screen()
                
                # Route to appropriate handler based on user selection
                if option == '1':
                    handle_save_operation()
                elif option == '2':
                    handle_get_operation()
                elif option == '3':
                    handle_delete_operation()
                
                # Ask if user wants to continue
                continue_app = get_user_input(
                    "\n🔄 Do you want to perform another operation? (y/n): ", 
                    ['y', 'n']
                )
                
                if continue_app == 'n':
                    print("\n👋 Thank you for using Spring Snake CLI! Goodbye!")
                    break
                    
                clear_screen()
                
            except Exception as e:
                print(f"❌ An unexpected error occurred: {str(e)}")
                print("🔄 Returning to main menu...")
                continue
                
    except KeyboardInterrupt:
        print("\n\n👋 Application interrupted by user. Goodbye!")
        sys.exit(0)
    except Exception as e:
        print(f"❌ Fatal error: {str(e)}")
        sys.exit(1)

if __name__ == "__main__":
    main()