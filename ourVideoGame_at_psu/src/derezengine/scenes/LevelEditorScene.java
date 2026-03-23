package derezengine.scenes;

import derezengine.*;
import derezengine.renderer.Renderbatch;
import derezengine.renderer.Shader;
import derezengine.renderer.Texture;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import static org.joml.Math.cos;
import static org.joml.Math.sin;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_DOWN;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;
import static org.lwjgl.opengl.GL45.glBindTextureUnit;

public class LevelEditorScene extends Scene {

    private Shader shader;

    //putting vertex data and such here for now (2 quads)
    private float[] vertexData = {
    //quad1://position              //color             //texCoord     //texIndex
            -0.75f, -1.0f,   0.0f, 0.0f, 1.0f, 1.0f,   0.0f, 0.0f,    3.0f,  //bottom left
            -0.25f, -1.0f,   0.0f, 0.0f, 1.0f, 1.0f,   1.0f, 0.0f,    3.0f,  //bottom right
            -0.25f,  1.0f,   0.0f, 0.0f, 1.0f, 1.0f,   1.0f, 1.0f,    3.0f,  //top right
            -0.75f,  1.0f,   0.0f, 0.0f, 1.0f, 1.0f,   0.0f, 1.0f,    3.0f,  //top left

    //quad2://position              //color             //texCoord     //texIndex
             0.25f, -1.0f,   0.0f, 1.0f, 0.0f, 1.0f,   0.0f, 0.0f,    3.0f,  //bottom left
             0.75f, -1.0f,   0.0f, 1.0f, 0.0f, 1.0f,   1.0f, 0.0f,    3.0f,  //bottom right
             0.75f,  1.0f,   0.0f, 1.0f, 0.0f, 1.0f,   1.0f, 1.0f,    3.0f,  //top right
             0.25f,  1.0f,   0.0f, 1.0f, 0.0f, 1.0f,   0.0f, 1.0f,    3.0f   //top left
    };

    private int[] elementArray = {
            0, 1, 2, 2, 3, 0,
            4, 5, 6, 6, 7, 4
    };

    private int vaoID, vboID, eboID;

    private FloatBuffer vertexBuffer;

    private String filepathA = "assets/testImages/testEmoji.png";
    private String filepathB = "assets/testImages/marioImageTest.png";
    private String sheetFilepath = "assets/spritesheets/obstacles_sheet.png";
    private String sheetFilepathB = "assets/spritesheets/characterImportSheetTest.png";
    private Texture textureA;
    private Texture textureB;
    //testing Spritesheet
    private Spritesheet spritesheetObstacles;
    private Texture sheetTexObstacles;
    private Spritesheet spritesheetB;
    private Texture sheetTexB;

    //may be batchRenderer future implementation
    private List<Texture> textures; //TODO: this should prob be abstracted.
    private int[] samplers = { 0, 1, 2, 3, 4 };

    Renderbatch batch;

    public LevelEditorScene() {
        System.out.println("Inside Level editor scene.");
    }

    @Override
    public void update(float dt) {
        //TODO:
        //batch.render();
        handleCameraInput(dt);

        //OLD IMPLEMENTATION
        shader.use();

        //TEXTURE loading

        for (int i=0; i < textures.size(); i++) {
            glActiveTexture(GL_TEXTURE0 + 1 + i);
            textures.get(i).bind();
        }
        //(remember some funny stuff happens under the hood to convert from int to sampler2D)
        shader.uploadIntArray("uTextures", samplers);

        //*trying to just put texCoords & id in correct spots
        int[] desiredSprites = {3, 0};
        setTexCoordsInVertexData(vboID, vertexData, desiredSprites, spritesheetObstacles); //PROOF OF CONCEPT


        handleCameraInput(dt);

        shader.uploadMat4f("projection", camera.getProjectionMatrix());
        shader.uploadMat4f("view", camera.getViewMatrix());

        glBindVertexArray(vaoID);
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);
        glEnableVertexAttribArray(2);
        glEnableVertexAttribArray(3);
        // *SINGLE draw call*
        glDrawElements(GL_TRIANGLES, elementArray.length, GL_UNSIGNED_INT, 0);
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glDisableVertexAttribArray(2);
        glDisableVertexAttribArray(3);
        glBindVertexArray(0);

        for (int i=0; i < textures.size(); i++) {
            textures.get(i).unbind();
        }

