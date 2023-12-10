
package me.ridaennasry;

import me.ridaennasry.PreProcessor;

public class PreProcessorToLowerImpl implements PreProcessor {

    public PreProcessorToLowerImpl() {

    }

    public String preProcess(String letters) {
        return letters.toLowerCase();
    }
}