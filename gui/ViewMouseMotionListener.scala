import java.awt.event.MouseMotionAdapter
import java.awt.event.MouseMotionListener
import javax.swing.JComponent
import java.awt.event.MouseEvent
import javax.swing.JPanel

class ViewMouseMotionListener(using component: JPanel, angles: Angles) extends MouseMotionListener {

    override def mouseDragged(e: MouseEvent): Unit = {
    val xi = 180.0 / (component.getWidth /100)
    val yi = 180.0 / (component.getHeight /100)
    angles.heading = e.getX * xi
    angles.pitch = -e.getY * yi
    component.repaint()

    }

    override def mouseMoved(e: MouseEvent): Unit = {}

}