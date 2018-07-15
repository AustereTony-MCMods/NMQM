package ru.austeretony.nmqm.coremod;

import java.util.Iterator;

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
import ru.austeretony.nmqm.main.NMQMMain;

public class NMQMClassTransformer implements IClassTransformer {
	
	private static final String HOOKS_CLASS = "ru/austeretony/nmqm/coremod/NMQMHooks";
	
	public NMQMClassTransformer() {
				
		try {
			
			ConfigLoader.loadConfiguration();
		}
		
		catch (JsonSyntaxException exception) {
			
			NMQMMain.LOGGER.println("[NMQM Core][ERROR] Config parsing failure! Fix syntax errors!");
			
			exception.printStackTrace();
		}
	}

    @Override
    public byte[] transform(String name, String transformedName, byte[] basicClass) {   
    	
    	switch (name) {
    	
    		case "net.minecraft.inventory.Container":   			
    			if (ConfigLoader.isDebugModeEnabled())
    				return patchContainer(basicClass, false);   			
    		case "uy":   			
    			if (ConfigLoader.isDebugModeEnabled())
    				return patchContainer(basicClass, true);
    			
    			
			case "net.minecraft.client.gui.inventory.GuiContainer":							
	    		return patchGuiContainer(basicClass, false);
			case "awy":					
				return patchGuiContainer(basicClass, true);			
	    		
    		
    		case "net.minecraft.network.packet.Packet102WindowClick":
				return patchClickWindowPacket(basicClass, false);
    		case "du":
				return patchClickWindowPacket(basicClass, true);
    	}
    	
		return basicClass;
    }

	private byte[] patchContainer(byte[] basicClass, boolean obfuscated) {
		
	    ClassNode classNode = new ClassNode();
	    ClassReader classReader = new ClassReader(basicClass);
	    classReader.accept(classNode, 0);
	    
	    String 
	    slotClickMethodName = obfuscated ? "a" : "slotClick",
	    containerClassName = obfuscated ? "uy" : "net/minecraft/inventory/Container",
	    itemStackClassName = obfuscated ? "ye" : "net/minecraft/item/ItemStack",
	    entityPlayerClassName = obfuscated ? "uf" : "net/minecraft/entity/player/EntityPlayer";
	 		    
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
                    	nodesList.add(new MethodInsnNode(Opcodes.INVOKESTATIC, HOOKS_CLASS, "getContainerClassName", "(L" + containerClassName + ";L" + entityPlayerClassName + ";)V"));

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
	    	NMQMMain.LOGGER.println("[NMQM Core] <Container.class> patched!");   
	    	    
	    return writer.toByteArray();	
	}
	
	private byte[] patchGuiContainer(byte[] basicClass, boolean obfuscated) {
        
	    ClassNode classNode = new ClassNode();
        ClassReader classReader = new ClassReader(basicClass);
        classReader.accept(classNode, 0);
        
	 	String 
	 	handleMouseClickMethodName = obfuscated ? "a" : "handleMouseClick",
	 	slotClassName = obfuscated ? "we" : "net/minecraft/inventory/Slot";  
	 	
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
	                    	nodesList.add(new MethodInsnNode(Opcodes.INVOKESTATIC, HOOKS_CLASS, "verifyQuickMoveClient", "(I)I"));          
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
	    	NMQMMain.LOGGER.println("[NMQM Core] <GuiContainer.class> patched!"); 

        return writer.toByteArray();				
	}
	
	private byte[] patchClickWindowPacket(byte[] basicClass, boolean obfuscated) {
		
	    ClassNode classNode = new ClassNode();
	    ClassReader classReader = new ClassReader(basicClass);
	    classReader.accept(classNode, 0);
	    
	    String 
	    modeFieldName = obfuscated ? "d" : "action",
	    processPacketMethodName = obfuscated ? "a" : "processPacket",
	    iNetHandlerPlayServerClassName = obfuscated ? "ez" : "net/minecraft/network/packet/NetHandler",
	    cPacketClickWindowClassName = obfuscated ? "du" : "net/minecraft/network/packet/Packet102WindowClick";
	 		    
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
                    	nodesList.add(new FieldInsnNode(Opcodes.GETFIELD, cPacketClickWindowClassName, modeFieldName, "S"));
                    	nodesList.add(new MethodInsnNode(Opcodes.INVOKESTATIC, HOOKS_CLASS, "verifyQuickMoveServer", "(L" + iNetHandlerPlayServerClassName + ";I)I"));
                    	nodesList.add(new FieldInsnNode(Opcodes.PUTFIELD, cPacketClickWindowClassName, modeFieldName, "S"));
                    	
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
	    	NMQMMain.LOGGER.println("[NMQM Core] <C0EPacketClickWindow.class> patched!");   
	    	    
	    return writer.toByteArray();	
	}
}

