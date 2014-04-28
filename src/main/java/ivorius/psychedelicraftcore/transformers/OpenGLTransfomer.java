/*
 *  Copyright (c) 2014, Lukas Tenbrink.
 *  * http://lukas.axxim.net
 */

package ivorius.psychedelicraftcore.transformers;

import ivorius.psychedelicraftcore.ivToolkit.IvClassTransformerGeneral;
import ivorius.psychedelicraftcore.ivToolkit.IvInsnHelper;
import ivorius.psychedelicraftcore.ivToolkit.IvNodeFinder;
import ivorius.psychedelicraftcore.ivToolkit.IvNodeMatcherMethodSRG;
import org.apache.logging.log4j.Logger;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.*;

import java.util.List;

import static org.objectweb.asm.Opcodes.DUP;
import static org.objectweb.asm.Opcodes.INVOKESTATIC;

/**
 * Created by lukas on 12.03.14.
 */
public class OpenGLTransfomer extends IvClassTransformerGeneral
{
    public OpenGLTransfomer(Logger logger)
    {
        super(logger);
    }

    @Override
    public boolean transformMethod(String className, MethodNode methodNode, boolean obf)
    {
        int transformed = 0;

        transformed += proxyGLSwitchMethods(methodNode);
        transformed += proxyGLClearMethods(methodNode);

        return transformed > 0;
    }

    public static int proxyGLSwitchMethods(MethodNode methodNode)
    {
        int caught = 0;

        List<AbstractInsnNode> glEnableNodes = IvNodeFinder.findNodes(new IvNodeMatcherMethodSRG(INVOKESTATIC, "glEnable", "org/lwjgl/opengl/GL11", null), methodNode);
        List<AbstractInsnNode> glDisableNodes = IvNodeFinder.findNodes(new IvNodeMatcherMethodSRG(INVOKESTATIC, "glDisable", "org/lwjgl/opengl/GL11", null), methodNode);

        for (AbstractInsnNode callListNode : glEnableNodes)
        {
            InsnList listBefore = new InsnList();
            listBefore.add(new InsnNode(DUP));
            listBefore.add(new MethodInsnNode(INVOKESTATIC, "ivorius/psychedelicraftcore/PsycheCoreBusClient", "psycheGLEnable", getMethodDescriptor(Type.VOID_TYPE, Type.INT_TYPE)));
            methodNode.instructions.insertBefore(callListNode, listBefore);

            caught++;
        }

        for (AbstractInsnNode callListNode : glDisableNodes)
        {
            InsnList listBefore = new InsnList();
            listBefore.add(new InsnNode(DUP));
            listBefore.add(new MethodInsnNode(INVOKESTATIC, "ivorius/psychedelicraftcore/PsycheCoreBusClient", "psycheGLDisable", getMethodDescriptor(Type.VOID_TYPE, Type.INT_TYPE)));
            methodNode.instructions.insertBefore(callListNode, listBefore);

            caught++;
        }

        return caught;
    }

    public static int proxyGLClearMethods(MethodNode methodNode)
    {
        int caught = 0;

        List<AbstractInsnNode> glClearNodes = IvNodeFinder.findNodes(new IvNodeMatcherMethodSRG(INVOKESTATIC, "glClear", "org/lwjgl/opengl/GL11", null), methodNode);

        for (AbstractInsnNode callListNode : glClearNodes)
        {
            InsnList listBefore = new InsnList();
            listBefore.add(new MethodInsnNode(INVOKESTATIC, "ivorius/psychedelicraftcore/PsycheCoreBusClient", "psycheGLClear", getMethodDescriptor(Type.INT_TYPE, Type.INT_TYPE)));
            methodNode.instructions.insertBefore(callListNode, listBefore);

            caught++;
        }

        return caught;
    }

    public static int proxyGLMatrixMethods(MethodNode methodNode)
    {
        int caught = 0;

        List<AbstractInsnNode> glTranslatefNodes = IvNodeFinder.findNodes(new IvNodeMatcherMethodSRG(INVOKESTATIC, "glTranslatef", "org/lwjgl/opengl/GL11", null), methodNode);
        List<AbstractInsnNode> glRotatefNodes = IvNodeFinder.findNodes(new IvNodeMatcherMethodSRG(INVOKESTATIC, "glRotatef", "org/lwjgl/opengl/GL11", null), methodNode);
        List<AbstractInsnNode> glScalefNodes = IvNodeFinder.findNodes(new IvNodeMatcherMethodSRG(INVOKESTATIC, "glScalef", "org/lwjgl/opengl/GL11", null), methodNode);

        for (AbstractInsnNode callListNode : glTranslatefNodes)
        {
            InsnList listBefore = new InsnList();
            IvInsnHelper.insertDUP3(listBefore);
            listBefore.add(new MethodInsnNode(INVOKESTATIC, "ivorius/psychedelicraftcore/PsycheCoreBusClient", "psycheGLTranslatef", getMethodDescriptor(Type.VOID_TYPE, Type.FLOAT_TYPE, Type.FLOAT_TYPE, Type.FLOAT_TYPE)));
            methodNode.instructions.insertBefore(callListNode, listBefore);

            caught++;
        }

        for (AbstractInsnNode callListNode : glRotatefNodes)
        {
            InsnList listBefore = new InsnList();
            IvInsnHelper.insertDUP4(listBefore);
            listBefore.add(new MethodInsnNode(INVOKESTATIC, "ivorius/psychedelicraftcore/PsycheCoreBusClient", "psycheGLRotatef", getMethodDescriptor(Type.VOID_TYPE, Type.FLOAT_TYPE, Type.FLOAT_TYPE, Type.FLOAT_TYPE, Type.FLOAT_TYPE)));
            methodNode.instructions.insertBefore(callListNode, listBefore);

            caught++;
        }

        for (AbstractInsnNode callListNode : glScalefNodes)
        {
            InsnList listBefore = new InsnList();
            IvInsnHelper.insertDUP3(listBefore);
            listBefore.add(new MethodInsnNode(INVOKESTATIC, "ivorius/psychedelicraftcore/PsycheCoreBusClient", "psycheGLScalef", getMethodDescriptor(Type.VOID_TYPE, Type.FLOAT_TYPE, Type.FLOAT_TYPE, Type.FLOAT_TYPE)));
            methodNode.instructions.insertBefore(callListNode, listBefore);

            caught++;
        }

        return caught;
    }
}
