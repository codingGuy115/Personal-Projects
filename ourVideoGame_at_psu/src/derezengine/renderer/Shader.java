package derezengine.renderer;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.BufferUtils;

import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.lwjgl.opengl.GL20.*;

public class Shader { //we will make the shader abstraction here

    private int shaderProgramID;
    private boolean beingUsed = false;

    public String vertexSource;
    public String fragmentSource;
    public String filepath;

    public Shader(String filepath) // I still need to figure this method out. I know its reading a file and putting the appropriate stuff in vertexSource & fragmentSource, but why all the fancy syntax?
    {
        //we're pretty much just accessing the default.glsl shader file here
        this.filepath = filepath;
        try {
            String source = new String(Files.readAllBytes(Paths.get(filepath)));
            String[] splitString = source.split("(#type)( )+([a-zA-Z]+)"); //this is the regular expression (regex) stuff

            // find the first pattern after #type 'pattern'
            int index = source.indexOf("#type") + 6;
            int eol = source.indexOf("\r\n", index);
            String firstPattern = source.substring(index, eol).trim();

            // find the second pattern after #type 'pattern'
            index = source.indexOf("#type", eol) + 6;
            eol = source.indexOf("\r\n", index);
            String secondPattern = source.substring(index, eol).trim();

            if (firstPattern.equals("vertex")) {
                vertexSource = splitString[1];
            } else if (firstPattern.equals("fragment")) {
                fragmentSource = splitString[1];
            } else {
                throw new IOException("Unexpected token '" + firstPattern + "'");
            }

            if (secondPattern.equals("vertex")) {
                vertexSource = splitString[2];
            } else if (secondPattern.equals("fragment")) {
                fragmentSource = splitString[2];
            } else {
                throw new IOException("Unexpected token '" + secondPattern + "'");
            }

        } catch(IOException e) {
            e.printStackTrace();
            assert false : "Error: Could not open file for shader: " + filepath;
        }

        //----PRINTS OUT THE SHADER SOURCES----
//        System.out.println(vertexSource);
//        System.out.println(fragmentSource);
    }

    public void compileAndLink() { // *I know how to do this method*
        int vertexShaderID;
        int fragmentShaderID;

        //create OpenGL shader object (referenced by ID). Next, create shader with glCreateShader()
        vertexShaderID = glCreateShader(GL_VERTEX_SHADER);
        //attach source code to shader object and compile shader
        glShaderSource(vertexShaderID, vertexSource);
        glCompileShader(vertexShaderID);
        //check compilation success
        int success = glGetShaderi(vertexShaderID, GL_COMPILE_STATUS);
        if (success == GL_FALSE) {
            int len = glGetShaderi(vertexShaderID, GL_INFO_LOG_LENGTH);
            System.out.println("ERROR: Vertex shader compilation failed.");
            System.out.println(glGetShaderInfoLog(vertexShaderID, len));
            assert false : "";
        }

        //now onto setting up the fragment shader
        fragmentShaderID = glCreateShader(GL_FRAGMENT_SHADER);
        glShaderSource(fragmentShaderID, fragmentSource);
        glCompileShader(fragmentShaderID);
        success = glGetShaderi(fragmentShaderID, GL_COMPILE_STATUS);
        if (success == GL_FALSE) {
            int len = glGetShaderi(fragmentShaderID, GL_INFO_LOG_LENGTH);
            System.out.println("ERROR: Fragment shader compilation failed.");
            System.out.println(glGetShaderInfoLog(fragmentShaderID, len));
            assert false : "";
        }

        // - ok cool, both shaders are compiled, we must now LINK into a Shader Program
        shaderProgramID = glCreateProgram();
        glAttachShader(shaderProgramID, vertexShaderID);
        glAttachShader(shaderProgramID, fragmentShaderID);
        glLinkProgram(shaderProgramID);
        success = glGetProgrami(shaderProgramID, GL_LINK_STATUS);
        if (success == GL_FALSE) {
            int len = glGetProgrami(shaderProgramID, GL_INFO_LOG_LENGTH);
            System.out.println("ERROR: Linking of shaders failed.");
            System.out.println(glGetProgramInfoLog(shaderProgramID, len));
            assert false : "";
        }
    }

    public void use() {
        if (!beingUsed) {
            glUseProgram(shaderProgramID);
        }
        glUseProgram(shaderProgramID);
    }

    public void detach() {
        glUseProgram(0);
        beingUsed = false;
    }

    public int getShaderProgramID() {
        return shaderProgramID;
    }

    //-- the following 'upload' functions will query a uniform location and set its value
    public void uploadFloat(String varName, float val) {
        int varLocation = glGetUniformLocation(shaderProgramID, varName);
        use();
        glUniform1f(varLocation, val);
    }

    public void uploadVec4f(String varName, Vector4f val) {
        int varLocation = glGetUniformLocation(shaderProgramID, varName);
        use();
        glUniform4f(varLocation, val.x, val.y, val.z, val.w);
    }

    public void uploadVec3f(String varName, Vector3f val) {
        int varLocation = glGetUniformLocation(shaderProgramID, varName);
        use();
        glUniform3f(varLocation, val.x, val.y, val.z);
    }

    public void uploadMat4f(String varName, Matrix4f mat4) {
        int varLocation = glGetUniformLocation(shaderProgramID, varName);
        use();
        FloatBuffer matBuffer = BufferUtils.createFloatBuffer(16); //this will store the mat4
        mat4.get(matBuffer); //put the mat4 into the FloatBuffer
        glUniformMatrix4fv(varLocation, false, matBuffer);
    }

    public void uploadIntArray(String varName, int[] array) {
        int varLocation = glGetUniformLocation(shaderProgramID, varName);
        use();
        glUniform1iv(varLocation, array);
    }

    public void uploadTextureSlot(String varName, int slot) {
        int varLocation = glGetUniformLocation(shaderProgramID, varName);
        use();
        glUniform1i(varLocation, slot);
    }

}