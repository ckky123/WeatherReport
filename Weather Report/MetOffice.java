import java.util.ArrayList;
import java.io.*;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
/**
 * class MetOffice, to define a national meteorological office, e.g. the UK Met Office.
 * Users can update and interrogate the data files for different weather stations 
 * through the main method in class MetOffice.
 * 
 * @author (Wendy) 
 * @version (a version number or a date)
 */
public class MetOffice
{
    private ArrayList<WeatherStation>  sWeatherStation;
    private JFrame                     metOfficeFrame;
    private JButton                    loadFile;
    private JButton                    saveFile;
    private DefaultListModel           weatehrStationlistModel;
    private JList                      weatehrStationList;
    private JScrollPane                scrollPane;
    private JTextArea                  summaryText;

    /**
     * Constructor for objects of class MetOffice
     * 
     */
    public MetOffice()
    {
        this.sWeatherStation = new ArrayList<WeatherStation> ();
        metOfficeFrame = new JFrame ("UK Met Office");
        metOfficeFrame.getContentPane ().setLayout (null);
        metOfficeFrame.setSize (450, 400);
        metOfficeFrame.setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE); 
        metOfficeFrame.setVisible (true); 

        summaryText = new JTextArea ();
        summaryText.setBounds (10, 250, 400, 80);
        summaryText.setEditable(false);

        metOfficeFrame.getContentPane ().add (summaryText); 

        loadFile = new JButton ("Load File");
        loadFile.setBounds (280, 40, 120, 30);
        loadFile.setToolTipText ("Read weather satation file.");
        loadFile.setEnabled (true);
        loadFile.addActionListener (new ActionListener() {
                public void actionPerformed (ActionEvent e) {
                    loadWeatherStationFileChooser ();
                }
            });
        metOfficeFrame.getContentPane ().add (loadFile); 

        saveFile = new JButton ("Save File");
        saveFile.setBounds (280, 80, 120, 30);
        saveFile.setToolTipText ("Save selected weather satation file.");
        saveFile.setEnabled (true);
        saveFile.addActionListener (new ActionListener() {
                public void actionPerformed (ActionEvent e) {
                    saveWeatherStationFileChooser ();
                }
            });
        metOfficeFrame.getContentPane ().add (saveFile);     

        weatehrStationlistModel = new DefaultListModel ();
        weatehrStationList = new JList (weatehrStationlistModel);
        scrollPane = new JScrollPane (weatehrStationList);

