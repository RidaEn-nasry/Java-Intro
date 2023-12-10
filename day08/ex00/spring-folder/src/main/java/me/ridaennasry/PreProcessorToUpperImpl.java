
package me.ridaennasry;

import me.ridaennasry.PreProcessor;

public class PreProcessorToUpperImpl implements PreProcessor {

    public PreProcessorToUpperImpl() {
        
    }
    public String preProcess(String letters) {
        return letters.toUpperCase();
    }
}
