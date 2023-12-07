package prodcons.v3;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;

public class TestProdCons {

	public static void main(String[] args) {
		/* Initialisation des valeurs de variables */
		Properties properties = new Properties();
		int nProd, nCons, bufSz, prodTime, consTime, minProd, maxProd;
		try {
			properties.loadFromXML(TestProdCons.class.getClassLoader().getResourceAsStream("options.xml"));
			nProd = Integer.parseInt(properties.getProperty("nProd"));
			nCons = Integer.parseInt(properties.getProperty("nCons"));
			bufSz = Integer.parseInt(properties.getProperty("bufSz"));
			prodTime = Integer.parseInt(properties.getProperty("prodTime"));
			consTime = Integer.parseInt(properties.getProperty("consTime"));
			minProd = Integer.parseInt(properties.getProperty("minProd"));
			maxProd = Integer.parseInt(properties.getProperty("maxProd"));
		} catch (IOException | NullPointerException e) {
			System.err.println("Impossible de charger le fichier option, les valeurs par défaut vont etres prises !");
			nProd = 15;
			nCons = 10;
			bufSz = 1;
			prodTime = 10;
			consTime = 10;
			minProd = 5;
			maxProd = 10;

		}

		/* Création du buffer partagé */
		ProdConsBuffer buff = new ProdConsBuffer(bufSz);

		/* création des Consumer */
		ArrayList<Consumer> listCons = new ArrayList<>();
		Consumer c;
		for (int i = 0; i < nCons; i++) {
			c = new Consumer(consTime, buff);
			listCons.add(c);
		}

		/* création des Producers */
		ArrayList<Producter> listProd = new ArrayList<>();
		Producter p;
		for (int i = 0; i < nProd; i++) {
			p = new Producter(minProd, maxProd, prodTime, buff, String.valueOf(i));
			listProd.add(p);
		}

		/* attente des Producer */
		for (Producter pr : listProd) {
			try {
				pr.join();
			} catch (InterruptedException e) {
				System.err.println("Erreur durant attente d'un producer : " + pr.getName());
				e.printStackTrace();
			}
		}
	while(buff.nmsg() != 0) { 
		System.out.println("Messages dans le buffer : " + buff.nmsg());
		Thread.yield();
	}
	
	// NB MESSAGE PRODUIT
	System.out.println("\nNB MESSAGE PRODUIT : " + buff.totmsg());
	
	// NB MESSAGE TRAITE
	System.out.println("NB MESSAGE TRAITE : " + Consumer.nbrTraite);
	
	if(buff.totmsg() != Consumer.nbrTraite) {			
		System.err.println("SYNCHONIZATION PROBLEM");
	}else {
		System.out.println("That's all folks !!");
	} 
}


}
