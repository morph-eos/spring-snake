#!/bin/bash

echo "🚀 Spring Snake - Quick Setup"
echo "============================="

# Colors
GREEN='\033[0;32m'
BLUE='\033[0;34m'
NC='\033[0m'

info() { echo -e "${BLUE}ℹ️  $1${NC}"; }
success() { echo -e "${GREEN}✅ $1${NC}"; }

# Check Docker
if ! docker info > /dev/null 2>&1; then
    echo "❌ Docker not running. Please start Docker first."
    exit 1
fi

# Setup Python environment
info "Setting up Python environment..."
cd Python
if [ ! -d "venv" ]; then
    python3 -m venv venv
    source venv/bin/activate
    pip install --upgrade pip
    pip install -r requirements.txt
    success "Python environment created"
else
    success "Python environment already exists"
fi
cd ..

# Build and start services
info "Building and starting Docker services..."
docker compose build
docker compose up -d

echo ""
success "Setup complete!"
echo ""
echo "🔍 Verify installation: ./verify.sh"
echo "🐍 Use Python CLI: cd Python && python main.py"
echo "🌐 API available at: http://localhost:8080/api"
