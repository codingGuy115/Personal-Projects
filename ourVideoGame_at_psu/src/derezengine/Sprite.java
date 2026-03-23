package derezengine;

import derezengine.renderer.Texture;
import org.joml.Vector2f;

public class Sprite { //info for a sprite. Sprite is image, image needs tex coords.

    private Texture texture; //the texture the Sprite is associated with
    private Vector2f[] texCoords = {  //will be assigned a subset from Texture ^^
            new Vector2f(0, 0),
            new Vector2f(1, 0),
            new Vector2f(1, 1),
            new Vector2f(0, 1)
    };

    private float width;
    private float height;


    public Sprite() {

    }

    public Sprite(Texture texture) {
        this.texture = texture;
    }
//just in case we can supply texCoords as well
    public Sprite(Texture texture, Vector2f[] texCoords) {
        this.texture = texture;
        this.texCoords = texCoords;
    }

    public void setTexture(Texture texture) {
        this.texture = texture;
    }

    public Texture getTexture() {
        return this.texture;
    }

    public void setTexCoords(Vector2f[] texCoords) {
        this.texCoords = texCoords;
    }

    public Vector2f[] getTexCoords() {
        return this.texCoords;
    }

    public float getWidth() {
        return this.width;
    }

    public float getHeight() {
        return this.height;
    }

    public int getTexID() {
        return this.texture.getID();
    }

}
