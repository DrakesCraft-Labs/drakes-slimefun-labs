@echo off
title Reparador de Emojis DrakesLab
echo ============================================================
echo Reparando Fuentes y Emojis de Windows...
echo ============================================================

echo.
echo 1. Re-registrando fuentes de sistema...
reg add "HKLM\SOFTWARE\Microsoft\Windows NT\CurrentVersion\Fonts" /v "Segoe UI Emoji (TrueType)" /t REG_SZ /d "seguiemj.ttf" /f

echo.
echo 2. Limpiando cache de fuentes (Esto requiere reinicio manual luego)...
net stop fontcache
del /f /s /q /a %localappdata%\Microsoft\FontCache\*
net start fontcache

echo.
echo ============================================================
echo PROCESO COMPLETADO. 
echo Si sigues sin ver emojis, por favor REINICIA tu PC.
echo ============================================================
pause
