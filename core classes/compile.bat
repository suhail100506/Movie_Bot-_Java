@echo off
echo 🔨 Compiling Java Movie Recommendation System...

javac *.java

if %errorlevel% == 0 (
    echo ✅ Compilation successful!
    echo 🚀 Run with: java MovieRecommendationSystem
) else (
    echo ❌ Compilation failed!
    pause
)
