import pt.ipb.esact.compgraf.engine.Skybox;
import pt.ipb.esact.compgraf.engine.obj.ObjLoader;
import pt.ipb.esact.compgraf.tools.Camera;
import pt.ipb.esact.compgraf.tools.Cameras;
import pt.ipb.esact.compgraf.tools.DefaultGLWindow;

import java.nio.FloatBuffer;

//ERROR: Exception in thread "main" java.lang.ClassNotFoundException

/**
 * Created by MateusBranco on 06/02/2017.
 */
public class Main extends DefaultGLWindow {

    // .obj loaders
    private ObjLoader moon;

    // skybox
    private Skybox skybox;


    public Main () {
        super ("Silent Hill 12", true );
        setMousePan(true);
        setMouseZoom(true);
    }

    public static void main(String[] args) {
        new Main();
    }

    @Override
    public void init() {

        /*  select clearing (background) color  */
        glClearColor(0.2f, 0.2f, 0.2f, 1.0f);
        //BLENDING 4 MIRRORS & TRANSPARENCY
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        glEnable(GL_DEPTH_TEST);
        glEnable(GL_MULTISAMPLE);
        glEnable(GL_CULL_FACE);
        glCullFace(GL_BACK);

        glShadeModel(GL_SMOOTH);


        //Activate material
        configureLighting();

        //Activate light
        configureMaterials();

        //Activate Fog
        //configureFog();

        skybox = new Skybox(this);
        skybox.load(
                "assets/skyboxes/landscape/px.png",
                "assets/skyboxes/landscape/py.png",
                "assets/skyboxes/landscape/pz.png",
                "assets/skyboxes/landscape/nx.png",
                "assets/skyboxes/landscape/ny.png",
                "assets/skyboxes/landscape/nz.png"
        );

        //moon = new ObjLoader(this);
        //moon.load("assets/models/moon/moon.obj", "assets/models/moon/moon.mtl");

    }

    private void configureMaterials() {
        //Color Tracking
        glEnable(GL_COLOR_MATERIAL);
        glColorMaterial(GL_FRONT,GL_AMBIENT_AND_DIFFUSE);
        glMateriali(GL_FRONT,GL_SHININESS, 15);

        //material Specularity
        glMaterialfv(GL_FRONT,GL_SPECULAR, newFloatBuffer(1.0f, 1.0f ,1.0f, 1.0f));
    }

    private void configureLighting() {
        //enable light
        glEnable(GL_LIGHTING);

        // Ambient Light model
        glLightModelfv(GL_LIGHT_MODEL_AMBIENT, newFloatBuffer(0.5f, 0.5f ,0.5f, 1.0f));

        // Config Light0
        glLightfv(GL_LIGHT0, GL_AMBIENT, newFloatBuffer(0.6f, 0.6f, 0.6f, 1.0f));		// Ambient
        glLightfv(GL_LIGHT0, GL_DIFFUSE, newFloatBuffer(0.5f, 0.5f, 0.5f, 1.0f));		// Diffuse
        glLightfv(GL_LIGHT0, GL_SPECULAR, newFloatBuffer(0.5f, 0.5f, 0.5f, 1.0f));		// Specular

        //activate light
        glEnable(GL_LIGHT0);

    }

    private void configureFog() {
        glEnable(GL_FOG);

        FloatBuffer FOG_color = newFloatBuffer (0.2f, 0.2f, 0.2f, 1.0f);
        glFogfv(GL_FOG_COLOR, FOG_color);
        glFogf(GL_FOG_START, 1.0f);
        glFogf(GL_FOG_END, 50.0f);
        glFogi(GL_FOG_MODE, GL_LINEAR);
    }

    @Override
    public void resize(int width, int height) {
        setProjectionPerspective(width, height, 100.0f, 0.001f, 150.0f);
        Cameras.setCurrent(new Camera(20, 20, 20));
        setupCamera();
    }

    @Override
    public void render(int width, int height) {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        
        //light Pos
        glLightfv(GL_LIGHT0, GL_POSITION, newFloatBuffer(20.0f, 20.0f, 0.0f, 1.0f));

        skybox.render();

        glBegin(GL_QUADS);
        {
            glColor4f(0.5f, 0.5f, 0.5f, 1f);
            glVertex3f(-20, 0, 20);
            glVertex3f(20, 0, 20);
            glVertex3f(20, 0, -20);
            glVertex3f(-20, 0, -20);
        }
        glEnd();

        glColor4f(0.0f, 0.0f, 0.0f, 1.0f);
        glPushMatrix();
        {
            glTranslatef(0,5.0f,0);
            //moon.render();
            glutSolidSphere(5f,64,32);
        }
        glPopMatrix();

        glFlush();
    }

    @Override
    public void release() {}
}