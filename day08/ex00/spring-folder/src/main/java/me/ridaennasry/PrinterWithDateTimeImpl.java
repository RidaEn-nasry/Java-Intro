package fr.fortytwo;

import fr.fortytwo.Printer;
import fr.fortytwo.Renderer;
import java.time.LocalDateTime;

import java.time.format.DateTimeFormatter;

public class PrinterWithDateTimeImpl implements Printer {
    private Renderer renderer;

    public PrinterWithDateTimeImpl(Renderer renderer) {
        this.renderer = renderer;
    }

    private String getCurrentTimeAsString() {
        LocalDateTime dateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yy-mm-dd/hh-mm-ss");
        return dateTime.format(formatter);
    }

    public void print(String toPrint) {
        this.renderer.render(getCurrentTimeAsString() + ": " + toPrint);
    }

}
