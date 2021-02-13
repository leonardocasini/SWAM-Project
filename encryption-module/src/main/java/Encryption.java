import simmetric.SimmetricCryptography;

public class Encryption {

    public static void main(String[] args) {
        // TODO Auto-generated method stub

		String defaultpassword = "defaultpassword";
		String prova = "prova";


		SimmetricCryptography c = new SimmetricCryptography(defaultpassword);
		String cryptedProva = c.getEncriptedText(prova);

		System.out.println(cryptedProva);
        System.out.println("Funziona");


    }

}
