package processor.memorysystem;

import configuration.Configuration;
import generic.Element;
import generic.Event;
import generic.MemoryReadEvent;
import generic.MemoryResponseEvent;
import generic.MemoryWriteEvent;
import generic.Simulator;
import processor.Clock;
import processor.Processor;
public class Cache implements Element{
    CacheLine[] cacheLine;
    int numberofsets;
    boolean isCacheHit=false;
    Processor containingProcessor;
    public Cache(int cachesize, Processor containing_Processor) 
    {
        cacheLine = new CacheLine[cachesize/4];
        for (int i = 0; i < cachesize/4; i++) {
            cacheLine[i] = new CacheLine();
        }
        numberofsets = cachesize/8;
        containingProcessor=containing_Processor;
        
    }
    public void decrementcount() {
        for (int i = 0; i < numberofsets*2; i++) {
            cacheLine[i].decrementcount();
        }
    }

    public int  CacheRead(int address) {
        // System.out.println("hi in cache read function printing address unique stringdj"+address);
        int index=address%numberofsets;
        int tag=(address-(index))/numberofsets;
        
        if((cacheLine[index*2].getisvalid()&&cacheLine[index*2].gettag()==tag))
        {
            isCacheHit=true;
            cacheLine[index*2].incrementcount();
            // System.out.println("hi in cache read hit at "+index*2 +"for the address "+address+"and returned"+cacheLine[(index*2)].getdata());
            return cacheLine[(index*2)].getdata();
        }
        else if((cacheLine[(index*2)+1].getisvalid()&&cacheLine[(index*2)+1].gettag()==tag))
        {
            isCacheHit=true;
            cacheLine[(index*2)+1].incrementcount();
            // System.out.println("hi in cache read hit at "+(index*2)+1+" for the address "+address+"and returned"+cacheLine[(index*2)+1].getdata());

            return cacheLine[(index*2)+1].getdata();
        }
        else
        {
            isCacheHit=false;
            return -1;
        }
    }
    public void CacheWrite(int address, int data) {
        int index=address%numberofsets;
        int tag=(address-(index))/numberofsets;
        
        if((cacheLine[index*2].getisvalid()&&cacheLine[index*2].gettag()==tag))
        {
            isCacheHit=true;
            cacheLine[index*2].incrementcount();
            cacheLine[index*2].setdata( data);

        }
        else if((cacheLine[(index*2)+1].getisvalid()&&cacheLine[(index*2)+1].gettag()==tag))
        {
            isCacheHit=true;
            cacheLine[(index*2)+1].incrementcount();
            cacheLine[(index*2)+1].setdata(data);
        }
        else    
        {
            isCacheHit=false;
        }
    }


    public void handlemiss(boolean is_read,int address,Element previousElement,int datatobewritten)
    {
        if(is_read)
        {
            Simulator.getEventQueue().addEvent(new MemoryReadEvent(Clock.getCurrentTime()+Configuration.mainMemoryLatency,this,containingProcessor.getMainMemory(),address,true,previousElement,datatobewritten));
        }
        else
        {
            Simulator.getEventQueue().addEvent(new MemoryReadEvent(Clock.getCurrentTime()+Configuration.mainMemoryLatency,this,containingProcessor.getMainMemory(),address,false,previousElement,datatobewritten));
        }
    }
    
    public void handlememoryresponse(int address,int value) {
        int index=address%numberofsets;
        int tag=(address-(index))/numberofsets;
        
        if(!cacheLine[index*2].getisvalid())
        {
            cacheLine[index*2].settag(tag);
            cacheLine[index*2].setdata(value);
            cacheLine[index*2].setisvalid(true);
            cacheLine[index*2].setcount(7);
        }
        else if(!cacheLine[(index*2)+1].getisvalid())
        {
            cacheLine[(index*2)+1].settag( tag);
            cacheLine[(index*2)+1].setdata(value);
            cacheLine[(index*2)+1].setisvalid( true);
            cacheLine[(index*2)+1].setcount(7);
        }
        else if(cacheLine[(index*2)].getcount()>cacheLine[(index*2)+1].getcount())
        {
            cacheLine[(index*2)+1].settag(tag);
            cacheLine[(index*2)+1].setdata( value);
            cacheLine[(index*2)+1].setisvalid(true);
            cacheLine[(index*2)+1].setcount(7);
        }
        else
        {
            cacheLine[index*2].settag( tag);
            cacheLine[index*2].setdata(value);
            cacheLine[index*2].setisvalid(true);
            cacheLine[index*2].setcount(7);
        }
    }


    @Override
    public void handleEvent(Event event) {
        if(event.getEventType()==Event.EventType.MemoryRead)
        {
            MemoryReadEvent  event_mre=(MemoryReadEvent) event;
            int address=event_mre.getAddressToReadFrom();
            // System.out.println("hi in cache read hit with pc: "+address);
            int readresult=CacheRead(address);
            if(!isCacheHit)
            {
                // System.out.println("hi in cache read miss before handlemiss for the address: "+event_mre.getAddressToReadFrom());
                handlemiss(true, address,event.getRequestingElement(),0);
            }
            else
            {
                // System.out.println("hi in cache read hit "+readresult);
                Simulator.getEventQueue().addEvent(new MemoryResponseEvent(Clock.getCurrentTime(),this,event_mre.getRequestingElement(),readresult,true,null,event_mre.getAddressToReadFrom(),0));
            }
        }
        else if(event.getEventType() == Event.EventType.MemoryWrite) 
        {
            // System.out.println("hi in cache write"+event.getRequestingElement());
            MemoryWriteEvent event_mwe=(MemoryWriteEvent) event;
            int address=event_mwe.getAddressToWriteTo();
            CacheWrite(address, event_mwe.getValue());
            if(!isCacheHit)
            {
                handlemiss(false, address,event.getRequestingElement(),event_mwe.getValue());
            }
            else
            {
                
               // Simulator.getEventQueue().addEvent(new MemoryResponseEvent(Clock.getCurrentTime(),this,event_mwe.getRequestingElement(),0,false,null,event_mwe.getAddressToWriteTo(),0));
                Simulator.getEventQueue().addEvent(new MemoryWriteEvent(Clock.getCurrentTime()+Configuration.mainMemoryLatency,event_mwe.getRequestingElement(),containingProcessor.getMainMemory(),address,event_mwe.getValue()));
            }
        }
        else if(event.getEventType() == Event.EventType.MemoryResponse)
        {
            MemoryResponseEvent event_mre=(MemoryResponseEvent) event;
            handlememoryresponse(event_mre.getaddress(),event_mre.getValue());
            if(event_mre.wasRead())
            {
                // System.out.println("hi in cache read miss "+event_mre.getValue());
                Simulator.getEventQueue().addEvent(new MemoryResponseEvent(Clock.getCurrentTime(),this,event_mre.getPreviousElement(),event_mre.getValue(),true,null,event_mre.getaddress(),0));
            }
            else
            {
                
                CacheWrite(event_mre.getaddress(), event_mre.getdatatobewritten());
               // Simulator.getEventQueue().addEvent(new MemoryResponseEvent(Clock.getCurrentTime(),this,event_mre.getPreviousElement(),0,false,null,event_mre.getaddress(),0));
                Simulator.getEventQueue().addEvent(new MemoryWriteEvent(Clock.getCurrentTime()+Configuration.mainMemoryLatency,event_mre.getPreviousElement(),containingProcessor.getMainMemory(),event_mre.getaddress(),event_mre.getdatatobewritten()));
            }
        }
        
    }
}
