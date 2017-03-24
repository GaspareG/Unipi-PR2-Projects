import javax.swing.*;        

public class Prova {
    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event-dispatching thread.
     */
    private static void createAndShowGUI() {
        //Create and set up the window.
        JFrame frame = new JFrame("Prova Swing by Gaspare");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Add the ubiquitous "Hello World" label.
        JLabel label = new JLabel("Hello World");
        frame.getContentPane().add(label);


		JProgressBar progressBar = new JProgressBar(0, 100);
		progressBar.setValue(45);
		progressBar.setStringPainted(true);
		progressBar.setSize(100,20);
		
		frame.getContentPane().add(progressBar);
		
        //Display the window.
        frame.setSize(500, 500);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    	System.out.println("PROVA");
    }
}