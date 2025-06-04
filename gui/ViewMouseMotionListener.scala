import java.awt.event.MouseMotionAdapter
import java.awt.event.MouseMotionListener
import javax.swing.JComponent
import java.awt.event.MouseEvent
import javax.swing.JPanel

class ViewMouseMotionListener(using component: JPanel, angles: Angles) extends MouseMotionListener {

    var lastX = 0
    var lastY = 0

    override def mouseDragged(e: MouseEvent): Unit = {

        val deltaX = e.getX - lastX
        val deltaY = e.getY - lastY
        lastX = e.getX
        lastY = e.getY

        angles.yaw += deltaX * 0.5   // sensitivity factor
        angles.pitch += deltaY * 0.5

        // clamp pitch to avoid flipping over
        if (angles.pitch > 89) angles.pitch = 89
        if (angles.pitch < -89) angles.pitch = -89

        component.repaint()
    }

    override def mouseMoved(e: MouseEvent): Unit = {}

}