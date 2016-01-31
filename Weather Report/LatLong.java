/**
 *  class LatLong represents a geographical position, based on latitude and longitude.
 * 
 * @author (Wendy) 
 * @version (11/11/2015)
 */
public class LatLong
{
    private int lat;
    private int lon;

    /**
     * Constructor for objects of class LatLong
     * 
     * @param lon New value for the position's longitude.
     * @param lat New value for the position's latitude.
     */
    public LatLong(int lon, int lat)
    {
        this.lat=lat;
        this.lon=lon;
    }

    /**
     * Constructor for objects of class LatLong
     * 
     * @param position New value for the position. 
     * The format must be "####E ####N" (# is a decimal number) i.e. in the form 1234E 5678N.
     */
    public LatLong(String position) throws InvailidDataException
    {
        try{
            String[] pArray= position.split("\\s +");
            String slat= pArray[1].substring(0,pArray[1].length()-1);
            String slon= pArray[0].substring(0,pArray[0].length()-1);
            String sllat= pArray[1].substring(pArray[1].length()-1,pArray[1].length());
            String sllon= pArray[0].substring(pArray[0].length()-1,pArray[0].length());

            if(sllon.equals("E"))
            {
                this.lon= Integer.parseInt(slon);
            }
            else if(sllon.equals("W"))
            {
                this.lon= -Integer.parseInt(slon);
            }
            else
            {
                throw new InvailidDataException("LatLong : Longitude format error.");
            }

            if (sllat.equals("N"))
            {
                this.lat=Integer.parseInt(slat);
            }
            else if(sllat.equals("S")){
                this.lat=- Integer.parseInt(slat);
            }
            else{
                throw new InvailidDataException("LatLong : Latitude format error.");
            }
        }

        catch(StringIndexOutOfBoundsException e)
        {
            throw new InvailidDataException("LatLong : String format error.") ;
        }
        catch(NumberFormatException e) 
        {
            throw new InvailidDataException("LatLong : Digital number format error.") ;
        }
    }

    /**
     * A set method - Latitude of the position.
     * 
     * @param lat New value for this position's latitude.
     */
    public void setLat(int lat)
    {
        this.lat=lat;
    }

    /**
     * A set method - Longitude of the position.
     * 
     * @param lon New value for the position's longitude.
     */
    public void setLon(int lon)
    {
        this.lon=lon;
    }

    /**
     * A get method - Latitude of the position.
     * 
     * @return The current value of the position's latitude.
     */  
    public double getLat()
    {
        return lat;
    }

    /**
     * A get method - Longitude of the position.
     * 
     * @return The current value of the position's longitude.
     */ 
    public double getlon()
    {
        return lon;
    }

    /**
     * A toString method- returns a string representation of the LatLong's position.
     * 
     * @return A string representation of the LatLong's position in the UK grid system. i.e. in the form 1234E 5678N
     */ 
    public String toString()
    {
        int ilat;
        int ilon;
        String slat;
        String slon;
        ilat= (Math.abs(this.lat));
        ilon= (Math.abs(this.lon));
        slat=(this.lat > 0) ? "N" : "S";
        slon=(this.lon > 0) ? "E" : "W";
        return ilon + slon+" "+ ilat + slat;
    }
}