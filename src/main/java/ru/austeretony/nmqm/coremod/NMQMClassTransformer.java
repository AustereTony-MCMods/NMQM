package ru.austeretony.nmqm.coremod;

import java.util.Iterator;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

import com.google.gson.JsonSyntaxException;

import net.minecraft.launchwrapper.IClassTransformer;
import ru.austeretony.nmqm.main.ConfigLoader;

public class NMQMClassTransformer implements IClassTransformer {

	public static final Logger LOGGER = LogManager.getLogger("NMQM Core");
	
	private static final String HOOKS_CLASS = "ru/austeretony/nmqm/coremod/NMQMHooks";
	
	public NMQMClassTransformer() {
				
		try {
			
			ConfigLoader.loadConfiguration();
		}
		
		catch (JsonSyntaxException exception) {
			
			LOGGER.error("Config parsing failure! Fix syntax errors!");
			
			exception.printStackTrace();
		}
	}

    @Override
    public byte[] transform(String name, String transformedName, byte[] basicClass) {   
    	
    	switch (name) {
    	
    		case "net.minecraft.inventory.Container":   			
    			if (ConfigLoader.isDebugModeEnabled())
    				return patchContainer(basicClass, false);   			
    		case "aau":   			
    			if (ConfigLoader.isDebugModeEnabled())
    				return patchContainer(basicClass, true);
    			
    			
			case "net.minecraft.client.gui.inventory.GuiContainer":							
	    		return patchGuiContainer(basicClass, false);		
			case "bfr":					
				return patchGuiContainer(basicClass, true);		
	    		
    		
    		case "net.minecraft.network.play.client.CPacketClickWindow":
				return patchCLickWindowPacket(basicClass, false);
    		case "ip":
				return patchCLickWindowPacket(basicClass, true);
    	}
    	
		return basicClass;
    }

	private byte[] patchContainer(byte[] basicClass, boolean obfuscated) {
		
	    ClassNode classNode = new ClassNode();
	    ClassReader classReader = new ClassReader(basicClass);
	    classReader.accept(classNode, 0);
	    
	    String 
	    slotClickMethodName = obfuscated ? "a" : "slotClick",
	    containerClassName = obfuscated ? "aau" : "net/minecraft/inventory/Container",
	    itemStackClassName = obfuscated ? "adq" : "net/minecraft/item/ItemStack",
	    clickTypeClassName = obfuscated ? "aaz" : "net/minecraft/inventory/ClickType",
	    entityPlayerClassName = obfuscated ? "zj" : "net/minecraft/entity/player/EntityPlayer";
	 		    
        boolean isSuccessful = false;
        
        AbstractInsnNode currentInsn;
	 		    
	    for (MethodNode methodNode : classNode.methods) {
	    	
			if (methodNode.name.equals(slotClickMethodName) && methodNode.desc.equals("(IIL" + clickTypeClassName + ";L" + entityPlayerClassName + ";)L" + itemStackClassName + ";")) {		
	    	
	            Iterator<AbstractInsnNode> insnIterator = methodNode.instructions.iterator();
	           
	            while (insnIterator.hasNext()) {
	            	
	                currentInsn = insnIterator.next(); 
	                
	                if (currentInsn.getOpcode() == Opcodes.ALOAD) {
	                	
                    	InsnList nodesList = new InsnList();
                
                    	nodesList.add(new VarInsnNode(Opcodes.ALOAD, 0));
                    	nodesList.add(new VarInsnNode(Opcodes.ALOAD, 4));
                    	nodesList.add(new MethodInsnNode(Opcodes.INVOKESTATIC, HOOKS_CLASS, "getContainerClassName", "(L" + containerClassName + ";L" + entityPlayerClassName + ";)V", false));

                        methodNode.instructions.insertBefore(currentInsn, nodesList); 

                        isSuccessful = true;
                        
                        break;
	                }
	            }	                      
	            
	            break;
			}
	    }

	    ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);	    
	    classNode.accept(writer);
	    
	    if (isSuccessful)
	    	LOGGER.info("<Container.class> patched!");   
	    	    
