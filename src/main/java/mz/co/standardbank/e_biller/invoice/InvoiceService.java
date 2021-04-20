package mz.co.standardbank.e_biller.invoice;

import mz.co.standardbank.e_biller.biller.Biller;
import mz.co.standardbank.e_biller.biller.BillerService;
import mz.co.standardbank.e_biller.client.Client;
import mz.co.standardbank.e_biller.client.ClientService;
import mz.co.standardbank.e_biller.sms.Sms;
import mz.co.standardbank.e_biller.sms.SmsGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * @author - C816346 on 2020/09/16
 */
@Service
public class InvoiceService {
	private InvoiceRepository invoiceRepository;
	private BillerService billerService;
	private ClientService clientService;
	private SmsGateway smsGateway;

	@Value( "${sms.message.template}" )
	private String TEMPLATE;
	@Value( "${sms.queue.priority}" )
	private int PRIORITY;
	@Value( "$sms.message.subject" )
	private String SUBJECT;

	public InvoiceIssueDao issueInvoices ( List<InvoiceDao> invoiceDaoList ) {
		InvoiceIssueDao invoiceIssueDao = new InvoiceIssueDao();
		for ( InvoiceDao invoiceDao : invoiceDaoList ) {
			try {
				invoiceIssueDao.addToSuccessful(save(invoiceDao));
				//TODO query customer contact
				String number = "";
				smsGateway.forwardPayload(mapToSms(number , mapToMessage(invoiceDao) , mapToSubject(invoiceDao)));
			} catch ( Exception e ) {
				FailedInvoiceDao failedInvoiceDao = new FailedInvoiceDao();
				failedInvoiceDao.setInvoiceDao(invoiceDao);
				failedInvoiceDao.setMessage(e.getMessage());
				invoiceIssueDao.addToFailed(failedInvoiceDao);
			}
		}
		return invoiceIssueDao;
	}

	public Invoice acceptInvoice ( UUID id ) {
		Invoice invoice = invoiceRepository.findById(id).orElseThrow(EntityNotFoundException::new);
		if ( invoice.getInvoiceStatus() != InvoiceStatus.PENDING ) throw new IllegalArgumentException();
		//TODO call transfer
		invoice.setDatePaid(LocalDateTime.now());
		invoice.setInvoiceStatus(InvoiceStatus.ACCEPTED);
		return invoiceRepository.save(invoice);
		//TODO catch failure
	}

	public Invoice denyInvoice ( UUID id ) {
		Invoice invoice = invoiceRepository.findById(id).orElseThrow(EntityNotFoundException::new);
		if ( invoice.getInvoiceStatus() != InvoiceStatus.PENDING ) throw new IllegalArgumentException();
		invoice.setInvoiceStatus(InvoiceStatus.DENIED);
		//TODO notify biller
		return invoiceRepository.save(invoice);
	}

	public Invoice cancelInvoice ( UUID id ) {
		Invoice invoice = invoiceRepository.findById(id).orElseThrow(EntityNotFoundException::new);
		if ( invoice.getInvoiceStatus() != InvoiceStatus.PENDING ) throw new IllegalArgumentException();
		invoice.setInvoiceStatus(InvoiceStatus.CANCELLED);
		//TODO notify biller
		return invoiceRepository.save(invoice);
	}

	private Invoice save ( InvoiceDao invoiceDao ) throws EntityNotFoundException, IllegalArgumentException {
		if ( invoiceRepository.existsByBillerApiKeyAndInvoiceNumber(
				invoiceDao.getBiller() , invoiceDao.getInvoiceNumber()) )
			throw new EntityExistsException();
		Client client = clientService.find(invoiceDao.getClient());
		Biller biller = billerService.findByApiKey(invoiceDao.getBiller());
		if ( !client.hasBiller(biller) ) throw new IllegalArgumentException();
		Invoice invoice = new Invoice();
		invoice.setInvoiceNumber(invoiceDao.getInvoiceNumber());
		invoice.setClient(client);
		invoice.setBiller(biller);
		invoice.setDescription(invoiceDao.getDescription());
		invoice.setAmount(invoiceDao.getAmount());
		invoice.setDateDue(invoiceDao.getDateDue());
		invoice.setDateIssued(LocalDateTime.now());
		invoice.setInvoiceStatus(InvoiceStatus.PENDING);
		return invoice;
	}

	public Page<Invoice> findAll ( Pageable pageable ) {
		return invoiceRepository.findAll(pageable);
	}

