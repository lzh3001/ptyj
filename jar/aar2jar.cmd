del jar-release.jar
cd build\outputs\aar
rmdir /S /Q classes
rmdir /S /Q tmp
::pause
"%programfiles%\7-Zip\7z.exe" x jar-release.aar -otmp -y
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
"%programfiles%\7-Zip\7z.exe" a -tzip ../../../../jar-release-%date:~0,4%%date:~5,2%%date:~8,2%.jar
::pause
cd ..
::pause
rmdir /S /Q classes
pause