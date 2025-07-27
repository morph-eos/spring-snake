#!/bin/bash

echo "🐍 Spring Snake - Python Environment Setup"
echo "=========================================="

cd Python

# Check if virtual environment already exists
if [ -d "venv" ]; then
    echo "✅ Virtual environment already exists"
else
    echo "📦 Creating virtual environment..."
    python3 -m venv venv
    echo "✅ Virtual environment created"
fi

# Activate virtual environment
echo "🔧 Activating virtual environment..."
source venv/bin/activate

# Upgrade pip
echo "⬆️  Upgrading pip..."
pip install --upgrade pip

# Install dependencies
echo "📥 Installing dependencies..."
pip install -r requirements.txt

echo ""
echo "🎉 Python environment setup completed!"
echo ""
echo "To activate the environment in the future, run:"
echo "   cd Python"
echo "   source venv/bin/activate"
echo ""
echo "To run the CLI client:"
echo "   python main.py"
echo ""
echo "To deactivate the environment:"
echo "   deactivate"
