/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openstreetmap.josm.plugins.tofix.util;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import org.openstreetmap.josm.Main;
import static org.openstreetmap.josm.actions.UploadAction.checkPreUploadConditions;
import org.openstreetmap.josm.actions.upload.UploadHook;
import org.openstreetmap.josm.data.APIDataSet;
import org.openstreetmap.josm.gui.io.UploadDialog;
import org.openstreetmap.josm.gui.io.UploadPrimitivesTask;
import org.openstreetmap.josm.gui.layer.OsmDataLayer;
import static org.openstreetmap.josm.gui.mappaint.mapcss.ExpressionFactory.Functions.tr;

/**
 *
 * @author ruben
 */
public class Upload {
    private Iterable<UploadHook> lateUploadHooks;

    public void uploadData(final OsmDataLayer layer, APIDataSet apiData) {
        if (apiData.isEmpty()) {
            JOptionPane.showMessageDialog(Main.parent, tr("No changes to upload."), tr("Warning"), JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        if (!checkPreUploadConditions(layer, apiData)) {
            return;
        }
        final UploadDialog dialog = UploadDialog.getUploadDialog();
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                dialog.setDefaultChangesetTags(layer.data.getChangeSetTags());
            }
        }
        );
        dialog.setUploadedPrimitives(apiData);
        dialog.setVisible(true);
        if (dialog.isCanceled()) {
            return;
        }
        dialog.rememberUserInput();
        for (UploadHook hook : lateUploadHooks) {
            if (!hook.checkUpload(apiData)) {
                return;
            }
        }
        Main.worker.execute(new UploadPrimitivesTask(UploadDialog.getUploadDialog().getUploadStrategySpecification(), layer, apiData, UploadDialog.getUploadDialog().getChangeset()));
    }

}
