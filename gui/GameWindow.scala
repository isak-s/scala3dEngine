import java.awt.{BorderLayout, Color, Graphics, Graphics2D}
import javax.swing.{JFrame, JPanel, SwingUtilities}
import java.util.ArrayList
import java.awt.geom.Path2D
import java.awt.event.MouseMotionListener
import java.awt.event.MouseEvent
import java.awt.image.BufferedImage
class GameWindow {

    def launch(): Unit = {
        SwingUtilities.invokeLater(() => {
            val frame = new JFrame("Scala 3d engine")
            frame.setDefaultCloseOperation(3) //JFrame.EXIT_ON_CLOSE
            frame.setSize(600, 600)
            frame.add(new RenderPanel(), BorderLayout.CENTER)
            frame.setVisible(true)
        })
    }
}

case class Angles(var pitch: Double = 0.0, var heading: Double = 0.0)

class RenderPanel() extends JPanel {

    private val angles = new Angles

    private val tris = createTetrahedron()

    private val lightSource = LightSource(Vertex3(0, 0, Integer.MIN_VALUE), Vertex3(0, 0, 1))

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

        val dx = Math.toRadians(angles.heading)
        val dy = Math.toRadians(angles.pitch)
        val transform = Matrix3.heading(dx) * Matrix3.pitch(dy)

        // Z buffer
        val zBuffer: Array[Double] = Array.fill(height*width)(Double.MinValue)

        tris.forEach { t =>
            // Transform vertices
            val v1 = transform.transform(t.v1)
            val v2 = transform.transform(t.v2)
            val v3 = transform.transform(t.v3)

            // Move into screen space
            v1.x += width / 2.0; v1.y += height / 2.0
            v2.x += width / 2.0; v2.y += height / 2.0
            v3.x += width / 2.0; v3.y += height / 2.0

            val minX = Math.max(0, Math.ceil(Math.min(v1.x, Math.min(v2.x, v3.x)))).toInt
            val maxX = Math.min(width - 1, Math.floor(Math.max(v1.x, Math.max(v2.x, v3.x)))).toInt
            val minY = Math.max(0, Math.ceil(Math.min(v1.y, Math.min(v2.y, v3.y)))).toInt
            val maxY = Math.min(height - 1, Math.floor(Math.max(v1.y, Math.max(v2.y, v3.y)))).toInt

            val imgWidth = img.getWidth()

            for (y <- minY until maxY) {
                for (x <- minX until maxX) {
                    val p = new Vertex3(x, y, 0)
                    if (Triangle.pointInTriangle(v1, v2, v3, p)) {
                        val depth = (v1.z + v2.z + v3.z) / 3
                        val zIndex = y * imgWidth + x
                        if (zBuffer(zIndex) < depth) {

                            val norm = ((v2 - v1) cross (v3 - v1)).normalize // Normal vector of the face
                            val costheta = Math.abs(norm dot lightSource.direction) // Both are already normalized
                            val c = getShade(t.color, costheta)

                            img.setRGB(x, y, c.getRGB())
                            zBuffer.update(zIndex, depth)
                        }

                    }
                }
            }
        }

        g2.drawImage(img, 0, 0, null)
    }


    addMouseMotionListener(new MouseMotionListener {
        override def mouseDragged(e: MouseEvent): Unit = {
        val xi = 180.0 / (getWidth /100)
        val yi = 180.0 / (getHeight /100)
        angles.heading = e.getX * xi
        angles.pitch = -e.getY * yi
        repaint()

        }

        override def mouseMoved(e: MouseEvent): Unit = {}

        })

    def getShade(color: Color, shade: Double): Color = {

        // Some evil scaled format to linear and back bs
        val redLinear: Double = (Math.pow(color.getRed(), 2.2) * shade)
        val greenLinear: Double = (Math.pow(color.getGreen(), 2.2) * shade)
        val blueLinear: Double = (Math.pow(color.getBlue(), 2.2) * shade)

        val red = Math.pow(redLinear, 1 / 2.2).toInt
        val green = Math.pow(greenLinear, 1 / 2.2).toInt
        val blue = Math.pow(blueLinear, 1 / 2.2).toInt

        new Color(red, green, blue);
}

    private def createTetrahedron(): ArrayList[Triangle] = {
        val tris = new ArrayList[Triangle]()
        tris.add(new Triangle(new Vertex3(100, 100, 100),
                            new Vertex3(-100, -100, 100),
                            new Vertex3(-100, 100, -100),
                            Color.WHITE))
        tris.add(new Triangle(new Vertex3(100, 100, 100),
                            new Vertex3(-100, -100, 100),
                            new Vertex3(100, -100, -100),
                            Color.RED))
        tris.add(new Triangle(new Vertex3(-100, 100, -100),
                            new Vertex3(100, -100, -100),
                            new Vertex3(100, 100, 100),
                            Color.GREEN))
        tris.add(new Triangle(new Vertex3(-100, 100, -100),
                            new Vertex3(100, -100, -100),
                            new Vertex3(-100, -100, 100),
                            Color.BLUE))
        tris
    }
}