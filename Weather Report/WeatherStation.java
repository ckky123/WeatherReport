import java.util.ArrayList; 
/**
 * class WeatherStation represents weather stations and their associated observations. 
 * 
 * @author (Wendy) 
 * @version (a version number or a date)
 */
public class WeatherStation
{
    private String name;
    private LatLong position;
    private int seaLevel;
    private int yearOfOpen;
    private int yearOfClosing;
    private ArrayList<WeatherObservation> observations;

    /**
     * Constructor for objects of class WeatherStation
     * 
     * @param name New value for the weather station.
     * @param seaLevel New value for a height above mean sea level(e.g. 242) in metres.
     * @param yo New value for a year of opening of the weather station.
     * @param yc New value for a year of closing of the weather station.
     * @param position New value for the weather station's position.
     */
    public WeatherStation(String name, int seaLevel, int yo, int yc, LatLong position){

        this.name=name;
        yearOfOpen=yo;
        yearOfClosing=yc;
        this.position=position;
        this.seaLevel=seaLevel;
        observations = new ArrayList<WeatherObservation>();

    }

    /**
     * Constructor for objects of WeatherStation
     * 
     * @param format New value for the WeatherStation . 
     * 
     */
    public WeatherStation(String format) throws InvailidDataException {
        String[] pArray = format.trim().split("\\s+");
        observations = new ArrayList<WeatherObservation>();
        yearOfOpen=Integer.MAX_VALUE;
        yearOfClosing=Integer.MIN_VALUE;
        try { //"Eskdalemuir Location 3234E 6026N 242m amsl"
            name = pArray[0];
            position = new LatLong (pArray[2] + " " + pArray[3]);
            seaLevel = Integer.parseInt(pArray[4].substring(0, pArray[4].length()-1));
        } catch (IndexOutOfBoundsException e) {
            throw new InvailidDataException ("WeatherStation : Data is not complete");
        } catch (NumberFormatException e) {
            throw new InvailidDataException ("WeatherStation : Digital number format error.");
        } catch (InvailidDataException e) {
            throw new InvailidDataException ("WeatherStation -> " + e.getMessage());
        }
    }

    /**
     * A get method - name of the weather station.
     * 
     * @return The current value of the name of the weather station
     */
    public String getName ()
    {
        return name;
    }

    /**
     * A set method - name of the weather station.
     * 
     * @param name New value for the name of the weather station.
     */
    public void setName (String name)
    {
        this.name = name;
    }

    /**
     * A get method - position of the weather station.
     * 
     * @return The current value of the position of the weather station
     */
    public LatLong getPosition ()
    {
        return position;
    }

    /**
     * A get method - a height above mean sea level of the weather station.
     * 
     * @return The current value of the height above mean sea level of the weather station
     */
    public int getSeaLevel()
    {
        return seaLevel;
    }

    /**
     * to save observations
     * 
     * @return The current value of observatons.
     */
    public ArrayList<WeatherObservation> observations ()
    {
        return observations;
    }

    /**
     * Add an observation to the station.  
     * 
     * @param o The observatoin to be added.
     */
    public void addObservation (WeatherObservation o) throws InvailidDataException
    {
        int year = o.getYear();
        if (observations.contains (o))
        {
            throw new InvailidDataException("This year/month already been added");
        }
        yearOfOpen = ( yearOfOpen > year) ? year :  yearOfOpen;
        yearOfClosing = (yearOfClosing> year) ? yearOfClosing : year;

        for (int i = 0; i < observations.size(); i++)
        {
            if (o.compareTo ( observations.get(i) ) < 0 )
            {
                observations.add (i, o);
                return;
            }
        }
        observations.add(o);
    }

    /**
     * A get method - to get an observation for a given month and year
     * 
     * @return If the year and month is exsit, return the value of observation, if cannot find the data, return null
     */
    public WeatherObservation getObservation(int month, int year) 
    {   
        for (WeatherObservation search: observations) 
        {
            if (search.getYear() == year && search.getMonth() == month) 
            {
                return search;
            }
        }
        return null;
    }

    /**
     * A get method - to find the total rainfall for a given month. 
     * 
     * @return The current value of  the total rainfall for a given month of the weather station
     */
    public double getTRain(int year) throws InvailidDataException
    {   
        double sum=0;
        int i=0;
        for (WeatherObservation r: observations)
        {
            if (r.getYear() == year) {
                if((r.getTRain()==WeatherObservation.missingData ))
                {
                    throw new InvailidDataException("missing data");
                }
                sum += r.getTRain();
                i++;             
            }
            if (i!=12){
                throw new InvailidDataException("missing data");
            }
        }
        return sum;
    }

    /**
     * A get method - to find the wettest year(highest total rainfall)
     * 
     * @return The current value of maxYear which is the wettest year of the weather station
     */
    public int getWettestYear ()throws InvailidDataException
    {
        double maxRain = -1;
        double sumRain = 0;
        int maxYear = 0;
        int year = this.yearOfOpen;

        if (observations.size() == 0) 
        {
            throw new InvailidDataException("missing data");
        }

        for (WeatherObservation o : observations)
        {
            if (o.getYear() != year)
            {
                maxYear = (sumRain > maxRain) ? year : maxYear;
                maxRain = (sumRain > maxRain) ? sumRain : maxRain;
                year = o.getYear();
                sumRain = 0;
            }
            sumRain += ((o.getTRain() != WeatherObservation.missingData) ? o.getTRain()  : 0);  
        }
        maxYear = (sumRain > maxRain) ? year : maxYear;
        return maxYear;
    }

