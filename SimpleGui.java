import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.File;
import java.io.IOException;
import java.util.List;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;

public class SimpleGui {
    private static String backgroundPath = "background.jpg"; // Default background

    public static void main(String[] args) {
        // Initialize JavaFX
        SwingUtilities.invokeLater(() -> {
            try {
                loadCustomBackground();
                // Create the main frame
                JFrame mainFrame = new JFrame("Main Window");
                mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                mainFrame.setSize(800, 600); // Set size for better visibility
                mainFrame.setLayout(new BorderLayout());

                // Create and set the main panel with a background image
                JPanel mainPanel = createMainPanel(mainFrame);
                mainFrame.add(mainPanel, BorderLayout.CENTER); // Add the main panel to the frame

                // Add key listener for F5
                mainFrame.addKeyListener(new KeyAdapter() {
                    @Override
                    public void keyPressed(KeyEvent e) {
                        if (e.getKeyCode() == KeyEvent.VK_F5) {
                            System.out.println("F5 pressed"); // Debugging output
                            showPowerOffDialog(mainFrame);
                        }
                    }
                });

                // Add key listener for F4
                mainFrame.addKeyListener(new KeyAdapter() {
                    @Override
                    public void keyPressed(KeyEvent e) {
                        if (e.getKeyCode() == KeyEvent.VK_F4) {
                            System.out.println("F4 pressed"); // Debugging output
                            showRebootDialog(mainFrame);
                        }
                    }
                });

                // Add key listener for F10
                mainFrame.addKeyListener(new KeyAdapter() {
                    @Override
                    public void keyPressed(KeyEvent e) {
                        if (e.getKeyCode() == KeyEvent.VK_F10) {
                            System.out.println("F10 pressed"); // Debugging output
                            showSettings(mainFrame);
                        }
                    }
                });

                // Add key listener for F9
                mainFrame.addKeyListener(new KeyAdapter() {
                    @Override
                    public void keyPressed(KeyEvent e) {
                        if (e.getKeyCode() == KeyEvent.VK_F9) {
                            System.out.println("F9 pressed"); // Debugging output
                            showKCRSOFTPanel(mainFrame);
                        }
                    }
                });

                mainFrame.setVisible(true);
                mainFrame.setExtendedState(JFrame.MAXIMIZED_BOTH); // Maximize the frame to cover the entire screen
                mainFrame.setFocusable(true); // Make sure the frame is focusable
                mainFrame.requestFocusInWindow(); // Request focus to ensure key events are received
            } catch (Exception ex) {
                showErrorDialog("An error occurred", ex);
            }
        });
    }

    // Function to display the error dialog
    private static void showErrorDialog(String message, Exception ex) {
        // Create a gray dialog similar to the settings one
        JPanel errorPanel = new JPanel(new BorderLayout());
        errorPanel.setBackground(Color.LIGHT_GRAY);
        errorPanel.setBorder(BorderFactory.createTitledBorder("Error"));

        // Add a warning icon on the left side
        JLabel iconLabel = new JLabel(new ImageIcon("warning.png"));
        errorPanel.add(iconLabel, BorderLayout.WEST);

        // Create a label to display the error message
        JLabel errorLabel = new JLabel("<html>" + message + "<br>" + ex.toString() + "</html>");
        errorLabel.setFont(new Font("Arial", Font.BOLD, 16));
        errorLabel.setHorizontalAlignment(SwingConstants.CENTER);
        errorPanel.add(errorLabel, BorderLayout.CENTER);

        // Create the dialog window
        JDialog errorDialog = new JDialog();
        errorDialog.setTitle("Critical Error");
        errorDialog.setSize(400, 200);
        errorDialog.setModal(true); // Make it a modal dialog
        errorDialog.add(errorPanel);

        // Position and show the dialog
        errorDialog.setLocationRelativeTo(null);
        errorDialog.setVisible(true);
    }

