@ECHO OFF

if NOT exist _class MKDIR _class

javac *.java -d _class

IF ["%ERRORLEVEL%"] == ["0"] (
	java -cp _class; frontendAgent
)
