
/**
 * class WeatherObservation stores information about an individual monthly weather observation
 * 
 * @author (Wendy) 
 * @version (a version number or a date)
 */
public class WeatherObservation
{
    public static final double missingData=Double.NEGATIVE_INFINITY;
    private int year;
    private int month;
    private double tMax;
    private double tMin;
    private double dayOfFrost;
    private double tRain;
    private double tSunshine;
    private boolean provisional;

    /**
     * Constructor for objects of class WeatherObservation
     * 
     * @param y New value for the WeatherObservation's year.
     * @param m New value for the WeatherObservation's month.
     */
    public WeatherObservation(int y, int m) throws InvailidDataException
    {
        year=y;
        month=m;
        tMax = missingData;
        tMin = missingData;
        dayOfFrost= missingData;
        tRain = missingData;
        tSunshine = missingData;
        provisional = false;
    }

    /**
     * Constructor for objects of WeatherObservation
     * 
     * @param s New value for the observation . 
     * 
     */
    public WeatherObservation(String s) throws InvailidDataException
    {
        String[] splitString = s.trim().split("[\\s*#]+");
        try {
            this.year = Integer.parseInt (splitString[0]);
            setMonth (Integer.parseInt (splitString[1]));
            this.tMax = missingData;
            this.tMin = missingData;
            this.dayOfFrost = missingData;
            this.tRain = missingData;
            this.tSunshine = missingData;
            this.provisional = false;
            if (splitString[2].equals ("---") == false) {
                setTMax (Double.parseDouble (splitString[2]));
            }
            if (splitString[3].equals ("---") == false) {
                setTMin (Double.parseDouble (splitString[3]));
            }
            if (splitString[4].equals ("---") == false) {
                setDayOfFrost (Integer.parseInt (splitString[4]));
            }
            if (splitString[5].equals ("---") == false) {
                setTRain (Double.parseDouble (splitString[5]));
            }
            if (splitString[6].equals ("---") == false) {
                setTSunshine (Double.parseDouble (splitString[6]));
            }
        } catch (InvailidDataException e) {
            throw new InvailidDataException ("Data is not complete");
        } catch (NumberFormatException e) {
            throw new InvailidDataException ("Digital number format error.");
        }
        this.provisional = (splitString.length > 7 && splitString[7].equals ("Provisional")) ? true : false;
    }

    /**
     * A get method - year of the observation.
     * 
     * @return The current value of the observation's year.
     */
    public int getYear(){
        return year;
    }

    /**
     * A get method - month of the observation.
     * 
     * @return The current value of the observation's month.
     */
    public int getMonth(){
        return month;
    }

    /**
     * A get method -  mean daily maximum temperature in celsius of the observation.
     * 
     * @return The current value of the observation's mean daily maximum temperature(e.g.6.3) in celsius).
     */
    public double getTMax(){
        return tMax;
    }

    /**
     * A get method - mean daily minimum temperature in celsius of the observation.
     * 
     * @return The current value of the observation's mean daily minimum temperature(e.g.0.7) in celsius).
     */
    public double getTMin(){
        return tMin;
    }

    /**
     * A get method -  number of days of air frost in mm of the observation.
     * 
     * @return The current value of the observation's number of days of air frost(e.g. 258.2) in mm.
     */
    public double getDayOfFrost(){
        return dayOfFrost;
    }

    /**
     * A get method - the observation's total rainfall in mm of the observation.
     * 
     * @return The current value of the observation's total rainfall(e.g. 258.2) in mm).
     */
    public double getTRain(){
        return tRain;
    }

    /**
     * A get method - the observation's total hours of sunshine of the observation.
     * 
     * @return The current value of the observation's total hours of sunshine(e.g.42.0)).
     */
    public double getTSunshine(){
        return tSunshine;
    }

    /**
     * A set method - year of the observation.
     * 
     * @param year New value for the observation's year.
     */
    public void setYear(int year){

        this.year=year;
    }

    /**
     * A set method - month of the observation.
     * 
     * @param month New value for the observation's month.
     */
    public void setMonth(int month) throws InvailidDataException { 
        if ( month > 12 || month < 1) {
            throw new InvailidDataException ("Month is not vailed");
        }
        this.month=month;
    }

    /**
     * A set method -  mean daily maximum temperature in celsius of the observation.
     * 
     * @param tMax New value for the observation's mean daily maximum temperature in celsius.
     */
    public void setTMax(double tMax)throws InvailidDataException{
        if ( tMax < -273.15 ) {
            throw new  InvailidDataException ("Mean daily maximum temperature must > -273.15 (absolute zero)");   
        }
        this.tMax=tMax;
    }

