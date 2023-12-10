
package me.ridaennasry;

import me.ridaennasry.Renderer;
import me.ridaennasry.PreProcessor;

public class RendererErrImpl implements Renderer {

    private PreProcessor preProcessor;

    public RendererErrImpl(PreProcessor preProcessor) {
        this.preProcessor = preProcessor;
    }

    public void render(String toRender) {
        String precessedString = this.preProcessor.preProcess(toRender);
        System.err.println(precessedString);
    }

}