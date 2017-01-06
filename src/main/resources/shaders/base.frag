#version 330 core
in vec4 vertexColor;
in vec2 vertexTexCoords;
out vec4 color;

uniform sampler2D boundTexture;

void main()
{
    color = vertexColor * texture(boundTexture, vertexTexCoords);
}
