package model.dao;

import model.dao.impl.SellerDaoJDBC;

public class DaoFactory {

	//´fabrica: classe auxiliar responsável por instanciar os Daos;
	// possui operações static para instanciar os Daos;
	
		public static SellerDao createSellerDao() {
			return new SellerDaoJDBC();
		}
		
}
