
package acme.features.administrator.aircraft;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Administrator;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.aircraft.Aircraft;
import acme.entities.aircraft.ServiceStatus;
import acme.entities.airline.Airline;

@GuiService
public class AircraftUpdateService extends AbstractGuiService<Administrator, Aircraft> {

	@Autowired
	private AircraftRepository repository;


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}
	@Override
	public void load() {
		Aircraft aircraft;
		int id = super.getRequest().getData("id", int.class);
		aircraft = this.repository.findAircraftById(id);
		super.getBuffer().addData(aircraft);
	}

	@Override
	public void bind(final Aircraft aircraft) {
		super.bindObject(aircraft, "model", "registrationNumber", "capacity", "cargoWeight", "status", "details", "airline");
	}

	@Override
	public void validate(final Aircraft aircraft) {
		boolean confirmation;

		confirmation = super.getRequest().getData("confirmation", boolean.class);
		super.state(confirmation, "confirmation", "acme.validation.confirmation.message");
		super.state(aircraft.getCargoWeight() >= 2000 && aircraft.getCargoWeight() <= 50000, "cargoWeight", "acme.validation.cargoWeight.message");
		super.state(aircraft.getCapacity() > 1 && aircraft.getCapacity() < 255, "capacity", "acme.validation.capacity.message");
		Aircraft existing = this.repository.findAircraftByNumber(aircraft.getRegistrationNumber());
		boolean valid = existing == null || existing.getId() == aircraft.getId();
		super.state(valid, "registrationNumber", "administrator.aircraft.form.error.duplicateRegistrationNumber");
	}

	@Override
	public void perform(final Aircraft aircraft) {
		this.repository.save(aircraft);
	}

	@Override
	public void unbind(final Aircraft aircraft) {

		Dataset dataset;
		SelectChoices choices;
		SelectChoices selectedAirlines;
		Collection<Airline> airlines;

		choices = SelectChoices.from(ServiceStatus.class, aircraft.getStatus());
		airlines = this.repository.findAllAirlines();
		selectedAirlines = SelectChoices.from(airlines, "name", aircraft.getAirline());

		dataset = super.unbindObject(aircraft, "model", "registrationNumber", "capacity", "cargoWeight", "status", "details", "airline");

		dataset.put("statuses", choices);

		dataset.put("airline", selectedAirlines.getSelected().getKey());
		dataset.put("airlines", selectedAirlines);

		super.getResponse().addData(dataset);

	}

}