        shader.detach();

    }

    @Override
    public void init() {
        //define camera
        camera = new Camera(new Vector3f(0.0f, 0.0f, 20.0f));
        //define shader
        shader = new Shader("assets/shaders/defaultShader.glsl");
        shader.compileAndLink();

        //renderer = new Renderer();
        //renderer.start();

        //OLD IMPLEMENTATION
        //Texture setup
        this.textureA = new Texture(filepathB);
        this.textureB = new Texture(filepathA);
        this.sheetTexObstacles = new Texture(sheetFilepath);
        this.sheetTexB = new Texture(sheetFilepathB);
        textures = new ArrayList<>();
        textures.add(textureA);
        textures.add(textureB);
        textures.add(sheetTexObstacles);
        textures.add(sheetTexB);

        //spritesheet setup
        spritesheetObstacles = new Spritesheet(sheetTexObstacles, 160.0f, 48.0f, 16.0f, 16.0f, 10, 3, 0.0f);
        spritesheetB = new Spritesheet(sheetTexB, 52.0f, 100.0f, 26.0f, 50.0f, 2, 2, 0.0f);
        System.out.println("Sprites count: "+spritesheetObstacles.getNumSprites());
        for (int i=0; i<spritesheetObstacles.getNumSprites(); i++) {
            System.out.println("Sprite "+ i + " NDC texCoords: ");
            for (Vector2f coord : spritesheetObstacles.getSprite(i).getTexCoords()) {
                System.out.println(coord.x + ", " + coord.y);
            }
        }

//        //RenderBatch test
//        batch = new Renderbatch(1);
//        batch.init();
//
//        SpriteRenderer sprRenderer = new SpriteRenderer();
//        sprRenderer.setSprite(spritesheet.getSprite(0));
//        sprRenderer.setTransform(new Transform(new Vector2f(0, 0), new Vector2f(1, 1)));
//        batch.addSpriteRenderer(sprRenderer);


        //TODO: finish batch rendering abstraction then get rid of stuff here
        //bind vao & do other stuff
        vaoID = glGenVertexArrays();
        glBindVertexArray(vaoID);
        vertexBuffer = BufferUtils.createFloatBuffer(vertexData.length);
        vertexBuffer.put(vertexData).flip(); //putting the vertex data into the buffer format for GPU
        vboID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBufferData(GL_ARRAY_BUFFER, vertexBuffer, GL_DYNAMIC_DRAW); //#NOTE: HERE'S VERTEX DATA BUFFER CREATION+INITIALIZATION. (in case you have business here.)

        // Create and upload indices buffer
        IntBuffer elementBuffer = BufferUtils.createIntBuffer(elementArray.length);
        elementBuffer.put(elementArray).flip();

        eboID = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboID);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, elementBuffer, GL_STATIC_DRAW);

        // Vertex Attribute stuff
        int posSize = 2;
        int colorSize = 4;
        int texCoordSize = 2;
        int texIndexSize = 1;
        int vertexSizeBytes = (posSize + colorSize + texCoordSize + texIndexSize) * Float.BYTES;
        glVertexAttribPointer(0, posSize, GL_FLOAT, false, vertexSizeBytes, 0);
        glEnableVertexAttribArray(0);
        glVertexAttribPointer(1, colorSize, GL_FLOAT, false, vertexSizeBytes, posSize * Float.BYTES);
        glEnableVertexAttribArray(1);
        glVertexAttribPointer(2, texCoordSize, GL_FLOAT, false, vertexSizeBytes, (colorSize + posSize) * Float.BYTES);
        glEnableVertexAttribArray(2);
        glVertexAttribPointer(3, texIndexSize, GL_FLOAT, false, vertexSizeBytes, (colorSize + posSize + texCoordSize) * Float.BYTES);
        glEnableVertexAttribArray(3);


        shader.uploadMat4f("view", camera.getViewMatrix());
        shader.uploadMat4f("projection", camera.getProjectionMatrix());
    }

    //This method is just for convenience. It should prob be abstracted elsewhere in future
    public void handleCameraInput(float dt) {
        camera.setCameraSpeed(2.5f);
        float distToMove = camera.getCameraSpeed() * dt;

        //moving at constant speed d = s * t;
        if (KeyListener.isKeyPressed(GLFW_KEY_RIGHT)) {
            camera.position.x += distToMove;
            camera.cameraTarget.x += distToMove;
            //System.out.println("camera is moving");
        }
        if (KeyListener.isKeyPressed(GLFW_KEY_LEFT)) {
            camera.position.x -= distToMove;
            camera.cameraTarget.x -= distToMove;
        }
        if (KeyListener.isKeyPressed(GLFW_KEY_UP)) {
            camera.position.y += distToMove;
            camera.cameraTarget.y += distToMove;
        }
        if (KeyListener.isKeyPressed(GLFW_KEY_DOWN)) {
            camera.position.y -= distToMove;
            camera.cameraTarget.y -= distToMove;
        }
    }

    //PROOF OF CONCEPT ONLY, we're not actually using this in the implementation
    public void setTexCoordsInVertexData(int vboID, float[] vertexData, int[] desiredSprites, Spritesheet sprSheet) {
        //TODO: preserve all data (yes its bad practice), only change texCoords according to sprite.
        int numQuads = desiredSprites.length;
        int vertexSize = 9; //size of ONE vertex
        int texCoordsOffset = 6; //occurrence of texCoords in ONE vertex

        //What data are you putting in?? its the spritesheet.sprites.get().getTexCoords();

        int amtOffset = 0;
        for (int sprIndex : desiredSprites) { //quads do not have texture choice freedom yet
            Vector2f[] quadCoords = sprSheet.getSprite(sprIndex).getTexCoords(); //coords for whole quad
            for (int v=0; v<4; v++) {
                Vector2f tmp = quadCoords[v]; //assigning 2 floats per vertex
                vertexData[texCoordsOffset + amtOffset] = tmp.x;
                vertexData[texCoordsOffset + amtOffset + 1] = tmp.y;
                amtOffset += vertexSize;
            }
        }

        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBufferSubData(GL_ARRAY_BUFFER, 0, vertexData); //the whole array for now. (why is bufferSubData missing a "from -> to" design? why is it different from C version?)
    }

}