    /**
     * A get method - to find the month which is sunniest on average
     * 
     * @return The current value of maxMonthSun which is  sunniest month on average of the weather station
     */
    public int getSunniestMonth()throws InvailidDataException
    {  
        int month;
        int [] months=new int [12];
        double []tSun=new double[12];
        double[]aSun= new double[12];
        int maxMonthSun=0;

        if (observations.size() == 0) 
        {
            throw new InvailidDataException("missing data");
        }

        for(WeatherObservation o: observations ) // get total sunshine in every month
        {
            month= o.getMonth()-1;
            tSun[month]+=o.getTSunshine();
            months[month]++;
        }

        for(int i=0; i<12 ; i++){

            if( months[i] > 0)
            {
                aSun[i] = tSun[i] / months[i];
            }
            else
            {
                aSun[i]=Double.NEGATIVE_INFINITY; 
            }
            maxMonthSun= (aSun[i]> aSun[maxMonthSun])? i:maxMonthSun;
        }
        return maxMonthSun;
    }

    /**
     * A completion method - to find data completion percentage of the weather station.
     * 
     * @return The current value of the weather station's data completion percentage.
     */
    public double completion ()
    {
        double sum = 0;
        int first = 0;
        int last = 0;
        if (observations.size() == 0) 
        {
            return WeatherObservation.missingData;
        }

        for (WeatherObservation o : observations)
        {
            sum += o.completion();
        }
        first = (observations.get(0).getYear() * 12) + observations.get(0).getMonth();
        last = (observations.get(observations.size()-1).getYear() * 12) + observations.get(observations.size()-1).getMonth();
        return sum / (last - first + 1);
    }

    /**
     * A euqals method - The method determines whether the Number object that invokes the method is equal to the object that is passed as argument.
     * 
     * @return The methods returns True if the argument is not null and is an object of the same type and with the same numeric value.
     */
    public boolean equals (Object object)
    {
        boolean same = false;
        if (object != null && object instanceof WeatherStation)
        {
            same = ((WeatherStation) object).getName().equals (this.name);
        }
        return same;
    }

    /**
     * A get method - to find the averaged mean monthly temperature 
     * 
     * @return The current value of aTemp which is the averaged mean monthly temperature the weather station
     */
    public double getAvgMeanMonthlyTemp ()
    {
        double sumTemp = 0;
        int total= 0;
        double aTemp;

        for (WeatherObservation o : observations)
        {
            double temp = o.getMMTemp();
            sumTemp += (temp == WeatherObservation.missingData) ? 0 : temp;
            total += (temp == WeatherObservation.missingData) ? 0 : 1;
        }
        if(total>0)
        {
            aTemp= sumTemp/total;
        }
        else
        {
            aTemp=WeatherObservation.missingData;
        }
        return  aTemp; 
    }

    /**
     * A get method - to find the averaged monthly rainfall
     * 
     * @return The current value of aRain which is the averaged monthly rainfall of the weather station
     */
    public double getAvgMonthlyRainFull ()
    {
        double sumRain = 0;
        int total = 0;
        double aRain;

        for (WeatherObservation o : observations)
        {
            double rain = o.getTRain();
            sumRain += (rain == WeatherObservation.missingData) ? 0 : rain;
            total += (rain == WeatherObservation.missingData) ? 0 : 1;
        }
        if(total>0)
        {
            aRain= sumRain/total;
        }
        else
        {
            aRain=WeatherObservation.missingData;
        }
        return  aRain; 
    }

    /**
     * A get method - to find the averaged monthly sunshine
     * 
     * @return The current value of aRain which is the averaged monthly sunshine of the weather station
     */
    public double getAvgMonthlySunShine ()
    {
        double sumSun = 0;
        int total = 0;
        double aSun;

        for (WeatherObservation o : observations)
        {
            double sun = o.getTSunshine();
            sumSun += (sun == WeatherObservation.missingData) ? 0 : sun;
            total += (sun == WeatherObservation.missingData) ? 0 : 1;
        }
        if(total>0)
        {
            aSun= sumSun/total;
        }
        else
        {
            aSun=WeatherObservation.missingData;
        }
        return  aSun; 
    }

    /**
     * A toString method - gives a  string representation of the station,
     * including its name and position and observation summary statistics of 
     * the averaged mean monthly temperature, the averaged monthly rainfall and the averaged monthly sunshine of the weather station.
     * 
     * @return A string representation of the averaged mean monthly temperature, the averaged monthly rainfall and the averaged monthly sunshine of the weather station.
     */
    public String toString ()
    {
        String strNewLine = System.getProperty("line.separator");
        String strAvgRainFull = (getAvgMonthlyRainFull() == WeatherObservation.missingData) ? "---" : String.format ("%.2f", getAvgMonthlyRainFull());
        String strAvgMeanTemp = (getAvgMeanMonthlyTemp() == WeatherObservation.missingData) ? "---" : String.format ("%.2f", getAvgMeanMonthlyTemp());
        String strAvgSunShine = (getAvgMonthlySunShine() == WeatherObservation.missingData) ? "---" : String.format ("%.2f", getAvgMonthlySunShine());
        return name + " station : " + strNewLine +
        " - Position : " + position.toString() + strNewLine +
        " - Average mean monthly temperature : " + strAvgMeanTemp + strNewLine +
        " - Average monthly rain fail : " + strAvgRainFull + strNewLine +
        " - Average monthly sun shine : " + strAvgSunShine;
    }
}