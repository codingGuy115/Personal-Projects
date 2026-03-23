package derezengine.renderer;

import java.util.List;

public class Renderer {
    private List<Renderbatch> batches;


    public void addBatch(Renderbatch batch) { //TODO
        this.batches.add(batch);
    }

    public void render() {
        for (Renderbatch batch: batches) {
            batch.render();
        }
    }
}
