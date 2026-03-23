package derezengine.renderer;

import derezengine.*;
import org.joml.Vector2f;
import org.joml.Vector4f;
import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class Renderbatch { //REMEMBER: the idea is to group together all vertex data -> treat as ONE piece of geometry.
    //TODO: attributes & elements get defined here. also, gotta figure out z-index implementation. layers would be nice

    private SpriteRenderer[] spriteRenderers; //will hold spriteRenderers for the batch
    private float[] vertices; //array to hold all vertex data
    private int numSprites;
    private List<Texture> textures; //will hold textures associated with the batch. when sprites are added, this becomes relevant.
    private int[] samplers = { 0, 1, 2, 3, 4 }; //the slots essentially

    //now for some necessary info
    private final int POS_SIZE = 2;
    private final int COLOR_SIZE = 4;
    private final int TEXCOORD_SIZE = 2;
    private final int TEXID_SIZE = 1;

    private final int VERTEX_SIZE = 9;
    private final int VERTEX_SIZE_BYTES = VERTEX_SIZE * Float.BYTES;

    //offsets within ONE vertex
    private final int POS_OFFSET = 0;
    private final int COLOR_OFFSET = POS_SIZE * Float.BYTES;
    private final int TEXCOORD_OFFSET = (POS_SIZE + COLOR_SIZE) * Float.BYTES;
    private final int TEXID_OFFSET = (POS_SIZE + COLOR_SIZE + TEXCOORD_SIZE) * Float.BYTES;

    private int maxNumQuads; //max number of QUADS that can be stored.

    private Shader shader;

    //VAO stuff
    private int vaoID;
    private int vboID;
    private int eboID;

    public Renderbatch(int maxNumQuads) {
        this.shader = new Shader("assets/shaders/defaultShader.glsl");
        shader.compileAndLink();

        this.maxNumQuads = maxNumQuads; //remember this is max number of QUADS
        vertices = new float[maxNumQuads * 4 * VERTEX_SIZE]; //making room for the correct number of floats to accommodate 'maxBatchSize' number of quads
        spriteRenderers = new SpriteRenderer[maxNumQuads]; //now in a set size

        numSprites = 0;
        textures = new ArrayList<>();
    }

    public void init() { //MOVE VAO stuff here, because this is where we create space for our data & define the attributes. (we cannot go over in space)
        //bind vao & do other stuff
        vaoID = glGenVertexArrays();
        glBindVertexArray(vaoID);

        //TODO: NOTE: no need for a FloatBuffer in this stage, because we DON'T have any vertex data yet.
        vboID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBufferData(GL_ARRAY_BUFFER, vertices.length * Float.BYTES, GL_DYNAMIC_DRAW); //#NOTE: HERE'S VERTEX DATA BUFFER CREATION+INITIALIZATION. (in case you have business here.)

        // the indices only need to be generated ONCE. they are dependent on maxNumQuads
        eboID = glGenBuffers();
        int[] indices = generateIndices();

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboID);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices, GL_STATIC_DRAW);

        // Vertex Attribute stuff
        glVertexAttribPointer(0, POS_SIZE, GL_FLOAT, false, VERTEX_SIZE_BYTES, POS_OFFSET);
        glEnableVertexAttribArray(0);
        glVertexAttribPointer(1, COLOR_SIZE, GL_FLOAT, false, VERTEX_SIZE_BYTES, COLOR_OFFSET);
        glEnableVertexAttribArray(1);
        glVertexAttribPointer(2, TEXCOORD_SIZE, GL_FLOAT, false, VERTEX_SIZE_BYTES, TEXCOORD_OFFSET);
        glEnableVertexAttribArray(2);
        glVertexAttribPointer(3, TEXID_SIZE, GL_FLOAT, false, VERTEX_SIZE_BYTES, TEXID_OFFSET);
        glEnableVertexAttribArray(3);
    }

    public void render() { //rendering sequence for the batch. Remember: ONE DRAW CALL
        //should put things that we did in lvlEditor update() in here

        //use shader
        shader.use();
        //bind textures and upload samplers
        for (int i=0; i < textures.size(); i++) {
            glActiveTexture(GL_TEXTURE0 + 1 + i);
            textures.get(i).bind();
        }
        //(remember some funny stuff happens under the hood to convert from int to sampler2D)
        shader.uploadIntArray("uTextures", samplers);
        shader.uploadMat4f("view", SceneManager.get().currentScene.getCamera().getViewMatrix());
        shader.uploadMat4f("projection", SceneManager.get().currentScene.getCamera().getProjectionMatrix());

        //uploading the Data.
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBufferSubData(GL_ARRAY_BUFFER, 0, vertices);

        glBindVertexArray(vaoID);
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);
        // *SINGLE draw call*
        glDrawElements(GL_TRIANGLES, this.spriteRenderers.length * 6, GL_UNSIGNED_INT, 0);
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glBindVertexArray(0);

        for (int i=0; i < textures.size(); i++) {
            textures.get(i).unbind();
        }

        shader.detach();

    }

    public void addSpriteRenderer(SpriteRenderer spriteRenderer) {
        int index = this.numSprites;
        spriteRenderers[index] = spriteRenderer; //ensures we put it in the next empty spot in the array (at the end)
        this.numSprites++;
        //check if spriteRenderer's texture is already in list
        if ( !textures.contains(spriteRenderer.getSprite().getTexture())) {
            textures.add(spriteRenderer.getSprite().getTexture());
        }

        loadVertexData(index);
    }

    public int[] generateIndices() { //this should return an array with correct ordering for vertices to be drawn in accordance to maxNumQuads.
        int[] elements = new int[maxNumQuads * 6]; //create int array of correct size
        //PATTERN: 0, 1, 2, 2, 3, 0,
        //         4, 5, 6, 6, 7, 4, ...
        for (int quadIndex = 0; quadIndex < maxNumQuads; quadIndex++) {
            int startLoc = quadIndex * 6; //ensures we skip to correct spot in array.
            int startVal = quadIndex * 4;

            elements[startLoc] = startVal;
            elements[startLoc+1] = startVal + 1;
            elements[startLoc+2] = startVal + 2;
            elements[startLoc+3] = startVal + 2;
            elements[startLoc+4] = startVal + 3;
            elements[startLoc+5] = startVal;
        }

        return elements;
    }

    public void loadVertexData(int index) {
        //TODO: this method will load vertex data for ONE QUAD
        //This is where a bunch of heavy lifting happens, so then we can just add and remove sprites at a high level without needing to worry about the raw data
        SpriteRenderer spriteRenderer = this.spriteRenderers[index];
        int startLoc = index * VERTEX_SIZE * 4; //starting location in 'vertices'

        //get DATA (pieces of relevant info) for this specific spriteRenderer
        Transform transform = spriteRenderer.getTransform();
        Vector4f color = spriteRenderer.getColor();
        Vector2f[] texCoords = spriteRenderer.getSprite().getTexCoords();
        //(must find the texture of the sprite the vertex is part of)
        int texID = 0;

        if (spriteRenderer.getSprite().getTexture() == null) { //TODO: Does this check need to happen? if it gets thru the next loop unchanged?
            texID = 0; //should be 0, because then it would be a solid color
        }
        else {
            for (int i=0; i<textures.size(); i++) {
                if (spriteRenderer.getSprite().getTexture() == textures.get(i)) {
                    texID = i + 1; // (+1) bc we dont want it to be zero
                    break;
                }
            }
        }

        //generalized variable
        int amtToAdd = 0;

        //loop four times, once through each vertex
        for (int v=0; v<4; v++) {
            //TODO: within one vertex
            //loading position (using scale)
            switch (v) {
                case 0:
                {   vertices[startLoc + amtToAdd] = transform.getPosition().x;
                    vertices[startLoc+1 + amtToAdd] = transform.getPosition().y;
                    break; }
                case 1:
                {   vertices[startLoc + amtToAdd] = transform.getPosition().x + transform.getScale().x;
                    vertices[startLoc+1 + amtToAdd] = transform.getPosition().y;
                    break; }
                case 2:
                {   vertices[startLoc + amtToAdd] = transform.getPosition().x + transform.getScale().x;
                    vertices[startLoc+1 + amtToAdd] = transform.getPosition().y + transform.getScale().y;
                    break; }
                case 3:
                {   vertices[startLoc + amtToAdd] = transform.getPosition().x;
                    vertices[startLoc+1 + amtToAdd] = transform.getPosition().y + transform.getScale().y;
                    break; }
            }

            //loading color
            vertices[startLoc+2 + amtToAdd] = color.x;
            vertices[startLoc+3 + amtToAdd] = color.y;
            vertices[startLoc+4 + amtToAdd] = color.z;
            vertices[startLoc+5 + amtToAdd] = color.w;

            //loading texCoords
            Vector2f UV = texCoords[v]; //assigning 2 floats per vertex
            vertices[startLoc+6 + amtToAdd] = UV.x;
            vertices[startLoc+7 + amtToAdd] = UV.y;

            //loading textureID
            vertices[startLoc+8 + amtToAdd] = texID;


            amtToAdd += VERTEX_SIZE;
        }

        //testing
//        for (int i=0; i<vertices.length; i++) {
//            if (i%9 == 0) {
//                System.out.println();
//            }
//            System.out.print(vertices[i] + ",");
//        }

    }
}