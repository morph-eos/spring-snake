#!/bin/bash

echo "🏗️  Spring Snake - Build Script"
echo "=============================="

# Color codes for output
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

# Check if Docker is running
if ! docker info > /dev/null 2>&1; then
    error "Docker is not running. Please start Docker and try again."
    exit 1
fi

success "Docker is running"

# Stop any existing containers
info "Stopping existing containers..."
docker-compose down

# Build Docker containers with no cache to ensure fresh build
info "Building Docker containers (this may take a few minutes)..."
docker-compose build --no-cache

if [ $? -eq 0 ]; then
    success "Docker containers built successfully"
else
    error "Failed to build Docker containers"
    exit 1
fi

echo ""
success "Build completed successfully!"
echo ""
echo "🚀 To start the services:"
echo "   docker-compose up -d"
echo ""
echo "🧪 To run tests:"
echo "   ./test-setup.sh"
