import java.awt.BorderLayout
import javax.swing.SwingUtilities
import javax.swing.JFrame

class GameWindow {

    def launch(): Unit = {
        SwingUtilities.invokeLater(() => {
            val frame = new JFrame("Scala 3d engine")
            frame.setDefaultCloseOperation(3) //JFrame.EXIT_ON_CLOSE
            frame.setSize(16 * 100, 9 * 100)
            frame.add(new RenderPanel(), BorderLayout.CENTER)
            frame.setVisible(true)
        })
    }
}