    /**
     * A set method -  mean daily minimum temperature in celsius of the observation.
     * 
     * @param tMin New value for the observation's mean daily minimum temperature in celsius.
     */
    public void setTMin(double tMin)throws InvailidDataException{
        if ( tMin < -273.15 ) {
            throw new InvailidDataException ("Mean daily minimum temperature must > -273.15 (absolute zero)"); 
        }
        this.tMin=tMin;
    }

    /**
     * A set method -   number of days of air frost in mm of the observation.
     * 
     * @param day New value for the observation's number of days of air frost in mm.
     */
    public void setDayOfFrost(double day) throws InvailidDataException{
        if (day < 0) {
            throw new  InvailidDataException ("Number of days of air frost must > 0");   
        }
        dayOfFrost=day;
    }

    /**
     * A set method -   the observation's total rainfall in mm of the observation.
     * 
     * @param tRain New value for the observation's  the observation's total rainfall in mm.
     */
    public void setTRain(double tRain)throws InvailidDataException{
        if (tRain < 0) {
            throw new InvailidDataException ("Total hours of sunshine must > 0");   
        }
        this.tRain=tRain;
    }

    /**
     * A set method -  mean daily minimum temperature in celsius of the observation.
     * 
     * @param tSunshine New value for the observation's mean daily minimum temperature in celsius.
     */
    public void setTSunshine(double tSunshine)throws InvailidDataException {
        this.tSunshine=tSunshine;
    }

    /**
     * A set method - to indicate whether or not the observation is provisional.
     * 
     * @param isP New value for indicating whether or not the observation is provisional .
     */
    public void setProvisional (boolean isP)
    {
        provisional = isP;    
    }

    /**
     * A get method - to find the monthly mean temperature, calculated from the average of the mean daily maximum and mean daily munimum temperature.
     * 
     * @return The current value of the observation's monthly mean temperature.
     */
    public double getMMTemp(){

        double monthlymtemp= Math.round((tMax+tMin)/2);
        return monthlymtemp;

    }

    /**
     * A toSting method- returns a string representation that formats the data in exactly the same way taht the MetOffice files record the data.
     * 
     * @return A string representation  that formats the data in exactly the same way taht the MetOffice files record the data.
     */ 
    public String toString(){
        String strTMax, strTMin, strForst, strRain, strSunshine;
        String NullString = "    --- ";
        strTMax = (this.tMax == missingData) ? NullString : String.format ("%8.1f", tMax);
        strTMin = (this.tMin ==  missingData) ? NullString : String.format ("%8.1f", tMin);
        strForst = (this. dayOfFrost==  missingData) ? NullString : String.format ("%8d", dayOfFrost);
        strRain = (this.tRain ==  missingData) ? NullString : String.format ("%8.1f", tRain);
        strSunshine = (this.tSunshine==  missingData) ? NullString : String.format ("%8.1f", tSunshine);
        return String.format ("%4d  %2d", this.year, this.month) + strTMax + strTMin + strForst + strRain + strSunshine;

    }

    /**
     * A completion method - to find data completion percentage of the obsevation.
     * 
     * @return The current value of the observation's data completion percentage.
     */
    public double completion ()
    {
        double valid = 0;
        valid = (this.tMax == missingData) ? valid : valid + 1;
        valid = (this.tMin == missingData) ? valid : valid + 1;
        valid = (this.dayOfFrost == missingData) ? valid : valid + 1;
        valid = (this.tRain == missingData) ? valid : valid + 1;
        valid = (this.tSunshine == missingData) ? valid : valid + 1;
        return (valid / 5) * 100;
    }

    /**
     * A euqals method - The method determines whether the observation that invokes the method is equal to the observation that is passed as argument.
     * 
     * @return The methods returns True if the argument is not null and is an object of the same type and with the same numeric value.
     */
    public boolean equals (Object o)
    {
        boolean same = false;
        if (o != null && o instanceof WeatherObservation)
        {
            same = ((this.year == ((WeatherObservation) o).year) && (this.month == ((WeatherObservation) o).month));
        }
        return same;
    }

    /**
     * A compareTo method - The method compares the Number object that invoked the method to the argument.
     * 
     * @return If the Integer is equal to the argument then 0 is returned.
     * @return If the Integer is less than the argument then -1 is returned.
     * @return If the Integer is greater than the argument then 1 is returned.
     */
    public int compareTo (WeatherObservation o)
    {
        if (year == o.getYear()) {
            if (month == o.getMonth()) {
                return 0;
            }
            return (month > o.getMonth()) ? 1 : -1;
        } 
        return (year > o.getYear()) ? 1 : -1;
    }

}