
package dk.cintix.horizon.core;

import dk.cintix.horizon.ui.HorizonFrame;
import dk.cintix.horizon.ui.states.WindowControls;

public class HorizonApp {
    static void main(String[] args) {
        HorizonFrame horizonFrame = new HorizonFrame(WindowControls.CLOSE_HIDE_MAXIMIZE);
    }
}
