import mcpc.patchengine.api.IPatch;
import mcpc.patchengine.asm.util.ClassUtil;
import mcpc.patchengine.asm.util.MethodUtil;
import mcpc.patchengine.asm.util.NavigationUtil;
import mcpc.patchengine.common.Configuration;
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
public class ComputerCraftTurtleFix implements IPatch {

    private String Classname = "dan200.turtle.shared.TurtleTool";

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
            MethodNode methodNode = ClassUtil.getMethod(classNode, "dig", "(Ldan200/turtle/api/ITurtleAccess;I)Z");

            if (methodNode == null){
                Logger.getLogger("Minecraft").info("THOMAS YOUR A NOOB AGAIN STOP MAKING MISTAKES");
                return;
            }

            LabelNode node1 = NavigationUtil.getNextLabel(MethodUtil.getLabelWithInsn( methodNode , new MethodInsnNode(Opcodes.INVOKESPECIAL, "dan200/turtle/shared/TurtleTool", "getBlockDropped",  "(Lyc;III)Ljava/util/ArrayList;")));


            if (node1 == null) {
                Logger.getLogger("Minecraft").info("Hmm this no work u suc");
                return;
            }



            InsnList blockbreakcode = new InsnList();
            LabelNode label1 = new LabelNode();
            blockbreakcode.add( label1 );
            blockbreakcode.add( new VarInsnNode(Opcodes.ALOAD, 3 ) );

            blockbreakcode.add( new VarInsnNode(Opcodes.ILOAD, 5) );
            blockbreakcode.add( new VarInsnNode(Opcodes.ILOAD, 6) );
            blockbreakcode.add( new VarInsnNode(Opcodes.ILOAD, 7) );

            blockbreakcode.add( new InsnNode(Opcodes.ICONST_0) );
            blockbreakcode.add( new InsnNode(Opcodes.ICONST_0) );
            blockbreakcode.add( new InsnNode(Opcodes.ICONST_1) );
            blockbreakcode.add (new LdcInsnNode("[ComputerCraft]" ));
            blockbreakcode.add( new InsnNode(Opcodes.ICONST_0) );
            blockbreakcode.add( new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "yc", "trySetBlockAndMetadata", "(IIIIIZLjava/lang/String;Z)Z" ) );
            LabelNode label2 = new LabelNode();
            blockbreakcode.add ( new JumpInsnNode(Opcodes.IFEQ, label2) );

            methodNode.instructions.insertBefore(node1, blockbreakcode);

            InsnList finishcode = new InsnList();
            finishcode.add(label2);
            finishcode.add( new InsnNode(Opcodes.ICONST_0) );
            finishcode.add( new InsnNode(Opcodes.IRETURN));

            methodNode.instructions.insertBefore(MethodUtil.getLastLabel(methodNode), finishcode);

            LabelNode node2 = NavigationUtil.getPreviousLabel(label1);
            methodNode.instructions.remove(node2.getPrevious());
            methodNode.instructions.insertBefore(node2, new JumpInsnNode(Opcodes.IFEQ, label2));

           /* if (node2 == null)
                Logger.getLogger("WTF ITS NULL HOOOOOOOOOOOOOOOOOOOW");

            printlabel(label1);
            printlabel(label2);
            printlabel(NavigationUtil.getPreviousLabel(label1));

            printlabel(NavigationUtil.getPreviousLabel(NavigationUtil.getPreviousLabel(label1)));
            Logger.getLogger("Minecraft").info(insnToString(NavigationUtil.getPreviousLabel(label1).getPrevious()));
            //printlabel(node2);
           // printlabel(NavigationUtil.getPreviousLabel(node2));
            //Logger.getLogger("Minecraft").info(insnToString(node2.getPrevious()));


            try {
                Thread.sleep(8000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }*/

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
        Logger.getLogger("Minecraft").info("----------------------");
    }

    public static void clearlabel(LabelNode node, MethodNode method){
        AbstractInsnNode next = node.getNext();
        while ((next != null) && (!(next instanceof LabelNode)))
        {
            method.instructions.remove(next);
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
