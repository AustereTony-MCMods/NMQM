package austeretony.nmqm.common.core;

import java.util.Iterator;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

public enum EnumInputClasses {

    MC_CONTAINER("Minecraft", "Container", 0, ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES),
    MC_GUI_CONTAINER("Minecraft", "GuiContainer", 0, ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES),
    MC_CPACKET_CLICK_WINDOW("Minecraft", "CPacketClickWindow", 0, ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);

    private static final String HOOKS_CLASS = "austeretony/nmqm/common/core/NMQMHooks";

    public final String domain, clazz;

    public final int readerFlags, writerFlags;

    EnumInputClasses(String domain, String clazz, int readerFlags, int writerFlags) {
        this.domain = domain;
        this.clazz = clazz;
        this.readerFlags = readerFlags;
        this.writerFlags = writerFlags;
    }

    public boolean patch(ClassNode classNode) {
        switch (this) {
        case MC_CONTAINER:
            return pathcMCContainer(classNode);
        case MC_GUI_CONTAINER:
            return patchMCGuiContainer(classNode);
        case MC_CPACKET_CLICK_WINDOW:
            return patchMCCPacketClickWindow(classNode);
        }
        return false;
    }

    private boolean pathcMCContainer(ClassNode classNode) {
        String 
        slotClickMethodName = NMQMCorePlugin.isObfuscated() ? "a" : "slotClick",
                containerClassName = NMQMCorePlugin.isObfuscated() ? "afr" : "net/minecraft/inventory/Container",
                        itemStackClassName = NMQMCorePlugin.isObfuscated() ? "aip" : "net/minecraft/item/ItemStack",
                                clickTypeClassName = NMQMCorePlugin.isObfuscated() ? "afw" : "net/minecraft/inventory/ClickType",
                                        entityPlayerClassName = NMQMCorePlugin.isObfuscated() ? "aed" : "net/minecraft/entity/player/EntityPlayer";                         
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
        return isSuccessful;
    }

    private boolean patchMCGuiContainer(ClassNode classNode) {
        String 
        handleMouseClickMethodName = NMQMCorePlugin.isObfuscated() ? "a" : "handleMouseClick",
                clickTypeClassName = NMQMCorePlugin.isObfuscated() ? "afw" : "net/minecraft/inventory/ClickType",
                        slotClassName = NMQMCorePlugin.isObfuscated() ? "agr" : "net/minecraft/inventory/Slot";                 
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
                            isSuccessful = true;                            
                            break;
                        }
                    }
                }                  
                break;
            }
        }
        return isSuccessful;
    }

    private boolean patchMCCPacketClickWindow(ClassNode classNode) {
        String 
        modeFieldName = NMQMCorePlugin.isObfuscated() ? "f" : "mode",
                processPacketMethodName = NMQMCorePlugin.isObfuscated() ? "a" : "processPacket",
                        clickTypeClassName = NMQMCorePlugin.isObfuscated() ? "afw" : "net/minecraft/inventory/ClickType",
                                iNetHandlerPlayServerClassName = NMQMCorePlugin.isObfuscated() ? "kx" : "net/minecraft/network/play/INetHandlerPlayServer",
                                        cPacketClickWindowClassName = NMQMCorePlugin.isObfuscated() ? "lf" : "net/minecraft/network/play/client/CPacketClickWindow";                        
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
        return isSuccessful;
    }
}
