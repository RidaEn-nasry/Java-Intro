
Instructions to compile and run the ImagesToChar project:


1. Compile the source code:
   $ javac -d target/fr.42.printer/ src/**/*.java

2. Run the application:
   $ java -cp target/fr.42.printer app.App --black=<char> --white=<char> --path=<bmp path>

3. Archive the application in JAR format:
   $ jar cfm target/images-to-char.jar src/manifest.txt -C target/fr.42.printer/ .

4. View Content of Archived application:
   $ jar tf target/images-to-char.jar 

5. Extract all files from Archived Application:
   $ jar xf target/images-to-char.jar 

6. Run Java application from JAR file
   $ java -jar  target/images-to-char.jar