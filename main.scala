import java.awt.{BorderLayout, Color, Graphics, Graphics2D}
import javax.swing.{JFrame, JPanel, SwingUtilities}
import java.util.ArrayList
import java.awt.geom.Path2D
import java.awt.event.MouseMotionListener
import java.awt.event.MouseEvent

@main def main(): Unit = {

    val pitchAngle = Array(0.0) // mutable array of 1 double
    val headingAngle = Array(0.0)

    val tris: ArrayList[Triangle] = new ArrayList[Triangle]()
        tris.add(new Triangle(new Vertex(100, 100, 100),
                            new Vertex(-100, -100, 100),
                            new Vertex(-100, 100, -100),
                            Color.WHITE));
        tris.add(new Triangle(new Vertex(100, 100, 100),
                            new Vertex(-100, -100, 100),
                            new Vertex(100, -100, -100),
                            Color.RED));
        tris.add(new Triangle(new Vertex(-100, 100, -100),
                            new Vertex(100, -100, -100),
                            new Vertex(100, 100, 100),
                            Color.GREEN));
        tris.add(new Triangle(new Vertex(-100, 100, -100),
                            new Vertex(100, -100, -100),
                            new Vertex(-100, -100, 100),
                            Color.BLUE));


    SwingUtilities.invokeLater(() => {
        val frame = new JFrame("Scala Render Panel")
        val pane = frame.getContentPane
        pane.setLayout(new BorderLayout())

        // panel to display render results
        val renderPanel = new JPanel() {
        override def paintComponent(g: Graphics): Unit = {
            super.paintComponent(g)
            val g2 = g.asInstanceOf[Graphics2D]
            g2.setColor(Color.BLACK)
            g2.fillRect(0, 0, getWidth, getHeight)

            // rendering magic will happen here

            g2.translate(getWidth() / 2, getHeight() / 2);
            g2.setColor(Color.WHITE);

            val dx: Double = Math.toRadians(headingAngle(0));
            val dy: Double = Math.toRadians(pitchAngle(0));
            // Rotation magic
            val transform: Matrix3 = Matrix3.heading(dx) *  Matrix3.pitch(dy)

            tris.forEach(t => {

                val v1: Vertex = transform.transform(t.v1);
                val v2: Vertex = transform.transform(t.v2);
                val v3: Vertex = transform.transform(t.v3);
                val path: Path2D = new Path2D.Double()
                path.moveTo(v1.x, v1.y)
                path.lineTo(v2.x, v2.y)
                path.lineTo(v3.x, v3.y)
                path.closePath()
                g2.draw(path)
            })
        }
        }



        renderPanel.addMouseMotionListener(new MouseMotionListener() {
            override def mouseDragged(e: MouseEvent) = {
                val yi: Double = 180.0 / 6 //renderPanel.getHeight();
                val xi: Double = 180.0 / 6 //renderPanel.getWidth();
                headingAngle(0) = (e.getX() * xi)
                pitchAngle(0) = -(e.getY() * yi)
                renderPanel.repaint();
            }

            override def mouseMoved(e: MouseEvent) = {
            }
            });

        pane.add(renderPanel, BorderLayout.CENTER)

        frame.setSize(600, 600)
        frame.setDefaultCloseOperation(3) // JFrame.EXIT_ON_CLOSE
        frame.setVisible(true)
    })
}