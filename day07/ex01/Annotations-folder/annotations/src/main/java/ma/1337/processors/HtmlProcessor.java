
package ma._1337.processor;

import javax.annotation.processing.AbstractProcessor;
import ma._1337.annotations.HtmlForm;
import ma._1337.annotations.HtmlInput;
import java.lang.annotation.Annotation;
import java.util.Set;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import java.io.File;
import java.io.IOException;
import java.io.FileWriter;
import javax.tools.Diagnostic;
import javax.annotation.processing.Processor;
import com.google.auto.service.AutoService;
import javax.annotation.processing.Messager;

@SupportedAnnotationTypes(value = { "ma._1337.annotations.HtmlForm", "ma._1337.annotations.HtmlInput" })
@SupportedSourceVersion(SourceVersion.RELEASE_21)
@AutoService(javax.annotation.processing.Processor.class)
public class HtmlProcessor extends AbstractProcessor {
    // a method to handle file creation and writing
    private void writeToFile(String fileName, String content) {
        try {
            // create the file if it doesn't exist
            // resources folder
            String path = "app/src/main/resources/" + fileName;
            File file = new File(path);
            if (!file.exists()) {
                file.createNewFile();
            }
            // write to the file
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write(content);
            fileWriter.close();
        } catch (IOException e) {
            System.err.println("Error while writing to file: " + e.getMessage());
            System.exit(1);
        }
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations,
            RoundEnvironment roundEnv) {
        // noting the processing round
        Messager messager = processingEnv.getMessager();
        messager.printMessage(Diagnostic.Kind.NOTE, "Processing round started");
        // get the annotated elements
        // get the annotated elements
        Set<? extends Element> annotatedElements = roundEnv.getElementsAnnotatedWith(HtmlForm.class);
        for (Element element : annotatedElements) {
            // check if it's a class
            if (element.getKind() == ElementKind.CLASS) {
                // get the annotation
                HtmlForm annotation = element.getAnnotation(HtmlForm.class);
                // get the annotation's values
                String fileName = annotation.fileName();
                String action = annotation.action();
                String method = annotation.method();
                // create the html file
                String html = "<form action = \"" + action + "\" method = \"" + method + "\">\n";
                // get the annotated fields
                Set<? extends Element> annotatedFields = roundEnv.getElementsAnnotatedWith(HtmlInput.class);
                for (Element field : annotatedFields) {
                    // check if it's a field
                    if (field.getKind() == ElementKind.FIELD) {
                        // get the annotation
                        HtmlInput inputAnnotation = field.getAnnotation(HtmlInput.class);
                        // get the annotation's values
                        String type = inputAnnotation.type();
                        String name = inputAnnotation.name();
                        String placeholder = inputAnnotation.placeholder();
                        // add the input to the html file
                        html += "\t<input type = \"" + type + "\" name = \"" + name + "\" placeholder = \""
                                + placeholder
                                + "\">\n";
                    }
                }
                html += "\t<input type = \"submit\" value = \"Send\">\n";
                html += "</form>";
                // write to file
                writeToFile(fileName, html);
            }
        }
        // print a message to the compiler
        messager.printMessage(Diagnostic.Kind.NOTE, "Html file created successfully");
        return true;
    }

    // @Override
    // public boolean process(Set<? extends TypeElement> annotations,
    // RoundEnvironment roundEnv) {

    // Messager messager = processingEnv.getMessager();
    // messager.printMessage(Diagnostic.Kind.NOTE, "Processing round started");
    // for (TypeElement annotation : annotations) {
    // Set<? extends Element> annotatedElements =
    // roundEnv.getElementsAnnotatedWith(annotation);
    // for (Element element : annotatedElements) {
    // // create a file to make sure the method was invoked
    // try {
    // File file = new File("method.java");
    // file.createNewFile();
    // FileWriter fileWriter = new FileWriter(file);
    // fileWriter.write("hey");
    // fileWriter.close();
    // } catch (IOException e) {
    // //
    // }
    // }
    // }
    // return true;
    // }

}