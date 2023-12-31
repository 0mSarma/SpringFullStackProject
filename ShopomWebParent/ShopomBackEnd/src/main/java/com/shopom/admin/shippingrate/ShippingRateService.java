package com.shopom.admin.shippingrate;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shopom.admin.paging.PagingAndSortingHelper;
import com.shopom.admin.product.ProductRepository;
import com.shopom.admin.setting.country.CountryRepository;
import com.shopom.common.entity.Country;
import com.shopom.common.entity.ShippingRate;
import com.shopom.common.entity.product.Product;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class ShippingRateService {
	public static final int RATES_PER_PAGE = 2;
	public static final int DIM_DIVISOR = 139;
	
	@Autowired private ShippingRateRepository shipRepo;
	@Autowired private CountryRepository countryRepo;
	@Autowired private ProductRepository productRepo;
	
	public void listByPage(int pageNum, PagingAndSortingHelper helper) {
		helper.listEntities(pageNum, RATES_PER_PAGE, shipRepo);
	}
	
	public  List<Country> listAllCountries(){
		return countryRepo.findAllByOrderByNameAsc();
	}
	
	public void save(ShippingRate rateInForm) throws ShippingRateAlreadyExistsException {
	    ShippingRate rateInDB = shipRepo.findByCountryAndState(rateInForm.getCountry().getId(), rateInForm.getState());

	    // Check if it's an edit operation
	    boolean isEditMode = rateInForm.getId() != null;

	    // Check if an existing rate is found for the specified country and state
	    if (isEditMode && rateInDB != null && !rateInDB.getId().equals(rateInForm.getId())) {
	        // If an existing rate is found with a different ID during edit, throw an exception
	        throw new ShippingRateAlreadyExistsException("There's already a rate for the destination " +
	                rateInForm.getCountry().getName() + ", " + rateInForm.getState());
	    }

	    // If it's a new rate or an edit operation with the same ID, proceed to save
	    shipRepo.save(rateInForm);
	}


	
	public ShippingRate get(Integer id) throws ShippingRateNotFoundException{
		try {
			return shipRepo.findById(id).get();
		} catch (NoSuchElementException ex) {
			throw new ShippingRateNotFoundException("Could not find this shipping rate with ID " + id);
		}
	}
	
	
	public void updateCODSupport(Integer id, boolean codSupported) throws ShippingRateNotFoundException{
		Long count = shipRepo.countById(id);
		if(count == null || count == 0) {
			throw new ShippingRateNotFoundException("Could not find shipping rate with ID " + id);
		}
		shipRepo.updateCODSupport(id, codSupported);

	}
	
	public void delete(Integer id) throws ShippingRateNotFoundException{
		Long count = shipRepo.countById(id);
		if(count == null || count == 0) {
			throw new ShippingRateNotFoundException("could not find this shipping rates");
		}
		shipRepo.deleteById(id);
	}
	
	public float claculateShippingCost(Integer productId, Integer countryId, String state) throws ShippingRateNotFoundException {
		ShippingRate shippingRate = shipRepo.findByCountryAndState(countryId, state);
		
		if (shippingRate == null) {
			throw new ShippingRateNotFoundException("No Shipping rate found for the given" + " destination. You have to enter Shipping cost manually.");
		}
		
		Product product = productRepo.findById(productId).get();
		
		float dimWeight = (product.getLength() * product.getWidth() * product.getHeight()) / DIM_DIVISOR;
		float finalWeight = product.getWeight() > dimWeight ? product.getWeight() : dimWeight;
		
		return finalWeight* shippingRate.getRate();
	}
	
	
}
