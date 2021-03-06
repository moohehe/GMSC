package com.basicsqledu.www.dao;

import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.basicsqledu.www.mapper.CertMapper;
import com.basicsqledu.www.vo.Certification;

@Repository
public class CertDAO
{
	@Autowired
	SqlSession session;

	private final static Logger logger = LoggerFactory.getLogger(CertDAO.class);

	public int insertCert(Certification certify){
		int i = 0;
		CertMapper mapper = null;
		try
		{
			mapper = session.getMapper(CertMapper.class);
			i = mapper.insertCert(certify);
		} catch (Exception e)
		{
			e.printStackTrace();
		}

		return i;
	}
	public Certification selectCert(Certification cert){
		logger.info("start in CertiDAO selectCert(cert):{}",cert);
		Certification sel_cert = null;
		CertMapper mapper = null;
		try
		{
			mapper = session.getMapper(CertMapper.class);
			sel_cert = mapper.selectCert(cert);
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		logger.info("end of CertiDAO selectCert(cert):{}",cert);
		return sel_cert;
	}
	public Certification searchCert(String text) {
		Certification cert = null;
		CertMapper mapper = null;
		try
		{
			mapper = session.getMapper(CertMapper.class);
			cert = mapper.searchCert(text);
		} catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
		return cert;
	}

}
