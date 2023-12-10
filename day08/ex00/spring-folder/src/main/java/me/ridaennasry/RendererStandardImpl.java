
package me.ridaennasry;

import me.ridaennasry.Renderer;
import me.ridaennasry.PreProcessor;

public class RendererStandardImpl implements Renderer {

    private PreProcessor preProcessor;

    public RendererStandardImpl(PreProcessor preProcessor) {
        this.preProcessor = preProcessor;
    }

    public void render(String toRender) {
        String precessedString = this.preProcessor.preProcess(toRender);
        System.out.println(precessedString);
    }
}