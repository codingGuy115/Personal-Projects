package derezengine;

import derezengine.renderer.Texture;
import org.joml.Vector2f;

import java.util.ArrayList;
import java.util.List;

public class Spritesheet { //meant to serve as an easy way to get the sprite data AS SPRITE objects

    private Texture texture; //source image literally (the spritesheet)-> good because Texture class does dirty work of sending pixel data to GPU
    private List<Sprite> assocSprites; //will hold the sprites on this sheet
//public for testing purpose^
    public Spritesheet(Texture texture, float sheetWpxl, float sheetHpxl, float sprWpxl, float sprHpxl, int numSprHor, int numSprVert, float padding) {
        this.texture = texture;
        assocSprites = new ArrayList<>();

        //the 'setup' variables
        float sprWuv = sprWpxl / sheetWpxl;
        float sprHuv = sprHpxl / sheetHpxl;
        float paddingUV = padding / sheetWpxl;
        Vector2f pointer = new Vector2f(0, 1); //-> the pointer (starting spot is top left)

        //the calculations
        Vector2f[] tempCoords;

        for (int row = 0; row < numSprVert; row++) {
            for (int col = 0; col < numSprHor; col++) {

                tempCoords = new Vector2f[]{
                        new Vector2f(pointer.x, pointer.y-sprHuv),           //bl
                        new Vector2f(pointer.x+sprWuv, pointer.y-sprHuv), //br
                        new Vector2f(pointer.x+sprWuv, pointer.y),           //tr
                        new Vector2f(pointer.x, pointer.y)                      //tl
                };
                assocSprites.add(new Sprite(texture, tempCoords));
                pointer.x += (sprWuv + paddingUV);
            }
            pointer.x = 0;
            pointer.y -= (sprHuv + paddingUV);
        }
    }
    // most of work done here ^^ in constructor

    public Texture getSheetTexture() {
        return this.texture;
    }

    public Sprite getSprite(int index) {
        //will give the Sprite from sprites list at index
        return assocSprites.get(index);
    }

    public int getNumSprites() {
        return assocSprites.size();
    }


}