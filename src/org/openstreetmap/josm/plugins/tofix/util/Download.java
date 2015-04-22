package org.openstreetmap.josm.plugins.tofix.util;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.openstreetmap.josm.Main;
import org.openstreetmap.josm.data.Bounds;
import org.openstreetmap.josm.data.osm.Node;
import org.openstreetmap.josm.plugins.tofix.TofixDialog;
import org.openstreetmap.josm.actions.downloadtasks.DownloadOsmTask;
import org.openstreetmap.josm.data.osm.Way;
import org.openstreetmap.josm.gui.progress.ProgressMonitor;
import org.openstreetmap.josm.plugins.tofix.bean.ItemUnconnectedBean;

/**
 *
 * @author ruben
 */
public class Download {

    private static Future<?> future;

    public static void Download(DownloadOsmTask task, Bounds bounds, final Long osm_obj_id) {
        ProgressMonitor monitor = null;
        final Future<?> future = task.download(true, bounds, monitor);
        Runnable runAfterTask = new Runnable() {

            @Override
            public void run() {
                try {
                    future.get(); // wait for the download task to complete                
                    //Node node = new Node(itemBean.getValue().getNode_id());
                   // Way way = new Way(itemBean.getValue().getWay_id());
                    
                    Node node = new Node(osm_obj_id);
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
}