	public Invoice findById ( UUID id ) {
		return invoiceRepository.findById(id).orElseThrow(EntityNotFoundException::new);
	}

	public Page<Invoice> findAllByBiller ( UUID apiKey , Pageable pageable ) {
		return invoiceRepository.findAllByBillerApiKey(apiKey , pageable);
	}

	public Page<Invoice> findAllByBillerAndStatus (
			UUID apiKey ,
			InvoiceStatus invoiceStatus ,
			Pageable pageable
												  ) {
		return invoiceRepository.findAllByBillerApiKeyAndInvoiceStatus(apiKey , invoiceStatus , pageable);

	}

	public Page<Invoice> findAllByClient ( String contract , Pageable pageable ) {
		return invoiceRepository.findAllByClientContract(contract , pageable);
	}

	public Page<Invoice> findAllByClientAndStatus (
			String contract ,
			InvoiceStatus invoiceStatus ,
			Pageable pageable
												  ) {
		return invoiceRepository.findAllByClientContractAndInvoiceStatus(contract , invoiceStatus , pageable);
	}

	public Page<Invoice> findAllByClientAndIssued (
			String contract ,
			LocalDateTime dateIssued ,
			Pageable pageable
												  ) {
		return invoiceRepository.findAllByClientContractAndDateDue(contract , dateIssued , pageable);
	}

	public Page<Invoice> findAllByClientAndDue ( String contract , LocalDateTime dateDue , Pageable pageable ) {
		return invoiceRepository.findAllByClientContractAndDateDue(contract , dateDue , pageable);
	}

	public Page<Invoice> findAllByClientAndBiller ( String contract , UUID apiKey , Pageable pageable ) {
		return invoiceRepository.findAllByClientContractAndBillerApiKey(contract , apiKey , pageable);
	}

	public Page<Invoice> findAllByClientAndBillerAndStatus (
			String contract ,
			UUID apiKey ,
			InvoiceStatus invoiceStatus ,
			Pageable pageable
														   ) {
		return invoiceRepository.findAllByClientContractAndBillerApiKeyAndInvoiceStatus(
				contract , apiKey , invoiceStatus , pageable);
	}

	public Page<Invoice> findAllByClientAndBillerAndIssued (
			String contract ,
			UUID apiKey ,
			LocalDateTime dateIssued ,
			Pageable pageable
														   ) {
		return invoiceRepository.findAllByClientContractAndBillerApiKeyAndDateIssued(
				contract , apiKey , dateIssued , pageable);
	}

	public Page<Invoice> findAllByClientAndBillerAndDue (
			String contract ,
			UUID apiKey ,
			LocalDateTime dateDue ,
			Pageable pageable
														) {
		return invoiceRepository.findAllByClientContractAndBillerApiKeyAndDateDue(contract , apiKey , dateDue ,
																				  pageable);
	}

	public Page<Invoice> findAllByClientAndBillerAndPaid (
			String contract ,
			UUID apiKey ,
			LocalDateTime datePaid ,
			Pageable pageable
														 ) {
		return invoiceRepository.findAllByClientContractAndBillerApiKeyAndDatePaid(
				contract , apiKey , datePaid , pageable);
	}

	//TODO format amount and due date
	//TODO maybe change this to a proper templating package for more intricate message creation
	private String mapToMessage ( InvoiceDao invoiceDao ) {
		String billerName = billerService.findByApiKey(invoiceDao.getBiller()).getName();
		return String.format(TEMPLATE , invoiceDao.getAmount() , billerName , invoiceDao.getDescription() ,
							 invoiceDao.getDateDue());
	}

	private String mapToSubject ( InvoiceDao invoiceDao ) {
		return String.format(SUBJECT , invoiceDao.getInvoiceNumber());
	}

	private Sms mapToSms ( String number , String message , String subject ) {
		Sms sms = new Sms();
		sms.setRecipient(number);
		sms.setBody(message);
		sms.setSubject(subject);
		sms.setPriority(PRIORITY);
		return sms;
	}

	@Autowired
	public void setInvoiceRepository ( InvoiceRepository invoiceRepository ) { this.invoiceRepository = invoiceRepository; }

	@Autowired
	public void setBillerService ( BillerService billerService ) { this.billerService = billerService; }

	@Autowired
	public void setClientService ( ClientService clientService ) { this.clientService = clientService; }

	@Autowired
	public void setSmsGateway ( SmsGateway smsGateway ) { this.smsGateway = smsGateway; }
}
