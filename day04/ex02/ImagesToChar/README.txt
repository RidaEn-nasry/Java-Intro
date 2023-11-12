
Instructions to compile and run the ImagesToChar project:


. Compile the source code:
   $ avac -d target/fr.42.printer/ src/**/*.java -cp lib/jcommander-1.82.jar:lib/JColor-5.5.1.jar

. Run the application (includes external libraries):
   $ java -cp target/fr.42.printer/:lib/jcommander-1.82.jar:lib/JColor-5.5.1.jar app.App --black=YELLOW --white=BLUE

. Archive the application in JAR format:
   $ jar cfm target/images-to-char.jar src/manifest.txt -C target/fr.42.printer/ .

. View Content of Archived application:
   $ jar tf target/images-to-char.jar 

. Extract all files from Archived Application:
   $ jar xf target/images-to-char.jar 


. Expand JCommander/Jcolor library into target folder:
   first navigate to target dir and run:
   $ jar -xvf …/lib/libraryName.jar


. Run Java application from JAR file
   $ java -jar  target/images-to-char.jar --black=BLUE --white=RED