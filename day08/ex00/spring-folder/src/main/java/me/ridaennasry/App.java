
package me.ridaennasry;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import me.ridaennasry.PreProcessor;
import me.ridaennasry.PreProcessorToUpperImpl;
import me.ridaennasry.RendererErrImpl;
import me.ridaennasry.Printer;
import me.ridaennasry.PrinterWithPrefixImpl;

public class App {
    public static void main(String[] args) {

        // Standard way of intra-dependencies instantition /

        /*
         * PreProcessor preProcessor = new PreProcessorToUpperImpl();
         * Renderer renderer = new RendererErrImpl(preProcessor);
         * PrinterWithPrefixImpl printer = new PrinterWithPrefixImpl(renderer);
         * printer.setPrefix("Prefix");
         * printer.print("Hello!");
         */

        // using Spring IOC //
        ApplicationContext context = new ClassPathXmlApplicationContext("context.xml");
        Printer printer = context.getBean("printerWithPrefix", Printer.class);
        printer.print("Hello!");
    }
}
