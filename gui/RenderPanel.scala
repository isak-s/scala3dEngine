import java.awt.{BorderLayout, Color, Graphics, Graphics2D}
import javax.swing.{JFrame, JPanel, SwingUtilities}
import java.util.ArrayList
import java.awt.geom.Path2D
import java.awt.image.BufferedImage
import scala.collection.mutable.ArrayBuffer
import javax.swing.JComponent
import Matrix4.{viewMatrix, perspectiveProjection}

class RenderPanel() extends JPanel {
    given JPanel = this

    private val angles = new Angles
    given Angles = angles

    private val camera = new Camera(Vertex4(0,0,500,1))
    given Camera = camera

    private val globe = Globe(Vertex4(0,0,0,1)).scaled(5)

    private val bodies = ArrayBuffer(globe)

    private val lightSource = DirectionalLight(Vertex4(0,0,1, 0), 1)

    addMouseMotionListener(new ViewMouseMotionListener())

    override def paintComponent(g: Graphics): Unit = {
        super.paintComponent(g)
        val g2 = g.asInstanceOf[Graphics2D]

        val width = getWidth()
        val height = getHeight()

        // Black background
        val img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB)
        val gImg = img.createGraphics()
        gImg.setColor(Color.BLACK)
        gImg.fillRect(0, 0, width, height)

        val mv = perspectiveProjection * viewMatrix // * model//  * rotation

        // Move to middle of screen
        val toScreenCoords = Matrix4.translation(width / 2.0, height / 2.0, 0)

        // Z buffer
        val zBuffer: Array[Double] = Array.fill(height*width)(Double.MinValue)

        bodies.foreach { b => b.tris.foreach { t =>

            val mvp = mv * b.modelMatrix
            // Move to middle of screen
            val mvpt = toScreenCoords * mvp

            // Transform vertices
            val v1 = mvp.transform(t.v1)
            val v2 = mvp.transform(t.v2)
            val v3 = mvp.transform(t.v3)

            v1.x += width / 2.0
            v1.y += height / 2.0
            v2.x += width / 2.0
            v2.y += height / 2.0
            v3.x += width / 2.0
            v3.y += height / 2.0

            val minX = Math.max(0, Math.ceil(Math.min(v1.x, Math.min(v2.x, v3.x)))).toInt
            val maxX = Math.min(width - 1, Math.floor(Math.max(v1.x, Math.max(v2.x, v3.x)))).toInt
            val minY = Math.max(0, Math.ceil(Math.min(v1.y, Math.min(v2.y, v3.y)))).toInt
            val maxY = Math.min(height - 1, Math.floor(Math.max(v1.y, Math.max(v2.y, v3.y)))).toInt

            val imgWidth = img.getWidth()

            for (y <- minY to maxY) { // Until
                for (x <- minX to maxX) {
                    val p = Vertex4(x, y, 0, 0)
                    if (Triangle.pointInTriangle(v1, v2, v3, p)) {
                        val depth = (v1.z + v2.z + v3.z) / 3
                        val zIndex = y * imgWidth + x
                        if (zBuffer(zIndex) < depth) {

                            val norm = ((v2 - v1) cross (v3 - v1)).normalize // Normal vector of the face
                            // Ambient light
                            val costheta = Math.max(0.01, Math.abs(norm dot lightSource.direction)) // Both are already normalized
                            val c = SimpleShader.getShade(t.color, costheta)

                            img.setRGB(x, y, c.getRGB())
                            zBuffer.update(zIndex, depth)
                        }

                    }
                }
            }
        }

        g2.drawImage(img, 0, 0, null)
    }
    }

}