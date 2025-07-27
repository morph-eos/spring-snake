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

Spring Snake is a comprehensive key-value store system that combines a Spring Boot REST API backend with MongoDB storage and an interactive Python CLI client. This project demonstrates modern software architecture principles including containerization, comprehensive error handling, input validation, and user-friendly interfaces.

## Features

### Backend (Spring Boot + MongoDB)

- **REST API**: Complete CRUD operations for key-value pairs
- **MongoDB Integration**: Persistent data storage with timestamps
- **Docker Multi-stage Build**: Optimized container images with SHA256 hashes
- **Health Checks**: Built-in actuator endpoints for monitoring
- **Comprehensive Error Handling**: Proper HTTP status codes and error messages
- **Input Validation**: Bean validation with detailed error responses
- **Logging**: Structured logging for debugging and monitoring

### Frontend (Python CLI)

- **Interactive Menu System**: User-friendly command-line interface
- **Enhanced Error Handling**: Graceful error recovery and user feedback
- **Input Validation**: Comprehensive validation with helpful error messages
- **Debug Mode**: Optional detailed logging with `--debug` flag
- **Export Capabilities**: Save data to JSON, CSV, and TXT formats
- **Keyboard Interrupt Handling**: Graceful exit with Ctrl+C

## Prerequisites

- Docker & Docker Compose
- Python 3.13+ (for CLI client)
- curl or similar HTTP client (for manual API testing)

## Quick Start

### 1. Build and Start the Application

```bash
# Build and start all services with Docker Compose
docker compose up -d

# Check service health
curl http://localhost:8080/actuator/health
```

The system will automatically:

- Build the Spring Boot application using multi-stage Docker builds
- Set up MongoDB with authentication
- Configure health checks and networking
- Start all services with proper dependencies

### 2. Python CLI Client Setup

```bash
# Navigate to Python directory
cd Python

# Create virtual environment
python3 -m venv venv
source venv/bin/activate  # On Windows: venv\Scripts\activate

# Install dependencies
pip install -r requirements.txt
```

### 3. Using the Python CLI Client

```bash
# Normal operation (clean interface)
python main.py

# Debug mode (detailed logging)
python main.py --debug
```

The CLI provides:

- **Save Operation**: Store single or multiple key-value pairs
- **Get Operation**: Retrieve values by key or get all data
- **Delete Operation**: Remove specific keys or all data
- **Export Functions**: Save data to JSON, CSV, or text files

## API Documentation

### Available Endpoints

The Spring Boot backend exposes the following REST API endpoints:

#### Read Operations

- `GET /api/get?key={key}` - Get value by key (value only)
- `GET /api/getfull?key={key}` - Get complete object with metadata
- `GET /api/getall` - Get all key-value pairs

#### Write Operations

- `PUT /api/put` - Create new key-value pair (fails if key exists)
- `PUT /api/putall` - Create multiple key-value pairs (batch operation)
- `POST /api/update` - Update existing key-value pair

#### Delete Operations

- `DELETE /api/delete?key={key}` - Delete specific key-value pair
- `DELETE /api/deleteall` - Delete all data (use with caution)

#### Health & Monitoring

- `GET /actuator/health` - Application health check
- `GET /actuator/info` - Application information

### API Examples

```bash
# Save a value
curl -X PUT "http://localhost:8080/api/put" \
  -H "Content-Type: application/json" \
  -d '{"key":"username","value":"john_doe"}'

# Get a value
curl -X GET "http://localhost:8080/api/get?key=username"

# Get full object with metadata
curl -X GET "http://localhost:8080/api/getfull?key=username"

# Update a value
curl -X POST "http://localhost:8080/api/update" \
  -H "Content-Type: application/json" \
  -d '{"key":"username","value":"jane_doe"}'

# Delete a value
curl -X DELETE "http://localhost:8080/api/delete?key=username"
```

## Configuration

### Environment Variables

The application uses environment variables for configuration:

- `MONGO_USERNAME`: MongoDB username (default: springsnake)
- `MONGO_PASSWORD`: MongoDB password (default: defaultpassword)  
- `MONGO_DATABASE`: Database name (default: springsnake_db)
- `SPRING_PORT`: Application port (default: 8080)
- `SPRING_PROFILE`: Spring profile (default: docker)

### Health Checks

The application includes health checks for both services:

- MongoDB: Available at `mongodb:27017`
- Spring Boot: Available at `http://localhost:8080/actuator/health`

### Batch Operations Examples

```bash
# Save multiple values at once
curl -X PUT "http://localhost:8080/api/putall" \
  -H "Content-Type: application/json" \
  -d '[
    {"key":"user1","value":"john"},
    {"key":"user2","value":"jane"}
  ]'

# Get all values
curl -X GET "http://localhost:8080/api/getall"

# Delete all values (caution!)
curl -X DELETE "http://localhost:8080/api/deleteall"
```

## Usage

