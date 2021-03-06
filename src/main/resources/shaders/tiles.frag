#version 330 core
in vec2 texCoords;
out vec4 color;

uniform sampler2D boundTexture;

void main()
{
    color = texture(boundTexture, texCoords);
}
