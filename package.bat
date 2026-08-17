@echo off
echo ===================================================
echo Building Protein Motif Consensus Generator Installer
echo ===================================================

echo [1/4] Creating build directories...
if exist bin rmdir /s /q bin
if exist out rmdir /s /q out
if exist dist rmdir /s /q dist
mkdir bin
mkdir out
mkdir dist

echo [2/4] Compiling Java source files...
javac -d bin src/*.java
if %errorlevel% neq 0 (
    echo Compilation failed!
    exit /b %errorlevel%
)

echo [3/4] Packaging into a runnable JAR...
jar --create --file out/ProteinMotifConsensusGenerator.jar --main-class=Main -C bin .
if %errorlevel% neq 0 (
    echo JAR packaging failed!
    exit /b %errorlevel%
)

echo [4/4] Creating installer with jpackage...
jpackage --type exe --input out --dest dist --name "ProteinMotifConsensusGenerator" --main-jar ProteinMotifConsensusGenerator.jar --main-class Main --win-menu --win-shortcut --win-dir-chooser --icon logo.ico
if %errorlevel% neq 0 (
    echo Installer creation failed!
    exit /b %errorlevel%
)

echo ===================================================
echo Build completed successfully!
echo Installer is located in: dist/ProteinMotifConsensusGenerator-1.0.exe
echo ===================================================
