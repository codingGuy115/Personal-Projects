#type vertex
#version 330 core

layout (location=0) in vec2 aPos;
layout (location=1) in vec4 aColor;
layout (location=2) in vec2 aTexCoord;
layout (location=3) in float aTexIndex;

out vec4 fColor;
out vec2 texCoords;
out float texIndex;

uniform mat4 view;
uniform mat4 projection;

void main() {
    //gl_Position = vec4(aPos, 1.0f);
    gl_Position = projection * view * vec4(aPos, 0.0f, 1.0f);
    texCoords = aTexCoord;
    texIndex = aTexIndex;
    fColor = aColor;
}


#type fragment
#version 330 core

in vec4 fColor;
in vec2 texCoords;
in float texIndex;

out vec4 fragColor;

uniform sampler2D uTextures[5];
//*we want to store textures as array of sampler2Ds

void main() {
    int index = int(texIndex);

    if (texIndex == 0) {
        fragColor = fColor;
    } else {
        //fragColor = texture(texSampler0, texCoords); //this WAS single slot implementation.
        fragColor = texture(uTextures[index], texCoords);
    }
}