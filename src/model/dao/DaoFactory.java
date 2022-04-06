package model.dao;

import db.DB;
import model.dao.impl.DepartmentDaoJDBC;
import model.dao.impl.SellerDaoJDBC;

public class DaoFactory {

	//�fabrica: classe auxiliar respons�vel por instanciar os Daos;
	// possui opera��es static para instanciar os Daos;
	
		public static SellerDao createSellerDao() {
			return new SellerDaoJDBC(DB.getConnection());
		}
		
		public static DepartmentDao createDepartmentDao() {
			return new DepartmentDaoJDBC(DB.getConnection());
		}
}
