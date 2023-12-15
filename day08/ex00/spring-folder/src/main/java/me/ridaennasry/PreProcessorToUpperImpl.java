
package fr.fortytwo;

import fr.fortytwo.PreProcessor;

public class PreProcessorToUpperImpl implements PreProcessor {

    public PreProcessorToUpperImpl() {
        
    }
    public String preProcess(String letters) {
        return letters.toUpperCase();
    }
}