    private static JPanel createMainPanel(JFrame mainFrame) {
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // Load and draw the background image
                Image backgroundImage = new ImageIcon(backgroundPath).getImage();
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            }
        };

        panel.setLayout(new FlowLayout(FlowLayout.CENTER, 50, 50)); // Center the buttons with spacing

        // Create buttons with icons
        JButton settingsButton = createButton("settings.png", e -> showSettings(mainFrame));
        panel.add(settingsButton);

        JButton phoneButton = createButton("phone.png", e -> showPhone(mainFrame));
        panel.add(phoneButton);
        
        JButton browserButton = createButton("browser.png", e -> showWebPanel(mainFrame));
        panel.add(browserButton);

        JButton mediaButton = createButton("media.png", e -> showMediaPlayer(mainFrame)); // New media button
        panel.add(mediaButton);

        JButton youtubeButton = createButton("youtube.png", e -> showYoutubeScreen(mainFrame));
        panel.add(youtubeButton); // Add button to the gallery panels

        JButton rebootButton = createButton("reboot.png", e -> showRebootDialog(mainFrame));
        panel.add(rebootButton); // Add button to the gallery panels

        JButton exitButton = createButton("exit.png", e -> showPowerOffDialog(mainFrame)); // Call power-off dialog
        panel.add(exitButton);

        return panel;
    }

    private static JButton createButton(String iconPath, ActionListener actionListener) {
        JButton button = new JButton(new ImageIcon(iconPath)); // Ensure the image is in the project directory
        button.setPreferredSize(new Dimension(150, 150)); // Increased button size
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        button.addActionListener(actionListener);
        return button;
    }

    private static List<String> audioFiles = new ArrayList<>();

    private static MediaPlayer mediaPlayer; // MediaPlayer to control playback
    private static JFrame mainFrame; // Keep a reference to the main frame

    private static void showMediaPlayer(JFrame frame) {
        mainFrame = frame; // Store the main frame reference
        JPanel mediaPanel = new JPanel(new BorderLayout());
        mediaPanel.setBackground(Color.WHITE);

        JLabel mediaLabel = new JLabel("Media Player", SwingConstants.CENTER);
        mediaLabel.setFont(new Font("Arial", Font.BOLD, 24));
        mediaPanel.add(mediaLabel, BorderLayout.NORTH);

        JFXPanel jfxPanel = new JFXPanel(); // JavaFX panel for media player
        mediaPanel.add(jfxPanel, BorderLayout.CENTER);

        // Button to load music files
        JButton loadMusicButton = new JButton("Load Music");
        loadMusicButton.addActionListener(e -> openFileChooser());
        mediaPanel.add(loadMusicButton, BorderLayout.SOUTH);

        // Add an exit button to return to the main panel
        JButton exitButton = new JButton("Exit to Main");
        exitButton.setPreferredSize(new Dimension(150, 50));
        exitButton.addActionListener(e -> returnFromMediaToMainPanel());
        mediaPanel.add(exitButton, BorderLayout.NORTH);

        mainFrame.getContentPane().removeAll();
        mainFrame.add(mediaPanel, BorderLayout.CENTER);
        mainFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        mainFrame.revalidate();
        mainFrame.repaint();
    }

    private static void openFileChooser() {
        // Open a file chooser to select audio files
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File("musics")); // Set initial directory
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Audio Files", "mp3", "wav", "aac", "flac"));

        int result = fileChooser.showOpenDialog(mainFrame);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            playAudio(selectedFile);
        }
    }

    private static void playAudio(File audioFile) {
        if (mediaPlayer != null) {
            mediaPlayer.stop(); // Stop current playback if any
        }

        // Create a new media player for the selected audio file
        Media media = new Media(audioFile.toURI().toString());
        mediaPlayer = new MediaPlayer(media);
        mediaPlayer.play(); // Start playback
        System.out.println("Playing audio: " + audioFile.getName());
    }

    private static void returnFromMediaToMainPanel() {
         if (mediaPlayer != null) {
            mediaPlayer.stop(); // Stop playback when exiting
        }
        // Call the method to return to the main panel
        returnToMainPanel(mainFrame); // Update this method according to your main panel logic
    }

    private static void showPowerOffDialog(JFrame mainFrame) {
        JPanel powerOffPanel = new JPanel(new BorderLayout());
        powerOffPanel.setBackground(Color.LIGHT_GRAY);
        powerOffPanel.setBorder(BorderFactory.createTitledBorder("Power Off"));

        JLabel powerOffLabel = new JLabel("Are you sure you want to power off?", SwingConstants.CENTER);
        powerOffLabel.setFont(new Font("Arial", Font.BOLD, 24));
        powerOffPanel.add(powerOffLabel, BorderLayout.CENTER);

        JButton confirmButton = new JButton("Power Off");
        confirmButton.setPreferredSize(new Dimension(150, 50));
        confirmButton.addActionListener(e -> System.exit(0));

        JButton cancelButton = new JButton("Cancel");
        cancelButton.setPreferredSize(new Dimension(150, 50));
        cancelButton.addActionListener(e -> returnToMainPanel(mainFrame));

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(confirmButton);
        buttonPanel.add(cancelButton);
        powerOffPanel.add(buttonPanel, BorderLayout.SOUTH);

        mainFrame.getContentPane().removeAll();
        mainFrame.add(powerOffPanel, BorderLayout.CENTER);
        mainFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        mainFrame.revalidate();
        mainFrame.repaint();
    }


    private static void showRebootDialog(JFrame mainFrame) {
        JPanel RebootPanel = new JPanel(new BorderLayout());
        RebootPanel.setBackground(Color.LIGHT_GRAY);
        RebootPanel.setBorder(BorderFactory.createTitledBorder("Reboot"));

        JLabel RebootLabel = new JLabel("Are you sure you want to Reboot?", SwingConstants.CENTER);
        RebootLabel.setFont(new Font("Arial", Font.BOLD, 24));
        RebootPanel.add(RebootLabel, BorderLayout.CENTER);

        JButton confirmButton = new JButton("Reboot");
        confirmButton.setPreferredSize(new Dimension(150, 50));
        confirmButton.addActionListener(e -> System.exit(0));

        JButton cancelButton = new JButton("Cancel");
        cancelButton.setPreferredSize(new Dimension(150, 50));
        cancelButton.addActionListener(e -> returnToMainPanel(mainFrame));

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(confirmButton);
        buttonPanel.add(cancelButton);
        RebootPanel.add(buttonPanel, BorderLayout.SOUTH);

        mainFrame.getContentPane().removeAll();
        mainFrame.add(RebootPanel, BorderLayout.CENTER);
        mainFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        mainFrame.revalidate();
        mainFrame.repaint();
    }

    private static void showWebPanel(JFrame mainFrame) {
        JPanel webPanel = new JPanel(new BorderLayout());
        webPanel.setBackground(Color.WHITE);

        JLabel webLabel = new JLabel("Web Browser Interface", SwingConstants.CENTER);
        webLabel.setFont(new Font("Arial", Font.BOLD, 24));
        webPanel.add(webLabel, BorderLayout.NORTH);

        JFXPanel jfxPanel = new JFXPanel();
        webPanel.add(jfxPanel, BorderLayout.CENTER);

        Platform.runLater(() -> {
            WebView webView = new WebView();
            WebEngine webEngine = webView.getEngine();
            webEngine.load("https://www.google.com"); // Change to the desired website

            jfxPanel.setScene(new Scene(webView));
        });

        // Add an exit button to return to the main panel
        JButton exitButton = new JButton("Exit to Main");
        exitButton.setPreferredSize(new Dimension(150, 50));
        exitButton.addActionListener(e -> returnToMainPanel(mainFrame));
        webPanel.add(exitButton, BorderLayout.SOUTH);

        mainFrame.getContentPane().removeAll();
        mainFrame.add(webPanel, BorderLayout.CENTER);
        mainFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        mainFrame.revalidate();
        mainFrame.repaint();
    }

    private static void showKCRSOFTPanel(JFrame mainFrame) {
        JPanel webPanel = new JPanel(new BorderLayout());
        webPanel.setBackground(Color.WHITE);

        JLabel webLabel = new JLabel("Web Browser Interface", SwingConstants.CENTER);
        webLabel.setFont(new Font("Arial", Font.BOLD, 24));
        webPanel.add(webLabel, BorderLayout.NORTH);

        JFXPanel jfxPanel = new JFXPanel();
        webPanel.add(jfxPanel, BorderLayout.CENTER);

        Platform.runLater(() -> {
            WebView webView = new WebView();
            WebEngine webEngine = webView.getEngine();
            webEngine.load("https://kenzobasar.github.io"); // Change to the desired website

            jfxPanel.setScene(new Scene(webView));
        });

        // Add an exit button to return to the main panel
        JButton exitButton = new JButton("Exit to Main");
        exitButton.setPreferredSize(new Dimension(150, 50));
        exitButton.addActionListener(e -> returnToMainPanel(mainFrame));
        webPanel.add(exitButton, BorderLayout.SOUTH);

        mainFrame.getContentPane().removeAll();
        mainFrame.add(webPanel, BorderLayout.CENTER);
        mainFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        mainFrame.revalidate();
        mainFrame.repaint();
    }

    private static void showYoutubeScreen(JFrame mainFrame) {
        JPanel youtubePanel = new JPanel(new BorderLayout());
        youtubePanel.setBackground(Color.WHITE);

        JLabel youtubeLabel = new JLabel("Youtube", SwingConstants.CENTER);
        youtubeLabel.setFont(new Font("Arial", Font.BOLD, 24));
        youtubePanel.add(youtubeLabel, BorderLayout.NORTH);

        JFXPanel jfxPanel = new JFXPanel();
        youtubePanel.add(jfxPanel, BorderLayout.CENTER);

        Platform.runLater(() -> {
            WebView webView = new WebView();
            WebEngine webEngine = webView.getEngine();
            webEngine.load("https://www.youtube.com"); // Change to the desired website

            jfxPanel.setScene(new Scene(webView));
        });

        // Add an exit button to return to the main panel
        JButton exitButton = new JButton("Exit to Main");
        exitButton.setPreferredSize(new Dimension(150, 50));
        exitButton.addActionListener(e -> returnToMainPanel(mainFrame));
        youtubePanel.add(exitButton, BorderLayout.SOUTH);

        mainFrame.getContentPane().removeAll();
        mainFrame.add(youtubePanel, BorderLayout.CENTER);
        mainFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        mainFrame.revalidate();
        mainFrame.repaint();
    }

    private static void loadCustomBackground() {
        try {
            String path = new String(Files.readAllBytes(Paths.get("customwallpaper.config")));
            backgroundPath = path.trim(); // Update background path
        } catch (IOException e) {
            System.out.println("No custom wallpaper found, using default.");
        }
    }

    private static void showSettings(JFrame mainFrame) {
        mainFrame.getContentPane().removeAll();

        JPanel settingsPanel = new JPanel();
        settingsPanel.setLayout(new BorderLayout());
        settingsPanel.setBackground(Color.LIGHT_GRAY);
        settingsPanel.setBorder(BorderFactory.createTitledBorder("Settings"));

        JLabel settingsLabel = new JLabel("Settings Content", SwingConstants.CENTER);
        settingsPanel.add(settingsLabel, BorderLayout.CENTER);

        JButton changeBackgroundButton = new JButton("Change Background");
        changeBackgroundButton.addActionListener(e -> changeBackground());
        settingsPanel.add(changeBackgroundButton, BorderLayout.SOUTH);

        JButton exitButton = new JButton("Return to Main");
        exitButton.setPreferredSize(new Dimension(100, 50));
        exitButton.addActionListener(e -> returnToMainPanel(mainFrame));

        settingsPanel.add(exitButton, BorderLayout.NORTH);

        mainFrame.add(settingsPanel, BorderLayout.CENTER);
        mainFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        mainFrame.revalidate();
        mainFrame.repaint();
    }

    private static void changeBackground() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Select Background Image");
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Image files", "png", "jpg", "jpeg"));
        
        int userSelection = fileChooser.showOpenDialog(null);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            String filePath = fileToSave.getAbsolutePath();
            backgroundPath = filePath; // Update background path

            try (BufferedWriter writer = new BufferedWriter(new FileWriter("customwallpaper.config"))) {
                writer.write(filePath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static void showPhone(JFrame mainFrame) {
        JPanel phonePanel = new JPanel();
        phonePanel.setLayout(new BorderLayout());

        // Phone number input field
        JTextField phoneNumberField = new JTextField();
        phoneNumberField.setPreferredSize(new Dimension(200, 40));
        phonePanel.add(phoneNumberField, BorderLayout.NORTH);

        // Call status label
        JLabel callStatusLabel = new JLabel("Enter a phone number and press 'Call'", SwingConstants.CENTER);
        phonePanel.add(callStatusLabel, BorderLayout.CENTER);

        // Call button
        JButton callButton = new JButton("Call");
        callButton.setPreferredSize(new Dimension(100, 50));
        callButton.addActionListener(e -> simulateCall(phoneNumberField.getText(), callStatusLabel));

        // Exit button
        JButton exitButton = new JButton("Return to Main");
        exitButton.setPreferredSize(new Dimension(100, 50));
        exitButton.addActionListener(e -> returnToMainPanel(mainFrame));

        // Panel for buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(callButton);
        buttonPanel.add(exitButton);
        
        phonePanel.add(buttonPanel, BorderLayout.SOUTH);

        // Display the phone panel
        mainFrame.getContentPane().removeAll();
        mainFrame.add(phonePanel, BorderLayout.CENTER);
        mainFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        mainFrame.revalidate();
        mainFrame.repaint();
    }

    private static void simulateCall(String phoneNumber, JLabel callStatusLabel) {
        if (phoneNumber.isEmpty()) {
            callStatusLabel.setText("Please enter a valid phone number.");
        } else {
            callStatusLabel.setText("Calling " + phoneNumber + "...");
            // Simulate call duration
            Timer timer = new Timer(3000, e -> callStatusLabel.setText("Call ended."));
            timer.setRepeats(false); // Only run once
            timer.start();
        }
    }

    private static void returnToMainPanel(JFrame mainFrame) {
        mainFrame.getContentPane().removeAll();
        JPanel mainPanel = createMainPanel(mainFrame);
        mainFrame.add(mainPanel, BorderLayout.CENTER);
        mainFrame.revalidate();
        mainFrame.repaint();
    }
}
