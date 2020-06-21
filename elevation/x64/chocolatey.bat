@echo off
echo Instalando/Atualizando Node.js
@"%SystemRoot%\System32\WindowsPowerShell\v1.0\powershell.exe" -NoProfile -InputFormat None -ExecutionPolicy Bypass -Command "iex ((New-Object System.Net.WebClient).DownloadString('https://chocolatey.org/install.ps1'))" && SET "PATH=%PATH%;%ALLUSERSPROFILE%\chocolatey\bin"
choco install nodejs-lts -y
echo Salve seus arquivos e precione qualquer tecla para reiniciar!
pause
shutdown -r -t 0