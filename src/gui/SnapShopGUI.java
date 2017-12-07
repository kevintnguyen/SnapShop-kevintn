/*
 * TCSS 305 – Autumn 2015 Assignment 4 – SnapShop
 */

package gui;

import filters.EdgeDetectFilter;
import filters.EdgeHighlightFilter;
import filters.Filter;
import filters.FlipHorizontalFilter;
import filters.FlipVerticalFilter;
import filters.GrayscaleFilter;
import filters.SharpenFilter;
import filters.SoftenFilter;
import image.PixelImage;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 * SnapShopGUI is a graphic user interface that allows users to display and
 * manipulates images.
 * 
 * @author Kevin Nguen
 * @version 4.5.0 November 2015
 */
public class SnapShopGUI extends JPanel {
    /**
     * serialVersionUID is used to generate an id, and to remove a CheckStyle error.
     */
    private static final long serialVersionUID = -1840617741326366493L;
    /**
     * myButtons is stored as new ArrayList.
     */
    private final List<JButton> myButtons = new ArrayList<>();
    /**
     * myOpen is stored as a new JButton.
     */
    private final JButton myOpen = new JButton("Open...");
    /**
     * mySave is stored as a new JButton.
     */
    private final JButton mySave = new JButton("Save..");
    /**
     * myCloseimage is stored as a new JButton.
     */
    private final JButton myCloseimage = new JButton("Close Image");
    /**
     * myPixelImage is stored as a new PixelImage.
     */
    private PixelImage myPixelImage;
    /**
     * myJframeWindow is stored as a new JFrame.
     */
    private final JFrame myJframeWindow = new JFrame("TCSS 305 SnapShop");
    /**
     * myCenterPanel is stored as a new JPanel.
     */
    private final JPanel myCenterPanel = new JPanel();
    /**
     * myLabelForDisplay is stored as a new JLabel.
     */
    private final JLabel myLabelForDisplay = new JLabel();
    /**
     * myFileChooser is stored as a new JFileChooser.
     */
    private final JFileChooser myFileChooser = 
                    new JFileChooser(System.getProperty("user.dir"));

    /**
     * start() sets up the JButtons and JPanels, which is then used on to the overall Jframe.
     */
    public void start() {

        mainpanel();

        final Dimension frameSize = new Dimension(727, 111);

        myJframeWindow.add(this);
        myJframeWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        myJframeWindow.setSize(frameSize);
        myJframeWindow.setMinimumSize(frameSize);
        myJframeWindow.pack();
        myJframeWindow.setVisible(true);
    }

