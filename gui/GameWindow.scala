import java.awt.{BorderLayout, Color, Graphics, Graphics2D}
import javax.swing.{JFrame, JPanel, SwingUtilities}
import java.util.ArrayList
import java.awt.geom.Path2D
import java.awt.event.MouseMotionListener
import java.awt.event.MouseEvent
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

    override def paintComponent(g: Graphics): Unit = {
        super.paintComponent(g)
        val g2 = g.asInstanceOf[Graphics2D]
        g2.setColor(Color.BLACK)
        g2.fillRect(0, 0, getWidth, getHeight)

        g2.translate(getWidth / 2, getHeight / 2)

        val dx = Math.toRadians(angles.heading)
        val dy = Math.toRadians(angles.pitch)
        val transform = Matrix3.heading(dx) * Matrix3.pitch(dy)

        tris.forEach { t =>
            // Rotate points
            val v1 = transform.transform(t.v1)
            val v2 = transform.transform(t.v2)
            val v3 = transform.transform(t.v3)

            val path = new Path2D.Double()
            path.moveTo(v1.x, v1.y)
            path.lineTo(v2.x, v2.y)
            path.lineTo(v3.x, v3.y)
            path.closePath()
            g2.setColor(t.color)
            g2.draw(path)
            }
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

    private def createTetrahedron(): ArrayList[Triangle] = {
        val tris = new ArrayList[Triangle]()
        tris.add(new Triangle(new Vertex(100, 100, 100),
                            new Vertex(-100, -100, 100),
                            new Vertex(-100, 100, -100),
                            Color.WHITE))
        tris.add(new Triangle(new Vertex(100, 100, 100),
                            new Vertex(-100, -100, 100),
                            new Vertex(100, -100, -100),
                            Color.RED))
        tris.add(new Triangle(new Vertex(-100, 100, -100),
                            new Vertex(100, -100, -100),
                            new Vertex(100, 100, 100),
                            Color.GREEN))
        tris.add(new Triangle(new Vertex(-100, 100, -100),
                            new Vertex(100, -100, -100),
                            new Vertex(-100, -100, 100),
                            Color.BLUE))
        tris
    }
}