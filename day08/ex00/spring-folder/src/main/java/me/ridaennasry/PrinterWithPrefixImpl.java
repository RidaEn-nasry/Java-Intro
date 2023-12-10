package me.ridaennasry;

import me.ridaennasry.Printer;
import me.ridaennasry.Renderer;

public class PrinterWithPrefixImpl implements Printer {

    private Renderer renderer;
    private String prefix;

    public PrinterWithPrefixImpl(Renderer renderer) {
        this.renderer = renderer;
    }

    public void print(String toPrint) {
        if (this.prefix != null && this.prefix.length() > 0) {

            this.renderer.render(this.prefix + " " + toPrint);
        } else {
            this.renderer.render(toPrint);
        }
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

}