package austeretony.nmqm.common.core;

import java.util.Map;

import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin.TransformerExclusions;

@TransformerExclusions({"austeretony.nmqm.common.core"})
public class NMQMCorePlugin implements IFMLLoadingPlugin {
		
    private static boolean isObfuscated;
	
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
    public void injectData(Map<String, Object> data) {
    	isObfuscated = (boolean) data.get("runtimeDeobfuscationEnabled");
    }

    @Override
    public String getAccessTransformerClass() {  	
        return null;
    }
    
    public static boolean isObfuscated() {   	
    	return isObfuscated;
    }
}
