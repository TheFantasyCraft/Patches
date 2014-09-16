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
 * Created by thomas on 8/10/2014.
 */
public class AdvancedMachinesDupeFix implements IPatch {

    private String Classname = "ic2.advancedmachines.common.BlockAdvancedMachines";

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

            MethodNode methodNode = ClassUtil.getMethod(classNode, "a", "(Lyc;IIIII)V");

            if (methodNode == null)
            {
                Logger.getLogger("Minecraft").info("TOM UR SUTCHA NOOB");
                delay();
                return;
            }


            LabelNode node1 = NavigationUtil.getPreviousLabel(MethodUtil.getLastLabel(methodNode));

            LabelNode node2 = NavigationUtil.getNextLabel(MethodUtil.getFirstLabel(methodNode));

            LabelNode l1 = new LabelNode();

            AbstractInsnNode next = node2.getNext();
            Boolean changed = false;
            while (true) {
                if (next.getOpcode() == Opcodes.IFNONNULL) {
                    methodNode.instructions.insert(NavigationUtil.getPreviousLabel(node1), l1);
                    methodNode.instructions.remove(NavigationUtil.getPreviousLabel(l1).getPrevious());
                    methodNode.instructions.insert(next, new JumpInsnNode(Opcodes.IFNULL, l1 ) );
                    methodNode.instructions.remove(NavigationUtil.getNextLabel(next).getNext().getNext());
                    methodNode.instructions.remove(next);
                    break;
                }
                next = next.getNext();

            }

            InsnList deletetile = new InsnList();

            InsnList supermethode = new InsnList();
            supermethode.add( new VarInsnNode(Opcodes.ALOAD, 0));
            supermethode.add( new VarInsnNode(Opcodes.ALOAD, 1));
            supermethode.add( new VarInsnNode(Opcodes.ILOAD, 2));
            supermethode.add( new VarInsnNode(Opcodes.ILOAD, 3));
            supermethode.add( new VarInsnNode(Opcodes.ILOAD, 4));
            supermethode.add( new VarInsnNode(Opcodes.ILOAD, 5));
            supermethode.add( new VarInsnNode(Opcodes.ILOAD, 6));
            supermethode.add( new MethodInsnNode(Opcodes.INVOKESPECIAL, "akb", "a", "(Lyc;IIIII)V") );
            methodNode.instructions.insert(node1, supermethode);

            System.out.println("[PatchEngine - AdvMachines] Lets drop our items and cleanup correctly Ya :D!!!");
        }
    }

    public void delay(){
        try {
            Thread.sleep(8000);
        } catch (InterruptedException e) {
            e.printStackTrace();
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



    /*
            deletetile.add(new LabelNode());
            deletetile.add( new VarInsnNode(Opcodes.ALOAD, 1) );
            deletetile.add( new MethodInsnNode(Opcodes.INVOKESTATIC, "ic2/api/energy/EnergyNet", "getForWorld", "(Lyc;)Lic2/api/energy/EnergyNet;") );
            deletetile.add( new VarInsnNode(Opcodes.ALOAD, 1) );
            deletetile.add( new VarInsnNode(Opcodes.ILOAD, 2) );
            deletetile.add( new VarInsnNode(Opcodes.ILOAD, 3) );
            deletetile.add( new VarInsnNode(Opcodes.ILOAD, 4) );
            deletetile.add( new MethodInsnNode(Opcodes.INVOKEVIRTUAL, Constants.WorldClass, "q", "(III)Lany;"));
            deletetile.add( new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "ic2/api/energy/EnergyNet", "removeTileEntity", "(Lany;)V" ) );
            deletetile.add( new InsnNode(Opcodes.RETURN) );


            methodNode.instructions.insertBefore(node, deletetile);*/

    /*
            InsnList supermethode = new InsnList();
            supermethode.add( new VarInsnNode(Opcodes.ALOAD, 0));
            supermethode.add( new VarInsnNode(Opcodes.ALOAD, 1));
            supermethode.add( new VarInsnNode(Opcodes.ILOAD, 2));
            supermethode.add( new VarInsnNode(Opcodes.ILOAD, 3));
            supermethode.add( new VarInsnNode(Opcodes.ILOAD, 4));
            supermethode.add( new VarInsnNode(Opcodes.ILOAD, 5));
            supermethode.add( new VarInsnNode(Opcodes.ILOAD, 6));
            supermethode.add( new MethodInsnNode(Opcodes.INVOKESPECIAL, "akb", "a", "(Lyc;IIIII)V") );
            //supermethode.add( new InsnNode(Opcodes.RETURN) );
            */
}
