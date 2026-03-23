package derezengine;

import org.joml.Vector2f;
import org.joml.Vector4f;

public class SpriteRenderer { // (need to extend from Component later) sort of like a 'wrapper' for a Sprite
    private Sprite sprite = new Sprite(); //stores the sprite associated with this SpriteRenderer
    private Vector4f color = new Vector4f(1.0f, 1.0f, 1.0f, 1.0f); //for the case in which we want solid color rather than texture
    private Transform transform;

    //TODO: it is quite possible that we may need to get rid of constructors for this and sprite class
//    public SpriteRenderer(Sprite sprite, Transform transform) {
//        this.sprite= sprite;
//        this.transform = transform;
//        this.color = new Vector4f(1.0f, 1.0f, 1.0f, 1.0f);
//    }
//
//    public SpriteRenderer(Sprite sprite) { //just incase we can only supply a sprite
//        this.sprite = sprite;
//        this.color = new Vector4f(1.0f, 1.0f, 1.0f, 1.0f);
//    }

    public Sprite getSprite() {
        return this.sprite;
    }

    public void setSprite(Sprite sprite) {
        this.sprite = sprite; //sprite can change. (think animation system)
    }

    public Transform getTransform() {
        return this.transform;
    }

    public void setTransform(Transform transform) {
        this.transform = transform;
    }

    public void setColor(Vector4f newColor) {
        this.color = newColor;
    }

    public Vector4f getColor() {
        return this.color;
    }

}
