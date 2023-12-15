
package fr.fortytwo;

import fr.fortytwo.Renderer;
import fr.fortytwo.PreProcessor;

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