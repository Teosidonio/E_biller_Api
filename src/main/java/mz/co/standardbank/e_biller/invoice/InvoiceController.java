package mz.co.standardbank.e_biller.invoice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * @author - C816346 on 2020/09/16
 */
@RestController
@RequestMapping( "invoice" )
public class InvoiceController {
	private InvoiceService service;

	@PostMapping( "/issue" )
	public ResponseEntity<InvoiceIssueDao> issueInvoices ( @RequestBody List<InvoiceDao> invoiceDaoList ) {
		try {
			return new ResponseEntity<>(service.issueInvoices(invoiceDaoList) , HttpStatus.OK);
		} catch ( Exception e ) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR , e.getMessage());
		}
	}

	@PostMapping( "/{id}/accept" )
	public ResponseEntity<Invoice> acceptInvoice ( @PathVariable UUID id ) {
		try {
			return new ResponseEntity<>(service.acceptInvoice(id) , HttpStatus.OK);
		} catch ( EntityNotFoundException e ) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND , e.getMessage());
		} catch ( IllegalArgumentException e ) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST , e.getMessage());
		} catch ( Exception e ) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR , e.getMessage());
		}
	}

	@PostMapping( "/{id}/deny" )
	public ResponseEntity<Invoice> denyInvoice ( @PathVariable UUID id ) {
		try {
			return new ResponseEntity<>(service.denyInvoice(id) , HttpStatus.OK);
		} catch ( EntityNotFoundException e ) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND , e.getMessage());
		} catch ( IllegalArgumentException e ) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST , e.getMessage());
		} catch ( Exception e ) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR , e.getMessage());
		}
	}

	@PostMapping( "/{id}/cancel" )
	public ResponseEntity<Invoice> cancelInvoice ( @PathVariable UUID id ) {
		try {
			return new ResponseEntity<>(service.cancelInvoice(id) , HttpStatus.OK);
		} catch ( EntityNotFoundException e ) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND , e.getMessage());
		} catch ( IllegalArgumentException e ) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST , e.getMessage());
		} catch ( Exception e ) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR , e.getMessage());
		}
	}

	@GetMapping( "/find/all" )
	public ResponseEntity<Page<Invoice>> findAll (
			@RequestParam int page ,
			@RequestParam int size ,
			@RequestParam String direction ,
			@RequestParam String column
												 ) {
		try {
			Pageable pageable = PageRequest.of(page , size , Sort.Direction.fromString(direction) , column);
			return new ResponseEntity<>(service.findAll(pageable) , HttpStatus.OK);
		} catch ( Exception e ) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR , e.getMessage());
		}
	}

	@GetMapping( "/find/{id}" )
	public ResponseEntity<Invoice> findById ( @PathVariable UUID id ) {
		try {
			return new ResponseEntity<>(service.findById(id) , HttpStatus.OK);
		} catch ( EntityNotFoundException e ) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND , e.getMessage());
		} catch ( Exception e ) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR , e.getMessage());
		}
	}

	@GetMapping( "/find/biller/{key}" )
	public ResponseEntity<Page<Invoice>> findAllByBiller (
			@PathVariable UUID key ,
			@RequestParam int page ,
			@RequestParam int size ,
			@RequestParam String direction ,
			@RequestParam String column
														 ) {
		try {
			Pageable pageable = PageRequest.of(page , size , Sort.Direction.fromString(direction) , column);
			return new ResponseEntity<>(service.findAllByBiller(key , pageable) , HttpStatus.OK);
		} catch ( Exception e ) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR , e.getMessage());
		}
	}

	@GetMapping( "/find/biller/{key}/{status}" )
	public ResponseEntity<Page<Invoice>> findAllByBillerAndStatus (
			@PathVariable UUID key ,
			@PathVariable InvoiceStatus status ,
			@RequestParam int page ,
			@RequestParam int size ,
			@RequestParam String direction ,
			@RequestParam String column
																  ) {
		try {
			Pageable pageable = PageRequest.of(page , size , Sort.Direction.fromString(direction) , column);
			return new ResponseEntity<>(service.findAllByBillerAndStatus(key , status , pageable) , HttpStatus.OK);
		} catch ( Exception e ) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR , e.getMessage());
		}
	}

	@GetMapping( "/find/client/{contract}" )
	public ResponseEntity<Page<Invoice>> findAllByClient (
			@PathVariable String contract ,
			@RequestParam int page ,
			@RequestParam int size ,
			@RequestParam String direction ,
			@RequestParam String column
														 ) {
		try {
			Pageable pageable = PageRequest.of(page , size , Sort.Direction.fromString(direction) , column);
			return new ResponseEntity<>(service.findAllByClient(contract , pageable) , HttpStatus.OK);
		} catch ( Exception e ) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR , e.getMessage());
		}
	}

	@GetMapping( "/find/client/{contract}/{status}" )
	public ResponseEntity<Page<Invoice>> findAllByClientAndStatus (
			@PathVariable String contract ,
			@PathVariable InvoiceStatus status ,
			@RequestParam int page ,
			@RequestParam int size ,
			@RequestParam String direction ,
			@RequestParam String column
																  ) {
		try {
			Pageable pageable = PageRequest.of(page , size , Sort.Direction.fromString(direction) , column);
			return new ResponseEntity<>(service.findAllByClientAndStatus(contract , status , pageable) , HttpStatus.OK);
		} catch ( Exception e ) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR , e.getMessage());
		}
	}

	@GetMapping( "/find/client/{contract}/{issued}" )
	public ResponseEntity<Page<Invoice>> findAllByClientAndIssued (
			@PathVariable String contract ,
			@PathVariable LocalDateTime issued ,
			@RequestParam int page ,
			@RequestParam int size ,
			@RequestParam String direction ,
			@RequestParam String column
																  ) {
		try {
			Pageable pageable = PageRequest.of(page , size , Sort.Direction.fromString(direction) , column);
			return new ResponseEntity<>(service.findAllByClientAndIssued(contract , issued , pageable) , HttpStatus.OK);
		} catch ( Exception e ) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR , e.getMessage());
		}
	}

	@GetMapping( "/find/client/{contract}/{due}" )
	public ResponseEntity<Page<Invoice>> findAllByClientAndDue (
			@PathVariable String contract ,
			@PathVariable LocalDateTime due ,
			@RequestParam int page ,
			@RequestParam int size ,
			@RequestParam String direction ,
			@RequestParam String column
															   ) {
		try {
			Pageable pageable = PageRequest.of(page , size , Sort.Direction.fromString(direction) , column);
			return new ResponseEntity<>(service.findAllByClientAndDue(contract , due , pageable) , HttpStatus.OK);
		} catch ( Exception e ) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR , e.getMessage());
		}
	}

	@GetMapping( "/find/{key}/{contract}" )
	public ResponseEntity<Page<Invoice>> findAllByClientAndBiller (
			@PathVariable String contract ,
			@PathVariable UUID key ,
			@RequestParam int page ,
			@RequestParam int size ,
			@RequestParam String direction ,
			@RequestParam String column
																  ) {
		try {
			Pageable pageable = PageRequest.of(page , size , Sort.Direction.fromString(direction) , column);
			return new ResponseEntity<>(service.findAllByClientAndBiller(contract , key , pageable) , HttpStatus.OK);
		} catch ( Exception e ) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR , e.getMessage());
		}
	}

	@GetMapping( "/find/{key}/{contract}/{status}" )
	public ResponseEntity<Page<Invoice>> findAllByClientAndBillerAndStatus (
			@PathVariable String contract ,
			@PathVariable UUID key ,
			@PathVariable InvoiceStatus status ,
			@RequestParam int page ,
			@RequestParam int size ,
			@RequestParam String direction ,
			@RequestParam String column
																		   ) {
		try {
			Pageable pageable = PageRequest.of(page , size , Sort.Direction.fromString(direction) , column);
			return new ResponseEntity<>(service.findAllByClientAndBillerAndStatus(contract , key , status , pageable) ,
										HttpStatus.OK);
		} catch ( Exception e ) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR , e.getMessage());
		}
	}

	@GetMapping( "/find/{key}/{contract}/{issued}" )
	public ResponseEntity<Page<Invoice>> findAllByClientAndBillerAndIssued (
			@PathVariable String contract ,
			@PathVariable UUID key ,
			@PathVariable LocalDateTime issued ,
			@RequestParam int page ,
			@RequestParam int size ,
			@RequestParam String direction ,
			@RequestParam String column
																		   ) {
		try {
			Pageable pageable = PageRequest.of(page , size , Sort.Direction.fromString(direction) , column);
			return new ResponseEntity<>(service.findAllByClientAndBillerAndIssued(contract , key , issued , pageable) ,
										HttpStatus.OK);
		} catch ( Exception e ) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR , e.getMessage());
		}
	}

	@GetMapping( "/find/{key}/{contract}/{due}" )
	public ResponseEntity<Page<Invoice>> findAllByClientAndBillerAndDue (
			@PathVariable String contract ,
			@PathVariable UUID key ,
			@PathVariable LocalDateTime due ,
			@RequestParam int page ,
			@RequestParam int size ,
			@RequestParam String direction ,
			@RequestParam String column
																		) {
		try {
			Pageable pageable = PageRequest.of(page , size , Sort.Direction.fromString(direction) , column);
			return new ResponseEntity<>(service.findAllByClientAndBillerAndDue(contract , key , due , pageable) ,
										HttpStatus.OK);
		} catch ( Exception e ) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR , e.getMessage());
		}
	}

	@GetMapping( "/find/{key}/{contract}/{paid}" )
	public ResponseEntity<Page<Invoice>> findAllByClientAndBillerAndPaid (
			@PathVariable String contract ,
			@PathVariable UUID key ,
			@PathVariable LocalDateTime paid ,
			@RequestParam int page ,
			@RequestParam int size ,
			@RequestParam String direction ,
			@RequestParam String column
																		 ) {
		try {
			Pageable pageable = PageRequest.of(page , size , Sort.Direction.fromString(direction) , column);
			return new ResponseEntity<>(service.findAllByClientAndBillerAndPaid(contract , key , paid , pageable) ,
										HttpStatus.OK);
		} catch ( Exception e ) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR , e.getMessage());
		}
	}

	@Autowired
	public void setService ( InvoiceService service ) { this.service = service; }
}
