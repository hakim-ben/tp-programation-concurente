package prodcons.v6;

public class Message {
    String message; 
    private int OccurInBuffer;
    public Message(String message){
        this.message = message; 
		this.OccurInBuffer = 0;
    }  
    public void addcopy(){ 
      this.OccurInBuffer++;
    }
    public void removecopy(){ 
        if(OccurInBuffer>0)
    this.OccurInBuffer--;
    }  
    public int getOccurInBuffer() {
		return OccurInBuffer;
	}



}