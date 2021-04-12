
package org.strauteka.jbin.draw3d;

import com.sun.j3d.utils.behaviors.mouse.MouseRotate;
import com.sun.j3d.utils.behaviors.mouse.MouseTranslate;
import com.sun.j3d.utils.behaviors.mouse.MouseWheelZoom;

import com.sun.j3d.utils.universe.SimpleUniverse;

import org.strauteka.jbin.core.AbstractBin;
import org.strauteka.jbin.core.Bin;
import org.strauteka.jbin.core.Cargo;
import org.strauteka.jbin.core.Dimension;
import org.strauteka.jbin.core.Space;

import java.awt.BorderLayout;
import java.awt.GraphicsConfiguration;
import java.awt.Color;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.media.j3d.Appearance;
import javax.media.j3d.Background;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Canvas3D;
import javax.media.j3d.ColoringAttributes;
import javax.media.j3d.GeometryArray;
import javax.media.j3d.LineStripArray;
import javax.media.j3d.Material;
import javax.media.j3d.PointArray;
import javax.media.j3d.PointAttributes;
import javax.media.j3d.PolygonAttributes;
import javax.media.j3d.QuadArray;
import javax.media.j3d.RenderingAttributes;
import javax.media.j3d.Shape3D;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.media.j3d.TransparencyAttributes;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Point3f;
import javax.vecmath.Vector3d;
import javax.vecmath.Vector3f;

public class Draw3d<T extends Space> extends JPanel {
    private final Map<Dimension, Color3f> colors;
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    /**
     * @param args the command line arguments
     */
    /**
     * Fill your 3D world with content
     */

    public Draw3d(List<AbstractBin<?>> containers, javax.vecmath.Point3f myPointer, boolean skipEmptySpace,
            boolean skipBin, Map<Dimension, Color> colors) {

        this.colors = Optional.ofNullable(colors).orElseGet(() -> new HashMap<Dimension, Color>()).entrySet().stream()
                .collect(Collectors.toMap(e -> e.getKey(), e -> new Color3f(e.getValue().getRed() / 255f,
                        e.getValue().getGreen() / 255f, e.getValue().getBlue() / 255f)));

        Point3f startPointer = new Point3f(myPointer.x, myPointer.y, myPointer.z);

        setLayout(new BorderLayout());
        GraphicsConfiguration gc = SimpleUniverse.getPreferredConfiguration();
        Canvas3D canvas3D = new Canvas3D(gc);// See the added gc? this is a preferred config
        add("Center", canvas3D);

        BranchGroup scene = new BranchGroup();

        Background background = new Background(new Color3f(0.3f, 0.3f, 0.3f));
        BoundingSphere sphere = new BoundingSphere(new Point3d(0, 0, 0), 100000);
        background.setApplicationBounds(sphere);
        scene.addChild(background);
        //
        Transform3D rotation = new Transform3D();
        Transform3D temp = new Transform3D();
        Point3d center = new Point3d();
        Point3d eyePos = new Point3d(center);
        eyePos.z += 2.5;
        eyePos.x = 1.0;
        eyePos.y = 2.0;
        Vector3d up = new Vector3d();
        up.y = 1;

        Transform3D viewTrans = new Transform3D();
        viewTrans.setIdentity();
        viewTrans.lookAt(eyePos, center, up);

        // rotation.rotX(Math.PI/8);
        // temp.rotY(Math.PI/4);
        rotation.mul(temp);
        rotation.mul(viewTrans);
        ////
        TransformGroup objRotate = new TransformGroup(rotation);
        objRotate.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        objRotate.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);

        for (AbstractBin<?> container : containers) {
            drawContent(objRotate, container, startPointer, skipEmptySpace, skipBin); // getShape
            startPointer.x = (startPointer.x + translate(container.w()) + 0.2f);
        }

