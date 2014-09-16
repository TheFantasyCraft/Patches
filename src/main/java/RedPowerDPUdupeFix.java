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

/**
 * Created by thomas on 8/7/2014.
 */
public class RedPowerDPUdupeFix implements IPatch {

        private String classname = "com.eloraam.redpower.core.CoreLib";

        @Override
        public String[] getClassNames() {
            return new String[]{classname};
        }

        @Override
        public void loadConfigurations(Configuration configuration) {
        }

        @Override
        public void transform(String s, ClassNode classNode) {

            if (s.equalsIgnoreCase(classname)) {
                MethodNode method = ClassUtil.getMethod(classNode, "compareItemStack", String.format("(Lur;Lur;)I", new Object[] { Constants.ItemStackClass, Constants.ItemStackClass }));

            if (method == null) {
                System.out.println("[PatchEngine - Redpower2] A LibCore class Without this methode has no reason of exist. Crash Bitch!! ");
                return;
            }

            LabelNode label = NavigationUtil.getPreviousLabel(method.instructions, method.instructions.size() - 1);
            InsnList instructions = new InsnList();

            instructions.add( new VarInsnNode(Opcodes.ALOAD, 0 ) );
            instructions.add( new MethodInsnNode(Opcodes.INVOKEVIRTUAL, Constants.ItemStackClass, "o", "()Z") );
            LabelNode l3 = new LabelNode();
            instructions.add( new JumpInsnNode(Opcodes.IFEQ, l3) );
            instructions.add( new InsnNode(Opcodes.ICONST_M1) );
            instructions.add( new InsnNode(Opcodes.IRETURN));
            instructions.add( l3 );

            method.instructions.insert(MethodUtil.getFirstLabel(method), instructions);
            InsnList inList = method.instructions;

            System.out.println("[PatchEngine - Redpower2] Fixed ItemStack Compare methode");
        }
    }


    private static Printer printer = new Textifier();
    private static TraceMethodVisitor mp = new TraceMethodVisitor(printer);


    public static String insnToString(AbstractInsnNode insn){
        insn.accept(mp);
        StringWriter sw = new StringWriter();
        printer.print(new PrintWriter(sw));
        printer.getText().clear();
        return sw.toString();
    }
}
