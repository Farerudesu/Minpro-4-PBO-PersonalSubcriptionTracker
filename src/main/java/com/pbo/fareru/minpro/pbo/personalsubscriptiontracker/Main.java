package com.pbo.fareru.minpro.pbo.personalsubscriptiontracker;

import com.pbo.fareru.minpro.pbo.controller.LanggananController;
import com.pbo.fareru.minpro.pbo.view.LanggananView;

/**
 * 
 * @author Muhammad Fahriel (2509116050)
 */
public class Main {
    public static void main(String[] args) {
        try {
            LanggananView view = new LanggananView();
            LanggananController controller = new LanggananController(view);
            controller.jalankanAplikasi();
        } finally {
            try {
                com.mysql.cj.jdbc.AbandonedConnectionCleanupThread.checkedShutdown();
            } catch (Exception ignored) { }
        }
    }
}
