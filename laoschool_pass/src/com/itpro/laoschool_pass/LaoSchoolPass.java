package com.itpro.laoschool_pass;

public class LaoSchoolPass {

	public static void main(String[] args) {
		if (args.length <=0){
			System.out.println("Please input password from command line");
			return;
		}
		String pass = args[0];
		System.out.println(">>input pass="+pass);
		
		String encryp_pass = encryptPass(pass);
		System.out.println(">>encrypted pass="+encryp_pass);

	}
	public static String encryptPass(String new_pass) {
		String saltedPass =new_pass;
		try {
			saltedPass = Password.getSaltedHash(new_pass);
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
		return saltedPass;
		
	}

}
