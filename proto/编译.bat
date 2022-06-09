@echo off
echo path : %~dp0
dir    %~dp0\*.proto /B > proto.list


for  /f  %%a  in  (proto.list)  do (
	echo %%a building...
	..\protoc --java_out=G:\im\myWar\im-config\src\main\java %%a
)

del proto.list
pause
