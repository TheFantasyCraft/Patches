import mcpc.patchengine.api.IPatch;
import mcpc.patchengine.asm.util.ClassUtil;
import mcpc.patchengine.asm.util.MethodUtil;
import mcpc.patchengine.common.Configuration;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

import java.util.logging.Logger;

/**
 * Created by thomas on 8/11/2014.
 */
public class UnifierDupeFix implements IPatch {

    private String Classname = "powercrystals.minefactoryreloaded.processing.TileEntityUnifier";

    @Override
    public String[] getClassNames() {
        return new String[]{Classname};
    }

    @Override
    public void loadConfigurations(Configuration configuration) {

    }

    @Override
    public void transform(String s, ClassNode classNode) {
        if (s.equals(Classname)){

            MethodNode methodNode = ClassUtil.getMethod(classNode, "moveItemStack", "(Lur;)V");
            methodNode.maxStack = 4;
            methodNode.maxLocals = 4;

            if (methodNode == null){
                Logger.getLogger("Minecraft").info("Tom ur are a noob");
                return;
            }
            //INVOKEVIRTUAL powercrystals/minefactoryreloaded/processing/TileEntityUnifier.c ()I


            LabelNode node1 = MethodUtil.getLabelWithInsnList(methodNode, new VarInsnNode(Opcodes.ALOAD, 0), new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "powercrystals/minefactoryreloaded/processing/TileEntityUnifier", "c", "()I"),new InsnNode(Opcodes.AALOAD) );
            clearlabel(node1, methodNode);

            //methodNode.instructions.insert(l1, new JumpInsnNode(Opcodes.GOTO, l1));

            /*
                L1
                ALOAD 0
                GETFIELD powercrystals/minefactoryreloaded/processing/TileEntityUnifier._inventory : [Lur;
                ICONST_0
                AALOAD
                GETFIELD ur.a : I

                ALOAD 0
                GETFIELD powercrystals/minefactoryreloaded/processing/TileEntityUnifier._inventory : [Lur;
                ICONST_1
                AALOAD
                INVOKEVIRTUAL ur.d ()I

                ALOAD 0
                GETFIELD powercrystals/minefactoryreloaded/processing/TileEntityUnifier._inventory : [Lur;
                ICONST_1
                AALOAD
                GETFIELD ur.a : I

                ISUB

                INVOKESTATIC java/lang/Math.min (II)I

                ISTORE 2

                L2


             */
            InsnList changeline = new InsnList();
            changeline.add(new VarInsnNode(Opcodes.ALOAD, 0));
            changeline.add(new FieldInsnNode(Opcodes.GETFIELD, "powercrystals/minefactoryreloaded/processing/TileEntityUnifier", "_inventory", "[Lur;"));
            changeline.add(new InsnNode(Opcodes.ICONST_0));
            changeline.add(new InsnNode(Opcodes.AALOAD));
            changeline.add( new FieldInsnNode( Opcodes.GETFIELD, "ur", "a", "I") );

            changeline.add(new VarInsnNode(Opcodes.ALOAD, 0));
            changeline.add(new FieldInsnNode(Opcodes.GETFIELD, "powercrystals/minefactoryreloaded/processing/TileEntityUnifier", "_inventory", "[Lur;"));
            changeline.add(new InsnNode(Opcodes.ICONST_0));
            changeline.add(new InsnNode(Opcodes.AALOAD));
            changeline.add( new MethodInsnNode( Opcodes.INVOKEVIRTUAL, "ur", "d", "()I") );


            changeline.add(new VarInsnNode(Opcodes.ALOAD, 0));
            changeline.add(new FieldInsnNode(Opcodes.GETFIELD, "powercrystals/minefactoryreloaded/processing/TileEntityUnifier", "_inventory", "[Lur;"));
            changeline.add(new InsnNode(Opcodes.ICONST_1));
            changeline.add(new InsnNode(Opcodes.AALOAD));
            changeline.add( new FieldInsnNode( Opcodes.GETFIELD, "ur", "a", "I") );

            changeline.add( new InsnNode(Opcodes.ISUB));

            changeline.add( new MethodInsnNode(Opcodes.INVOKESTATIC, "java/lang/Math","min", "(II)I" ) );

            changeline.add( new VarInsnNode(Opcodes.ISTORE ,2) );


            methodNode.instructions.insert(node1, changeline);







           // delay();

        }
    }

    public static void clearlabel(LabelNode node, MethodNode methodNode){
        while (!(node.getNext() instanceof LabelNode))
        {
            methodNode.instructions.remove(node.getNext());
        }
    }

    public void delay(){
        try {
            Thread.sleep(8000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
