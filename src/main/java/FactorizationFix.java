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
 * Created by thomas on 8/9/2014.
 */
public class FactorizationFix implements IPatch {

    private String Classname = "factorization.common.TileEntityMixer";

    @Override
    public String[] getClassNames() {
        return new String[]{Classname};
    }

    @Override
    public void loadConfigurations(Configuration configuration) {

    }

    @Override
    public void transform(String s, ClassNode classNode) {
        if (s.equalsIgnoreCase(Classname)) {
            MethodNode methodNode = ClassUtil.getMethod(classNode, "doLogic", "()V");
            if (methodNode == null) {
                Logger.getLogger("Minecraft").info("Tom u are sutcha Noob");
                return;
            }

            LabelNode node = NavigationUtil.getNextLabel(MethodUtil.getLabelWithInsn(methodNode, new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "factorization/common/TileEntityMixer", "normalize",  "([Lur;)V")));
          /*  LabelNode l1 = new LabelNode();
            methodNode.instructions.insert(node, new JumpInsnNode(Opcodes.GOTO, l1 ));
            methodNode.instructions.insert(NavigationUtil.getNextLabel(node), l1);
            */
            clearlabel(node, methodNode);

            System.out.println("[PatchEngine - Factorization] We will not crash anymore i promise :)!");
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
