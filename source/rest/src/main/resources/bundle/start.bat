@ECHO OFF

java -jar -Dspring.config.additional-location=table-games-backend-rest-config.yml lib/table-games-backend-rest-localbuild.jar

pause
