#version 330 core

layout (location = 0) in vec3 inPosition;
layout (location = 1) in vec2 inTexCoords;
out vec2 texCoords;


uniform mat4 modelView;
uniform mat4 projection;

void main()
{
    gl_Position = projection * modelView * vec4(inPosition.x, inPosition.y, inPosition.z, 1.0f);
    texCoords = inTexCoords;
}
