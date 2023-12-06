package prodcons.v1;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.InvalidPropertiesFormatException;
import java.util.Properties;

public class TestProdCons {
	static int nProd, nCons, bufSz, prodTime, consTime, minProd, maxProd;
	
	static void loadData() throws InvalidPropertiesFormatException, IOException{
		Properties propreties = new Properties();
		propreties.loadFromXML(new FileInputStream("options.xml"));
		propreties.loadFromXML(TestProdCons.class.getClassLoader().getResourceAsStream("options.xml"));
		nProd = Integer.parseInt(propreties.getProperty("nProd"));
		nCons = Integer.parseInt(propreties.getProperty("nCons"));
		bufSz = Integer.parseInt(propreties.getProperty("bufSz"));
		prodTime = Integer.parseInt(propreties.getProperty("prodTime"));
		consTime = Integer.parseInt(propreties.getProperty("consTime"));
		minProd = Integer.parseInt(propreties.getProperty("minProd"));
		maxProd = Integer.parseInt(propreties.getProperty("maxProd"));
	}
	public static void main(String[] args) throws InvalidPropertiesFormatException, IOException, InterruptedException{
		loadData();
		ProdConsBuffer pcbuffer = new ProdConsBuffer(bufSz);
		Message msg1 = new Message("test1");
		
		Producter[] prods = new Producter[nProd];
		Consumer[] cons = new Consumer[nCons];
		
		for(int i = 0; i<nProd; i++)
			prods[i] = new Producter(pcbuffer, msg1);
		
		for(int i = 0; i<nCons; i++)
			cons[i] = new Consumer(pcbuffer);
		
		for(int i = 0; i<prods.length; i++)
				prods[i].join();
		for(int i = 0; i<cons.length; i++)
			cons[i].join();
	}

}