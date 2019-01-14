package austeretony.nmqm.common.core;

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

import net.minecraft.launchwrapper.IClassTransformer;

public class NMQMClassTransformer implements IClassTransformer {

	public static final Logger LOGGER = LogManager.getLogger("NMQM Core");
	
	private static final String HOOKS_CLASS = "austeretony/nmqm/common/core/NMQMHooks";

    @Override
    public byte[] transform(String name, String transformedName, byte[] basicClass) {   
    	switch (transformedName) {   	
    		case "net.minecraft.inventory.Container":   			
    			return patchContainer(basicClass);   			 			
			case "net.minecraft.client.gui.inventory.GuiContainer":							
	    		return patchGuiContainer(basicClass);		
    		case "net.minecraft.network.play.client.C0EPacketClickWindow":
				return patchClickWindowPacket(basicClass);
    	}   	
		return basicClass;
    }

	private byte[] patchContainer(byte[] basicClass) {		
	    ClassNode classNode = new ClassNode();
	    ClassReader classReader = new ClassReader(basicClass);
	    classReader.accept(classNode, 0);	 
	    
	    String 
	    slotClickMethodName = NMQMCorePlugin.isObfuscated() ? "a" : "slotClick",
	    containerClassName = NMQMCorePlugin.isObfuscated() ? "zs" : "net/minecraft/inventory/Container",
	    itemStackClassName = NMQMCorePlugin.isObfuscated() ? "add" : "net/minecraft/item/ItemStack",
	    entityPlayerClassName = NMQMCorePlugin.isObfuscated() ? "yz" : "net/minecraft/entity/player/EntityPlayer";	 		    
        boolean isSuccessful = false;        
        AbstractInsnNode currentInsn;
        
	    for (MethodNode methodNode : classNode.methods) {	    	
			if (methodNode.name.equals(slotClickMethodName) && methodNode.desc.equals("(IIIL" + entityPlayerClassName + ";)L" + itemStackClassName + ";")) {			    	
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
	
	private byte[] patchGuiContainer(byte[] basicClass) {       
	    ClassNode classNode = new ClassNode();
        ClassReader classReader = new ClassReader(basicClass);
        classReader.accept(classNode, 0);
        
	 	String 
	 	handleMouseClickMethodName = NMQMCorePlugin.isObfuscated() ? "a" : "handleMouseClick",
	 	slotClassName = NMQMCorePlugin.isObfuscated() ? "aay" : "net/minecraft/inventory/Slot";  	 	
        boolean isSuccessful = false;        
        int aloadCount = 0;        
        AbstractInsnNode currentInsn;
        
		for (MethodNode methodNode : classNode.methods) {			
			if (methodNode.name.equals(handleMouseClickMethodName) && methodNode.desc.equals("(L" + slotClassName + ";III)V")) {               
                Iterator<AbstractInsnNode> insnIterator = methodNode.instructions.iterator();               
                while (insnIterator.hasNext()) {                	
                    currentInsn = insnIterator.next();                     
                    if (currentInsn.getOpcode() == Opcodes.ALOAD) {                    	
                    	aloadCount++;                    		
                    	if (aloadCount == 3) {                    		
	                        InsnList nodesList = new InsnList();	                    	
	                        nodesList.add(new VarInsnNode(Opcodes.ILOAD, 4));
	                    	nodesList.add(new MethodInsnNode(Opcodes.INVOKESTATIC, HOOKS_CLASS, "verifyQuickMoveClient", "(I)I", false));          
	                        nodesList.add(new VarInsnNode(Opcodes.ISTORE, 4));	                        
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
	
	private byte[] patchClickWindowPacket(byte[] basicClass) {		
	    ClassNode classNode = new ClassNode();
	    ClassReader classReader = new ClassReader(basicClass);
	    classReader.accept(classNode, 0);
	    
	    String 
	    modeFieldName = NMQMCorePlugin.isObfuscated() ? "f" : "field_149549_f",
	    processPacketMethodName = NMQMCorePlugin.isObfuscated() ? "a" : "processPacket",
	    iNetHandlerPlayServerClassName = NMQMCorePlugin.isObfuscated() ? "io" : "net/minecraft/network/play/INetHandlerPlayServer",
	    cPacketClickWindowClassName = NMQMCorePlugin.isObfuscated() ? "ix" : "net/minecraft/network/play/client/C0EPacketClickWindow";	 		    
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
                    	nodesList.add(new FieldInsnNode(Opcodes.GETFIELD, cPacketClickWindowClassName, modeFieldName, "I"));
                    	nodesList.add(new MethodInsnNode(Opcodes.INVOKESTATIC, HOOKS_CLASS, "verifyQuickMoveServer", "(L" + iNetHandlerPlayServerClassName + ";I)I", false));
                    	nodesList.add(new FieldInsnNode(Opcodes.PUTFIELD, cPacketClickWindowClassName, modeFieldName, "I"));                    	
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
	    	LOGGER.info("<C0EPacketClickWindow.class> patched!");   
	    	    
	    return writer.toByteArray();	
	}
}

