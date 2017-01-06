#version 330 core

layout (location = 0) in vec2 position;
layout (location = 1) in vec4 color;
layout (location = 2) in vec2 texCoords;
out vec4 vertexColor;
out vec2 vertexTexCoords;


uniform mat4 modelView;
uniform mat4 projection;

void main()
{
    gl_Position = projection * modelView * vec4(position.x, position.y, 0.0f, 1.0f);
    vertexColor = color;
    vertexTexCoords = texCoords;
}
