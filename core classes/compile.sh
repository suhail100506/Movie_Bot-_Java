#!/bin/bash
# Compile all Java files

echo "🔨 Compiling Java Movie Recommendation System..."

# Compile all Java files
javac *.java

if [ $? -eq 0 ]; then
    echo "✅ Compilation successful!"
    echo "🚀 Run with: java MovieRecommendationSystem"
else
    echo "❌ Compilation failed!"
    exit 1
fi
