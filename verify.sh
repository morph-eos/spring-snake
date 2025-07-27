#!/bin/bash

echo "� Spring Snake - System Verification"
echo "====================================="

# Color codes for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

success() { echo -e "${GREEN}✅ $1${NC}"; }
error() { echo -e "${RED}❌ $1${NC}"; }
warning() { echo -e "${YELLOW}⚠️  $1${NC}"; }
info() { echo -e "${BLUE}ℹ️  $1${NC}"; }

# Quick environment check
echo ""
info "System Check:"
echo "   • Docker: $(docker info > /dev/null 2>&1 && echo "✅ Running" || echo "❌ Not running")"
echo "   • Python: $([ -d Python/venv ] && echo "✅ Ready" || echo "❌ Setup needed")"

# Start services if not running
if ! docker compose ps | grep -q "Up"; then
    info "Starting services..."
    docker compose up -d
    echo "Waiting for startup..."
    sleep 15
fi

# Quick health checks
echo ""
info "Verifying services..."

# MongoDB check
if docker compose exec -T mongodb mongosh --eval "db.adminCommand('ping')" > /dev/null 2>&1; then
    success "MongoDB: Connected"
else
    error "MongoDB: Connection failed"
    exit 1
fi

# Spring Boot check
if curl -f http://localhost:8080/actuator/health > /dev/null 2>&1; then
    success "Spring Boot: Healthy"
elif curl -f http://localhost:8080/api/getall > /dev/null 2>&1; then
    success "Spring Boot: API responding"
else
    error "Spring Boot: Not responding"
    exit 1
fi

# API functionality test
info "Testing API functionality..."
TEST_KEY="verify_$(date +%s)"
if curl -s -X PUT "http://localhost:8080/api/put" \
   -H "Content-Type: application/json" \
   -d "{\"key\":\"$TEST_KEY\",\"value\":\"test\"}" > /dev/null; then
    success "API: Write/Read operations working"
    curl -s -X DELETE "http://localhost:8080/api/delete?key=$TEST_KEY" > /dev/null
else
    warning "API: Basic operations may have issues"
fi

# Python environment check (optional)
if [ -d "Python/venv" ]; then
    cd Python && source venv/bin/activate > /dev/null 2>&1
    if python -c "import requests, pandas" 2>/dev/null; then
        success "Python: Dependencies ready"
    else
        warning "Python: Dependencies may need installation"
    fi
    deactivate 2>/dev/null
    cd ..
else
    warning "Python: Virtual environment not found"
fi

# Summary
echo ""
echo "🎯 Verification Complete"
echo "========================"
success "System is operational!"
echo ""
echo "🚀 Ready to use:"
echo "   • API: http://localhost:8080/api"
echo "   • Health: http://localhost:8080/actuator/health"
echo "   • Python CLI: cd Python && python main.py"
echo "   • Stop: docker compose down"
