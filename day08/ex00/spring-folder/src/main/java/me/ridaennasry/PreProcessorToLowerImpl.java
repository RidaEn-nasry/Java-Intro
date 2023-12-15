
package fr.fortytwo;

import fr.fortytwo.PreProcessor;

public class PreProcessorToLowerImpl implements PreProcessor {

    public PreProcessorToLowerImpl() {

    }

    public String preProcess(String letters) {
        return letters.toLowerCase();
    }
}