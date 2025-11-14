@echo off
set JAVA_FX_LIB="C:\javafx-sdk-25.0.1\lib"
set JAR_PATH="C:\Users\hiter\Desktop\Exercicios POO\PROJETO CONTROLE DE ESTOQUE\principal\target\sistema-estoque-1.0.0-shaded.jar"

echo Iniciando Sistema de Controle de Estoque...
java --module-path %JAVA_FX_LIB% --add-modules javafx.controls,javafx.fxml -jar %JAR_PATH%
pause
