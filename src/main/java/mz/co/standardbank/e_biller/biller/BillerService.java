package mz.co.standardbank.e_biller.biller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.UUID;

/**
 * @author - C816346 on 2020/09/16
 */
@Service
public class BillerService {
	private BillerRepository repository;


	public Page<Biller> findAll ( Pageable pageable ) { return repository.findAll(pageable); }

	public Biller findByName ( String name ) {
		return repository.findByName(name).orElseThrow(EntityNotFoundException::new);
	}

	public Biller findByApiKey ( UUID apiKey ) {
		return repository.findById(apiKey).orElseThrow(EntityNotFoundException::new);
	}

	public Biller save ( BillerDao billerDao ) { return repository.save(mapToBiller(billerDao)); }

	public void delete ( UUID apiKey ) { repository.deleteById(apiKey); }

	private Biller mapToBiller ( BillerDao billerDao ) {
		Biller biller = new Biller();
		biller.setContract(billerDao.getContract());
		biller.setName(billerDao.getName());
		biller.setUrl(biller.getUrl());
		return biller;
	}

	@Autowired
	public void setBillerRepository ( BillerRepository repository ) {
		this.repository = repository;
	}
}
