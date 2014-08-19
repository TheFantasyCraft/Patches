import mcpc.patchengine.api.IPatch;
import mcpc.patchengine.asm.util.ClassUtil;
import mcpc.patchengine.asm.util.MethodUtil;
import mcpc.patchengine.asm.util.NavigationUtil;
import mcpc.patchengine.common.Configuration;
import mcpc.patchengine.common.Constants;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;
import org.objectweb.asm.util.Printer;
import org.objectweb.asm.util.Textifier;
import org.objectweb.asm.util.TraceMethodVisitor;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.logging.Logger;

/**
 * Created by thomas on 8/8/2014.
 */
public class RedPowerBlockBreakerFix implements IPatch {

    private String Classname = "com.eloraam.redpower.machine.TileBreaker";
    @Override
    public String[] getClassNames() {
        return new String[]{Classname};
    }

    @Override
    public void loadConfigurations(Configuration configuration) {

    }

    @Override
    public void transform(String s, ClassNode classNode) {
        if (s.equalsIgnoreCase(Classname)){
            MethodNode method = ClassUtil.getMethod(classNode, "onBlockNeighborChange", String.format("(I)V", new Object[]{"I"}));

            if (method == null)
                Logger.getLogger("Minecraft").info("Dangit :'(");
            else {

                method.maxStack = 10;
              //  printinstruction(method.instructions);

                //INVOKEVIRTUAL amq.getBlockDropped (Lyc;IIIII)Ljava/util/ArrayList;

                LabelNode LabelMethode1 = MethodUtil.getLabelWithInsn(method, new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "amq", "getBlockDropped", "(Lyc;IIIII)Ljava/util/ArrayList;"));

                LabelNode LabelMethode2 = MethodUtil.getLabelWithInsn(method, new MethodInsnNode(Opcodes.INVOKEVIRTUAL, Constants.WorldClass, "e", "(IIII)Z"));



                /*  ALOAD 0
                    GETFIELD com/eloraam/redpower/machine/TileBreaker.k : Lyc;
                    ALOAD 3
                    GETFIELD com/eloraam/redpower/core/WorldCoord.x : I
                    ALOAD 3
                    GETFIELD com/eloraam/redpower/core/WorldCoord.y : I
                    ALOAD 3
                    GETFIELD com/eloraam/redpower/core/WorldCoord.z : I
                    ICONST_0
                    */

                InsnList blockbreakcode = new InsnList();
                blockbreakcode.add( new VarInsnNode(Opcodes.ALOAD, 0 ) );
                blockbreakcode.add( new FieldInsnNode( Opcodes.GETFIELD, "com/eloraam/redpower/machine/TileBreaker", "k", "Lyc;" ) );
                blockbreakcode.add( new VarInsnNode(Opcodes.ALOAD, 3 ) );
                blockbreakcode.add( new FieldInsnNode( Opcodes.GETFIELD, "com/eloraam/redpower/core/WorldCoord", "x", "I" ) );
                blockbreakcode.add( new VarInsnNode(Opcodes.ALOAD, 3 ) );
                blockbreakcode.add( new FieldInsnNode( Opcodes.GETFIELD, "com/eloraam/redpower/core/WorldCoord", "y", "I" ) );
                blockbreakcode.add( new VarInsnNode(Opcodes.ALOAD, 3 ) );
                blockbreakcode.add( new FieldInsnNode( Opcodes.GETFIELD, "com/eloraam/redpower/core/WorldCoord", "z", "I" ) );
                blockbreakcode.add( new InsnNode(Opcodes.ICONST_0) );
                blockbreakcode.add( new InsnNode(Opcodes.ICONST_0) );
                blockbreakcode.add( new InsnNode(Opcodes.ICONST_1) );
                blockbreakcode.add (new LdcInsnNode("[Redpower2]" ));
                blockbreakcode.add( new InsnNode(Opcodes.ICONST_0) );
                blockbreakcode.add( new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "yc", "trySetBlockAndMetadata", "(IIIIIZLjava/lang/String;Z)Z" ) );
                LabelNode label1 = new LabelNode();
                blockbreakcode.add ( new JumpInsnNode(Opcodes.IFEQ, label1) );


                clearlabel(LabelMethode2, method);

                method.instructions.insert(LabelMethode1, blockbreakcode);
                method.instructions.insert(NavigationUtil.getNextLabel(LabelMethode2), label1);

                //method.instructions.insert(permLabel, new JumpInsnNode(Opcodes.GOTO , label2));
              /*  LabelNode label2 = new LabelNode();
                method.instructions.insert(label1, new JumpInsnNode(Opcodes.GOTO, label2));
                method.instructions.insert(NavigationUtil.getNextLabel(label1), label2);*/




                System.out.println("[PatchEngine - Redpower2] Sorry Griefers i got your ass now :) YOUR ASS IS MINE :o!!!!");

            }

        }
    }

    public static void clearlabel(LabelNode node, MethodNode methodNode){
        while (!(node.getNext() instanceof LabelNode))
        {
            methodNode.instructions.remove(node.getNext());
        }
    }

    private static Printer printer = new Textifier();
    private static TraceMethodVisitor mp = new TraceMethodVisitor(printer);


    public static void printinstruction(InsnList list){
        for (AbstractInsnNode node : list.toArray())
            Logger.getLogger("Minecraft").info(insnToString(node));

    }

    public static void printlabel(LabelNode node){
        AbstractInsnNode next = node.getNext();
            while ((next != null) && (!(next instanceof LabelNode)))
            {
                Logger.getLogger("Minecraft").info(insnToString(next));
                next = next.getNext();
            }
    }


    public static String insnToString(AbstractInsnNode insn){
        insn.accept(mp);
        StringWriter sw = new StringWriter();
        printer.print(new PrintWriter(sw));
        printer.getText().clear();
        return sw.toString();
    }
}
