package ru.austeretony.nmqm.coremod;

import java.util.Map;

import cpw.mods.fml.relauncher.IFMLLoadingPlugin;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin.TransformerExclusions;

@TransformerExclusions({"ru.austeretony.nmqm.coremod"})
public class NMQMCorePlugin implements IFMLLoadingPlugin {
		
    @Override
    public String[] getASMTransformerClass() {
    	
        return new String[] {NMQMClassTransformer.class.getName()};
    }

    @Override
    public String getModContainerClass() {
    	
        return null;
    }

    @Override
    public String getSetupClass() {
    	
        return null;
    }

    @Override
    public void injectData(Map<String, Object> data) {}

    @Override
    public String getAccessTransformerClass() {
    	
        return null;
    }
}
