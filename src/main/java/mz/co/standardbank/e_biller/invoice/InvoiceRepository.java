package mz.co.standardbank.e_biller.invoice;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * @author - C816346 on 2020/09/16
 */
@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, UUID> {
	boolean existsByBillerApiKeyAndInvoiceNumber ( UUID apiKey , String invoiceNumber );

	Invoice findByInvoiceNumber ( String invoiceNumber );

	Page<Invoice> findAllByBillerApiKey ( UUID apiKey , Pageable pageable );

	Page<Invoice> findAllByBillerApiKeyAndInvoiceStatus (
			UUID apiKey ,
			InvoiceStatus invoiceStatus ,
			Pageable pageable
														);

	Page<Invoice> findAllByClientContract ( String contract , Pageable pageable );

	Page<Invoice> findAllByClientContractAndInvoiceStatus (
			String contract ,
			InvoiceStatus invoiceStatus ,
			Pageable pageable
														  );

	Page<Invoice> findAllByClientContractAndDateIssued (
			String contract ,
			LocalDateTime dateIssued ,
			Pageable pageable
													   );

	Page<Invoice> findAllByClientContractAndDateDue ( String contract , LocalDateTime dateDue , Pageable pageable );

	Page<Invoice> findAllByClientContractAndBillerApiKey ( String contract , UUID apiKey , Pageable pageable );

	Page<Invoice> findAllByClientContractAndBillerApiKeyAndInvoiceStatus (
			String contract ,
			UUID apiKey ,
			InvoiceStatus invoiceStatus ,
			Pageable pageable
																		 );

	Page<Invoice> findAllByClientContractAndBillerApiKeyAndDateIssued (
			String contract ,
			UUID apiKey ,
			LocalDateTime dateIssued ,
			Pageable pageable
																	  );

	Page<Invoice> findAllByClientContractAndBillerApiKeyAndDateDue (
			String contract ,
			UUID apiKey ,
			LocalDateTime dateDue ,
			Pageable pageable
																   );

	Page<Invoice> findAllByClientContractAndBillerApiKeyAndDatePaid (
			String contract ,
			UUID apiKey ,
			LocalDateTime datePaid ,
			Pageable pageable
																	);

}
