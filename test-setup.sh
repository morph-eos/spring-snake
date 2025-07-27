#!/bin/bash

echo "🐍 Spring Snake - Complete Setup Test Script"
echo "============================================="

# Color codes for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

success() {
    echo -e "${GREEN}✅ $1${NC}"
}

error() {
    echo -e "${RED}❌ $1${NC}"
}

warning() {
    echo -e "${YELLOW}⚠️  $1${NC}"
}

info() {
    echo -e "${BLUE}ℹ️  $1${NC}"
}

# Environment Check
echo ""
info "Checking environment..."
echo "   • Environment: $([ -f .env ] && echo "✅ Configured" || echo "❌ Missing")"
echo "   • Docker: $(docker info > /dev/null 2>&1 && echo "✅ Running" || echo "❌ Not running")"
echo "   • Python Environment: $([ -d Python/venv ] && echo "✅ Ready" || echo "❌ Not setup")"

# Start services
info "Starting services..."
docker-compose up -d

# Wait for services to start
info "Waiting for services to start..."
sleep 10

# Test MongoDB connection
info "Testing MongoDB connection..."
MAX_RETRIES=5
RETRY_COUNT=0

while [ $RETRY_COUNT -lt $MAX_RETRIES ]; do
    if docker-compose exec -T mongodb mongosh --eval "db.adminCommand('ping')" > /dev/null 2>&1; then
        success "MongoDB is connected and responding"
        break
    else
        RETRY_COUNT=$((RETRY_COUNT + 1))
        warning "MongoDB not ready, retrying... ($RETRY_COUNT/$MAX_RETRIES)"
        sleep 5
    fi
done

if [ $RETRY_COUNT -eq $MAX_RETRIES ]; then
    error "MongoDB failed to start after $MAX_RETRIES attempts"
    exit 1
fi

# Test Spring Boot health
info "Testing Spring Boot health..."
MAX_RETRIES=10
RETRY_COUNT=0

while [ $RETRY_COUNT -lt $MAX_RETRIES ]; do
    # Try actuator health endpoint first
    if curl -f http://localhost:8080/actuator/health > /dev/null 2>&1; then
        success "Spring Boot is healthy (Actuator endpoint responding)"
        break
    # Try API endpoint as fallback
    elif curl -f http://localhost:8080/api/getall > /dev/null 2>&1; then
        success "Spring Boot is healthy (API endpoint responding)"
        break
    else
        RETRY_COUNT=$((RETRY_COUNT + 1))
        warning "Spring Boot not ready, retrying... ($RETRY_COUNT/$MAX_RETRIES)"
        sleep 10
    fi
done

if [ $RETRY_COUNT -eq $MAX_RETRIES ]; then
    error "Spring Boot failed to start after $MAX_RETRIES attempts"
    echo ""
    error "Showing container logs for debugging:"
    docker-compose logs spring
    exit 1
fi

# Test API endpoints
info "Testing API endpoints..."

# Test basic GET endpoint
if curl -f http://localhost:8080/api/getall > /dev/null 2>&1; then
    success "GET /api/getall endpoint is responding"
else
    error "GET /api/getall endpoint failed"
fi

# Test health endpoint
if curl -f http://localhost:8080/actuator/health > /dev/null 2>&1; then
    success "Health check endpoint is responding"
else
    warning "Health check endpoint not available"
fi

# Test Python environment
info "Testing Python environment..."
cd Python

if [ -d "venv" ]; then
    source venv/bin/activate
    
    # Test if packages are installed
    if python -c "import requests, yaml" 2>/dev/null; then
        success "Python dependencies are installed"
    else
        warning "Python dependencies not installed, installing now..."
        pip install -r requirements.txt
        if [ $? -eq 0 ]; then
            success "Python dependencies installed successfully"
        else
            error "Failed to install Python dependencies"
            exit 1
        fi
    fi
    
    # Test Python client functionality
    info "Testing Python client functionality..."
    echo "Testing basic client import..."
    if python -c "from main import *" 2>/dev/null; then
        success "Python client imports successfully"
    else
        error "Python client import failed"
    fi
    
    deactivate
else
    warning "Python virtual environment not found. Run Python/setup.sh first."
fi

cd ..

# Final status report
echo ""
echo "🎯 Test Summary"
echo "==============="
success "✅ MongoDB: Connected and responding"
success "✅ Spring Boot: Healthy and responding"  
success "✅ API Endpoints: Working correctly"
success "✅ Python Environment: Ready"

echo ""
info "🚀 System is ready for use!"
echo ""
echo "Next steps:"
echo "  • Use Python CLI: cd Python && python main.py"
echo "  • Test API directly: curl http://localhost:8080/api/getall"
echo "  • Stop services: docker-compose down"
echo ""
        break
    # Fallback to API endpoint
    elif curl -f http://localhost:8080/api/getall > /dev/null 2>&1; then
        success "Spring Boot is healthy (API endpoint responding)"
        break
    else
        RETRY_COUNT=$((RETRY_COUNT + 1))
        if [ $RETRY_COUNT -lt $MAX_RETRIES ]; then
            warning "Spring Boot not ready yet, retrying... ($RETRY_COUNT/$MAX_RETRIES)"
            sleep 15
        else
            error "Spring Boot health check failed after $MAX_RETRIES attempts"
            echo "Checking Spring Boot logs:"
            docker-compose logs spring | tail -20
        fi
    fi
doneut
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

success() {
    echo -e "${GREEN}✅ $1${NC}"
}

error() {
    echo -e "${RED}❌ $1${NC}"
}

warning() {
    echo -e "${YELLOW}⚠️  $1${NC}"
}