        scrollPane.setBounds(10, 40, 240, 200);
        metOfficeFrame.getContentPane ().add (scrollPane);
        try {
            this.loadWeatherStationFolder ("file");
        } catch (Exception e) {};
    }

    /**
     * Add a weatherstation to the Met Office.  
     * 
     * @param station The weather station to be added.
     */
    public void addWeatherStation (WeatherStation station) throws InvailidDataException
    {
        if (sWeatherStation.contains (station))
        {
            throw new InvailidDataException("This weatger station already been added");
        }
        sWeatherStation.add (station);
    }

    /**
     * A get method - to get the weather station.
     * 
     * @return If the weather staion o is equal to the parameter, name then the station, o is returned.
     * @return If the weather staion o is not equal to the parameter, name then the station, null is returned.
     */
    public WeatherStation getWeatherStation (String name)
    {
        for (WeatherStation o : sWeatherStation)
        {
            if (o.getName ().equals (name))
            {
                return o;
            }
        }
        return null;
    }

    /**
     * A get method - to find the largest annual rainfall 
     * 
     * @return The largest annual rainfall of the Met Offic.
     */
    public WeatherStation getLargestAnnualRainfall (int year) 
    {
        double dMax = -1;
        WeatherStation sMaxSation = null;
        for (WeatherStation obj : sWeatherStation)
        {
            try {
                double dAnuRainFall = obj.getTRain (year);
                sMaxSation = (dAnuRainFall > dMax) ? obj : sMaxSation;
                dMax = (dAnuRainFall > dMax) ? dAnuRainFall : dMax;
            } catch (InvailidDataException e) {}
        }
        return sMaxSation;
    }

    /**
     * A get method - to find the average mean monthly temperature with a given position
     * 
     * @return The average mean monthly temperature of the Met Office
     */
    public double getAvgMeanMonthlyTemp (LatLong position)
    {
        double dSum = 0;
        double iCount = 0;
        for (WeatherStation o : sWeatherStation)
        {
            if (o.getPosition().getLat () >= position.getLat ())
            {
                double dMeanTemp = o.getAvgMeanMonthlyTemp ();
                dSum += (dMeanTemp == WeatherObservation.missingData) ? 0 : dMeanTemp;
                iCount += (dMeanTemp == WeatherObservation.missingData) ? 0 : 1;
            }
        }
        return (iCount > 0) ? (dSum /iCount) : WeatherObservation.missingData;
    }

    public void loadWeatherStationFile (String strFilePath) throws IOException, InvailidDataException
    {
        String line;
        String strInfo;
        WeatherStation newWeatherStation;

        FileReader file = new FileReader (strFilePath);
        BufferedReader buffer = new BufferedReader (file);

        strInfo = buffer.readLine () + " ";
        strInfo += buffer.readLine ();
        try {
            newWeatherStation = new WeatherStation (strInfo);
        } catch (InvailidDataException e) {
            throw new InvailidDataException ("Parser File failed (" + strFilePath + ") -> " + e.getMessage ());
        }

        while ((line = buffer.readLine()) != null) 
        {
            try {
                WeatherObservation newWeatherObservation = new WeatherObservation (line);
                newWeatherStation.addObservation (newWeatherObservation);
            } catch (InvailidDataException e) {};
        }
        addWeatherStation (newWeatherStation);
        if (weatehrStationlistModel != null) {
            weatehrStationlistModel.addElement (newWeatherStation.getName());
        }
    }

    public void loadWeatherStationFolder (String strFilePath) throws IOException, InvailidDataException
    {
        File folder = new File (strFilePath);
        String[] strFileList = folder.list (); 
        for (String s : strFileList) {
            loadWeatherStationFile (strFilePath + "\\" + s);             
        }
    }

    public void loadWeatherStationFileChooser ()
    {
        JFileChooser chooser = new JFileChooser (); 
        chooser.setCurrentDirectory (new java.io.File ("."));
        chooser.setDialogTitle ("Read weather station file");
        chooser.setFileSelectionMode (JFileChooser.FILES_ONLY);
        chooser.setAcceptAllFileFilterUsed (false);
        if (chooser.showOpenDialog(metOfficeFrame) == JFileChooser.APPROVE_OPTION) { 
            try {
                loadWeatherStationFile (chooser.getSelectedFile ().toString ());
            } catch (Exception eException) {
                JOptionPane.showMessageDialog (null, eException.getMessage ());
            }
        }
    }

    public void saveWeatherStationFile (WeatherStation ws, String strFilePath) throws IOException
    {
        FileWriter fw =  new FileWriter (strFilePath);
        String strNewLine = System.getProperty ("line.separator");

        fw.write (ws.getName () + strNewLine);
        fw.write ("Location " + ws.getPosition ().toString () + " " + ws.getSeaLevel() + "m amsl" + strNewLine);
        fw.write ("Missing data (more than 2 days missing in month) is marked by  ---." + strNewLine);
        fw.write ("yyyy  mm    tmax    tmin      af    rain     sun" + strNewLine);
        fw.write ("            degC    degC    days      mm   hours" + strNewLine);
        for (WeatherObservation o : ws.observations()) {
            fw.write (o.toString () + strNewLine);
        }
        fw.flush ();
        fw.close ();
    }

    public void saveWeatherStationFileChooser ()
    {
        JFileChooser chooser = new JFileChooser (); 
        chooser.setCurrentDirectory (new java.io.File ("."));
        chooser.setDialogTitle ("Save weather station file");
        chooser.setFileSelectionMode (JFileChooser.FILES_ONLY);
        chooser.setAcceptAllFileFilterUsed (false);
        if (chooser.showSaveDialog(metOfficeFrame) == JFileChooser.APPROVE_OPTION) { 
            if(weatehrStationList.getSelectedIndex() != -1) {
                try {
                    WeatherStation ws = getWeatherStation (weatehrStationList.getSelectedValue ().toString ());
                    saveWeatherStationFile (ws , chooser.getSelectedFile ().toString ());
                } catch (Exception eException) {
                    JOptionPane.showMessageDialog (null, eException.getMessage ());
                }
            }
        }
    }

    public static void main (String[] args)
    {
        MetOffice sUKMetOffice = new MetOffice ();
    }
}
