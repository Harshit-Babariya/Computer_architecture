package processor.memorysystem;

public class CacheLine {
    int tag;
    int data;
    boolean isvalid;
    int count;
    public CacheLine()
    {
        tag = 0;
        data = 0;
        isvalid = false;
        count = 0;
    }

    public void incrementcount()
    {
        if(this.count<7)
        {
            this.count = this.count + 1;
        }
    }

    public void decrementcount()
    {
        if(this.count>0)
        {
            this.count = this.count - 1;
        }
    }

    public void setcount(int value)
    {
        this.count = value;
    }

    public int getcount()
    {
        return count;
    }

    public void setisvalid(boolean value)
    {
        this.isvalid = value;
    }
    public boolean getisvalid()
    {
        return isvalid;
    }
    public int gettag()
    {
        return tag;
    }
    public int getdata()
    {
        return data;
    }
    public void settag(int value)
    {
        this.tag = value;
    }
    public void setdata(int value)
    {
        this.data = value;
    }

}
