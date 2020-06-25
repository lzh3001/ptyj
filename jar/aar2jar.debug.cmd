del jar-debug.jar
cd build\outputs\aar
rmdir /S /Q classes
rmdir /S /Q tmp
::pause
"%programfiles%\7-Zip\7z.exe" x jar-debug.aar -otmp -y
::pause
del tmp\libs\*.tmp.jar
::pause
"%programfiles%\7-Zip\7z.exe" x tmp\libs\*.jar -oclasses -r -y
::pause
"%programfiles%\7-Zip\7z.exe" x tmp\*.jar -oclasses -r -y
::pause
rmdir /S /Q tmp
::pause
cd classes
::pause
"%programfiles%\7-Zip\7z.exe" a -tzip ../../../../jar-debug.jar
::pause
cd ..
::pause
rmdir /S /Q classes