@echo off
set GIT_PATH="C:\Users\PC\Desktop\BlockChain\Git\bin\git.exe"
:P
%GIT_PATH% add -A
%GIT_PATH% commit -am "Auto-committed on %date%"
%GIT_PATH% push %BRANCH%
goto P