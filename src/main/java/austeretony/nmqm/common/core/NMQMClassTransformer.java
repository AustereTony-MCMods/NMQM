package austeretony.nmqm.common.core;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.ClassNode;

import net.minecraft.launchwrapper.IClassTransformer;

public class NMQMClassTransformer implements IClassTransformer {

    public static final Logger CORE_LOGGER = LogManager.getLogger("NMQM Core");

    @Override
    public byte[] transform(String name, String transformedName, byte[] basicClass) {   
        switch (transformedName) {
        case "net.minecraft.inventory.Container":                    
            return patch(basicClass, EnumInputClasses.MC_CONTAINER);
        case "net.minecraft.client.gui.inventory.GuiContainer":                    
            return patch(basicClass, EnumInputClasses.MC_GUI_CONTAINER); 
        case "net.minecraft.network.play.client.CPacketClickWindow":                    
            return patch(basicClass, EnumInputClasses.MC_CPACKET_CLICK_WINDOW);        
        }       
        return basicClass;
    }

    private byte[] patch(byte[] basicClass, EnumInputClasses enumInput) {
        ClassNode classNode = new ClassNode();
        ClassReader classReader = new ClassReader(basicClass);
        classReader.accept(classNode, enumInput.readerFlags);
        if (enumInput.patch(classNode))
            CORE_LOGGER.info(enumInput.domain + " <" + enumInput.clazz + ".class> patched!");
        ClassWriter writer = new ClassWriter(enumInput.writerFlags);        
        classNode.accept(writer);
        return writer.toByteArray();  
    }
}

