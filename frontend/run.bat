@ECHO OFF

if NOT exist node_modules (
  npm i
)

IF ["%ERRORLEVEL%"] == ["0"] (
  ng serve
)
