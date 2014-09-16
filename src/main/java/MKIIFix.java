import mcpc.patchengine.api.IPatch;
import mcpc.patchengine.asm.util.ClassUtil;
import mcpc.patchengine.asm.util.MethodUtil;
import mcpc.patchengine.common.Configuration;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

import java.util.logging.Logger;

/**
 * Created by thomas on 8/10/2014.
 */
public class MKIIFix implements IPatch {

    private String Classname = "immibis.tubestuff.ContainerAutoCraftingMk2";

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
            try {
                Class.forName("com.fantasycraft.fixes.LimitedSlot");

                MethodNode methodNode = ClassUtil.getMethod(classNode, "<init>", "(Lqx;Limmibis/tubestuff/TileAutoCraftingMk2;)V");

                LabelNode node = MethodUtil.getLabelWithInsn(methodNode, new MethodInsnNode(Opcodes.INVOKESPECIAL, "sr", "<init>", "(Lla;III)V"));

                AbstractInsnNode next1 = node.getNext();
                while (next1.getOpcode() != Opcodes.NEW){
                    next1 = next1.getNext();
                }

                methodNode.instructions.insert(next1, new TypeInsnNode(Opcodes.NEW, "com/fantasycraft/fixes/LimitedSlot" ));
                methodNode.instructions.remove(next1);



                AbstractInsnNode next2 = node.getNext();
                while (next2.getOpcode() != Opcodes.INVOKESPECIAL){
                    next2 = next2.getNext();
                }

                methodNode.instructions.insert(next2, new MethodInsnNode(Opcodes.INVOKESPECIAL, "com/fantasycraft/fixes/LimitedSlot", "<init>", "(Lla;III)V"));
                methodNode.instructions.remove(next2);

                System.out.println("[PatchEngine - TubeStuff] Dupe Free!!!!!!!!!");

            } catch (ClassNotFoundException e) {
                Logger.getLogger("Minecraft").info("Sorry We can not work without this!!!!!!!!!!!!");
                e.printStackTrace();
            }
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