        // objRotate.addChild(draw3DLine());
        // mouserotate
        MouseRotate mouseRotate = new MouseRotate();
        mouseRotate.setSchedulingBounds(new BoundingSphere());
        mouseRotate.setTransformGroup(objRotate);

        scene.addChild(mouseRotate);

        MouseTranslate translate = new MouseTranslate();
        translate.setTransformGroup(objRotate);
        translate.setSchedulingBounds(new BoundingSphere());
        scene.addChild(translate);

        MouseWheelZoom mouseZoom = new MouseWheelZoom();// zoom MouseBehavior.INVERT_INPUT
        mouseZoom.setSchedulingBounds(new BoundingSphere());
        mouseZoom.setTransformGroup(objRotate);
        scene.addChild(objRotate);
        scene.addChild(mouseZoom);
        scene.compile();

        // SimpleUniverse is a Convenience Utility class
        SimpleUniverse simpleU = new SimpleUniverse(canvas3D);

        // This moves the ViewPlatform back a bit so the
        // objects in the scene can be viewed.
        simpleU.getViewingPlatform().setNominalViewingTransform();

        simpleU.addBranchGraph(scene);
    }

    private void drawBox(TransformGroup objRotate, Cargo<? extends Dimension> content, Point3f posPointer,
            boolean skipEmptySpace, boolean skipBin) {
        if (content.cargo() instanceof AbstractBin<?>) {
            drawContent(objRotate, ((AbstractBin<?>) content.cargo()).binRotate(content.rotation()), posPointer,
                    skipEmptySpace, skipBin);
        } else {
            final Dimension size = content.cargo().rotate(content.rotation());
            final Dimension stack = content.stack();
            Random rnd = new Random();
            colors.merge(content.cargo(), new Color3f(rnd.nextFloat(), rnd.nextFloat(), rnd.nextFloat()),
                    (o1, o2) -> o1);
            IntStream.rangeClosed(1, stack.l()).forEach(
                    l -> IntStream.rangeClosed(1, stack.h()).forEach(h -> IntStream.rangeClosed(1, stack.w()).forEach(
                            w -> createBox(objRotate, posPointer, colors.get(content.cargo()), l, h, w, size))));
        }
    }

    private void createBox(TransformGroup objRotate, Point3f startPointer, Color3f col, int l, int h, int w,
            Dimension size) {
        final Point3f posPointer = new Point3f(startPointer.x + translate(size.w() * (w - 1)),
                startPointer.y + translate(size.h() * (h - 1)), startPointer.z + translate(size.l() * (l - 1)));
        objRotate.addChild(draw3DRectangleLine(posPointer, size, new Color3f(1.0f, 1.0f, 1.0f)));
        objRotate.addChild(draw3DRectangle(posPointer, size, col, false));
    }

    private void drawContent(TransformGroup objRotate, AbstractBin<?> bin, Point3f startPointer, boolean skipEmptySpace,
            boolean skipBin) {
        if (!skipBin) {
            objRotate.addChild(draw3DRectangleLine(startPointer, bin, new Color3f(0.0f, 0.0f, 0.0f)));
        }
        for (Cargo<? extends Dimension> cargo : bin.cargo()) {
            final Point3f cargoPosPoint = new Point3f(startPointer.x + translate(cargo.w_()),
                    startPointer.y + translate(cargo.h_()), startPointer.z + translate(cargo.l_()));
            drawBox(objRotate, cargo, cargoPosPoint, skipEmptySpace, skipBin);
        }
        if (!skipEmptySpace) {
            for (Space space : bin.emptySpace()) {
                final Point3f spacePosPoint = new Point3f(startPointer.x + translate(space.w_()),
                        startPointer.y + translate(space.h_()), startPointer.z + translate(space.l_()));
                Random rnd = new Random();
                Color3f col = new Color3f(rnd.nextFloat(), rnd.nextFloat(), rnd.nextFloat());
                objRotate.addChild(draw3DRectangleLine(spacePosPoint, space, col));
                objRotate.addChild(draw3DPoint(spacePosPoint, col));
            }
        }
    }

    public Shape3D draw3DRectangleLine(Point3f position, Dimension size, Color3f color3f) {
        Point3f a = new Point3f(position.x, position.y, position.z + translate(size.l())); // ..
        Point3f b = new Point3f(position.x + translate(size.w()), position.y, position.z + translate(size.l())); // ..
        Point3f c = new Point3f(position.x, position.y + translate(size.h()), position.z + translate(size.l())); // ..
        Point3f d = new Point3f(position.x + translate(size.w()), position.y + translate(size.h()),
                position.z + translate(size.l())); // ..
        Point3f e = new Point3f(position.x, position.y, position.z); // ..
        Point3f f = new Point3f(position.x + translate(size.w()), position.y, position.z); // ..
        Point3f g = new Point3f(position.x, position.y + translate(size.h()), position.z); // ..
        Point3f h = new Point3f(position.x + translate(size.w()), position.y + translate(size.h()), position.z); // ..

        Point3f[] pts = new Point3f[20];
        int[] stripVertexCounts = { 5, 5, 5, 5 };

        pts[0] = a;
        pts[1] = b;
        pts[2] = d;
        pts[3] = c;
        pts[4] = a;

        pts[5] = e;
        pts[6] = f;
        pts[7] = h;
        pts[8] = g;
        pts[9] = e;

        pts[10] = a;
        pts[11] = e;
        pts[12] = g;
        pts[13] = c;
        pts[14] = a;

        pts[15] = b;
        pts[16] = f;
        pts[17] = h;
        pts[18] = d;
        pts[19] = b;

        Color3f[] colors = { color3f, color3f, color3f, color3f, color3f,

                color3f, color3f, color3f, color3f, color3f,

                color3f, color3f, color3f, color3f, color3f,

                color3f, color3f, color3f, color3f, color3f, };

        Shape3D shape = new Shape3D();
        LineStripArray geometry = new LineStripArray(pts.length, GeometryArray.COORDINATES | GeometryArray.COLOR_3,
                stripVertexCounts);
        geometry.setCoordinates(0, pts);
        geometry.setColors(0, colors);
        shape.setGeometry(geometry);

        // Shape3D shape = new Shape3D(geometry);
        // Because we're about to spin this triangle, be sure to draw
        // backfaces. If we don't, the back side of the triangle is invisible.
        Appearance ap = new Appearance();
        PolygonAttributes pa = new PolygonAttributes();
        pa.setCullFace(PolygonAttributes.POLYGON_LINE);
        ap.setPolygonAttributes(pa);
        shape.setAppearance(ap);

        return shape;

    }

    public Shape3D draw3DPoint(Point3f position, Color3f color3f) {
        position.x += 0.01f;
        position.y += 0.02f;
        position.z += 0.02f;
        Point3f[] plaPts = new Point3f[1];
        plaPts[0] = position;
        PointArray pla = new PointArray(1, GeometryArray.COORDINATES);
        pla.setCoordinates(0, plaPts);

        PointAttributes a_point_just_bigger = new PointAttributes();
        a_point_just_bigger.setPointSize(10.0f);// 10 pixel-wide point
        a_point_just_bigger.setPointAntialiasingEnable(true);// now points are sphere-like(not a cube)

        Appearance app = new Appearance();
        ColoringAttributes ca = new ColoringAttributes(color3f, ColoringAttributes.SHADE_FLAT);
        app.setColoringAttributes(ca);

        app.setPointAttributes(a_point_just_bigger);
        // and here! sets the point-attributes so it is easily seen.
        Shape3D shape = new Shape3D(pla, app);
        shape.setBoundsAutoCompute(true);
        return shape;

    }

    public Shape3D draw3DRectangle(Point3f position, Dimension size, Color3f color3f, boolean isTransparency) {
        // BranchGroup lineGroup = new BranchGroup();
        // box dimensions
        // ---------------------------------------------------------------------------
        // ---G-___________-H-------+Y-|----------------------------------------------
        // ----/|---------/|-----------| /+Z------------------|-Height;---/Length;----
        // -C-/-|________/D|-----/-X---|/----------+X---------|----------/------------
        // --|-E|_______|__|-F--_______o______________>-------|---------/-------------
        // --| /--------|-/-----------/|----------------------------------------------
        // A-|/_________|/-B---------/-|--/-Y------------------Width------------------
        // ---------------------/-Z-/-------------------------__________--------------
        // mini, reduces box size a little to appear box line
        final float mini = 0.0005f;
        Appearance app = new Appearance();
        Point3f a = new Point3f(position.x + mini, position.y + mini, position.z + translate(size.l()) - mini); // ..
        Point3f b = new Point3f(position.x + translate(size.w()) - mini, position.y + mini,
                position.z + translate(size.l()) - mini); // ..
        Point3f c = new Point3f(position.x + mini, position.y + translate(size.h()) - mini,
                position.z + translate(size.l()) - mini); // ..
        Point3f d = new Point3f(position.x + translate(size.w()) - mini, position.y + translate(size.h()) - mini,
                position.z + translate(size.l()) - mini); // ..
        Point3f e = new Point3f(position.x + mini, position.y + mini, position.z); // ..
        Point3f f = new Point3f(position.x + translate(size.w()) - mini, position.y + mini, position.z); // ..
        Point3f g = new Point3f(position.x + mini, position.y + translate(size.h()) - mini, position.z); // ..
        Point3f h = new Point3f(position.x + translate(size.w()) - mini, position.y + translate(size.h()) - mini,
                position.z); // ..

        QuadArray lsa = new QuadArray(48, QuadArray.COORDINATES | QuadArray.NORMALS);
        Vector3f[] normals = new Vector3f[24];
        for (int i = 0; i < 24; i++)
            normals[i] = new Vector3f();
        Point3f[] pts = new Point3f[24];
        for (int i = 0; i < 24; i++)
            pts[i] = new Point3f();
        // Color3f [] clrs=new Color3f[24];
        // for(int i=0;i<24;i++)clrs[i]=new Color3f(0.5f,0.5f,0.5f);
        // cube=6 quads
        // first quad
        pts[0] = a;
        pts[1] = b;
        pts[2] = d;
        pts[3] = c;

        pts[4] = e;
        pts[5] = f;
        pts[6] = h;
        pts[7] = g;

        pts[8] = a;
        pts[9] = e;
        pts[10] = g;
        pts[11] = c;

        pts[12] = b;
        pts[13] = f;
        pts[14] = h;
        pts[15] = d;

        pts[16] = d;
        pts[17] = h;
        pts[18] = g;
        pts[19] = c;

        pts[20] = a;
        pts[21] = b;
        pts[22] = f;
        pts[23] = e;

        normals[0].x = -1;
        normals[1].x = -1;
        normals[2].x = -1;
        normals[3].x = -1;
        normals[4].x = 1;
        normals[5].x = 1;
        normals[6].x = 1;
        normals[7].x = 1;
        normals[8].z = -1;
        normals[9].z = -1;
        normals[10].z = -1;
        normals[11].z = -1;
        normals[12].z = 1;
        normals[13].z = 1;
        normals[14].z = 1;
        normals[15].z = 1;
        normals[16].y = -1;
        normals[17].y = -1;
        normals[18].y = -1;
        normals[19].y = -1;
        normals[20].y = 1;
        normals[21].y = 1;
        normals[22].y = 1;
        normals[23].y = 1;

        lsa.setNormals(0, normals);
        lsa.setCoordinates(0, pts);
        Shape3D shape = new Shape3D();
        PolygonAttributes pa = new PolygonAttributes();
        pa.setPolygonMode(PolygonAttributes.POLYGON_FILL);
        pa.setCullFace(PolygonAttributes.CULL_NONE);
        Material mat = new Material();
        mat.setEmissiveColor(color3f);
        mat.setAmbientColor(new Color3f(0.1f, 0.1f, 0.1f));
        mat.setDiffuseColor(new Color3f(0.2f, 0.3f, 0.4f));
        mat.setSpecularColor(new Color3f(0.6f, 0.3f, 0.2f));
        mat.setLightingEnable(true);
        RenderingAttributes ra = new RenderingAttributes();
        ra.setIgnoreVertexColors(true);

        ColoringAttributes ca = new ColoringAttributes();
        ca.setShadeModel(ColoringAttributes.SHADE_GOURAUD);
        ca.setColor(new Color3f(0.2f, 0.5f, 0.9f));
        app.setColoringAttributes(ca);
        app.setRenderingAttributes(ra);
        app.setMaterial(mat);
        app.setPolygonAttributes(pa);

        if (isTransparency) {
            TransparencyAttributes t_attr = new TransparencyAttributes(TransparencyAttributes.BLENDED, 0.8f,
                    TransparencyAttributes.BLEND_SRC_ALPHA, TransparencyAttributes.BLEND_ONE);
            app.setTransparencyAttributes(t_attr);
        }
        shape.setGeometry(lsa);
        shape.setAppearance(app);
        return shape;

    }

    public Shape3D draw3DLine() {

        Point3f x = new Point3f(1.0f, 0.0f, 0.0f); // ..
        Point3f y = new Point3f(0.0f, 1.0f, 0.0f); // ..
        Point3f z = new Point3f(0.0f, 0.0f, 1.0f); // ..
        Point3f zero = new Point3f(0.0f, 0.0f, 0.0f); // ..

        Point3f[] pts = new Point3f[6];
        int[] stripVertexCounts = { 2, 2, 2 };

        pts[0] = zero;
        pts[1] = x;
        pts[2] = zero;
        pts[3] = y;
        pts[4] = zero;
        pts[5] = z;

        Color3f[] colors = { // x,y,z
                new Color3f(1.0f, 0.0f, 0.0f), new Color3f(1.0f, 0.0f, 0.0f), new Color3f(0.0f, 1.0f, 0.0f),
                new Color3f(0.0f, 1.0f, 0.0f), new Color3f(0.0f, 0.0f, 1.0f), new Color3f(0.0f, 0.0f, 1.0f) };

        Shape3D shape = new Shape3D();
        LineStripArray geometry = new LineStripArray(pts.length, GeometryArray.COORDINATES | GeometryArray.COLOR_3,
                stripVertexCounts);
        geometry.setCoordinates(0, pts);
        geometry.setColors(0, colors);
        shape.setGeometry(geometry);

        // Because we're about to spin this triangle, be sure to draw
        // backfaces. If we don't, the back side of the triangle is invisible.
        Appearance ap = new Appearance();
        PolygonAttributes pa = new PolygonAttributes();
        pa.setCullFace(PolygonAttributes.CULL_NONE);
        ap.setPolygonAttributes(pa);
        shape.setAppearance(ap);

        return shape;
    }

    public static float translate(int value) {
        return (float) value / 1000;
    }

    public static void draw(Bin... b) {
        draw(false, false, 1200, 800, null, b);
    }

    public static void draw(boolean skipEmptySpace, boolean skipBin, int frameW, int frameL,
            Map<Dimension, Color> colors, AbstractBin<?>... b) {
        List<AbstractBin<?>> con = Arrays.asList(b);
        javax.vecmath.Point3f startPointer3D = new Point3f(
                -(translate(con.get(0).w()) * con.size() + (con.size() - 1) * 0.1f) / 2, -translate(con.get(0).h()) / 2,
                -translate(con.get(0).l()) / 2);

        JFrame frame = new JFrame();
        frame.add(new JScrollPane(new Draw3d<Space>(con, startPointer3D, skipEmptySpace, skipBin, colors)));
        frame.setSize(frameW, frameL);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}