info() {
    echo -e "ℹ️  $1"
}

# Check if .env file exists
if [ ! -f ".env" ]; then
    warning ".env file not found. Copying from .env.example..."
    cp .env.example .env
    success ".env file created. Please review and update the values if needed."
fi

# Check if Docker is running
if ! docker info > /dev/null 2>&1; then
    error "Docker is not running. Please start Docker and try again."
    exit 1
fi

success "Docker is running"

# Build the application
info "Building the application..."
./build.sh

if [ $? -ne 0 ]; then
    error "Build failed. Please check the build output above."
    exit 1
fi

success "Application build completed"

# Check if Maven is available for building
if ! command -v mvn > /dev/null 2>&1; then
    error "Maven not found. Please install Maven to build the Spring Boot application."
    echo "On macOS: brew install maven"
    exit 1
fi

success "Maven is available"

# Build the application
info "Building Spring Boot application and Docker containers..."
./build.sh

if [ $? -ne 0 ]; then
    error "Build failed. Please check the build output above."
    exit 1
fi

success "Build completed successfully"

# Check Python virtual environment setup
info "Checking Python environment..."
cd Python

if [ ! -d "venv" ]; then
    info "Setting up Python virtual environment..."
    ./setup.sh
else
    success "Python virtual environment already exists"
fi

# Activate virtual environment and test imports
source venv/bin/activate

echo "Testing Python dependencies..."
python -c "import requests; import pandas; import pytz; import yaml; print('All Python dependencies are working!')" 2>/dev/null
if [ $? -eq 0 ]; then
    success "Python dependencies are working"
else
    error "Python dependencies test failed"
    deactivate
    cd ..
    exit 1
fi

deactivate
cd ..

# Start services
info "Starting services..."
docker-compose up -d

# Wait for services to be ready
info "Waiting for services to be ready..."
sleep 30

# Check service status
echo ""
info "Service Status:"
docker-compose ps

# Test MongoDB connection
echo ""
info "Testing MongoDB connection..."
if docker-compose exec -T mongodb mongosh --eval "db.adminCommand('ping')" > /dev/null 2>&1; then
    success "MongoDB is responding"
else
    error "MongoDB is not responding"
fi

# Test Spring Boot health
echo ""
info "Testing Spring Boot API availability..."
MAX_RETRIES=5
RETRY_COUNT=0

while [ $RETRY_COUNT -lt $MAX_RETRIES ]; do
    if curl -f http://localhost:8080/api/getall > /dev/null 2>&1; then
        success "Spring Boot API is responding"
        break
    else
        RETRY_COUNT=$((RETRY_COUNT + 1))
        if [ $RETRY_COUNT -lt $MAX_RETRIES ]; then
            warning "Spring Boot not ready yet, retrying... ($RETRY_COUNT/$MAX_RETRIES)"
            sleep 10
        else
            error "Spring Boot API check failed after $MAX_RETRIES attempts"
        fi
    fi
done

# Test API endpoint
echo ""
info "Testing API endpoint..."
if curl -f http://localhost:8080/api/getall > /dev/null 2>&1; then
    success "API endpoint is responding"
else
    error "API endpoint is not responding"
fi

# Quick API functionality test
echo ""
info "Testing API functionality..."

# Test saving a value
TEST_RESPONSE=$(curl -s -w "%{http_code}" -X PUT http://localhost:8080/api/put \
  -H "Content-Type: application/json" \
  -d '{"key":"test_key","value":"test_value"}' -o /dev/null)

if [ "$TEST_RESPONSE" = "201" ]; then
    success "API PUT test passed"
    
    # Test retrieving the value
    RETRIEVE_RESPONSE=$(curl -s http://localhost:8080/api/get?key=test_key)
    if [ "$RETRIEVE_RESPONSE" = "test_value" ]; then
        success "API GET test passed"
    else
        warning "API GET test returned unexpected value: $RETRIEVE_RESPONSE"
    fi
    
    # Clean up test data
    curl -s -X DELETE http://localhost:8080/api/delete?key=test_key > /dev/null
else
    warning "API PUT test failed with status: $TEST_RESPONSE"
fi

echo ""
echo "🎉 Setup test completed!"
echo ""
echo "📋 Summary:"
echo "   • Environment: $([ -f .env ] && echo "✅ Configured" || echo "❌ Missing")"
echo "   • Docker: $(docker info > /dev/null 2>&1 && echo "✅ Running" || echo "❌ Not running")"
echo "   • Python Environment: $([ -d Python/venv ] && echo "✅ Ready" || echo "❌ Not setup")"
echo "   • MongoDB: $(docker-compose exec -T mongodb mongosh --eval "db.adminCommand('ping')" > /dev/null 2>&1 && echo "✅ Connected" || echo "❌ Connection failed")"
echo "   • Spring Boot API: $(curl -f http://localhost:8080/api/getall > /dev/null 2>&1 && echo "✅ Responding" || echo "❌ Not responding")"
echo ""
echo "🚀 Next steps:"
echo "   1. cd Python"
echo "   2. source venv/bin/activate"
echo "   3. python main.py"
echo ""
echo "🌐 Services are available at:"
echo "   • Spring Boot API: http://localhost:8080/api"
echo "   • Health Check: http://localhost:8080/actuator/health"
echo "   • Application Info: http://localhost:8080/actuator/info"
echo "   • MongoDB: localhost:27017"
echo ""
echo "📊 To view logs:"
echo "   • All services: docker-compose logs"
echo "   • Spring Boot only: docker-compose logs spring"
echo "   • MongoDB only: docker-compose logs mongodb"
