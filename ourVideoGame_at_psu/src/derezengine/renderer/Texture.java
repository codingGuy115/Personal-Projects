package derezengine.renderer;

import org.lwjgl.BufferUtils;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.stb.STBImage.*;

public class Texture { //NOT MY CODE. Texture loading seems simple enough, i just have to figure out how the C code translates to java.
    private String filepath;
    private int texID;
    private int width, height;

    public Texture(String filepath) {
        init(filepath);
    }

    public void init(String filepath) {
        this.filepath = filepath;

        //generate texture on GPU
        texID = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, texID);

        //set texture parameters
        //repeat image in both directions
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
        //when stretching the image, pixelate it
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        //when shrinking the image, pixelate it
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

        //next 3 lines are setting up places to store image data
        IntBuffer width = BufferUtils.createIntBuffer(1);
        IntBuffer height = BufferUtils.createIntBuffer(1);
        IntBuffer channels = BufferUtils.createIntBuffer(1);

        stbi_set_flip_vertically_on_load(true);
        ByteBuffer image = stbi_load(filepath, width, height, channels, 0);
        // ^^ this byteBuffer is the pixel data for the image, and puts the correct data into the PLACES we specified.

        if (image != null) {
            this.width = width.get(0);
            this.height = height.get(0);

            /**
             * All channels are are the image components. Ex: RGB is the red, green, blue components. A is opacity.
             */
            if (channels.get(0) == 3) {
                //this method UPLOADS the pixels to the GPU. ('image' is the data)
                glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, width.get(0), height.get(0),
                        0, GL_RGB, GL_UNSIGNED_BYTE, image);
            } else if (channels.get(0) == 4) {
                glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width.get(0), height.get(0),
                        0, GL_RGBA, GL_UNSIGNED_BYTE, image);
            }
        } else {
            assert false : "Error: (Texture) unknown number of channels '"+channels.get(0)+"'";
        }

        stbi_image_free(image); //Frees space, because we already sent data to GPU, so no longer need it.
    }

    public void bind() {
        glBindTexture(GL_TEXTURE_2D, texID);
    }

    public void unbind() {
        glBindTexture(GL_TEXTURE_2D, 0);
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public int getID() {
        return texID;
    }
}