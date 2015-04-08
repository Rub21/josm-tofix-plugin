package org.openstreetmap.josm.plugins.tofix;
import static org.openstreetmap.josm.tools.I18n.tr;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import javax.swing.JOptionPane;

import org.openstreetmap.josm.actions.JosmAction;
import org.openstreetmap.josm.tools.Shortcut;


@SuppressWarnings("serial")
public class TofixAcction extends JosmAction {

    public TofixAcction() {
        super(tr("To-Fix"),"icontofix",tr("Fix data"),
            Shortcut.registerShortcut("tool:to-fix",
                tr("Tool: {0}", tr("To-fix")),
                KeyEvent.VK_F, Shortcut.CTRL_SHIFT),
                true);
    }

    @Override
    public void actionPerformed(ActionEvent arg0)  {
        
        JOptionPane.showConfirmDialog(null, "holas");
    }
}