The Python CLI client provides an interactive menu for seamless interaction with the Spring Boot backend:

### CLI Operations

1. **Save Operation**: Store single or multiple key-value pairs with validation
2. **Get Operation**: Retrieve specific values by key or export all data to files
3. **Delete Operation**: Remove specific keys or clear the entire database
4. **Export Functions**: Save retrieved data to JSON, CSV, or text formats

### CLI Usage Examples

```bash
# Start the interactive CLI (normal mode)
python main.py

# Start with debug logging enabled
python main.py --debug

# The CLI will guide you through available operations
```

## Development

### Project Structure

``` sh
├── Python/                          # Python CLI client
│   ├── venv/                       # Virtual environment (auto-generated)
│   ├── requirements.txt            # Python dependencies
│   ├── main.py                     # Main CLI application with debug support
│   ├── helper.py                   # Utility functions and data structures
│   ├── requester.py                # HTTP client for API communication
├── SpringBoot/                     # Spring Boot backend
│   ├── src/main/java/              # Java source code
│   │   └── com/springsnake/backend/
│   │       ├── BackendApplication.java    # Main Spring Boot application
│   │       ├── ValueController.java       # REST API controller
│   │       ├── ValueService.java          # Business logic service
│   │       ├── values.java                # Entity model
│   │       └── utils/                     # Data transfer objects
│   ├── Dockerfile                  # Multi-stage Docker build
│   └── pom.xml                     # Maven dependencies
├── compose.yaml                    # Docker Compose configuration
├── README.md                       # This file
└── SpringSnake_APIs_Tests.postman_collection.json  # Postman API tests
```

### Recent Improvements (Phase 2)

This version includes significant enhancements over the initial implementation:

#### Backend Improvements

- **Enhanced Error Handling**: Comprehensive HTTP status codes and error messages
- **Input Validation**: Bean validation with detailed error responses  
- **Improved Documentation**: Extensive JavaDoc comments and API documentation
- **Logging Enhancement**: Structured logging for better debugging and monitoring
- **Code Quality**: Improved code organization and maintainability

#### Frontend Improvements

- **Debug Mode**: Optional detailed logging with `--debug` flag
- **Enhanced UX**: Clean interface without verbose logging by default
- **Error Recovery**: Graceful handling of HTTP errors and network issues
- **Input Validation**: Comprehensive validation with helpful error messages
- **Keyboard Interrupt Handling**: Proper cleanup on Ctrl+C

#### Infrastructure Improvements

- **Docker Optimization**: Multi-stage builds with SHA256 hash pinning
- **Reproducible Builds**: Consistent container builds across environments
- **Health Checks**: Built-in monitoring and health verification
- **Security**: Improved MongoDB authentication and container security

#### Bug Fixes (Latest)

- **ValueDAO Constructor Fix**: Resolved critical NullPointerException in `/api/putall` endpoint
- **Lombok Compatibility**: Fixed field initialization conflicts with @AllArgsConstructor
- **Error Handling**: Improved error messages and HTTP status code consistency
- **Build Stability**: Enhanced Docker build process and dependency management

## Testing

### Manual API Testing

Use the provided Postman collection (`SpringSnake_APIs_Tests.postman_collection.json`) or test endpoints manually:

```bash
# Test all endpoints
curl http://localhost:8080/actuator/health
curl -X GET "http://localhost:8080/api/getall"
curl -X PUT "http://localhost:8080/api/put" -H "Content-Type: application/json" -d '{"key":"test","value":"hello"}'
curl -X GET "http://localhost:8080/api/get?key=test"
curl -X DELETE "http://localhost:8080/api/delete?key=test"
```

### Python CLI Testing

```bash
# Test normal operation
cd Python && python main.py

# Test with debug logging
cd Python && python main.py --debug
```

## Troubleshooting

### Common Issues

1. **Port already in use**: Ensure ports 8080 and 27017 are available
2. **Docker build fails**: Run `docker compose build --no-cache`
3. **Python dependencies**: Ensure you're using Python 3.13+ and have activated the virtual environment
4. **Connection refused**: Wait for services to fully start (check with `docker compose ps`)

### Logs

```bash
# View backend logs
docker logs spring-snake-spring-1

# View MongoDB logs  
docker logs spring-snake-mongodb-1

# View all service logs
docker compose logs -f
```

## Contributing

This project demonstrates modern software development practices including:

- Containerized microservices architecture
- Comprehensive error handling and input validation
- User-friendly interfaces with proper feedback
- Structured logging and monitoring
- Reproducible builds and deployment

### Security Notes

- Environment variables are used instead of hardcoded credentials
- The `.env` file is excluded from version control
- CORS is configured for development (configure properly for production)
- Input validation is implemented on API endpoints

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Author

**M04ph3u2** - [GitHub Profile](https://github.com/M04ph3u2)

Project Repository: [Spring-Snake](https://github.com/M04ph3u2/Spring-Snake)
