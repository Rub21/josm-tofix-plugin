/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openstreetmap.josm.plugins.tofix.util;

import java.util.Collection;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.openstreetmap.josm.Main;
import org.openstreetmap.josm.data.Bounds;
import org.openstreetmap.josm.data.osm.Node;
import org.openstreetmap.josm.data.osm.OsmPrimitive;
import org.openstreetmap.josm.plugins.tofix.TofixDialog;
import org.openstreetmap.josm.actions.downloadtasks.DownloadOsmTask;
import org.openstreetmap.josm.data.osm.Way;
import org.openstreetmap.josm.gui.progress.NullProgressMonitor;
import org.openstreetmap.josm.gui.progress.ProgressMonitor;
import org.openstreetmap.josm.plugins.tofix.bean.TaskBean;

/**
 *
 * @author ruben
 */
public class Download {

    private static Future<?> future;

    public static void Download(final DownloadOsmTask task, Bounds bounds, final TaskBean taskBean) {
        ProgressMonitor monitor = null;
        final Future<?> future = task.download(true, bounds, monitor);
        Runnable runAfterTask = new Runnable() {

            @Override
            public void run() {
                try {
                    future.get(); // wait for the download task to complete                
                    Node node = new Node(taskBean.getValue().getNode_id());
                    Way way = new Way(taskBean.getValue().getWay_id());
                    //Collection<OsmPrimitive> selection = task.getDownloadedData().allPrimitives();
                    Main.main.getCurrentDataSet().setSelected(node);

                } catch (InterruptedException ex) {
                    Logger.getLogger(TofixDialog.class.getName()).log(Level.SEVERE, null, ex);
                } catch (ExecutionException ex) {
                    Logger.getLogger(TofixDialog.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        };

        Main.worker.submit(runAfterTask);
    }

    public void selectobjects(TaskBean taskBean, DownloadOsmTask task) {

    }
}