	    return writer.toByteArray();	
	}
	
	private byte[] patchGuiContainer(byte[] basicClass, boolean obfuscated) {
        
	    ClassNode classNode = new ClassNode();
        ClassReader classReader = new ClassReader(basicClass);
        classReader.accept(classNode, 0);
        
	 	String 
	 	handleMouseClickMethodName = obfuscated ? "a" : "handleMouseClick",
	 	clickTypeClassName = obfuscated ? "aaz" : "net/minecraft/inventory/ClickType",
	 	slotClassName = obfuscated ? "abt" : "net/minecraft/inventory/Slot";  
	 	
        boolean isSuccessful = false;
        
        int aloadCount = 0;
        
        AbstractInsnNode currentInsn;
        
		for (MethodNode methodNode : classNode.methods) {
			
			if (methodNode.name.equals(handleMouseClickMethodName) && methodNode.desc.equals("(L" + slotClassName + ";IIL" + clickTypeClassName + ";)V")) {
                
                Iterator<AbstractInsnNode> insnIterator = methodNode.instructions.iterator();
               
                while (insnIterator.hasNext()) {
                	
                    currentInsn = insnIterator.next(); 
                    
                    if (currentInsn.getOpcode() == Opcodes.ALOAD) {
                    	
                    	aloadCount++;
                    		
                    	if (aloadCount == 3) {
                    		
	                        InsnList nodesList = new InsnList();
	                    	
	                        nodesList.add(new VarInsnNode(Opcodes.ALOAD, 4));
	                    	nodesList.add(new MethodInsnNode(Opcodes.INVOKESTATIC, HOOKS_CLASS, "verifyQuickMoveClient", "(L" + clickTypeClassName + ";)L" + clickTypeClassName + ";", false));          
	                        nodesList.add(new VarInsnNode(Opcodes.ASTORE, 4));
	                        
	                        methodNode.instructions.insertBefore(currentInsn, nodesList); 
	                    	
	                    	break;
                    	}
                    }
                }    
                
                isSuccessful = true;
				
				break;
			}
		}
		
	    ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);	    
	    classNode.accept(writer);
	    
	    if (isSuccessful)
	    	LOGGER.info("<GuiContainer.class> patched!"); 

        return writer.toByteArray();				
	}
	
	private byte[] patchCLickWindowPacket(byte[] basicClass, boolean obfuscated) {
		
	    ClassNode classNode = new ClassNode();
	    ClassReader classReader = new ClassReader(basicClass);
	    classReader.accept(classNode, 0);
	    
	    String 
	    modeFieldName = obfuscated ? "f" : "mode",
	    processPacketMethodName = obfuscated ? "a" : "processPacket",
	    clickTypeClassName = obfuscated ? "aaz" : "net/minecraft/inventory/ClickType",
	    iNetHandlerPlayServerClassName = obfuscated ? "ih" : "net/minecraft/network/play/INetHandlerPlayServer",
	    cPacketClickWindowClassName = obfuscated ? "ip" : "net/minecraft/network/play/client/CPacketClickWindow";
	 		    
        boolean isSuccessful = false;
        
        AbstractInsnNode currentInsn;
	 		    
	    for (MethodNode methodNode : classNode.methods) {
	    	
			if (methodNode.name.equals(processPacketMethodName) && methodNode.desc.equals("(L" + iNetHandlerPlayServerClassName + ";)V")) {		
	    	
	            Iterator<AbstractInsnNode> insnIterator = methodNode.instructions.iterator();
	           
	            while (insnIterator.hasNext()) {
	            	
	                currentInsn = insnIterator.next(); 
	                
	                if (currentInsn.getOpcode() == Opcodes.ALOAD) {
	                	
                    	InsnList nodesList = new InsnList();
                
                    	nodesList.add(new VarInsnNode(Opcodes.ALOAD, 0));
                    	nodesList.add(new VarInsnNode(Opcodes.ALOAD, 1));
                    	nodesList.add(new VarInsnNode(Opcodes.ALOAD, 0));
                    	nodesList.add(new FieldInsnNode(Opcodes.GETFIELD, cPacketClickWindowClassName, modeFieldName, "L" + clickTypeClassName + ";"));
                    	nodesList.add(new MethodInsnNode(Opcodes.INVOKESTATIC, HOOKS_CLASS, "verifyQuickMoveServer", "(L" + iNetHandlerPlayServerClassName + ";L" + clickTypeClassName + ";)L" + clickTypeClassName + ";", false));
                    	nodesList.add(new FieldInsnNode(Opcodes.PUTFIELD, cPacketClickWindowClassName, modeFieldName, "L" + clickTypeClassName + ";"));
                    	
                        methodNode.instructions.insertBefore(currentInsn, nodesList); 

                        isSuccessful = true;
                        
                        break;
	                }
	            }	                      
	            
	            break;
			}
	    }

	    ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);	    
	    classNode.accept(writer);
	    
	    if (isSuccessful)
	    	LOGGER.info("<CPacketClickWindow.class> patched!");   
	    	    
	    return writer.toByteArray();	
	}
}

