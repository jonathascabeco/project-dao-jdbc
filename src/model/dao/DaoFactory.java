package model.dao;

import model.dao.impl.SellerDaoJDBC;

public class DaoFactory {

	//�fabrica: classe auxiliar respons�vel por instanciar os Daos;
	// possui opera��es static para instanciar os Daos;
	
		public static SellerDao createSellerDao() {
			return new SellerDaoJDBC();
		}
		
}
