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
- Docker & Docker Compose
- Python 3.13+ (for CLI client)

## Quick Start

### 1. Environment Setup
```bash
# Copy the environment template
cp .env.example .env

# Edit the .env file with your preferred values
# The default values will work for development
```

### 2. Build and Start the Application
```bash
# Build and start all services with one command
./build.sh

# Alternatively, start services manually after build
docker-compose up -d

# Check service health
docker-compose ps
```

### 3. Run Tests (Optional)

```bash
# Run comprehensive tests
./test-setup.sh
```

### 4. Install Python Dependencies

#### Option A: Automated Setup (Recommended)
```bash
# Navigate to Python directory
cd Python

# Run the setup script (macOS/Linux)
./setup.sh
```

#### Option B: Manual Setup
```bash
# Navigate to Python directory
cd Python

# Create virtual environment (recommended)
python3 -m venv venv

# Activate virtual environment
source venv/bin/activate  # macOS/Linux
# OR
venv\Scripts\activate     # Windows

# Install dependencies
pip install -r requirements.txt
```

### 5. Run the Python CLI Client
```bash
# Run the client
python main.py
```

## Configuration

### Environment Variables
The application uses environment variables for configuration. Key variables include:

- `MONGO_USERNAME`: MongoDB username (default: springsnake)
- `MONGO_PASSWORD`: MongoDB password (default: defaultpassword)
- `MONGO_DATABASE`: Database name (default: springsnake_db)
- `SPRING_PORT`: Application port (default: 8080)
- `SPRING_PROFILE`: Spring profile (default: docker)

### Health Checks
The application includes health checks for both services:
- MongoDB: Available at `mongodb:27017`
- Spring Boot: Available at `http://localhost:8080/actuator/health`

## API Endpoints

### Core Operations
- `GET /api/get?key={key}` - Retrieve a value by key
- `GET /api/getfull?key={key}` - Retrieve full value object by key  
- `GET /api/getall` - Retrieve all values
- `PUT /api/put` - Save a new value
- `PUT /api/putall` - Save multiple values
- `POST /api/update` - Update an existing value
- `DELETE /api/delete?key={key}` - Delete a specific value
- `DELETE /api/deleteall` - Delete all values

### Health & Monitoring
- `GET /actuator/health` - Application health status
- `GET /actuator/info` - Application information

## Usage
The Python CLI client provides a menu for interacting with the Spring Boot backend. Here are the options:

1. Save values: You can save key-value pairs to the database.
2. Get values: You can retrieve specific or all values from the database. You can also choose to save the retrieved values to a JSON or YAML file.
3. Delete values: You can delete a specific value or all values from the database.

## Development

### Project Structure
```
├── Python/                 # Python CLI client
│   ├── requirements.txt    # Python dependencies
│   ├── main.py            # Main CLI application
│   ├── helper.py          # Helper functions and classes
│   ├── requester.py       # HTTP client for API calls
│   └── setup.sh           # Python environment setup script
├── SpringBoot/            # Spring Boot backend
│   ├── src/main/java/     # Java source code
│   ├── src/main/resources/ # Configuration files
│   ├── pom.xml            # Maven dependencies
│   └── Dockerfile         # Multi-stage Docker build
├── compose.yaml           # Docker Compose configuration
├── .env.example          # Environment variables template
├── build.sh              # Build and start script
├── test-setup.sh         # Comprehensive testing script
└── README.md             # This file
```

### Security Notes
- Environment variables are used instead of hardcoded credentials
- The `.env` file is excluded from version control
- CORS is configured for development (configure properly for production)
- Input validation is implemented on API endpoints

## License
This project is open-source and available under the MIT License.
