import derezengine.Window;

public class Main {
    public static void main(String[] args) {
        Window window = Window.get();
        window.run();
    }
}

//TODO: add all project files to git commit and upload to github
//TODO: implement dirty flag system in batch renderer
//TODO: ensure things still work when you have multiple batches. (make sure texture slots work)
//TODO: draw a flowchart diagram for the program flow.
//TODO: revise Renderer 'addBatch()' function to your liking. (control)
//TODO: review 'generateIndices()' method in renderBatch class. Think about what we need to do. We have a max batch size which is
// immediately associated with that batch. This means we can immediately generate the pattern knowing maxbatchsize (which is maxQuads)
// and the size of a vertex. What we must know is: whats the purpose of the method? where are the 'elements' array getting accessed, and how?