    /**
     * mainpanel() sets up the required JButtons, and places it into JPanels.
     * which is later used on the main JFrame.
     */
    public void mainpanel() {
        setLayout(new BorderLayout());
        final JPanel northPanel = new JPanel();
        final JPanel southPanel = new JPanel();
        final List<Filter> filters = new ArrayList<>();
        
        //adds filters into the ArrayList filters.
        addFilters(filters);

        for (final Filter f : filters) {
            myButtons.add(createButtons(f));
        }

        for (final JButton b : myButtons) {
            northPanel.add(b);
        }

        addOpenButton();
        addSaveButton();
        addCloseImageButton();

        southPanel.add(myOpen);
        southPanel.add(mySave);
        southPanel.add(myCloseimage);

        add(northPanel, BorderLayout.NORTH);
        add(southPanel, BorderLayout.SOUTH);

    }
/**
 * addSaveButton() adds an ActionListener into the JButton mySave.
 */
    private void addSaveButton() {
        mySave.setEnabled(false);
        mySave.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(final ActionEvent arg0) {
                saveButtonAction();
               

            }
        });
        
    }
    /**
     * saveButtonAction() complies the action for the save button.
     */
    private void saveButtonAction() {
        if (myFileChooser.getSelectedFile() != null) {   
            final int saveornot = myFileChooser.showSaveDialog(getParent());
            if (saveornot == JFileChooser.APPROVE_OPTION) {
                try {
                    myPixelImage.save(myFileChooser.getSelectedFile());
                } catch (final IOException e) {
                    JOptionPane.showMessageDialog(null,
                            "The selected file did not save the image!" , "Error!",
                            JOptionPane.INFORMATION_MESSAGE);
                }
            }
        }
    }
    /**
     * addOpenButton() adds an ActionListener into the JButton myOpen.
     */
    private void addOpenButton() {
        myOpen.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent arg0) {
                openButtonAction();
                
            }
        });
        
    }
    /**
     * openButtonAction() complies the action for the open button.
     */
    private void openButtonAction() {
        final int openornot = myFileChooser.showOpenDialog(myOpen.getParent());
        try { 
            if ((myFileChooser.getSelectedFile() != null) 
                            && (openornot == JFileChooser.APPROVE_OPTION)) {
                myPixelImage = PixelImage.load(myFileChooser.getSelectedFile());
                if (myPixelImage != null) {
                    flipboolean(true);
                    setIcon();
                }
            }
        } catch (final IOException e) {
            JOptionPane.showMessageDialog(null,
                "The selected file did not contain an image!", "Error! ", 
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }

    /**
     * addCloseImageButton() adds an ActionListener into the JButton myCloseImage.
     */
    private void addCloseImageButton() {
        
        myCloseimage.setEnabled(false);
        myCloseimage.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(final ActionEvent theEvent) {
                closeButtonAction();
                
            }
        });
    }
    /**
     * closeButtonAction() complies the action for the close button.
     */
    private void closeButtonAction() {
        flipboolean(false);
        myLabelForDisplay.setIcon(null);
        myJframeWindow.setSize(getMinimumSize());
        
       
    }

    /**
     * addFilters() adds filters into theFilters ArrayList.
     * 
     * @param theFilters ArrayList is passed so filters can be added into it.
     */
    private void addFilters(final List<Filter> theFilters) {
        theFilters.add(new EdgeDetectFilter());
        theFilters.add(new EdgeHighlightFilter());
        theFilters.add(new FlipHorizontalFilter());
        theFilters.add(new FlipVerticalFilter());
        theFilters.add(new GrayscaleFilter());
        theFilters.add(new SharpenFilter());
        theFilters.add(new SoftenFilter());

    }

    /**
     * setIcon() sets myPixelImage as an icon so it can set as a label for myLabelForDisplay. 
     * The label is then added to myCenterPanel to display the image.
     */
    private void setIcon() {
        myLabelForDisplay.setIcon(new ImageIcon(myPixelImage));
        myCenterPanel.add(myLabelForDisplay);
        add(myCenterPanel, BorderLayout.CENTER);
        myJframeWindow.pack();
    }

    /**
     * flipboolean() sets all the JButtons false, except myOpen. If an image is opened,
     * then the JButtons will turn true, otherwise false.
     * @param theOpenAnswer is passed from myOpen's ActionListner.
     */
    private void flipboolean(final boolean theOpenAnswer) {
        if (theOpenAnswer) {
            for (final JButton s : myButtons) {
                s.setEnabled(true);
            }

            mySave.setEnabled(true);
            myCloseimage.setEnabled(true);
        } else {
            for (final JButton s : myButtons) {
                s.setEnabled(false);
            }

            mySave.setEnabled(false);
            myCloseimage.setEnabled(false);

        }
    }

    /**
     * createButtons() creates Filter JButtons with correct names and filter
     * effects.
     * 
     * @param theObject are the filters.
     * @return the completed buttons.
     */
    private JButton createButtons(final Filter theObject) {
        //getFilternames returns the filter's name by removing 
        //"Filter" from it's class name and 
        //adding spaces between every cap letters.
        final String getFilternames = 
            theObject.getClass().getSimpleName().replace(
                              "Filter", "").replaceAll("(.)([A-Z])", "$1 $2");
        final JButton button = new JButton(getFilternames);

 
        button.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(final ActionEvent theEvent) {
                theObject.filter(myPixelImage);
                setIcon();
            }
        });
        button.setEnabled(false);
        return button;
    }
}
