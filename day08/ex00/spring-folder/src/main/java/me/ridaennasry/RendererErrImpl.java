
package fr.fortytwo;

import fr.fortytwo.Renderer;
import fr.fortytwo.PreProcessor;

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